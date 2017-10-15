package com.ApacheCommon.log

import java.sql.{Connection, DriverManager, PreparedStatement}


/**
  * MySQL操作工具类
  */
object MySQLUtils {
  /**
    * 获取数据库连接
    */
  def getConnection() = {
    DriverManager.getConnection("jdbc:mysql://localhost:3306/ApacheCommon", "root", "chuchu910408")
  }

  /**
    * 释放MySQL数据库连接等资源
    * @param connection
    * @param pstmt
    */
  def release(connection: Connection, pstmt: PreparedStatement): Unit = {
    try {
      if(pstmt != null) {
        pstmt.close()
      }
    } catch {
      case e: Exception => e.printStackTrace()
    } finally {
      if(connection != null) {
        connection.close()
      }
    }
  }

  def main(args: Array[String]): Unit = {
    println(getConnection())
  }
}
