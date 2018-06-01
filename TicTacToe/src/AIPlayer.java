import java.util.Random;

// The AIPlayer is a sub class of Player
public class AIPlayer extends Player
{
	// AIPlayer constructor - Takes a character argument for placement character
	public AIPlayer(char character)
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
	
	// Take Turn function used for the AI Player taking its turn
	public int[] TakeTurn()
	{
		// Create temporary array of integers used for slot coords
		int[] slotCoords = new int[2];
		
		// If the AI has found a slot that is not empty
		if(this.PickRandomSlot(slotCoords)) // Find a new placement in the algorithm function
			this.PlacementAlgorithm(slotCoords);
		
		// Return the coordinates for placement of the character
		return slotCoords;
	}

	// Picks a random generated set of coordinates. Takes an array of integers as reference for storing.
	public boolean PickRandomSlot(int[] coords)
	{
		// Temp variable for max size of a row/column on the board
		int maxSize = GameManager.Instance().maxSize;
		Random rand = new Random();
		
		// Set up temp variables
		int x = 0;
		int y = 0;
		
		// Randomize numbers between 0 and max size of row/columns on the board
		x = rand.nextInt(maxSize);
		y = rand.nextInt(maxSize);
		
		// Set the coordinates accordingly
		coords[0] = x;
		coords[1] = y;
		
		// Get the slot at the location on the board
		Slot s = GameManager.Instance().getSlot(x, y);
		
		// Return whether there has been a character placed in the slot or not
		return (s.slotChar != ' ');
	}
	
	// Check neighbor slots and find an empty one. Takes an array of integers as reference for storing.
	private void PlacementAlgorithm(int[] coords)
	{
		// Store temp variable of the maxsize of row/columns and subtract 1
		int max = GameManager.Instance().maxSize - 1;
		
		// Get the slot to check neighbors from
		Slot currentS = GameManager.Instance().getSlot(coords[0], coords[1]);

		// Loop through the neighbors of the slot
		for(int i = -1; i < GameManager.Instance().maxSize; i++)
		{
			for(int j = -1; j < GameManager.Instance().maxSize; j++)
			{
			// Determine if the x or y position of the neighbor slot from the current slot is within range of the board.
				// If not, then continue iterating through the loop.
			   if((currentS.X + i < 0 || currentS.Y + j < 0) || (currentS.X + i > max || currentS.Y + j > max))
				   continue;
			   
			   // If the neighbor that is found doesn't have a character already placed from either players
			   // then set the coordinates to that neighbor.
			   if(GameManager.Instance().getSlot(currentS.X + i, currentS.Y + j).slotChar == ' ')
			   {// Set the X and Y Coordinates of the neighbor slot.
				   coords[0] = currentS.X + i;
				   coords[1] = currentS.Y + j;
				   // Return from the function
				   return;
			   }
			}
		}	
	}
}
