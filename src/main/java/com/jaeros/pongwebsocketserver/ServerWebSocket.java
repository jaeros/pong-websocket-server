
package com.jaeros.pongwebsocketserver;

import com.jaeros.pongwebsocketserver.Game.GameModel;
import com.jaeros.pongwebsocketserver.Game.Player;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketError;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

/**
 *
 * @author jeff
 */
@WebSocket
public class ServerWebSocket implements Observer {
    
    private Session session;
    int game;
    GameModelStorage storage;
    
    public ServerWebSocket(GameModelStorage storage) {
	this.storage = storage;
    }
    
    @OnWebSocketConnect
    public void handleConnect(Session session) {
	this.session = session;
	game = storage.joinGame(new Player());
	System.out.println("Joined game " + game);
	storage.addObserver(this);
    }
    
    @OnWebSocketClose
    public void handleClose(int code, String reason) {
	System.out.println("Connection closed with statusCode=" + code + ", reason=" + reason);
    }
    
    @OnWebSocketMessage
    public void handleMessage(String message) {
	
    }
    
    @OnWebSocketError
    public void handleError(Throwable error) {
	
    }

    @Override
    public void update(Observable o, Object arg) {
	try {
	    if (session.isOpen()) {
		session.getRemote().sendString(this.storage.getGame(game).serialize().toJSONString());
	    } else {
		
	    }
	} catch (IOException ex) {
	    System.err.println("Could not send update: " + ex.getMessage());
	} catch (Exception ex) {
	    ex.printStackTrace();
	}
    }
}
