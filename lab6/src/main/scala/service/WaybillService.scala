package service

import akka.actor.{Actor, ActorLogging, Props}
import model.Waybill
import response.Response
import slick.lifted.TableQuery
import table.Waybills

import scala.concurrent.Await
import scala.concurrent.duration.Duration
import slick.jdbc.MySQLProfile.api._

import scala.concurrent.ExecutionContext.Implicits.global

object WaybillService {
  sealed trait WaybillRequest
  case class GetWaybill(id: Int)             extends WaybillRequest
  case class GetWaybills(status :Option[String],
                         weight : Option[Double],
                         cost : Option[Double],
                         amount : Option[Int],
                         volume : Option[Double])
                                            extends WaybillRequest
  case class CreateWaybill(waybill: Waybill) extends WaybillRequest
  case class UpdateWaybill(waybill: Waybill) extends WaybillRequest
  case class RemoveWaybill(id: Int)          extends WaybillRequest

  def props() = Props(new WaybillService)
}

class WaybillService extends Actor with ActorLogging {
  import WaybillService._

  // A map where for (key -> value) key is student ID and value is Student structure itself.
  // Notice that students is mutable. However, Map is immutable data structure.
  // Ex: students = students + ("id-1" -> Student("id-1", fullname))

  val dbWaybills = TableQuery[Waybills]
  val db = Database.forConfig("mysql")

  override def receive: Receive = {

    case GetWaybills(status, weight, cost, amount, volume) =>
      var waybills = Await.result(db.run(
        dbWaybills.result
      ), Duration.Inf).toList

      status match {
        case Some(value)=>
          waybills = waybills.filter(_.status == value)
        case None =>
      }

      weight match {
        case Some(value)=>
          waybills = waybills.filter(_.weight > value)
        case None =>
      }

      cost match {
        case Some(value)=>
          waybills = waybills.filter(_.cost > value)
        case None =>
      }

      amount match {
        case Some(value)=>
          waybills = waybills.filter(_.amount > value)
        case None =>
      }

      volume match {
        case Some(value)=>
          waybills = waybills.filter(_.volume > value)
        case None =>
      }

      sender() ! waybills


    case GetWaybill(id) =>
      val waybill = Await.result(db.run(
        dbWaybills.filter(_.id === id).result
      ), Duration.Inf).toList

      if (waybill.nonEmpty) {
        sender() !  Right(waybill.head)
      }
      else {
        sender() ! Left(Response.Error (s"Could not find waybill with id: $id"))
      }



    case CreateWaybill(item) =>
      val waybill = Await.result(db.run(
        dbWaybills.filter(_.id === item.id).result
      ), Duration.Inf).toList

      if (waybill.nonEmpty) {
        sender() ! Left(Response.Error (s"Waybill with id: ${item.id} already exists"))
      } else {
        Await.result(db.run(
          dbWaybills += item
        ), Duration.Inf)
        sender() ! Right(Response.Accepted())
      }




    case UpdateWaybill(updateWaybill) =>
      val waybill = Await.result(db.run(
        dbWaybills.filter(_.id === updateWaybill.id).result
      ), Duration.Inf).toList

      if (waybill.isEmpty) {
        sender() ! Left(Response.Error (s"Waybill with id: ${updateWaybill.id} doesn't exists"))
      } else {
        Await.result(db.run(
          dbWaybills.filter(_.id === updateWaybill.id).update(updateWaybill)
        ), Duration.Inf)
        sender() ! Right(updateWaybill)
      }




    case RemoveWaybill(id) =>
      val waybill = Await.result(db.run(
        dbWaybills.filter(_.id === id).result
      ), Duration.Inf).toList

      if (waybill.isEmpty) {
        sender() ! Left(Response.Error (s"Waybill with id: $id doesn't exists"))
      } else {
        Await.result(db.run(
          dbWaybills.filter(_.id === id).delete
        ), Duration.Inf)
        sender() ! Right(Response.Accepted ())
      }

  }
}
