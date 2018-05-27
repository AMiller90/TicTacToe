// Main class for running the application
public class Main 
{
 public static void main(String[] args)
 {
	 // If the instance starts up then run it
	 if(GameManager.Instance().Start())
		 GameManager.Instance().Run();
	 
	 // Shutdown the instance
	 GameManager.Instance().ShutDown();			
 }
 
}
