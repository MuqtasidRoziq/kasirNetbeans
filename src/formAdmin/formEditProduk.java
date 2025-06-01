package formAdmin;
import javax.swing.JOptionPane;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import konektor.koneksi;
import loging.loging.ActivityLogger;

public class formEditProduk extends javax.swing.JFrame {
    
    private String idProduk;
    private String nama;
    private String produkLama;
    
    public formEditProduk(String nama, String idProduk, String produkLama, String hargaBeli, String hargaJual, String stokProduk, String satuanProduk) {
        initComponents();
        this.nama = nama;
        this.idProduk = idProduk;
        inputIdProduk.setText(idProduk);
        inputNamaProduk.setText(produkLama);
        inputHargaBeli.setText(hargaBeli);
        inputHargaJual.setText(hargaJual);
        inputStok.setText(stokProduk);
        selectSatuan.setSelectedItem(satuanProduk);
    }

    private formEditProduk() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
// edit dataProduk  //
    private void editDataProduk(){
        String IdProdukLama = this.idProduk;
        String produkLama = this.produkLama;
        String idProdukBaru = inputIdProduk.getText();
        String namaProduk = inputNamaProduk.getText();
        String hargaBeli = inputHargaBeli.getText();
        String hargaJual = inputHargaJual.getText();
        String stokProduk = inputStok.getText();
        String satuanProduk = (String) selectSatuan.getSelectedItem();

        // Cek jika ada kolom yang kosong
        if (idProdukBaru.isEmpty() || namaProduk.isEmpty() || hargaBeli.isEmpty() || hargaJual.isEmpty() || stokProduk.isEmpty() || satuanProduk.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Semua kolom harus diisi!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            Connection con = koneksi.getConnection();
            PreparedStatement checkUser = con.prepareStatement("SELECT * FROM produk WHERE id_produk = ?");
            checkUser.setString(1, IdProdukLama);
            ResultSet rs;
            rs = checkUser.executeQuery();
            if (!rs.next()) {
                JOptionPane.showMessageDialog(this, "ID Produk tidak ditemukan di database.", "Error", JOptionPane.ERROR_MESSAGE);
                return;  // Berhenti di sini jika ID tidak ada
            }

            // 2. Lakukan update data
            String updateQuery = "UPDATE produk SET id_produk = ?, nama_produk = ?, harga_beli = ?, harga_jual = ?, stok = ?, satuan = ? WHERE id_produk = ?";
            PreparedStatement pst = con.prepareStatement(updateQuery);

            pst.setString(1, idProdukBaru);
            pst.setString(2, namaProduk);
            pst.setString(3, hargaBeli);
            pst.setString(4, hargaJual);
            pst.setString(5, stokProduk);
            pst.setString(6, satuanProduk);
            pst.setString(7, IdProdukLama);

            int rowsAffected = pst.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Produk berhasil diperbarui.");
                ActivityLogger.logEditProduk(this.nama, produkLama);
            } else {
                JOptionPane.showMessageDialog(this, "Produk gagal diperbarui.");
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
        lblHargaBeli = new javax.swing.JLabel();
        inputHargaBeli = new javax.swing.JTextField();
        lblHargaJual = new javax.swing.JLabel();
        inputHargaJual = new javax.swing.JTextField();
        lblStok = new javax.swing.JLabel();
        inputStok = new javax.swing.JTextField();
        lblSatuan = new javax.swing.JLabel();
        selectSatuan = new javax.swing.JComboBox<>();
        btnSave = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        setPreferredSize(new java.awt.Dimension(283, 550));

        pnHead.setBackground(new java.awt.Color(255, 255, 255));
        pnHead.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnHead.setPreferredSize(new java.awt.Dimension(304, 80));

        pnHeader.setBackground(new java.awt.Color(255, 255, 255));
        pnHeader.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lblTambahProduk.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 24)); // NOI18N
        lblTambahProduk.setText("Edit Produk");

        javax.swing.GroupLayout pnHeaderLayout = new javax.swing.GroupLayout(pnHeader);
        pnHeader.setLayout(pnHeaderLayout);
        pnHeaderLayout.setHorizontalGroup(
            pnHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnHeaderLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblTambahProduk)
                .addContainerGap(143, Short.MAX_VALUE))
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
        lblIdProduk.setText("Id Produk");

        inputIdProduk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inputIdProdukActionPerformed(evt);
            }
        });

        lblNamaProduk.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        lblNamaProduk.setText("Nama Produk");

        inputNamaProduk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inputNamaProdukActionPerformed(evt);
            }
        });

        lblHargaBeli.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        lblHargaBeli.setText("Harga Beli");

        inputHargaBeli.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inputHargaBeliActionPerformed(evt);
            }
        });

        lblHargaJual.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        lblHargaJual.setText("Harga Jual");

        inputHargaJual.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inputHargaJualActionPerformed(evt);
            }
        });

        lblStok.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        lblStok.setText("Stok");

        inputStok.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inputStokActionPerformed(evt);
            }
        });

        lblSatuan.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        lblSatuan.setText("Satuan");

        selectSatuan.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "- Pilih Satuan -", "pcs", "kg" }));
        selectSatuan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectSatuanActionPerformed(evt);
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
                    .addGroup(pnIsiContentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(inputHargaBeli, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 197, Short.MAX_VALUE)
                        .addComponent(lblIdProduk)
                        .addComponent(inputIdProduk, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblNamaProduk)
                        .addComponent(inputNamaProduk, javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(lblHargaBeli)
                        .addComponent(lblHargaJual)
                        .addComponent(inputHargaJual)
                        .addComponent(lblStok)
                        .addComponent(inputStok)
                        .addComponent(lblSatuan)
                        .addGroup(pnIsiContentLayout.createSequentialGroup()
                            .addComponent(selectSatuan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(btnSave)))
                    .addComponent(btnCancel))
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblHargaBeli)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(inputHargaBeli, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblHargaJual)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(inputHargaJual, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblStok)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(inputStok, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblSatuan)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnIsiContentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(selectSatuan, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(54, Short.MAX_VALUE))
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
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        getContentPane().add(pnContent, java.awt.BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void inputHargaBeliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inputHargaBeliActionPerformed
        inputHargaJual.requestFocus();
    }//GEN-LAST:event_inputHargaBeliActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        editDataProduk();
        dispose();
    }//GEN-LAST:event_btnSaveActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        dispose();
    }//GEN-LAST:event_btnCancelActionPerformed

    private void inputIdProdukActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inputIdProdukActionPerformed
        inputNamaProduk.requestFocus();
    }//GEN-LAST:event_inputIdProdukActionPerformed

    private void inputNamaProdukActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inputNamaProdukActionPerformed
        inputHargaBeli.requestFocus();
    }//GEN-LAST:event_inputNamaProdukActionPerformed

    private void inputHargaJualActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inputHargaJualActionPerformed
        inputStok.requestFocus();
    }//GEN-LAST:event_inputHargaJualActionPerformed

    private void inputStokActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inputStokActionPerformed
        
    }//GEN-LAST:event_inputStokActionPerformed

    private void selectSatuanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectSatuanActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_selectSatuanActionPerformed

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
            java.util.logging.Logger.getLogger(formEditProduk.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(formEditProduk.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(formEditProduk.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(formEditProduk.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new formEditProduk().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnSave;
    private javax.swing.JTextField inputHargaBeli;
    private javax.swing.JTextField inputHargaJual;
    private javax.swing.JTextField inputIdProduk;
    private javax.swing.JTextField inputNamaProduk;
    private javax.swing.JTextField inputStok;
    private javax.swing.JLabel lblHargaBeli;
    private javax.swing.JLabel lblHargaJual;
    private javax.swing.JLabel lblIdProduk;
    private javax.swing.JLabel lblNamaProduk;
    private javax.swing.JLabel lblSatuan;
    private javax.swing.JLabel lblStok;
    private javax.swing.JLabel lblTambahProduk;
    private javax.swing.JPanel pnContent;
    private javax.swing.JPanel pnHead;
    private javax.swing.JPanel pnHeader;
    private javax.swing.JPanel pnIsiContent;
    private javax.swing.JComboBox<String> selectSatuan;
    // End of variables declaration//GEN-END:variables
}
