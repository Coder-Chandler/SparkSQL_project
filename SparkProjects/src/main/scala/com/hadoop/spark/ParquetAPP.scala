package com.hadoop.spark
import org.apache.spark.sql.SparkSession
/**
  * Parquet文件操作
  */
object ParquetAPP {
  def main(args: Array[String]): Unit = {

    val spark = SparkSession.builder().appName("DataFrameRDDApp").master("local[2]").getOrCreate()

    //parquet的测试文件在spark安装目录下有提供测试文件
    val path = "/Users/chandler/Downloads/hadoop/spark-2.2.0-bin-2.6.0cdh5.7.0/examples/src/main/resources/users.parquet"

    //把parquet文件read进来
    val parquettest = spark.read.format("parquet").load(path)

    //查看Schema信息
    parquettest.printSchema()

    //查看所有数据
    parquettest.show(false)

    //我们把name和favorite_color写出为json格式并save到指定的路径下
    parquettest.select("name", "favorite_color").write.format("json").save("/Users/chandler/Desktop/test")

    spark.stop()
  }

}
