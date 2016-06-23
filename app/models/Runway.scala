package models

case class Runway(id: Int, length: Int, width: Int, surface: String)

object Runway {

  import play.api.libs.json._
  import play.api.libs.functional.syntax._

  implicit val RunwayToJson: Writes[Runway] = (
    (__ \ "id").write[Int] ~
      (__ \ "length").write[Int] ~
      (__ \ "width").write[Int] ~
      (__ \ "surface").write[String]
    ) ((runway: Runway) => (
    runway.id,
    runway.length,
    runway.width,
    runway.surface
    ))
}
