package MSWServer;

public class MSWS_Powerup extends GameElement implements Shared.Constants
{

	public MSWS_Powerup(double x, double y, double angle, double xV, double yV)
	{
		super(x, y, angle, xV, yV);
		// TODO Auto-generated constructor stub
	}

	public MSWS_Powerup()
	{
		super(0,0,0,0,0);
		setxPos(SCREEN_WIDTH*Math.random());
		setyPos(SCREEN_HEIGHT*Math.random());
		double theta = 2*Math.PI * Math.random() - Math.PI;
		setxVel(POWERUP_MAX_SPEED*Math.cos(theta));
		setyVel(POWERUP_MAX_SPEED*Math.sin(theta));
		
	}
	
	@Override
	public String[] description()
	{
		String[] result = {POWERUP_GAME_ELEMENT_TYPE, ""+getxPos(), ""+getyPos()};
		return result;
	}

}
