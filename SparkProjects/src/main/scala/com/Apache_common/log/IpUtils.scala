package com.Apache_common.log
import com.ggstar.util.ip.IpHelper

/**
  * 解析IP工具类
  */
object IpUtils {
  def getCity(ip:String) = {
    IpHelper.findRegionByIp(ip)

  }

  def main(args: Array[String]): Unit = {
    println(getCity("61.150.43.40"))
  }

}
