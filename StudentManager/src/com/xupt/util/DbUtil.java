package com.xupt.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author ܰ
 *���ݿ�������
 */
public class DbUtil {
	private String dbUrl = "jdbc:mysql://localhost:3306/db_student_manager?useUnicode=true&characterEncoding=utf8";
	private String dbUser = "root";
	private String dbPassword = "123456";
	private String jdbcName = "com.mysql.jdbc.Driver";
	private Connection connection = null;
	public Connection getConnection(){
		
		try {
			Class.forName(jdbcName);
			connection = DriverManager.getConnection(dbUrl,dbUser,dbPassword);
			System.out.println("���ݿ����ӳɹ���");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("���ݿ�����ʧ�ܣ�");
			e.printStackTrace();
		}
		
		return connection;
	}
	
	public void closeCon(){
		if (connection != null)
			try {
				connection.close();
				System.out.println("���ݿ������ѹر�");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
	public static void main(String[] args){
		DbUtil dbUtil = new DbUtil();
		dbUtil.getConnection();
	}
}