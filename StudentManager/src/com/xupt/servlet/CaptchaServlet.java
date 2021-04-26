package com.xupt.servlet;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.xupt.util.CaptchaUtil;

/**
 * @author ‹∞
 *—È÷§¬ÎServlet
 */
public class CaptchaServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(req, resp);
	}

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		String method = req.getParameter("method");
		if ("loginCaptcha".equals(method)){
			generateLoginCaptcha(req, resp);
			return;
		}
		resp.getWriter().write("error method");
	}
	private void generateLoginCaptcha(HttpServletRequest req, HttpServletResponse resp) throws IOException{
		CaptchaUtil captchaUtil = new CaptchaUtil();
		String generatorVCode = captchaUtil.generatorVCode();
		req.getSession().setAttribute("loginCaptcha", generatorVCode);
		BufferedImage generatorRotateVCodeImage = captchaUtil.generatorRotateVCodeImage(generatorVCode, true);
		ImageIO.write(generatorRotateVCodeImage, "gif", resp.getOutputStream());
	}

}
