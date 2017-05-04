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
	
	final int SCREEN_WIDTH = 800;
	final int SCREEN_HEIGHT = 800;
	
	final double ANGULAR_ACCELERATION_OF_PLAYER = Math.PI/15;
	final double THRUST_OF_PLAYER = 10.0;
	
	
}
