package dao

import javax.inject.{Inject, Singleton}

import org.apache.spark.sql.functions._
import models.{Airport, Country, Page, Runway}
import util.SparkLoader

@Singleton
class Dao @Inject()(val s: SparkLoader) extends util.Logging {

  def airportsByCountryCode(countryCode: String, page: Int, pageSize: Int): Page[Airport] = {
    val offset = pageSize * page
    val df = s.sqlCtx.sql(
      s"""SELECT a.id, a.name, a.iso_region FROM airport a
          WHERE a.iso_country='$countryCode' ORDER BY a.name""")
    val total = df.count()
    val filteredRdd = df.rdd.zipWithIndex()
      .collect {
        case (r, i) if i >= offset && i < (offset+pageSize) => r }
    val newDf = s.sqlCtx.createDataFrame(filteredRdd, df.schema)
    val items = newDf.collect().map{ row =>
      val id = row.getInt(0)
      val name = row.getString(1)
      val region = row.getString(2)
      val runways = runwaysByAirport(id)
      Airport(id, name, region, runways)
    }
    Page(items, page, offset, total)
  }

  def runwaysByAirport(airportId: Int): Seq[Runway] = {
    s.sqlCtx.sql(
      s"""SELECT r.id, r.length_ft, r.width_ft, r.surface FROM runway r
          WHERE r.airport_ref='$airportId' ORDER BY r.id""")
      .collect
      .map{ row =>
        Runway(
          row.getInt(0),
          if (row.isNullAt(1)) -1 else row.getInt(1),
          if (row.isNullAt(2)) -1 else row.getInt(2),
          row.getString(3))
      }
  }

  /** Select countries with more airports */
  def highestAirports(): Map[String, Long] = {
    logger.info("Fetching top 10 countries wist highest number of airports")
    s.sqlCtx.sql(
      """SELECT c.name, x.count FROM
           (SELECT iso_country, count(id) as count FROM airport GROUP BY iso_country) x
            JOIN country c ON c.code=x.iso_country
        ORDER BY x.count DESC LIMIT 10""")
        .collect()
        .map{row => row.getString(0) -> row.getLong(1)}
        .toMap
  }

  /** Select countries with less airports */
  def lowestAirports(): Map[String, Long] = {
    s.sqlCtx.sql(
      """SELECT c.name, x.count FROM
           (SELECT iso_country, count(id) as count FROM airport GROUP BY iso_country) x
            JOIN country c ON c.code=x.iso_country
        ORDER BY x.count ASC LIMIT 10""")
      .collect()
      .map{row => row.getString(0) -> row.getLong(1)}
      .toMap
  }

  def countries(): Seq[Country] = {
    s.sqlCtx
      .sql(s"""SELECT code, name FROM country ORDER BY name""")
      .collect()
      .map{row => Country(row.getString(0),row.getString(1))}
  }

  def countryByCode(code: String): Option[Country] = {
    logger.info(s"Looking for country with code $code")
    s.countries.filter(upper(col("code")) === code.toUpperCase)
      .select(col("code"), col("name"))
      .take(1)
      .map{row => Country(row.getString(0), row.getString(1))}
      .headOption
  }

  def countriesWithNameLike(input: String, nbRows: Int = 10): Seq[Country] = {
    logger.info(s"Looking for countries with name like $input")
    s.countries.filter(upper(col("name")).like(s"%${input.toUpperCase}%") )
      .select(col("code"), col("name"))
      .take(nbRows)
      .map{row => Country(row.getString(0), row.getString(1))}
  }

  def surfaceTypesByCountry(countryCode: String): Seq[String] = {
    logger.info(s"Fetching distinct surfaceTypes in $countryCode")
    s.sqlCtx
      .sql(s"""SELECT distinct r.surface
               FROM runway r
                  JOIN airport a ON r.airport_ref = a.id AND r.surface <> ''
               WHERE a.iso_country = '$countryCode'
               ORDER BY r.surface""")
        .collect()
      .map{row => row.getString(0)}
  }
}