



public class ClientView extends javax.swing.JFrame 
{
    String name, HostAdd, PostAdd;
    public ClientView() 
    {
        initComponents();
        
    }

    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lName = new javax.swing.JLabel();
        tfName = new javax.swing.JTextField();
        okButton = new javax.swing.JButton();
        lHostAdd = new javax.swing.JLabel();
        tfHostAdd = new javax.swing.JTextField();
        lPortAdd = new javax.swing.JLabel();
        tfPortAdd = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        lName.setText("Enter Your Name: ");

        okButton.setText("OK");
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });

        lHostAdd.setText("Enter Host Address: ");

        tfHostAdd.setText("127.0.0.1");

        lPortAdd.setText("Enter Port Address:");

        tfPortAdd.setText("1234");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(91, 91, 91)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(okButton)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(lName)
                            .addGap(30, 30, 30)
                            .addComponent(tfName, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lHostAdd)
                            .addComponent(lPortAdd))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(tfHostAdd)
                            .addComponent(tfPortAdd))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(52, 52, 52)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lName)
                    .addComponent(tfName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lHostAdd)
                    .addComponent(tfHostAdd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lPortAdd)
                    .addComponent(tfPortAdd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15)
                .addComponent(okButton)
                .addContainerGap(130, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
        // TODO add your handling code here:
        name = tfName.getText().toString();
        System.out.println(name);
        Main.ClientName=name;
        HostAdd = tfHostAdd.getText().toString();
        PostAdd = tfPortAdd.getText().toString();
        Main.cc = new ChatClient(HostAdd, Integer.parseInt(PostAdd), name);
        
        Main.frame2 = new ClientMonitor();
        Main.frame2.setVisible(true);
        Main.frame.dispose();
        
        
        
    }//GEN-LAST:event_okButtonActionPerformed

   

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel lHostAdd;
    private javax.swing.JLabel lName;
    private javax.swing.JLabel lPortAdd;
    private javax.swing.JButton okButton;
    private javax.swing.JTextField tfHostAdd;
    private javax.swing.JTextField tfName;
    private javax.swing.JTextField tfPortAdd;
    // End of variables declaration//GEN-END:variables
}
