package com.xupt.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.Response;

import org.apache.jasper.tagplugins.jstl.core.ForEach;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.xupt.dao.ClazzDao;
import com.xupt.dao.StudentDao;
import com.xupt.domain.Clazz;
import com.xupt.domain.Page;
import com.xupt.domain.Student;
import com.xupt.util.SnGenerateUtil;

/**
 * @author 馨
 *学生信息管理功能实现
 */
public class StudentServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3240581016297702796L;
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws  IOException, ServletException {
		// TODO Auto-generated method stub
		doPost(req, resp);
	}

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws  IOException {
		// TODO Auto-generated method stub
		String method = req.getParameter("method");
		if ("toStudentListView".equals(method)){
			studentList(req,resp);
		}else if ("AddStudent".equals(method)){
			addStudent(req,resp);
		}else if ("StudentList".equals(method)){
			getStudentList(req,resp);
		}else if ("EditStudent".equals(method)){
			editStudent(req,resp);
		}else if ("DeleteStudent".equals(method)){
			deleteStudent(req,resp);
		}
		
	}

	private void deleteStudent(HttpServletRequest req, HttpServletResponse resp) {
		// TODO Auto-generated method stub
		String[] ids = req.getParameterValues("ids[]");
		String idStr = "";
		for (String id : ids){
			idStr += id + ",";
		}
		idStr = idStr.substring(0,idStr.length()-1);
		StudentDao studentDao = new StudentDao();
		if (studentDao.deleteStudent(idStr)){
			try {
				resp.getWriter().write("success");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
				studentDao.closeCon();
			}
		}
	}

	private void editStudent(HttpServletRequest req, HttpServletResponse resp) {
		// TODO Auto-generated method stub
		String name = req.getParameter("name");
		int id = Integer.parseInt(req.getParameter("id"));
		String sex = req.getParameter("sex");
		String mobile = req.getParameter("mobile");
		String qq = req.getParameter("qq");
		int clazzId = Integer.parseInt(req.getParameter("clazzid"));
		Student student = new Student();
		student.setClazzId(clazzId);
		student.setMobile(mobile);
		student.setName(name);
		student.setId(id);
		student.setQq(qq);
		student.setSex(sex);
		StudentDao studentDao = new StudentDao();
		if (studentDao.editStudent(student)){
			try {
				resp.getWriter().write("success");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void getStudentList(HttpServletRequest req, HttpServletResponse resp) {
		// TODO Auto-generated method stub
		String name = req.getParameter("studentName");
		Integer currentPage = req.getParameter("page") == null ? 1 : Integer.parseInt(req.getParameter("page"));
		Integer pageSize = req.getParameter("rows") == null ? 999 : Integer.parseInt(req.getParameter("rows"));
		Integer clazz = req.getParameter("clazzid") == null ? 0 : Integer.parseInt(req.getParameter("clazzid"));
		int userType = Integer.parseInt(req.getSession().getAttribute("userType").toString());
		Student student = new Student();
		student.setClazzId(clazz);
		student.setName(name);
		if (userType == 2){
			//学生登录后，只能查看自己的信息
			Student currentUser = (Student)req.getSession().getAttribute("user");
			student.setId(currentUser.getId());
		}
		StudentDao studentDao = new StudentDao();
		List<Student> studentList = studentDao.getStudentList(student , new Page(currentPage, pageSize));
		int total = studentDao.getStudentListTotal(student);
		studentDao.closeCon();
		resp.setCharacterEncoding("UTF-8");
		Map<String, Object> ret = new HashMap<String, Object>();
		ret.put("total",total);
		ret.put("rows",studentList);
		//JsonConfig jsonConfig = new JsonConfig();
		//String clazzListString = JSONArray.fromObject(clazzList,jsonConfig).toString();
		try {
			String from = req.getParameter("from");
			if("combox".equals(from)){
				resp.getWriter().write(JSONArray.fromObject(studentList).toString());
			}else{
				resp.getWriter().write(JSONObject.fromObject(ret).toString());
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void addStudent(HttpServletRequest req, HttpServletResponse resp) {
		// TODO Auto-generated method stub
		String name = req.getParameter("name");
		String password = req.getParameter("password");
		String sex = req.getParameter("sex");
		String mobile = req.getParameter("mobile");
		String qq = req.getParameter("qq");
		int clazzId = Integer.parseInt(req.getParameter("clazzid"));
		Student student = new Student();
		student.setClazzId(clazzId);
		student.setMobile(mobile);
		student.setName(name);
		student.setPassword(password);
		student.setQq(qq);
		student.setSex(sex);
		student.setSn(SnGenerateUtil.generateSn(clazzId));
		StudentDao studentDao = new StudentDao();
		if (studentDao.addStudent(student)){
			try {
				resp.getWriter().write("success");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
				studentDao.closeCon();
			}
		}
	}

	private void studentList(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		// TODO Auto-generated method stub
		try {
			req.getRequestDispatcher("view/studentList.jsp").forward(req, resp);
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
