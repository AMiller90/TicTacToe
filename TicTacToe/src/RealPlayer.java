import java.util.Scanner;

public class RealPlayer extends Player
{
	public RealPlayer(char character)
	{
		this.myTurn = false;
		this.playerChar = character;
	}
	
	public int[] TakeTurn()
	{
		int x = 0;
		int y = 0;
		
		Scanner userInput;
		
		System.out.println("Please Enter 1st Coordinate:");
		userInput = new Scanner(System.in);
		
		if (userInput.hasNextInt())
		{
			x = userInput.nextInt();	
		}
		
		System.out.println("Please Enter 2nd Coordinate:");
		userInput = new Scanner(System.in);
		
		if (userInput.hasNextInt())
		{
			y = userInput.nextInt();	
		}
		
		int[] slotCoords = new int[2];
		
		slotCoords[0] = x;
		slotCoords[1] = y;
		
		return slotCoords;
	}
}
