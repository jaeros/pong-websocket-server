
package com.jaeros.pongwebsocketserver.Game;

/**
 *
 * @author jeff
 */
public class Player {
    
    public Player() {
	this("");
    }
    
    public Player(String name) {
	this.name = name;
	paddlePosition = 0;
    }
    
    String name;
    double paddlePosition;
}
