package controllers

import javax.inject.{Inject, Singleton}

import _root_.util.Logging
import dao.Dao
import play.api.mvc.{Action, Controller}

/** main controller */
@Singleton
class Application @Inject()(val webJarAssets: WebJarAssets)
  extends Controller with Logging {

  def index = Action(parse.empty) { req =>
    Ok(views.html.index(webJarAssets))
  }
}
