package com.girl

import org.apache.spark.sql.{SaveMode, SparkSession}

object sfz1400_1600 {
  def main(args: Array[String]): Unit = {

    val spark = SparkSession.builder().appName("DataFrameApp")
      .config("spark.sql.parquet.compression.codec", "gzip")
      .master("local[2]")
      .getOrCreate()

    //把json文件加载成DataFrame
    val accessRDD = spark.sparkContext.textFile("/Users/chandler/Desktop/1400W-1600W.txt")

    //导入隐式转换
    import spark.implicits._
    val dataframe = accessRDD.map(_.split("\t")).map(line => People(line(0), line(4),
      line(19),line(20),line(22),line(21),line(5),line(6),line(7),line(8))).toDF()


    dataframe.show(false)
    //输出dataframe对应的Schema信息
    dataframe.printSchema()

    dataframe.coalesce(1).write.format("csv").mode(SaveMode.Overwrite)
      .save("/Users/chandler/Desktop/1400_1600")

    spark.stop()
  }
  case class People(Name: String, CtfId: String, Mobile: String,
                    Tel: String, EMail: String, Fax: String,
                    Gender: String,Birthday: String,Address: String, Zip: String)
}
