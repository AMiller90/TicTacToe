import java.util.Scanner;

// The RealPlayer class is a sub class of Player
public class RealPlayer extends Player
{
	// RealPlayer constructor - Takes a character argument for placement character
	public RealPlayer(char character)
	{
		// Set the turn to false
		this.myTurn = false;
		// Set the character to the passed in character
		this.playerChar = character;
	}
	
	// Take Turn function used for the Real Player taking its turn
	public int[] TakeTurn()
	{
		// Set up temp variables
		int x = 0;
		int y = 0;
		
		// Temp instance of the scanner object used for getting coordinates from the user.
		Scanner userInput;
		
		// Store reference to max number user can input
		int max = GameManager.Instance().maxSize -1;
		
		// Tell user to enter first coordinates within range
		System.out.println("Please Enter 1st Coordinate 0 - " + max + ":");
		
		// Store the reference and grab input from user
		userInput = new Scanner(System.in);
		
		// If the user put an integer
		if (userInput.hasNextInt())
		{ // Set the x coordinate to the number
			x = userInput.nextInt();
			
			// The number is not in range then repeat turn
			if(x < 0 || x > max)
				this.TakeTurn();
	
		}else // If not a valid number then repeat turn
			this.TakeTurn();
		
		// Tell user to enter second coordinate
		System.out.println("Please Enter 2nd Coordinate 0 - " + max + ":");
		
		// Store the reference and grab input from user
		userInput = new Scanner(System.in);
		
		// If the user put an integer
		if (userInput.hasNextInt())
		{ // Set the y coordinate to the number
			y = userInput.nextInt();
			
			// The number is not in range then repeat turn
			if(y < 0 || y > max)
				this.TakeTurn();
						
		}else // If not a valid number then repeat turn
			this.TakeTurn();
		
		// Set up temp array of integers for coordinates
		int[] slotCoords = new int[2];
		
		// Set the coordinates
		slotCoords[0] = x;
		slotCoords[1] = y;
		
		// Return the coordinates
		return slotCoords;
	}
}
