import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;

public class Visuals
{
	public final JFrame window;
	private JButton board[][];
	private JMenuBar menuBar;
	private JMenu menu;
	private JMenuItem newGame;
	private JMenuItem quitGame;
	private ActionListener quitClick;
	private Map<String, ActionListener> actionsMap = new HashMap<>();
	
	private Visuals()
	{
		// Quit
		this.actionsMap.put("Quit", new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				System.exit(0);
			}
		} );
		
		// New Game
		this.actionsMap.put("NewGame", new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{	
				Panel p = new Panel();
				Label l = new Label("Please Enter a number 3 - 10 for number of rows/columns:");
				p.add(l);
				//p.setLayout(new FlowLayout(FlowLayout.CENTER));
				window.add(p);

				window.setVisible(true);
				
			}
		} );
		
		
		instance = this;
		this.window = new JFrame();
		this.window.setTitle("TicTacToe");
		this.window.setSize(500, 500);
		this.window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.window.setResizable(false);
		
		this.menuBar = new JMenuBar();
		this.menu = new JMenu("File");
		this.menuBar.add(this.menu);
		
		this.newGame = new JMenuItem("New Game");
		this.newGame.addActionListener(this.actionsMap.get("NewGame"));
		
		this.quitGame = new JMenuItem("Quit");
		this.quitGame.addActionListener(this.actionsMap.get("Quit"));
		
		this.menu.add(this.newGame);
		this.menu.add(this.quitGame);
		
		this.window.setJMenuBar(this.menuBar);
		this.window.setLayout(new BorderLayout());
		
		//Panel p = new Panel();
		//p.setLayout(new FlowLayout(FlowLayout.CENTER));
		//Button b = new Button("ok");
		//b.addActionListener(quitClick);
		//p.add(b);
		//this.window.add(BorderLayout.SOUTH, p);
		//this.window.add(p);

		this.window.setVisible(true);
	}
	
	private static Visuals instance;
	
	public static Visuals Instance()
	{
		// If the instance is null, then create a new instance and return it.
		// Else just return the instance.
		return instance = (instance == null) ? new Visuals() : instance;
	}
}
