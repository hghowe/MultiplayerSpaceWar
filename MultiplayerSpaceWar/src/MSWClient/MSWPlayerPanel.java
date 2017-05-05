package MSWClient;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.LayoutManager;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

public class MSWPlayerPanel extends JPanel implements Shared.Constants
{
	/**
	 * a list of the items that should be drawn, each in the form of a list of strings.
	 */
	private List<List<String>> itemsInWorld;
	
	public MSWPlayerPanel()
	{
		super(true);
		setBackground(Color.BLACK);
		itemsInWorld = new ArrayList<List<String>>();
	}

	public void beginGame()
	{
		
		
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g); // clears the screen
		for (List<String> item: itemsInWorld)
		{
			String type = item.get(0);
			if (type.equals(PLAYER_GAME_ELEMENT_TYPE))
			{
				// draw a player, based on the values in item, many of which will need to be converted from String to double, int or boolean.
				// item format:
				//     type(String) --> id(int) --> xPos(double) --> yPos(double) --> bearing(double) --> isThrusting(double) --> health (double)
				//          --> powerupType (int) --> isUsingpowerup (boolean) --> powerupDuration (double)
			}
			else if (type.equals(PROJECTILE_GAME_ELEMENT_TYPE))
			{
				// draw a projectile, based on the values in item, many of which will need to be converted from String to double, int or boolean.
				// item format:
				//     type(String) --> ownerId(int) --> xPos(double) --> yPos(double)
			}
			// ... etc.
		}
		
	}

}
