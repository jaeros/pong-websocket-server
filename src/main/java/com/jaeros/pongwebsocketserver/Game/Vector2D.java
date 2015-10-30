/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jaeros.pongwebsocketserver.Game;

/**
 *
 * @author Jeff
 */
public class Vector2D {

    double x, y;

    public Vector2D() {
	x = 0;
	y = 0;
    }

    public Vector2D(double x, double y) {
	this.x = x;
	this.y = y;
    }

    public double getX() {
	return x;
    }

    public double getY() {
	return y;
    }

    public void set(double x, double y) {
	this.x = x;
	this.y = y;
    }

    public void translate(double x, double y) {
	this.x += x;
	this.y += y;
    }

    public void translate(Vector2D other) {
	this.translate(other.getX(), other.getY());
    }
}
