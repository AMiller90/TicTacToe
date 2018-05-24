
public class Main 
{
 public static void main(String[] args)
 {
	 GameManager tictactoe = new GameManager();
	 
	 if(tictactoe.Start())
		 tictactoe.Run();
	 
	 tictactoe.ShutDown();
 }
 
}
