package Shared;

public interface Constants
{
	// Broadcast constants...
	final String[] messageTypes = {"NEW_PLAYER","UPDATE","PLAYER_LIST","PLAYER_LEAVING","USER_CONTROLS","DISPLAY"};
	final int NEW_PLAYER_MESSAGE_TYPE     = 0;
	final int UPDATE_MESSAGE_TYPE 	  	  = 1;
	final int PLAYER_LIST_MESSAGE_TYPE    = 2;
	final int PLAYER_LEAVING_MESSAGE_TYPE = 3;
	final int USER_CONTROLS_MESSAGE_TYPE  = 4;
	final int DISPLAY_MESSAGE_TYPE        = 5;

	final String PLAYER_GAME_ELEMENT_TYPE = "PLAYER";
	final String PROJECTILE_GAME_ELEMENT_TYPE = "PROJECTILE";
	final String POWERUP_GAME_ELEMENT_TYPE = "POWERUP";
	final String ASTEROID_GAME_ELEMENT_TYPE = "ASTEROID";
	final String WORMHOLE_GAME_ELEMENT_TYPE = "WORMHOLE";
	
	final int SCREEN_WIDTH = 800;
	final int SCREEN_HEIGHT = 800;
	
	final double ANGULAR_ACCELERATION_OF_PLAYER = Math.PI/15;
	final double THRUST_OF_PLAYER = 10.0;
	final double PLAYER_MAX_VELOCITY = 50.0;
	final double PLAYER_MAX_VELOCITY_SQUARED = 2500;
	
	final double PROJECTILE_MUZZLE_VELOCITY = 75.0;
	final double PROJECTILE_LIFETIME = 1.5; // seconds
	final double PROJECTILE_TIME_BETWEEN_SHOTS = 0.5; // seconds
	final int    NONE_PROJECTILE_MODIFIER = 0;
	
	final int TURN_LEFT_COMMAND = 1;
	final int TURN_RIGHT_COMMAND = 2;
	final int THRUST_COMMAND = 4;
	final int FIRE_COMMAND = 8;
	final int USE_POWERUP_COMMAND = 16;
	
}
