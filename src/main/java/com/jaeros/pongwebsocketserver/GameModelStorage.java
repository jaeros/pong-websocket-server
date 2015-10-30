package com.jaeros.pongwebsocketserver;

import com.jaeros.pongwebsocketserver.Game.GameModel;
import com.jaeros.pongwebsocketserver.Game.Player;
import java.util.Observable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author Jeff
 */
public class GameModelStorage extends Observable implements Runnable, JsonSerializable {

    int count;
    ScheduledExecutorService executorService;

    private GameModel[] games;

    public GameModelStorage(int maxGames) {
	games = new GameModel[maxGames];
	executorService = Executors.newSingleThreadScheduledExecutor();
	count = 0;
    }

    public void start() {
	executorService.scheduleAtFixedRate(this, 33, 33, TimeUnit.MILLISECONDS);
    }

    public synchronized int joinGame(Player p) {
	int emptyIndex = -1;
	for (int i = 0; i < games.length; i++) {
	    if (games[i] != null) {
		if (games[i].canJoin()) {
		    return games[i].join(p);
		}
	    } else if (emptyIndex == -1) {
		emptyIndex = i;
	    }
	}
	if (emptyIndex != -1) {
	    games[emptyIndex] = new GameModel();
	    return emptyIndex;
	}
	return -1;
    }
    
    public int addGame(GameModel game) throws OutOfMemoryError {
	for (int i = 0; i < games.length; i++) {
	    if (games[i] == null) {
		games[i] = game;
		return i;
	    }
	}
	throw new OutOfMemoryError("Game pool full!");
    }

    @Override
    public void run() {
	if (count++ % 300 == 0) {
	    System.out.println("Server time: " + count);
	}
	this.tick();
    }

    private void tick() {
	for (int i = 0; i < games.length; i++) {
	    if (games[i] != null) {
		games[i].tick();
	    }
	}
	setChanged();
	notifyObservers();
    }
    
    public GameModel getGame(int gameIndex) {
	return games[gameIndex];
    }

    @Override
    public JSONObject serialize() {
	JSONObject root = new JSONObject();
	JSONArray gamesArray = new JSONArray();
	for (GameModel gameState : games) {
	    if (gameState != null) {
		gamesArray.add(gameState.serialize());
	    }
	}
	root.put("games", gamesArray);
	return root;
    }
    
}
