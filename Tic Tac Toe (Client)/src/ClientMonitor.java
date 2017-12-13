import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.InputMethodEvent;
import java.awt.event.InputMethodListener;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.text.StyledEditorKit;


public class ClientMonitor extends javax.swing.JFrame 
{
    public static int userConfirmation;
    public static String req;
    private Socket skt = null;
    private DataInputStream dis = null;
    private DataOutputStream dos = null;
    JLabel lclientName;
    JComboBox comboBox;
    public ClientMonitor() 
    {
        initComponents();
        skt = Main.cc.getSocketinfo();
        try {
            dis = Main.cc.get_dis();
            dos = Main.cc.get_dos();
            System.out.println(skt+" "+dis+" "+dos);
        } catch (Exception ex) {
            Logger.getLogger(ClientMonitor.class.getName()).log(Level.SEVERE, null, ex);
        }
        lclientName = new JLabel(Main.ClientName);
        Font f = new Font("Times New Roman", Font.BOLD, 20);
        lclientName.setFont(f);
        add(lclientName);
        lclientName.setBounds(150, 160, 100, 30);
        System.out.println(Main.ClientName+"  -->In the clientMonitor");
        
       
    }
    void PrintNewMessage(String str)
    {
        taChathistory.setText(str);
    }
    void RequestFromOtherClient(String msg) 
    {
        req=msg;
        //JOptionPane.showMessageDialog(null,Main.ClientName+" Found the message: "+ msg);
        System.out.println(msg);
        String st="";
        int k=0;
        for(int i=msg.length()-1;i>0;i--)
        {
            if(msg.charAt(i)=='#')
                k++;
            if(k==2)
                break;
            st+=msg.charAt(i);
            
        }
        st=reverse(st);
        System.out.println(st);
        userConfirmation=0;
        Main.frame3 = new OptionPane(st);
        Main.frame3.setVisible(true);
        
            
            
       //int confirm = JOptionPane.showOptionDialog(null, "Do you want to play with -> "+st, "Game Play Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
        
        
        
                
        
        
        
    }
    void UpdateCombo(String msg) 
    {
        String course[] = new String[20];
        int k=-1;
        String ss="";
        char c[] = msg.toCharArray();
        int i=0;
        while(true)
        {
            if(c[i]=='\n')
            {
                i++;
                break;
            }
            i++;
        }
        while(true)
        {
           
            if(c[i]=='\n')
            {
                i++;
                break;
            }
            ss+=c[i];
            i++;
        }
        Main.ClientId=ss;
        System.out.println("Found the id: "+Main.ClientId);
        for(;i<c.length;i++)
        {
            int j=i;
            ss="";
            while(j<c.length&&c[j]!='\n')
            {
                ss+=c[j];
                j++;
            }
            if(i==0)
            {
                i=j;
                continue;
            }
            
            System.out.println(ss);
            course[++k]=ss;
            i=j;
        }
        comboBox = new JComboBox(course);
        add(comboBox);
        comboBox.setBounds(100, 70, 120, 30);
        
       
        
        
    }
    void reply_for_request(int ans)
    {
        if(ans==1)
        {
            int i;
            String st = "";
            for (i = req.length() - 1; i > 0; i--) 
            {
                if (req.charAt(i) == '#') break;
                st += req.charAt(i);
            
            }
            st=reverse(st);
            Main.OpponentId=st;
            System.out.println("Playing with "+Main.ClientId+" and "+Main.OpponentId);
            st="";
            for(i=i-1;i>0;i--)
            {
                if (req.charAt(i) == '#') break;
                st += req.charAt(i);
            }
            st=reverse(st);
            Main.OpponentName=st;
            
                    
        }
        
        try {
            // TODO add your handling code here:
            dos.writeUTF("reply_for_"+req+"#"+String.valueOf(ans));
            dos.flush();
            
        } catch (IOException ex) {
            Logger.getLogger(ClientMonitor.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tfUserInput = new javax.swing.JTextField();
        sendButton = new javax.swing.JButton();
        lOnlineClients = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        taChathistory = new javax.swing.JTextArea();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        requestButton = new javax.swing.JButton();
        UpdateButton = new javax.swing.JButton();
        PersonalChatButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        sendButton.setText("Send");
        sendButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sendButtonActionPerformed(evt);
            }
        });

        lOnlineClients.setText("Online Clients: ");

        taChathistory.setColumns(20);
        taChathistory.setRows(5);
        jScrollPane1.setViewportView(taChathistory);

        jLabel1.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel1.setText("User Name: ");

        requestButton.setText("Request For Game (Tic Tac Toe)");
        requestButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                requestButtonActionPerformed(evt);
            }
        });

        UpdateButton.setText("Update Client Status");
        UpdateButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                UpdateButtonActionPerformed(evt);
            }
        });

        PersonalChatButton.setText("Start Personal Chat");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addGroup(layout.createSequentialGroup()
                                            .addGap(239, 239, 239)
                                            .addComponent(jLabel2))
                                        .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING))
                                    .addComponent(lOnlineClients, javax.swing.GroupLayout.Alignment.LEADING))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(tfUserInput, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(sendButton, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(UpdateButton)
                        .addGap(18, 18, 18)
                        .addComponent(requestButton)
                        .addGap(35, 35, 35)
                        .addComponent(PersonalChatButton, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(requestButton)
                    .addComponent(UpdateButton)
                    .addComponent(PersonalChatButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(29, 29, 29)
                        .addComponent(lOnlineClients)
                        .addGap(60, 60, 60)
                        .addComponent(jLabel2)
                        .addGap(10, 10, 10)
                        .addComponent(jLabel1))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tfUserInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(sendButton))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void sendButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sendButtonActionPerformed
        try {
            // TODO add your handling code here:
            dos.writeUTF(tfUserInput.getText().toString());
            dos.flush();
            tfUserInput.setText(null);
        } catch (IOException ex) {
            Logger.getLogger(ClientMonitor.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }//GEN-LAST:event_sendButtonActionPerformed

    private void UpdateButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_UpdateButtonActionPerformed
        try {
            // TODO add your handling code here:
            Main.pos_x = Main.frame2.getX();
            Main.pos_y = Main.frame2.getY();
            Main.frame2.dispose();
            Main.frame2 = new ClientMonitor();
            Main.frame2.setLocation(Main.pos_x, Main.pos_y);
            Main.frame2.setVisible(true);
            dos.writeUTF("give_online_client_list");
            dos.flush();
            
        } catch (IOException ex) {
            Logger.getLogger(ClientMonitor.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }//GEN-LAST:event_UpdateButtonActionPerformed

    private void requestButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_requestButtonActionPerformed
        // TODO add your handling code here:
        System.out.println(comboBox.getSelectedItem().toString());
        
        String t1="",t2="";
        t2=comboBox.getSelectedItem().toString();
        for(int i=t2.length()-2;i>0;i--)
        {
            if(t2.charAt(i)=='(')
                break;
            t1+=t2.charAt(i);
        }
        t2="";
        for(int i=t1.length()-1;i>=0;i--)
        {
            t2+=t1.charAt(i);
        }
        System.out.println(t2);
        
        System.out.println(Main.ClientId);
        if(Main.ClientId.compareTo(t2)==0)
        {
            JOptionPane.showMessageDialog(null, "Wrong Selection of Opponent");
        }
        else if(Main.playing==true)
        {
            JOptionPane.showMessageDialog(null, "Sorry, You are playing another  Game");
        }
        else
        {
            try {
                dos.writeUTF("request_for_a_game: #"+t2);
                dos.flush();
            } catch (IOException ex) {
                Logger.getLogger(ClientMonitor.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            
        }
        
    }//GEN-LAST:event_requestButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton PersonalChatButton;
    private javax.swing.JButton UpdateButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lOnlineClients;
    private javax.swing.JButton requestButton;
    private javax.swing.JButton sendButton;
    public javax.swing.JTextArea taChathistory;
    private javax.swing.JTextField tfUserInput;
    // End of variables declaration//GEN-END:variables

    public String reverse(String st) 
    {
        String temp="";
        for(int i=st.length()-1;i>=0;i--)
            temp+=st.charAt(i);
        return temp;
        
    }

    

    
}
