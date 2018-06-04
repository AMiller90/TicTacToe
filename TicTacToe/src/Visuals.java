// Import for graphics
import java.awt.*;
// Import for events
import java.awt.event.*;
// Import for maps
import java.util.*;
// Import for graphics
import javax.swing.*;

// Public Visuals Class. Used for graphics that the user can interact with.
// It is a singleton and can not be inherited from.
public final class Visuals
{
	// Private final reference to the window
	private final JFrame window;
	// Private reference to the pane that objects will be drawn to.
	private Container pane;
	// Private Reference to the visual board
	private JButton board[][];
	// Private Reference to the menu bar
	private JMenuBar menuBar;
	// Private Reference to the file menu
	private JMenu fileMenu;
	// Private References to the newGame and quitGame menu items
	private JMenuItem newGame, quitGame;
	// Private References to the player turn text and win loss counter labels
	private Label playerTurnText, winLossCounter;
	// Private references to the playerAIChoice and newGameClicked variables
	private boolean playerAIChoice, newGameClicked;
	
	// Private reference to a new map for holding events
	private Map<String, ActionListener> actionsMap = new HashMap<>();
	// Private reference to a map for holding all the components in the window.
	private Map<String, Integer> componentsMap = new HashMap<>();
	
	// Private static instance reference to this class
	private static Visuals instance;
	
	// Private constructor
	private Visuals()
	{
		// Set the instance to this
		instance = this;
		
		// Set the window to a new JFrame object
		this.window = new JFrame();
		// Set the title
		this.window.setTitle("TicTacToe");
		// Set the size
		this.window.setSize(600, 600);
		// Set the default close operation
		this.window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		// Set the window re-sizable to false
		this.window.setResizable(false);
		// Set the reference to the pane on the window
		this.pane = this.window.getContentPane();
		// Set the layout to a new flow layout at the left
		this.pane.setLayout(new FlowLayout(FlowLayout.LEFT));
		// Set the newGameClicked reference to false
		this.newGameClicked = false;
		
		// Initialize the maps
		this.InitializeMaps();
		// Setup the UI Elements
		this.SetUpUIElements();

		// Set the window to be visible
		this.window.setVisible(true);
	}
	
	// Public static Instance function. Used to return the instance of this class.
	public static Visuals Instance()
	{
		// If the instance is null, then create a new instance and return it.
		// Else just return the instance.
		return instance = (instance == null) ? new Visuals() : instance;
	}

	// Private InitializeMaps function. Used to initialize the data in all the map objects.
	private void InitializeMaps()
	{
		// Size Label is the 0 index component
		this.componentsMap.put("SizeLabel", 0);
		// Size Field is the 1 index component
		this.componentsMap.put("SizeField", 1);
		// Character Choice Label is the 2 index component
		this.componentsMap.put("CCLabel", 2);
		// Character Choice Field is the 3 index component
		this.componentsMap.put("CCField", 3);
		// AI Label is the 4 index component
		this.componentsMap.put("AILabel", 4);
		// AI Field is the 5 index component
		this.componentsMap.put("AIField", 5);
		// Button Panel is the 6 index component
		this.componentsMap.put("ButtonPanel", 6);
		// Current Player Label is the 7 index component
		this.componentsMap.put("CPLabel", 7);
		// Current Wins Label is the 8 index component
		this.componentsMap.put("WinsLabel", 8);
				
		// Quit action
		this.actionsMap.put("Quit", new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				// Shutdown the game manager instance
				GameManager.Instance().ShutDown();
				// Exit the application
				System.exit(0);
			}
		} );
		
		// New Game action
		this.actionsMap.put("NewGame", new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{	
				// If the number of components is greater than 0 and the user has not clicked new game
				if(pane.getComponentCount() > 0 && !newGameClicked)
				{
					// Set the component visibility for the size label to true
					SetComponentVisibility(componentsMap.get("SizeLabel"), true);
					// Set the component visibility for the size field to true
					SetComponentVisibility(componentsMap.get("SizeField"), true);
					// Set the window visibility to true
					window.setVisible(true);
					// Set the newGameClicked variable to true
					newGameClicked = true;
					// Set the focus of the mouse cursor to be in this text field
					pane.getComponent(componentsMap.get("SizeField")).requestFocusInWindow();
				}			
			}
		} );
		
		// Grid Size action
		this.actionsMap.put("GridSize", new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{	// Grab reference to the source
				Object o = e.getSource();
				
				// If it is an instance of a JTextField
				if(o instanceof JTextField)
				{
					// Cast it
					JTextField i = (JTextField)o;
										
					// Temporary variable for storing the size the user chose
					int size;
					
					// If string is empty set default to 3
					if(i.getText().isEmpty())
						size = 3;
					else // Else set the max size of the grid to the number returned from the first character of the text
						size = Character.getNumericValue(i.getText().charAt(0));
					
					// If the user puts 10 as the grid size it will only return 1 so set it to 10
					if(size == 1)
						size = 10;
					
					// Check to make sure the size is in range, if not then set to 3
					if(size < 3)
						size = 3;
					
					// Set the max size in the game manager to the size
					GameManager.Instance().maxSize = size;
					// Set up the board size
					board = new JButton[size][size];
					// Turn off the size label visibility
					SetComponentVisibility(componentsMap.get("SizeLabel"), false);
					// Turn off the JTextField visibility
					i.setVisible(false);
					// Set the text back to empty
					i.setText("");
					
					// Set the component visibility for the CC label to true
					SetComponentVisibility(componentsMap.get("CCLabel"), true);
					// Set the component visibility for the CC field to true
					SetComponentVisibility(componentsMap.get("CCField"), true);
					// Set the window visibility to true
					window.setVisible(true);
					
					// Set the focus of the mouse cursor to be in this text field
					pane.getComponent(componentsMap.get("CCField")).requestFocusInWindow();
				}	
			}
		});
		
		// Player Character Choice
		this.actionsMap.put("PlayerCharChoice", new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{	// Grab reference to the source
				Object o = e.getSource();
				
				// If it is an instance of a JTextField
				if(o instanceof JTextField)
				{
					// Cast it
					JTextField i = (JTextField)o;

					// Temporary char variable
					String s = i.getText().toUpperCase();
					char p1 = s.charAt(0);			
					char p2 = ' ';
					
					// Else if the user puts another character besides X or O then just set it to X
					if(p1 != 'X' && p1 != 'O')
						p1 = 'X';
					
					// If p1 is X then p2 will be O else p2 will be X
					p2 = (p1 == 'X') ? 'O' : 'X';
					
					// Set the player characters reference to the playerCharacterChoice variable
					GameManager.Instance().setPlayerCharacters(p1, p2);
					
					// Turn off the CC label visibility
					SetComponentVisibility(componentsMap.get("CCLabel"), false);
					// Turn off the JTextField visibility
					i.setVisible(false);
					// Set the text back to empty
					i.setText("");
					
					// Set the component visibility for the AI label to true
					SetComponentVisibility(componentsMap.get("AILabel"), true);
					// Set the component visibility for the AI field to true
					SetComponentVisibility(componentsMap.get("AIField"), true);
					// Set the window visibility to true
					window.setVisible(true);
					
					// Set the focus of the mouse cursor to be in this text field
					pane.getComponent(componentsMap.get("AIField")).requestFocusInWindow();
				}	
			}
		});
		
		// Player AI Choice
		this.actionsMap.put("PlayerAIChoice", new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{	// Grab reference to the source
				Object o = e.getSource();
				
				// If it is an instance of a JTextField
				if(o instanceof JTextField)
				{
					// Cast it
					JTextField i = (JTextField)o;

					// Temporary char variable
					String s = i.getText().toUpperCase();
					char AIChoice = s.charAt(0);

					//If the user puts another character besides Y or N then just set it to N
					if(AIChoice != 'Y' && AIChoice != 'N')
					{
						AIChoice = 'N';
					}
					
					// If AIChoice is Y then AIChoice will be true else AIChoice will be false
					playerAIChoice = (AIChoice == 'Y') ? true : false;
					
					// Set the AIChoice to the playerAIChoice
					GameManager.Instance().setAIChoice(playerAIChoice);
									
					// Turn off the AI label visibility
					SetComponentVisibility(componentsMap.get("AILabel"), false);
					// Turn off the JTextField visibility
					i.setVisible(false);
					// Set the text back to empty
					i.setText("");
					
					// Draw the Board
					DrawBoard();
				}	
			}
		});

		// Player Slot Choice
		this.actionsMap.put("PlayerSlotChoice", new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{	// Grab reference to the source
				Object o = e.getSource();
				
				// If it is an instance of a JTextField
				if(o instanceof JButton)
				{
					// Cast it
					JButton i = (JButton)o;
					
					// If the text is equal to a white space then the button has not been clicked
					if(i.getText() == " ")
					{
						// Get the numerical value of the first character in the buttons name
						int x = Character.getNumericValue(i.getName().charAt(0));
						// Get the numerical value of the second character in the buttons name
						int y = Character.getNumericValue(i.getName().charAt(1));
						
						// Set the current players x coordinate to x
						GameManager.Instance().currentPlayer.coords[0] = x;
						// Set the current players y coordinate to y
						GameManager.Instance().currentPlayer.coords[1] = y;
						// Run the game manager function for checking for win/tie game
						GameManager.Instance().Run();
					}
				}	
			}
		});

	}

	// Private SetUpUIElements function. Used to setup all the UI Elements in the game.
	private void SetUpUIElements()
	{
		// Set up the menu
		this.SetUpMenu();

		// Create a new label prompting the user to enter a number for grid size
		Label l = new Label("Please Enter a number 3 - 10 for number of rows/columns:");
		// Create a new JTextField for the user to type the grid size into
		JTextField  gridSizeField = new JTextField(2);
		// Add the Grid size action to the field
		gridSizeField.addActionListener(actionsMap.get("GridSize"));
		// Create a new label prompting the user to choose their character
		Label playerCharChoice = new Label("Do you want to be X or O?");
		// Create a JTextField for the user to type their character choice into
		JTextField  CharField = new JTextField(2);
		// Add the PlayerCharChoice action to the field
		CharField.addActionListener(actionsMap.get("PlayerCharChoice"));
		// Create a label prompting the user to choose whether to play with the AI or not
		Label playerAIChoice = new Label("Do you want to play against the AI? Y/N");
		// Create a JTextField for the user to type Y or N for the AI Choice
		JTextField  AIField = new JTextField(2);
		// Add the PlayerAIChoice action to the field
		AIField.addActionListener(actionsMap.get("PlayerAIChoice"));
		
		// Add the label to pane
		pane.add(l);
		// Add the grid size field to the pane
		pane.add(gridSizeField);
		// Add the player choice to the pane
		pane.add(playerCharChoice);
		// Add the char field to the pane
		pane.add(CharField);
		// Add the player ai choice to the pane
		pane.add(playerAIChoice);
		// Add the ai field to the pane
		pane.add(AIField);
		
		// Turn off the visibility for each component that was just added
		SetComponentVisibility(0,false);
		SetComponentVisibility(1, false);
		SetComponentVisibility(2,false);
		SetComponentVisibility(3, false);
		SetComponentVisibility(4,false);
		SetComponentVisibility(5, false);
		
	}

	// Private SetUpMenu function. Used to set up the menu.
	private void SetUpMenu()
	{
		// Initialize a new menu bar
		this.menuBar = new JMenuBar();
		
		// Initialize the File Menu
		this.fileMenu = new JMenu("File");
		// Add the file menu to the menu bar
		this.menuBar.add(this.fileMenu);
		

		// Initialize a new game menu item
		this.newGame = new JMenuItem("New Game");
		// Add an action listener for when the item is clicked on
		this.newGame.addActionListener(this.actionsMap.get("NewGame"));
		// Initialize the quit game menu item
		this.quitGame = new JMenuItem("Quit");
		// Add an action listener for when the item is clicked on
		this.quitGame.addActionListener(this.actionsMap.get("Quit"));
		
		// Add the new game item to the file menu
		this.fileMenu.add(this.newGame);
		// Add the quit game item to the file menu
		this.fileMenu.add(this.quitGame);
		
		// Set the JMenuBar in the window to the menu bar
		this.window.setJMenuBar(this.menuBar);
	}

	// Private DrawBoard function. Used to visually draw the board for the user to interact with.
	private void DrawBoard()
	{	
		// Set up the board in the game manager instance
		GameManager.Instance().SetupBoard();
		
		// Create a new label to display the current players turn text
		Label pc = new Label("It is Player " + GameManager.Instance().currentPlayer.playerChar + "'s turn");
		// Set the playerTurnText reference to the new label
		this.playerTurnText = pc;
		
		// Create a string that will be used to display the number of wins for each player
		String winLoss = "Player " + GameManager.Instance().thePlayers[0].playerChar + "'s wins " + GameManager.Instance().thePlayers[0].wins
				+ "  Player " + GameManager.Instance().thePlayers[1].playerChar + "'s wins " + GameManager.Instance().thePlayers[1].wins;
		
		// Create a new label and giving it the string of win loss
		Label wl = new Label(winLoss);
		// Set the winLossCounter reference to the new label
		this.winLossCounter = wl;
		
		// Create a new JPanel
		JPanel jPanel = new JPanel();
		// Set the layout to be a GridLayout with the dimensions of the grid size
		jPanel.setLayout(new GridLayout(GameManager.Instance().maxSize,GameManager.Instance().maxSize));
		
		// Loop through the grid size
		for(int i = 0; i < GameManager.Instance().maxSize; i++)
		{
			for(int j = 0; j < GameManager.Instance().maxSize; j++)
			{
				// Create a new button with white space
				JButton jB = new JButton(" ");
				// Set the buttons position and size
				jB.setBounds(0 + (i * 45), 0 + (j * 45), 45, 45);
				// Give the button the name of its coordinates in the board
				jB.setName(i + "" + j);
				// Add the PlayersSlotChoice action to the button
				jB.addActionListener(this.actionsMap.get("PlayerSlotChoice"));
				// Add this button to the board
				this.board[i][j] = jB;
				// Add the button to the JPanel
				jPanel.add(this.board[i][j]);			
			}
		}
		
		// Add the JPanel to the pane
		this.pane.add(jPanel);
		// Add the playerTurnText reference to the pane
		this.pane.add(this.playerTurnText);
		// Add the winLossCounter reference to the pane
		this.pane.add(this.winLossCounter);
		
		// Set the window visibility to true
		this.window.setVisible(true);
		
		// If the current player is an AIPlayer
		if(GameManager.Instance().currentPlayer instanceof AIPlayer)
		{// Then call the Game Manager Run function so it can take its turn
			GameManager.Instance().Run();
		}					
	}

	// Public ClearBoard function. Used to clear the visually displayed board.
	public void ClearBoard()
	{
		// Loop through the grid size
		for(int i = 0; i < GameManager.Instance().maxSize; i++)
		{
			for(int j = 0; j < GameManager.Instance().maxSize; j++)
			{// Set the button text to white space
				this.board[i][j].setText(" ");			
			}
		}
	}
	
	// Private SetComponentVisibility function that takes an integer and boolean as arguments.
	// Used to set the visibility of the component at the passed in index to the value of b.
	private void SetComponentVisibility(int index, boolean b)
	{
		// Set the component at the given index to the value b
		pane.getComponents()[index].setVisible(b);
	}

	// Public SetCharacterOnButon function that takes 2 integers and a string as arguments.
	// The integers represent the index on the board of the button and the string is for setting in the button.
	public void SetCharacterOnButton(int x, int y, String c)
	{
		// Store a reference to the passed in string trimmed of all white space
		String s = c.trim();
		// Set the button text to the string
		this.board[x][y].setText(s);
	}
	
	// Public UpdateText function used to update the winLossCounter label and set the playerTurnText label to the passed in string.
	public void UpdateText(String playerTurn)
	{
		// Create a string that will be used to display the number of wins for each player
		String win = "Player " + GameManager.Instance().thePlayers[0].playerChar + "'s wins " + GameManager.Instance().thePlayers[0].wins
				+ "  Player " + GameManager.Instance().thePlayers[1].playerChar + "'s wins " + GameManager.Instance().thePlayers[1].wins;
		
		// Set the playerTurnText text to the passed in variable
		this.playerTurnText.setText(playerTurn);
		// Set the win loss counter text to the win text reference
		this.winLossCounter.setText(win);
	}
}
