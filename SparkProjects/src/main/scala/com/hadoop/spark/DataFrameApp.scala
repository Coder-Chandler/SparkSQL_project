package com.hadoop.spark
import org.apache.spark.sql.SparkSession
/**
  * DataFrame API 基本操作
  */
object DataFrameApp {

  def main(args: Array[String]): Unit = {

    val spark = SparkSession.builder().appName("DataFrameApp").master("local[2]").getOrCreate()

    //把json文件加载成DataFrame
    val peopledataframe = spark.read.format("json").load("file:///Users/chandler/Documents/Projects/SparkProjects/people.json")

    //输出dataframe对应的Schema信息
    peopledataframe.printSchema()

    //show的方法来得到我们的内容(注意show方法默认只显示20条数据，要想显示多少可以自己🈯️指定，在括号内输入多少条就可以了)
    peopledataframe.show()

    //只输出name这一列:select name from table
    peopledataframe.select("name").show()

    //查询某几列数据，并且对列进行计算：select name, age+10 as age2 from table
    peopledataframe.select(peopledataframe.col("name"), (peopledataframe.col("age")+10).as("age2")).show()

    //查询年龄大于20岁，也就是过滤数据：select * from table where age > 20
    peopledataframe.filter(peopledataframe.col("age") > 20).show()

    //根据某一列进行分组，然后再进行聚合操作：select age,count(1) from table group by age
    peopledataframe.groupBy("age").count().show()

    spark.stop()
  }
}
