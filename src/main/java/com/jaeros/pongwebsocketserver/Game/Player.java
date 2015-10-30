
package com.jaeros.pongwebsocketserver.Game;

/**
 *
 * @author jeff
 */
public class Player {
    
    GameModel game;
    String name;
    
    public Player(GameModel game) {
	this(game, "");
    }
    
    public Player(GameModel game, String name) {
	this.game = game;
	this.name = name;
	paddlePosition = 0;
    }
    
    public void setPaddlePosition(double y) {
	paddlePosition = y;
    }
    
    public GameModel getGame() {
	return game;
    }
    
    double paddlePosition;
}
