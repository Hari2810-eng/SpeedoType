package com.play.speedoType.repository;


import java.util.ArrayList;
import java.util.List;

import com.play.speedoType.dto.User;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public class UserRepository {
	private static UserRepository userRepository = null;
	private List<User> users; 
	
	private UserRepository() {
		users = new ArrayList<>();
		User user = new User();
        user.setUsername("admin");
        user.setPassword("admin");
        user.setEmail("admin@gmail.com");
        users.add(user);
        User user2 = new User();
        user2.setUsername("hari");
        user2.setPassword("hari");
        user2.setEmail("hari@gmail.com");
        users.add(user2);
	}
	
	public static UserRepository getInstance() {
		if(userRepository == null)
			userRepository = new UserRepository();
		return userRepository;
	}
	
	public User getUserByUsername(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user; 
            }
        }
        return null;
    }
	
	public boolean validateUser(HttpServletRequest request, String username, String password) {
        User user = getUserByUsername(username);
		if( user!= null && user.getPassword().equals(password) ) {
        	HttpSession session = request.getSession();
        	session.setAttribute("username", user.getUsername());
        	return true;
		}
        return false;
    }
	
	public User createUser(String username, String email, String password) {
		User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);
        users.add(user);
        System.out.println("username: "+username+" password: "+password+" email: "+email);
        return user;
	}
	
	public boolean isLoggedIn(HttpServletRequest request) {
        HttpSession session = request.getSession(false); 
		return session!=null && session.getAttribute("username") != null;
    }
	
	public void logout(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if(session!=null) {
			session.invalidate();
		}
    }
	
	public String getLoggedInUser(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		return (session!=null) ? (String) session.getAttribute("username") : null;
	}
}
