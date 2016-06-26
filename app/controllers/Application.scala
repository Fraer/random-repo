package controllers

import javax.inject.{Inject, Singleton}

import _root_.util.Logging
import dao.Dao
import play.api.mvc.{Action, Controller}

@Singleton
class Application @Inject()(val webJarAssets: WebJarAssets) extends Controller {

  def index = Action(parse.empty) { req =>
    Ok(views.html.index(webJarAssets))
  }
}
