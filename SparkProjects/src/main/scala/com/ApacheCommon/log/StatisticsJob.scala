package com.ApacheCommon.log
import org.apache.spark.sql.{DataFrame, SparkSession}
import org.apache.spark.sql.functions.{count, sum}

import scala.collection.mutable.ListBuffer

/**
  * 统计Spark作业
  */
object StatisticsJob {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder().appName("StatisticsJob")
        .config("spark.sql.sources.partitionColumnTypeInference.enabled", "false")
      .master("local[2]").getOrCreate()

    val accessdataframe = spark.read.format("parquet").load("hdfs://localhost:8020/WEB_log/Apache_common/clean_data_2")

    urlPvTrafficStatistics(spark, accessdataframe)

    spark.stop()
  }

  /**
    * 计算url的PV和流量
    * @param spark
    * @param accessdataframe
    */
  def urlPvTrafficStatistics(spark: SparkSession, accessdataframe: DataFrame): Unit = {

    import spark.implicits._
    // 按照url进行统计每个url的总流量和访问次数
    val statistics_url_Pv_traffic = accessdataframe.filter($"day" === "20130530")
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


//    val statistics_ip = accessdataframe.filter($"day" === "20130530")
//      .groupBy("day", "ip").agg(count("ip").as("ip_count")).orderBy($"ip_count")
//
//    println(statistics_ip.count())
//    statistics_ip.orderBy(statistics_ip("ip_count").desc).describe().show(false)
  }
}
