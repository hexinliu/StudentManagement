package com.xupt.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;



import com.xupt.util.DbUtil;

/**
 * @author 馨
 *基础dao，封装基础操作
 */
public class BaseDao {
	private DbUtil dbUtil = new DbUtil();
	
	public void closeCon(){
		dbUtil.closeCon();
	}
	//查询
	public ResultSet query(String sql){
		try {
			PreparedStatement prepareStatement = dbUtil.getConnection().prepareStatement(sql);
			return prepareStatement.executeQuery();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	//修改
	public boolean update(String sql){
		try {
			return dbUtil.getConnection().prepareStatement(sql).executeUpdate() > 0;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	public Connection getConnection(){
		return dbUtil.getConnection();
	}
}
