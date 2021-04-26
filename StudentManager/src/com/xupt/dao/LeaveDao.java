package com.xupt.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.xupt.domain.Leave;
import com.xupt.domain.Page;


public class LeaveDao extends BaseDao {

	public boolean addLeave(Leave leave) {
		// TODO Auto-generated method stub
		String sql = "insert into s_leave values(null,"+leave.getStudentId()+",'"+leave.getInfo()+"',"+Leave.LEAVE_STATUS_WAIT+",'"+leave.getRemark()+"') ";

		return update(sql);
	}

	public List<Leave> getLeaveList(Leave leave, Page page) {
		// TODO Auto-generated method stub
		List<Leave> ret = new ArrayList<Leave>();
		String sql = "select * from s_leave ";
		if(leave.getStudentId() != 0){
			sql += "where student_id = "+leave.getStudentId()+"";
		}
		sql += " limit " + page.getStart() + "," + page.getPageSize();
		ResultSet resultSet = query(sql.replaceFirst("and", "where"));
		try {
			while(resultSet.next()){
				Leave cl = new Leave();
				cl.setId(resultSet.getInt("id"));
				cl.setStudentId(resultSet.getInt("student_id"));
				cl.setInfo(resultSet.getString("info"));
				cl.setStatus(resultSet.getInt("status"));
				cl.setRemark(resultSet.getString("remark"));
				ret.add(cl);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}

	public int getLeaveListTotal(Leave leave) {
		// TODO Auto-generated method stub
		int  total = 0;
		String sql = "select count(*) as total from s_leave ";
		if (leave.getStudentId() != 0){
			sql += "and student_id = " + leave.getStudentId();
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

	public boolean deleteLeave(Integer id) {
		// TODO Auto-generated method stub
		String sql = "delete from s_leave where id = "+id+"";

		return update(sql);
	}

	public boolean editLeave(Leave leave) {
		// TODO Auto-generated method stub
		String sql = "update s_leave set student_id = "+leave.getStudentId()+", info = '"+leave.getInfo()+"',status = "+leave.getStatus()+",remark = '"+leave.getRemark()+"' where id = " + leave.getId();
		return update(sql);
	}




	
	
}
