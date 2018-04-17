package com.hadoop.spark

import org.apache.spark.sql.SparkSession

/**
  * 使用外部数据源综合查询Hive和MySQL的表数据
  */
object HiveMySQLApp {

  def main(args: Array[String]) {
    val spark = SparkSession.builder().appName("HiveMySQLApp")
      .master("local[2]").getOrCreate()

    // 加载Hive表数据
//    val hiveDF = spark.table("emp")

    // 加载MySQL表数据
    val mysql_zhihuanswer_DF = spark.read.format("jdbc").option("url", "jdbc:mysql://localhost:3306").option("dbtable", "spider.zhihu_answer").option("user", "root").option("password", "chuchu910408").option("driver", "com.mysql.jdbc.Driver").load()
    val mysql_zhihuquestion_DF = spark.read.format("jdbc").option("url", "jdbc:mysql://localhost:3306").option("dbtable", "spider.zhihu_question").option("user", "root").option("password", "chuchu910408").option("driver", "com.mysql.jdbc.Driver").load()

    // JOIN
    val resultDF = mysql_zhihuquestion_DF.join(mysql_zhihuanswer_DF, mysql_zhihuanswer_DF.col("question_id") === mysql_zhihuquestion_DF.col("zhihu_id"))
    resultDF.show

//    resultDF.select(hiveDF.col("empno"),hiveDF.col("ename"),
//      mysqlDF.col("deptno"), mysqlDF.col("dname")).show

    spark.stop()
  }

}
