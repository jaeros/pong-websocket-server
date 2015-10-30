package com.jaeros.pongwebsocketserver.Game;

import com.jaeros.pongwebsocketserver.JsonSerializable;
import org.json.simple.JSONObject;

/**
 *
 * @author Jeff
 */
class Ball implements JsonSerializable {

    Vector2D position, velocity;

    public Ball() {
	position = new Vector2D(0, 0);
	velocity = new Vector2D(0, 0);
    }

    public void setPosition(Vector2D pos) {
	position = pos;
    }

    public void setVelocity(Vector2D v) {
	velocity = v;
    }

    public Vector2D getPosition() {
	return position;
    }

    public Vector2D getVelocity() {
	return velocity;
    }

    public void move() {
	position.translate(velocity);
    }

    @Override
    public JSONObject serialize() {
	JSONObject game = new JSONObject();
	JSONObject ballPosition = new JSONObject();
	ballPosition.put("x", position.x);
	ballPosition.put("y", position.y);
	game.put("ball", ballPosition);
	return game;
    }
}
