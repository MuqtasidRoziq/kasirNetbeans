package formOwner;
import java.awt.BorderLayout;
import java.awt.Dimension;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import javax.swing.*;
import java.sql.*;

public class formPenjualan extends javax.swing.JPanel {

    private Connection connection;
    
    public formPenjualan() {
        initComponents();
        connection = konektor.koneksi.getConnection();
        loadData();
    }

    private void loadData() {
        try {
            // Query untuk total produk terjual
            String queryProdukTerjual = "SELECT SUM(jumlah) AS total FROM detail_transaksi";
            try (PreparedStatement ps = connection.prepareStatement(queryProdukTerjual);
                 ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    lbProdukTerjual.setText(String.valueOf(rs.getInt("total")));
                }
            }

            // Query untuk produk terlaris
            String queryProdukTerlaris = 
                "SELECT p.nama_produk, SUM(d.jumlah) AS total " +
                "FROM detail_transaksi d " +
                "JOIN produk p ON d.id_produk = p.id_produk " +
                "GROUP BY p.nama_produk " +
                "ORDER BY total DESC LIMIT 1";
            try (PreparedStatement ps = connection.prepareStatement(queryProdukTerlaris);
                 ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    lbProdukTerlaris.setText(rs.getString("nama_produk"));
                }
            }

            // Query untuk stok produk
            String queryStokProduk = "SELECT SUM(stok) AS total_stok FROM produk";
            try (PreparedStatement ps = connection.prepareStatement(queryStokProduk);
                 ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    lbStok.setText(String.valueOf(rs.getInt("total_stok")));
                }
            }

//            // Query untuk grafik penjualan
//            String queryGrafikPenjualan = 
//                "SELECT DATE(t.tanggal_transaksi) AS tanggal, SUM(d.jumlah) AS total " +
//                "FROM transaksi t " +
//                "JOIN detail_transaksi d ON t.id_transaksi = d.id_transaksi " +
//                "GROUP BY DATE(t.tanggal_transaksi)";
//            try (PreparedStatement ps = connection.prepareStatement(queryGrafikPenjualan);
//                 ResultSet rs = ps.executeQuery()) {
//                while (rs.next()) {
//                    String tanggal = rs.getString("tanggal");
//                    int total = rs.getInt("total");
//                }
//            }
            
            updateGrafikPenjualan();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateGrafikPenjualan() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        try {
            String query = "SELECT MONTH(tanggal_transaksi) AS bulan, SUM(total_harga) AS total_penjualan FROM transaksi GROUP BY MONTH(tanggal_transaksi)";
            try (PreparedStatement ps = connection.prepareStatement(query);
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int bulan = rs.getInt("bulan");
                    double total = rs.getDouble("total_penjualan");
                    dataset.addValue(total, "Penjualan", "Bulan " + bulan);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        JFreeChart chart = ChartFactory.createBarChart( "","Bulan", "Total Penjualan", dataset);
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(400, 300));

        pnGrafikPenjualan.removeAll();
        pnGrafikPenjualan.setLayout(new BorderLayout());
        pnGrafikPenjualan.add(chartPanel, BorderLayout.CENTER);
        pnGrafikPenjualan.revalidate();
        pnGrafikPenjualan.repaint();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jRadioButtonMenuItem1 = new javax.swing.JRadioButtonMenuItem();
        jPanel1 = new javax.swing.JPanel();
        pnProdukTerjual = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        lbProdukTerjual = new javax.swing.JLabel();
        btnShowProdukTerjual = new javax.swing.JButton();
        pnProdukTerlaris = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        lbProdukTerlaris = new javax.swing.JLabel();
        btnShowProdukTerlaris = new javax.swing.JButton();
        pnStokProduk = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        lbStok = new javax.swing.JLabel();
        btnShowStok = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        pnGrafikPenjualan = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();

        jRadioButtonMenuItem1.setSelected(true);
        jRadioButtonMenuItem1.setText("jRadioButtonMenuItem1");

        setBackground(new java.awt.Color(255, 255, 255));
        setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        pnProdukTerjual.setBackground(new java.awt.Color(153, 153, 255));

        jLabel1.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 14)); // NOI18N
        jLabel1.setText("Produk Terjual");

        lbProdukTerjual.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 36)); // NOI18N
        lbProdukTerjual.setText("0");

        btnShowProdukTerjual.setBackground(new java.awt.Color(51, 51, 255));
        btnShowProdukTerjual.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        btnShowProdukTerjual.setForeground(new java.awt.Color(255, 255, 255));
        btnShowProdukTerjual.setText("Lihat Detail");
        btnShowProdukTerjual.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnShowProdukTerjualActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnProdukTerjualLayout = new javax.swing.GroupLayout(pnProdukTerjual);
        pnProdukTerjual.setLayout(pnProdukTerjualLayout);
        pnProdukTerjualLayout.setHorizontalGroup(
            pnProdukTerjualLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnProdukTerjualLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnProdukTerjualLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbProdukTerjual, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(pnProdukTerjualLayout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnShowProdukTerjual, javax.swing.GroupLayout.DEFAULT_SIZE, 103, Short.MAX_VALUE)))
                .addContainerGap())
        );
        pnProdukTerjualLayout.setVerticalGroup(
            pnProdukTerjualLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnProdukTerjualLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnProdukTerjualLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(btnShowProdukTerjual))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lbProdukTerjual, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(26, Short.MAX_VALUE))
        );

        pnProdukTerlaris.setBackground(new java.awt.Color(153, 153, 255));

        jLabel4.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 14)); // NOI18N
        jLabel4.setText("Produk Terlaris");

        lbProdukTerlaris.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 36)); // NOI18N
        lbProdukTerlaris.setText("Nama produk");

        btnShowProdukTerlaris.setBackground(new java.awt.Color(51, 51, 255));
        btnShowProdukTerlaris.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        btnShowProdukTerlaris.setForeground(new java.awt.Color(255, 255, 255));
        btnShowProdukTerlaris.setText("Lihat Detail");
        btnShowProdukTerlaris.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnShowProdukTerlarisActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnProdukTerlarisLayout = new javax.swing.GroupLayout(pnProdukTerlaris);
        pnProdukTerlaris.setLayout(pnProdukTerlarisLayout);
        pnProdukTerlarisLayout.setHorizontalGroup(
            pnProdukTerlarisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnProdukTerlarisLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnProdukTerlarisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnProdukTerlarisLayout.createSequentialGroup()
                        .addComponent(lbProdukTerlaris, javax.swing.GroupLayout.PREFERRED_SIZE, 222, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 9, Short.MAX_VALUE))
                    .addGroup(pnProdukTerlarisLayout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnShowProdukTerlaris, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        pnProdukTerlarisLayout.setVerticalGroup(
            pnProdukTerlarisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnProdukTerlarisLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnProdukTerlarisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(btnShowProdukTerlaris))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbProdukTerlaris, javax.swing.GroupLayout.DEFAULT_SIZE, 57, Short.MAX_VALUE)
                .addGap(18, 18, 18))
        );

        pnStokProduk.setBackground(new java.awt.Color(153, 153, 255));

        jLabel6.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 14)); // NOI18N
        jLabel6.setText("Stok Produk");

        lbStok.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 36)); // NOI18N
        lbStok.setText("0");

        btnShowStok.setBackground(new java.awt.Color(51, 51, 255));
        btnShowStok.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        btnShowStok.setForeground(new java.awt.Color(255, 255, 255));
        btnShowStok.setText("Lihat Detail");
        btnShowStok.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnShowStokActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnStokProdukLayout = new javax.swing.GroupLayout(pnStokProduk);
        pnStokProduk.setLayout(pnStokProdukLayout);
        pnStokProdukLayout.setHorizontalGroup(
            pnStokProdukLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnStokProdukLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnStokProdukLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbStok, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(pnStokProdukLayout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnShowStok, javax.swing.GroupLayout.DEFAULT_SIZE, 103, Short.MAX_VALUE)))
                .addContainerGap())
        );
        pnStokProdukLayout.setVerticalGroup(
            pnStokProdukLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnStokProdukLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnStokProdukLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(btnShowStok))
                .addGap(18, 18, 18)
                .addComponent(lbStok, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(26, Short.MAX_VALUE))
        );

        jLabel8.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 24)); // NOI18N
        jLabel8.setText("Grafik Penjualan");

        pnGrafikPenjualan.setBackground(new java.awt.Color(255, 255, 255));
        pnGrafikPenjualan.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout pnGrafikPenjualanLayout = new javax.swing.GroupLayout(pnGrafikPenjualan);
        pnGrafikPenjualan.setLayout(pnGrafikPenjualanLayout);
        pnGrafikPenjualanLayout.setHorizontalGroup(
            pnGrafikPenjualanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 445, Short.MAX_VALUE)
        );
        pnGrafikPenjualanLayout.setVerticalGroup(
            pnGrafikPenjualanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 357, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(pnProdukTerlaris, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(pnProdukTerjual, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(pnStokProduk, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pnGrafikPenjualan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(89, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(pnStokProduk, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pnProdukTerjual, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pnProdukTerlaris, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(pnGrafikPenjualan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jLabel9.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 24)); // NOI18N
        jLabel9.setText("Dashboard ");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(8, Short.MAX_VALUE)
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnShowProdukTerlarisActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnShowProdukTerlarisActionPerformed
        formProdukTerlaris produkLaris = new formProdukTerlaris();
        produkLaris.setVisible(true);
    }//GEN-LAST:event_btnShowProdukTerlarisActionPerformed

    private void btnShowStokActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnShowStokActionPerformed
        formStokProduk stok = new formStokProduk();
        stok.setVisible(true);
    }//GEN-LAST:event_btnShowStokActionPerformed

    private void btnShowProdukTerjualActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnShowProdukTerjualActionPerformed
        formProdukTerjual produkTerjual = new formProdukTerjual();
        produkTerjual.setVisible(true);
    }//GEN-LAST:event_btnShowProdukTerjualActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnShowProdukTerjual;
    private javax.swing.JButton btnShowProdukTerlaris;
    private javax.swing.JButton btnShowStok;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JRadioButtonMenuItem jRadioButtonMenuItem1;
    private javax.swing.JLabel lbProdukTerjual;
    private javax.swing.JLabel lbProdukTerlaris;
    private javax.swing.JLabel lbStok;
    private javax.swing.JPanel pnGrafikPenjualan;
    private javax.swing.JPanel pnProdukTerjual;
    private javax.swing.JPanel pnProdukTerlaris;
    private javax.swing.JPanel pnStokProduk;
    // End of variables declaration//GEN-END:variables
}
