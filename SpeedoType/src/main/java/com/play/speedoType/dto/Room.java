package com.play.speedoType.dto;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import com.play.speedoType.WebSocket.ClientSession;

import jakarta.websocket.Session;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

//
//public class Room {
//	
//	private String roomCode;
//	private Set<User> participants;
//	private boolean isGameStarted;
//	private TreeMap<Integer, User> leaderboard;
//	private String text;
//	private String level;
//	
//	public Room(String roomCode, String level, User creator) {
//		this.roomCode = roomCode;
//		this.participants = new HashSet<>();
//		this.participants.add(creator);
//		this.leaderboard = new TreeMap<>((a, b) -> b-a);
//		this.isGameStarted = false;
//		this.level = level;
//		this.text =  Text.getInstance().getGameText(level);
//	}
//	
//	public String getRoomCode() {
//		return roomCode;
//	}
//	
//	public void setRoomCode(String roomCode) {
//		this.roomCode = roomCode;
//	}
//	
//	public Set<User> getParticipants() {
//		return participants;
//	}
//
//	public void addParticipants(User user) {
//		 if (!participants.contains(user)) {
//			 participants.add(user);
//	        System.out.println("Player " + user.getUsername() + " joined the room.");
//	     } else {
//	         System.out.println("Player " + user.getUsername() + " is already in the room.");
//         }
//	}
//	
//	public void removeParticipants(User user) {
//		participants.remove(user);
//		System.out.println("Player " + user.getUsername() + " left the room.");
//	}
//	
//	public boolean isGameStarted() {
//		return isGameStarted;
//	}
//	
//	public void setGameStarted(boolean isGameStarted) {
//		this.isGameStarted = isGameStarted;
//	}
//	
//	public boolean isRoomFull() {
//		return participants.size() >= 4;
//	}
//	
//	public void updateScore(User user, int score) {
//		if(leaderboard.containsValue(user)) {
//			leaderboard.put(score,  user);
//		}
//	}
//	
//	public Map<Integer, User> getLeaderboard() {
//        return leaderboard;
//    }
//	
//	public String getText() {
//		return text;
//	}
//
//	public void setText(String text) {
//		this.text = text;
//	}
//
//	public String getLevel() {
//		return level;
//	}
//
//	public void setLevel(String level) {
//		this.level = level;
//	}
//	public void startGame() {
//	    if (!isGameStarted) {
//	        isGameStarted = true; 
//	        System.out.println("Game has started in room: " + roomCode);
//
//	    } else {
//	        System.out.println("Game is already in progress.");
//	    }
//	}
//
//}

public class Room {
    private final String roomId;
    private final Map<Session, Boolean> clients = Collections.synchronizedMap(new HashMap<>());
    private final Map<Session, Boolean> finishedClients = Collections.synchronizedMap(new HashMap<>());
    private String textContent;
    private boolean gameStarted = false;
    private final String level;
    private Map<String, Integer> leaderboard;
    
    public Room(String roomId, String level) {
        this.roomId = roomId;
        this.level = level;
        this.textContent = Text.getInstance().getGameText(level);
        this.leaderboard = new HashMap<>();
    }

    public String getRoomId() {
        return roomId;
    }

    public String getTextContent() {
        return textContent;
    }
    
    public void setTextContent(String textContent) {
    	this.textContent = textContent;
    }
    public String getLevel() {
    	return level;
    }

    public void addClient(Session session, String username) {
        if (gameStarted) {
            sendMessageToClient(session, "ROOM_IN_GAME");
            return;
        }
        clients.put(session, false);
        finishedClients.put(session, false);
        leaderboard.put(username,  0);
        sendMessageToClient(session, "TEXT:" + textContent);
        broadcastLeaderboard();
    }

    public void removeClient(Session session, String username) {
        leaderboard.remove(username);
        clients.remove(session);
        finishedClients.remove(session);
        broadcastLeaderboard(); // Update leaderboard after removal
    }

    public void setClientReady(Session session) {
    	System.out.println("Setted player session" + session + "as ready");
        clients.put(session, true);
    }

    public boolean checkAllClientsReady() {
        for (boolean ready : clients.values()) {
            if (!ready) {
                return false;
            }
        }
        startGame();
        return true;
    }

    public void setClientFinished(Session session) {
        finishedClients.put(session, true);
    }

    public boolean areAllClientsFinished() {
        for (Boolean finished : finishedClients.values()) {
            if (!finished) {
                return false;
            }
        }
        return true;
    }
    
    public void startGame() {
        gameStarted = true;
    }

    public void resetGame() {
    	textContent = Text.getInstance().getGameText(level);
        gameStarted = false;
        for (Session session : clients.keySet()) {
            clients.put(session, false);
            finishedClients.put(session, false);
        }
    }

    public boolean isGameStarted() {
        return gameStarted;
    }
  

    private void sendMessageToClient(Session session, String message) {
        try {
        	session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void broadcastMessage(String message) {
        synchronized (clients) {
            for (Session client : clients.keySet()) {
                sendMessageToClient(client, message);
            }
        }
    }
    
    public void broadcastProgress(Session session, String progress) {
        broadcastMessage(progress);
    }

    public void updateScore(Session session, String username, int score) {
        if (clients.containsKey(session)) {
            leaderboard.put(username, score); 
            System.out.println("Updated score for user: " + username + " to " + score);
            broadcastLeaderboard(); 
        }
    }
    
    public Map<String, Integer> getLeaderBoard(){
    	return leaderboard;
   
    }
	
    public void broadcastLeaderboard() {
    	List<Map.Entry<String, Integer>> sortedLeaderboard = new ArrayList<>(leaderboard.entrySet());
        sortedLeaderboard.sort((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()));
        
        StringBuilder leaderboardMessage = new StringBuilder("LEADERBOARD:");
        for (Map.Entry<String, Integer> entry : sortedLeaderboard) {
            leaderboardMessage.append(entry.getKey()).append(":").append(entry.getValue()).append(";");
        }
        broadcastMessage(leaderboardMessage.toString());
    }
    public String getWinner() {
        String winner = null;
        int highestScore = -1;

        
        for (Map.Entry<String, Integer> entry : leaderboard.entrySet()) {
            if (entry.getValue() > highestScore) {
                highestScore = entry.getValue();
                winner = entry.getKey(); 
            }
        }
        
        if(highestScore==0) winner="no one";
        
        return winner; 
    }
}

