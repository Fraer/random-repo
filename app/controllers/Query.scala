package controllers

import javax.inject.{Inject, Singleton}

import _root_.util.Logging
import dao.Dao
import play.api.libs.json._
import play.api.mvc.{Action, Controller}

/** main controller */
@Singleton
class Query @Inject()(val dao: Dao,
                      val webJarAssets: WebJarAssets)
  extends Controller with Logging {

//  def findCountryByName(countryCode: String) = Action(parse.empty) { req =>
//    Ok(Json.toJson(dao.airportsByCountryCode(countryCode, 0, 10)))
//  }

  def findCountryByCode(countryCode: String, page: Int, pageSize:Int) = Action(parse.empty) { req =>
    val res = dao.airportsByCountryCode(countryCode, page, pageSize)
    Ok(Json.obj(
      "items" -> res.items,
      "from" -> (res.offset+1),
      "to" -> (res.offset + res.items.size),
      "total" -> res.total
    ))
  }
}
