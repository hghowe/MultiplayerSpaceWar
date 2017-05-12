package MSWServer;

public class MSWS_Wormhole extends GameElement
{
	public double xPos2, yPos2;
	
	public MSWS_Wormhole(double x, double y, double angle, double xV, double yV)
	{
		super(x, y, angle, xV, yV);
		// TODO Auto-generated constructor stub
		setRadius(WORMHOLE_RADIUS);
	}
	
	public MSWS_Wormhole()
	{
		this(SCREEN_WIDTH*Math.random(), SCREEN_HEIGHT*Math.random(), 0, 0, 0);
		do
		{
			xPos2 = SCREEN_WIDTH*Math.random();
			yPos2 = SCREEN_HEIGHT*Math.random();
		} while (Math.pow(getxPos()-xPos2, 2)+Math.pow(getyPos()-yPos2, 2) < 100*100);
	}

	public Double[][] getWormHoleCoordinates()
	{
		Double[][] result = new Double [2][2];
		result[0] = new Double[] {getxPos(),getyPos()};
		result[1] = new Double[] {xPos2,yPos2};
		
		return result;
	}
	
	@Override
	public String[] description()
	{
		String[] result = new String[5];
		result[0] = WORMHOLE_GAME_ELEMENT_TYPE;
		result[1] = ""+getxPos();
		result[2] = ""+getyPos();
		result[3] = ""+xPos2;
		result[4] = ""+yPos2;
		return result;
	}

}
