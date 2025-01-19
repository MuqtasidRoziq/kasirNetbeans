/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package formAdmin;
import javax.swing.JOptionPane;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import konektor.koneksi;

public class formEditUser extends javax.swing.JFrame {

    private String idUser;
    
    public formEditUser(String idUser, String nama, String role, String email, String username, String password) {
        initComponents();
        inputIdUser.requestFocus();
        this.idUser = idUser;
        inputIdUser.setText(idUser);
        inputFullName.setText(nama);
        selectRole.setSelectedItem(role);
        inputEmail.setText(email);
        inputUsername.setText(username);
        inputPassword.setText(password);
    }
    
// edit data user //
    private void editDataUser(){
        String IDUserLama = this.idUser;
        String IDUserBaru = inputIdUser.getText();
        String nama = inputFullName.getText();
        String role = (String) selectRole.getSelectedItem();
        String email = inputEmail.getText();
        String username = inputUsername.getText();
        String password = inputPassword.getText();

        // Cek jika ada kolom yang kosong
        if (IDUserBaru.isEmpty() || nama.isEmpty() || email.isEmpty() || role.isEmpty() || username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Semua kolom harus diisi!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            Connection con = koneksi.getConnection();

            // 1. Cek jika ID user ada di dalam database
            PreparedStatement checkUser = con.prepareStatement("SELECT * FROM user WHERE id_user = ?");
            checkUser.setString(1, IDUserLama);
            ResultSet rs;
            rs = checkUser.executeQuery();
            if (!rs.next()) {
                JOptionPane.showMessageDialog(this, "ID User tidak ditemukan di database.", "Error", JOptionPane.ERROR_MESSAGE);
                return;  // Berhenti di sini jika ID tidak ada
            }

            // 2. Lakukan update data
            String updateQuery = "UPDATE user SET id_user = ?, nama_user = ?, role = ?, email_user = ?, username_user = ?, password_user = ? WHERE id_user = ?";
            PreparedStatement pst = con.prepareStatement(updateQuery);

            pst.setString(1, IDUserBaru);
            pst.setString(2, nama);
            pst.setString(3, role);
            pst.setString(4, email);
            pst.setString(5, username);
            pst.setString(6, password);
            pst.setString(7, IDUserLama);

            int rowsAffected = pst.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Data user berhasil diperbarui.");
            } else {
                JOptionPane.showMessageDialog(this, "Data user gagal diperbarui.");
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

        jPanel1 = new javax.swing.JPanel();
        lbTambahUser = new javax.swing.JLabel();
        pnBG = new javax.swing.JPanel();
        lbIdUser = new javax.swing.JLabel();
        inputIdUser = new javax.swing.JTextField();
        lbFullName = new javax.swing.JLabel();
        inputFullName = new javax.swing.JTextField();
        lbRole = new javax.swing.JLabel();
        selectRole = new javax.swing.JComboBox<>();
        lbEmail = new javax.swing.JLabel();
        inputEmail = new javax.swing.JTextField();
        lbUsername = new javax.swing.JLabel();
        inputUsername = new javax.swing.JTextField();
        lbPassword = new javax.swing.JLabel();
        inputPassword = new javax.swing.JTextField();
        btnCancel = new javax.swing.JButton();
        btnSave = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        lbTambahUser.setBackground(new java.awt.Color(255, 255, 255));
        lbTambahUser.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 24)); // NOI18N
        lbTambahUser.setText("Edit User");

        pnBG.setBackground(new java.awt.Color(255, 255, 255));
        pnBG.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        lbIdUser.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        lbIdUser.setText("Id User");

        inputIdUser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inputIdUserActionPerformed(evt);
            }
        });

        lbFullName.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        lbFullName.setText("Full Name");

        inputFullName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inputFullNameActionPerformed(evt);
            }
        });

        lbRole.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        lbRole.setText("Role");

        selectRole.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "- Pilih Role - ", "owner", "admin", "kasir" }));
        selectRole.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectRoleActionPerformed(evt);
            }
        });

        lbEmail.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        lbEmail.setText("Email");

        inputEmail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inputEmailActionPerformed(evt);
            }
        });

        lbUsername.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        lbUsername.setText("Username");

        inputUsername.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inputUsernameActionPerformed(evt);
            }
        });

        lbPassword.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        lbPassword.setText("Password");

        inputPassword.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inputPasswordActionPerformed(evt);
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

        btnSave.setBackground(new java.awt.Color(51, 51, 255));
        btnSave.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        btnSave.setForeground(new java.awt.Color(255, 255, 255));
        btnSave.setText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnBGLayout = new javax.swing.GroupLayout(pnBG);
        pnBG.setLayout(pnBGLayout);
        pnBGLayout.setHorizontalGroup(
            pnBGLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnBGLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnBGLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pnBGLayout.createSequentialGroup()
                        .addComponent(btnCancel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnSave))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pnBGLayout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(pnBGLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lbIdUser)
                            .addComponent(inputIdUser, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbFullName)
                            .addComponent(inputFullName, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbRole)
                            .addComponent(selectRole, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbEmail)
                            .addComponent(inputEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 221, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbUsername)
                            .addComponent(inputUsername, javax.swing.GroupLayout.PREFERRED_SIZE, 227, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbPassword)
                            .addComponent(inputPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 233, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnBGLayout.setVerticalGroup(
            pnBGLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnBGLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lbIdUser)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(inputIdUser, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbFullName)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(inputFullName, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbRole)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(selectRole, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbEmail)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(inputEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbUsername)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(inputUsername, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbPassword)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(inputPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(pnBGLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(30, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnBG, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(lbTambahUser, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lbTambahUser, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(pnBG, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void inputIdUserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inputIdUserActionPerformed
        inputFullName.requestFocus();
    }//GEN-LAST:event_inputIdUserActionPerformed

    private void inputFullNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inputFullNameActionPerformed
        selectRole.requestFocus();
    }//GEN-LAST:event_inputFullNameActionPerformed

    private void inputEmailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inputEmailActionPerformed
        inputUsername.requestFocus();
    }//GEN-LAST:event_inputEmailActionPerformed

    private void inputUsernameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inputUsernameActionPerformed
        inputPassword.requestFocus();
    }//GEN-LAST:event_inputUsernameActionPerformed

    private void inputPasswordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inputPasswordActionPerformed

    }//GEN-LAST:event_inputPasswordActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        dispose();
    }//GEN-LAST:event_btnCancelActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        editDataUser();
        dispose();
    }//GEN-LAST:event_btnSaveActionPerformed

    private void selectRoleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectRoleActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_selectRoleActionPerformed

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
            java.util.logging.Logger.getLogger(formEditUser.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(formEditUser.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(formEditUser.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(formEditUser.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                setVisible setVisible = new formEditUser.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnSave;
    private javax.swing.JTextField inputEmail;
    private javax.swing.JTextField inputFullName;
    private javax.swing.JTextField inputIdUser;
    private javax.swing.JTextField inputPassword;
    private javax.swing.JTextField inputUsername;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel lbEmail;
    private javax.swing.JLabel lbFullName;
    private javax.swing.JLabel lbIdUser;
    private javax.swing.JLabel lbPassword;
    private javax.swing.JLabel lbRole;
    private javax.swing.JLabel lbTambahUser;
    private javax.swing.JLabel lbUsername;
    private javax.swing.JPanel pnBG;
    private javax.swing.JComboBox<String> selectRole;
    // End of variables declaration//GEN-END:variables

    private static class setVisible {

        public setVisible(boolean b) {
        }
    }
}
