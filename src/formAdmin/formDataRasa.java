/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package formAdmin;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import konektor.koneksi;
import javax.swing.event.ListSelectionEvent;
import loging.loging.ActivityLogger;

public class formDataRasa extends javax.swing.JPanel {
    
    private final String username;
    public formDataRasa(String username) {
        initComponents();
        this.username = username;
        loadDataProduk();
        
//      Menonaktifkan tombol Edit dan Delete saat pertama kali dibuka
        btnEdit.setEnabled(false);
        btnHapus.setEnabled(false);
        
//      Menambahkan ListSelectionListener ke tabel
        tabProduk.getSelectionModel().addListSelectionListener((ListSelectionEvent event) -> {
            // Cek jika ada baris yang dipilih
            if (!event.getValueIsAdjusting() && tabProduk.getSelectedRow() != -1) {
                btnEdit.setEnabled(true);
                btnHapus.setEnabled(true);
            } else {
                btnEdit.setEnabled(false);
                btnHapus.setEnabled(false);
            }
        });
    }
    
    
//  Fungsi loadDataProduk //
    
    private void loadDataProduk() {
        DefaultTableModel model = (DefaultTableModel) tabProduk.getModel();
        model.setRowCount(0);

        try {
            Connection con = koneksi.getConnection();
            Statement st = con.createStatement();
            String query = "SELECT id_rasa, rasa FROM rasa"; 
            ResultSet rs = st.executeQuery(query);

            while (rs.next()) {
                String idProduk = rs.getString("id_rasa");
                String namaProduk = rs.getString("rasa");
                // Tambahkan data ke model tabel
                model.addRow(new Object[]{idProduk, namaProduk});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    } 
        
//  Fungsi loadDataProduk END //    
    
//  Fungsi editProduk //
    
    private void editDataProduk(){
        int selectedRow = tabProduk.getSelectedRow();
        if (selectedRow != -1) {
            String nama = this.username;
            String idRasa = tabProduk.getValueAt(selectedRow, 0).toString();
            String rasaLama = tabProduk.getValueAt(selectedRow, 1).toString();

        //   Buka form edit dengan data pengguna yang dipilih
            formEditRasa editRasa = new formEditRasa(nama, idRasa, rasaLama);
            editRasa.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                //  Muat ulang data tabel setelah form edit ditutup
                    loadDataProduk();
                }
            });
            editRasa.setVisible(true);
            
        }
    }
    
//  Fungsi editProduk END //
    
//  Fungsi deleteProduk //
    
    private void hapusDataProduk(){
        int selectedRow = tabProduk.getSelectedRow();
        if (selectedRow != -1) {
            String idProduk = tabProduk.getValueAt(selectedRow, 0).toString();
            String namaProduk = tabProduk.getValueAt(selectedRow, 1). toString();
            // Konfirmasi dan hapus data dari database berdasarkan ID
            int confirm = JOptionPane.showConfirmDialog(this, "Apakah Anda yakin ingin menghapus profuk ini?", "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    Connection con = koneksi.getConnection();
                    Statement st = con.createStatement();
                    String deleteQuery = "DELETE FROM rasa WHERE id_rasa = '" + idProduk + "'";
                    st.executeUpdate(deleteQuery);
                    loadDataProduk();
                    JOptionPane.showMessageDialog(this, "produk berhasil dihapus.");
                    ActivityLogger.logDeleteProduk(username, namaProduk);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, "Terjadi kesalahan saat menghapus data.");
                }
            }
        }
    }
    
//  Fungsi deleteProduk END //
    
//  Fungsi cariProduk //
    private void searchProduk(){
        String keyword = inputSearching.getText().trim();
        DefaultTableModel model = (DefaultTableModel) tabProduk.getModel();

        if (keyword.isEmpty()) {
            loadDataProduk();
            JOptionPane.showMessageDialog(this, "Kolom pencarian harus diisi!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        model.setRowCount(0);

        try {
            Connection con = koneksi.getConnection();
            Statement st = con.createStatement();

            // Query untuk mencari data user berdasarkan ID, Nama, atau Email yang cocok dengan kata kunci
            String query = "SELECT id_rasa, rasa " +
                            "FROM rasa WHERE id_rasa LIKE '%" + keyword + "%' " +
                            "OR nama_produk LIKE '%" + keyword + "%' ";

            ResultSet rs = st.executeQuery(query);
            
            // Jika data ditemukan, tambahkan ke model tabel
            while (rs.next()) {
                String idRasa = rs.getString("id_rasa");
                String namaRasa = rs.getString("rasa");
                model.addRow(new Object[]{idRasa, namaRasa});
                ActivityLogger.logSearchProduk(this.username, namaRasa);
            }

            // Pesan jika tidak ada data ditemukan
            if (model.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this, "Data Rasa tidak ditemukan.", "Pencarian", JOptionPane.INFORMATION_MESSAGE);
                loadDataProduk(); 
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Terjadi kesalahan saat mencari data.");
        }
    }
    
//  Fungsi cariProduk END //  
    
    

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        btnTambah = new javax.swing.JButton();
        btnEdit = new javax.swing.JButton();
        btnHapus = new javax.swing.JButton();
        inputSearching = new javax.swing.JTextField();
        btnSearch = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabProduk = new javax.swing.JTable();

        setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 24)); // NOI18N
        jLabel1.setText("Data Rasa");

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        btnTambah.setBackground(new java.awt.Color(51, 51, 255));
        btnTambah.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        btnTambah.setForeground(new java.awt.Color(255, 255, 255));
        btnTambah.setText("Tambah");
        btnTambah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTambahActionPerformed(evt);
            }
        });

        btnEdit.setBackground(new java.awt.Color(51, 51, 255));
        btnEdit.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        btnEdit.setForeground(new java.awt.Color(255, 255, 255));
        btnEdit.setText("Edit");
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });

        btnHapus.setBackground(new java.awt.Color(51, 51, 255));
        btnHapus.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        btnHapus.setForeground(new java.awt.Color(255, 255, 255));
        btnHapus.setText("Hapus");
        btnHapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHapusActionPerformed(evt);
            }
        });

        inputSearching.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inputSearchingActionPerformed(evt);
            }
        });

        btnSearch.setBackground(new java.awt.Color(51, 51, 255));
        btnSearch.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/cari.png"))); // NOI18N
        btnSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchActionPerformed(evt);
            }
        });

        tabProduk.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Id Rasa", "Rasa"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tabProduk);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jScrollPane1)
                        .addContainerGap())
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(btnTambah)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnEdit)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnHapus)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 200, Short.MAX_VALUE)
                        .addComponent(inputSearching, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnSearch)
                        .addGap(37, 37, 37))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnTambah, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnHapus, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(inputSearching, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnTambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTambahActionPerformed
        formTambahProduk tambahProduk = new formTambahProduk(username);
        tambahProduk.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
            public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                loadDataProduk();
                }
            });
        tambahProduk.setVisible(true);
    }//GEN-LAST:event_btnTambahActionPerformed

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        editDataProduk();
    }//GEN-LAST:event_btnEditActionPerformed

    private void btnHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHapusActionPerformed
        hapusDataProduk();
    }//GEN-LAST:event_btnHapusActionPerformed

    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchActionPerformed
        searchProduk();
    }//GEN-LAST:event_btnSearchActionPerformed

    private void inputSearchingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inputSearchingActionPerformed
        searchProduk();
    }//GEN-LAST:event_inputSearchingActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnEdit;
    private javax.swing.JButton btnHapus;
    private javax.swing.JButton btnSearch;
    private javax.swing.JButton btnTambah;
    private javax.swing.JTextField inputSearching;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tabProduk;
    // End of variables declaration//GEN-END:variables
}
