
public class Main 
{
 public static void main(String[] args)
 {

	 if(GameManager.Instance().Start())
		 GameManager.Instance().Run();
	 
	 GameManager.Instance().ShutDown();
 }
 
}
