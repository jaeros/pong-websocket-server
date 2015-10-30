
package com.jaeros.pongwebsocketserver.Game;

/**
 *
 * @author jeff
 */
public enum GameState {
    NEW_GAME,	    // New game, no players
    WAIT_P2,	    // Single player joined, waiting for second player
    STARTING,	    // Alert players that game is starting. Automatically progress
    PLAYING,	    // Game currently in progress
    SHOW_SCORE,	    // After a point is scored, automatically returns to playing
    GAME_OVER	    // Game is completed, display winner
}
