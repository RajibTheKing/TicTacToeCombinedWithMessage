
import java.net.*;
import java.io.*;
import java.util.LinkedList;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ChatServer implements Runnable 
{
    public static String opMessage;
    public static int pos_x, pos_y;
    public static ServerMonitor frame;
    public static String tempName;
    public static LinkedList<String> ClientList = new LinkedList<String>();
    private ChatServerThread clients[] = new ChatServerThread[50];
    private ServerSocket server = null;
    private Socket socket=null;
    private Thread thread = null;
    private int clientCount = 0;
    private DataInputStream ds = null;

    public ChatServer(int port) 
    {
        try {
            System.out.println("Binding to port " + port + ", please wait  ...");
            server = new ServerSocket(port);
            System.out.println("Server started: " + server);
            start();
        } catch (IOException ioe) {
            System.out.println("Can not bind to port " + port + ": " + ioe.getMessage());
        }
    }

    public void run() {
        while (thread != null) 
        {
            try {
                System.out.println("Waiting for a client ...");
                socket=server.accept();
                Receive_UserName();
                addThread(socket);
            } catch (Exception ioe) {
                System.out.println("Server accept error: " + ioe);
                stop();
            }
        }
    }

    public void start() {
        if (thread == null) {
            thread = new Thread(this);
            thread.start();
        }

    }

    public void stop() {
        if (thread != null) {
            thread.stop();
            thread = null;
        }
    }

    private int findClient(int ID) {
        for (int i = 0; i < clientCount; i++) {
            if (clients[i].getID() == ID) {
                return i;
            }
        }
        return -1;
    }

    public synchronized void handle(int ID, String input) 
    {
        if (input.equals(".bye")) 
        {
            clients[findClient(ID)].send(".bye");
            remove(ID);
        }
        else if(input.startsWith("give_online_client_list"))
        {
            String str="give_online_client_list"+"\n"+ID;
            for(int i=0;i<clientCount;i++)
            {
                str+="\n"+clients[i].name+"("+clients[i].ID+")";
            }
            
            clients[findClient(ID)].send(str);
            
        }
        else if(input.startsWith("request_for_a_game: #"))
        {
            int id1, id2;
            id1=ID;
            String s="", t="";
            for(int i=input.length()-1;i>0;i--)
            {
                if(input.charAt(i)=='#')
                    break;
                s+=input.charAt(i);
            }
            for(int i=s.length()-1;i>=0;i--)
            {
                t+=s.charAt(i);
            }
            id2=Integer.parseInt(t);
            //System.out.println("I am inside request for a game");
            
            clients[findClient(id2)].send("request_for_a_game: #"+clients[findClient(id1)].name+"#"+id1);
            
            
        }
        else if(input.startsWith("reply_for_request_for_a_game:"))
        {
            String s="";
            s+=input.charAt(input.length()-1);
            int r = Integer.parseInt(s);
            s="";
            for(int i=input.length()-3;i>0;i--)
            {
                if(input.charAt(i)=='#') break;
                s+=input.charAt(i);
            }
            s=reverse(s);
            int id1=Integer.parseInt(s);
            if(r==0)
                clients[findClient(id1)].send("reply_for_request_for_a_game: #"+clients[findClient(ID)].name+"#"+ID+"#0");
            else
                clients[findClient(id1)].send("reply_for_request_for_a_game: #"+clients[findClient(ID)].name+"#"+ID+"#1");
            
        }
        else if(input.startsWith("start_the_game: #"))
        {
            int id1, id2, i;
            String s="";
            
            for(i=input.length()-1;i>=0;i--)
            {
                if(input.charAt(i)=='#') break;
                s+=input.charAt(i);
                
            }
            s=reverse(s);
            id2 = Integer.parseInt(s);
            s="";
            for(i=i-1;i>0;i--)
            {
                if(input.charAt(i)=='#') break;
                s+=input.charAt(i);
                
            }
            s=reverse(s);
            id1=Integer.parseInt(s);
            
            clients[findClient(id1)].send("start_the_game");
            clients[findClient(id2)].send("start_the_game");
            
            
        }
        else if(input.startsWith("send_game_board_information: #"))
        {
            int destID, i;
            String s="", boardInfo;
            
            for(i=input.length()-1;i>=0;i--)
            {
                if(input.charAt(i)=='#') break;
                s+=input.charAt(i);
                
            }
            s=reverse(s);
            destID = Integer.parseInt(s);
            s="";
            for(i=i-1;i>0;i--)
            {
                if(input.charAt(i)=='#') break;
                s+=input.charAt(i);
                
            }
            s=reverse(s);
            boardInfo=s;
            boolean result = CheckBoardInfo(boardInfo);
            boolean checkFull = CheckIsFull(boardInfo);
            if(result==true)
            {
                clients[findClient(ID)].send("send_winning_result: #"+clients[findClient(ID)].name);
                clients[findClient(destID)].send("send_winning_result: #"+clients[findClient(ID)].name);
            }
            else if(checkFull==true)
            {
                clients[findClient(destID)].send("send_game_board_information: #"+boardInfo);
                
                clients[findClient(ID)].send("send_draw_result: #");
                clients[findClient(destID)].send("send_draw_result: #");
            }
            else
                clients[findClient(destID)].send("send_game_board_information: #"+boardInfo);
            
            
            
        }
        else 
        {
            for (int i = 0; i < clientCount; i++) 
            {
                clients[i].send(clients[findClient(ID)].name + ": " + input);
            }
        }
    }

    public synchronized void remove(int ID) 
    {
        int pos = findClient(ID);
        
        if (pos >= 0) 
        {
            ChatServerThread toTerminate = clients[pos];
            System.out.println("Removing client thread " + ID + " at " + pos);
            ChatServer.opMessage = ChatServer.frame.taOperation.getText();
            ChatServer.opMessage = ChatServer.opMessage + "\n" + "Removing client Thread "+ID+" at "+pos;
            if (pos < clientCount - 1) {
                for (int i = pos + 1; i < clientCount; i++) {
                    clients[i - 1] = clients[i];
                }
            }
            clientCount--;
            Remove_From_ClientList();
            try {
                toTerminate.close();
            } catch (IOException ioe) {
                System.out.println("Error closing thread: " + ioe);
            }
            toTerminate.stop();
        }
        
        System.out.println("About to call remove from client");
        
    }

    private void addThread(Socket socket) 
    {
        
        if (clientCount < clients.length) 
        {
            System.out.println("Client accepted: " + socket);
            ChatServer.opMessage = ChatServer.frame.taOperation.getText();
            ChatServer.opMessage = ChatServer.opMessage + "\n" + "Client Accepted:  "+socket;
            
            ChatServer.pos_x = ChatServer.frame.getX();
            ChatServer.pos_y = ChatServer.frame.getY();
            ChatServer.frame.dispose();
            ChatServer.frame = new ServerMonitor();
            
            clients[clientCount] = new ChatServerThread(this, socket);
            try {
                clients[clientCount].open();
                clients[clientCount].start();
                clientCount++;
            } catch (IOException ioe) {
                System.out.println("Error opening thread: " + ioe);
            }
        } else {
            System.out.println("Client refused: maximum " + clients.length + " reached.");
        }
    }

    

    private void Receive_UserName() throws IOException {
        //Reading the message from the client
        ds = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
        tempName=ds.readUTF();
        System.out.println(tempName);
        ClientList.add(tempName);
        ChatServer.pos_x = ChatServer.frame.getX();
        ChatServer.pos_y = ChatServer.frame.getY();
        ChatServer.frame.dispose();
        ChatServer.frame = new ServerMonitor();
        
    }

    private String reverse(String s) 
    {
        String t="";
        for(int i=s.length()-1;i>=0;i--)
            t+=s.charAt(i);
        return t;
    }

    private boolean CheckBoardInfo(String s) 
    {
        if(s.charAt(0)!='.'&& s.charAt(0)==s.charAt(1)&&s.charAt(1)==s.charAt(2))
            return true;
        if(s.charAt(3)!='.'&&s.charAt(3)==s.charAt(4)&&s.charAt(4)==s.charAt(5))
            return true;
        if(s.charAt(6)!='.'&&s.charAt(6)==s.charAt(7)&&s.charAt(7)==s.charAt(8))
            return true;
        if(s.charAt(0)!='.'&&s.charAt(0)==s.charAt(3)&&s.charAt(3)==s.charAt(6))
            return true;
        if(s.charAt(1)!='.'&&s.charAt(1)==s.charAt(4)&&s.charAt(4)==s.charAt(7))
            return true;
        if(s.charAt(2)!='.'&&s.charAt(2)==s.charAt(5)&&s.charAt(5)==s.charAt(8))
            return true;
        if(s.charAt(0)!='.'&&s.charAt(0)==s.charAt(4)&&s.charAt(4)==s.charAt(8))
            return true;
        if(s.charAt(2)!='.'&&s.charAt(2)==s.charAt(4)&&s.charAt(4)==s.charAt(6))
            return true;
        return false;
    }

    private boolean CheckIsFull(String b) 
    {
        for(int i=0;i<b.length();i++)
        {
            if(b.charAt(i)=='.')
                return false;
        }
        return true;
    }
    private void Remove_From_ClientList() 
    {
        ClientList.removeAll(ClientList);
        for(int i=0;i<clientCount;i++)
            ClientList.add(clients[i].name);
        ChatServer.pos_x = ChatServer.frame.getX();
        ChatServer.pos_y = ChatServer.frame.getY();
        ChatServer.frame.dispose();
        ChatServer.frame = new ServerMonitor();
        
        
    }
    
    
    public static void main(String args[]) 
    {
       pos_x=100;
       pos_y=100;
       opMessage = "Oparation List is given below:";
       ChatServer s = new ChatServer(1234);
       
       frame = new ServerMonitor();
       
    }

    
}