package timepss;

import javax.swing.JFrame;
public class clienttest
{
   public static void main(String args[])
   {
       client c;
       c=new client("127.0.0.1");
       c.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       c.startRunning();
   }
}
