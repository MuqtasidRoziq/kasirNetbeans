/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package formAdmin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import konektor.koneksi;

/**
 *
 * @author muqta
 */
public class formRTransaksi extends javax.swing.JPanel {

    /**
     * Creates new form formTransaksi
     */
    public formRTransaksi() {
        initComponents();
        historyTransaction();
    }

// Riwayat Transaksi //
    private void historyTransaction(){
        DefaultTableModel tbl = (DefaultTableModel) tabRiwayat.getModel(); // Mengambil model tabel
        tbl.setRowCount(0);
        try {
            Connection conn = koneksi.getConnection();  // Menghubungkan ke database
            String sql = "SELECT t.id_transaksi, t.tanggal_transaksi, u.nama_user, "
                       + "p.nama_produk, dt.harga_produk, dt.jumlah, dt.subtotal "
                       + "FROM transaksi t "
                       + "JOIN detail_transaksi dt ON t.id_transaksi = dt.id_transaksi "
                       + "JOIN user u ON t.id_user = u.id_user "
                       + "JOIN produk p ON dt.id_produk = p.id_produk "
                       + "ORDER BY t.tanggal_transaksi DESC";
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            
            SimpleDateFormat dbFormat = new SimpleDateFormat("yyyy-MM-dd"); // Format tanggal dari database
            SimpleDateFormat ubahFormat = new SimpleDateFormat("dd-MMMM-yyyy"); // Format tanggal yang diinginkan

            // Mengisi data ke dalam table model
            while (rs.next()) {
                String idTransaksi = rs.getString("id_transaksi");
                String tanggalTransaksi = rs.getString("tanggal_transaksi");
                String namaUser = rs.getString("nama_user");
                String namaProduk = rs.getString("nama_produk");
                double hargaProduk = rs.getDouble("harga_produk");
                int jumlah = rs.getInt("jumlah");
                double subtotal = rs.getDouble("subtotal");

                // Format ulang tanggal
                try {
                    tanggalTransaksi = ubahFormat.format(dbFormat.parse(tanggalTransaksi));
                } catch (Exception e) {
                    tanggalTransaksi = "Invalid Date";
                }
                
                Object[] row = { idTransaksi, tanggalTransaksi, namaUser, namaProduk, 
                                 hargaProduk, jumlah, subtotal };
                tbl.addRow(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error loading transaction history: " + e.getMessage());
        }
    }
// Riwayat Transaksi End // 
    
// Search History //
    private void searchHistory(){
        String keyword = jTextField1.getText().trim();
        DefaultTableModel model = (DefaultTableModel) tabRiwayat.getModel();

        if (keyword.isEmpty()) {
            historyTransaction();
            JOptionPane.showMessageDialog(this, "Kolom pencarian harus diisi!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        model.setRowCount(0);

        String sql = "SELECT t.id_transaksi, t.tanggal_transaksi, u.nama_user, "
                      + "p.nama_produk, dt.harga_produk, dt.jumlah, dt.subtotal "
                      + "FROM transaksi t "
                      + "JOIN detail_transaksi dt ON t.id_transaksi = dt.id_transaksi "
                      + "JOIN user u ON t.id_user = u.id_user "
                      + "JOIN produk p ON dt.id_produk = p.id_produk "
                      + "WHERE t.id_transaksi LIKE ? OR u.nama_user LIKE ? OR p.nama_produk LIKE ? "
                      + "ORDER BY t.tanggal_transaksi DESC";
    
        try (Connection conn = koneksi.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            // Mengganti tanda tanya dengan kata kunci pencarian
            String searchPattern = "%" + keyword + "%";
            pst.setString(1, searchPattern);  // Pencarian berdasarkan id_transaksi
            pst.setString(2, searchPattern);  // Pencarian berdasarkan nama_user
            pst.setString(3, searchPattern);  // Pencarian berdasarkan nama_produk

            ResultSet rs = pst.executeQuery();

            // Menambahkan hasil pencarian ke dalam tabel
            while (rs.next()) {
                String idTransaksi = rs.getString("id_transaksi");
                String tanggalTransaksi = rs.getString("tanggal_transaksi");
                String namaUser = rs.getString("nama_user");
                String namaProduk = rs.getString("nama_produk");
                double hargaProduk = rs.getDouble("harga_produk");
                int jumlah = rs.getInt("jumlah");
                double subtotal = rs.getDouble("subtotal");
                
                // Menambahkan baris hasil pencarian ke tabel
                Object[] row = { idTransaksi, tanggalTransaksi, namaUser, namaProduk, 
                                 hargaProduk, jumlah, subtotal };
                model.addRow(row);
                
                            // Pesan jika tidak ada data ditemukan
                if (model.getRowCount() == 0) {
                    JOptionPane.showMessageDialog(this, "Data Transaksi tidak ditemukan.", "Pencarian", JOptionPane.INFORMATION_MESSAGE);
                    historyTransaction(); // Tampilkan kembali data asli jika tidak ada hasil
                }    
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error searching transaction history: " + e.getMessage());
        }
    }
    
// Search History End //
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel2 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jTextField1 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabRiwayat = new javax.swing.JTable();

        setBackground(new java.awt.Color(255, 255, 255));

        jLabel2.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 24)); // NOI18N
        jLabel2.setText("Riwayat Transaksi");

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });

        jButton1.setBackground(new java.awt.Color(51, 51, 255));
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/cari.png"))); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        tabRiwayat.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Id Transaksi", "Tanggal Transaksi", "Nama Kasir", "Nama Produk", "Harga", "Jumlah", "SubTotal"
            }
        ));
        jScrollPane1.setViewportView(tabRiwayat);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1)
                .addGap(42, 42, 42))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 583, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                    .addComponent(jTextField1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 356, Short.MAX_VALUE)
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
                        .addComponent(jLabel2)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        searchHistory();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        searchHistory();
    }//GEN-LAST:event_jTextField1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTable tabRiwayat;
    // End of variables declaration//GEN-END:variables
}
