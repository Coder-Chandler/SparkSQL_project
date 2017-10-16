package com.ApacheCommon.log

//每个url的PV和traffic统计的实体类
case class IpStatistics (day:String, ip:String, traffic_sums:Long, page_view :Long)

