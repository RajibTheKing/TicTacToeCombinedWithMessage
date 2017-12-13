
import javax.swing.JFrame;
public class Main 
{
    public static int pos_x, pos_y;
    public static JFrame frame;
    public static ClientMonitor frame2;
    public static OptionPane frame3;
    public static GameBoard frame4;
    public static ChatClient cc;
    public static String ClientName,OpponentName=null, ClientId, OpponentId=null;
    public static char boardInfo[][] = new char[3][3];
    public static boolean playing;
    public static int ticket;
    public static String moveItem;
    
    public static void main(String args[])
    {
        playing=false;
        frame = new ClientView();
        frame.setVisible(true);
    }
}
