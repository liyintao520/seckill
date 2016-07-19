package org.seckill.test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Demo {
	public static String select() {
		String sql = "select seckill_id from seckill ";
		Connection conn = DBConnection.getConn();// 此处为通过自己写的方法getConn()获得连接
		String str = "";
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);

			if (rs.next()) {
				int m1 = rs.getInt(1);// 或者为rs.getString(1)，根据数据库中列的值类型确定，参数为第一列
				str = rs.getString(1);
			}
			// 可以将查找到的值写入类，然后返回相应的对象
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return str;
	}
	public static void main(String[] args) {
		System.out.println("-----------"+ select());
	}
}