package com.ApacheCommon.log
import org.apache.spark.sql.SparkSession

/**
  * 统计Spark作业
  */
object StatisticsJob {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder().appName("StatisticsJob")
      .master("local[2]").getOrCreate()

    val accessDF = spark.read.format("parquet").load("hdfs://localhost:8020/WEB_log/Apache_common/claen_data_2")

    accessDF.printSchema()
    accessDF.show(false)

    spark.stop()

  }
}
