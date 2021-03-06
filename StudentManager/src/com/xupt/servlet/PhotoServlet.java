package com.xupt.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileUploadException;

import com.lizhou.exception.FileFormatException;
import com.lizhou.exception.NullFileException;
import com.lizhou.exception.ProtocolException;
import com.lizhou.exception.SizeException;
import com.lizhou.fileload.FileUpload;
import com.xupt.dao.StudentDao;
import com.xupt.dao.TeacherDao;
import com.xupt.domain.Student;
import com.xupt.domain.Teacher;



/**
 * @author ܰ
 *ͼƬ??????servlet
 */
public class PhotoServlet extends HttpServlet {





	/**
	 * 
	 */
	private static final long serialVersionUID = 4526646239256076386L;

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws  IOException, ServletException {
		// TODO Auto-generated method stub
		doPost(req, resp);
	}

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws  IOException {
		// TODO Auto-generated method stub
		String method = req.getParameter("method");
		if ("getPhoto".equals(method)){
			getPhoto(req, resp);
		}else if("SetPhoto".equals(method)){
			uploadPhoto(req,resp);
		}
		
	}

	private void uploadPhoto(HttpServletRequest req, HttpServletResponse resp) {
		// TODO Auto-generated method stub
		int sid = req.getParameter("sid") == null ? 0 : Integer.parseInt(req.getParameter("sid"));
		int tid = req.getParameter("tid") == null ? 0 : Integer.parseInt(req.getParameter("tid"));
		FileUpload fileUpload = new FileUpload(req);
		fileUpload.setFileFormat("jpg");
		fileUpload.setFileFormat("png");
		fileUpload.setFileFormat("jpeg");
		fileUpload.setFileFormat("gif");
		fileUpload.setFileSize(2048);
		resp.setCharacterEncoding("UTF-8");
		try {
			InputStream uploadInputStream = fileUpload.getUploadInputStream();
			if(sid != 0){
				Student student = new Student();
				student.setId(sid);
				student.setPhoto(uploadInputStream);
				StudentDao studentDao = new StudentDao();
				if(studentDao.setStudentPhoto(student)){
					resp.getWriter().write("<div id='message'>?ϴ??ɹ???</div>");
				}else{
					resp.getWriter().write("<div id='message'>?ϴ?ʧ?ܣ?</div>");
				}
			}
			if(tid != 0){
				Teacher teacher = new Teacher();
				teacher.setId(tid);
				teacher.setPhoto(uploadInputStream);
				TeacherDao teacherDao = new TeacherDao();
				if(teacherDao.setTeacherPhoto(teacher)){
					resp.getWriter().write("<div id='message'>?ϴ??ɹ???</div>");
				}else{
					resp.getWriter().write("<div id='message'>?ϴ?ʧ?ܣ?</div>");
				}
			}
		} catch (ProtocolException e) {
			// TODO Auto-generated catch block
			try {
				resp.getWriter().write("<div id='message'>?ϴ?Э????????</div>");
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			e.printStackTrace();
		}catch (NullFileException e1) {
			// TODO: handle exception
			try {
				resp.getWriter().write("<div id='message'>?ϴ????ļ?Ϊ??!</div>");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			e1.printStackTrace();
		}
		catch (SizeException e2) {
			// TODO: handle exception
			try {
				resp.getWriter().write("<div id='message'>?ϴ??ļ???С???ܳ???"+fileUpload.getFileSize()+"??</div>");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			e2.printStackTrace();
		}
		catch (IOException e3) {
			// TODO: handle exception
			try {
				resp.getWriter().write("<div id='message'>??ȡ?ļ???????</div>");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			e3.printStackTrace();
		}
		catch (FileFormatException e4) {
			// TODO: handle exception
			try {
				resp.getWriter().write("<div id='message'>?ϴ??ļ???ʽ????ȷ?????ϴ? "+fileUpload.getFileFormat()+" ??ʽ???ļ???</div>");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			e4.printStackTrace();
		}
		catch (FileUploadException e5) {
			// TODO: handle exception
			try {
				resp.getWriter().write("<div id='message'>?ϴ??ļ?ʧ?ܣ?</div>");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			e5.printStackTrace();
		}
	}

	private void getPhoto(HttpServletRequest req, HttpServletResponse resp) {
		// TODO Auto-generated method stub
		int sid = req.getParameter("sid") == null ? 0 : Integer.parseInt(req.getParameter("sid"));
		int tid = req.getParameter("tid") == null ? 0 : Integer.parseInt(req.getParameter("tid"));
		if(sid != 0){
			//ѧ??
			StudentDao studentDao = new StudentDao();
			Student student = studentDao.getStudent(sid);
			studentDao.closeCon();
			if(student != null){
				InputStream photo = student.getPhoto();
				if(photo != null){
					try {
						byte[] b = new byte[photo.available()];
						photo.read(b);
						resp.getOutputStream().write(b,0,b.length);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					return;
				}
			}
		}
		if(tid != 0){
			//??ʦ
			TeacherDao teacherDao = new TeacherDao();
			Teacher teacher = teacherDao.getTeacher(tid);
			teacherDao.closeCon();
			if(teacher != null){
				InputStream photo = teacher.getPhoto();
				if(photo != null){
					try {
						byte[] b = new byte[photo.available()];
						photo.read(b);
						resp.getOutputStream().write(b,0,b.length);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					return;
				}
			}
		}
		String path = req.getSession().getServletContext().getRealPath("");
		File file = new File(path+"\\image\\533.jpg");
		try {
			FileInputStream fis = new FileInputStream(file);
			byte[] b = new byte[fis.available()];
			fis.read(b);
			resp.getOutputStream().write(b,0,b.length);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
