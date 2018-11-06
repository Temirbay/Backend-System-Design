package serialization

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import model.Waybill
import response.Response
import spray.json.{DefaultJsonProtocol, RootJsonFormat}

/**
  * Adds JSON support for models and reponses. Akka uses these formats when serializing/deserializing JSON.
  */
trait JsonSupport extends SprayJsonSupport with DefaultJsonProtocol {

  //models
  implicit val waybillFormat: RootJsonFormat[Waybill] = jsonFormat6(Waybill)

  // responses
  implicit val acceptedFormat: RootJsonFormat[Response.Accepted] = jsonFormat2(Response.Accepted)
  implicit val errorFormat: RootJsonFormat[Response.Error]       = jsonFormat2(Response.Error)
}
