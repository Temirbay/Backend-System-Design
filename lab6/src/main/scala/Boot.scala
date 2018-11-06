
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import model.Waybill
import response.Response
import service.WaybillService
import akka.pattern.ask
import akka.util.Timeout
import serialization.JsonSupport
import slick.lifted.TableQuery

import scala.concurrent.{Await, ExecutionContextExecutor}
import scala.concurrent.duration._
import scala.io.StdIn
import slick.jdbc.MySQLProfile.api._
import table.Waybills



object Boot extends App with JsonSupport {

  implicit val system: ActorSystem             = ActorSystem("lab6-system")
  implicit val materializer: ActorMaterializer = ActorMaterializer()

  // needed for the future map/flatmap in the end and future in fetchItem and saveOrder
  implicit val executionContext: ExecutionContextExecutor = system.dispatcher

  // needed fot akka's ASK pattern
  implicit val timeout: Timeout = Timeout(60.seconds)

  val waybillService = system.actorOf(WaybillService.props(), "waybill-service")


  val route: Route = {
    concat(
      path("waybills") {
        pathEndOrSingleSlash {
          concat(
            get {
              parameters("hasStatus".?, "moreWeight".?, "moreCost".?, "moreAmount".?
                , "moreVolume".?) {
                (status, weight, cost, amount, volume) =>
                  complete {
                    (waybillService ? WaybillService.GetWaybills(
                      status,
                      weight.map(_.toDouble),
                      cost.map(_.toDouble),
                      amount.map(_.toInt),
                      volume.map(_.toDouble)
                    )).mapTo[List[Waybill]]
                  }
              }
            })
        }
      },
      path("waybill") {
        pathEndOrSingleSlash {
          concat(
            post {
              decodeRequest {
                entity(as[Waybill]) { waybill =>
                  complete {
                    (waybillService ? WaybillService.CreateWaybill(waybill))
                      .mapTo[Either[Response.Error, Response.Accepted]]
                  }
                }
              }
            },
            put {
              decodeRequest {
                entity(as[Waybill]) { waybill =>
                  complete {
                    (waybillService ? WaybillService.UpdateWaybill(waybill))
                      .mapTo[Either[Response.Error, Waybill]]
                  }
                }
              }
            }
          )
        }
      },
      pathPrefix("waybill" / IntNumber) { waybillId =>
        pathEndOrSingleSlash {
          concat(
            get {
              complete {
                (waybillService ? WaybillService.GetWaybill(waybillId))
                  .mapTo[Either[Response.Error, Waybill]]
              }
            },
            delete {
              complete {
                (waybillService ? WaybillService.RemoveWaybill(waybillId))
                  .mapTo[Either[Response.Error, Response.Accepted]]
              }
            }
          )
        }
      }
    )
  }

  val bindingFuture = Http().bindAndHandle(route, "localhost", 8081)
  println(s"Server online at http://localhost:8081/\nPress RETURN to stop...")
  StdIn.readLine() // let it run until user presses return
  bindingFuture
    .flatMap(_.unbind()) // trigger unbinding from the port
    .onComplete(_ ⇒ system.terminate()) // and shutdown when done
}
