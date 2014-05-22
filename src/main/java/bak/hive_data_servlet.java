package bak;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hive.Redis.HiveRedis;

public class hive_data_servlet extends HttpServlet {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

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
		String username = null;
		Integer da;
		PrintWriter out = resp.getWriter();
		
//		da = (int)(HiveRedis.Sechange(username));
//		out.write(da.toString());
		out.close();
		
	}
}

