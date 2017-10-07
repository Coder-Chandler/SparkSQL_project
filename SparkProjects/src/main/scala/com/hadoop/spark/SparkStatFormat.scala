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

    //access.take(20).foreach(println)

    access.map(line => {
      val splits = line.split(" ")
      val ip = splits(0)

      /**
        *原始日志的第三个和第四个字段拼接就是完整的访问时间：
        * [30/May/2013:17:38:20 +0800] ==> yyyy-MM-dd HH:mm:ss
        */
      val time = splits(3)+" "+splits(4)
      (ip, DataUtils.parse(time))
    }).take(10).foreach(println)

    spark.stop()
  }

}
