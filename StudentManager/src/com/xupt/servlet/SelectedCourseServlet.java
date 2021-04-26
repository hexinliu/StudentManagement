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

import com.xupt.dao.CourseDao;
import com.xupt.dao.SelectedCourseDao;
import com.xupt.domain.Page;
import com.xupt.domain.SelectedCourse;
import com.xupt.domain.Student;

public class SelectedCourseServlet extends HttpServlet {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1139889270203298506L;

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws  IOException, ServletException {
		// TODO Auto-generated method stub
		doPost(req, resp);
	}

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws  IOException {
		// TODO Auto-generated method stub
		String method = req.getParameter("method");
		if ("toSelectedCourseListView".equals(method)){
			selectedCourseList(req,resp);
		}else if ("SelectedCourseList".equals(method)){
			getSelectedCourseList(req,resp);
		}else if ("AddSelectedCourse".equals(method)){
			addSelectedCourse(req,resp);
		}else if ("DeleteSelectedCourse".equals(method)){
			deleteSelectedCourse(req,resp);
		}
	}

	private void deleteSelectedCourse(HttpServletRequest req,
			HttpServletResponse resp) throws IOException {
		// TODO Auto-generated method stub
		int id = Integer.parseInt(req.getParameter("id"));
		SelectedCourseDao selectedCourseDao = new SelectedCourseDao();
		SelectedCourse selectedCourse = selectedCourseDao.getSelectedCourse(id);
		String msg = "success";
		if(selectedCourse == null){
			msg = "not found";
			resp.getWriter().write(msg);
			selectedCourseDao.closeCon();
			return;
		}
		if(selectedCourseDao.deleteSelectedCourse(selectedCourse.getId())){
			CourseDao courseDao = new CourseDao();
			courseDao.updateCourseSelectedNum(selectedCourse.getCourseId(), -1);
			courseDao.closeCon();
		}else{
			msg = "error";
		}
		selectedCourseDao.closeCon();
		resp.getWriter().write(msg);
	}

	private void addSelectedCourse(HttpServletRequest req,
			HttpServletResponse resp) {
		// TODO Auto-generated method stub
		int studentId = req.getParameter("studentid") == null ? 0 : Integer.parseInt(req.getParameter("studentid").toString());
		int courseId = req.getParameter("courseid") == null ? 0 : Integer.parseInt(req.getParameter("courseid").toString());
		CourseDao courseDao = new CourseDao();
		String msg = "success";
		if (courseDao.isFull(courseId)){
			msg = "courseFull";
			try {
				resp.getWriter().write(msg);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
				courseDao.closeCon();
			}
			return;
		}
		SelectedCourseDao selectedCourseDao = new SelectedCourseDao();
		if (selectedCourseDao.isSelected(studentId, courseId)){
			msg = "courseSelected";
			try {
				resp.getWriter().write(msg);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
				selectedCourseDao.closeCon();
			}
			return;
		}
		SelectedCourse selectedCourse = new SelectedCourse();
		selectedCourse.setStudentId(studentId);
		selectedCourse.setCourseId(courseId);
		if (selectedCourseDao.addSelectedCourse(selectedCourse)){
			msg = "success";
		}
		courseDao.updateCourseSelectedNum(courseId, 1);
		courseDao.closeCon();
		selectedCourseDao.closeCon();
		try {
			resp.getWriter().write(msg);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void getSelectedCourseList(HttpServletRequest req,
			HttpServletResponse resp) {
		// TODO Auto-generated method stub
		Integer studentId = req.getParameter("studentid") == null ? 0 : Integer.parseInt(req.getParameter("studentid"));
		Integer currentPage = req.getParameter("page") == null ? 1 : Integer.parseInt(req.getParameter("page"));
		Integer pageSize = req.getParameter("rows") == null ? 999 : Integer.parseInt(req.getParameter("rows"));
		Integer courseId = req.getParameter("courseid") == null ? 0 : Integer.parseInt(req.getParameter("courseid"));
		SelectedCourse selectedCourse = new SelectedCourse();
		//获取当前用户类型
		int userType = Integer.parseInt(req.getSession().getAttribute("userType").toString());
		if (userType == 2){
			//如果是学生，只能查看自己的选课信息
			Student currentUser = (Student)req.getSession().getAttribute("user");
			studentId = currentUser.getId();
		}
		selectedCourse.setStudentId(studentId);
		selectedCourse.setCourseId(courseId);
		SelectedCourseDao selectedCourseDao = new SelectedCourseDao();
		List<SelectedCourse> selectedCourseList = selectedCourseDao.getSelectedCourseList(selectedCourse , new Page(currentPage, pageSize));
		int total = selectedCourseDao.getSelectedCourseListTotal(selectedCourse);
		selectedCourseDao.closeCon();
		resp.setCharacterEncoding("UTF-8");
		Map<String, Object> ret = new HashMap<String, Object>();
		ret.put("total",total);
		ret.put("rows",selectedCourseList);
		//JsonConfig jsonConfig = new JsonConfig();
		//String clazzListString = JSONArray.fromObject(clazzList,jsonConfig).toString();
		try {
			String from = req.getParameter("from");
			if("combox".equals(from)){
				resp.getWriter().write(JSONArray.fromObject(selectedCourseList).toString());
			}else{
				resp.getWriter().write(JSONObject.fromObject(ret).toString());
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void selectedCourseList(HttpServletRequest req,
			HttpServletResponse resp) {
		// TODO Auto-generated method stub
		try {
			req.getRequestDispatcher("view/selectedCourseList.jsp").forward(req, resp);
		} catch (ServletException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	} 
}
