package dao

import javax.inject.{Inject, Singleton}

import models.{Airport, Page, Runway}

@Singleton
class Dao @Inject()(val s: SparkLoader) extends util.Logging {


  def airportsByCountryCode(countryCode: String, page: Int, pageSize: Int): Page[Airport] = {
    val offset = pageSize * page
    val df = s.sqlContext.sql(
      s"""SELECT a.id, a.name FROM airport a
          WHERE a.iso_country='$countryCode' ORDER BY a.name""")
    val total = df.count()
    println("_________________> " + total)
    val filteredRdd = df.rdd.zipWithIndex()
      .collect {
        case (r, i) if i >= offset && i < (offset+pageSize) => r }
    val newDf = s.sqlContext.createDataFrame(filteredRdd, df.schema)
    val items = newDf.collect().map{ row =>
      val id = row.getInt(0)
      val name = row.getString(1)
      val runways = runwaysByAirport(id)
      Airport(id, name, runways)
    }
    Page(items, page, offset, total)
  }

  def runwaysByAirport(airportId: Int): Seq[Runway] = {
    s.sqlContext.sql(
      s"""SELECT r.id, r.length_ft, r.width_ft, r.surface FROM runway r
          WHERE r.airport_ref='$airportId' ORDER BY r.id""")
      .collect
      .map{ row =>
        Runway(
          row.getInt(0),
          row.getInt(1),
          row.getInt(2),
          row.getString(3))
      }
  }

  /** Select countries with more airports */
  def highestAirports(): Map[String, Long] = {
    logger.info("Fetching top 10 countries wist highest number of airports")
    s.sqlContext.sql(
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
    s.sqlContext.sql(
      """SELECT c.name, x.count FROM
           (SELECT iso_country, count(id) as count FROM airport GROUP BY iso_country) x
            JOIN country c ON c.code=x.iso_country
        ORDER BY x.count ASC LIMIT 10""")
      .collect()
      .map{row => row.getString(0) -> row.getLong(1)}
      .toMap
  }

  def countries(): Map[String, String] = {
    s.sqlContext
      .sql(s"""SELECT code, name FROM country ORDER BY name""")
      .collect()
      .map{row => row.getString(0) -> row.getString(1)}
      .toMap
  }

  def surfaceTypesByCountry(countryCode: String): Seq[String] = {
    logger.info(s"Fetching distinct surfaceTypes in $countryCode")
    s.sqlContext
      .sql(s"""SELECT distinct r.surface
               FROM runway r
                  JOIN airport a ON r.airport_ref = a.id AND r.surface <> ''
               WHERE a.iso_country = '$countryCode'
               ORDER BY r.surface""")
        .collect()
      .map{row => row.getString(0)}
  }
}