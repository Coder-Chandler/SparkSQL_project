package com.ApacheCommon.log

import org.apache.spark.sql.expressions.Window
import org.apache.spark.sql.functions.{count, row_number, sum}
import org.apache.spark.sql.{DataFrame, SparkSession}

import scala.collection.mutable.ListBuffer

/**
  * 统计Spark作业：运行在Yarn上
  */
object StatisticsJobYarn {
  def main(args: Array[String]): Unit = {

    if(args.length != 2) {
      println("Usage: SparkStartCleanJobYarn <inputPath> <day>")
      System.exit(1)
    }

    val Array(inputPath, day) = args

    val spark = SparkSession.builder()
        .config("spark.sql.sources.partitionColumnTypeInference.enabled", "false").getOrCreate()

    val accessdataframe = spark.read.format("parquet").load(inputPath)

    //先删除指定日期的数据再执行插入数据，保证数据不重复
    StatisticsDAO.deleteData(day)

    //计算url的PV和流量
    urlPvTrafficStatistics(spark, accessdataframe, day)

    //计算按照地理区域统计
    urlAreaStatistics(spark, accessdataframe, day)

    //ip相关统计
    IpAddressStatistics(spark, accessdataframe, day)

    spark.stop()
  }

  /**
    * 计算url的PV和流量
    * @param spark
    * @param accessdataframe
    */
  def urlPvTrafficStatistics(spark: SparkSession, accessdataframe: DataFrame, day: String): Unit = {

    import spark.implicits._
    // 按照url进行统计每个url的总流量和访问次数
    val statistics_url_Pv_traffic = accessdataframe.filter($"day" === day)
      .groupBy("day", "url")
      .agg(sum("traffic").as("traffic_sums"),count("traffic").as("page_view"),
        (sum("traffic")/count("traffic")).as("avg"))
      .orderBy($"traffic_sums")

    /**
      * 将统计结果写到mysql中
      */
    try {
      statistics_url_Pv_traffic.foreachPartition(partitionOfRecords => {
        val list = new ListBuffer[UrlPvTraffic]

        partitionOfRecords.foreach(info => {
          val day = info.getAs[String]("day")
          val url = info.getAs[String]("url")
          val traffic_sums = info.getAs[Long]("traffic_sums")
          val page_view = info.getAs[Long]("page_view")
          val avg = info.getAs[Double]("avg")
          list.append(UrlPvTraffic(day, url, traffic_sums, page_view, avg))
        })
        StatisticsDAO.insertDayUrlPvTraffic(list)
      })
    } catch {
      case e: Exception => e.printStackTrace()
    }
  }

  /**
    * 计算按照地理区域统计访问量最高的url topN访问次数
    * @param spark
    * @param accessdataframe
    */
  def urlAreaStatistics(spark: SparkSession, accessdataframe: DataFrame, day: String): Unit = {

    import spark.implicits._
    // 按照地理区域统计访问量最高的url topN访问次数
    val url_city_statistics = accessdataframe.filter($"day" === day)
      .groupBy("day", "city", "url")
      .agg(sum("traffic").as("traffic_sums"),
        count("traffic").as("page_view"),
        (sum("traffic")/count("traffic")).as("avg"))
      .orderBy($"traffic_sums")

    //window窗口函数在Spark SQL中的使用
    val url_area_statistics = url_city_statistics.select(

      url_city_statistics("day"),
      url_city_statistics("url"),
      url_city_statistics("city"),
      url_city_statistics("traffic_sums"),
      url_city_statistics("page_view"),

      row_number().over(
        Window.partitionBy(url_city_statistics("city"))
        .orderBy(url_city_statistics("page_view").desc)
      ).as("page_view_rank")

    ).filter("page_view_rank <= 3")

    /**
      * 将统计结果写到mysql中
      */
    try {
      url_area_statistics.foreachPartition(partitionOfRecords => {
        val list = new ListBuffer[AreaPvTraffic]

        partitionOfRecords.foreach(info => {
          val day = info.getAs[String]("day")
          val url = info.getAs[String]("url")
          val city = info.getAs[String]("city")
          val traffic_sums = info.getAs[Long]("traffic_sums")
          val page_view = info.getAs[Long]("page_view")
          val page_view_rank = info.getAs[Int]("page_view_rank")
          list.append(AreaPvTraffic(day, url, city, traffic_sums, page_view, page_view_rank))
        })
        StatisticsDAO.insertDayCityPvTraffic(list)
      })
    } catch {
      case e: Exception => e.printStackTrace()
    }
  }

  /**
    * ip地址相关统计
    * @param spark
    * @param accessdataframe
    */
  def IpAddressStatistics(spark: SparkSession, accessdataframe: DataFrame, day: String): Unit = {

    import spark.implicits._
    // 按照地理区域统计访问量最高的url topN访问次数
    val ip_statistics = accessdataframe.filter($"day" === day)
      .groupBy("day", "ip")
      .agg(sum("traffic").as("traffic_sums"),
        count("traffic").as("page_view")
      ).orderBy($"page_view".desc)

    /**
      * 将统计结果写到mysql中
      */
    try {
      ip_statistics.foreachPartition(partitionOfRecords => {
        val list = new ListBuffer[IpStatistics]

        partitionOfRecords.foreach(info => {
          val day = info.getAs[String]("day")
          val ip = info.getAs[String]("ip")
          val traffic_sums = info.getAs[Long]("traffic_sums")
          val page_view = info.getAs[Long]("page_view")
          list.append(IpStatistics(day, ip, traffic_sums, page_view))
        })
        StatisticsDAO.insertIpStatistics(list)
      })
    } catch {
      case e: Exception => e.printStackTrace()
    }
  }
}
