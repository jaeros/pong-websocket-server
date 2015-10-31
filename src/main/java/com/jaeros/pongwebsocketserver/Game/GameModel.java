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
    int id = -1;
    
    static final int START_TIME = 30 * 10; // Number of ticks for start
    static final int SCORE_TIME = 30 * 5;
    static final int MAX_SCORE = 10;

    public GameModel() {
	stage = new GameStage(400, 400, this);
	state = GameState.NEW_GAME;
    }
    
    public boolean canJoin() {
	return state == GameState.NEW_GAME || state == GameState.WAIT_P2 && 
		(players[0] == null || players[1] == null);
    }
    
    private void setState(GameState state) {
	this.state = state;
	elapsed = 0;
	System.out.println("State changed to " + state.toString());
	setChanged();
	notifyObservers(ServerMessageTypes.CHANGE_STATE);
    }
    
    public GameStage getStage() {
	return stage;
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
		updateGame();
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
    
    private void updateGame() {
	stage.tick();
	setChanged();
	notifyObservers(ServerMessageTypes.GAME_UPDATE);
    }
    
    void playerScored(int pid) {
	players[pid].addPoint();
	if (players[pid].getScore() >= MAX_SCORE) 
	    setState(GameState.GAME_OVER);
	else
	    setState(GameState.SHOW_SCORE);
    }
    
    public int getCountdown() {
	
	int secondsInCountdown;
	double progress = 0;
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
		System.out.println("Bad state in getCountdown(): " + state.toString());
		break;
	}
	return (int)progress;
    }
    
    public void setId(int n) {
	id = n;
    }
    
    public int getId() {
	return id;
    }
    
    public Player[] getPlayers() {
	return players;
    }

    @Override
    public JSONObject serialize() {
	JSONObject stageJson = stage.serialize();
	JSONArray playersJson = new JSONArray();
	for (int i = 0; i < 2; i++) {
	    if (players[i] != null) {
		JSONObject playerData = new JSONObject();
		playerData.put("x", i == 0 ? 0 : 380);
		playerData.put("y", players[i].getPaddlePosition());
		playerData.put("score", players[i].getScore());
		playersJson.add(playerData);
	    }
	}
	stageJson.put("players", playersJson);
	return stageJson;
    }
    
    public JSONArray getScore() {
	JSONArray scores = new JSONArray();
	scores.add(players[0].getScore());
	scores.add(players[1].getScore());
	return scores;
    }
}
