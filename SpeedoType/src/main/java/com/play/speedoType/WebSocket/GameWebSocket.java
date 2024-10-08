package com.play.speedoType.WebSocket;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.play.speedoType.dto.Room;
import com.play.speedoType.repository.GameRepo;
import com.play.speedoType.utils.RoomUtil;

import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;

@ServerEndpoint("/speedoType/{roomCode}/{level}/{username}")
public class GameWebSocket {

    @OnOpen
    public void onOpen(Session session,  @PathParam("roomCode") String roomCode, @PathParam("level") String level, @PathParam("username") String username) throws IOException {
        System.out.println("Connected: " + session.getId());
        System.out.println("Connected to room: " + roomCode);
        System.out.println("Level: "+level);
        
        Room room = GameRepo.getInstance().getOrCreateRoom(roomCode, level); 
        System.out.println("New Client added in " + roomCode + " " + session.getId());
        if (room.isGameStarted()) {
        	GameRepo.getInstance().addToWaitingList(roomCode, session);
        } else {
        	room.addClient(session, username);
        	String textForRoom = room.getTextContent();
        	session.getBasicRemote().sendText("TEXT: " + textForRoom);
        } 
    }

    @OnMessage
    public void onMessage(String message, Session session, @PathParam("roomCode") String roomCode) throws IOException{
        System.out.println("Message from client: " + message);
        
        Room room = GameRepo.getInstance().getRoom(roomCode);
        if(room != null) {
        	if (message.startsWith("READY")) {
        		room.setClientReady(session);
        		if (room.checkAllClientsReady()) {
        			room.broadcastMessage("START");
        		}
        	} else if ( message.startsWith("RESULT")) {
        		System.out.println("inside the result");
        		String[] resultparts = message.split(":");

        		String winnerUsername = room.getWinner(); 
        		String username = resultparts[1];
        		String score = resultparts[2];
        		int scorevalue = Integer.parseInt(score);
        		room.updateScore(session, username, scorevalue);
        		
        		StringBuilder finalResultMessage = new StringBuilder("RESULT:");
        		finalResultMessage.append(winnerUsername);
        		List<Map.Entry<String, Integer>> leaderboardEntries = new ArrayList<>(room.getLeaderBoard().entrySet());
        	    leaderboardEntries.sort((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue())); // Descending order

        		for (Map.Entry<String, Integer> entry : leaderboardEntries) {
        	        String player = entry.getKey();
        	        int playerScore = entry.getValue();
        	        
        	        finalResultMessage.append(":").append(player).append(",").append(playerScore);
        	    }
        		room.broadcastMessage(finalResultMessage.toString());
        		
        	} else if (message.startsWith("PROGRESS:")) {
        		String[] progressData = message.split(":");
        	    if (progressData.length == 4) {
        	        String progress = progressData[1]; 
        	        String username = progressData[2]; 
        	        String wpm = progressData[3];      

        	        int progressValue = Integer.parseInt(progress);
        	        int wpmValue = Integer.parseInt(wpm);

        	        room.updateScore(session, username, progressValue);
        	        room.broadcastProgress(session, message);
        	       
        	    }
            } else if (message.startsWith("REMATCH")) {
            	
            	
            }
        	else {
        		room.broadcastMessage(session.getId() + " : " + message);
        		System.out.println("Message received in Room " + roomCode + " : " + message + " from " + session.getId());
        	}

        } 
    }

    @OnClose
    public void onClose(Session session,@PathParam("roomCode") String roomCode) {
    	Room room = GameRepo.getInstance().getRoom(roomCode);
    	if(room!=null)
    	        System.out.println("Connection close in room "+roomCode+" "+session.getId());

    }
    
    @OnError
    public void onError(Session session, Throwable throwable) {
        throwable.printStackTrace();
    }
   

}