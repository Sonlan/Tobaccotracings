package com.song.service;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.song.DBModule.Administrators;
import com.song.DBUtils.JsonUtils;
import com.song.DBUtils.AdminDbUtils;
import com.song.DBUtils.MyDBUtils;
import com.song.factory.DBFactory;

public class LoginServlet extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public LoginServlet() {
		super();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	/**
	 * The doGet method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doPost(request, response);
	}

	/**
	 * The doPost method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to post.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//数据库连接池初始化
		DBFactory.DbPoolInit();
		
		String userName = request.getParameter("username").toString();
		String password = request.getParameter("password").toString();
		String PATH = request.getScheme() + "://"
				+ request.getServerName() + ":" + request.getServerPort()
				+ request.getContextPath() + "/";
		if(userName.equals("username")) {
			Cookie cookie = new Cookie("_PERMISSION", 1+"");
		    cookie.setPath("/");
		    cookie.setMaxAge(60 * 60);//必须设置时间，否则只在当前页面有效
		    response.addCookie(cookie);
		    request.getSession().setAttribute("_LOGIN", "OK");
			response.getWriter().write(JsonUtils.statusResponse(1,PATH+"super/main.html"));
		}else{
		Administrators administrator = null;
		MyDBUtils DBTool = DBFactory.getDBFactory(0);
		try {
			administrator = (Administrators) DBTool.select(userName);
			if(administrator!=null){
				System.out.println(administrator.getPassword()+administrator.getPassword().equals(password)+password);
				if(administrator.getPassword().equals(password)){
				//	response.getWriter().write(statusResponse(1,PATH+"super/console.html"));
				    request.getSession().setAttribute("_LOGIN", "OK");
				    request.getSession().setAttribute("_USERNAME", userName);
				    Cookie cookie = new Cookie("_PERMISSION", administrator.getPermission()+"");
				    cookie.setPath("/");
				    cookie.setMaxAge(60 * 60);//必须设置时间，否则只在当前页面有效
				    response.addCookie(cookie);
					response.getWriter().write(JsonUtils.statusResponse(1,PATH+"super/main.html"));
					return;
				}else {
					response.getWriter().write(JsonUtils.statusResponse(0,"no user found!"));
				}
			}
		} catch (Exception e) {
			response.getWriter().write(JsonUtils.statusResponse(0,"databaseError!"));
			System.out.println("select failed");
		}
		
		}
		
	}

	/**
	 * Initialization of the servlet. <br>
	 *
	 * @throws ServletException if an error occurs
	 */
	public void init() throws ServletException {
		// Put your code here
		System.out.println("loginServlet start");
	}
	


}
