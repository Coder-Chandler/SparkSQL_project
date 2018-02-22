package com.ApacheCommon.log

import org.apache.spark.sql.{SaveMode, SparkSession}

/**
  * DataFrame API 基本操作
  */
object JiangSuApp {

  def main(args: Array[String]): Unit = {

    val spark = SparkSession.builder().appName("JiangSuApp")
      .config("spark.sql.parquet.compression.codec", "gzip")
      .master("local[2]")
      .getOrCreate()

    //把json文件加载成DataFrame
    val peopledataframe = spark.read.format("csv").load("/Users/chandler/Desktop/clean_sfz/1-200/1-200.csv")

    // 查看数据量
    println(peopledataframe.count())

    //每一列对应的标记
    val Name = "_c0"
    val CtfId = "_c1"
    val Mobile = "_c2"
    val Tel = "_c3"
    val EMail = "_c4"
    val Fax = "_c5"
    val Gender = "_c6"
    val Birthday = "_c7"
    val Address = "_c8"
    val Zip = "_c9"

    peopledataframe.createOrReplaceTempView("people_dataframe")
    spark.sql("show tables").show()
    println(spark.sql("select * from people_dataframe where _c8 like '%南京%' and _c7>19800101 ").count())
    //    spark.sql("select * from people_dataframe where _c8 like '%南京%' and _c7>19800101 ").show(false)

    val jiangsu = spark.sql("select * from people_dataframe where _c8 like '%江苏%' ")
    println(jiangsu.count())
    jiangsu.show()

    jiangsu.coalesce(1).write.format("csv").mode(SaveMode.Overwrite)
      .save("/Users/chandler/Documents/Data/NumPhone/jiangsu")

    spark.stop()
  }
}
