package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.ModelException;
import model.User;
import model.dao.DAOFactory;
import model.dao.UserDAO;

//Rotas
@WebServlet(urlPatterns = {"/users", "/user/create", "/user/update", "/user/delete"})
public class UsersController extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {
		
		String action = req.getRequestURI();
		System.out.println(action);
		
		switch (action) {
		case "/facebook/users": {
			// Listagem dos usuários
			listUsers(req);
			
			// Redirecionar para a página de exibição (index)
			RequestDispatcher rd = req.getRequestDispatcher("users.jsp");
			rd.forward(req, resp);
			break;
		}
		case "/facebook/user/create" :{
			
			saveUser(req);
			
			
			resp.sendRedirect("/facebook/users");
			break;
		}
		
		case "/facebook/user/update" :{
			
			updateUser(req, resp);
			
			break;
		}
		
		case "/facebook/user/delete" : {
			
			deleteUser(req, resp);
			break;
		}
			
		case "/facebook/": {
			// Redirecionar para a página de exibição (index)
			RequestDispatcher rd = req.getRequestDispatcher("index.jsp");
			rd.forward(req, resp);
			break;	
		}
		
		default: 
			throw new IllegalArgumentException("URL não reconhecida: "+ action);
		}
	}

	private void deleteUser(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		String userIDStr= req.getParameter("userId");
		int userId = Integer.parseInt(userIDStr);
		
		UserDAO dao = DAOFactory.createDAO(UserDAO.class);
		
		User user = new User(userId);
		try {
			dao.delete(user);
		} catch (ModelException e) {
			// Tratar erro depois
			e.printStackTrace(); // não é mostrado pro cliente. Apenas servidor.
		}
		
		//Redireciona para a listagem
		resp.sendRedirect("/facebook/users");
	}

	private void updateUser(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String userIDStr= req.getParameter("userId");
		int userId = Integer.parseInt(userIDStr);
		
		UserDAO dao = DAOFactory.createDAO(UserDAO.class);
		
		User user = new User();
		try {
			user = dao.findById(userId);
		} catch (ModelException e) {
			// Tratar erro depois
			e.printStackTrace(); // não é mostrado pro cliente. Apenas servidor.
		}
		
		req.setAttribute("usuario", user);
		
		RequestDispatcher rd = req.getRequestDispatcher("/form_user.jsp");
		rd.forward(req, resp);
	}

	private void listUsers(HttpServletRequest req) {
		
		UserDAO dao = DAOFactory.createDAO(UserDAO.class);
		
		List<User> users = new ArrayList<User>();
		try {
			users = dao.listAll();
		} catch (ModelException e) {
			e.printStackTrace();
		}
		
		req.setAttribute("usuarios", users);
		
	}
	private void saveUser(HttpServletRequest req) {
		// Recuperando os parametros da requisição
					// Inputs (name) do html
					String userIdStr = req.getParameter("user_id");
					String userName = req.getParameter("user_name");
					String userGender = req.getParameter("user_gender");
					String userEmail = req.getParameter("user_email");
					
					boolean newUser = userIdStr.equals("");
					
					//Cria e seta os valores do usuário
					User user = newUser ? new User() : new User(Integer.parseInt(userIdStr));
					user.setName(userName);
					user.setGender(userGender);
					user.setEmail(userEmail);
					
					
								
					//Salva o usuário no Banco
					UserDAO dao = DAOFactory.createDAO(UserDAO.class);
					try {
						if(newUser)
							dao.save(user);
						else
							dao.update(user);
					} catch (ModelException e) {
						System.err.println("Erro ao salvar usuário");
						e.printStackTrace();
					}
					
	}
}
