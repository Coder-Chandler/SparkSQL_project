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

  /**
    * 批量保存AreaPvTraffic到mysql数据库
    * @param list
    */
  def insertDayCityPvTraffic(list: ListBuffer[AreaPvTraffic]): Unit = {

    var connection:Connection = null
    var pstmt:PreparedStatement = null

    try {

      connection = MySQLUtils.getConnection()

      connection.setAutoCommit(false) //把自动提交关闭改为手动提交

      val sql = "insert into url_city_statistics(day, url, city, traffic_sums, page_view, page_view_rank) " +
        "VALUES (?, ?, ?, ?, ?, ?) " +
        "ON DUPLICATE KEY UPDATE day=VALUES(day),url=VALUES(url), " +
        "city=VALUES(city), traffic_sums=VALUES(traffic_sums), " +
        "page_view=VALUES(page_view), page_view_rank=VALUES(page_view_rank)"
      pstmt = connection.prepareStatement(sql)

      for(element <- list) {
        pstmt.setString(1, element.day)
        pstmt.setString(2, element.url)
        pstmt.setString(3, element.city)
        pstmt.setLong(4, element.traffic_sums)
        pstmt.setLong(5, element.page_view)
        pstmt.setInt(6, element.page_view_rank)

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

  /**
    * 批量保存AreaPvTraffic到mysql数据库
    * @param list
    */
  def insertIpStatistics(list: ListBuffer[IpStatistics]): Unit = {

    var connection:Connection = null
    var pstmt:PreparedStatement = null

    try {

      connection = MySQLUtils.getConnection()

      connection.setAutoCommit(false) //把自动提交关闭改为手动提交

      val sql = "insert into ip_statistics(day, ip, traffic_sums, page_view) " +
        "VALUES (?, ?, ?, ?) " +
        "ON DUPLICATE KEY UPDATE day=VALUES(day),ip=VALUES(ip), " +
        "traffic_sums=VALUES(traffic_sums), page_view=VALUES(page_view)"
      pstmt = connection.prepareStatement(sql)

      for(element <- list) {
        pstmt.setString(1, element.day)
        pstmt.setString(2, element.ip)
        pstmt.setLong(3, element.traffic_sums)
        pstmt.setLong(4, element.page_view)

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
