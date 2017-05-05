/**
 * This class represents one of the players in the game, as used by the MSWServer class.
 * If you are writing a client class, write your own!
 * @author harlan.howe
 *
 */

package MSWServer;

import java.io.PrintWriter;


public class MSWS_Player extends GameElement
{
	private String myName;
	private int myID;
	private PrintWriter myPrintWriter;
	private int myUserControls;
	
	private double health;
	
	
	public MSWS_Player(String name, int id, PrintWriter pw)
	{
		super(0,0,0,0,0);
		setxPos(Math.random() * SCREEN_WIDTH);
		setyPos(Math.random() * SCREEN_HEIGHT);
		setBearing( Math.random() * 2 * Math.PI - Math.PI);
		
		setRadius(7);
		myName = name;
		myID = id;
		myPrintWriter = pw;
		
		myUserControls = 0;
		health = 15;
	}
	
	@Override
	/**
	 * makes a small step in motion for the last dt of time, using the player controls.
	 */
	public void makeMove(double dT)
	{
		if ((myUserControls & TURN_LEFT_COMMAND) > 0)
			setBearing(getBearing() + ANGULAR_ACCELERATION_OF_PLAYER * dT);
		if ((myUserControls & TURN_RIGHT_COMMAND) >0)
			setBearing(getBearing() - ANGULAR_ACCELERATION_OF_PLAYER * dT);
		if ((myUserControls & THRUST_COMMAND) > 0 )
		{
			setxVel(getxVel()+THRUST_OF_PLAYER*Math.cos(getBearing())*dT);
			setyVel(getyVel()+THRUST_OF_PLAYER*Math.sin(getBearing())*dT);
			
			double velSquared = Math.pow(getxVel(), 2) + Math.pow(getyVel(), 2);
			if (velSquared > PLAYER_MAX_VELOCITY_SQUARED)
			{
				double vel = Math.sqrt(velSquared);
				setxVel(getxVel()*PLAYER_MAX_VELOCITY/vel);
				setyVel(getyVel()*PLAYER_MAX_VELOCITY/vel);
			}
		}
		super.makeMove(dT);
	}
	
	
	public String getName() {return myName;}
	public int getID() 	    {return myID;}

	public void setControls(int control) {myUserControls = control;}
	public int  getControls()		     {return myUserControls;}

	
	public void sendMessage(String message)
	{
		myPrintWriter.println(message);
		myPrintWriter.flush();
	}
	
	public String toString()
	{
		return myID+"\t"+myName;
	}
	
	
}
