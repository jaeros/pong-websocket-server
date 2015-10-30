package com.jaeros.pongwebsocketserver.Game;

import com.jaeros.pongwebsocketserver.JsonSerializable;
import org.json.simple.JSONObject;

/**
 *
 * @author Jeff
 */
public class GameStage implements JsonSerializable {

    Ball ball;
    Vector2D dimensions;

    public GameStage(double width, double height) {
	ball = new Ball();
	ball.setVelocity(new Vector2D(10, 8));
	dimensions = new Vector2D(width, height);
    }

    public void tick() {
	ball.move();

	if (ball.getPosition().x > dimensions.x / 2) {
	    double moveX = 2 * ((dimensions.x / 2) - ball.getPosition().x);
	    ball.getPosition().translate(moveX, 0);
	    ball.setVelocity(new Vector2D(-1 * ball.getVelocity().x, ball.getVelocity().y));
	} else if (ball.getPosition().x < -(dimensions.x / 2)) {
	    double moveX = 2 * (-(dimensions.x / 2) - ball.getPosition().x);
	    ball.getPosition().translate(moveX, 0);
	    ball.setVelocity(new Vector2D(-1 * ball.getVelocity().x, ball.getVelocity().y));
	} 
	
	if (ball.getPosition().y > dimensions.y / 2) {
	    double moveY = 2 * ((dimensions.y / 2) - ball.getPosition().y);
	    ball.getPosition().translate(0, moveY);
	    ball.setVelocity(new Vector2D(ball.getVelocity().x, -1 * ball.getVelocity().y));
	} else if (ball.getPosition().y < -(dimensions.y / 2)) {
	    double moveY = 2 * (-(dimensions.y / 2) - ball.getPosition().y);
	    ball.getPosition().translate(0, moveY);
	    ball.setVelocity(new Vector2D(ball.getVelocity().x, -1 * ball.getVelocity().y));
	}
    }

    @Override
    public JSONObject serialize() {
	return ball.serialize();
    }
}
