package controllers

import javax.inject.{Inject, Singleton}

import _root_.util.Logging
import dao.Dao
import play.api.libs.json._
import play.api.mvc.{Action, Controller}

@Singleton
class Report @Inject()(val dao: Dao)
  extends Controller with Logging {

  def highestAirports = Action(parse.empty) { req =>
    Ok(Json.toJson(
      dao.highestAirports().map{ x =>
        Json.obj("name" -> x._1, "count" -> x._2)
      }))
  }

  def lowestAirports = Action(parse.empty) { req =>
    Ok(Json.toJson(
      dao.lowestAirports().map{ x =>
        Json.obj("name" -> x._1, "count" -> x._2)
      }))
  }

  def countries() = Action(parse.empty) { req =>
    Ok(Json.toJson(dao.countries()))
  }

  def surfaceTypesPerCountry(countryCode: String) = Action(parse.empty) { req =>
    if (countryCode.length != 2)
      BadRequest("Invalid country code")
    else
      Ok(Json.toJson(dao.surfaceTypesByCountry(countryCode)))
  }

  def mostCommonRunwayLatitude = Action(parse.empty) { req =>
    Ok(Json.obj("a"-> "b"))
  }
}
