package com.hadoop.spark
import org.apache.spark.sql.types.{IntegerType, StringType, StructField, StructType}
import org.apache.spark.sql.{Row, SparkSession}
/**
  * DataFrame和RDD的互操作
  */
object DataFrameRDDApp {
  def main(args: Array[String]): Unit = {

    val spark = SparkSession.builder().appName("DataFrameRDDApp").master("local[2]").getOrCreate()

    //通过反射的方式
    //peopleReflection(spark)

    //通过编程的方式
    //programe(spark)

    spark.stop()
  }

  //1、通过编程的方式
  def programe(spark: SparkSession): Unit = {
    //RDD ==> DataFrame
    val rdd = spark.sparkContext.textFile("file:///Users/chandler/Documents/Projects/SparkProjects/people.txt")

    //1、创建一个RDD，我们用RowS来创建
    val peopleRDD = rdd.map(_.split(",")).map(line => Row(line(0).toInt, line(1), line(2).toInt))

    //2、定义一个Schema，我们用StructType来定义
    val structType = StructType(Array(StructField("id", IntegerType, true),
      StructField("name", StringType, true),
      StructField("age", IntegerType, true)))
    //3、把这个Schema作用到RDD的RowS上面通过createDataFrame这个方法来实现，当然这个方法是通过SaprkSession来提供的
    val peopledataframe = spark.createDataFrame(peopleRDD, structType)
    peopledataframe.printSchema()
    peopledataframe.show()

  }
  //2、通过反射的方式
  private def peopleReflection(spark: SparkSession) = {
    //RDD ==> DataFrame
    val rdd = spark.sparkContext.textFile("file:///Users/chandler/Documents/Projects/SparkProjects/people.txt")

    //导入隐式转换
    import spark.implicits._
    val peopledataframe = rdd.map(_.split(",")).map(line => Info(line(0).toInt, line(1), line(2).toInt)).toDF()

    peopledataframe.show()

    //取出年龄大于20的数据
    peopledataframe.filter(peopledataframe.col("age") > 20).show

    //用sparkSQL的方式来查询数据
    peopledataframe.createOrReplaceTempView("people_dataframe")
    spark.sql("show tables").show()
    spark.sql("select * from people_dataframe where age > 20").show()
  }

  case class Info(id: Int, name: String, age: Int)
}
