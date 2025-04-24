package controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.ModelException;
import model.Post;
import model.User;
import model.dao.DAOFactory;
import model.dao.PostDAO;
import model.dao.UserDAO;

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
		
		case "/facebook/post/save": {
			String postId = req.getParameter("post_id");
			if (postId != null && !postId.equals(""))
				updatePost(req);
			else insertPost(req);
			
			resp.sendRedirect("/facebook/posts");
			break;
		}
		
		case "/facebook/post/update": {
			loadPost(req);

            RequestDispatcher rd = req.getRequestDispatcher("/form_post.jsp");
            rd.forward(req, resp);
            break;
		}
		
		case "/facebook/post/delete": {

            deletePost(req);

            resp.sendRedirect("/facebook/posts");
            break;
        }
        default:
            throw new IllegalArgumentException("Unexpected value: " + action);
		}
	}

	
	private void insertPost(HttpServletRequest req) {
		Post post = createPost(req);
		
		PostDAO dao = DAOFactory.createDAO(PostDAO.class);
		
		try {
			dao.save(post);
		} catch (ModelException e) {
			// log no servidor
			e.printStackTrace();
		}
	}


	private Post createPost(HttpServletRequest req) {
		String postId = req.getParameter("post_id");
		String postContent = req.getParameter("post_content");
		Date postDate = new Date();
		String postUser = req.getParameter("post_user");
		int postUserId = Integer.parseInt(postUser);
		UserDAO dao = DAOFactory.createDAO(UserDAO.class);
	
		
		Post post;
		
		if (postId.equals(""))
			post = new Post();
		else post = new Post(Integer.parseInt(postId));
		
		
		post.setContent(postContent);
		post.setPostDate(postDate);
		try {
			post.setUser(dao.findById(postUserId));
		} catch (ModelException e) {
			e.printStackTrace();
		}
		return post;
	}


	private void updatePost(HttpServletRequest req) {
        Post post = createPost(req);

        PostDAO dao = DAOFactory.createDAO(PostDAO.class);

        try {
            dao.update(post);
        } catch (ModelException e) {
            // log no servidor
            e.printStackTrace();
        }
    }
	
	 private void deletePost(HttpServletRequest req) {
	        String postIdString = req.getParameter("postId");
	        int postId = Integer.parseInt(postIdString);

	        Post post = new Post(postId);

	        PostDAO dao = DAOFactory.createDAO(PostDAO.class);

	        try {
	            dao.delete(post);
	        } catch (ModelException e) {
	            // log no servidor
	            e.getCause().printStackTrace();
	            e.printStackTrace();
	        }
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
	
	private void loadPost(HttpServletRequest req) {
        String postIdParameter = req.getParameter("postId");

        int postId = Integer.parseInt(postIdParameter);

        PostDAO dao = DAOFactory.createDAO(PostDAO.class);

        try {
            Post post = dao.findById(postId);

            if (post == null)
                throw new ModelException("Post não encontrado para alteração");

            req.setAttribute("post", post);
        } catch (ModelException e) {
            // log no servidor
            e.printStackTrace();
        }
    }
	
	
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {
		
	}

	
}
