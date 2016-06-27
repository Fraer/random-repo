package controllers

import javax.inject.{Inject, Singleton}

import util.Logging
import dao.Dao
import play.api.libs.json._
import play.api.mvc.{Action, Controller}

@Singleton
class Query @Inject()(val dao: Dao)
  extends Controller with Logging {

  def similarCountries(input: String) = Action(parse.empty) { req =>
    Ok(Json.toJson(dao.similarCountries(input)))
  }

  /** Returns a json array that represents a page of airports by country code */
  def airportsByCountryCode(countryCode: String, page: Int, pageSize:Int) = Action(parse.empty) { req =>
    if (countryCode.length != 2)
      BadRequest("Invalid country code")
    else {
      val res = dao.airportsByCountryCode(countryCode, page, pageSize)
      Ok(Json.obj(
        "items" -> res.items,
        "from" -> (res.offset + 1),
        "to" -> (res.offset + res.items.size),
        "total" -> res.total
      ))
    }
  }
}
