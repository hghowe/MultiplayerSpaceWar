package MSWServer;

public abstract class GameElement implements Shared.Constants
{

	private double xPos, yPos;
	private double bearing;
	private int radius;
	private double xVel, yVel;
	private boolean isDead;
	
	public GameElement(double x, double y, double angle, double xV, double yV)
	{
		setxPos(x);
		setyPos(y);
		setBearing(angle);
		setxVel(xV);
		setyVel(yV);
		isDead = false;
	}
	
	/**
	 * changes this object's position, based on its velocity and the time since the last update. Wraps around the screen.
	 * @param dt - the time since the last motion.
	 * postcondition - (x,y) is within (0-SCREENWIDTH, 0-SCREENHEIGHT), inclusive.
	 */
	public void makeMove(double dT)
	{
		xPos += xVel * dT;
		yPos += yVel * dT;
		if (xPos > SCREEN_WIDTH)
			xPos -= SCREEN_WIDTH;
		if (xPos < 0)
			xPos += SCREEN_WIDTH;
		if (yPos > SCREEN_HEIGHT)
			yPos -= SCREEN_HEIGHT;
		if (yPos < 0)
			yPos += SCREEN_HEIGHT;
	}
	
	public double getxPos()
	{
		return xPos;
	}
	public void setxPos(double xPos)
	{
		this.xPos = xPos;
	}
	public double getyPos()
	{
		return yPos;
	}
	public void setyPos(double yPos)
	{
		this.yPos = yPos;
	}
	public double getBearing()
	{
		return bearing;
	}
	public void setBearing(double bearing)
	{
		this.bearing = bearing;
	}
	public int getRadius()
	{
		return radius;
	}
	public void setRadius(int radius)
	{
		this.radius = radius;
	}
	public double getxVel()
	{
		return xVel;
	}
	public void setxVel(double xVel)
	{
		this.xVel = xVel;
	}
	public double getyVel()
	{
		return yVel;
	}
	public void setyVel(double yVel)
	{
		this.yVel = yVel;
	}
	public boolean isDead()
	{
		return isDead;
	}
	public void die()
	{
		isDead = true;
	}
}
