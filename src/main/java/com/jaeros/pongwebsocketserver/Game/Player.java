
package com.jaeros.pongwebsocketserver.Game;

/**
 *
 * @author jeff
 */
public class Player {
    
    GameModel game;
    String name;
    boolean isAI;
    private double paddlePosition;
    int points;
    
    public Player(GameModel game) {
	this(game, "");
    }
    
    public Player(GameModel game, String name) {
	this.game = game;
	this.name = name;
	isAI = false;
	paddlePosition = 0;
	points = 0;
    }
    
    public void setPaddlePosition(double y) {
	paddlePosition = y;
    }
    
    public GameModel getGame() {
	return game;
    }
    
    public void setAI() {
	isAI = true;
    }
    
    public void addPoint() {
	points++;
    }
    
    public int getScore() {
	return points;
    }
    
    public double getPaddlePosition() {
	return isAI ? game.getStage().ball.getPosition().y : paddlePosition;
    }
}
