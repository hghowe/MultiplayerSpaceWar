package MSWServer;

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



public class MSWServer extends TimerTask implements Shared.Constants
{
	private int nextAvailableID;
	private ServerSocket mySocket;
	private Map<Integer, MSWS_Player> players;
	private Date lastUpdate;
	
	private List<GameElement> gameElements;
	
	
	
	public MSWServer()
	{
		super();
		nextAvailableID = 0;
		lastUpdate = new Date();
		Timer t = new Timer();
		t.scheduleAtFixedRate(this, 0, 20);
		players = new HashMap<Integer, MSWS_Player>();
		setupNetworking();
		
		gameElements = new ArrayList<GameElement>();
		
		
		
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
		
		plan(dt);
		move(dt);
		detect();
		prune();
		announce();
			
	}
	
	/**
	 * handle all pre-move decisions, such as spawning bullets, powerups, etc.
	 */
	public void plan(double dt)
	{
		// nothing yet... will be spawning.
		
	}
	/**
	 * change the locations of all the gameElements, as needed.
	 */
	public void move(double dt)
	{
		
	}
	
	/**
	 * check for collisions, end-of-life timers; potentially tell game elements to die or to change a state.
	 */
	public void detect()
	{
		
	}
	
	/**
	 * go through all the game elements and remove any one that isDead from all lists.
	 */
	public void prune()
	{
		
	}
	
	/**
	 * build the status string and broadcast it.
	 */
	public void announce()
	{
		
	}
	
	/**
	 * send the message type string and the long param to all players.
	 * @param messageType
	 * @param longParam
	 */
	public void broadcast(int messageType, String longParam)
	{
		String message = messageTypes[messageType]+"\t"+longParam;
		for (Integer id: players.keySet())
			players.get(id).sendMessage(message);
	}
	
	public void broadcast(int messageType, String[] params)
	{
		String message = messageTypes[messageType];
		for (String s:params)
			message+="\t"+s;
		
		for (Integer id: players.keySet())
			players.get(id).sendMessage(message);
	}
	
	public void handleMessage(String message, int playerID)
	{
		String[] messageComponents = message.split("\t");
		if (messageComponents[0].equals(messageTypes[USER_CONTROLS_MESSAGE_TYPE]))
		{
			players.get(playerID).setControls(Integer.parseInt(messageComponents[1]));
		}
	}
	
	public void disconnectClient(int whichID)
	{
		System.out.println("Disconnecting "+whichID);
		broadcast(PLAYER_LEAVING_MESSAGE_TYPE, new String[] {players.get(whichID).getName()});
		gameElements.remove(players.get(whichID));
		players.remove(whichID);
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
