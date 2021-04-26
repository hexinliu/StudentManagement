package com.xupt.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.xupt.domain.Page;
import com.xupt.domain.SelectedCourse;

/**
 * @author 馨
 *选课表数据库操作封装
 */
public class SelectedCourseDao extends BaseDao {

	public List<SelectedCourse> getSelectedCourseList(
			SelectedCourse selectedCourse, Page page) {
		// TODO Auto-generated method stub
		List<SelectedCourse> ret = new ArrayList<SelectedCourse>();
		String sql = "select * from s_selected_course ";
		if(selectedCourse.getStudentId() != 0){
			sql += " and student_id = " + selectedCourse.getStudentId();
		}
		if(selectedCourse.getCourseId() != 0){
			sql += " and course_id = " + selectedCourse.getCourseId();
		}
		sql += " limit " + page.getStart() + "," + page.getPageSize();
		sql = sql.replaceFirst("and", "where");
		ResultSet resultSet = query(sql);
		try {
			while(resultSet.next()){
				SelectedCourse cl = new SelectedCourse();
				cl.setId(resultSet.getInt("id"));
				cl.setCourseId(resultSet.getInt("course_id"));
				cl.setStudentId(resultSet.getInt("student_id"));
				ret.add(cl);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}

	public int getSelectedCourseListTotal(SelectedCourse selectedCourse) {
		// TODO Auto-generated method stub
		int  total = 0;
		String sql = "select count(*)as total from s_selected_course ";
		if(selectedCourse.getStudentId() != 0){
			sql += " and student_id = " + selectedCourse.getStudentId();
		}
		if (selectedCourse.getCourseId() != 0){
			sql += " and course_id = " + selectedCourse.getCourseId();
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

	public boolean addSelectedCourse(SelectedCourse selectedCourse) {
		// TODO Auto-generated method stub
		String sql = "insert into s_selected_course values(null,'"+selectedCourse.getStudentId()+"','"+selectedCourse.getCourseId()+"')";
		return update(sql);
	}

	public boolean deleteSelectedCourse(int id) {
		// TODO Auto-generated method stub
		String sql = "delete from s_selected_course where id = "+id;

		return update(sql);
	}

	//检查课程是否被选，避免重复选课
	public boolean isSelected(int studentId, int courseId){
		boolean flag = false;
		String sql = "select * from s_selected_course where student_id = " + studentId + " and course_id = "+courseId;
		ResultSet query = query(sql);
		try {
			if (query.next()){
				return true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return flag;
		
	}

	public SelectedCourse getSelectedCourse(int id) {
		// TODO Auto-generated method stub
		SelectedCourse ret = null;
		String sql = "select * from s_selected_course where id = " + id;
		ResultSet query = query(sql);
		try {
			if(query.next()){
				ret = new SelectedCourse();
				ret.setId(query.getInt("id"));
				ret.setCourseId(query.getInt("course_id"));
				ret.setStudentId(query.getInt("student_id"));
				return ret;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}
}
