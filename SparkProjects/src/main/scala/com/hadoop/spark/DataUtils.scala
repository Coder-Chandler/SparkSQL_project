package com.hadoop.spark
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.Date
import org.apache.commons.lang3.time.FastDateFormat
/**
  * 日期时间解析工具类
  */
object DataUtils {
  //输入文件日期时间格式
  //[30/May/2013:17:38:20 +0800]
  val YYYYMMDDHHMM_TIME_FORMAT = FastDateFormat.getInstance("dd/MMM/yyyy:HH:mm:ss Z", Locale.ENGLISH)

  //转换输入文件日期格式
  val TAGET_FORMAT = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss")
  /**
    *获取时间：yyyy-MM-dd HH:mm:ss
    */
  def parse(time:String) = {
    TAGET_FORMAT.format(new Date(getTime(time)))
  }
  /**
    * 获取输入日志时间：long类型
    */
  def getTime(time:String) = {
    try {
      YYYYMMDDHHMM_TIME_FORMAT.parse(time.substring(time.indexOf("[") + 1,
        time.lastIndexOf("]"))).getTime
    } catch {
      case e: Exception => {
        0l
      }
    }
  }

  def main(args: Array[String]): Unit = {
    println(parse("[30/May/2013:17:38:21 +0800]"))
  }
}
