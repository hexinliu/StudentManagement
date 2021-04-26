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

import com.xupt.dao.StudentDao;
import com.xupt.dao.TeacherDao;
import com.xupt.domain.Page;
import com.xupt.domain.Student;
import com.xupt.domain.Teacher;
import com.xupt.util.SnGenerateUtil;

/**
 * @author 馨
 *教师信息管理Servlet
 */
public class TeacherServlet extends HttpServlet {
	
	

	private static final long serialVersionUID = 922146260287208971L;

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws  IOException, ServletException {
		// TODO Auto-generated method stub
		doPost(req, resp);
	}

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws  IOException {
		// TODO Auto-generated method stub
		String method = req.getParameter("method");
		if ("toTeacherListView".equals(method)){
			teacherList(req,resp);
		}else if ("AddTeacher".equals(method)){
			addTeacher(req,resp);
		}else if ("TeacherList".equals(method)){
			getTeacherList(req,resp);
		}else if ("EditTeacher".equals(method)){
			editTeacher(req,resp);
		}else if ("DeleteTeacher".equals(method)){
			deleteTeacher(req,resp);
		}
		
	}

	private void deleteTeacher(HttpServletRequest req, HttpServletResponse resp) {
		// TODO Auto-generated method stub
		String[] ids = req.getParameterValues("ids[]");
		String idStr = "";
		for (String id : ids){
			idStr += id + ",";
		}
		idStr = idStr.substring(0,idStr.length()-1);
		TeacherDao teacherDao = new TeacherDao();
		if (teacherDao.deleteTeacher(idStr)){
			try {
				resp.getWriter().write("success");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
				teacherDao.closeCon();
			}
		}
	}

	private void editTeacher(HttpServletRequest req, HttpServletResponse resp) {
		// TODO Auto-generated method stub
		String name = req.getParameter("name");
		int id = Integer.parseInt(req.getParameter("id"));
		String sex = req.getParameter("sex");
		String mobile = req.getParameter("mobile");
		String qq = req.getParameter("qq");
		int clazzId = Integer.parseInt(req.getParameter("clazzid"));
		Teacher teacher = new Teacher();
		teacher.setClazzId(clazzId);
		teacher.setMobile(mobile);
		teacher.setName(name);
		teacher.setId(id);
		teacher.setQq(qq);
		teacher.setSex(sex);
		TeacherDao teacherDao = new TeacherDao();
		if (teacherDao.editTeacher(teacher)){
			try {
				resp.getWriter().write("success");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void getTeacherList(HttpServletRequest req, HttpServletResponse resp) {
		// TODO Auto-generated method stub
		String name = req.getParameter("teacherName");
		Integer currentPage = req.getParameter("page") == null ? 1 : Integer.parseInt(req.getParameter("page"));
		Integer pageSize = req.getParameter("rows") == null ? 999 : Integer.parseInt(req.getParameter("rows"));
		Integer clazz = req.getParameter("clazzid") == null ? 0 : Integer.parseInt(req.getParameter("clazzid"));
		int userType = Integer.parseInt(req.getSession().getAttribute("userType").toString());
		Teacher teacher = new Teacher();
		teacher.setClazzId(clazz);
		teacher.setName(name);
		if (userType == 3){
			//教师登录后，只能查看自己的信息
			Teacher currentUser = (Teacher)req.getSession().getAttribute("user");
			teacher.setId(currentUser.getId());
		}
		TeacherDao teacherDao = new TeacherDao();
		List<Teacher> teacherList = teacherDao.getTeacherList(teacher , new Page(currentPage, pageSize));
		int total = teacherDao.getTeacherListTotal(teacher);
		teacherDao.closeCon();
		resp.setCharacterEncoding("UTF-8");
		Map<String, Object> ret = new HashMap<String, Object>();
		ret.put("total",total);
		ret.put("rows",teacherList);
		//JsonConfig jsonConfig = new JsonConfig();
		//String clazzListString = JSONArray.fromObject(clazzList,jsonConfig).toString();
		try {
			String from = req.getParameter("from");
			if("combox".equals(from)){
				resp.getWriter().write(JSONArray.fromObject(teacherList).toString());
			}else{
				resp.getWriter().write(JSONObject.fromObject(ret).toString());
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void addTeacher(HttpServletRequest req, HttpServletResponse resp) {
		// TODO Auto-generated method stub
		String name = req.getParameter("name");
		String password = req.getParameter("password");
		String sex = req.getParameter("sex");
		String mobile = req.getParameter("mobile");
		String qq = req.getParameter("qq");
		int clazzId = Integer.parseInt(req.getParameter("clazzid"));
		Teacher teacher = new Teacher();//批量替换：Alt+Shift+R
		teacher.setClazzId(clazzId);
		teacher.setMobile(mobile);
		teacher.setName(name);
		teacher.setPassword(password);
		teacher.setQq(qq);
		teacher.setSex(sex);
		teacher.setSn(SnGenerateUtil.generateTeacherSn(clazzId));
		TeacherDao teacherDao = new TeacherDao();
		if (teacherDao.addTeacher(teacher)){
			try {
				resp.getWriter().write("success");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
				teacherDao.closeCon();
			}
		}
	}

	private void teacherList(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		// TODO Auto-generated method stub
		try {
			req.getRequestDispatcher("view/teacherList.jsp").forward(req, resp);
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
