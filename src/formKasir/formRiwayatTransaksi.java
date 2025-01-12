package formKasir;

import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import konektor.koneksi;

public class formRiwayatTransaksi extends javax.swing.JPanel {

    /**
     * Creates new form formRiwayatTransaksi
     */
    public formRiwayatTransaksi() {
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
            
            // Format ubah posisi tanggal
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
        String keyword = inputSeacrh.getText().trim();
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

        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabRiwayat = new javax.swing.JTable();
        inputSeacrh = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();

        setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 24)); // NOI18N
        jLabel1.setText("Riwayat Transaksi");

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

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
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tabRiwayat);
        if (tabRiwayat.getColumnModel().getColumnCount() > 0) {
            tabRiwayat.getColumnModel().getColumn(0).setResizable(false);
            tabRiwayat.getColumnModel().getColumn(1).setResizable(false);
            tabRiwayat.getColumnModel().getColumn(2).setResizable(false);
            tabRiwayat.getColumnModel().getColumn(3).setResizable(false);
            tabRiwayat.getColumnModel().getColumn(4).setResizable(false);
            tabRiwayat.getColumnModel().getColumn(5).setResizable(false);
            tabRiwayat.getColumnModel().getColumn(6).setResizable(false);
        }

        inputSeacrh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inputSeacrhActionPerformed(evt);
            }
        });

        jButton1.setBackground(new java.awt.Color(51, 51, 255));
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/cari.png"))); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 632, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(inputSeacrh, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1)
                        .addGap(34, 34, 34)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(inputSeacrh, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 374, Short.MAX_VALUE)
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

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        searchHistory();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void inputSeacrhActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inputSeacrhActionPerformed
        searchHistory();
    }//GEN-LAST:event_inputSeacrhActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField inputSeacrh;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tabRiwayat;
    // End of variables declaration//GEN-END:variables
}
