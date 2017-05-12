package MSWServer;

import java.util.ArrayList;
import java.util.List;

public class MSWS_Asteroid extends GameElement
{
	private int whichSize;
	private double health;
	
	public MSWS_Asteroid(double x, double y, double angle, double xV, double yV, int size)
	{
		super(x, y, angle, xV, yV);
		whichSize = size;
		health = ASTEROID_STARTING_HEALTH[whichSize];
	}
	
	public MSWS_Asteroid(double x, double y, int size)
	{
		this(x,y,0,0,0,size);
		double speed = MAX_ASTEROID_SPEED * Math.random();
		double theta = Math.PI * 2 * Math.random() - Math.PI;
		setxVel(speed*Math.cos(theta));
		setyVel(speed*Math.sin(theta));
	}

	public void addVelocity(double deltaVX, double deltaVY)
	{
		setxVel(getxVel()+deltaVX);
		setyVel(getyVel()+deltaVY);
	}
	
	public void takeHit(double damage)
	{
		health-= damage;	
	}
	
	public double getHealth() { return health;}
	
	@Override
	public int getRadius()
	{
		return ASTEROID_RADIUS_LEVELS[whichSize];
	}
	
	public List<MSWS_Asteroid> getChildren()
	{
		List<MSWS_Asteroid> result = new ArrayList<MSWS_Asteroid>();
		if (whichSize == 0)
			return result;
		MSWS_Asteroid first = new MSWS_Asteroid(getxPos(), getyPos(),whichSize-1);
		MSWS_Asteroid second = new MSWS_Asteroid(getxPos(), getyPos(),whichSize-1);
		double xVelSum = first.getxVel()+second.getxVel();
		double yVelSum = first.getyVel()+second.getyVel();
		MSWS_Asteroid third = new MSWS_Asteroid(getxPos(), getyPos(), 0, -xVelSum, -yVelSum, whichSize-1 );
		result.add(first);
		result.add(second);
		result.add(third);
		for (MSWS_Asteroid ast: result)
			ast.addVelocity(getxVel(), getyVel());
		
		return result;
	}
	
	
	@Override
	public String[] description()
	{
		String[] result = new String[4];
		result[0] = ASTEROID_GAME_ELEMENT_TYPE;
		result[1] = ""+getxVel();
		result[2] = ""+getyVel();
		result[3] = ""+getRadius();
		return result;
	}

}
