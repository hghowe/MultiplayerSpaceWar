package MSWServer;

public class MSWS_Projectile extends GameElement
{
	private double age;
	public MSWS_Projectile(double x, double y, double angle, double xV, double yV)
	{
		super(x, y, angle, xV, yV);
		age = 0.0;
		radius = 1;
	}
	
	public void makeMove(double dT)
	{
		super.makeMove(dT);
		age+= dT;
		if (age>PROJECTILE_LIFETIME)
			die();
	}

}
