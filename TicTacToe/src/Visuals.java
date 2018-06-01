import java.awt.*;
import java.awt.event.*;
import java.text.NumberFormat;
import java.util.*;

import javax.swing.*;

public class Visuals
{
	private final JFrame window;
	private Container pane;
	private JButton board[][];
	private JMenuBar menuBar;
	private JMenu fileMenu;
	private JMenuItem newGame;
	private JMenuItem quitGame;
	private Label playerTurnText;
	private Label winLossCounter;
	private boolean playerAIChoice;
	private boolean newGameClicked;
	
	private Map<String, ActionListener> actionsMap = new HashMap<>();
	private Map<String, Integer> componentsMap = new HashMap<>();
	
	private Visuals()
	{
		instance = this;
		
		this.window = new JFrame();
		this.window.setTitle("TicTacToe");
		this.window.setSize(600, 600);
		this.window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.window.setResizable(false);
		this.pane = this.window.getContentPane();
		this.pane.setLayout(new FlowLayout(FlowLayout.LEFT));
		this.newGameClicked = false;
		
		this.InitializeMaps();
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

	private void InitializeMaps()
	{
		// Quit
		this.actionsMap.put("Quit", new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				GameManager.Instance().ShutDown();
				System.exit(0);
			}
		} );
		
		// New Game
		this.actionsMap.put("NewGame", new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{	
				if(pane.getComponentCount() > 0 && !newGameClicked)
				{
					SetComponentVisibility(componentsMap.get("SizeLabel"), true);
					SetComponentVisibility(componentsMap.get("SizeField"), true);
					window.setVisible(true);
					newGameClicked = true;
				}			
			}
		} );
		
		// Grid Size
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
					
					int size;
					
					// If string is empty set default to 3
					if(i.getText().isEmpty())
						size = 3;
					else // Else set the max size of the grid to the number returned from the first character of the text
						size = Character.getNumericValue(i.getText().charAt(0));
					
					// If the user puts 10 as the grid size it will only return 1 so set it to 10
					if(size == 1)
						size = 10;
					
					// Set the max size to the size
					GameManager.Instance().maxSize = size;
					// Set up the board size
					board = new JButton[size][size];
					// Turn off the label visibility
					SetComponentVisibility(componentsMap.get("SizeLabel"), false);
					// Turn off the JTextField visibility
					i.setVisible(false);
					// Set the text back to empty
					i.setText("");
					
					SetComponentVisibility(componentsMap.get("CCLabel"), true);
					SetComponentVisibility(componentsMap.get("CCField"), true);
					window.setVisible(true);
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

					// Temp char variable
					String s = i.getText().toUpperCase();
					char p1 = s.charAt(0);			
					char p2 = ' ';
					
					// Else if the user puts another character besides X or O then just set it to X
					if(p1 != 'X' && p1 != 'O')
						p1 = 'X';
					
					// If p1 is X then p2 will be O else p2 will be X
					p2 = (p1 == 'X') ? 'O' : 'X';
					
					// Set the reference to the playerCharacterChoice variable
					GameManager.Instance().setPlayerCharacters(p1, p2);
					
					// Turn off the label visibility
					SetComponentVisibility(componentsMap.get("CCLabel"), false);
					// Turn off the JTextField visibility
					i.setVisible(false);
					// Set the text back to empty
					i.setText("");
					
					SetComponentVisibility(componentsMap.get("AILabel"), true);
					SetComponentVisibility(componentsMap.get("AIField"), true);
					window.setVisible(true);
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

					// Temp char variable
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
									
					// Turn off the label visibility
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
					
					if(i.getText() == " ")
					{
						int x = Character.getNumericValue(i.getName().charAt(0));
						int y = Character.getNumericValue(i.getName().charAt(1));
						
						GameManager.Instance().currentPlayer.coords[0] = x;
						GameManager.Instance().currentPlayer.coords[1] = y;
						GameManager.Instance().Run();
					}
				}	
			}
		});
		
		// Size Label
		this.componentsMap.put("SizeLabel", 0);
		// Size Field
		this.componentsMap.put("SizeField", 1);
		// Character Choice Label
		this.componentsMap.put("CCLabel", 2);
		// Character Choice Field
		this.componentsMap.put("CCField", 3);
		// AI Label
		this.componentsMap.put("AILabel", 4);
		// AI Field
		this.componentsMap.put("AIField", 5);
		// Button Panel
		this.componentsMap.put("ButtonPanel", 6);
		// Current Player Label
		this.componentsMap.put("CPLabel", 7);
		// Current Wins Label
		this.componentsMap.put("WinsLabel", 8);
	}

	private void SetUpUIElements()
	{
		this.SetUpMenu();

		Label l = new Label("Please Enter a number 3 - 10 for number of rows/columns:");
		JTextField  gridSizeField = new JTextField(2);
		gridSizeField.addActionListener(actionsMap.get("GridSize"));
		Label playerCharChoice = new Label("Do you want to be X or O?");
		JTextField  CharField = new JTextField(2);
		CharField.addActionListener(actionsMap.get("PlayerCharChoice"));
		Label playerAIChoice = new Label("Do you want to play against the AI? Y/N");
		JTextField  AIField = new JTextField(2);
		AIField.addActionListener(actionsMap.get("PlayerAIChoice"));
		
		pane.add(l);
		pane.add(gridSizeField);
		pane.add(playerCharChoice);
		pane.add(CharField);
		pane.add(playerAIChoice);
		pane.add(AIField);
				
		SetComponentVisibility(0,false);
		SetComponentVisibility(1, false);
		SetComponentVisibility(2,false);
		SetComponentVisibility(3, false);
		SetComponentVisibility(4,false);
		SetComponentVisibility(5, false);
		
	}

	private void SetUpMenu()
	{
		// Initialize a new menu bar
		this.menuBar = new JMenuBar();
		
		// Initialize the File Menu
		this.fileMenu = new JMenu("File");
		// Add the file menu to the menu bar
		this.menuBar.add(this.fileMenu);
		

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
	}

	private void DrawBoard()
	{	
		GameManager.Instance().SetupBoard();
		
		Label pc = new Label("It is Player " + GameManager.Instance().currentPlayer.playerChar + "'s turn");
		this.playerTurnText = pc;
		
		String winLoss = "Player " + GameManager.Instance().thePlayers[0].playerChar + "'s wins " + GameManager.Instance().thePlayers[0].wins
				+ "  Player " + GameManager.Instance().thePlayers[1].playerChar + "'s wins " + GameManager.Instance().thePlayers[1].wins;
		
		Label wl = new Label(winLoss);
		this.winLossCounter = wl;
		
		JPanel jPanel = new JPanel();
		jPanel.setLayout(new GridLayout(GameManager.Instance().maxSize,GameManager.Instance().maxSize));
		
		for(int i = 0; i < GameManager.Instance().maxSize; i++)
		{
			for(int j = 0; j < GameManager.Instance().maxSize; j++)
			{
				JButton jB = new JButton(" ");
				jB.setBounds(0 + (i * 45), 0 + (j * 45), 45, 45);
				jB.setName(i + "" + j);
				jB.addActionListener(this.actionsMap.get("PlayerSlotChoice"));
				this.board[i][j] = jB;
				jPanel.add(this.board[i][j]);
				
			}
		}
		
		this.pane.add(jPanel); //SetComponentVisibility(6, false);
		this.pane.add(this.playerTurnText); //SetComponentVisibility(7, false);
		this.pane.add(this.winLossCounter); //SetComponentVisibility(8, false);
		
		this.window.setVisible(true);
		
		if(GameManager.Instance().currentPlayer instanceof AIPlayer)
		{
			GameManager.Instance().Run();
		}					
	}

	public void ClearBoard()
	{
		for(int i = 0; i < GameManager.Instance().maxSize; i++)
		{
			for(int j = 0; j < GameManager.Instance().maxSize; j++)
			{
				this.board[i][j].setText(" ");			
			}
		}
	}
	
	private void SetComponentVisibility(int index, boolean b)
	{
		pane.getComponents()[index].setVisible(b);
	}

	public void SetCharacterOnButton(int x, int y, String c)
	{
		// Set the button text to the string trimmed of all white space
		String s = c.trim();
		this.board[x][y].setText(s);
	}
	
	public void UpdateText(String playerTurn)
	{
		String win = "Player " + GameManager.Instance().thePlayers[0].playerChar + "'s wins " + GameManager.Instance().thePlayers[0].wins
				+ "  Player " + GameManager.Instance().thePlayers[1].playerChar + "'s wins " + GameManager.Instance().thePlayers[1].wins;
		
		this.playerTurnText.setText(playerTurn);
		this.winLossCounter.setText(win);
	}
}
