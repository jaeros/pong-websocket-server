package com.jaeros.pongwebsocketserver.Game;

import com.jaeros.pongwebsocketserver.JsonSerializable;
import java.util.Observable;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author Jeff
 */
public class GameModel extends Observable implements JsonSerializable {

    GameState state;
    GameStage stage;
    Player[] players = new Player[2];
    int elapsed;
    
    static final int START_TIME = 30 * 10; // Number of ticks for start
    static final int SCORE_TIME = 30 * 5;

    public GameModel() {
	stage = new GameStage(400, 400);
	state = GameState.NEW_GAME;
    }
    
    public boolean canJoin() {
	return state == GameState.NEW_GAME || state == GameState.WAIT_P2;
    }
    
    private void setState(GameState state) {
	this.state = state;
	elapsed = 0;
	System.out.println("State changed to " + state.toString());
	setChanged();
	notifyObservers(ServerMessageTypes.CHANGE_STATE);
    }
    
    public GameState getState() {
	return state;
    }
    
    public synchronized void join(Player p) {
	switch(state) {
	    case NEW_GAME:
		// First player joining
		players[0] = p;
		System.out.println("First player joined");
		setState(GameState.WAIT_P2);
		break;
	    case WAIT_P2:
		// Second player joining
		players[1] = p;
		System.out.println("Second player joined");
		setState(GameState.STARTING);
		break;
	    default:
		// Handle bad state
		throw new RuntimeException("Can't join game! " + state);
	}
    }

    public void tick() {
	// Track time elapsed
	elapsed++;
	
	// Handle different states
	switch(state) {
	    case STARTING:
		if (elapsed > START_TIME) setState(GameState.PLAYING);
		if (elapsed % 30 == 0) {
		    setChanged();
		    notifyObservers(ServerMessageTypes.COUNT_DOWN);
		}
		break;
	    case PLAYING:
		stage.tick();
		setChanged();
		notifyObservers(ServerMessageTypes.GAME_UPDATE);
		break;
	    case SHOW_SCORE:
		if (elapsed > SCORE_TIME) setState(GameState.PLAYING);
		if (elapsed % 30 == 0) {
		    setChanged();
		    notifyObservers(ServerMessageTypes.COUNT_DOWN);
		}
		break;
	}
    }
    
    public int getCountdown() {
	
	int secondsInCountdown;
	double progress;
	switch(state) {
	    case STARTING:
		secondsInCountdown = START_TIME / 30;
		progress = secondsInCountdown - ((double)elapsed / (double)START_TIME) * secondsInCountdown;
		break;
	    case SHOW_SCORE:
		secondsInCountdown = SCORE_TIME / 30;
		progress = secondsInCountdown - ((double)elapsed / (double)START_TIME) * secondsInCountdown;
		break;
	    default:
		System.out.println("Bad state in getCountdown()");
		throw new RuntimeException();
	}
	return (int)progress;
    }

    @Override
    public JSONObject serialize() {
	JSONObject stageJson = stage.serialize();
	JSONArray playersJson = new JSONArray();
	for (int i = 0; i < 2; i++) {
	    if (players[i] != null) {
		JSONObject paddlePosition = new JSONObject();
		paddlePosition.put("x", i * 400);
		paddlePosition.put("y", players[i].paddlePosition);
		playersJson.add(paddlePosition);
	    }
	}
	stageJson.put("players", playersJson);
	return stageJson;
    }
}
