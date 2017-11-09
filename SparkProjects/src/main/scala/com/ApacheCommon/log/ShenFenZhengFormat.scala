package com.ApacheCommon.log

import org.apache.spark.sql.types.{ StringType, StructField, StructType}
import org.apache.spark.sql.{Row, SparkSession}
/**
  * 第一步清洗：抽取我们所需要的指定列的数据
  */
object ShenFenZhengFormat {

  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder().appName("ShenFenZhengFormat")
      .master("local[2]").getOrCreate()

    val shenfenzheng = spark.sparkContext.textFile("/Users/chandler/Desktop/1800w-2000w.csv")

    shenfenzheng.map(line => {
      val splits = line.split(",")
      //判断分割后的每一行日志是否长度为10，目的为过滤掉缺少列数据的日志，防止写入数据过程报数组越界的错误
      if (splits.length == 3) {
        val Id = splits(32)
        val Name = splits(0)
        val SFZ = splits(4)
        val Gender = splits(5)
        val Birthday = splits(6)
        val Address = splits(7)
        val Mobile = splits(19)
        val Company = splits(26)
        val Data = splits(31)
        Id + "\t" + Name + "\t" + SFZ + "\t" + Gender + "\t" + Birthday+ "\t" +
          Address + "\t" + Mobile  + "\t" + Company  + "\t" + Data
      }
    }).take(1).foreach(println)

    spark.stop()
  }

}
