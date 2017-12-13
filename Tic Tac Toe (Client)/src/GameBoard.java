
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.plaf.synth.SynthComboBoxUI;


public class GameBoard extends javax.swing.JFrame 
{
    String boardInfo;
    JButton b[][]=new JButton[3][3];
    ButtonHandler h = new ButtonHandler();
    JLabel l1, l2;
    private DataInputStream dis = null;
    private DataOutputStream dos = null;
    public GameBoard() 
    {
        initComponents();
        Initialize_board();
        setLocation(Main.pos_x+500, Main.pos_y-10);
        setVisible(true);
        try {
            dis = Main.cc.get_dis();
            dos = Main.cc.get_dos();
            
        } catch (Exception ex) {
            Logger.getLogger(ClientMonitor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void Initialize_board() 
    {
        for(int i=0;i<3;i++)
            for(int j=0;j<3;j++)
                b[i][j] = new JButton(".");
                
            
        b[0][0].setBounds(100,100,50,50); b[0][1].setBounds(160,100,50,50); b[0][2].setBounds(220,100,50,50);
        b[1][0].setBounds(100,160,50,50); b[1][1].setBounds(160,160,50,50); b[1][2].setBounds(220,160,50,50);
        b[2][0].setBounds(100,220,50,50); b[2][1].setBounds(160,220,50,50); b[2][2].setBounds(220,220,50,50);
        
        for(int i=0;i<3;i++)
            for(int j=0;j<3;j++)
            {
                add(b[i][j]);
                b[i][j].addActionListener(h);
            }
        l1=new JLabel("A Game : "+Main.ClientName+" VS "+Main.OpponentName);
        String sss="";
        if(Main.ticket==1)
            l2 = new JLabel("Now Move Turn is for, Me: ");
        else
            l2 = new JLabel("Now Move Turn is for, Opponent: ");
        l2.setBounds(10, 50, 300, 30);
        add(l2);
        
        l1.setBounds(10, 10, 300, 30);
        add(l1);
        
    }

    void UpdateGameBoard(String board) 
    {
        int k=-1;
        for(int i=0;i<3;i++)
            for(int j=0;j<3;j++)
            {
                String t="";
                t+=board.charAt(++k);
                b[i][j].setText(t);
            }
        l2.setText("Now Move Turn is for: Me");
    }
    class ButtonHandler implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e) 
        {
            if(Main.ticket==1)
            {
                JButton t;
                t=(JButton) e.getSource();
                //System.out.println(t.toString() +"is clicked");
                String ff=".";
                if(t.getText().compareTo(ff)!=0)
                {
                    JOptionPane.showMessageDialog(null, "Sorry This Place is already taken, input in another place");
                }
                else
                {
                    t.setText(Main.moveItem);
                    l2.setText("Now Mov Turn is for, Opponent: ");
                    Main.ticket=0;
                    boardInfo="";
                    for(int i=0;i<3;i++)
                        for(int j=0;j<3;j++)
                            boardInfo+=b[i][j].getText();
                    
                    try {
                        dos.writeUTF("send_game_board_information: #"+boardInfo+"#"+Main.OpponentId);
                        dos.flush();
                    } catch (IOException ex) {
                        Logger.getLogger(GameBoard.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                    
                    
                    
                }
            }
            else
            {
                JOptionPane.showMessageDialog(null, "Please wait for opponents move");
                
            }
        }
        
    }
    
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 386, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 341, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

    
}
