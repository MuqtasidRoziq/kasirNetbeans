package formOwner;
import java.awt.*;
import org.jfree.chart.*;
import java.sql.*;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

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
            
            String queryKinerjaKasir = "SELECT u.nama_user AS kasir, COUNT(t.id_transaksi) AS total_transaksi "
                                        + "FROM transaksi t "
                                        + "JOIN user u ON t.id_user = u.id_user "
                                        + "WHERE u.role = 'kasir' "
                                        + "GROUP BY t.id_user, u.nama_user "
                                        + "ORDER BY total_transaksi DESC LIMIT 1;";
            try (PreparedStatement ps = connection.prepareStatement(queryKinerjaKasir);
                 ResultSet rs = ps.executeQuery()) {
                if (rs.next()){
                    lbNamaKasir.setText(rs.getString("kasir"));
                }
            }
            
            GrafikPenjualan();
            GrafikProdukTerlaris();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void GrafikPenjualan() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        try {
            String query = "SELECT MONTH(tanggal_transaksi) AS bulan, SUM(total_harga) AS total_penjualan FROM transaksi GROUP BY MONTH(tanggal_transaksi)";
            try (PreparedStatement ps = connection.prepareStatement(query);
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int bulan = rs.getInt("bulan");
                    double total = rs.getDouble("total_penjualan");
                    String namaBulan = getNamaBulan(bulan);
                    dataset.addValue(total, "Penjualan", "Bulan " + namaBulan);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        JFreeChart chart = ChartFactory.createBarChart( "","Bulan", "", dataset);
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(400, 300));

        pnGrafikPenjualan.removeAll();
        pnGrafikPenjualan.setLayout(new BorderLayout());
        pnGrafikPenjualan.add(chartPanel, BorderLayout.CENTER);
        pnGrafikPenjualan.revalidate();
        pnGrafikPenjualan.repaint();
    }
    private void GrafikProdukTerlaris() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        try {
            String query = "SELECT p.nama_produk, SUM(dt.jumlah) AS total_terjual FROM detail_transaksi dt JOIN produk p ON dt.id_produk = p.id_produk GROUP BY p.nama_produk ORDER BY total_terjual DESC LIMIT 10";
            try (PreparedStatement ps = connection.prepareStatement(query);
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String namaProduk = rs.getString("nama_produk");
                    int totalTerjual = rs.getInt("total_terjual");
                    dataset.addValue(totalTerjual, namaProduk, ""); 
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        JFreeChart chart = ChartFactory.createBarChart( "","produk", "", dataset);
        
        // Kustomisasi warna batang
        CategoryPlot plot = chart.getCategoryPlot();
        BarRenderer renderer = (BarRenderer) plot.getRenderer();

        // Warna-warna untuk batang
        Color[] colors = {
            new Color(79, 129, 189), // Biru
            new Color(192, 80, 77),  // Merah
            new Color(155, 187, 89), // Hijau
            new Color(128, 100, 162),// Ungu
            new Color(75, 172, 198), // Biru Muda
            new Color(247, 150, 70), // Oranye
            new Color(146, 208, 80), // Hijau Terang
            new Color(255, 192, 0),  // Kuning
            new Color(112, 48, 160), // Ungu Gelap
            new Color(255, 128, 0)   // Oranye Terang
        };

        // Atur warna untuk setiap batang
        for (int i = 0; i < dataset.getRowCount(); i++) {
            renderer.setSeriesPaint(i, colors[i % colors.length]);
        }
        
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(400, 300));

        pnProdukLaris.removeAll();
        pnProdukLaris.setLayout(new BorderLayout());
        pnProdukLaris.add(chartPanel, BorderLayout.CENTER);
        pnProdukLaris.revalidate();
        pnProdukLaris.repaint();
    }

    // Fungsi untuk mengubah bulan angka menjadi nama
    private String getNamaBulan(int bulan) {
        String[] namaBulan = {"Januari", "Februari", "Maret", "April", "Mei", "Juni", "Juli", "Agustus", "September", "Oktober", "November", "Desember"};
        return namaBulan[bulan - 1];
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
        pnKinerjaKasir = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        btnShowKasir = new javax.swing.JButton();
        lbNamaKasir = new javax.swing.JLabel();
        pnProdukLaris = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();

        jRadioButtonMenuItem1.setSelected(true);
        jRadioButtonMenuItem1.setText("jRadioButtonMenuItem1");

        setBackground(new java.awt.Color(255, 255, 255));
        setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        pnProdukTerjual.setBackground(new java.awt.Color(153, 153, 255));
        pnProdukTerjual.setPreferredSize(new java.awt.Dimension(216, 96));

        jLabel1.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 14)); // NOI18N
        jLabel1.setText("Produk Terjual");

        lbProdukTerjual.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 36)); // NOI18N
        lbProdukTerjual.setText("0");

        btnShowProdukTerjual.setBackground(new java.awt.Color(51, 51, 255));
        btnShowProdukTerjual.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        btnShowProdukTerjual.setForeground(new java.awt.Color(255, 255, 255));
        btnShowProdukTerjual.setText("detail");
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
                    .addComponent(lbProdukTerjual, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addComponent(btnShowProdukTerjual))
                .addContainerGap(81, Short.MAX_VALUE))
        );
        pnProdukTerjualLayout.setVerticalGroup(
            pnProdukTerjualLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnProdukTerjualLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lbProdukTerjual, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnShowProdukTerjual)
                .addContainerGap())
        );

        pnProdukTerlaris.setBackground(new java.awt.Color(153, 153, 255));

        jLabel4.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 14)); // NOI18N
        jLabel4.setText("Produk Terlaris");

        lbProdukTerlaris.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 24)); // NOI18N
        lbProdukTerlaris.setText("Nama produk");

        btnShowProdukTerlaris.setBackground(new java.awt.Color(51, 51, 255));
        btnShowProdukTerlaris.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        btnShowProdukTerlaris.setForeground(new java.awt.Color(255, 255, 255));
        btnShowProdukTerlaris.setText("detail");
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
                    .addComponent(jLabel4)
                    .addComponent(btnShowProdukTerlaris)
                    .addComponent(lbProdukTerlaris, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnProdukTerlarisLayout.setVerticalGroup(
            pnProdukTerlarisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnProdukTerlarisLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbProdukTerlaris, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnShowProdukTerlaris)
                .addContainerGap())
        );

        pnStokProduk.setBackground(new java.awt.Color(153, 153, 255));
        pnStokProduk.setPreferredSize(new java.awt.Dimension(216, 96));

        jLabel6.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 14)); // NOI18N
        jLabel6.setText("Stok Produk");

        lbStok.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 36)); // NOI18N
        lbStok.setText("0");

        btnShowStok.setBackground(new java.awt.Color(51, 51, 255));
        btnShowStok.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        btnShowStok.setForeground(new java.awt.Color(255, 255, 255));
        btnShowStok.setText("detail");
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
                    .addComponent(jLabel6)
                    .addComponent(btnShowStok)
                    .addComponent(lbStok, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(80, Short.MAX_VALUE))
        );
        pnStokProdukLayout.setVerticalGroup(
            pnStokProdukLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnStokProdukLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lbStok, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnShowStok)
                .addContainerGap())
        );

        jLabel8.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 18)); // NOI18N
        jLabel8.setText("Grafik Penjualan Perbulan");

        pnGrafikPenjualan.setBackground(new java.awt.Color(255, 255, 255));
        pnGrafikPenjualan.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout pnGrafikPenjualanLayout = new javax.swing.GroupLayout(pnGrafikPenjualan);
        pnGrafikPenjualan.setLayout(pnGrafikPenjualanLayout);
        pnGrafikPenjualanLayout.setHorizontalGroup(
            pnGrafikPenjualanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 440, Short.MAX_VALUE)
        );
        pnGrafikPenjualanLayout.setVerticalGroup(
            pnGrafikPenjualanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 380, Short.MAX_VALUE)
        );

        pnKinerjaKasir.setBackground(new java.awt.Color(153, 153, 255));
        pnKinerjaKasir.setPreferredSize(new java.awt.Dimension(216, 96));

        jLabel7.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 14)); // NOI18N
        jLabel7.setText("Kinerja Kasir");

        btnShowKasir.setBackground(new java.awt.Color(51, 51, 255));
        btnShowKasir.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        btnShowKasir.setForeground(new java.awt.Color(255, 255, 255));
        btnShowKasir.setText("detail");
        btnShowKasir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnShowKasirActionPerformed(evt);
            }
        });

        lbNamaKasir.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 24)); // NOI18N
        lbNamaKasir.setText("Nama Kasir");

        javax.swing.GroupLayout pnKinerjaKasirLayout = new javax.swing.GroupLayout(pnKinerjaKasir);
        pnKinerjaKasir.setLayout(pnKinerjaKasirLayout);
        pnKinerjaKasirLayout.setHorizontalGroup(
            pnKinerjaKasirLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnKinerjaKasirLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnKinerjaKasirLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7)
                    .addComponent(btnShowKasir)
                    .addComponent(lbNamaKasir, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnKinerjaKasirLayout.setVerticalGroup(
            pnKinerjaKasirLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnKinerjaKasirLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbNamaKasir, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnShowKasir)
                .addContainerGap())
        );

        pnProdukLaris.setBackground(new java.awt.Color(255, 255, 255));
        pnProdukLaris.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout pnProdukLarisLayout = new javax.swing.GroupLayout(pnProdukLaris);
        pnProdukLaris.setLayout(pnProdukLarisLayout);
        pnProdukLarisLayout.setHorizontalGroup(
            pnProdukLarisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        pnProdukLarisLayout.setVerticalGroup(
            pnProdukLarisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jLabel10.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 18)); // NOI18N
        jLabel10.setText("Grafik Produk Terlaris");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(pnGrafikPenjualan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(pnProdukTerlaris, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(pnProdukTerjual, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(pnProdukLaris, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel10)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(pnStokProduk, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(pnKinerjaKasir, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(pnStokProduk, javax.swing.GroupLayout.DEFAULT_SIZE, 112, Short.MAX_VALUE)
                    .addComponent(pnProdukTerlaris, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnProdukTerjual, javax.swing.GroupLayout.DEFAULT_SIZE, 112, Short.MAX_VALUE)
                    .addComponent(pnKinerjaKasir, javax.swing.GroupLayout.DEFAULT_SIZE, 112, Short.MAX_VALUE))
                .addGap(24, 24, 24)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(jLabel10))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnGrafikPenjualan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnProdukLaris, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                        .addGap(0, 738, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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

    private void btnShowKasirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnShowKasirActionPerformed
        formKinerjaKasir kasir = new formKinerjaKasir();
        kasir.setVisible(true);
    }//GEN-LAST:event_btnShowKasirActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnShowKasir;
    private javax.swing.JButton btnShowProdukTerjual;
    private javax.swing.JButton btnShowProdukTerlaris;
    private javax.swing.JButton btnShowStok;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JRadioButtonMenuItem jRadioButtonMenuItem1;
    private javax.swing.JLabel lbNamaKasir;
    private javax.swing.JLabel lbProdukTerjual;
    private javax.swing.JLabel lbProdukTerlaris;
    private javax.swing.JLabel lbStok;
    private javax.swing.JPanel pnGrafikPenjualan;
    private javax.swing.JPanel pnKinerjaKasir;
    private javax.swing.JPanel pnProdukLaris;
    private javax.swing.JPanel pnProdukTerjual;
    private javax.swing.JPanel pnProdukTerlaris;
    private javax.swing.JPanel pnStokProduk;
    // End of variables declaration//GEN-END:variables
}
