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
import com.xupt.domain.Course;
import com.xupt.domain.Page;

public class CourseServlet extends HttpServlet {
	

	private static final long serialVersionUID = -3710629048046467108L;

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws  IOException, ServletException {
		// TODO Auto-generated method stub
		doPost(req, resp);
	}

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws  IOException {
		// TODO Auto-generated method stub
		String method = req.getParameter("method");
		if ("toCourseListView".equals(method)){
			courseListView(req,resp);
		}else if ("AddCourse".equals(method)){
			addCourse(req,resp);
		}else if ("getCourseList".equals(method)){
			getCourseList(req,resp);
		}else if ("EditCourse".equals(method)){
			editCourse(req,resp);
		}else if ("DeleteCourse".equals(method)){
			deleteCourse(req,resp);
		}
	}

	private void deleteCourse(HttpServletRequest req, HttpServletResponse resp) {
		// TODO Auto-generated method stub
		String[] ids = req.getParameterValues("ids[]");
		String idStr = "";
		for (String id : ids){
			idStr += id + ",";
		}
		idStr = idStr.substring(0,idStr.length()-1);
		CourseDao courseDao = new CourseDao();
		if (courseDao.deleteCourse(idStr)){
			try {
				resp.getWriter().write("success");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
				courseDao.closeCon();
			}
		}
	}

	private void editCourse(HttpServletRequest req, HttpServletResponse resp) {
		// TODO Auto-generated method stub
		Integer id = Integer.parseInt(req.getParameter("id"));
		String name = req.getParameter("name");
		Integer teacherId = Integer.parseInt(req.getParameter("teacherid"));
		String courseDate = req.getParameter("course_date");
		Integer maxNum = Integer.parseInt(req.getParameter("max_num"));
		String info = req.getParameter("info");
		Course course = new Course();
		course.setName(name);
		course.setTeacherId(teacherId);
		course.setCourseDate(courseDate);
		course.setMaxNum(maxNum);
		course.setInfo(info);
		course.setId(id);
		CourseDao courseDao = new CourseDao();
		if (courseDao.editCourse(course)){
			try {
				resp.getWriter().write("success");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
				courseDao.closeCon();
			}
		}
	}

	private void getCourseList(HttpServletRequest req, HttpServletResponse resp) {
		// TODO Auto-generated method stub
		String name = req.getParameter("name");
		Integer currentPage = req.getParameter("page") == null ? 1 : Integer.parseInt(req.getParameter("page"));
		Integer pageSize = req.getParameter("rows") == null ? 999 : Integer.parseInt(req.getParameter("rows"));
		Integer teacherId = req.getParameter("teacherid") == null ? 0 : Integer.parseInt(req.getParameter("teacherid"));
		Course course = new Course();
		course.setName(name);
		course.setTeacherId(teacherId);
		CourseDao courseDao = new CourseDao();
		List<Course> courseList = courseDao.getCourseList(course , new Page(currentPage, pageSize));
		int total = courseDao.getCourseListTotal(course);
		courseDao.closeCon();
		resp.setCharacterEncoding("UTF-8");
		Map<String, Object> ret = new HashMap<String, Object>();
		ret.put("total",total);
		ret.put("rows",courseList);
		//JsonConfig jsonConfig = new JsonConfig();
		//String clazzListString = JSONArray.fromObject(clazzList,jsonConfig).toString();
		try {
			String from = req.getParameter("from");
			if("combox".equals(from)){
				resp.getWriter().write(JSONArray.fromObject(courseList).toString());
			}else{
				resp.getWriter().write(JSONObject.fromObject(ret).toString());
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void addCourse(HttpServletRequest req, HttpServletResponse resp) {
		// TODO Auto-generated method stub
		String name = req.getParameter("name");
		int teacherId = Integer.parseInt(req.getParameter("teacherid"));
		String courseDate = req.getParameter("course_date");
		int maxNum = Integer.parseInt(req.getParameter("maxnum"));
		String info = req.getParameter("info");
		Course course = new Course();
		course.setName(name);
		course.setTeacherId(teacherId);
		course.setCourseDate(courseDate);
		course.setMaxNum(maxNum);
		course.setInfo(info);
		CourseDao courseDao = new CourseDao();
		if (courseDao.addCourse(course)){
			try {
				resp.getWriter().write("success");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
				courseDao.closeCon();
			}
		}
	}

	private void courseListView(HttpServletRequest req, HttpServletResponse resp) {
		// TODO Auto-generated method stub
		try {
			req.getRequestDispatcher("view/courseList.jsp").forward(req, resp);
		} catch (ServletException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
