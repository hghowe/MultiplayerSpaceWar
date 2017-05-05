package MSWClient;

import java.awt.GridLayout;

import javax.swing.JFrame;

public class MSW_GameRunner extends JFrame implements Shared.Constants
{
	private MSWPlayerPanel thePanel;
	
	public static void main(String[] args)
	{
		MSW_GameRunner theApp = new MSW_GameRunner();
	}
	
	public MSW_GameRunner()
	{
		super("Multiplayer Space War!");
		setSize(SCREEN_WIDTH,SCREEN_HEIGHT+16);
		getContentPane().setLayout(new GridLayout(1,1));
		thePanel = new MSWPlayerPanel();
		getContentPane().add(thePanel);
		setResizable(false);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		thePanel.beginGame();
		addKeyListener(thePanel);
		requestFocus();
	}
	

}
