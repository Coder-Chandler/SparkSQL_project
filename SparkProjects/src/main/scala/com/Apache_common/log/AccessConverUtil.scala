package com.Apache_common.log
import org.apache.spark.sql.Row
import org.apache.spark.sql.types._


/**
  *访问日志转换（输入 ==> 输出）工具类
  */
object AccessConverUtil {
  //定义的输出的字段
  val struct = StructType (
    Array (
      StructField("traffic",LongType),
      StructField("ip", StringType),
      StructField("city", StringType),
      StructField("time", StringType),
      StructField("day", StringType),
    )
  )

  /**
    * 根据输入的每一行转换成输出的样式
    * @param log 输入的每一行记录信息
    */
  def parseLog(log:String) = {

    try {
      val splits = log.split("\t")

      val traffic = splits(3).toLong
      val ip = splits(2)
      val city = ""
      val time = splits(0)
      val day = time.substring(0,10).replaceAll("-", "")

      Row(traffic, ip, city, time, day)
    } catch {
      case e: Exception => Row(0)
    }
  }
}
