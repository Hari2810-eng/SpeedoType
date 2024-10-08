package com.play.speedoType.ui;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

import com.play.speedoType.dto.User;
import com.play.speedoType.repository.UserRepository;

public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getRequestDispatcher("login.html").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String action = request.getParameter("action");
		switch(action) {
			case "login":
				login(request, response);
				break;
			case "signup":
				signUp(request, response);
				break;
			default:
				response.sendRedirect("login.html");
		}		
	}
	
	private void signUp(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		String username = request.getParameter("username");
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		String confirmPassword = request.getParameter("confirmPassword");
		
		
		if(usernameExists(username)) {
			request.setAttribute("errorMessage", "Username already exists");
			response.sendRedirect("signup.jsp");
		} else if(!password.equals(confirmPassword)) {
			request.setAttribute("errorMessage", "Passwords do not match.");
			response.sendRedirect("signup.html");
        } else if(newUser(username, email, password)) {
        	response.sendRedirect("login.html");
        } else {
        	request.setAttribute("errorMessage", "An error occurred during sign-up.");
        	response.sendRedirect("signup.jsp");
        }
	}
	
	private void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String username = request.getParameter("username");
        String password = request.getParameter("password");
        
        boolean isCorrect = UserRepository.getInstance().validateUser(request, username, password);
        
        if (isCorrect) {
            response.sendRedirect(request.getContextPath() + "/home.html"); 
        } else {
            request.setAttribute("errorMessage", "Invalid credentials.");
            request.getRequestDispatcher("login.html").forward(request, response);
        }
	}
	
	private boolean newUser(String username, String email, String password) {
		return (UserRepository.getInstance().createUser(username, email, password) != null);
	}
	private boolean usernameExists(String username) {
        return UserRepository.getInstance().getUserByUsername(username) != null; 
    }
}
