package formAdmin;
import javax.swing.JOptionPane;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import konektor.koneksi;
import loging.loging.ActivityLogger;

public class formEditRasa extends javax.swing.JFrame {
    
    private String idRasa;
    private String nama;
    private String rasaLama;
    
    public formEditRasa(String nama, String idRasa, String rasaLama) {
        initComponents();
        this.nama = nama;
        this.idRasa = idRasa;
        inputIdProduk.setText(idRasa);
        inputNamaProduk.setText(rasaLama);
    }

    private formEditRasa() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
// edit dataProduk  //
    private void editDataRasa(){
        String IdRasaLama = this.idRasa;
        String rasaLama = this.rasaLama;
        String idRasaBaru = inputIdProduk.getText();
        String namaRasa = inputNamaProduk.getText();

        // Cek jika ada kolom yang kosong
        if (idRasaBaru.isEmpty() || namaRasa.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Semua kolom harus diisi!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            Connection con = koneksi.getConnection();
            PreparedStatement checkUser = con.prepareStatement("SELECT * FROM rasa WHERE id_rasa = ?");
            checkUser.setString(1, IdRasaLama);
            ResultSet rs;
            rs = checkUser.executeQuery();
            if (!rs.next()) {
                JOptionPane.showMessageDialog(this, "ID Produk tidak ditemukan di database.", "Error", JOptionPane.ERROR_MESSAGE);
                return;  // Berhenti di sini jika ID tidak ada
            }

            // 2. Lakukan update data
            String updateQuery = "UPDATE rasa SET id_rasa = ?, rasa = ? WHERE id_rasa = ?";
            PreparedStatement pst = con.prepareStatement(updateQuery);

            pst.setString(1, idRasaBaru);
            pst.setString(2, namaRasa);
            pst.setString(3, IdRasaLama);

            int rowsAffected = pst.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Rasa berhasil diperbarui.");
                ActivityLogger.logEditProduk(this.nama, rasaLama);
            } else {
                JOptionPane.showMessageDialog(this, "Rasa gagal diperbarui.");
            }
            this.dispose();
            

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Terjadi kesalahan saat memperbarui data.");
        }
    }
// edit data user end // 
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnHead = new javax.swing.JPanel();
        pnHeader = new javax.swing.JPanel();
        lblTambahProduk = new javax.swing.JLabel();
        pnContent = new javax.swing.JPanel();
        pnIsiContent = new javax.swing.JPanel();
        lblIdProduk = new javax.swing.JLabel();
        inputIdProduk = new javax.swing.JTextField();
        lblNamaProduk = new javax.swing.JLabel();
        inputNamaProduk = new javax.swing.JTextField();
        btnSave = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        pnHead.setBackground(new java.awt.Color(255, 255, 255));
        pnHead.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnHead.setPreferredSize(new java.awt.Dimension(304, 80));

        pnHeader.setBackground(new java.awt.Color(255, 255, 255));
        pnHeader.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lblTambahProduk.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 24)); // NOI18N
        lblTambahProduk.setText("Edit Rasa");

        javax.swing.GroupLayout pnHeaderLayout = new javax.swing.GroupLayout(pnHeader);
        pnHeader.setLayout(pnHeaderLayout);
        pnHeaderLayout.setHorizontalGroup(
            pnHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnHeaderLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblTambahProduk)
                .addContainerGap(170, Short.MAX_VALUE))
        );
        pnHeaderLayout.setVerticalGroup(
            pnHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnHeaderLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(lblTambahProduk)
                .addContainerGap(19, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout pnHeadLayout = new javax.swing.GroupLayout(pnHead);
        pnHead.setLayout(pnHeadLayout);
        pnHeadLayout.setHorizontalGroup(
            pnHeadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnHeadLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnHeader, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        pnHeadLayout.setVerticalGroup(
            pnHeadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnHeadLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnHeader, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        getContentPane().add(pnHead, java.awt.BorderLayout.PAGE_START);

        pnContent.setBackground(new java.awt.Color(255, 255, 255));
        pnContent.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        pnIsiContent.setBackground(new java.awt.Color(255, 255, 255));
        pnIsiContent.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lblIdProduk.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        lblIdProduk.setText("Id Rasa");

        inputIdProduk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inputIdProdukActionPerformed(evt);
            }
        });

        lblNamaProduk.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        lblNamaProduk.setText("Nama Rasa");

        inputNamaProduk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inputNamaProdukActionPerformed(evt);
            }
        });

        btnSave.setBackground(new java.awt.Color(51, 51, 255));
        btnSave.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        btnSave.setForeground(new java.awt.Color(255, 255, 255));
        btnSave.setText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });

        btnCancel.setBackground(new java.awt.Color(51, 51, 255));
        btnCancel.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        btnCancel.setForeground(new java.awt.Color(255, 255, 255));
        btnCancel.setText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnIsiContentLayout = new javax.swing.GroupLayout(pnIsiContent);
        pnIsiContent.setLayout(pnIsiContentLayout);
        pnIsiContentLayout.setHorizontalGroup(
            pnIsiContentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnIsiContentLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnIsiContentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnIsiContentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(lblIdProduk)
                        .addComponent(inputIdProduk, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblNamaProduk)
                        .addComponent(inputNamaProduk, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnIsiContentLayout.createSequentialGroup()
                        .addComponent(btnCancel)
                        .addGap(18, 18, 18)
                        .addComponent(btnSave)))
                .addContainerGap(84, Short.MAX_VALUE))
        );
        pnIsiContentLayout.setVerticalGroup(
            pnIsiContentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnIsiContentLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblIdProduk)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(inputIdProduk, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblNamaProduk)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(inputNamaProduk, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(pnIsiContentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(15, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout pnContentLayout = new javax.swing.GroupLayout(pnContent);
        pnContent.setLayout(pnContentLayout);
        pnContentLayout.setHorizontalGroup(
            pnContentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnContentLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnIsiContent, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        pnContentLayout.setVerticalGroup(
            pnContentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnContentLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnIsiContent, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(7, Short.MAX_VALUE))
        );

        getContentPane().add(pnContent, java.awt.BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        dispose();
    }//GEN-LAST:event_btnCancelActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        editDataRasa();
        dispose();
    }//GEN-LAST:event_btnSaveActionPerformed

    private void inputNamaProdukActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inputNamaProdukActionPerformed

    }//GEN-LAST:event_inputNamaProdukActionPerformed

    private void inputIdProdukActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inputIdProdukActionPerformed
        inputNamaProduk.requestFocus();
    }//GEN-LAST:event_inputIdProdukActionPerformed

    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(formEditRasa.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(formEditRasa.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(formEditRasa.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(formEditRasa.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new formEditRasa().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnSave;
    private javax.swing.JTextField inputIdProduk;
    private javax.swing.JTextField inputNamaProduk;
    private javax.swing.JLabel lblIdProduk;
    private javax.swing.JLabel lblNamaProduk;
    private javax.swing.JLabel lblTambahProduk;
    private javax.swing.JPanel pnContent;
    private javax.swing.JPanel pnHead;
    private javax.swing.JPanel pnHeader;
    private javax.swing.JPanel pnIsiContent;
    // End of variables declaration//GEN-END:variables
}
