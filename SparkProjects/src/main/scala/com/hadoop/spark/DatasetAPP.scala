package com.hadoop.spark

import org.apache.spark.sql.SparkSession

/**
  * Dataset的操作
  */
object DatasetAPP {

  def main(args: Array[String]): Unit = {

    val spark = SparkSession.builder().appName("DatasetAPP").master("local[2]").getOrCreate()

    //csv文件路径
    val path = "file:///Users/chandler/Documents/Projects/SparkProjects/sales.csv"

    //导入隐式转换
    import spark.implicits._

    //spark如何解析csv文件（外部数据源功能）
    val csv_dataframe = spark.read.option("header","true").option("inferSchema","true").csv(path)
    csv_dataframe.show

    //把csv_dataframe转换成Dataset
    val csv_dataset = csv_dataframe.as[Sales]
    csv_dataset.map(line => line.itemId).show

    spark.stop()
  }

  //把csv文件的列名拷贝进来
  case class Sales(transactionId:Int,customerId:Int,itemId:Int,amountPaid:Double)

}
