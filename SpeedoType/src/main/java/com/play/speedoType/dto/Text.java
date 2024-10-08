package com.play.speedoType.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Text {
	private static Text text = null; 
	Map<String, List<String>> gameText;
	
	private Text(){
		gameText = new HashMap<>();
		addText("easy", "The garden is full of colorful flowers. Bees buzz around, collecting nectar. A gentle breeze makes the leaves dance. It is a peaceful place to relax and enjoy nature.");
		addText("easy", "The cat sits on the windowsill, watching the world outside. It sees birds hopping on the grass and feels the sun on its fur. The cat enjoys its quiet time, content and relaxed.");
		addText("medium", "On chilly autumn mornings, the streets are adorned with a blanket of fallen leaves. The crisp air carries the scent of pumpkin spice and cinnamon. People wrap themselves in cozy scarves as they stroll through the local market, where vendors display their harvest bounty. The vibrant colors of seasonal produce create a feast for the eyes.");
		addText("medium", "Every evening, the sun sets behind the mountains, casting a golden glow over the landscape. As darkness falls, stars begin to twinkle in the clear night sky. People gather around campfires, sharing stories and roasting marshmallows, while the crackling of the fire creates a soothing melody in the background.");
		addText("hard", "As twilight envelops the horizon, the sound of distant waves crashing against the rocky shore harmonizes with the whispering winds. The ocean, a vast expanse of mystery, reflects the fading light, shimmering with shades of indigo and silver. On the cliffside, an ancient lighthouse stands sentinel, its beam cutting through the darkness, guiding lost souls back to safety. In this serene yet powerful moment, nature's grandeur reminds us of the delicate balance between tranquility and the ferocity of the elements.");
		addText("hard", "In the heart of the ancient city, cobblestone streets wind through historic neighborhoods, where time seems to stand still. Majestic architecture, with intricate carvings and arched doorways, tells stories of a bygone era. As the sun sets, shadows lengthen, and the rich history of the place comes alive, echoing with tales of resilience and transformation. Locals gather in quaint cafes, sharing laughter and memories over cups of aromatic coffee, the warmth of companionship cutting through the evening chill.");
		
	}
	
	public static Text getInstance() {
		if(text==null) {
			text = new Text();
		}
		return text;
	}
	public boolean addText(String level, String paragraph) {
		List<String> paragraphs = gameText.get(level);
		if (paragraphs == null) {
            paragraphs = new ArrayList<>();
            gameText.put(level, paragraphs); 
        }
		return paragraphs.add(paragraph);
	}
	
	public String getGameText(String level) {
		List<String> selectByLevel = gameText.get(level);
		if(selectByLevel != null && !selectByLevel.isEmpty()) {
			int randomIndex = (int) (Math.random() * selectByLevel.size());
			return selectByLevel.get(randomIndex);
		}
		return null;
	}
	
}
