package com.Apache_common.log

import org.apache.spark.sql.SparkSession

/**
  * 使用Spark完成我们的数据清洗工作
  */
object SparkStartCleanJob {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder().appName("SparkStartCleanJob")
      .master("local[2]").getOrCreate()

    val accessRDD = spark.sparkContext.textFile("hdfs://localhost:8020/WEB_log/Apache_common/clean_data/part-00000")

    //RDD ==> DF
    val accessDF = spark createDataFrame(accessRDD.map(x => AccessConverUtil.parseLog(x)),
      AccessConverUtil.struct)

    accessDF.printSchema()

    accessDF.show(10)

    spark.stop()
  }
}
