package timepss;

import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
public class server extends JFrame {
    private JTextField userText;
    private JTextArea chatWindow;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private ServerSocket server;
    private Socket connection;

    public server()
    {
        super("Sam instant messenger");
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
        add(userText,BorderLayout.NORTH);
        chatWindow=new JTextArea();
        add(new JScrollPane(chatWindow));
        setSize(300,150);
        setVisible(true);
    }

    //set up server
    public void startRunning()
    {
        try{
            server= new ServerSocket(3456, 100);
            while(true)
            {
                try{
                    waitforConnection();
                    setupstreams();
                    whileChatting();
                }catch(EOFException e){
                    showMessage("\n Server ends!");
                }finally{
                    closeCrap();
                }
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    //Wait for Connection
    private void waitforConnection() throws IOException
    {
        showMessage("Waiting for someone to connect.. \n");
        connection=server.accept();
        showMessage("Now Connected to "+connection.getInetAddress()
            .getHostName());
    }

    //set up streams to send and recieve data
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
        String message="connected";
        sendMessage(message);
        ableToType(true);
        do{
            //have a conversation
            try{
                message=(String) input.readObject();
                showMessage("\n " +message);
            }catch(ClassNotFoundException e){
                showMessage("\n User send shit!");
            }
        }while(!message.equals("CLIENT - END"));
    }

    //close streams
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
            output.writeObject("SERVER - "+ message);
            output.flush();
            showMessage("\nSERVER - "+ message);
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

