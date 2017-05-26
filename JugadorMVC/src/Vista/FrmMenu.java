
package Vista;

import Control.Controlador;


public class FrmMenu extends javax.swing.JFrame {

    Controlador control;


    
    public FrmMenu(Controlador control){
        initComponents();
        setLocationRelativeTo(null);
        
        this.control = control;
        
    }
    
    

    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        btnEmpezar = new java.awt.Button();
        jLabel2 = new javax.swing.JLabel();
        lbConectados = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        lbJugador = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel1.setText("JUEGO DEL AHORCADO");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 10, -1, -1));

        btnEmpezar.setActionCommand("empezar");
        btnEmpezar.setBackground(new java.awt.Color(0, 204, 0));
        btnEmpezar.setForeground(new java.awt.Color(255, 255, 255));
        btnEmpezar.setLabel("Empezar");
        btnEmpezar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEmpezarActionPerformed(evt);
            }
        });
        jPanel1.add(btnEmpezar, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 90, -1, -1));

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel2.setText("Conectados:");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 90, -1, 24));

        lbConectados.setText("0");
        jPanel1.add(lbConectados, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 90, 71, 24));

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel3.setText("Jugador: ");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 80, -1, 10));

        lbJugador.setText("nickname");
        jPanel1.add(lbJugador, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 70, 50, 24));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 453, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnEmpezarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEmpezarActionPerformed
        
        control.iniciarJuego();
        
    }//GEN-LAST:event_btnEmpezarActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private java.awt.Button btnEmpezar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel lbConectados;
    private javax.swing.JLabel lbJugador;
    // End of variables declaration//GEN-END:variables

    public void setNombre(String nombre){
        lbJugador.setText(nombre);
    }
    
    public void setCantidadJugadores(int cantidad){
        lbConectados.setText( ""+cantidad );
    }
    
}
