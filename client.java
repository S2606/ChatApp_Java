package timepss;


import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
public class client extends JFrame
{
    private JTextField userText;
    private JTextArea chatWindow;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private String message="";
    private String serverIP;
    private Socket connection;
    
    
    public client(String host)
    {
        super("client shagun!");
        serverIP=host;
        userText= new JTextField();
        userText.setEditable(false);
        userText.addActionListener(
            new ActionListener(){
                public void actionPerformed(ActionEvent event){
                    sendMessage(event.getActionCommand());
                    userText.setText("");
                }
            }
        );
        add(userText, BorderLayout.NORTH);
        chatWindow=new JTextArea();
        add(new JScrollPane(chatWindow), BorderLayout.CENTER);
        setSize(300,150);
        setVisible(true);
    }
    
    public void startRunning()
    {
        try{
            connectToServer();
            setupstreams();
            whileChatting();
        }catch(EOFException e){
            showMessage("\n Messge terminated\n");
        }catch(IOException e1){
            e1.printStackTrace();
        }finally{
            closeCrap();
        }
    }
    
    //connect to server
    private void connectToServer() throws IOException
    {
        showMessage("Attempting connection");
        connection = new Socket(InetAddress.getByName(serverIP), 3456);
        showMessage("Connected to: " + connection.getInetAddress().getHostName());
        
    }
    
    private void setupstreams() throws IOException
    {
        output=new ObjectOutputStream(connection.getOutputStream());
        output.flush();
        input=new ObjectInputStream(connection.getInputStream());
        showMessage("\n Streams are now setup \n");
    }
    
    //during chat
    private void whileChatting() throws IOException
    {
        ableToType(true);
        do{
            //have a conversation
            try{
                message=(String) input.readObject();
                showMessage("\n " + message);
            }catch(ClassNotFoundException e){
                showMessage("\n Server send shit!");
            }
        }while(!message.equals("SERVER - END"));
    }
    
    //close the streams
    private void closeCrap()
    {
        showMessage("\n Closing Connections... \n");
        ableToType(false);
        try{
            output.close();
            input.close();
            connection.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    
    private void sendMessage(String message)
    {
        try{
            output.writeObject("CLIENT - "+ message);
            output.flush();
            showMessage("\nCLIENT - "+ message);
        }catch(IOException e){
            chatWindow.append("\n cant send message ");
        }
    }
    
    private void showMessage(final String message)
    {
        SwingUtilities.invokeLater(
            new Runnable(){
                public void run(){
                    chatWindow.append(message);
                }
            }       
        );
    }
    
    //let user type stuff
    private void ableToType(final boolean tof)
    {
        SwingUtilities.invokeLater(
            new Runnable(){
                public void run(){
                    userText.setEditable(tof);
                }
            }       
        );
    }
}
