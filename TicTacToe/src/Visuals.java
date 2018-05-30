import java.awt.*;
import java.awt.event.*;
import java.text.NumberFormat;
import java.util.*;

import javax.swing.*;

public class Visuals
{
	public final JFrame window;
	private Container pane;
	private JButton board[][];
	private JMenuBar menuBar;
	private JMenu fileMenu;
	private JMenu sizeMenu;
	private JMenuItem[] sizeOptions;
	private JMenuItem newGame;
	private JMenuItem quitGame;
	private Map<String, ActionListener> actionsMap = new HashMap<>();
	
	private Visuals()
	{
		instance = this;

		this.window = new JFrame();
		this.window.setTitle("TicTacToe");
		this.window.setSize(500, 500);
		this.window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.window.setResizable(false);
		this.pane = this.window.getContentPane();
		
		this.InitializeMap();
		this.SetUpUIElements();

		this.window.setVisible(true);
	}
	
	private static Visuals instance;
	
	public static Visuals Instance()
	{
		// If the instance is null, then create a new instance and return it.
		// Else just return the instance.
		return instance = (instance == null) ? new Visuals() : instance;
	}

	private void InitializeMap()
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
		
		// Grid Size
		this.actionsMap.put("GridSize", new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{	// Grab reference to the source
				Object o = e.getSource();
				
				// If it is an instance of a JMenuItem
				if(o instanceof JMenuItem)
				{
					// Cast it
					JMenuItem i = (JMenuItem)o;
					// Set the max size of the grid to the number returned from the first character of the text
					int size = Character.getNumericValue(i.getText().charAt(0));
					
					// If the user clicks 10 on the grid size it will only return 1 so set it to 10
					if(size == 1)
						size = 10;
					
					// Set the max size to the size
					GameManager.Instance().maxSize = size;
				}	
			}
		});
		
		// Position
		this.actionsMap.put("Position", new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{	// Grab reference to the source
				Object o = e.getSource();
				
				// If it is an instance of a JMenuItem
				if(o instanceof JTextField)
				{
					// Cast it
					JTextField i = (JTextField)o;
					
					// Set the max size of the grid to the number returned from the first character of the text
					int x = Character.getNumericValue(i.getText().charAt(0));
					
					if(i.getName() == "XPos" && (x >= 0 && x <= GameManager.Instance().maxSize - 1))
					{
						System.out.println("XWoohoo!");
						// Set the currentPlayers xCoordinate here
					}
					else if(i.getName() == "YPos" && (x >= 0 && x <= GameManager.Instance().maxSize - 1))
					{
						// Set the currentPlayers yCoordinate here
						System.out.println("YWoohoo!");
					}
				}	
			}
		});
	}

	private void SetUpUIElements()
	{
		this.SetUpMenu();
		
		Label xCoordinateLabel = new Label("Enter X Coordinate: ");
		xCoordinateLabel.setBounds(0,10,125,10);
		
		JTextField xField = new JTextField();
		xField.setBounds(130, 5, 20, 20);
		xField.addActionListener(this.actionsMap.get("Position"));
		xField.setName("XPos");
		
		Label yCoordinateLabel = new Label("Enter Y Coordinate: ");
		yCoordinateLabel.setBounds(0,40,125,10);

		JTextField  yField = new JTextField ();
		yField.setBounds(130, 35, 20, 20);
		yField.addActionListener(this.actionsMap.get("Position"));
		yField.setName("YPos");
		
		// Set the pane layout to flow layout center. The pane will be used for printing labels
		// for the user to see.
		//this.pane.setLayout(new FlowLayout(FlowLayout.CENTER));
		this.pane.setLayout(null);
		this.pane.add(xCoordinateLabel);
		this.pane.add(xField);
		this.pane.add(yCoordinateLabel);
		this.pane.add(yField);
	}

	private void SetUpMenu()
	{
		// Initialize the options array
		this.sizeOptions = new JMenuItem[8];
		// Initialize a new menubar
		this.menuBar = new JMenuBar();
		
		// Initialize the File Menu
		this.fileMenu = new JMenu("File");
		// Initialize the Grid Size menu
		this.sizeMenu = new JMenu("Grid Size");
		// Add the file menu to the menu bar
		this.menuBar.add(this.fileMenu);
		// Add the size menu to the menu bar
		this.menuBar.add(this.sizeMenu);
		
		// Loop from 3 to 10
		for(int i = 3, j = 0; i <= 10; i++, j++)
		{
			// Initialize a new JMenuItem with the proper number then set to index of size options
			this.sizeOptions[j] = new JMenuItem(i + "x" + i);
			// Add an action listener for when the item is clicked on
			this.sizeOptions[j].addActionListener(this.actionsMap.get("GridSize"));
			// Add the option to the size menu
			this.sizeMenu.add(this.sizeOptions[j]);
		}
		
		// Initialize a the new game menu item
		this.newGame = new JMenuItem("New Game");
		// Add an action listener for when the item is clicked on
		this.newGame.addActionListener(this.actionsMap.get("NewGame"));
		// Initialize a the quit game menu item
		this.quitGame = new JMenuItem("Quit");
		// Add an action listener for when the item is clicked on
		this.quitGame.addActionListener(this.actionsMap.get("Quit"));
		
		// Add the new game item to the file menu
		this.fileMenu.add(this.newGame);
		// Add the quit game item to the file menu
		this.fileMenu.add(this.quitGame);
		
		// Set the JMenuBar in the window to the menu bar
		this.window.setJMenuBar(this.menuBar);
		
		//Panel p = new Panel();
		//p.setLayout(new FlowLayout(FlowLayout.CENTER));
		//Button b = new Button("ok");
		//b.addActionListener(quitClick);
		//p.add(b);
		//this.window.add(BorderLayout.SOUTH, p);
		//this.window.add(p);
	}

}
