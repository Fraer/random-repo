import akka.stream.Materializer
import akka.util.ByteString
import controllers.{Query, Report}
import dao.Dao
import models._
import org.mockito.Mockito._
import org.scalatest.mock.MockitoSugar
import org.scalatestplus.play.PlaySpec
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.libs.json.Json
import play.api.libs.streams.Accumulator
import play.api.mvc.{Result, Results}
import play.api.test.FakeRequest
import play.api.test.Helpers._

import scala.concurrent.Future

class ReportControllerSpec extends PlaySpec with Results with MockitoSugar {

  def mockApp = new GuiceApplicationBuilder().build()
  val mtrlzr = mockApp.injector.instanceOf[Materializer]

  "highestAirports" should {
    "should return an array of 10 json objects {name: string, count: number}" in {
      val res = (0 to 10).map { x => s"Country$x" -> x.toLong }.toMap

      val dao = mock[Dao]
      when(dao.highestAirports()).thenReturn(res)

      val controller = new Report(dao)
      val result: Accumulator[ByteString, Result] = controller.highestAirports()(FakeRequest())
      val response: Future[Result] = result.run()(mtrlzr)

      contentAsJson(response) mustBe Json.toJson(
        res.map { x =>
          Json.obj("name" -> x._1, "count" -> x._2)
        })
    }
  }

  "lowestAirports" should {
    "should return an array of 10 json objects {name: string, count: number}" in {
      val res = (0 to 10).map { x => s"Country$x" -> x.toLong }.toMap

      val dao = mock[Dao]
      when(dao.lowestAirports()).thenReturn(res)

      val controller = new Report(dao)
      val result: Accumulator[ByteString, Result] = controller.lowestAirports()(FakeRequest())
      val response: Future[Result] = result.run()(mtrlzr)

      contentAsJson(response) mustBe Json.toJson(
        res.map { x =>
          Json.obj("name" -> x._1, "count" -> x._2)
        })
    }
  }

  "lowestAirports" should {
    "should return an array of 10 json objects {name: string, count: number}" in {
      val res = (0 to 10).map { x => s"Country$x" -> x.toLong }.toMap

      val dao = mock[Dao]
      when(dao.lowestAirports()).thenReturn(res)

      val controller = new Report(dao)
      val result: Accumulator[ByteString, Result] = controller.lowestAirports()(FakeRequest())
      val response: Future[Result] = result.run()(mtrlzr)

      contentAsJson(response) mustBe Json.toJson(
        res.map { x =>
          Json.obj("name" -> x._1, "count" -> x._2)
        })
    }
  }

}
