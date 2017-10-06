package com.hadoop.spark

import org.apache.spark.sql.SparkSession

/**
  * 第一步清洗：抽取我们所需要的指定列的数据
  */
object SparkStatFormat {

  def main(args: Array[String]): Unit = {

    val spark = SparkSession.builder().appName("HiveMySQLApp")
      .master("local[2]").getOrCreate()

    val access = spark.sparkContext.textFile("file:///Users/chandler/Documents/data/test_data/access_2013_05_30.log")

    access.take(20).foreach(println)

    spark.stop()
  }

}
