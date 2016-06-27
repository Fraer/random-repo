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

  "similarCountries" should {
    "should return similar countries" in {
      val res = dao.similarCountries("Ant")
      res.size mustBe 2
      res should contain (Country("AQ","Antarctica"))
      res should contain (Country("AG","Antigua and Barbuda"))
    }
  }

  "surfaceTypesByCountry" should {
    "should return surface type of a country" in {
      val res = dao.surfaceTypesByCountry("AS")
      res.size mustBe 3
      res should contain ("TURF")
      res should contain ("GRASS")
      res should contain ("GRAVEL")
    }
  }
}
