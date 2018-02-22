package com.girl

import org.apache.spark.sql.{SaveMode, SparkSession}

object sfz400_600 {
  def main(args: Array[String]): Unit = {

    val spark = SparkSession.builder().appName("DataFrameApp")
      .config("spark.sql.parquet.compression.codec", "gzip")
      .master("local[2]")
      .getOrCreate()

    //把json文件加载成DataFrame
    val peopledataframe = spark.read.format("csv").load("/Users/chandler/Documents/Data/NumPhone/2000W/400W-600W.csv")

    //输出dataframe对应的Schema信息
    //peopledataframe.printSchema()

    val Name = "_c0"
    val CtfId = "_c4"
    val Mobile = "_c19"
    val Tel = "_c20"
    val EMail = "_c22"
    val Fax = "_c21"
    val Gender = "_c5"
    val Birthday = "_c6"
    val Address = "_c7"
    val Zip = "_c8"


    //只输出name这一列:select name from table
    //peopledataframe.select(CtfId).show(50)
    //peopledataframe.select("_c19").show(100)
    val clean_sfz = peopledataframe.select(
      peopledataframe.col("_c0"),
      peopledataframe.col("_c4"),
      peopledataframe.col("_c19"),
      peopledataframe.col("_c20"),
      peopledataframe.col("_c22"),
      peopledataframe.col("_c21"),
      peopledataframe.col("_c5"),
      peopledataframe.col("_c6"),
      peopledataframe.col("_c7"),
      peopledataframe.col("_c8")
    ).filter("_c19 != ''")

    clean_sfz.show()

    clean_sfz.coalesce(1).write.format("csv").mode(SaveMode.Overwrite)
      .save("/Users/chandler/Documents/Data/NumPhone/clean_sfz/400-600")

    spark.stop()
  }
}
