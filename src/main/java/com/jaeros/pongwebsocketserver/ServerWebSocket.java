
package com.jaeros.pongwebsocketserver;

import com.jaeros.pongwebsocketserver.Game.GameModel;
import com.jaeros.pongwebsocketserver.Game.GameState;
import com.jaeros.pongwebsocketserver.Game.Player;
import com.jaeros.pongwebsocketserver.Game.ServerMessageTypes;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketError;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

/**
 *
 * @author jeff
 */
@WebSocket
public class ServerWebSocket implements Observer {
    
    private Session session;
    Player player;
    GameModelStorage storage;
    
    public ServerWebSocket(GameModelStorage storage) {
	this.storage = storage;
    }
    
    @OnWebSocketConnect
    public void handleConnect(Session session) {
	this.session = session;
    }
    
    @OnWebSocketClose
    public void handleClose(int code, String reason) {
	System.out.println("Connection closed with statusCode=" + code + ", reason=" + reason);
	
	// Unregister observer
	player.getGame().deleteObserver(this);
    }
    
    @OnWebSocketMessage
    public void handleMessage(String message) throws IOException {
	Object obj = JSONValue.parse(message);
	JSONObject jsonMessage = (JSONObject)obj;
	
	String messageType = (String)jsonMessage.get("messageType");
	switch(messageType) {
	    case "JOIN_GAME":
		player = storage.joinGame();
		System.out.println("Joined game " + player.getGame().getId());
		player.getGame().addObserver(this);
		sendStateUpdate(session, player.getGame().getState());
		break;
	    case "PLAYER_INPUT":
		System.out.println("Got player input! " + message);
		handleInput((JSONObject)jsonMessage.get("data"));
		break;
	}
    }
    
    @OnWebSocketError
    public void handleError(Throwable error) {
	System.out.println("Welp, error: " + error.getMessage());
    }

    @Override
    public void update(Observable o, Object arg) {
	try {
	    if (session.isOpen()) {
		if (arg != null) {
		    ServerMessageTypes type = (ServerMessageTypes)arg;
		    switch (type) {
			case CHANGE_STATE:
			    sendStateUpdate(session, player.getGame().getState());
			    break;
			case COUNT_DOWN:
			    sendCountdownUpdate(session, player.getGame().getCountdown());
			    break;
			case GAME_UPDATE:
			    sendGameUpdate(session, player.getGame());
			    break;
		    }
		} else {
		    System.out.println("No server message type specified");
		    session.getRemote().sendString(player.getGame().serialize().toJSONString());
		}
	    } else {
		
	    }
	} catch (IOException ex) {
	    System.err.println("Could not send update: " + ex.getMessage());
	} catch (Exception ex) {
	    ex.printStackTrace();
	}
    }
    
    void sendStateUpdate(Session session, GameState state) throws IOException {
	JSONObject data = new JSONObject();
	data.put("messageType", ServerMessageTypes.CHANGE_STATE.toString());
	data.put("data", state.toString());
	session.getRemote().sendString(data.toJSONString());
    }
    
    void sendGameUpdate(Session session, GameModel game) throws IOException {
	JSONObject data = new JSONObject();
	data.put("messageType", ServerMessageTypes.GAME_UPDATE.toString());
	data.put("data", game.serialize());
	session.getRemote().sendString(data.toJSONString());
    }
    
    void sendCountdownUpdate(Session session, int countdownTime) throws IOException {
	JSONObject data = new JSONObject();
	data.put("messageType", ServerMessageTypes.COUNT_DOWN.toString());
	data.put("data", countdownTime);
	session.getRemote().sendString(data.toJSONString());
    }
    
    void handleInput(JSONObject data) {
	long y = (long)data.get("mouseY");
	player.setPaddlePosition(y);
    }
}
