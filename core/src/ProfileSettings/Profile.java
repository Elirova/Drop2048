package ProfileSettings;

import java.util.ArrayList;

import screenControl.GameSelectScreen.Status;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Json.Serializable;
import com.badlogic.gdx.utils.JsonValue;

public class Profile implements Serializable {
	private static ArrayList<ArrayList<Integer>> maxScore;
	private static int SCORES = 10;
	private static boolean music, sound;
	
	public Profile() {
		maxScore = new ArrayList<ArrayList<Integer>>();
		maxScore.add(new ArrayList<Integer>(SCORES));
		maxScore.add(new ArrayList<Integer>(SCORES));
		maxScore.add(new ArrayList<Integer>(SCORES));
		maxScore.add(new ArrayList<Integer>(SCORES));
		
		for(int i = 0; i < SCORES; i++) {
			maxScore.get(0).add(0);
			maxScore.get(1).add(0);
			maxScore.get(2).add(0);
			maxScore.get(3).add(0);
		}
		
		music = true;
		sound = true;
	}
	
	// Methods get / set
	public static void setScore(int newScore, Status status) {
		int array = 0;
		switch(status) {
		case NORMAL:
			array = 1;
			break;
		case HARD:
			array = 2;
			break;
		case EXTREME:
			array = 3;
			break;
		default:
			break;
		}
		if(maxScore.get(array).get(SCORES-1) < newScore) { // Entra en el rango de mejores puntuaciones
			for(int i = 0; i < SCORES; i++) {
				if(newScore > maxScore.get(array).get(i)) {
					maxScore.get(array).remove(SCORES-1);
					maxScore.get(array).add(i, newScore);
					break;
				}
			}
		}
	}
	
	public static ArrayList<Integer> getScore(Status status) {
		switch(status) {
		case NORMAL:
			return maxScore.get(1);
		case HARD:
			return maxScore.get(2);
		case EXTREME:
			return maxScore.get(3);
		default:
			return maxScore.get(0);
		}
	}
	
	public static int getMaxScore(Status status) {
		switch(status) {
		case NORMAL:
			return maxScore.get(1).get(0);
		case HARD:
			return maxScore.get(2).get(0);
		case EXTREME:
			return maxScore.get(3).get(0);
		default:
			return maxScore.get(0).get(0);
		}
	}

	public static boolean isMusic() {
		return music;
	}

	public static void setMusic(boolean music) {
		Profile.music = music;
	}
	
	public static boolean updateMusic() {
		Profile.music = Profile.isMusic()? false : true;
		return Profile.music;
	}

	public static boolean isSound() {
		return sound;
	}

	public static void setSound(boolean sound) {
		Profile.sound = sound;
	}
	
	public static boolean updateSound() {
		Profile.sound = Profile.isSound()? false : true;
		return Profile.sound;
	}

	@Override
	public void write(Json json) {
		for(int i = 0; i < maxScore.size(); i++) {
			json.writeArrayStart("Scores" + i);
	        for(int score : maxScore.get(i)) {
	        	json.writeValue(score);
	        }
	        json.writeArrayEnd();
	        json.writeValue( "music", music );
	        json.writeValue( "sound", sound );
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void read(Json json, JsonValue jsonData) {
		for(int i = 0; i < maxScore.size(); i++)
			maxScore.set(i, json.readValue( "Scores" + i, ArrayList.class, Integer.class, jsonData ));
		music = json.readValue( "music", Boolean.class, jsonData );
		sound = json.readValue( "sound", Boolean.class, jsonData );
	}
}
