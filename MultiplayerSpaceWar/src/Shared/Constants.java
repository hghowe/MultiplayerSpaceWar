package Shared;

public interface Constants
{
	// Broadcast constants...
	final String[] MESSAGE_TYPE_STRINGS = {"NEW_PLAYER","UPDATE","PLAYER_LIST","PLAYER_LEAVING","USER_CONTROLS","DISPLAY"};
	final int NEW_PLAYER_MESSAGE_TYPE     = 0;
	final int UPDATE_MESSAGE_TYPE 	  	  = 1;
	final int PLAYER_LIST_MESSAGE_TYPE    = 2;
	final int PLAYER_LEAVING_MESSAGE_TYPE = 3;
	final int USER_CONTROLS_MESSAGE_TYPE  = 4;
	final int DISPLAY_MESSAGE_TYPE        = 5;
	
	final int BROADCAST_PERIOD = 30;

	// strings used in communicating types of game elements from server to client
	final String PLAYER_GAME_ELEMENT_TYPE = "PLAYER";
	final String PROJECTILE_GAME_ELEMENT_TYPE = "PROJECTILE";
	final String POWERUP_GAME_ELEMENT_TYPE = "POWERUP";
	final String ASTEROID_GAME_ELEMENT_TYPE = "ASTEROID";
	final String WORMHOLE_GAME_ELEMENT_TYPE = "WORMHOLE";
	
	// number of strings transmitted from server to client for each type of game element.
	final int NUM_VALUES_IN_PLAYER_DESCRIPTION = 10;
	final int NUM_VALUES_IN_PROJECTILE_DESCRIPTION = 5;
	final int NUM_VALUES_IN_POWERUP_DESCRIPTION = 3;
	final int NUM_VALUES_IN_ASTEROID_DESCRIPTION = 4;
	final int NUM_VALUES_IN_WORMHOLE_DESCRIPTION = 5;
	
	// World variables
	final int SCREEN_WIDTH = 800;
	final int SCREEN_HEIGHT = 800;
	
	// player behavior constants.
	final double ANGULAR_ACCELERATION_OF_PLAYER = 3*Math.PI/4;
	final double THRUST_OF_PLAYER = 30.0;
	final double PLAYER_MAX_VELOCITY = 75.0;
	final double PLAYER_MAX_VELOCITY_SQUARED = 5625;
	
	// projectile behavior constants
	final double PROJECTILE_MUZZLE_VELOCITY = 100.0;
	final double PROJECTILE_LIFETIME = 1.5; // seconds
	final double PROJECTILE_TIME_BETWEEN_SHOTS = 0.125; // seconds
	final int    NONE_PROJECTILE_MODIFIER = 0;
	final int 	DOUBLE_DAMAGE_PROJECTILE_MODIFIER = 1;
	
	// command codes for client to server transmission of user's controls, to be combined into one number.
	final int TURN_LEFT_COMMAND = 1;
	final int TURN_RIGHT_COMMAND = 2;
	final int THRUST_COMMAND = 4;
	final int FIRE_COMMAND = 8;
	final int USE_POWERUP_COMMAND = 16;
	
	// dimensions of objects for collision detection
	final int PLAYER_RADIUS = 7;
	final int PROJECTILE_RADIUS = 2;
	final int POWERUP_RADIUS = 10;
	
	// powerup specific
	final int POWERUP_MAX_SPEED = 75;
	final double POWERUP_SPAWN_CONSTANT = 1000;
	final int MAX_NUM_OF_POWERUPS = 3;
	
	// powerup types
	final String[] POWERUP_NAMES = {"????","","Teleport","Shield","Multishot","Heavy Shot"};
	final boolean[] POWERUP_IS_IMMEDIATE = {false, false, false, false, true, true};
	final double[] POWERUP_START_DURATION = {0, 0, -1, 2.0, -15, 7};
	final int POWERUP_UNKNOWN = 0; // player has one, but others don't know what it is... yet.
	final int POWERUP_NONE = 1;
	final int POWERUP_TELEPORT = 2;
	final int POWERUP_SHIELD = 3;
	final int POWERUP_MULTISHOT = 4;
	final int POWERUP_HEAVY_SHOT = 5;
	
	// powerup features
	final double POWERUP_MULTISHOT_SPREAD = Math.PI / 8;
	
	// asteroid features
	final double MAX_ASTEROID_SPEED = 125;
	final int[] ASTEROID_RADIUS_LEVELS = {5, 9, 15};
	final double[] ASTEROID_STARTING_HEALTH = {1.0, 1.0, 2.0};
	final int MIN_NUMBER_OF_ASTEROIDS = 4;
	final double ASTEROID_SPAWN_CONSTANT = 5000;
	
	// wormhole features
	final double MIN_WORMHOLE_SEPARATION = 100;
	final int WORMHOLE_RADIUS = 25;
	
	
	// damage levels
	final double PROJECTILE_PLAYER_DAMAGE = 1;
	final double[] ASTEROID_DAMAGE_TO_PLAYER = {2.0, 4.0, 6.0};
	
	
	
	
}
