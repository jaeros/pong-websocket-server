package com.jaeros.pongwebsocketserver.Game;

import com.jaeros.pongwebsocketserver.JsonSerializable;
import org.json.simple.JSONObject;

/**
 *
 * @author Jeff
 */
public class GameStage implements JsonSerializable {

    Ball ball;
    GameModel model;
    Vector2D dimensions;

    public GameStage(double width, double height, GameModel model) {
	ball = new Ball();
	ball.setVelocity(new Vector2D(10, 8));
	this.model = model;
	dimensions = new Vector2D(width, height);
    }

    public void tick() {
	ball.move();
	Vector2D pos = ball.getPosition();
	double paddlePosition;

	if (pos.x > dimensions.x / 2) {
	    // Check for player 2 paddle
	    paddlePosition = model.getPlayers()[1].getPaddlePosition();
	    if (pos.y > paddlePosition - 50 && pos.y < paddlePosition + 50) {
		double moveX = 2 * ((dimensions.x / 2) - pos.x);
		pos.translate(moveX, 0);
		ball.setVelocity(new Vector2D(-1 * ball.getVelocity().x, ball.getVelocity().y));
	    } else {
		ball.setPosition(new Vector2D(0,0));
		model.playerScored(1);
	    }
	} else if (pos.x < -(dimensions.x / 2)) {
	    // Check for player 1 paddle
	    paddlePosition = model.getPlayers()[0].getPaddlePosition();
	    if (pos.y > paddlePosition - 50 && pos.y < paddlePosition + 50) {
		double moveX = 2 * (-(dimensions.x / 2) - pos.x);
		pos.translate(moveX, 0);
		ball.setVelocity(new Vector2D(-1 * ball.getVelocity().x, ball.getVelocity().y));
	    } else {
		ball.setPosition(new Vector2D(0,0));
		model.playerScored(0);
	    }
	} 
	
	if (pos.y > dimensions.y / 2) {
	    double moveY = 2 * ((dimensions.y / 2) - pos.y);
	    pos.translate(0, moveY);
	    ball.setVelocity(new Vector2D(ball.getVelocity().x, -1 * ball.getVelocity().y));
	} else if (pos.y < -(dimensions.y / 2)) {
	    double moveY = 2 * (-(dimensions.y / 2) - pos.y);
	    pos.translate(0, moveY);
	    ball.setVelocity(new Vector2D(ball.getVelocity().x, -1 * ball.getVelocity().y));
	}
    }

    @Override
    public JSONObject serialize() {
	return ball.serialize();
    }
}
