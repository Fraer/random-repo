package util

import play.api.Logger

/**
 * Created by vbodnarchuk on 27.11.2015.
 */
trait Logging {
  val logger = Logger(s"application.${this.getClass}")
}
