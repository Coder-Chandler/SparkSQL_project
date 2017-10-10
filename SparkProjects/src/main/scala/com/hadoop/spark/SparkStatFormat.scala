package com.hadoop.spark
import org.apache.spark.sql.SparkSession
/**
  * 第一步清洗：抽取我们所需要的指定列的数据
  */
object SparkStatFormat {

  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder().appName("HiveMySQLApp")
      .master("local[2]").getOrCreate()

    val access = spark.sparkContext.textFile("hdfs://localhost:8020/WEB_log/Apache_common/access_2013_05_30.log")

    access.map(line => {
      val splits = line.split(" ")
      //判断分割后的每一行日志是否长度为10，目的为过滤掉缺少列数据的日志，防止写入数据过程报数组越界的错误
      if (splits.length == 10){
        val ip = splits(0)
        /**
          *原始日志的第三个和第四个字段拼接就是完整的访问时间：
          * [30/May/2013:17:38:20 +0800] ==> yyyy-MM-dd HH:mm:ss
          */
        val time = splits(3)+" "+splits(4)
        val time_format = DataUtils.parse(time)
        val url = splits(6)
        val status = splits(8).replaceAll(" ", "")
        val traffic = splits(9).replaceAll("-", "0")
        time_format + "\t" + url + "\t" + ip + "\t" + traffic + "\t" + status
      }
    }).saveAsTextFile("/Users/chandler/Desktop/test/clen_data_1")
    spark.stop()
  }
}
