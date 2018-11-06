package table


import slick.jdbc.MySQLProfile.api._
import model.Waybill
import slick.lifted.Tag

class Waybills(tag : Tag) extends Table[Waybill](tag, "waybills"){

  def id = column[Int]("id", O.PrimaryKey)
  def status = column[String]("status")
  def weight  = column[Double]("weight")
  def cost  = column[Double]("cost")
  def amount = column[Int]("amount")
  def volume = column[Double]("volume")

  def * =
    (
      id,
      status,
      weight,
      cost,
      amount,
      volume
    ) <> ((Waybill.apply _).tupled, Waybill.unapply)

}
