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
      //判断分割后的每一行是否长度为33，防止写入数据过程报数组越界的错误
      if (splits.length == 33) {
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
    })

//    //导入隐式转换
//    import spark.implicits._
//    val shenfenzhengRDD = shenfenzhengformat.map(_.split("\t")).map(line => if(line.length==9) {
//      Row(
//        line(0), line(1), line(2), line(3), line(4), line(5), line(6), line(7), line(8)
//      )} else {
//      Row("", "", "", "", "", "", "", "", "")
//    })
//
//    //定义一个Schema，我们用StructType来定义
//    val structType = StructType(Array(
//      StructField("Id",StringType , true),
//      StructField("Name", StringType, true),
//      StructField("SFZ", StringType, true),
//      StructField("Gender", StringType, true),
//      StructField("Birthday", StringType, true),
//      StructField("Address", StringType, true),
//      StructField("Mobile", StringType, true),
//      StructField("Company", StringType, true),
//      StructField("Data", StringType, true)))
//
//    val shenfenzhengDF = spark.createDataFrame(shenfenzhengRDD, structType)
//
//    //shenfenzhengDF.show(false)
//    shenfenzhengDF.filter("Id <= 18100000").write.format("csv")
//      .save("/Users/chandler/Desktop/SfzCleanData")

    spark.stop()
  }

}
