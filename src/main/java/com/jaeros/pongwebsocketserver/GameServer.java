
package com.jaeros.pongwebsocketserver;

import com.jaeros.pongwebsocketserver.Game.GameModel;
import com.jaeros.pongwebsocketserver.GameModelStorage;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.websocket.server.WebSocketHandler;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeRequest;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeResponse;
import org.eclipse.jetty.websocket.servlet.WebSocketCreator;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;


/**
 *
 * @author jeff
 */
public class GameServer {
    
    static GameModelStorage storage;
    
    public static void main(String[] args) {
	
	try {
	    storage = new GameModelStorage(50);
	    storage.start();
	    storage.addGame(new GameModel());
	    
	    Server server = new Server(8000);
	    server.setHandler(new GameSocketHandler());
	    
	    server.start();
	    server.join();
	} catch (Exception ex) {
	    System.out.println("Got exception: " + ex.getMessage());
	}
    }
   
    public static class GameSocketHandler extends WebSocketHandler {

	@Override
	public void configure(WebSocketServletFactory factory) {
	    factory.setCreator(new GameSocketCreator(storage));
	}
    }
    
    public static class GameSocketCreator implements WebSocketCreator {

	GameModelStorage storage;
	public GameSocketCreator(GameModelStorage storage) {
	    this.storage = storage;
	}
	
	@Override
	public Object createWebSocket(ServletUpgradeRequest req, ServletUpgradeResponse res) {
//	    System.out.println("Creating socket for: " + req.getRequestPath());
	    // Create new web socket and pass it the game storage object
	    return new ServerWebSocket(storage);
	}
	
    }
    
}
