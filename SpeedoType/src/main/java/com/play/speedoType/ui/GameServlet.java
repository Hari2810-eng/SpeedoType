package com.play.speedoType.ui;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

import com.play.speedoType.dto.Room;
import com.play.speedoType.dto.User;
import com.play.speedoType.repository.GameRepo;
import com.play.speedoType.repository.UserRepository;
import com.play.speedoType.utils.RoomUtil;
/**
 * Servlet implementation class GameServlet
 */
public class GameServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private GameRepo gameRepo;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GameServlet() {
        super();
        gameRepo = GameRepo.getInstance();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		String username = (String) session.getAttribute("username");
		System.out.println(username);
		User currentUser = UserRepository.getInstance().getUserByUsername(username);
		
		if(currentUser == null) {
			response.sendRedirect("login.html");
			return;
		}
		
		String action = request.getParameter("action");
		
		switch (action) {
	        case "createRoom":
	            createRoom(request, response, session, currentUser);
	            break;
	        
	        case "joinRoom":
	            joinRoom(request, response, session, currentUser);
	            break;
	        
	        default:
	            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
	            response.getWriter().write("Invalid action.");
	            break;
	    }		    
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
	private void createRoom(HttpServletRequest request, HttpServletResponse response, HttpSession session, User currentUser) throws ServletException, IOException {
	    String level = request.getParameter("level");
	    session.setAttribute("level", level);

	    if (level == null || level.isEmpty()) {
	        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
	        response.getWriter().write("Difficulty level not specified.");
	        return;
	    }
	    
	    String roomCode = RoomUtil.generateRandomRoomCode(gameRepo);
	    System.out.println("Created room code : "+roomCode);  
	    session.setAttribute("username", currentUser.getUsername());
	    session.setAttribute("roomCode", roomCode);
	    request.setAttribute("roomCode", roomCode); 
	    System.out.println("Created the room successfully with difficulty " + level);
	    request.getRequestDispatcher("PlayMulti.jsp").forward(request, response);
	}

	private void joinRoom(HttpServletRequest request, HttpServletResponse response, HttpSession session, User currentUser) throws ServletException, IOException {
	    String roomCode = request.getParameter("roomCode");
	    session.setAttribute("level", "");
	    
	    if (roomCode == null || roomCode.isEmpty()) {
	        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
	        response.getWriter().write("Room code is missing.");
	        return;
	    }
	    String defaultLevel = "easy"; 
	    session.setAttribute("level", defaultLevel);
	    session.setAttribute("username", currentUser.getUsername());
	    session.setAttribute("roomCode", roomCode);
	    request.setAttribute("roomCode", roomCode); 
	    System.out.println("Joined the room successfully with code " + roomCode);
	    request.getRequestDispatcher("PlayMulti.jsp").forward(request, response); 
	}

}
