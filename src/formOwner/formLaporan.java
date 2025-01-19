/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package formOwner;

import konektor.koneksi;
import java.sql.Connection;
import java.sql.ResultSet;
import javax.swing.JOptionPane;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.table.DefaultTableModel;


public class formLaporan extends javax.swing.JPanel {

    /**
     * Creates new form formLaporan
     */
    public formLaporan() {
        initComponents();
        txtPendapatanBersih.setEditable(false);
        txtPendapatanKotor.setEditable(false);
        txtTotalPendapatanHariIni.setEditable(false);
        txtPendapatanKeseluruhan.setEditable(false);
        setupCalendarListeners(); 
        
        // Ambil tanggal hari ini
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String tanggalHariIni = sdf.format(new Date());

        // Tampilkan laporan keuangan untuk hari ini.
        laporanKeuangan(tanggalHariIni, tanggalHariIni);

        // Cek apakah ada transaksi hari ini
        if (!cekTransaksiHariIni()) {
            JOptionPane.showMessageDialog(this, "Belum ada pemasukan hari ini!");
        }

        double totalPendapatanHariIni = TotalPendapatanHariIni();
        txtTotalPendapatanHariIni.setText(String.format("%.2f", totalPendapatanHariIni));
        double totalPendapatanKeseluruhan = TotalPendapatanKeseluruhan();
        txtPendapatanKeseluruhan.setText(String.format("%.2f", totalPendapatanKeseluruhan));
    }
    
    private boolean cekTransaksiHariIni() {
        boolean adaTransaksi = false;
        try {
            // Koneksi ke database
            Connection conn = koneksi.getConnection();
            String sql = "SELECT COUNT(*) AS jumlah FROM transaksi WHERE DATE(tanggal_transaksi) = CURDATE()";

            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                int jumlah = rs.getInt("jumlah");
                adaTransaksi = jumlah > 0; 
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error checking today's transactions: " + e.getMessage());
            e.printStackTrace();
        }
        return adaTransaksi;
    }
    
    private double TotalPendapatanHariIni(){
        double totalPendapatanHariIni = 0;
        try{
            Connection conn = koneksi.getConnection();
            String sql = "SELECT SUM(total_harga) AS totalHariIni FROM transaksi WHERE DATE(tanggal_transaksi) = CURDATE()";
             PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery();
             
             if(rs.next()){
                 totalPendapatanHariIni = rs.getDouble("totalHariIni");
             }
        } catch(SQLException e){
            JOptionPane.showMessageDialog(this, "Eror" + e.getMessage());
        }
        return totalPendapatanHariIni;
    }

    private double TotalPendapatanKeseluruhan() {
        double totalPendapatan = 0;
        try {
            // Koneksi ke database
            Connection conn = koneksi.getConnection();
            String sql = "SELECT SUM(total_harga) AS total FROM transaksi";

            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                totalPendapatan = rs.getDouble("total");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error fetching total revenue: " + e.getMessage());
            e.printStackTrace();
        }
        return totalPendapatan;
    }

    // Laporan Keuangan Harian //
    private void laporanKeuangan(String tanggalAwal, String tanggalAkhir) {
        
        tabLaporan.setModel(new DefaultTableModel(
            new Object[][]{},
            new String[]{"Tanggal Transaksi", "Nama Produk", "Total Jumlah", "Total Penjualan", "Total Keuntungan"}
        ));

        try {
            // Koneksi ke database
            Connection conn = koneksi.getConnection();
            String sql = "SELECT t.tanggal_transaksi, p.nama_produk, SUM(dt.jumlah) AS total_jumlah, "
                   + "SUM(dt.subtotal) AS total_penjualan, "
                   + "SUM(dt.jumlah * (p.harga_jual - p.harga_beli)) AS total_keuntungan "
                   + "FROM transaksi t "
                   + "JOIN detail_transaksi dt ON t.id_transaksi = dt.id_transaksi "
                   + "JOIN produk p ON dt.id_produk = p.id_produk "
                   + "WHERE DATE(t.tanggal_transaksi) BETWEEN ? AND ? "
                   + "GROUP BY t.tanggal_transaksi, p.nama_produk";

            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, tanggalAwal); // Parameter tanggal awal
            pst.setString(2, tanggalAkhir); // Parameter tanggal akhir

            ResultSet rs = pst.executeQuery();

            // Reset tabel
            DefaultTableModel tbl = (DefaultTableModel) tabLaporan.getModel(); // Ambil model tabel dari UI
            tbl.setRowCount(0);
            
            double totalPendapatan = 0;
            double totalKeuntungan = 0;

            // Mengisi tabel dengan data
            while (rs.next()) {
                String tanggal = rs.getString("tanggal_transaksi");
                String namaProduk = rs.getString("nama_produk");
                int totalJumlah = rs.getInt("total_jumlah");
                double penjualan = rs.getDouble("total_penjualan");
                double keuntungan = rs.getDouble("total_keuntungan");

                // Tambahkan baris ke tabel
                Object[] row = { tanggal, namaProduk, totalJumlah, penjualan, keuntungan };
                tbl.addRow(row);

                totalPendapatan += penjualan;
                totalKeuntungan += keuntungan;
            }

            // Hitung pendapatan kotor (total pendapatan)
            double pendapatanKotor = totalPendapatan;

            // Hitung pendapatan bersih
            double pendapatanBersih = totalKeuntungan;

            txtPendapatanKotor.setText(String.format("%.2f", pendapatanKotor));
            txtPendapatanBersih.setText(String.format("%.2f", pendapatanBersih));

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error fetching financial report: " + e.getMessage());
            JOptionPane.showMessageDialog(this, "Terjadi kesalahan saat mengambil data laporan: " + e.getMessage());
            e.printStackTrace(); // Untuk debugging
        }
    }
    // Laporan Keuangan Harian End //
    
    private void setupCalendarListeners() {
        txtTanggalAwal.getDateEditor().addPropertyChangeListener("date", evt -> {
            cariLaporan();
        });

        txtTanggalAkhir.getDateEditor().addPropertyChangeListener("date", evt -> {
            cariLaporan();
        });
    }

    
    private void cariLaporan() {
        
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        if (txtTanggalAwal.getDate() != null && txtTanggalAkhir.getDate() != null) {
            String tanggalAwal = sdf.format(txtTanggalAwal.getDate());
            String tanggalAkhir = sdf.format(txtTanggalAkhir.getDate());

            laporanKeuangan(tanggalAwal, tanggalAkhir);
        } else {
            JOptionPane.showMessageDialog(null, "Masukkan kedua tanggal untuk melihat laporan!");
        }
    }


    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txtTotalPendapatanHariIni = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtPendapatanKeseluruhan = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtPendapatanKotor = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtPendapatanBersih = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabLaporan = new javax.swing.JTable();
        txtTanggalAwal = new com.toedter.calendar.JDateChooser();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txtTanggalAkhir = new com.toedter.calendar.JDateChooser();
        btnSearch = new javax.swing.JButton();

        setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 24)); // NOI18N
        jLabel1.setText("Laporan Keuangan");

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel2.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        jLabel2.setText("Total pendapatan hari ini");

        jLabel3.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        jLabel3.setText("Total Pendapatan Keseluruhan");

        txtPendapatanKeseluruhan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPendapatanKeseluruhanActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        jLabel4.setText("Pendapatan Kotor");

        jLabel5.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        jLabel5.setText("Pendapatan Bersih");

        tabLaporan.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(tabLaporan);

        jLabel6.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        jLabel6.setText("Tanggal");

        jLabel7.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        jLabel7.setText("s/d");

        btnSearch.setBackground(new java.awt.Color(51, 51, 255));
        btnSearch.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        btnSearch.setForeground(new java.awt.Color(255, 255, 255));
        btnSearch.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/cari.png"))); // NOI18N
        btnSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(txtPendapatanKotor, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(txtTotalPendapatanHariIni, javax.swing.GroupLayout.Alignment.LEADING))
                            .addComponent(jLabel4))
                        .addGap(50, 50, 50)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel5)
                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtPendapatanKeseluruhan)
                            .addComponent(txtPendapatanBersih))
                        .addGap(43, 43, 43)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(txtTanggalAwal, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel7)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtTanggalAkhir, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnSearch))
                            .addComponent(jLabel6))
                        .addContainerGap(20, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jScrollPane1)
                        .addGap(21, 21, 21))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnSearch, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtTanggalAkhir, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtTanggalAwal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtTotalPendapatanHariIni, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                    .addComponent(txtPendapatanKeseluruhan))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtPendapatanKotor, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                    .addComponent(txtPendapatanBersih))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 248, Short.MAX_VALUE)
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
                        .addGap(0, 514, Short.MAX_VALUE)))
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

    private void txtPendapatanKeseluruhanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPendapatanKeseluruhanActionPerformed
        
    }//GEN-LAST:event_txtPendapatanKeseluruhanActionPerformed

    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchActionPerformed
        cariLaporan();
    }//GEN-LAST:event_btnSearchActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnSearch;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tabLaporan;
    private javax.swing.JTextField txtPendapatanBersih;
    private javax.swing.JTextField txtPendapatanKeseluruhan;
    private javax.swing.JTextField txtPendapatanKotor;
    private com.toedter.calendar.JDateChooser txtTanggalAkhir;
    private com.toedter.calendar.JDateChooser txtTanggalAwal;
    private javax.swing.JTextField txtTotalPendapatanHariIni;
    // End of variables declaration//GEN-END:variables
}
