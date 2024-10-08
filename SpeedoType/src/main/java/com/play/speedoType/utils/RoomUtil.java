package com.play.speedoType.utils;

import java.util.Random;

import com.play.speedoType.repository.GameRepo;

public class RoomUtil {
	private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWEXYZ1234567890";
	private static final int ROOM_CODE_LENGTH = 8;
	public static String generateRandomRoomCode(GameRepo gameRepo) {
		Random random = new Random();
		StringBuilder roomCode;
		do {
			roomCode = new StringBuilder(ROOM_CODE_LENGTH);
			for(int i=0; i<ROOM_CODE_LENGTH; i++) {
				roomCode.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
			}
		} while(gameRepo.roomExists(roomCode.toString()));
		return roomCode.toString();
	}

}
