import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.apache.commons.lang.StringUtils;

@MultipartConfig(location = "e:/file/", maxFileSize = 1024 * 1024 * 1024)
@WebServlet(name = "getPartBodyContentServlet", urlPatterns = { "/upload.do" }, loadOnStartup = 0)
public class Uploader extends HttpServlet {

	int psw = 0;
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		if(psw==0){
			doGet(request,response);
			return;
		}
		else{
			String s = request.getParameter("psw");
			String m = ""+psw;
			if(s.equals(m)){
		//设置处理编码
		request.setCharacterEncoding("UTF-8");
		Part part = request.getPart("mainfile");
		// 获得文件名字
		String filename = getFilename(part);
		filename=StringUtils.replace(filename, "\\", ",");
		filename=StringUtils.replace(filename, "/", ",");
		filename=StringUtils.replace(filename, ":", ".");
		try{part.write(filename);}
		catch(Exception e){
			response.setContentType("text/html;charset=UTF-8");// 设置响应的类型和编码
			PrintWriter out = response.getWriter();// 取得PrintWriter()对象
			out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
			out.println("<HTML>");
			out.println("  <HEAD><TITLE>Uploader</TITLE></HEAD>");
			out.println("  <script>alert(\"上传失败\")</script><BODY>filename='"+filename+"'&part='"+part+"'&error=<br/>'"+e+"'");
			out.println("  </BODY>");
			out.println("</HTML>");
			out.flush();
			out.close();
			return;
		}
		// 返回页面消息
		response.setContentType("text/html;charset=UTF-8");// 设置响应的类型和编码
		PrintWriter out = response.getWriter();// 取得PrintWriter()对象
		out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
		out.println("<HTML>");
		out.println("  <HEAD><TITLE>Uploader</TITLE></HEAD>");
		out.println("  <script>alert(\"上传成功\")</script><BODY>filename='"+filename+"'&part='"+part+"'");
		out.println("  </BODY>");
		out.println("</HTML>");
		out.flush();
		out.close();
			}else{
				doGet(request,response);
				return;
			}
			}
	}
	// 获取文件名称
	private String getFilename(Part part) {
		if (part == null)
			return null;
		String fileName = part.getHeader("content-disposition");
		if (StringUtils.isBlank(fileName)) {
			return null;
		}
		return StringUtils.substringBetween(fileName, "filename=\"", "\"");
	}
	
	private void writepsw(int psw) throws FileNotFoundException,IOException{
		FileOutputStream os = 
				new FileOutputStream("e:/psw/psw.txt");
		String str="-" + psw + "-";
		os.write(str.getBytes());
		os.flush();
		os.close();
	}
	
	public void doGet(HttpServletRequest request,HttpServletResponse response) throws ServletException,IOException{
		psw = new Random().nextInt() % 1000000;
		System.out.println(psw);
		writepsw(psw);
		response.setContentType("text/html;charset=UTF-8");//设置响应的类型和编码
		PrintWriter out = response.getWriter();//取得PrintWriter()对象
		out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
		out.println("<HTML>");
		out.println("<HEAD><TITLE>Uploader</TITLE>");
		out.println("<BODY>");
		out.println("<form id='frmMain' action='' method='POST' enctype='multipart/form-data'>");
		out.println("<input type='password' name='psw' />");
		out.println("<input type='file' name='mainfile' />");
		out.println("<input type='submit' value='提交' />");
		out.println("</form><!--Final-->");
		out.println("</BODY>");
		out.println("</HTML>");
		out.flush();
		out.close();
		return;
	}
}
