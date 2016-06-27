import akka.stream.Materializer
import akka.util.ByteString
import controllers.Query
import dao.Dao
import models.{Airport, Country, Page, Runway}
import org.mockito.Mockito._
import org.scalatest.mock.MockitoSugar
import org.scalatestplus.play._
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.libs.json.Json
import play.api.libs.streams.Accumulator
import play.api.mvc._
import play.api.test.Helpers._
import play.api.test._

import scala.concurrent.Future

class QueryControllerSpec extends PlaySpec with Results with MockitoSugar {

  def mockApp = new GuiceApplicationBuilder().build()
  val mtrlzr = mockApp.injector.instanceOf[Materializer]

  "fetchCountries" should {
    "should return an json array of similar countries" in {
      val countries = Seq(Country("1", "Bielorus"), Country("2", "Russia"))

      val dao = mock[Dao]
      when(dao.countriesWithNameLike("Rus")).thenReturn(countries)

      val controller = new Query(dao)
      val result: Accumulator[ByteString, Result]  = controller.similarCountries("Rus")(FakeRequest())
      val response: Future[Result] = result.run()(mtrlzr)

      contentAsJson(response) mustBe Json.toJson(countries)
    }

    "should return empty json array if no country was found" in {
      val dao = mock[Dao]
      when(dao.countriesWithNameLike("Atl")).thenReturn(Seq())

      val controller = new Query(dao)
      val result: Accumulator[ByteString, Result]  = controller.similarCountries("Atl")(FakeRequest())
      val response: Future[Result] = result.run()(mtrlzr)

      contentAsJson(response) mustBe Json.toJson(Seq.empty[Country])
    }
  }

  "airportsByCountryCode" should {
    "should return a page of airports as json object" in {
      val a1Runways = Seq(Runway(1, 10, 15, "X"), Runway(2, 10, 15, "X"))
      val a2Runways = Seq(Runway(3, 10, 15, "X"), Runway(4, 10, 15, "X"))

      val airports = Seq(Airport(1, "a1", "us", a1Runways), Airport(2, "a2", "us", a2Runways))
      val page = Page(airports, 0, 0, airports.size)

      val dao = mock[Dao]
      when(dao.airportsByCountryCode("US",0,2)).thenReturn(page)

      val controller = new Query(dao)
      val result: Accumulator[ByteString, Result] = controller.airportsByCountryCode("US",0,2)(FakeRequest())
      val response: Future[Result] = result.run()(mtrlzr)

      contentAsJson(response) mustBe Json.obj(
        "items" -> page.items,
        "from" -> (page.offset+1),
        "to" -> (page.offset + page.items.size),
        "total" -> page.total
      )
    }
  }
}