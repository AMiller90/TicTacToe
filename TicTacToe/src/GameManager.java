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
	private Player[] thePlayers;
	// Reference to userInput
	private Scanner userInput;
	// Reference for determining if the game is still running or not
	private boolean isPlaying;
	//Reference to the turn count of the game
	private int TurnCount;
	// Reference to the current player
	public Player currentPlayer;
	//Reference to the player 1 character
	private char p1Char;
	//Reference to the player 2 character
	private char p2Char;
	//Reference to the AI Choice
	private boolean AIChoice;
	
	// Private constructor
	private GameManager()
	{
		// Initialize the players array
		this.thePlayers = new Player[2];
		// Set isPlaying to true
		this.isPlaying = true;
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
	
	// Public Start function. Returns true if user wants to play, else false.
	public boolean Start()
	{	
		// Instance of the Visuals class
	    Visuals.Instance();
	    
//		 // Tell user to enter p to play or anything to quit.
//		 		System.out.println("Please Enter P to play or anything to quit:");
//
//		 		// Set the reference to a new input
//		 		this.userInput = new Scanner(System.in);
//		 		
//		 		// If the user entered P or p
//		 		if(userInput.hasNext("P") || userInput.hasNext("p"))
//		 		{
//		 			// Set up temp variables
//		 			char p1char;
//		 			char p2char;
//		 			
//		 			// Initialize the board 2D array...by default it will be 3
//		 			this.Board = new Slot[this.maxSize][this.maxSize];
//		 			
//		 			// Ask user which character to use
//		 			System.out.println("Play as X or O?");
//		 			// Set the reference to a new input
//		 			this.userInput = new Scanner(System.in);
//		 			// If the user chose X then player 1 is X, else player 1 is O
//		 			p1char = (userInput.hasNext("X") || userInput.hasNext("x")) ? 'X' : 'O';
//		 			// Player 2 will be O if player 1 is X, else player 2 will be X
//		 			p2char = (p1char == 'X') ? 'O' : 'X';
//		 			
//		 			// Ask user if they would like to play against AI
//		 			System.out.println("Play against AI: Y/N?");
//		 			// Set the reference to a new input
//		 			this.userInput = new Scanner(System.in);
//		 			// If the user chose Y, then store true as reference, else store false.
//		 			boolean AI = (userInput.hasNext("Y") || userInput.hasNext("y")) ? true : false;
//		 			
//		 			// Set up the board
//		 			this.SetupBoard();
//		 		}
//		 		else // Else return false if user entered anything other than p
//		 			return false;
//			
		// Return true
		return true;
	}
	
	// Public Run function. The Game Loop.
	public boolean Run()
	{
		// Temp coords
		int[] coords;
		
		// Display the board
		//.DisplayBoard();
		
		// While the game isPlaying
		while(isPlaying)
		{
			// Set the coords to the returned coords. If it's player 1s turn then call the take turn function for them
			// Else, call player 2s take turn function
			coords = (this.thePlayers[0].myTurn) ? this.thePlayers[0].TakeTurn() : this.thePlayers[1].TakeTurn();
			
			// Check for empty slot 
			if(this.getSlot(coords[0], coords[1]).slotChar == ' ')
			{
				// Increase the turn count
				this.TurnCount++;
				// Grab the character of the player1 if its their turn
				// If not, then get the character of player2
				char c = (this.thePlayers[0].myTurn) ? this.thePlayers[0].playerChar : this.thePlayers[1].playerChar;
				// Set the slot character to the character of the player that placed it
				this.setSlotChar(coords[0], coords[1], c);
				// Display the board
				this.DisplayBoard();
				
				// If a winner was found
				if(this.CheckForWinner(this.getSlot(coords[0], coords[1]), c))
				{// Call Play Again function
					this.PlayAgain();
				} // If the turn count is equal to the size of the board then it is a tie
				else if(this.TurnCount == (this.maxSize * this.maxSize))
				{
					// Call Play Again Function
					this.PlayAgain();
				}
				else // Else if there is no winner and no tie then change turns 
				{
					// Change turns
					this.thePlayers[0].myTurn = !this.thePlayers[0].myTurn;
					this.thePlayers[1].myTurn = !this.thePlayers[1].myTurn;
					
					// Set the new current player
					this.currentPlayer = (this.thePlayers[0].myTurn) ? this.thePlayers[0] : this.thePlayers[1];
					// Display the current characters turn
					System.out.println("It is Player " + currentPlayer.playerChar + "'s turn");
				}
			}
			else // Else there is already a character there so loop again
				System.out.println("Already a character there!");

		}
		
		// Return isPlaying
		return this.isPlaying;
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
	
	// Public DisplayBoard Function. Displays the board for user.
	private void DisplayBoard()
	{			
		// Loop through the board
		for (int i = 0; i < this.maxSize; i ++)
		{
			// Print the i value and a '|' for formatting
			System.out.print("|");
			for(int j = 0; j < this.maxSize; j++)
			{// Print the character of the current slot and a '|' for formatting.
				System.out.print(this.Board[i][j].slotChar + "|");
			}
			// Print a ' ' for formatting
			System.out.println(' ');
		}
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
	}
	
	// Private PlayAgain Function. Gives the user the choice of playing again or not.
	private void PlayAgain()
	{
		// Ask user whether to play again or not
		System.out.println("Would You Like To Play Again..Y/N? ");
		// Store reference to the input
		this.userInput = new Scanner(System.in);
		
		// If the input is Y or y
		if (userInput.hasNext("Y") || userInput.hasNext("y"))
		{
			// Call the ClearBoard function
			this.ClearBoard();
			// Call the DisplayBoard function
			this.DisplayBoard();
			// Set the turn count back to 0
			this.TurnCount = 0;
			
			// Reset whose turn it is
			// Player that has X always goes first
		    if(this.thePlayers[0].playerChar == 'X')
		    { // Set player 1 to true
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
		    
		 // Display the current characters turn
			System.out.println("It is Player " + currentPlayer.playerChar + "'s turn");
		}
		else // Else the user pressed something else and the game ends
		{
			// Set isPlaying to false and end the game
			this.isPlaying = false;
		}
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
					System.out.println("Winner is: " + c);
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
					System.out.println("Winner is: " + c);
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
						System.out.println("Winner is: " + c);
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
						System.out.println("Winner is: " + c);
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
	
	// Private setSlotChar Function. Takes 2 integers as the coordinates on the board to find the slot and the character to set in it.
	private void setSlotChar(int x, int y, char c) {
		// Set the character of the slot to the passed in character
		this.Board[x][y].slotChar = c;
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
