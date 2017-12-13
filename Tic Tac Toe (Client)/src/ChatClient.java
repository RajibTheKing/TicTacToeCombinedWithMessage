import java.net.*;
import java.io.*;
import java.util.jar.Attributes;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class ChatClient implements Runnable 
{
    private Socket socket = null;
    private Thread thread = null;
    private DataInputStream console = null;
    private DataOutputStream streamOut = null;
    private ChatClientThread client = null;
    private String name;
    
    public ChatClient(String serverName, int serverPort, String n) 
    {
        name=n;
        System.out.println("Establishing connection. Please wait ...");
        
             
        try {
            socket = new Socket(serverName, serverPort);
            System.out.println("Connected: " + socket);
            sendUserName();
            start();
            
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
        
        
    }
    Socket getSocketinfo()
    {
        return socket;
    }
    DataInputStream get_dis()
    {
        return console;
    }
    
    DataOutputStream get_dos()
    {
        return streamOut;
    }

    public void run() {
        while (thread != null) {
            try {
                streamOut.writeUTF(console.readLine());
                streamOut.flush();
            } catch (IOException ioe) {
                System.out.println("Sending error: " + ioe.getMessage());
                stop();
            }
        }
    }

    public void handle(String msg) 
    {
        if (msg.equals(".bye")) 
        {
            System.out.println("Good bye. Press RETURN to exit ...");
            Main.frame2.dispose();
            stop();
        } 
        else if(msg.startsWith("give_online_client_list"))
        {
            //System.out.println(msg);
            Main.frame2.UpdateCombo(msg);
        }
        else if(msg.startsWith("request_for_a_game: #"))
        {
            Main.pos_x = Main.frame2.getX();
            Main.pos_y = Main.frame2.getY();
            Main.frame2.dispose();
            Main.frame2 = new ClientMonitor();
            Main.frame2.setLocation(Main.pos_x, Main.pos_y);
            Main.frame2.setVisible(true);
            try {
                streamOut.writeUTF("give_online_client_list");
                streamOut.flush();
            } catch (IOException ex) {
                Logger.getLogger(ChatClient.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            
            if(Main.playing==false)
                Main.frame2.RequestFromOtherClient(msg);
            else
            {
                
                try {
                    streamOut.writeUTF("reply_for_"+msg+"#0");
                    streamOut.flush();
                } catch (IOException ex) {
                    Logger.getLogger(ChatClient.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                
            }
            System.out.println("Inside request");
        }
        else if(msg.startsWith("reply_for_request_for_a_game:"))
        {
            String s="";
            s+=msg.charAt(msg.length()-1);
            int r = Integer.parseInt(s);
            if(r==0)
                JOptionPane.showMessageDialog(null, "Sorry, He/She is not willing to play with you");
            else
            {
                System.out.println("He/She wants to Play with you");
                s="";
                int i;
                for(i=msg.length()-3;i>0;i--)
                {
                    if(msg.charAt(i)=='#') break;
                    s+=msg.charAt(i);
                }
                s=Main.frame2.reverse(s);
                Main.OpponentId=s;
                s="";
                for(i=i-1;i>0;i--)
                {
                    if(msg.charAt(i)=='#') break;
                    s+=msg.charAt(i);
                    
                }
                s=Main.frame2.reverse(s);
                Main.OpponentName=s;
                Main.playing=true;
                System.out.println("Playing with "+Main.ClientId+" and "+Main.OpponentId);
                try {
                    streamOut.writeUTF("start_the_game: #"+Main.ClientId+"#"+Main.OpponentId);
                    streamOut.flush();

                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                
                
            }
        }
        else if(msg.startsWith("start_the_game"))
        {
            System.out.println("Started Game: "+Main.ClientName+" VS "+Main.OpponentName);
            //JOptionPane.showMessageDialog(null, "Started Game: "+Main.ClientName+" VS "+Main.OpponentName);
            
            if(Integer.parseInt(Main.ClientId)>Integer.parseInt(Main.OpponentId))
            {
                Main.ticket=1;
                Main.moveItem="X";
                System.out.println("I got start the game in ticket 1");
            }
            else 
            {
                System.out.println("I got start the game in ticket 0");
                Main.ticket=0;
                Main.moveItem="O";
            }
            
            Main.frame4 = new GameBoard();
            
            
            
        }
        else if(msg.startsWith("send_game_board_information: #"))
        {
            String s="", boardInfo;
            int i;
            for(i=msg.length()-1;i>=0;i--)
            {
                if(msg.charAt(i)=='#') break;
                s+=msg.charAt(i);
                
            }
            s=Main.frame2.reverse(s);
            boardInfo=s;
            Main.ticket=1;
            Main.frame4.UpdateGameBoard(boardInfo);
            
        }
        else if(msg.startsWith("send_winning_result: #"))
        {
            String s="",winningName;
            int i;
            for(i=msg.length()-1;i>=0;i--)
            {
                if(msg.charAt(i)=='#') break;
                s+=msg.charAt(i);
                
            }
            s=Main.frame2.reverse(s);
            winningName=s;
            JOptionPane.showMessageDialog(null, winningName+"  Has Won The Match\nThank You For Playing This Game");
            Main.frame4.dispose();
            Main.playing=false;
            
        }
        else if(msg.startsWith("send_draw_result: #"))
        {
            JOptionPane.showMessageDialog(null, "----->>>>  This Game is Drawn  <<<<<------\nThank You For Playing This Game");
            Main.frame4.dispose();
            Main.playing=false;
        }
            
        else 
        {
            System.out.println(msg);
            String ss;
            ss = Main.frame2.taChathistory.getText();
            if(ss.isEmpty())
                ss=msg;
            else
                ss = ss+"\n"+msg;
            Main.frame2.PrintNewMessage(ss);
          
            
        }
    }

    public void start() throws IOException 
    {
//        console = new DataInputStream(System.in);
//        streamOut = new DataOutputStream(socket.getOutputStream());
        if (thread == null) {
            client = new ChatClientThread(this, socket);
            thread = new Thread(this);
            thread.start();
        }
        
    }

    public void stop() {
        if (thread != null) {
            thread.stop();
            thread = null;
        }
        try {
            if (console != null) {
                console.close();
            }
            if (streamOut != null) {
                streamOut.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException ioe) {
            System.out.println("Error closing ...");
        }
        client.close();
        client.stop();
    }
    
    
    

//    public static void main(String args[]) {
//        ChatClient client = new ChatClient("localhost", 1234);
//        //System.out.println(client.Name);
//    }

    private void sendUserName() 
    {
        //Send the message to the server
        try{
            console = new DataInputStream(System.in);
            streamOut = new DataOutputStream(socket.getOutputStream());
            streamOut.writeUTF(name);
            streamOut.flush();
            
        }catch(Exception e)
        {
            System.out.println(e.getMessage());
        }
            
    }

    
}