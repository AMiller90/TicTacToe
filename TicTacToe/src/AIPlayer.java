import java.util.Random;

public class AIPlayer extends Player
{
	public AIPlayer(char character)
	{
		this.myTurn = false;
		this.playerChar = character;
	}
	
	public int[] TakeTurn()
	{
		
		int[] slotCoords = new int[2];

		System.out.println("AI Searching...:");
		
		if(this.PickRandomSlot(slotCoords))
			this.PlacementAlgorithm(slotCoords);
		
		return slotCoords;
	}

	public boolean PickRandomSlot(int[] coords)
	{
		int maxSize = GameManager.Instance().maxSize;
		Random rand = new Random();
		
		int x = 0;
		int y = 0;
		
		x = rand.nextInt(maxSize);
		y = rand.nextInt(maxSize);
		
		coords[0] = x;
		coords[1] = y;
		
		Slot s = GameManager.Instance().getSlot(x, y);
		
		return (s.slotChar != ' ' && s.slotChar != this.playerChar);
	}
	
	private void PlacementAlgorithm(int[] coords)
	{
		int x = coords[0];
		int y = coords[1];
		
		int max = GameManager.Instance().maxSize - 1;
		
		Slot currentS = GameManager.Instance().getSlot(coords[0], coords[1]);

		// Top left
		if((currentS.X - 1 >= 0 && currentS.Y - 1 >= 0) && GameManager.Instance().getSlot(currentS.X - 1, currentS.Y - 1).slotChar == ' ')
		{
			System.out.println("Top Left from:"+ currentS.X + " " + currentS.Y);
			Slot TopLeft = GameManager.Instance().getSlot(currentS.X - 1, currentS.Y - 1);
			x = TopLeft.X;
			y = TopLeft.Y;
			System.out.println("New coordinates: " + x + " " + y);
		} // Top
		else if(currentS.X - 1 >= 0 && GameManager.Instance().getSlot(currentS.X - 1, currentS.Y).slotChar == ' ')
		{
			System.out.println("Top from:"+ currentS.X + " " + currentS.Y);
			Slot Top = GameManager.Instance().getSlot(currentS.X - 1, currentS.Y);
			x = Top.X;
			y = Top.Y;
			System.out.println("New coordinates: " + x + " " + y);
		} // Top Right
		else if((currentS.X - 1 >= 0 && currentS.Y + 1 <= max) && GameManager.Instance().getSlot(currentS.X - 1, currentS.Y + 1).slotChar == ' ')
		{
			System.out.println("Top Right from:"+ currentS.X + " " + currentS.Y);
			Slot TopRight = GameManager.Instance().getSlot(currentS.X - 1, currentS.Y + 1);
			x = TopRight.X;
			y = TopRight.Y;
			System.out.println("New coordinates: " + x + " " + y);
		} // Bot Left
		else if((currentS.X + 1 <= max && currentS.Y - 1 >= 0) && GameManager.Instance().getSlot(currentS.X + 1, currentS.Y - 1).slotChar == ' ')
		{
			System.out.println("Bot Left from:"+ currentS.X + " " + currentS.Y);
			Slot BotLeft = GameManager.Instance().getSlot(currentS.X + 1, currentS.Y - 1);
			x = BotLeft.X;
			y = BotLeft.Y;
			System.out.println("New coordinates: " + x + " " + y);
		} // Bot
		else if(currentS.X + 1 <= max && GameManager.Instance().getSlot(currentS.X + 1, currentS.Y).slotChar == ' ')
		{
			System.out.println("Bot from:"+ currentS.X + " " + currentS.Y);
			Slot Bot = GameManager.Instance().getSlot(currentS.X + 1, currentS.Y);
			x = Bot.X;
			y = Bot.Y;
			System.out.println("New coordinates: " + x + " " + y);
		} // Bot Right
		else if((currentS.X + 1 <= max && currentS.Y + 1 <= max) && GameManager.Instance().getSlot(currentS.X + 1, currentS.Y + 1).slotChar == ' ')
		{
			System.out.println("Bot Right from:"+ currentS.X + " " + currentS.Y);
			Slot BotRight = GameManager.Instance().getSlot(currentS.X + 1, currentS.Y + 1);
			x = BotRight.X;
			y = BotRight.Y;
			System.out.println("New coordinates: " + x + " " + y);
		}// Left
		else if(currentS.Y - 1 >= 0 && GameManager.Instance().getSlot(currentS.X, currentS.Y - 1).slotChar == ' ')
		{
			System.out.println("Left from:"+ currentS.X + " " + currentS.Y);
			Slot Left = GameManager.Instance().getSlot(currentS.X, currentS.Y - 1);
			x = Left.X;
			y = Left.Y;
			System.out.println("New coordinates: " + x + " " + y);
		}
		// Right
		else if(currentS.Y + 1 <= max && GameManager.Instance().getSlot(currentS.X, currentS.Y + 1).slotChar == ' ')
		{
			System.out.println("Right from:"+ currentS.X + " " + currentS.Y);
			Slot Right = GameManager.Instance().getSlot(currentS.X, currentS.Y + 1);
			x = Right.X;
			y = Right.Y;
			System.out.println("New coordinates: " + x + " " + y);
		}
		
		coords[0] = x;
		coords[1] = y;
	}
}
