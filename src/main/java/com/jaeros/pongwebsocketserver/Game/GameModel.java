package com.jaeros.pongwebsocketserver.Game;

import com.jaeros.pongwebsocketserver.JsonSerializable;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author Jeff
 */
public class GameModel implements JsonSerializable {

    GameStage stage;
    Player[] players = new Player[2];

    public GameModel() {
	stage = new GameStage(400, 400);
    }
    
    public boolean canJoin() {
	return players[0] == null || players[1] == null;
    }
    
    public int join(Player p) {
	if (!canJoin()) throw new RuntimeException("Game full!");
	for (int i = 0; i < 2; i++) {
	    if (players[i] == null) {
		players[i] = p;
		return i;
	    }
	}
	return -1;
    }

    public void tick() {
	stage.tick();
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
