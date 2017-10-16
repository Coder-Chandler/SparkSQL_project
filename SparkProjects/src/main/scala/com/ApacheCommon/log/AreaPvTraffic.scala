package com.ApacheCommon.log

//每个url的PV和traffic统计的实体类
case class AreaPvTraffic (day:String, url:String, city:String,
                          traffic_sums:Long, page_view :Long, page_view_rank:Int)


