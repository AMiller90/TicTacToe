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
		// Set the number of wins to 0
		this.wins = 0;
		// Initialize the coordinates
		this.coords = new int[2];
	}
	
	// Take Turn function used for the Real Player taking its turn
	public int[] TakeTurn()
	{
		// Return the coordinates
		return coords;
	}

}
