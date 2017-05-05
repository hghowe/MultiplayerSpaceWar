package MSWClient;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.LayoutManager;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class MSWPlayerPanel extends JPanel implements Shared.Constants, KeyListener
{
	private String myName;
	private int myID;
	private Socket mySocket;
	private Scanner mySocketScanner;
	private PrintWriter mySocketWriter;
	private final String IP_ADDRESS = "10.1.27.175"; // change this as needed.
	private final int CHANNEL = 5000;
	private Thread readerThread;
	
	
	// A list of the items that should be drawn, each in the form of a list of strings. 
	private List<List<String>> itemsInWorld;
	
	// a map that connects playerIDs with their names.
	private Map<Integer,String> playerNames;
	
	private boolean currentlyDrawing;
	private boolean leftIsPressed, rightIsPressed, fireIsPressed, thrustIsPressed, powerupIsPressed;
	
	
	public MSWPlayerPanel()
	{
		super(true);
		setBackground(Color.BLACK);
		itemsInWorld = new ArrayList<List<String>>();
		currentlyDrawing = false;
		leftIsPressed    = false;
		rightIsPressed   = false;
		fireIsPressed    = false;
		thrustIsPressed  = false;
		powerupIsPressed = false;
		playerNames = new HashMap<Integer,String>();
	}

	public void beginGame()
	{
		
		
	}
	
	public void setupNetworking()
	{
		do
		{
		   myName = JOptionPane.showInputDialog("Please enter your name.");
		} while (myName.equals(""));
		System.out.println("Awaiting connection to server.");
		try
		{
			mySocket = new Socket(IP_ADDRESS, CHANNEL);
			mySocketScanner = new Scanner(mySocket.getInputStream());
			mySocketWriter = new PrintWriter(mySocket.getOutputStream());
			readerThread = new Thread(new IncomingReader());
		
			readerThread.start();
			mySocketWriter.println(myName);
			mySocketWriter.flush();
			System.out.println("Connected.");
		}
		catch (IOException ioExcp)
		{
			System.out.println("I couldn't connect.");
			ioExcp.printStackTrace();
		}
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g); // clears the screen
		currentlyDrawing = true; // "lock" itemsInWorld, so that we don't accidentally change it while you are drawing.
		for (List<String> item: itemsInWorld)
		{
			String type = item.get(0);
			if (type.equals(PLAYER_GAME_ELEMENT_TYPE))
			{
				// draw a player, based on the values in item, many of which will need to be converted from String to double, int or boolean.
				// item format:
				//     type(String) --> id(int) --> xPos(double) --> yPos(double) --> bearing(double) --> isThrusting(double) --> health (double)
				//          --> powerupType (int) --> isUsingpowerup (boolean) --> powerupDuration (double)
				//------------------------
				// TODO: write your code here.
				
				
				//------------------------

			}
			else if (type.equals(PROJECTILE_GAME_ELEMENT_TYPE))
			{
				// draw a projectile, based on the values in item, many of which will need to be converted from String to double, int or boolean.
				// item format:
				//     type(String) --> ownerId(int) --> xPos(double) --> yPos(double)
				//------------------------
				// TODO: write your code here.
				
				
				//------------------------

			}
			// ... etc.
			//------------------------
			// TODO: write your code here. Keep going with the other types!
			
			
			//------------------------

			
			// Optionally, draw a message from the server onscreen.
			//------------------------
			// TODO: write your code here.
			
			
			//------------------------

		}
		currentlyDrawing = false;
	}
	
	/**
	 * You are receiving a long array of Strings, which you should convert into an arrayList of arrayLists of strings, of varying lengths.
	 * @param messageElements
	 */
	public void updateItemsInWorldFromMessage(String[] messageElements)
	{
		List<List<String>> workingList = new ArrayList<List<String>>();
		int index = 1;
		while (index<messageElements.length)
		{
			ArrayList<String> itemInfo = new ArrayList<String>();
			String itemType = messageElements[index];
			int numElements = getNumElementsForType(itemType);
			// now collect the next numElements of Strings from messageElements (including the type you just grabbed) and add each one to the
			//    itemInfo. This will require a loop of some sort. Be thoughtful - you are adding the itemType first, and that counts towards 
			//    your count!
			//---------------------
			// TODO: add your code here.
			
			//---------------------
			workingList.add(itemInfo);
		}
		
		while (currentlyDrawing)
			; // wait until paintComponent finishes so we can change the list between cycles.
		itemsInWorld = workingList;
		repaint(); // set the flag that the screen needs to be refreshed - the paintComponent() method will be called soon (automatically).
	}
	
	/**
	 * figures out how many strings should be in a complete description of one of these objects.
	 * @param type - the string description of the type
	 * @return - the number of elements that will describe this item.
	 */
	public int getNumElementsForType(String type)
	{
		if (type.equals(PLAYER_GAME_ELEMENT_TYPE))
			return NUM_VALUES_IN_PLAYER_DESCRIPTION;
		if (type.equals(PROJECTILE_GAME_ELEMENT_TYPE))
			return NUM_VALUES_IN_PROJECTILE_DESCRIPTION;
		if (type.equals(POWERUP_GAME_ELEMENT_TYPE))
			return NUM_VALUES_IN_POWERUP_DESCRIPTION;
		if (type.equals(ASTEROID_GAME_ELEMENT_TYPE))
			return NUM_VALUES_IN_ASTEROID_DESCRIPTION;
		if (type.equals(WORMHOLE_GAME_ELEMENT_TYPE))
			return NUM_VALUES_IN_WORMHOLE_DESCRIPTION;
		// uh-oh. If we got here, there's a problem.
		throw new RuntimeException("Tried to get number of elements for type that isn't one of our types: \""+type+"\"");
	}
	
	public void parseMessage(String message)
	{
		String[] parts = message.split("\t"); // split message by tabs.
		if (parts[0].equals(messageTypes[NEW_PLAYER_MESSAGE_TYPE]))
		{
			handlePlayerAddedMessage(parts);
		}
		if (parts[0].equals(messageTypes[UPDATE_MESSAGE_TYPE]))
		{
			updateItemsInWorldFromMessage(parts);
		}
		if (parts[0].equals(messageTypes[PLAYER_LIST_MESSAGE_TYPE]))
		{
			handlePlayerListMessage(parts);
		}
		if (parts[0].equals(messageTypes[PLAYER_LEAVING_MESSAGE_TYPE]))
		{
			handlePlayerLeavingMessage(parts);
		}
		if (parts[0].equals(messageTypes[DISPLAY_MESSAGE_TYPE]))
		{
			handleDisplayMessage(parts);
		}
		
	}
	
	/**
	 * the server has just sent an updated list of id's and matching names. Update our list of names.
	 * @param message
	 */
	public void handlePlayerListMessage(String[] message)
	{
		// I've written this one for you.
		Map<Integer,String> temp = new HashMap<Integer,String>();
		int index = 1;
		while (index<message.length)
		{
			int id = Integer.parseInt(message[index]);
			temp.put(id, message[index+1]);
			index += 2;
		}
		while (currentlyDrawing) // wait for the paint component thread to finish.
			;
		playerNames = temp;
	}
	
	public void handlePlayerAddedMessage(String[] message)
	{
		// not currently implemented. You might want to send a message onscreen, "Bob has joined the game."
	}
	
	public void handlePlayerLeavingMessage(String[] message)
	{
		// not currently implemented. You might want to send a message onscreen, "Bob has just left the game."
	}
	
	public void handleDisplayMessage(String[] message)
	{
		// not currently implemented. You might want to display this message, which might be something like "Bob has just died and respawned."
	}
	
	/**
	 * turns the various leftIsPressed, rightIsPressed, etc booleans into a single int, based on the instructions in the game bible.
	 * You may want to use Constants TURN_LEFT_COMMAND, TURN_RIGHT_COMMAND, etc.
	 * @return a binary-coded int that describes the state of the keys.
	 */
	public int generateKeyCommandCode()
	{
		//------------------------
		// TODO: write your code here.
		
		
		//------------------------
		return 0; // obviously, replace this.
	}
	//*********************************************************** KEY LISTENER METHODS
	
	/**
	 * the user just pressed a key. Update the state of the pressed booleans and then send a message to the server if they changed.
	 * @param kEvt
	 */
	public void keyPressed(KeyEvent kEvt)
	{
		/*
		 * There are two ways of recognizing keys.
		 * #1 - works for normal letter keys, no arrows, shift, delete etc.
		 *     (pretty intuitive)
		 * if (kEvt.getKeyChar() == 'a')...
		 * 
		 * #2 - works for all keys
		 *     (slightly more complicated)
		 * if (kEvt.getKeyCode() == KeyEvent.VK_UP)... // or VK_A, or VK_SPACE, or VK_DELETE, etc.
		 */
		
		//------------------------
		// TODO: write your code here.
		
		
		//------------------------
	}
	
	/**
	 * the user just let go of a key. Update the state of the pressed booleans and then send a message to the server if they changed.
	 * @param kEvt
	 */
	public void keyReleased(KeyEvent kEvt)
	{
		// see note in keyPressed.
		//------------------------
		// TODO: write your code here.
		
		
		//------------------------
	}
	
	public void keyTyped(KeyEvent kEvt)
	{
		// do nothing in this method - it is for when you have pressed-and-released a key, not good for gaming.
		// "keyTyped" is part of the KeyListener interface, so we have to have one... but it doesn't have to do anything.
	}

	// ******************************************************* IncomingReader internal class
	public class IncomingReader implements Runnable
	{
		public void run()
		{
			try
			{
				// the first thing we receive from the server is an id number.
				myID = Integer.parseInt(mySocketScanner.nextLine());
				
				while(true)
				{
					parseMessage(mySocketScanner.nextLine());
				}
				
			}
			catch (NoSuchElementException nseExcp)
			{
				JOptionPane.showConfirmDialog(null,"Lost connection.");
				System.exit(1);;
				
			}
		}
	}
	
}
