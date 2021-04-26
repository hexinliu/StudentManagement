package com.xupt.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.xupt.dao.LeaveDao;
import com.xupt.domain.Leave;
import com.xupt.domain.Page;
import com.xupt.domain.Student;


public class LeaveServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2079367787897493516L;

	
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws  IOException, ServletException {
		// TODO Auto-generated method stub
		doPost(req, resp);
	}

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws  IOException {
		// TODO Auto-generated method stub
		String method = req.getParameter("method");
		if ("toLeaveListView".equals(method)){
			leaveList(req,resp);
		}else if ("LeaveList".equals(method)){
			getLeaveList(req,resp);
		}else if ("AddLeave".equals(method)){
			addLeave(req,resp);
		}else if ("EditLeave".equals(method)){
			editLeave(req,resp);
		}else if ("DeleteLeave".equals(method)){
			deleteLeave(req,resp);
		}else if ("CheckLeave".equals(method)){
			checkLeave(req,resp);
		}
	}

	private void checkLeave(HttpServletRequest req, HttpServletResponse resp) {
		// TODO Auto-generated method stub
		int studentId = Integer.parseInt(req.getParameter("studentid"));
		int id = Integer.parseInt(req.getParameter("id"));
		int status = Integer.parseInt(req.getParameter("status"));
		String info = req.getParameter("info");
		String remark = req.getParameter("remark");
		Leave leave = new Leave();
		leave.setStudentId(studentId);
		leave.setInfo(info);
		leave.setRemark(remark);
		leave.setStatus(status);
		leave.setId(id);
		LeaveDao leaveDao = new LeaveDao();
		String msg = "error";
		if(leaveDao.editLeave(leave)){
			msg = "success";
		}
		try {
			resp.getWriter().write(msg);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			leaveDao.closeCon();
		}
	}

	private void editLeave(HttpServletRequest req, HttpServletResponse resp) {
		// TODO Auto-generated method stub
		int studentId = Integer.parseInt(req.getParameter("studentid"));
		int id = Integer.parseInt(req.getParameter("id"));
		String info = req.getParameter("info");
		Leave leave = new Leave();
		leave.setStudentId(studentId);
		leave.setInfo(info);
		leave.setRemark("");
		leave.setStatus(Leave.LEAVE_STATUS_WAIT);
		leave.setId(id);
		LeaveDao leaveDao = new LeaveDao();
		String msg = "error";
		if(leaveDao.editLeave(leave)){
			msg = "success";
		}
		try {
			resp.getWriter().write(msg);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			leaveDao.closeCon();
		}
	}

	private void deleteLeave(HttpServletRequest req, HttpServletResponse resp) {
		// TODO Auto-generated method stub
		Integer id = Integer.parseInt(req.getParameter("id"));
		LeaveDao leaveDao = new LeaveDao();
		if (leaveDao.deleteLeave(id)){
			try {
				resp.getWriter().write("success");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
				leaveDao.closeCon();
			}
		}
	}

	private void getLeaveList(HttpServletRequest req, HttpServletResponse resp) {
		// TODO Auto-generated method stub
		Integer studentId = req.getParameter("studentid") == null ? 0 : Integer.parseInt(req.getParameter("studentid"));
		//Integer currentPage = Integer.parseInt(req.getParameter("page"));
		//Integer pageSize = Integer.parseInt(req.getParameter("rows"));
		Integer currentPage = req.getParameter("page") == null ? 1 : Integer.parseInt(req.getParameter("page"));
		Integer pageSize = req.getParameter("rows") == null ? 999 : Integer.parseInt(req.getParameter("rows"));
		Leave leave = new Leave();
		//获取当前登录用户类型
		int userType = Integer.parseInt(req.getSession().getAttribute("userType").toString());
		if(userType == 2){
			//如果是学生，只能查看自己的信息
			Student currentUser = (Student)req.getSession().getAttribute("user");
			studentId = currentUser.getId();
		}
		leave.setStudentId(studentId);
		LeaveDao leaveDao = new LeaveDao();
		List<Leave> leaveList = leaveDao.getLeaveList(leave, new Page(currentPage, pageSize));
		int total = leaveDao.getLeaveListTotal(leave);
		leaveDao.closeCon();
		resp.setCharacterEncoding("UTF-8");
		Map<String, Object> ret = new HashMap<String, Object>();
		ret.put("total",total);
		ret.put("rows",leaveList);
		//JsonConfig jsonConfig = new JsonConfig();
		//String clazzListString = JSONArray.fromObject(clazzList,jsonConfig).toString();
		try {
			String from = req.getParameter("from");
			if("combox".equals(from)){
				resp.getWriter().write(JSONArray.fromObject(leaveList).toString());
			}else{
				resp.getWriter().write(JSONObject.fromObject(ret).toString());
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void addLeave(HttpServletRequest req, HttpServletResponse resp) {
		// TODO Auto-generated method stub
		int studentId = req.getParameter("studentid") == null ? 0 : Integer.parseInt(req.getParameter("studentid").toString());
		String info = req.getParameter("info");
		Leave leave = new Leave();
		leave.setStudentId(studentId);
		leave.setInfo(info);
		leave.setRemark("");
		LeaveDao leaveDao = new LeaveDao();
		String msg = "error";
		if (leaveDao.addLeave(leave)){
			msg = "success";
		}
		try {
			resp.getWriter().write(msg);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			leaveDao.closeCon();
		}
	}

	private void leaveList(HttpServletRequest req, HttpServletResponse resp) {
		// TODO Auto-generated method stub
		try {
			req.getRequestDispatcher("view/leaveList.jsp").forward(req, resp);
		} catch (ServletException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
