package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

//Rotas
@WebServlet(urlPatterns = {""})
public class TesteController extends HttpServlet  {
	
	
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {
		
//		String addr = req.getRequestURI();
		
//		System.out.println(addr);//aparece no console local pois o servidor é local
		String hora = Calendar.getInstance().getTime().toLocaleString();
		
		PrintWriter writer = resp.getWriter();
		writer.append("<!DOCTYPE html>");
		writer.append("<html>");
		writer.append("<head>");
		writer.append("<meta charset=\"UTF-8\">");
		writer.append("<title>Insert title here</title>");
		writer.append("</head>");
		writer.append("<body>");
		writer.append("<h1>Minha primeira página!</h1>");
		writer.append("<p>"+ hora +"</p>");
		writer.append("</body>");
		writer.append("</html>");	
			
		
		
	}
}
