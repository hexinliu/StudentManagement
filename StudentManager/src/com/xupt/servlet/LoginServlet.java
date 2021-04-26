package com.xupt.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import jdk.nashorn.internal.ir.RuntimeNode.Request;








import com.xupt.dao.AdminDao;
import com.xupt.dao.StudentDao;
import com.xupt.dao.TeacherDao;
import com.xupt.domain.Admin;
import com.xupt.domain.Student;
import com.xupt.domain.Teacher;
import com.xupt.util.StringUtil;

/**
 * @author 馨
 *登录验证servlet
 */
public class LoginServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(req, resp);
	}

	
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String method = req.getParameter("method");
		if ("logout".equals(method)){
			logout(req, resp);
			return;
		}
		String vcode = req.getParameter("vcode");
		String name = req.getParameter("account");
		String password = req.getParameter("password");
		int type = Integer.parseInt(req.getParameter("type"));
		String loginCaptcha = req.getSession().getAttribute("loginCaptcha").toString();
		if (StringUtil.isEmpty(vcode)){
			resp.getWriter().write("vcode Error");
			return;
		}
		if (!vcode.toUpperCase().equals(loginCaptcha.toUpperCase())){
			resp.getWriter().write("vcode Error");
			return;
		}
		//验证码验证通过，对比用户名和密码是否正确
		String loginStatus = "loginFailed";
		switch (type) {
		case 1:{
			AdminDao adminDao = new AdminDao();
			Admin admin = adminDao.login(name, password);
			adminDao.closeCon();
			if(admin == null){
				resp.getWriter().write("loginError");
				return;
			}
			HttpSession session = req.getSession();
			session.setAttribute("user", admin);
			session.setAttribute("userType", type);
			loginStatus = "loginSuccess";
			break;
		}
		case 2:{
			StudentDao studentDao = new StudentDao();
			Student student = studentDao.login(name, password);
			studentDao.closeCon();
			if(student == null){
				resp.getWriter().write("loginError");
				return;
			}
			HttpSession session = req.getSession();
			session.setAttribute("user", student);
			session.setAttribute("userType", type);
			loginStatus = "loginSuccess";
			break;
		}
		
		case 3:{
			TeacherDao teacherDao = new TeacherDao();
			Teacher teacher = teacherDao.login(name, password);
			teacherDao.closeCon();
			if(teacher == null){
				resp.getWriter().write("loginError");
				return;
			}
			HttpSession session = req.getSession();
			session.setAttribute("user", teacher);
			session.setAttribute("userType", type);
			loginStatus = "loginSuccess";
			break;
		}
		default:
			break;
		}
		resp.getWriter().write(loginStatus);
		//用户名密码正确
		
	}
	private void logout(HttpServletRequest req, HttpServletResponse resp){
		req.getSession().removeAttribute("user");
		req.getSession().removeAttribute("userType");
		try {
			resp.sendRedirect("index.jsp");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}


