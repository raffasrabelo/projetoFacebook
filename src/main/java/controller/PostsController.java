package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.ModelException;
import model.Post;
import model.dao.DAOFactory;
import model.dao.PostDAO;
import java.io.IOException;
import java.util.List;

/**
 * Servlet implementation class PostsController
 */
@WebServlet(urlPatterns = {"/post/delete", "/post/update", "/post/save", "/posts"})
public class PostsController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {
		
		String action = req.getRequestURI();
		
		System.out.println(action);
		
		switch (action) {
		case "/facebook/posts": {
			loadPosts(req);
			
			req.getRequestDispatcher("posts.jsp").forward(req, resp);
			break;
		}
		
		}
	}

	
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {
		
	}

	private void loadPosts(HttpServletRequest req) {
		PostDAO dao = DAOFactory.createDAO(PostDAO.class);
		
		List<Post> posts = null;
		try {
			posts = dao.listAll();
		} catch (ModelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (posts != null)
			req.setAttribute("posts", posts); //o primeiro é a lista no jsp; o segundo é a lista que foi gerada pelo controller no java.
	}
}
