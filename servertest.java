package timepss;
import javax.swing.JFrame;
	
public class servertest
	{
	    public static void main(String args[])
	    {
	        server sam= new server();
	        sam.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        sam.startRunning();
	    }
}


