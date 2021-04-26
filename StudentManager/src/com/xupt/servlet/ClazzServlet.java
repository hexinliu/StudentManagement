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

import com.xupt.dao.ClazzDao;
import com.xupt.domain.Clazz;
import com.xupt.domain.Page;

/**
 * @author 馨
 *班级信息管理功能
 */
public class ClazzServlet extends HttpServlet {

	

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws  IOException, ServletException {
		// TODO Auto-generated method stub
		doPost(req, resp);
	}

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws  IOException {
		// TODO Auto-generated method stub
		String method = req.getParameter("method");
		if ("toClazzListView".equals(method)){
			clazzList(req,resp);
		}else if("getClazzList".equals(method)){
			getClazzList(req, resp);
		}else if("AddClazz".equals(method)){
			addClazz(req, resp);
		}else if ("DeleteClazz".equals(method)){
			deleteClazz(req, resp);
		}else if ("EditClazz".equals(method)){
			editClazz(req, resp);
		}
	}

	private void editClazz(HttpServletRequest req, HttpServletResponse resp) {
		// TODO Auto-generated method stub
		Integer id = Integer.parseInt(req.getParameter("id"));
		String name = req.getParameter("name");
		String info = req.getParameter("info");
		Clazz clazz = new Clazz();
		clazz.setName(name);
		clazz.setInfo(info);
		clazz.setId(id);
		ClazzDao clazzDao = new ClazzDao();
		if (clazzDao.editClazz(clazz)){
			try {
				resp.getWriter().write("success");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
				clazzDao.closeCon();
			}
		}
	}

	private void deleteClazz(HttpServletRequest req, HttpServletResponse resp) {
		// TODO Auto-generated method stub
		Integer id = Integer.parseInt(req.getParameter("clazzid"));
		ClazzDao clazzDao = new ClazzDao();
		if (clazzDao.deleteClazz(id)){
			try {
				resp.getWriter().write("success");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
				clazzDao.closeCon();
			}
		}
	}

	private void addClazz(HttpServletRequest req, HttpServletResponse resp) {
		// TODO Auto-generated method stub
		String name = req.getParameter("name");
		String info = req.getParameter("info");
		Clazz clazz = new Clazz();
		clazz.setName(name);
		clazz.setInfo(info);
		ClazzDao clazzDao = new ClazzDao();
		if (clazzDao.addClazz(clazz)){
			try {
				resp.getWriter().write("success");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
				clazzDao.closeCon();
			}
		}
	}

	private void clazzList(HttpServletRequest req, HttpServletResponse resp) {
		// TODO Auto-generated method stub
		try {
			req.getRequestDispatcher("view/clazzList.jsp").forward(req, resp);
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void getClazzList(HttpServletRequest req, HttpServletResponse resp){
		String name = req.getParameter("clazzName");
		//Integer currentPage = Integer.parseInt(req.getParameter("page"));
		//Integer pageSize = Integer.parseInt(req.getParameter("rows"));
		Integer currentPage = req.getParameter("page") == null ? 1 : Integer.parseInt(req.getParameter("page"));
		Integer pageSize = req.getParameter("rows") == null ? 999 : Integer.parseInt(req.getParameter("rows"));
		Clazz clazz = new Clazz();
		clazz.setName(name);
		ClazzDao clazzDao = new ClazzDao();
		List<Clazz> clazzList = clazzDao.getClazzList(clazz , new Page(currentPage, pageSize));
		int total = clazzDao.getClazzListTotal(clazz);
		clazzDao.closeCon();
		resp.setCharacterEncoding("UTF-8");
		Map<String, Object> ret = new HashMap<String, Object>();
		ret.put("total",total);
		ret.put("rows",clazzList);
		//JsonConfig jsonConfig = new JsonConfig();
		//String clazzListString = JSONArray.fromObject(clazzList,jsonConfig).toString();
		try {
			String from = req.getParameter("from");
			if("combox".equals(from)){
				resp.getWriter().write(JSONArray.fromObject(clazzList).toString());
			}else{
				resp.getWriter().write(JSONObject.fromObject(ret).toString());
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
