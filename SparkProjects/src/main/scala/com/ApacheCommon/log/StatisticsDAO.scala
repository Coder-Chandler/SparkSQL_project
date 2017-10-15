package com.ApacheCommon.log

import java.sql.{Connection, PreparedStatement}

import scala.collection.mutable.ListBuffer

/**
  *各个需求统计的DAO操作
  */
object StatisticsDAO {

  /**
    * 批量保存UrlPvTraffic到mysql数据库
    * @param list
    */
  def insertDayUrlPvTraffic(list: ListBuffer[UrlPvTraffic]): Unit = {

    var connection:Connection = null
    var pstmt:PreparedStatement = null

    try {

      connection = MySQLUtils.getConnection()

      connection.setAutoCommit(false) //把自动提交关闭改为手动提交

      val sql = "insert into day_url_pv_traffic(day, url, traffic_sums, page_view, avg) " +
        "VALUES (?, ?, ?, ?, ?) " +
        "ON DUPLICATE KEY UPDATE day=VALUES(day),url=VALUES(url), " +
        "traffic_sums=VALUES(traffic_sums), page_view=VALUES(page_view), avg=VALUES(avg)"
      pstmt = connection.prepareStatement(sql)

      for(element <- list) {
        pstmt.setString(1, element.day)
        pstmt.setString(2, element.url)
        pstmt.setLong(3, element.traffic_sums)
        pstmt.setLong(4, element.page_view)
        pstmt.setDouble(5, element.avg)

        pstmt.addBatch()
      }

      pstmt.executeBatch() //执行批量处理（调优点）
      connection.commit() //手动提交
    } catch {
      case e: Exception => e.printStackTrace()
    } finally {
      MySQLUtils.release(connection, pstmt)
    }
  }
}
