
package response
/**
  * A common trait for all possible responses from a service
  */
sealed trait Response {
  def message: String
  def status: Int
}

object Response {
  case class Accepted(message: String = "OK", status: Int = 200) extends Response

  case class Error(message: String, status: Int = 500) extends Response
}
