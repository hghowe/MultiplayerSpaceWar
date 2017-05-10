package MSWServer;

public class MSWS_Projectile extends GameElement
{
	private double age;
	private int ownerID;
	private int modifier;
	
	public MSWS_Projectile(double x, double y, double angle, double xV, double yV, int id)
	{
		super(x, y, angle, xV, yV);
		age = 0.0;
		setRadius(1);
		ownerID = id;
		modifier = NONE_PROJECTILE_MODIFIER;
	}
	
	public MSWS_Projectile(double x, double y, double angle, double xV, double yV, int id, int mod)
	{
		this(x, y, angle, xV, yV, id);
		modifier = mod;
	}
	
	public void makeMove(double dT)
	{
		super.makeMove(dT);
		age+= dT;
		if (age>PROJECTILE_LIFETIME)
			die();
	}

	public String[] description()
	{
		String[] result = new String[5];
		result[0] = PROJECTILE_GAME_ELEMENT_TYPE;
		result[1] = ""+ownerID;
		result[2] = ""+getxPos();
		result[3] = ""+getyPos();
		result[4] = ""+modifier;
		
		return result;
	}
	
	public double getDamage()
	{
		if (modifier == DOUBLE_DAMAGE_PROJECTILE_MODIFIER)
			return 2*PROJECTILE_PLAYER_DAMAGE;
		return PROJECTILE_PLAYER_DAMAGE;
	}
	
	public void setModifier(int mod) {modifier = mod;}
	public int getModifier() {return modifier;}
}
