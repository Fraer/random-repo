package models

case class Country(code: String, name: String)

object Country {

  import play.api.libs.json._
  import play.api.libs.functional.syntax._

  implicit val CountryToJson: Writes[Country] = (
    (__ \ "code").write[String] ~
      (__ \ "name").write[String]
    ) ((country: Country) => (
    country.code,
    country.name
    ))
}
