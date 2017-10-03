package com.hadoop.spark
import org.apache.spark.sql.SparkSession
/**
  * DataFrame中的操作(使用第一种反射的方式)
  */
object DataFrameCase {
  def main(args: Array[String]): Unit = {

    val spark = SparkSession.builder().appName("DataFrameRDDApp").master("local[2]").getOrCreate()

    //RDD ==> DataFrame
    val rdd = spark.sparkContext.textFile("file:///Users/chandler/Documents/Projects/SparkProjects/student.data")

    //导入隐式转换
    import spark.implicits._
    val studentdataframe = rdd.map(_.split("\\|")).map(line => Student(line(0).toInt, line(1), line(2), line(3))).toDF()

    //显示前30条数据
    studentdataframe.show(23, false)

    //我们筛选出名字是NULL和没有名字的数据
    studentdataframe.filter("name='' or name='NULL'").show(false)

    //name以B开头的人
    studentdataframe.filter("SUBSTR(name,0,1)='B'").show(false)

    //按照名字升序排序
    studentdataframe.sort(studentdataframe("name")).show(23, false)

    //按照名字倒序排序
    studentdataframe.sort(studentdataframe("name").desc).show(23, false)

    //如果name完全相同的按照id升序排序（这个数据里面只有两个没有名字的记录完全相同）
    studentdataframe.sort("name", "id").show(23, false)

    //按照name升序id降序排序
    studentdataframe.sort(studentdataframe("name").asc, studentdataframe("id").desc).show(23, false)

    //更改列名
    studentdataframe.select(studentdataframe("name").as("student_name")).show(23, false)

    //进行join操作（作为测试让这个表自己和自己join）
    val studentdataframe_test = rdd.map(_.split("\\|")).map(line => Student(line(0).toInt, line(1), line(2), line(3))).toDF()
    studentdataframe.join(studentdataframe_test, studentdataframe.col("id") === studentdataframe_test.col("id")).show(23, false)

    spark.stop()
  }
  case class Student(id: Int, name: String, phone: String, email: String)
}
