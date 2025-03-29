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
@WebServlet(urlPatterns = {"", "/create"})
public class UsersController extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {
		
		String action = req.getRequestURI();
		System.out.println(action);
		
		switch (action) {
		case "/facebook/": {
			// Listagem dos usuários
			listUsers(req);
			
			// Redirecionar para a página de exibição (index)
			RequestDispatcher rd = req.getRequestDispatcher("index.jsp");
			rd.forward(req, resp);
//			resp.sendRedirect("index.jsp");
			break;
		}
		case "/facebook/create" :{
			
			insertUser(req);
			
			resp.sendRedirect("/facebook/");
			break;
		}
		default: 
			throw new IllegalArgumentException("URL não reconhecida: "+ action);
		}
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
	private void insertUser(HttpServletRequest res) {
		// Recuperando os parametros da requisição
					// Inputs (name) do html
					String userName = req.getParameter("user_name");
					String userGender = req.getParameter("user_gender");
					String userEmail = req.getParameter("user_email");
					
					//Cria e seta os valores do usuário
					User user = new User();
					user.setName(userName);
					user.setGender(userGender);
					user.setEmail(userEmail);
								
					//Salva o usuário no Banco
					UserDAO dao = DAOFactory.createDAO(UserDAO.class);
					try {
						dao.save(user);
					} catch (ModelException e) {
						System.err.println("Erro ao salvar usuário");
						e.printStackTrace();
					}
	}
}
