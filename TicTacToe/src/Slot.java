
// The slot class. Represents slots on the board
public class Slot {

	// Public integer representing the slots x position on the board
	public int X;
	// Public integer representing the slots y position on the board
	public int Y;
	// Public char representing the slots character on the board
	public char slotChar;
	
	// Slot Constructor. Takes the x and y integer arguments for setting position on the board.
	public Slot(int x, int y)
	{
		// Set the X position to the passed in x argument
		this.X = x;
		// Set the Y position to the passed in y argument
		this.Y = y;
		// Set the character to default of ' '
		this.slotChar = ' ';
	}
	
}
