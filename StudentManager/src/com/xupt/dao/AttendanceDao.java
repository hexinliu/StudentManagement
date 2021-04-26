package com.xupt.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.xupt.domain.Attendance;
import com.xupt.domain.Page;
import com.xupt.util.StringUtil;

public class AttendanceDao extends BaseDao {

	public boolean addAttendance(Attendance attendance) {
		// TODO Auto-generated method stub
		String sql = "insert into s_attendance values(null,'"+attendance.getStudentId()+"','"+attendance.getCourseId()+"','"+attendance.getType()+"','"+attendance.getDate()+"')";
		return update(sql);
	}
	/*判断是否已签到*/
	public boolean isAttendanced(int studentId,int courseId,String type,String date){
		boolean ret = false;
		String sql = "select * from s_attendance where student_id = " + studentId + " and course_id = " + courseId + " and type = '"+type+"' and date = '"+date+"'";
		ResultSet query = query(sql);
		try {
			if (query.next()){
				return true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}
	/*获取签到列表*/
	public List<Attendance> getAttendanceList(
			Attendance attendance, Page page) {
		// TODO Auto-generated method stub
		List<Attendance> ret = new ArrayList<Attendance>();
		String sql = "select * from s_attendance ";
		if(attendance.getStudentId() != 0){
			sql += " and student_id = " + attendance.getStudentId();
		}
		if(attendance.getCourseId() != 0){
			sql += " and course_id = " + attendance.getCourseId();
		}
		if(!StringUtil.isEmpty(attendance.getType())){
			sql += " and type = '" + attendance.getType() + "'";
		}
		if(!StringUtil.isEmpty(attendance.getDate())){
			sql += " and date = '" + attendance.getDate() + "'";
		}
		sql += " limit " + page.getStart() + "," + page.getPageSize();
		sql = sql.replaceFirst("and", "where");
		ResultSet resultSet = query(sql);
		try {
			while(resultSet.next()){
				Attendance cl = new Attendance();
				cl.setId(resultSet.getInt("id"));
				cl.setCourseId(resultSet.getInt("course_id"));
				cl.setStudentId(resultSet.getInt("student_id"));
				cl.setType(resultSet.getString("type"));
				cl.setDate(resultSet.getString("date"));
				ret.add(cl);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}

	public int getAttendanceListTotal(Attendance attendance) {
		// TODO Auto-generated method stub
		int  total = 0;
		String sql = "select count(*)as total from s_attendance ";
		if(attendance.getStudentId() != 0){
			sql += " and student_id = " + attendance.getStudentId();
		}
		if (attendance.getCourseId() != 0){
			sql += " and course_id = " + attendance.getCourseId();
		}
		ResultSet resultSet = query(sql.replaceFirst("and", "where"));
		try {
			while(resultSet.next()){
				total = resultSet.getInt("total");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return total;
	}
	public boolean deleteAttendance(int id) {
		// TODO Auto-generated method stub
		String sql = "delete from s_attendance where id = "+id;

		return update(sql);
	}
}
