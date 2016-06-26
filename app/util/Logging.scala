package util

import play.api.Logger

trait Logging {
  val logger = Logger(s"application.${this.getClass}")
}
