package com.ApacheCommon.log

//每个url的PV和traffic统计的实体类
case class UrlPvTraffic (day:String, url:String, traffic_sums:Long, page_view :Long, avg:Double)


