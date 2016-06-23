package models

case class Airport(id: Int, name: String, runways: Seq[Runway])

object Airport {

  import play.api.libs.json._
  import play.api.libs.functional.syntax._

  implicit val AirportToJson: Writes[Airport] = (
    (__ \ "id").write[Int] ~
      (__ \ "name").write[String] ~
      (__ \ "runways").write[Seq[Runway]]
    ) ((airport: Airport) => (
    airport.id,
    airport.name,
    airport.runways
    ))
}
