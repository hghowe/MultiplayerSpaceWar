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
	private double timeSinceLastShot;
	
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
		timeSinceLastShot = 0;
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
	
	/**
	 * if there has been enough time since the last shot, creates a bullet just outside of player's radius with an appropriate velocity;
	 * otherwise, returns null.
	 * @param dT
	 * @return a new projectile (or null, if not enough time has passed since last shot).
	 */
	public MSWS_Projectile fire(double dT)
	{
		timeSinceLastShot += dT;
		if (timeSinceLastShot < PROJECTILE_TIME_BETWEEN_SHOTS)
			return null;
		double spawnX = getxPos()+(getRadius()+1)*Math.cos(getBearing());
		double spawnY = getyPos()+(getRadius()+1)*Math.sin(getBearing());
		double vX = getxVel() + PROJECTILE_MUZZLE_VELOCITY*Math.cos(getBearing());
		double vY = getyVel() + PROJECTILE_MUZZLE_VELOCITY*Math.sin(getBearing());
		MSWS_Projectile proj = new MSWS_Projectile(spawnX, spawnY, getBearing(),vX,vY);
		timeSinceLastShot = 0;
		return proj;		
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
