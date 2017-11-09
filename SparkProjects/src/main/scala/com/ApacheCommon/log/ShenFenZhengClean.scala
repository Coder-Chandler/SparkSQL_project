package com.ApacheCommon.log
import org.apache.spark.sql.{Row, SaveMode, SparkSession}
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types.{StringType, StructField, StructType}
/**
  * 使用Spark完成我们的数据清洗工作
  */
object ShenFenZhengClean {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder().appName("ShenFenZhengClean")
      .config("spark.sql.parquet.compression.codec", "gzip")
      .master("local[2]").getOrCreate()

    //把我们第一步清洗的log读进来
    val accessRDD = spark.sparkContext.textFile("/Users/chandler/Desktop/SfzCleanData")

    val shenfenzhengRDD = accessRDD.map(_.split("\t")).map(line => if (line.length == 9) {
      Row(
        line(0), line(1), line(2), line(3), line(4), line(5), line(6), line(7), line(8)
      )
    } else {
      Row("", "", "", "", "", "", "", "", "")
    })

    //定义一个Schema，我们用StructType来定义
    val structType = StructType(Array(
      StructField("Id", StringType, true),
      StructField("Name", StringType, true),
      StructField("SFZ", StringType, true),
      StructField("Gender", StringType, true),
      StructField("Birthday", StringType, true),
      StructField("Address", StringType, true),
      StructField("Mobile", StringType, true),
      StructField("Company", StringType, true),
      StructField("Data", StringType, true)))

    val shenfenzhengDF = spark.createDataFrame(shenfenzhengRDD, structType)

    val shenfenzhengOk = shenfenzhengDF.filter("Data!=''").filter("Id>19900000 and Id<=20000000")
//    shenfenzhengDF.filter("Id > 18000000").filter("Id <= 18100000").write.format("csv")
//      .save("/Users/chandler/Desktop/ShenFenZhengOk")

    shenfenzhengOk.coalesce(1).write.format("csv").mode(SaveMode.Overwrite)
      .save("/Users/chandler/Desktop/ShenFenZheng")
  }
}
