
import java.util.Scanner;

public final class GameManager
{
	public final short maxSize = 3;
	private Slot[][] Board;
	private Player[] thePlayers;
	private Scanner userInput;
	private boolean isPlaying;
	private int TurnCount;
	
	private GameManager()
	{
		this.thePlayers = new Player[2];
		this.Board = new Slot[maxSize][maxSize];
		this.isPlaying = true;
		this.TurnCount = 0;
		instance = this;
	}

	private static GameManager instance;
	
	public static GameManager Instance()
	{
		if (instance == null)
			instance = new GameManager();
		
		return instance;
	}
	
	public void ShutDown()
	{
		this.thePlayers = null;
		this.Board = null;
	}
	
	public boolean Start()
	{
		System.out.println("Please Enter P to play or anything to quit:");

		this.userInput = new Scanner(System.in);
		
		if(userInput.hasNext("P") || userInput.hasNext("p"))
		{
			char p1char;
			char p2char;
			
			System.out.println("Play as X or O?");
			this.userInput = new Scanner(System.in);
			p1char = (userInput.hasNext("X") || userInput.hasNext("x")) ? 'X' : 'O';
			p2char = (p1char == 'X') ? 'O' : 'X';
			
			System.out.println("Play against AI: Y/N?");		
			this.userInput = new Scanner(System.in);		
			boolean AI = (userInput.hasNext("Y") || userInput.hasNext("y")) ? true : false;
			
			this.SetupBoard(AI, p1char, p2char);
		}
		else
			return false;
		
		return true;
	}
	
	public boolean Run()
	{
		int[] coords;
		
		this.DisplayBoard();
		
		while(isPlaying)
		{
			coords = (this.thePlayers[0].myTurn) ? this.thePlayers[0].TakeTurn() : this.thePlayers[1].TakeTurn();
			
			// Check for empty slot 
			if(this.getSlot(coords[0], coords[1]).slotChar == ' ')
			{
				this.TurnCount++;
				char c = (this.thePlayers[0].myTurn) ? this.thePlayers[0].playerChar : this.thePlayers[1].playerChar;
				this.setSlot(coords[0], coords[1], c);
				this.DisplayBoard();
				
				if(this.CheckForWinner(this.getSlot(coords[0], coords[1]), c))
				{
					this.PlayAgain();
				}
				else if(this.TurnCount == (this.maxSize * this.maxSize))
				{
					System.out.println("Tie!");
					this.PlayAgain();
				}
				else 
				{
					// Change turns
					this.thePlayers[0].myTurn = !this.thePlayers[0].myTurn;
					this.thePlayers[1].myTurn = !this.thePlayers[1].myTurn;
				}
			}
			else
				System.out.println("Already a character there!");

		}
		
		return this.isPlaying;
	}
	
	private void SetupBoard(boolean AI, char p1, char p2)
	{
		// Set up the slots and store into the board
		for (int i = 0; i < this.maxSize; i ++)
		{
			for(int j = 0; j < this.maxSize; j++)
			{   
				Slot slot = new Slot(i,j);
				this.Board[i][j] = slot;
			}
		}
		
		// Set the first player as real player
	    this.thePlayers[0] = new RealPlayer(p1);
		
	    // Set the second player as AI or Real Player
	    this.thePlayers[1] = (AI) ? new AIPlayer(p2) : new RealPlayer(p2);
	    
	    // Player that has X always goes first
	    if(p1 == 'X')
	    	this.thePlayers[0].myTurn = true;
	    else
	    	this.thePlayers[1].myTurn = true;
	}
	
	private void DisplayBoard()
	{
		// Set up the slots and store into the board
		for (int i = 0; i < maxSize; i ++)
		{
			System.out.print('|');
			for(int j = 0; j < maxSize; j++)
			{
				System.out.print(this.Board[i][j].slotChar + "|");
			}
			
			System.out.println(' ');
		}
	}
	
	private void ClearBoard()
	{
		// Set up the slots and store into the board
			for (int i = 0; i < this.maxSize; i ++)
			{
				for(int j = 0; j < this.maxSize; j++)
				{
					this.Board[i][j].slotChar = ' ';
				}
			}
	}
	
	private void PlayAgain()
	{
		System.out.println("Would You Like To Play Again..Y/N? ");
		this.userInput = new Scanner(System.in);
		
		if (userInput.hasNext("Y") || userInput.hasNext("y"))
		{
			this.ClearBoard();
			this.DisplayBoard();
			this.TurnCount = 0;
			
			// Player that has X always goes first
		    if(this.thePlayers[0].playerChar == 'X')
		    {
		    	this.thePlayers[0].myTurn = true;
		    	this.thePlayers[1].myTurn = false;
		    }
		    else
		    {
		    	this.thePlayers[0].myTurn = false;
		    	this.thePlayers[1].myTurn = true;
		    }
		}
		else
		{
			this.isPlaying = false;
		}
	}
	
	private boolean CheckForWinner(Slot s, char c)
	{
		if(this.TurnCount < (this.maxSize * 2) - 1)
			return false;
		
		System.out.println("Checking for winner");
		
		if(this.CheckRowAndColumn(s, c))
			return true;
		else if(this.CheckCorners(s, c))
			return true;

		float checkForMid = this.maxSize / 2;
		int result = (int)Math.floor(checkForMid);
		boolean center = this.maxSize % 2 == 1 && (s.X == result && s.Y == result);
		
		// This means the size was based off of an odd number
		// So we will need to check a second diagonal in case the
		// slot chosen is in the center of the board
		if(center)
			if(this.CheckDiagonals(s, c))
				return true;
		
		return false;
	}
	
	private boolean CheckRowAndColumn(Slot s, char c)
	{
		int counter = 0;
		
		// Check the row X
		for(int i = 0; i < this.maxSize; i++)
		{
			if(this.Board[s.X][i].slotChar == c)
			{
				counter++;
				if (counter == this.maxSize)
				{
					// We have a winner
					System.out.println("Winner is: " + c);
					return true;
				}
			}
			else 
			{
				counter = 0;
				break;
			}
		}
		
		// Check the column Y
		for(int i = 0; i < this.maxSize; i++)
		{
			if(this.Board[i][s.Y].slotChar == c)
			{
				counter++;
				if (counter == this.maxSize)
				{
					// We have a winner
					System.out.println("Winner is: " + c);
					return true;
				}
			}
			else 
			{
				counter = 0;
				break;
			}
		}
		
		return false;
	}
	
	private boolean CheckCorners(Slot s, char c)
	{
		int counter = 0;
		
		//Check 4 corners
		boolean botleft = s.X == this.maxSize - 1 && s.Y == 0;
		boolean topright = s.X == 0 && s.Y == this.maxSize - 1;
		boolean topleft = s.X == 0 && s.Y == 0;
		boolean botright = s.X == this.maxSize - 1 && s.Y == this.maxSize - 1;
		
		
		if(topleft || botright)
		{
			// Check diagonal
			for(int i = 0, j = 0; i < this.maxSize; i++, j++)
			{
				if(this.Board[i][j].slotChar == c)
				{
					counter++;
					if (counter == this.maxSize)
					{
						// We have a winner
						System.out.println("Winner is: " + c);
						return true;
					}
				}
				else 
				{
					counter = 0;
					break;
				}								
			}
		}
		else if(topright || botleft)
		{
			// Check diagonal
			for(int i = 0, j = this.maxSize - 1; i >= 0; i++, j--)
			{
				if(this.Board[i][j].slotChar == c)
				{
					counter++;
					if (counter == this.maxSize)
					{
						// We have a winner
						System.out.println("Winner is: " + c);
						return true;
					}
				}
				else 
				{
					counter = 0;
					break;
				}								
			}
		}
		
		return false;
	}
	
	private boolean CheckDiagonals(Slot s, char c)
	{
		int counter = 0;
		
		System.out.println("Checking Top left diagonal!");
		// Check diagonal
		for(int i = 0, j = 0; i < this.maxSize; i++, j++)
		{
			if(this.Board[i][j].slotChar == c)
			{
				counter++;
				if (counter == this.maxSize)
				{
					// We have a winner
					System.out.println("Winner is: " + c);
					return true;
				}
			}
			else 
			{
				counter = 0;
				break;
			}								
		}
		
		System.out.println("Checking Top right diagonal!");
		// Check diagonal
		for(int i = 0, j = this.maxSize - 1; i >= 0; i++, j--)
		{
			if(this.Board[i][j].slotChar == c)
			{
				counter++;
				if (counter == this.maxSize)
				{
					// We have a winner
					System.out.println("Winner is: " + c);
					return true;
				}
			}
			else 
			{
				counter = 0;
				break;
			}								
		}
		
		return false;
	}
	
	private void setSlot(int x, int y, char c) {
		this.Board[x][y].slotChar = c;
	}
	
	public Slot getSlot(int x, int y)
	{
		return this.Board[x][y];
	}

}
