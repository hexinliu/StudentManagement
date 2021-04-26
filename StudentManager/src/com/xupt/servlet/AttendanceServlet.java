package com.xupt.servlet;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.xupt.dao.AttendanceDao;
import com.xupt.dao.CourseDao;
import com.xupt.dao.SelectedCourseDao;
import com.xupt.domain.Attendance;
import com.xupt.domain.Course;
import com.xupt.domain.Page;
import com.xupt.domain.SelectedCourse;
import com.xupt.domain.Student;
import com.xupt.util.DateFormatUtil;

public class AttendanceServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2893910291530654452L;

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws  IOException, ServletException {
		// TODO Auto-generated method stub
		doPost(req, resp);
	}

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws  IOException {
		// TODO Auto-generated method stub
		String method = req.getParameter("method");
		if ("toAttendanceListView".equals(method)){
			attendanceList(req,resp);
		}else if ("getStudentSelectedCourseList".equals(method)){
			getStudentSelectedCourseList(req,resp);
		}else if ("AddAttendance".equals(method)){
			addAttendance(req,resp);
		}else if ("AttendanceList".equals(method)){
			getAttendanceList(req,resp);
		}else if ("DeleteAttendance".equals(method)){
			deleteAttendance(req,resp);
		}
		
	}



	private void deleteAttendance(HttpServletRequest req,
			HttpServletResponse resp) throws IOException {
		// TODO Auto-generated method stub
		int id = Integer.parseInt(req.getParameter("id"));
		AttendanceDao attendanceDao = new AttendanceDao();
		String msg = "success";
		if(!attendanceDao.deleteAttendance(id)){
			msg = "error";
		}
		attendanceDao.closeCon();
		resp.getWriter().write(msg);
	}

	private void getAttendanceList(HttpServletRequest req,
			HttpServletResponse resp) {
		// TODO Auto-generated method stub
		Integer studentId = req.getParameter("studentid") == null ? 0 : Integer.parseInt(req.getParameter("studentid"));
		Integer currentPage = req.getParameter("page") == null ? 1 : Integer.parseInt(req.getParameter("page"));
		Integer pageSize = req.getParameter("rows") == null ? 999 : Integer.parseInt(req.getParameter("rows"));
		Integer courseId = req.getParameter("courseid") == null ? 0 : Integer.parseInt(req.getParameter("courseid"));
		String type = req.getParameter("type");
		String date = req.getParameter("date");
		//获取当前用户类型
		int userType = Integer.parseInt(req.getSession().getAttribute("userType").toString());
		if (userType == 2){
			//如果是学生，只能查看自己的考勤信息
			Student currentUser = (Student)req.getSession().getAttribute("user");
			studentId = currentUser.getId();
		}
		Attendance attendance = new Attendance();
		attendance.setStudentId(studentId);
		attendance.setCourseId(courseId);
		attendance.setDate(date);
		attendance.setType(type);
		AttendanceDao attendanceDao = new AttendanceDao();
		List<Attendance> attendanceList = attendanceDao.getAttendanceList(attendance, new Page(currentPage, pageSize));
		int total = attendanceDao.getAttendanceListTotal(attendance);
		attendanceDao.closeCon();
		resp.setCharacterEncoding("UTF-8");
		HashMap<String, Object> ret = new HashMap<String, Object>();
		ret.put("total", total);
		ret.put("rows", attendanceList);
		try {
			String from = req.getParameter("from");
			if("combox".equals(from)){
				resp.getWriter().write(JSONArray.fromObject(attendanceList).toString());
			}else{
				resp.getWriter().write(JSONObject.fromObject(ret).toString());
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private void addAttendance(HttpServletRequest req, HttpServletResponse resp) {
		// TODO Auto-generated method stub
		int studentId = req.getParameter("studentid") == null ? 0 : Integer.parseInt(req.getParameter("studentid").toString());
		int courseId = req.getParameter("courseid") == null ? 0 : Integer.parseInt(req.getParameter("courseid").toString());
		String type = req.getParameter("type").toString();
		Attendance attendance = new Attendance();
		attendance.setStudentId(studentId);
		attendance.setCourseId(courseId);
		attendance.setType(type);
		attendance.setDate(DateFormatUtil.getFormatDate(new Date(), "yyyy-MM-dd"));
		AttendanceDao attendanceDao = new AttendanceDao();
		String msg = "success";
		resp.setCharacterEncoding("UTF-8");
		if (attendanceDao.isAttendanced(studentId, courseId, type, DateFormatUtil.getFormatDate(new Date(), "yyyy-MM-dd"))){
			msg = "已签到，请勿重复签到！";
		}else if (!attendanceDao.addAttendance(attendance)){
			msg = "系统内部出错，请联系管理员！";
		}
		try {
			resp.getWriter().write(msg);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			attendanceDao.closeCon();
		}
	}

	private void getStudentSelectedCourseList(HttpServletRequest req,
			HttpServletResponse resp) {
		// TODO Auto-generated method stub
		Integer studentId = req.getParameter("student_id") == null ? 0 : Integer.parseInt(req.getParameter("student_id"));
		Integer currentPage = req.getParameter("page") == null ? 1 : Integer.parseInt(req.getParameter("page"));
		Integer pageSize = req.getParameter("rows") == null ? 999 : Integer.parseInt(req.getParameter("rows"));
		SelectedCourse selectedCourse = new SelectedCourse();
		selectedCourse.setStudentId(studentId);
		SelectedCourseDao selectedCourseDao = new SelectedCourseDao();
		List<SelectedCourse> selectedCourseList = selectedCourseDao.getSelectedCourseList(selectedCourse , new Page(currentPage, pageSize));
		selectedCourseDao.closeCon();
		String courseId  = "";
		for (SelectedCourse sc : selectedCourseList){
			courseId += sc.getCourseId() + ",";
		}
		courseId = courseId.substring(0,courseId.length()-1);//去掉最后一个多余的逗号
		CourseDao courseDao = new CourseDao();
		List<Course> courseList = courseDao.getCourse(courseId);
		courseDao.closeCon();
		resp.setCharacterEncoding("UTF-8");
		try {
			resp.getWriter().write(JSONArray.fromObject(courseList).toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private void attendanceList(HttpServletRequest req, HttpServletResponse resp) {
		// TODO Auto-generated method stub
		try {
			req.getRequestDispatcher("view/attendanceList.jsp").forward(req, resp);
		} catch (ServletException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
