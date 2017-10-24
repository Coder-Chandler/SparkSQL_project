package com.ApacheCommon.log

import org.apache.spark.sql.{SaveMode, SparkSession}

/**
  * 使用Spark完成我们的数据清洗工作：运行在Yarn上
  */
object SparkStartCleanJobYarn {
  def main(args: Array[String]): Unit = {

    if(args.length != 2) {
      println("Usage: SparkStartCleanJobYarn <inputPath><outputPath>")
      System.exit(1)
    }

    val Array(inputPath, outputPath) = args

    val spark = SparkSession.builder().getOrCreate()

    //把我们第一步清洗的log读进来
    val accessRDD = spark.sparkContext.textFile(inputPath)

    //导入隐式转换
    import spark.implicits._
    val accessDF = accessRDD.map(_.split("\t")).map(line => if(line.length==5) {Info(
      line(1), line(3).toLong, line(2), IpUtils.getCity(line(2)), line(0), line(0).substring(0,10).replaceAll("-", "")
    )} else {
      Info("", 0, "", "", "", "")
    }).toDF()

    val accessDFclean = accessDF.filter("url!='' or ip!='' or city!='' or time!='' or day!=''")

    val accessdataframe = accessDFclean.filter("traffic!=0")

    //以parquet的格式将清洗过的数据按照day分区存入HDFS里面去，注意coalesce表示输出为一个文件，这也是一个调优点
    accessdataframe.coalesce(1).write.format("parquet").mode(SaveMode.Overwrite)
      .partitionBy("day").save(outputPath)

    spark.stop()
  }
  case class Info(url: String, traffic: Long, ip: String, city: String, time: String, day: String)
}
