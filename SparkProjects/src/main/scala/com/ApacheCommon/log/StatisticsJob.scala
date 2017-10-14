package com.ApacheCommon.log
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions.{count, sum}

/**
  * 统计Spark作业
  */
object StatisticsJob {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder().appName("StatisticsJob")
      .master("local[2]").getOrCreate()

    val accessdataframe = spark.read.format("parquet").load("hdfs://localhost:8020/WEB_log/Apache_common/claen_data_2")

    import spark.implicits._
    val statistics = accessdataframe.filter($"day" === "20130530")
      .groupBy("day", "url")
      .agg(sum("traffic").as("sums"),count("traffic").as("times"),
        (sum("traffic")/count("traffic")).as("avg(sums/times)"))
      .orderBy($"sums")

    accessdataframe.printSchema()
    accessdataframe.show(false)

    spark.stop()

  }
}
