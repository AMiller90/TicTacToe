// Abstract base class for our players
public abstract class Player 
{
	// Character representing placement in a slot	
	public char playerChar;
	// Determines if it is this players turn or not
	public boolean myTurn;
	// Reference to the coordinates selected for character placement on the board
	public int[] coords;
	// Reference to number of games won
	public int wins;
	// Abstract function for a player taking their turn
	public abstract int[] TakeTurn();
}
