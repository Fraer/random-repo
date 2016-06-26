package dao

import javax.inject.{Inject, Singleton}

import org.apache.spark.sql.{DataFrame, SQLContext}
import org.apache.spark.{SparkConf, SparkContext}
import util.CsvPaths

/**
  * Handles configuration, context and creates data frames
  */
@Singleton
class SparkLoader @Inject()(val paths: CsvPaths) {

  //build the SparkConf object at once
  lazy val conf = {
    new SparkConf(false)
      .setMaster("local[*]")
      .setAppName("Lunatech")
      .set("spark.logConf", "true")
  }

  private lazy val sc = SparkContext.getOrCreate(conf)
  private lazy val s = new SQLContext(sc)

  lazy val sqlCtx: SQLContext = {
    countries
    airports
    runways
    s
  }

  lazy val countries = load(paths.countriesPath, "country")
  lazy val airports = load(paths.airportsPath, "airport")
  lazy val runways = load(paths.runwaysPath, "runway")

  private def load(path: String, tableName: String): DataFrame = {
    val df = s.read.format("com.databricks.spark.csv").option("header", "true")
      .option("inferSchema", "true")
      .load(path)
    df.registerTempTable(tableName)
    df
  }
}