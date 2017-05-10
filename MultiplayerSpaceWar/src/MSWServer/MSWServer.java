package MSWServer;

import java.awt.GridLayout;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;



public class MSWServer extends TimerTask implements Shared.Constants
{
	private int nextAvailableID;
	private ServerSocket mySocket;
	private Map<Integer, MSWS_Player> players;
	private Date lastUpdate;
	
	private List<GameElement> gameElements;
	private List<MSWS_Projectile> projectiles;
	private List<MSWS_Powerup> powerups;
	

	double timeSinceLastPowerup = 0;
	private JFrame statusWindow;
	private StatusPanel statusPanel;
	
	public MSWServer()
	{
		super();
		
		setupStatusWindow();
		
		nextAvailableID = 137;
		lastUpdate = new Date();
		Timer t = new Timer();
		players = new HashMap<Integer, MSWS_Player>();
		projectiles = new ArrayList<MSWS_Projectile>();
		powerups = new ArrayList<MSWS_Powerup>();
		gameElements = new ArrayList<GameElement>();
		t.scheduleAtFixedRate(this, 0, 20);
		
		setupNetworking();
		
		
		
		
		
	}
	
	public void setupStatusWindow()
	{
		statusWindow = new JFrame("ServerStatus");
		statusWindow.setSize (800,800);
		statusWindow.getContentPane().setLayout(new GridLayout(1,1));
		statusPanel = new StatusPanel();
		statusWindow.getContentPane().add(statusPanel);
		statusWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		statusWindow.setResizable(false);
		statusWindow.setVisible(true);
	}
	

	public void setupNetworking()
	{
		try
		{
			mySocket = new ServerSocket(5000);
			while (true)
			{
				System.out.println("Waiting for next client");
				Socket clientSocket = mySocket.accept();
				System.out.println("Got one.");
				
				PrintWriter pw = new PrintWriter(clientSocket.getOutputStream());
				ClientReader cr = new ClientReader(clientSocket, pw);
				MSWS_Player nextPlayer = new MSWS_Player(cr.getName(),nextAvailableID, pw);
				players.put(nextAvailableID, nextPlayer);
				gameElements.add(nextPlayer);
				broadcast(NEW_PLAYER_MESSAGE_TYPE,new String[]{""+nextAvailableID, cr.getName()});
				sendPlayerList();
				statusPanel.setInput(nextAvailableID,0);
				statusPanel.setAngle(nextAvailableID, 0);
				nextAvailableID++;
			}
		}
		catch (IOException ioExcp)
		{
			ioExcp.printStackTrace();
		}
	}
	
	
	
	
	
	public void run()
	{
		 // game loop
		Date currentTime = new Date();
		double dT = (currentTime.getTime() - lastUpdate.getTime()) / 1000.0;
		lastUpdate = currentTime;
		
		plan(dT);
		move(dT);
		detect();
		prune();
		announce();
			
	}
	
	/**
	 * handle all pre-move decisions, such as spawning bullets, powerups, etc.
	 */
	public void plan(double dT)
	{
		timeSinceLastPowerup += dT;
		
		for (Integer id: players.keySet())
		{
			MSWS_Player player = players.get(id);
			if ((player.getControls() & FIRE_COMMAND) > 0)
			{
				List<MSWS_Projectile> proj = player.fire(dT);
				if (proj == null)
					continue;
				
				projectiles.addAll(proj);
				gameElements.addAll(proj);
			}
			if ((player.getControls() & USE_POWERUP_COMMAND) > 0 && player.getPowerupType() == POWERUP_TELEPORT)
			{
				player.setxPos(SCREEN_WIDTH*Math.random());
				player.setyPos(SCREEN_HEIGHT*Math.random());
				player.resetPowerup();
			}
		}
		
		if (powerups.size() < MAX_NUM_OF_POWERUPS && POWERUP_SPAWN_CONSTANT*Math.random() < timeSinceLastPowerup)
		{
			MSWS_Powerup pUp = new MSWS_Powerup();
			powerups.add(pUp);
			gameElements.add(pUp);
		}
			
			
	}
	/**
	 * change the locations of all the gameElements, as needed.
	 */
	public void move(double dT)
	{
		for (GameElement element: gameElements)
		{
			element.makeMove(dT);
		}
		
		for (Integer id: players.keySet())
			statusPanel.setAngle(id, players.get(id).getBearing());
	}
	
	/**
	 * check for collisions, end-of-life timers; potentially tell game elements to die or to change a state.
	 */
	public void detect()
	{
		detectProjectilePlayerCollisions();
		detectPowerupPlayerCollisions();
	}
	
	/**
	 * go through all the game elements and remove any one that isDead from all lists.
	 */
	public void prune()
	{
		for (int i = 0; i<gameElements.size(); i++)
		{
			if (gameElements.get(i).isDead())
			{
				if (gameElements.get(i) instanceof MSWS_Projectile)
					projectiles.remove(gameElements.get(i));
				
				if (gameElements.get(i) instanceof MSWS_Powerup)
					powerups.remove(gameElements.get(i));
				
				gameElements.remove(i);
				i--; // since the next item just slotted into position i... we don't want to skip it.
			}
		}
	}
	
	/**
	 * build the status string and broadcast it.
	 */
	public void announce()
	{
		List<String> messageParts = new ArrayList<String>();
		for (GameElement element: gameElements)
		{
			String[] elementParts = element.description();
			for (String s: elementParts)
			{
				messageParts.add(s);
			}
		}
		broadcast(UPDATE_MESSAGE_TYPE, messageParts.toArray(new String[messageParts.size()]));
	}
	
	public void detectProjectilePlayerCollisions()
	{
		for (MSWS_Projectile proj: projectiles)
			for (Integer playerID: players.keySet())
			{
				MSWS_Player player = players.get(playerID);
				if (player.getPowerupType() == POWERUP_SHIELD && player.isUsingPowerup()) // immune to projectiles
					continue;
				double d_squared = Math.pow(proj.getxPos()-player.getxPos(), 2)+Math.pow(proj.getyPos()-player.getyPos(),2);
				double thresholdSquared = Math.pow(proj.getRadius()+player.getRadius(), 2);
				if (d_squared < thresholdSquared)
				{
					player.getHurt(proj.getDamage());
					proj.die();
					break;
				}
			}
	}
	
	public void detectPowerupPlayerCollisions()
	{
		for (Integer playerID: players.keySet())
		{
			MSWS_Player player = players.get(playerID);
			for (MSWS_Powerup pUp: powerups)
			{
				if (pUp.isDead()) // prevents two players from getting the same powerup.
				{ break; }
				
				double d_squared = Math.pow(pUp.getxPos()-player.getxPos(), 2)+Math.pow(pUp.getyPos()-player.getyPos(),2);
				double thresholdSquared = Math.pow(pUp.getRadius()+player.getRadius(), 2);
				if (d_squared < thresholdSquared)
				{
					pUp.die();
					int whichPowerup = (int)((POWERUP_NAMES.length-2)*Math.random())+2; // the +/- 2 here is because we are skipping UNKNOWN and NONE.
					player.setPowerup(whichPowerup, POWERUP_IS_IMMEDIATE[whichPowerup], POWERUP_START_DURATION[whichPowerup]);
					player.sendMessage(DISPLAY_MESSAGE_TYPE+"\t"+"You just picked up "+POWERUP_NAMES[whichPowerup]);
				}
			}
		}
		
	}
	
	/**
	 * send the message type string and the long param to all players.
	 * @param messageType
	 * @param longParam
	 */
	public void broadcast(int messageType, String longParam)
	{
		String message = MESSAGE_TYPE_STRINGS[messageType]+"\t"+longParam;
		for (Integer id: players.keySet())
			players.get(id).sendMessage(message);
	}
	
	public void broadcast(int messageType, String[] params)
	{
		String message = MESSAGE_TYPE_STRINGS[messageType];
		for (String s:params)
			message+="\t"+s;
		
		for (Integer id: players.keySet())
			players.get(id).sendMessage(message);
	}
	
	public void handleMessage(String message, int playerID)
	{
		String[] messageComponents = message.split("\t");
//		System.out.println("Got message:\t"+messageComponents[0]);
		if (messageComponents[0].equals(MESSAGE_TYPE_STRINGS[USER_CONTROLS_MESSAGE_TYPE]))
		{
			players.get(playerID).setControls(Integer.parseInt(messageComponents[1]));
			statusPanel.setInput(playerID, Integer.parseInt(messageComponents[1]));
			statusPanel.setAngle(playerID, players.get(playerID).getBearing());
		}
	}
	
	public void disconnectClient(int whichID)
	{
		System.out.println("Disconnecting "+whichID);
		broadcast(PLAYER_LEAVING_MESSAGE_TYPE, new String[] {players.get(whichID).getName()});
		gameElements.remove(players.get(whichID));
		players.remove(whichID);
		statusPanel.clearInput(whichID);
		statusPanel.clearAngle(whichID);
		this.sendPlayerList();
	}
	
	public void sendPlayerList()
	{
		int numPlayers = players.size();
		String[] message = new String[numPlayers*2];
		int i = 0;
		for (Integer id: players.keySet())
		{
			message[2*i] = ""+id;
			message[2*i+1] = players.get(id).getName();
		}
		broadcast(PLAYER_LIST_MESSAGE_TYPE,message);
	}
	
	private class ClientReader implements Runnable
	{
		private Socket mySocket;
		private PrintWriter myPrintWriter;
		private Scanner myScanner;
		
		private String myName;
		private int myID;
		
		public ClientReader(Socket s, PrintWriter pw)
		{
			mySocket = s;
			myPrintWriter = pw;
			try
			{
				myScanner = new Scanner(mySocket.getInputStream());
				myName = myScanner.nextLine();
				myID = nextAvailableID;
				myPrintWriter.println(myID);
				myPrintWriter.flush();
				new Thread(this).start();
			}
			catch (IOException ioExcp)
			{
				ioExcp.printStackTrace();
			}
		}
		
		public String getName() {return myName;}
		
		@Override
		public void run()
		{
			try
			{
				while(true)
				{
					handleMessage(myScanner.nextLine(), myID);
				}
			}
			catch(NoSuchElementException nsExcp)
			{
				disconnectClient(myID);
			}
			
		}
	}

}
