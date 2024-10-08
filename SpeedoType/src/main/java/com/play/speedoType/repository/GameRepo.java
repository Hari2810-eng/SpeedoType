package com.play.speedoType.repository;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.play.speedoType.dto.Room;

import jakarta.websocket.Session;

public class GameRepo {
	private static GameRepo gameRepo = null;
	
	private static final Map<String, Room> rooms = new HashMap<>();
	private static final Map<String, Queue<Session>> waiting_list = new ConcurrentHashMap<>();
	private static final ExecutorService threadPool = Executors.newFixedThreadPool(10);

	
	public static GameRepo getInstance() {
		if(gameRepo == null)
			gameRepo = new GameRepo();
		return gameRepo;
	}
	
	public Room getOrCreateRoom(String roomCode, String level) {
        return rooms.computeIfAbsent(roomCode, code -> new Room(code, level));
    }
	
	
	public void addRoom(Room room) {
		rooms.put(room.getRoomId(), room);
	}
	public Room getRoom(String roomCode) {
		return rooms.get(roomCode);
	}
	public void removeRoom(String roomCode) {
		rooms.remove(roomCode);
	}
	public boolean roomExists(String roomCode) {
		return rooms.containsKey(roomCode);
	}
	public void addToWaitingList(String roomCode, Session session) {
        waiting_list.computeIfAbsent(roomCode, k -> new ConcurrentLinkedQueue<>()).add(session);
    }
	public Queue<Session> getWaitingList(String roomCode) {
        return waiting_list.get(roomCode);
    }
	public void removeFromWaitingList(String roomCode, Session session) {
        Queue<Session> queue = waiting_list.get(roomCode);
        if (queue != null) {
            queue.remove(session);
        }
    }
	public void shutdown() {
        threadPool.shutdown();
    }
}
