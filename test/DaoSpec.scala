import dao.Dao
import org.scalatest.mock.MockitoSugar
import org.scalatestplus.play.PlaySpec
import play.api.mvc.Results
import util.{CsvPaths, SparkLoader}
import org.scalatest.Matchers._
import models.{Airport, Country, Page, Runway}

class DaoSpec extends PlaySpec with Results with MockitoSugar {

  org.slf4j.LoggerFactory.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME)
    .asInstanceOf[ch.qos.logback.classic.Logger]
    .setLevel(ch.qos.logback.classic.Level.WARN)

  val paths = new CsvPaths{
    override val countriesPath = "testResources/countries.csv"
    override val airportsPath = "testResources/airports.csv"
    override val runwaysPath = "testResources/runways.csv"
  }

  val s = new SparkLoader(paths)
  val dao = new Dao(s)

  "countries" should {
    "should return all countries" in {

      val res = dao.countries()
      res.size mustBe 11

      res.head.code mustBe "AF"
      res(5).code mustBe "AI"
      res.last.code mustBe "AE"
    }
  }

  "countriesWithNameLike" should {
    "should return similar countries" in {
      val res = dao.countriesWithNameLike("Ant")
      res.size mustBe 2
      res should contain (Country("AQ","Antarctica"))
      res should contain (Country("AG","Antigua and Barbuda"))
    }
  }

  "surfaceTypesByCountry" should {
    "should return surface type of a country" in {
      val res = dao.surfaceTypesByCountryCode("AS")
      res.size mustBe 3
      res should contain ("TURF")
      res should contain ("GRASS")
      res should contain ("GRAVEL")
    }
  }

  "countryByCode" should {
    "should return an option[country]" in {
      val res = dao.countryByCode("AS")
      res mustBe Some(Country("AS","American Samoa"))
    }
  }

  "runwaysByAirport" should {
    "should return all runways of an airport" in {
      val res = dao.runwaysByAirport(10)
      res.size mustBe 3
      res.head.id mustBe 10
      res.last.id mustBe 12
    }
  }

  "lowestAirports" should {
    "should return 10 countries with less airports" in {
      val res = dao.lowestAirports()
      res.size mustBe 10
      res should contain ("Argentina" -> 1)
      res should not contain ("American Samoa" -> 3)
    }
  }

  "highestAirports" should {
    "should return 10 countries with highest airports count" in {
      val res = dao.highestAirports()
      res.size mustBe 10
      res.head._1 mustBe "American Samoa"
    }
  }

  "mostCommonRunwayLatitudes" should {
    "should return 10 most common runway latitudes" in {
      val res = dao.mostCommonRunwayLatitudes()
      res.size mustBe 10
      res.head mustBe ("H1", 7)
    }
  }
}
