package formKasir;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.JOptionPane;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.text.SimpleDateFormat;
import java.util.Date;
import konektor.koneksi;


public class formHalamanKasir extends javax.swing.JPanel {
    
    public formHalamanKasir(String userId) {
        initComponents();
        getNamaKasir(userId);
        idKasir.setEditable(false);
        namaKasir.setEditable(false);
        inputNamaProduk.setEditable(false);
        inputHarga.setEditable(false);
        inputSubTotal.setEditable(false);
        inputIdTransaksi.setEditable(false);
        inputTanggal.setEditable(false);
        inputKembalian.setEditable(false);
        inputTotal.setEditable(false);
        inputIdProduk.requestFocus();
        btnHapus.setEnabled(false);
        
        tabProduk.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent event) {
                if (!event.getValueIsAdjusting() && tabProduk.getSelectedRow() != -1) {
                    btnHapus.setEnabled(true);
                } else {
                    btnHapus.setEnabled(false);
                }
            }
        });
    }
    
//  Set Nama Kasir //
    private void getNamaKasir(String userId){
        String query = "SELECT id_user, nama_user FROM user WHERE id_user=?";        
         try {
            Connection conn = koneksi.getConnection();
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setString(1, userId);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                idKasir.setText(rs.getString("id_user"));
                namaKasir.setText(rs.getString("nama_user"));
            } else {
                JOptionPane.showMessageDialog(null, "Data user tidak ditemukan!");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }
//  Set Nama Kasir END //
    
//  Function Search Product Tab //
    private void searchProduct(){
        
        String keyword = inputIdProduk.getText().trim();
        
        if (keyword.isEmpty()) {
            inputNamaProduk.setText("");
            inputHarga.setText("");
            JOptionPane.showMessageDialog(this, "Kolom pencarian harus diisi!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try{
            String sql = "SELECT * FROM produk WHERE id_produk ='" + inputIdProduk.getText() + "'";
            Connection conn = koneksi.getConnection();
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            
            
            if(rs.next()){
                inputIdProduk.setText(rs.getString("id_produk"));
                inputNamaProduk.setText(rs.getString("nama_produk"));
                inputHarga.setText(rs.getString("harga_jual"));
                inputJumlah.requestFocus();
            } else {
                JOptionPane.showMessageDialog(this, "Produk dengan ID tersebut tidak ditemukan.", "Informasi", JOptionPane.INFORMATION_MESSAGE);
                inputNamaProduk.setText("");
                inputHarga.setText("");
                inputIdProduk.requestFocus();
            }
            
        }catch(Exception e){
            
        }
    }
//  Function Search Product Tab End//    
    
// Add Product To Cart //
    private void addProduct() {
        DefaultTableModel tbl = (DefaultTableModel) tabProduk.getModel();
        SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat formatDateTransaksi = new SimpleDateFormat("yyMMyyyss");
        String dateTransaksi = formatDateTransaksi.format(new Date());
        String date = formatDate.format(new Date());      
        
        if (inputJumlah.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Input jumlah tidak boleh kosong!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            inputJumlah.requestFocus();
        } else {
            try {
                double harga = Double.parseDouble(inputHarga.getText());
                double jumlah = Double.parseDouble(inputJumlah.getText());
                double subTotal = harga * jumlah;

                tbl.addRow(new Object[]{
                    inputIdProduk.getText(),
                    inputNamaProduk.getText(),
                    inputHarga.getText(),
                    inputJumlah.getText(),
                    String.format("%.2f", subTotal)
                });

                double total = inputTotal.getText().isEmpty() ? 0 : Double.parseDouble(inputTotal.getText());
                total += subTotal;
                inputSubTotal.setText(String.format("%.2f", subTotal));
                inputTotal.setText(String.format("%.2f", total));
                inputIdTransaksi.setText("TRX" + dateTransaksi);
                inputTanggal.setText(date);
                inputIdProduk.setText("");
                inputNamaProduk.setText("");
                inputHarga.setText("");
                inputJumlah.setText("");
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Input harga atau jumlah tidak valid!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
//  Add Product To Cart End

//  Delete Row in TabProduk //
    private void deleteRow() {
        DefaultTableModel tbl = (DefaultTableModel) tabProduk.getModel();
        int selectedRow = tabProduk.getSelectedRow();

        if (selectedRow != -1) {
            tbl.removeRow(selectedRow);
            double total = 0;
            for (int i = 0; i < tbl.getRowCount(); i++) {
                Object subTotalObj = tbl.getValueAt(i , 4);
                if (subTotalObj != null) {
                    try {
                        double subTotal = Double.parseDouble(subTotalObj.toString());
                        total += subTotal;
                    } catch (NumberFormatException e) {
                        System.err.println("Error parsing Subtotal pada baris " + i + ": " + subTotalObj);
                    }
                }
            }
            inputTotal.setText(String.format("%.2f", total));
            
            if (tbl.getRowCount() == 0){
                inputIdTransaksi.setText("");
                inputIdTransaksi.repaint();
                inputTanggal.setText("");
                inputTanggal.repaint();
                inputBayar.setText("");
                inputBayar.repaint();
                inputKembalian.setText("");
                inputKembalian.repaint();
            }
            
        } else {
            JOptionPane.showMessageDialog(this, "Pilih baris yang ingin dihapus!");
        }
    }
//  Delete Row in TabProduk End //  
    
// fungsi hitung bayar dan kembali //    
    private void hitungKembalian() {
        try {
            double totalBelanja = Double.parseDouble(inputTotal.getText());
            double uangBayar = Double.parseDouble(inputBayar.getText());

            if (uangBayar < totalBelanja) {
                JOptionPane.showMessageDialog(this, 
                    "Uang yang dibayarkan tidak cukup! Total belanja adalah Rp" + totalBelanja,
                    "Peringatan", 
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            double kembalian = uangBayar - totalBelanja;
            inputKembalian.setText(String.format("%.2f", kembalian));
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, 
                "Masukkan angka yang valid untuk uang bayar dan total belanja!", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
// fungsi hitung bayar dan kembali end//

// add transaksi to databases //      
    private void processTransaction() {
        if (inputTotal.getText().isEmpty() || inputBayar.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Data transaksi tidak lengkap!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        DefaultTableModel tbl = (DefaultTableModel) tabProduk.getModel();

        if (tbl.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Keranjang belanja kosong!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Connection conn = null;

        try {
            conn = koneksi.getConnection();
            conn.setAutoCommit(false); // Mulai transaksi

            // Simpan ke tabel transaksi
            String sqlTransaksi = "INSERT INTO transaksi (id_transaksi , id_user, tanggal_transaksi, total_harga) VALUES (?, ?, ?, ?)";
            PreparedStatement psTransaksi = conn.prepareStatement(sqlTransaksi);
            psTransaksi.setString(1, inputIdTransaksi.getText());
            psTransaksi.setString(2, idKasir.getText());
            psTransaksi.setString(3, inputTanggal.getText());
            psTransaksi.setDouble(4, Double.parseDouble(inputTotal.getText()));
            psTransaksi.executeUpdate();

            // Simpan ke tabel detail_transaksi dan kurangi stok barang
            String sqlDetailTransaksi = "INSERT INTO detail_transaksi (id_transaksi, id_produk, jumlah, harga_produk, subtotal) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement psDetail = conn.prepareStatement(sqlDetailTransaksi);

            String sqlUpdateStok = "UPDATE produk SET stok = stok - ? WHERE id_produk = ?";
            PreparedStatement psUpdateStok = conn.prepareStatement(sqlUpdateStok);

            for (int i = 0; i < tbl.getRowCount(); i++) {
                String idProduk = tbl.getValueAt(i, 0).toString();
                int jumlah = Integer.parseInt(tbl.getValueAt(i, 3).toString());
                double hargaBarang = Double.parseDouble(tbl.getValueAt(i, 2).toString());

                // Simpan detail transaksi
                psDetail.setString(1, inputIdTransaksi.getText());
                psDetail.setString(2, idProduk);
                psDetail.setInt(3, jumlah);
                psDetail.setDouble(4, hargaBarang);
                psDetail.setDouble(5, Double.parseDouble(inputSubTotal.getText()));
                psDetail.addBatch();

                // Kurangi stok barang
                psUpdateStok.setInt(1, jumlah);
                psUpdateStok.setString(2, idProduk);
                psUpdateStok.addBatch();
            }

            psDetail.executeBatch();
            psUpdateStok.executeBatch();

            conn.commit(); // Commit transaksi

            JOptionPane.showMessageDialog(this, "Transaksi berhasil disimpan!", "Informasi", JOptionPane.INFORMATION_MESSAGE);

            // Reset semua input setelah transaksi berhasil
            tbl.setRowCount(0);
            inputIdTransaksi.setText("");
            inputTanggal.setText("");
            inputTotal.setText("");
            inputBayar.setText("");
            inputKembalian.setText("");
        } catch (SQLException e) {
            try {
                if (conn != null) {
                    conn.rollback(); // Rollback jika terjadi error
                }
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            JOptionPane.showMessageDialog(this, "Terjadi kesalahan saat menyimpan data transaksi!\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true); // Kembalikan ke mode default
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
// add transaksi to databases end //    
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lbDbKasir = new javax.swing.JLabel();
        bg1 = new javax.swing.JPanel();
        lbProduk = new javax.swing.JLabel();
        inputIdProduk = new javax.swing.JTextField();
        lbNamaProduk = new javax.swing.JLabel();
        inputNamaProduk = new javax.swing.JTextField();
        lbHarga = new javax.swing.JLabel();
        inputHarga = new javax.swing.JTextField();
        lbJumlah = new javax.swing.JLabel();
        inputJumlah = new javax.swing.JTextField();
        btnTambah = new javax.swing.JButton();
        btnSearch = new javax.swing.JButton();
        btnHapus = new javax.swing.JButton();
        inputSubTotal = new javax.swing.JTextField();
        lbJumlah1 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tabProduk = new javax.swing.JTable();
        bg2 = new javax.swing.JPanel();
        lbTotal = new javax.swing.JLabel();
        inputTotal = new javax.swing.JTextField();
        lbIdTransaksi = new javax.swing.JLabel();
        inputIdTransaksi = new javax.swing.JTextField();
        lbTanggal = new javax.swing.JLabel();
        lbBayar = new javax.swing.JLabel();
        inputBayar = new javax.swing.JTextField();
        lbKembalian = new javax.swing.JLabel();
        inputKembalian = new javax.swing.JTextField();
        btnBayar = new javax.swing.JButton();
        btnPrint = new javax.swing.JButton();
        inputTanggal = new javax.swing.JTextField();
        lbIdTransaksi1 = new javax.swing.JLabel();
        idKasir = new javax.swing.JTextField();
        namaKasir = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();

        setBackground(new java.awt.Color(255, 255, 255));

        lbDbKasir.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 24)); // NOI18N
        lbDbKasir.setText("Dashboard Kasir");

        bg1.setBackground(new java.awt.Color(255, 255, 255));
        bg1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        lbProduk.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        lbProduk.setText("Id Produk");

        inputIdProduk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inputIdProdukActionPerformed(evt);
            }
        });

        lbNamaProduk.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        lbNamaProduk.setText("Nama Produk");

        lbHarga.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        lbHarga.setText("Harga");

        inputHarga.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inputHargaActionPerformed(evt);
            }
        });

        lbJumlah.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        lbJumlah.setText("Jumlah");

        inputJumlah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inputJumlahActionPerformed(evt);
            }
        });

        btnTambah.setBackground(new java.awt.Color(51, 51, 255));
        btnTambah.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        btnTambah.setForeground(new java.awt.Color(255, 255, 255));
        btnTambah.setText("Tambah");
        btnTambah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTambahActionPerformed(evt);
            }
        });

        btnSearch.setBackground(new java.awt.Color(51, 51, 255));
        btnSearch.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        btnSearch.setForeground(new java.awt.Color(255, 255, 255));
        btnSearch.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/cari.png"))); // NOI18N
        btnSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchActionPerformed(evt);
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

        inputSubTotal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inputSubTotalActionPerformed(evt);
            }
        });

        lbJumlah1.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        lbJumlah1.setText("Total Harga");

        tabProduk.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Id Produk", "Nama Produk", "Harga", "Jumlah", "Sub Total"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(tabProduk);
        if (tabProduk.getColumnModel().getColumnCount() > 0) {
            tabProduk.getColumnModel().getColumn(0).setResizable(false);
            tabProduk.getColumnModel().getColumn(1).setResizable(false);
            tabProduk.getColumnModel().getColumn(2).setResizable(false);
            tabProduk.getColumnModel().getColumn(3).setResizable(false);
            tabProduk.getColumnModel().getColumn(4).setResizable(false);
        }

        javax.swing.GroupLayout bg1Layout = new javax.swing.GroupLayout(bg1);
        bg1.setLayout(bg1Layout);
        bg1Layout.setHorizontalGroup(
            bg1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(bg1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(bg1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2)
                    .addGroup(bg1Layout.createSequentialGroup()
                        .addGroup(bg1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(bg1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(bg1Layout.createSequentialGroup()
                                    .addComponent(inputIdProduk, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(btnSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(lbProduk)
                                .addComponent(lbNamaProduk)
                                .addComponent(lbHarga)
                                .addGroup(bg1Layout.createSequentialGroup()
                                    .addComponent(lbJumlah)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(lbJumlah1))
                                .addComponent(inputHarga, javax.swing.GroupLayout.DEFAULT_SIZE, 161, Short.MAX_VALUE)
                                .addComponent(inputNamaProduk))
                            .addGroup(bg1Layout.createSequentialGroup()
                                .addComponent(inputJumlah, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(inputSubTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 458, Short.MAX_VALUE))
                    .addGroup(bg1Layout.createSequentialGroup()
                        .addComponent(btnTambah)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnHapus, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        bg1Layout.setVerticalGroup(
            bg1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(bg1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lbProduk)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(bg1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(inputIdProduk, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbNamaProduk)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(inputNamaProduk, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbHarga)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(inputHarga, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(bg1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbJumlah)
                    .addComponent(lbJumlah1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(bg1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(inputJumlah, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(inputSubTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(bg1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnTambah, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnHapus, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 294, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(104, Short.MAX_VALUE))
        );

        bg2.setBackground(new java.awt.Color(153, 153, 255));
        bg2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        lbTotal.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 18)); // NOI18N
        lbTotal.setText("Total Harga");

        inputTotal.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 24)); // NOI18N
        inputTotal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inputTotalActionPerformed(evt);
            }
        });

        lbIdTransaksi.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        lbIdTransaksi.setText("Id Transaksi");

        inputIdTransaksi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inputIdTransaksiActionPerformed(evt);
            }
        });

        lbTanggal.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        lbTanggal.setText("Tanggal Pembelian");

        lbBayar.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        lbBayar.setText("Bayar");

        inputBayar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inputBayarActionPerformed(evt);
            }
        });

        lbKembalian.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        lbKembalian.setText("Kembalian");

        inputKembalian.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inputKembalianActionPerformed(evt);
            }
        });

        btnBayar.setBackground(new java.awt.Color(51, 51, 255));
        btnBayar.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        btnBayar.setForeground(new java.awt.Color(255, 255, 255));
        btnBayar.setText("Bayar");
        btnBayar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBayarActionPerformed(evt);
            }
        });

        btnPrint.setBackground(new java.awt.Color(51, 51, 255));
        btnPrint.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        btnPrint.setForeground(new java.awt.Color(255, 255, 255));
        btnPrint.setText("Print");

        lbIdTransaksi1.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        lbIdTransaksi1.setText("Id Kasir");

        idKasir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                idKasirActionPerformed(evt);
            }
        });

        namaKasir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                namaKasirActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        jLabel1.setText("Nama Kasir");

        javax.swing.GroupLayout bg2Layout = new javax.swing.GroupLayout(bg2);
        bg2.setLayout(bg2Layout);
        bg2Layout.setHorizontalGroup(
            bg2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(bg2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(bg2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(bg2Layout.createSequentialGroup()
                        .addGroup(bg2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(inputTotal)
                            .addGroup(bg2Layout.createSequentialGroup()
                                .addGroup(bg2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lbTotal)
                                    .addGroup(bg2Layout.createSequentialGroup()
                                        .addComponent(idKasir, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addGroup(bg2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(lbTanggal)
                                            .addComponent(namaKasir, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel1))))
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addContainerGap())
                    .addGroup(bg2Layout.createSequentialGroup()
                        .addGroup(bg2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(bg2Layout.createSequentialGroup()
                                .addComponent(inputBayar, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(inputKembalian))
                            .addGroup(bg2Layout.createSequentialGroup()
                                .addGroup(bg2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lbIdTransaksi1)
                                    .addComponent(lbIdTransaksi)
                                    .addGroup(bg2Layout.createSequentialGroup()
                                        .addGroup(bg2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(inputIdTransaksi, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(lbBayar))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addGroup(bg2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(lbKembalian)
                                            .addComponent(inputTanggal, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(bg2Layout.createSequentialGroup()
                                        .addComponent(btnBayar, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(btnPrint, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addGap(6, 6, 6))))
        );
        bg2Layout.setVerticalGroup(
            bg2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(bg2Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(bg2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(bg2Layout.createSequentialGroup()
                        .addGroup(bg2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(bg2Layout.createSequentialGroup()
                                .addGroup(bg2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(lbIdTransaksi1)
                                    .addComponent(jLabel1))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(bg2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(idKasir, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(namaKasir, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(bg2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(lbIdTransaksi)
                                    .addComponent(lbTanggal))
                                .addGap(35, 35, 35))
                            .addGroup(bg2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(inputIdTransaksi, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(inputTanggal, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(bg2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lbBayar)
                            .addComponent(lbKembalian))
                        .addGap(36, 36, 36))
                    .addGroup(bg2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(inputBayar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(inputKembalian, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(12, 12, 12)
                .addComponent(lbTotal)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(inputTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(bg2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnPrint, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBayar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lbDbKasir)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(bg1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bg2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lbDbKasir)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(bg2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(bg1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(6, 6, 6))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void inputJumlahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inputJumlahActionPerformed
        addProduct();
        inputIdProduk.requestFocus();
    }//GEN-LAST:event_inputJumlahActionPerformed
 
    private void btnTambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTambahActionPerformed
        addProduct();
        inputIdProduk.requestFocus();
    }//GEN-LAST:event_btnTambahActionPerformed

    private void btnHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHapusActionPerformed
        deleteRow();
    }//GEN-LAST:event_btnHapusActionPerformed

    private void inputBayarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inputBayarActionPerformed
        hitungKembalian();
    }//GEN-LAST:event_inputBayarActionPerformed

    private void inputHargaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inputHargaActionPerformed

    }//GEN-LAST:event_inputHargaActionPerformed

    private void inputKembalianActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inputKembalianActionPerformed
        
    }//GEN-LAST:event_inputKembalianActionPerformed

    private void inputTotalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inputTotalActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_inputTotalActionPerformed

    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchActionPerformed
        searchProduct();
        inputJumlah.requestFocus();
    }//GEN-LAST:event_btnSearchActionPerformed

    private void inputIdProdukActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inputIdProdukActionPerformed
        searchProduct();
    }//GEN-LAST:event_inputIdProdukActionPerformed

    private void inputIdTransaksiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inputIdTransaksiActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_inputIdTransaksiActionPerformed

    private void inputSubTotalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inputSubTotalActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_inputSubTotalActionPerformed

    private void btnBayarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBayarActionPerformed
        processTransaction();
    }//GEN-LAST:event_btnBayarActionPerformed

    private void idKasirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_idKasirActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_idKasirActionPerformed

    private void namaKasirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_namaKasirActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_namaKasirActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel bg1;
    private javax.swing.JPanel bg2;
    private javax.swing.JButton btnBayar;
    private javax.swing.JButton btnHapus;
    private javax.swing.JButton btnPrint;
    private javax.swing.JButton btnSearch;
    private javax.swing.JButton btnTambah;
    private javax.swing.JTextField idKasir;
    private javax.swing.JTextField inputBayar;
    private javax.swing.JTextField inputHarga;
    private javax.swing.JTextField inputIdProduk;
    private javax.swing.JTextField inputIdTransaksi;
    private javax.swing.JTextField inputJumlah;
    private javax.swing.JTextField inputKembalian;
    private javax.swing.JTextField inputNamaProduk;
    private javax.swing.JTextField inputSubTotal;
    private javax.swing.JTextField inputTanggal;
    private javax.swing.JTextField inputTotal;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lbBayar;
    private javax.swing.JLabel lbDbKasir;
    private javax.swing.JLabel lbHarga;
    private javax.swing.JLabel lbIdTransaksi;
    private javax.swing.JLabel lbIdTransaksi1;
    private javax.swing.JLabel lbJumlah;
    private javax.swing.JLabel lbJumlah1;
    private javax.swing.JLabel lbKembalian;
    private javax.swing.JLabel lbNamaProduk;
    private javax.swing.JLabel lbProduk;
    private javax.swing.JLabel lbTanggal;
    private javax.swing.JLabel lbTotal;
    private javax.swing.JTextField namaKasir;
    private javax.swing.JTable tabProduk;
    // End of variables declaration//GEN-END:variables

}
