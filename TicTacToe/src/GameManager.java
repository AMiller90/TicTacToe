import java.util.Scanner;

// The GameManager Class. Holds all logic to game play and is a singleton.
// Final keyword means this class can not be inherited from.
public final class GameManager
{
	// Reference to the max size of rows/columns of board
	public int maxSize = 3;
	// Reference to the board
	private Slot[][] Board;
	// Reference to the players
	public Player[] thePlayers;
	//Reference to the turn count of the game
	private int TurnCount;
	// Reference to the current player
	public Player currentPlayer;
	// Reference to the player 1 character
	private char p1Char;
	// Reference to the player 2 character
	private char p2Char;
	// Reference to the AI Choice
	private boolean AIChoice;
	
	// Private constructor
	private GameManager()
	{
		// Initialize the players array
		this.thePlayers = new Player[2];
		// The Turn count starts at 0
		this.TurnCount = 0;
		//Set the instance to this object
		instance = this;
	}

	// Private static instance reference
	private static GameManager instance;
	
	// Public static Instance function used to return the instance of this class
	public static GameManager Instance()
	{
		// If the instance is null, then create a new instance and return it.
		// Else just return the instance.
		return instance = (instance == null) ? new GameManager() : instance;
	}
	
	// Public shutdown function that sets the objects to null
	public void ShutDown()
	{
		// Players object set to null
		this.thePlayers = null;
		// Board object set to null
		this.Board = null;
	}
	
	// Public Start function.
	public void Start()
	{	
		// Instance of the Visuals class
	    Visuals.Instance();
	}
	
	// Public Run function.
	public void Run()
	{
		// Temp coords
		int[] coords;
		
		coords = this.currentPlayer.TakeTurn();
		
		// Check for empty slot 
		if(this.getSlot(coords[0], coords[1]).slotChar == ' ')
		{
			// Increase the turn count
			this.TurnCount++;
			// Get the current players char
			char c = this.currentPlayer.playerChar;
			
			// Set the character of the slot to the passed in character
			this.getSlot(coords[0], coords[1]).slotChar = c;
			// Set the button to the passed in char adding whitespace
			String s = c + " ";
			Visuals.Instance().SetCharacterOnButton(coords[0], coords[1], s);
			
			// If a winner was found
			if(this.CheckForWinner(this.getSlot(coords[0], coords[1]), c))
			{
				// Increment the win counter
				this.currentPlayer.wins++;				
				// Call Play Again function
				this.PlayAgain();
				Visuals.Instance().UpdateText("New Game! It is Player " + currentPlayer.playerChar + "'s turn");
				
			} // If the turn count is equal to the size of the board then it is a tie
			else if(this.TurnCount == (this.maxSize * this.maxSize))
			{
				// Call Play Again Function
				this.PlayAgain();
				 // Update the Visuals Text
				Visuals.Instance().UpdateText("Tie Game! New Game! It is Player " + currentPlayer.playerChar + "'s turn");
			}
			else // Else if there is no winner and no tie then change turns 
			{
				// Change turns
				this.thePlayers[0].myTurn = !this.thePlayers[0].myTurn;
				this.thePlayers[1].myTurn = !this.thePlayers[1].myTurn;
				
				// Set the new current player
				this.currentPlayer = (this.thePlayers[0].myTurn) ? this.thePlayers[0] : this.thePlayers[1];
				 // Update the Visuals Text
				Visuals.Instance().UpdateText("It is Player " + currentPlayer.playerChar + "'s turn");
				
				// If the current player is AI then run the function again so it can take its turn
				if(this.currentPlayer instanceof AIPlayer)
					this.Run();
			}
		}// Else if there is already a character and its the AIs turn, run the function again so the AI can take its turn
		else if(this.currentPlayer instanceof AIPlayer)
			this.Run();
	}
	
	// Public SetUpBoard Function. Setups the board with slots and players.
	public void SetupBoard()
	{
		// Initialize the board 2D array...by default it will be 3
		this.Board = new Slot[this.maxSize][this.maxSize];
			
		// Set up the slots and store into the board
		for (int i = 0; i < this.maxSize; i ++)
		{
			for(int j = 0; j < this.maxSize; j++)
			{   
				// Create a new instance of a slot and give it the coordinates in the board.
				// Set the slot in the board
				this.Board[i][j] = new Slot(i,j);
			}
		}
		
		// Set the first player as real player
	    this.thePlayers[0] = new RealPlayer(this.p1Char);
		    
	    // Set the second player as AI or Real Player
	    this.thePlayers[1] = (this.AIChoice) ? new AIPlayer(this.p2Char) : new RealPlayer(this.p2Char);
	    
	    // Player that has X always goes first
	    currentPlayer = (this.p1Char == 'X') ? this.thePlayers[0] : this.thePlayers[1];
	    currentPlayer.myTurn = true;
	}
	
	// Private ClearBoard function. Clears the board.
	private void ClearBoard()
	{
		// Loop through the board
		for (int i = 0; i < this.maxSize; i ++)
		{
			for(int j = 0; j < this.maxSize; j++)
			{
				// Set all the slot characters back to ' '
				this.Board[i][j].slotChar = ' ';
			}
		}
		
		Visuals.Instance().ClearBoard();
			
	}
	
	// Private PlayAgain Function. Gives the user the choice of playing again or not.
	private void PlayAgain()
	{
			Player winner = this.currentPlayer;
			
			// Call the ClearBoard function
			this.ClearBoard();
			// Set the turn count back to 0
			this.TurnCount = 0;
			
			// Reset whose turn it is
			// Player that has X always goes first
		    if(this.thePlayers[0].playerChar == 'X')
		    { 	// Set player 1 to true
		    	this.thePlayers[0].myTurn = true;
		    	// Set player 2 to false
		    	this.thePlayers[1].myTurn = false;
		    	// Set the currentPlayer
		    	this.currentPlayer = this.thePlayers[0];
		    }
		    else // Player 1 has O
		    {// Set player 1 to false
		    	this.thePlayers[0].myTurn = false;
		    	// Set player 2 to true
		    	this.thePlayers[1].myTurn = true;
		    	// Set the currentPlayer
		    	this.currentPlayer = this.thePlayers[1];
		    }
		    
		    // Update the Visuals Text
			Visuals.Instance().UpdateText("Player " + winner.playerChar + " wins! New Game! It is Player " + currentPlayer.playerChar + "'s turn");
			
			if(GameManager.Instance().currentPlayer instanceof AIPlayer)
				Run();
		
	}
	
	// Private CheckForWinner Function. Takes a Slot as an argument to check and the character to check against.
	private boolean CheckForWinner(Slot s, char c)
	{
		// If the turn count is less than 2 x the size of rows/columns minus 1
		// then there have not been enough turns to consider a winner yet. Return false.
		if(this.TurnCount < (this.maxSize * 2) - 1)
			return false;
		
		// Call the CheckRowAndColumn function passing in the slot and character
		// If true then there is a winner
		if(this.CheckRowAndColumn(s, c))
			return true;
		// Call the CheckDiagonals function passing in the slot and character
		// If true then there is a winner
		else if(this.CheckDiagonals(s, c))
			return true;
		
		// If there is no winner then return false
		return false;
	}
	
	// Private CheckRowAndColumn Function. Takes a Slot as an argument to check and the character to check against.
	private boolean CheckRowAndColumn(Slot s, char c)
	{
		// Set up temp counter variable
		int counter = 0;
		
		// Check the row X
		for(int i = 0; i < this.maxSize; i++)
		{ // If the slot is equal to the passed in character
			if(this.Board[s.X][i].slotChar == c)
			{// Increment the counter
				counter++;
				// If the counter is equal to max size
				if (counter == this.maxSize)
				{
					// We have a winner
					// Return true
					return true;
				}
			}
			else // Else the slot does not have the same character as the passed in one.
			{
				// Set counter to 0 and break the loop
				counter = 0;
				break;
			}
		}
		
		// Check the column Y
		for(int i = 0; i < this.maxSize; i++)
		{// If the slot is equal to the passed in character
			if(this.Board[i][s.Y].slotChar == c)
			{// Increment the counter
				counter++;
				// If the counter is equal to max size
				if (counter == this.maxSize)
				{
					// We have a winner
					// Return true
					return true;
				}
			}
			else // Else the slot does not have the same character as the passed in one.
			{
				// Set counter to 0 and break the loop
				counter = 0;
				break;
			}
		}
		
		// Return false. There was no winning match in the row or column.
		return false;
	}
	
	// Private CheckDiagonals Function. Takes a Slot as an argument to check and the character to check against.
	private boolean CheckDiagonals(Slot s, char c)
	{
		// Set up temp counter variable
		int counter = 0;
		// Get temporary variable for size - 2
		int size = this.maxSize - 2;
				
		// If the X Position of the slot is equal to the Y position
		boolean equal = s.X == s.Y;
		// If the X Position of the slot is equal to the max size minus 1 and Y position is equal to 0
		boolean botleft = s.X == this.maxSize - 1 && s.Y == 0;
		// If the X Position of the slot is equal to 0 and Y position is equal to the max size minus 1
		boolean topright = s.X == 0 && s.Y == this.maxSize - 1;
		// Reference to the right diagonal
		boolean rightdiagonal = false;
		
		
		// If equal
		if(equal)
		{	
			// Check diagonal
			for(int i = 0, j = 0; i < this.maxSize; i++, j++)
			{// If the slot is equal to the passed in character
				if(this.Board[i][j].slotChar == c)
				{// Increment the counter
					counter++;
					// If the counter is equal to max size
					if (counter == this.maxSize)
					{
						// We have a winner
						// Return true
						return true;
					}
				}
				else // Else the slot does not have the same character as the passed in one.
				{
					// Set counter to 0 and break the loop
					counter = 0;
					break;
				}								
			}
		}

		// Loop through the numbers
		for(int i = size, j = 1; i >= 1; i--, j++)
		{
			// Check if the slot at the location is possible for
			// a right diagonal check
			if(s.X == i && s.Y == j)
			{// Set right diagonal to true and then break the loop
				rightdiagonal = true;
				break;
			}				
		}
					
		// Else If top right or bot left or right diagonal
		if(topright || botleft || rightdiagonal)
		{
			// Check right diagonal
			for(int i = 0, j = this.maxSize - 1; i >= 0; i++, j--)
			{// If the slot is equal to the passed in character
				if(this.Board[i][j].slotChar == c)
				{// Increment the counter
					counter++;
					// If the counter is equal to max size
					if (counter == this.maxSize)
					{
						// We have a winner
						// Return true
						return true;
					}
				}
				else // Else the slot does not have the same character as the passed in one.
				{
					// Set counter to 0 and break the loop
					counter = 0;
					break;
				}								
			}
		}
		
		// Return false. There was no winning match in any of the diagonal checks from the corners.
		return false;
	}
	
	// Public GetSlot Function. Takes 2 integers as the coordinates on the board to find the slot.
	public Slot getSlot(int x, int y)
	{// Return the slot on the board from the passed in coordinates.
		return this.Board[x][y];
	}

	// Public setPlayerCharacter Function. Takes 2 chars as the characters for the players.
	public void setPlayerCharacters(char c1, char c2)
	{
		// Set p1Char to c1
		this.p1Char = c1;
		// Set p2Char to c2
		this.p2Char = c2;
	}

	// Public setAIChoice Function. Sets the AI Choice to te passed in argument
	public void setAIChoice(boolean ai)
	{
		// Set AIChoice to ai
		this.AIChoice = ai;
	}
}
