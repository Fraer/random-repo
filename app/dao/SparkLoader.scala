package dao

import javax.inject.{Inject, Singleton}

import org.apache.spark.sql.{DataFrame, SQLContext}
import org.apache.spark.{SparkConf, SparkContext}

/**
  * Handles configuration, context and so
  */
@Singleton
class SparkLoader @Inject()() extends util.Logging {

  //build the SparkConf object at once
  lazy val conf = {
    new SparkConf(false)
      .setMaster("local[*]")
      .setAppName("Lunatech")
      .set("spark.logConf", "true")
  }

  lazy val sc = SparkContext.getOrCreate(conf)
  lazy val sqlContext = {
    val s = new SQLContext(sc)
    load(s, "resources/countries.csv", "country")
    load(s, "resources/airports.csv", "airport")
    load(s, "resources/runways.csv", "runway")
    s
  }

  //lazy val countries = load("resources/countries.csv", "country")
  //lazy val airports = load("resources/airports.csv", "airport")
  //lazy val runways = load("resources/runways.csv", "runway")

  private def load(s: SQLContext, path: String, tableName: String): DataFrame = {
    val df = s
      .read.format("com.databricks.spark.csv").option("header", "true")
      .option("inferSchema", "true")
      .load(path)
    df.registerTempTable(tableName)
    df
  }
}