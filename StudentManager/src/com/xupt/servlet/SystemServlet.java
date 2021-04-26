package com.xupt.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.xupt.dao.AdminDao;
import com.xupt.dao.StudentDao;
import com.xupt.dao.TeacherDao;
import com.xupt.domain.Admin;
import com.xupt.domain.Student;
import com.xupt.domain.Teacher;

/**
 * @author ܰ
 *ϵͳ��¼���������
 */
public class SystemServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5126399054915547013L;
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws  IOException, ServletException {
		// TODO Auto-generated method stub
		doPost(req, resp);
	}

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws  IOException {
		// TODO Auto-generated method stub
		String method = req.getParameter("method");
		if ("toPersonalView".equals(method)){
			personalView(req,resp);
			return;
		}else if ("EditPasswod".equals(method)){
			editPassword(req,resp);
			return;
		}
		try {
			req.getRequestDispatcher("view/system.jsp").forward(req, resp);
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void editPassword(HttpServletRequest req, HttpServletResponse resp) {
		// TODO Auto-generated method stub
		String password = req.getParameter("password");
		String newPassword = req.getParameter("newpassword");
		resp.setCharacterEncoding("UTF-8");
		int userType = Integer.parseInt(req.getSession().getAttribute("userType").toString());
		if(userType == 1){
			//����Ա
			Admin admin = (Admin)req.getSession().getAttribute("user");
			if (!admin.getPassword().equals(password)){
				try {
					resp.getWriter().write("ԭ�������!");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			AdminDao adminDao = new AdminDao();
			if (adminDao.editPassword(admin, newPassword)){
				try {
					resp.getWriter().write("success");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else{
				try {
					resp.getWriter().write("���ݿ��޸Ĵ���!");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}finally{
					adminDao.closeCon();
				}
			}
		}
		if(userType == 2){
			//ѧ��
			Student student = (Student)req.getSession().getAttribute("user");
			if (!student.getPassword().equals(password)){
				try {
					resp.getWriter().write("ԭ�������!");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			StudentDao studentDao = new StudentDao();
			if (studentDao.editPassword(student, newPassword)){
				try {
					resp.getWriter().write("success");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else{
				try {
					resp.getWriter().write("���ݿ��޸Ĵ���!");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}finally{
					studentDao.closeCon();
				}
			}
		}
		
		if(userType == 3){
			//��ʦ
			Teacher teacher = (Teacher)req.getSession().getAttribute("user");
			if (!teacher.getPassword().equals(password)){
				try {
					resp.getWriter().write("ԭ�������!");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			TeacherDao teacherDao = new TeacherDao();
			if (teacherDao.editPassword(teacher, newPassword)){
				try {
					resp.getWriter().write("success");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else{
				try {
					resp.getWriter().write("���ݿ��޸Ĵ���!");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}finally{
					teacherDao.closeCon();
				}
			}
		}
	}

	private void personalView(HttpServletRequest req, HttpServletResponse resp) {
		// TODO Auto-generated method stub
		try {
			req.getRequestDispatcher("view/personalView.jsp").forward(req, resp);
		} catch (ServletException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
