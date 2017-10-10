package com.ApacheCommon.log
import org.apache.spark.sql.{SaveMode, SparkSession}

/**
  * 使用Spark完成我们的数据清洗工作
  */
object SparkStartCleanJob {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder().appName("SparkStartCleanJob")
      .master("local[2]").getOrCreate()

    //把我们第一步清洗的log读进来
    val accessRDD = spark.sparkContext.textFile("hdfs://localhost:8020/WEB_log/Apache_common/clean_data_1/part-00000")

    //导入隐式转换
    import spark.implicits._
    val accessDF = accessRDD.map(_.split("\t")).map(line => if(line.length==5) {
      Info(
      line(1), line(3).toLong, line(4).toInt, line(2), IpUtils.getCity(line(2)), line(0), line(0).substring(0,10).replaceAll("-", "")
    )} else {
      Info("", 0, 0, "", "", "", "")
    }).toDF()

    val accessDFclean = accessDF.filter("url!='' or ip!='' or city!='' or time!='' or day!=''")

    //测试用代码
    //查看缺失值
    //accessDFclean.filter("url=='' or ip=='' or city=='' or time=='' or day==''").show()
    //查看traffic正常的数据个数
    //println(accessDFclean.filter("traffic!=0").count())
    //accessDF.printSchema()
    //accessDF.show(false)

    accessDFclean.filter("traffic==0").coalesce(1).write.format("csv")
     .mode(SaveMode.Overwrite).partitionBy("day").save("/Users/chandler/Desktop/status_test")


    //以parquet的格式将清洗过的数据按照day分区存入HDFS里面去，注意coalesce表示输出为一个文件，这也是一个调优点
    //accessDF.coalesce(1).write.format("parquet").mode(SaveMode.Overwrite)
      //.partitionBy("day").save("/Users/chandler/Desktop/test/apache")

    spark.stop()
  }
  case class Info(url: String, traffic: Long, status: Int, ip: String, city: String, time: String, day: String)
}
