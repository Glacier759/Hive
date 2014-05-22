package bak;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hive.Thread.Main;

public class hive_sb_servlet extends HttpServlet {


	public void doGet(HttpServletRequest req, HttpServletResponse resp) 
				throws ServletException,IOException
	{
		process(req,resp);
	}
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp) 
				throws ServletException,IOException
	{
		process(req,resp);
	}
	
	private void process(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException,IOException
	{
		req.setCharacterEncoding("utf-8");
		String keyword = req.getParameter("keyword");
		String tag = req.getParameter("tag");
		resp.setCharacterEncoding("utf-8");
		resp.setContentType("text/html;charset=utf-8");
		String username = "user";
		Main m = new Main();
		m.startHive(username, "", keyword, tag, 2);

	}
}

