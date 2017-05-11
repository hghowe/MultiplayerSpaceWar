package MSWServer;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.JPanel;

public class StatusPanel extends JPanel implements Shared.Constants, KeyListener
{

	private Map<Integer, Integer> inputs;
	private Map<Integer, Double> angles;
	private Date lastUpdate;
	private String state;
	
	public StatusPanel()
	{
		super();
		inputs = new TreeMap<Integer,Integer>();
		angles = new TreeMap<Integer,Double>();
		lastUpdate = new Date();
		state = "***";
		
	}
	
	public void setState(String s) {state = s;}
	
	public void setInput(int id, int value)
	{
//		System.out.println("Setting input: "+id+" --> "+value);
		if (inputs.containsKey(id))
		{
			int oldVal = inputs.get(id);
			if (oldVal != value)
			{
				inputs.put(id, value);
				repaint();
			}
			
		}
		else
		{
			inputs.put(id, value);
			repaint();
		}
	}
	
	public void setAngle(int id, double value)
	{
//		System.out.println("Setting input: "+id+" --> "+value);
		if (angles.containsKey(id))
		{
			int oldVal = inputs.get(id);
			if (oldVal != value)
			{
				angles.put(id, value);
				repaint();
			}
			
		}
		else
		{
			angles.put(id, value);
			repaint();
		}
	}
	
	public void clearInput(int id)
	{
		if (inputs.containsKey(id))
		{
			inputs.remove(id);
			repaint();
		}
	}
	
	public void clearAngle(int id)
	{
		if (angles.containsKey(id))
		{
			angles.remove(id);
			repaint();
		}
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		int counter = 0;
		for (Integer id: inputs.keySet())
		{
			int locX = 150 * (counter % 5);
			int locY = 150 * (counter / 5);
			int input = inputs.get(id);
			g.setColor(((input & TURN_LEFT_COMMAND) >0) ? Color.red : Color.darkGray);
			g.fillRect(locX, locY+20, 20, 20);
			g.setColor(Color.black);
			g.drawRect(locX,  locY+20, 20, 20);
			
			g.setColor(((input & TURN_RIGHT_COMMAND) >0) ? Color.red : Color.darkGray);
			g.fillRect(locX+40, locY+20, 20, 20);
			g.setColor(Color.black);
			g.drawRect(locX+40, locY+20, 20, 20);
			
			g.setColor(((input & THRUST_COMMAND) >0) ? Color.red : Color.darkGray);
			g.fillRect(locX+20, locY, 20, 20);
			g.setColor(Color.black);
			g.drawRect(locX+20, locY, 20, 20);
			
			g.setColor(((input & FIRE_COMMAND) >0) ? Color.red : Color.darkGray);
			g.fillRect(locX, locY+40, 60, 20);
			g.setColor(Color.black);
			g.drawRect(locX, locY+40, 60, 20);
			
			g.setColor(((input & USE_POWERUP_COMMAND) >0) ? Color.red : Color.darkGray);
			g.fillRect(locX+20, locY+20, 20, 20);
			g.setColor(Color.black);
			g.drawRect(locX+20, locY+20, 20, 20);
			
			g.drawString(""+id, locX, locY+70);
			
			g.setColor(Color.lightGray);
			g.fillOval(locX+ 60, locY, 16, 16);
			g.setColor(Color.red);
			g.drawLine(locX+68, locY+8, (int)(locX+68+10*Math.cos(angles.get(id))), (int)(locY+8+10*Math.sin(angles.get(id))));
			
			Date now = new Date();
			long difference = now.getTime() - lastUpdate.getTime();
			g.setColor(Color.DARK_GRAY);
			g.drawString(""+difference,10, 780);
			
			
		}
		
	}
	
	public void resetLastUpdate()
	{
		lastUpdate = new Date();
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		System.out.println(state);
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	
}
