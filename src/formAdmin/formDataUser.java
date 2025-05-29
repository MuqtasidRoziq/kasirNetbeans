package formAdmin;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import konektor.koneksi;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import loging.loging.ActivityLogger;

public class formDataUser extends javax.swing.JPanel {

    private final String username;
    
    public formDataUser(String nama) {
        initComponents();
        this.username =nama;
        loadDataUser();
        
//      Menonaktifkan tombol Edit dan Delete saat pertama kali dibuka
        btnEdit.setEnabled(false);
        btnHapus.setEnabled(false);

//      Menambahkan ListSelectionListener ke tabel
        tabUSer.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent event) {
                // Cek jika ada baris yang dipilih
                if (!event.getValueIsAdjusting() && tabUSer.getSelectedRow() != -1) {
                    btnEdit.setEnabled(true);
                    btnHapus.setEnabled(true);
                } else {
                    btnEdit.setEnabled(false);
                    btnHapus.setEnabled(false);
                }
            }
        });
    }

// Load Data User //
    private void loadDataUser() {
        DefaultTableModel model = (DefaultTableModel) tabUSer.getModel();
        model.setRowCount(0);

        try {
            Connection con = koneksi.getConnection();
            Statement st = con.createStatement();
            String query = "SELECT id_user, username, password, role FROM user"; 
            ResultSet rs = st.executeQuery(query);

            while (rs.next()) {
                String idUser = rs.getString("id_user");
                String username = rs.getString("username");
                String password = rs.getString("password");
                String role = rs.getString("role");
                
                // Tambahkan data ke model tabel
                model.addRow(new Object[]{idUser, username, password, role});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }    
//  Load Data User end //   
    
//  Fungsi edit data user //
    private void editDataUser(){
        int selectedRow = tabUSer.getSelectedRow();
        if (selectedRow != -1) {
            String namaAdmin = this.username;
            String idUser = tabUSer.getValueAt(selectedRow, 0).toString();
            String usernameLama = tabUSer.getValueAt(selectedRow, 1).toString();
            String password = tabUSer.getValueAt(selectedRow, 2).toString();
            String role = tabUSer.getValueAt(selectedRow, 3).toString();

            // Buka form edit dengan data pengguna yang dipilih
            formEditUser editUserForm = new formEditUser(namaAdmin, idUser, usernameLama, password, role);
            editUserForm.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                    // Muat ulang data tabel setelah form edit ditutup
                    loadDataUser();
                }
            });
            editUserForm.setVisible(true);
            
        }
    }
//  Fungsi edit data user end //    
    
//  hapus data user //    
    private void hapusDataUser(){
        int selectedRow = tabUSer.getSelectedRow();
        if (selectedRow != -1) {
            String idUser = tabUSer.getValueAt(selectedRow, 0).toString();
            String namaUser = tabUSer.getValueAt(selectedRow, 1).toString();
            // Konfirmasi dan hapus data dari database berdasarkan ID
            int confirm = JOptionPane.showConfirmDialog(this, "Apakah Anda yakin ingin menghapus user ini?", "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    Connection con = koneksi.getConnection();
                    Statement st = con.createStatement();
                    String deleteQuery = "DELETE FROM user WHERE id_user = '" + idUser + "'";
                    st.executeUpdate(deleteQuery);
                    ActivityLogger.logDeleteUser(this.username, namaUser);
                    loadDataUser();
                    JOptionPane.showMessageDialog(this, "Data user berhasil dihapus.");
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, "Terjadi kesalahan saat menghapus data.");
                    ActivityLogger.logError(this.username + "gagal menghapus " + namaUser);
                }
            }
        }
    }
//  ungsi hapus data user end //   
    
//  Fungsi seacrh user //
    private void searchUser(){
        String keyword = jTextField1.getText().trim();
        DefaultTableModel model = (DefaultTableModel) tabUSer.getModel();

        if (keyword.isEmpty()) {
            // Jika keyword kosong, tampilkan pesan dan muat ulang data asli
            loadDataUser();
            JOptionPane.showMessageDialog(this, "Kolom pencarian harus diisi!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Reset tabel sebelum menampilkan hasil pencarian
        model.setRowCount(0);

        try {
            Connection con = koneksi.getConnection();
            Statement st = con.createStatement();

            // Query untuk mencari data user berdasarkan ID, Nama, atau Email yang cocok dengan kata kunci
            String query = "SELECT id_user, nama_user, email_user, role, username_user, password_user " +
                           "FROM user WHERE id_user LIKE '%" + keyword + "%' " +
                           "OR nama_user LIKE '%" + keyword + "%' " +
                           "OR email_user LIKE '%" + keyword + "%'";

            ResultSet rs = st.executeQuery(query);

            // Jika data ditemukan, tambahkan ke model tabel
            while (rs.next()) {
                String idUser = rs.getString("id_user");
                String nama = rs.getString("nama_user");
                String role = rs.getString("role");
                String email = rs.getString("email_user");
                String username = rs.getString("username_user");
                String password = rs.getString("password_user");
                model.addRow(new Object[]{idUser, nama, role, email, username, password});
                ActivityLogger.logSearching(this.username, nama);
            }

            // Pesan jika tidak ada data ditemukan
            if (model.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this, "Data user tidak ditemukan.", "Pencarian", JOptionPane.INFORMATION_MESSAGE);
                ActivityLogger.logError(this.username + "gagal melakukan pencarian");
                loadDataUser(); // Tampilkan kembali data asli jika tidak ada hasil
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Terjadi kesalahan saat mencari data.");
            ActivityLogger.logError(this.username + "gagal melakukan pencarian");
        }
    }
//  search user//    
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lbDataUser = new javax.swing.JLabel();
        bgDataUSer = new javax.swing.JPanel();
        btnTambah = new javax.swing.JButton();
        btnEdit = new javax.swing.JButton();
        btnHapus = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();
        btnSearch = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabUSer = new javax.swing.JTable();

        setBackground(new java.awt.Color(255, 255, 255));

        lbDataUser.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 24)); // NOI18N
        lbDataUser.setText("Data User");

        bgDataUSer.setBackground(new java.awt.Color(255, 255, 255));
        bgDataUSer.setBorder(javax.swing.BorderFactory.createEtchedBorder());

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

        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });

        btnSearch.setBackground(new java.awt.Color(51, 51, 255));
        btnSearch.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/cari.png"))); // NOI18N
        btnSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchActionPerformed(evt);
            }
        });

        tabUSer.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Id User", "Username", "Password", "Role"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tabUSer.setGridColor(new java.awt.Color(0, 0, 0));
        tabUSer.setSelectionBackground(new java.awt.Color(51, 51, 255));
        tabUSer.setSelectionForeground(new java.awt.Color(255, 255, 255));
        tabUSer.setShowGrid(false);
        jScrollPane1.setViewportView(tabUSer);

        javax.swing.GroupLayout bgDataUSerLayout = new javax.swing.GroupLayout(bgDataUSer);
        bgDataUSer.setLayout(bgDataUSerLayout);
        bgDataUSerLayout.setHorizontalGroup(
            bgDataUSerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(bgDataUSerLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(bgDataUSerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(bgDataUSerLayout.createSequentialGroup()
                        .addComponent(jScrollPane1)
                        .addContainerGap())
                    .addGroup(bgDataUSerLayout.createSequentialGroup()
                        .addComponent(btnTambah)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnEdit)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnHapus)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 165, Short.MAX_VALUE)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnSearch)
                        .addGap(37, 37, 37))))
        );
        bgDataUSerLayout.setVerticalGroup(
            bgDataUSerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(bgDataUSerLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(bgDataUSerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(bgDataUSerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnTambah, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnHapus, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 368, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(bgDataUSer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lbDataUser)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lbDataUser)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bgDataUSer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnTambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTambahActionPerformed
        formTambahUser tambahuser = new formTambahUser(username);
        tambahuser.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                // Muat ulang data tabel setelah form edit ditutup
                loadDataUser();
                }
            });        
        tambahuser.setVisible(true);
    }//GEN-LAST:event_btnTambahActionPerformed

    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchActionPerformed
        searchUser();
    }//GEN-LAST:event_btnSearchActionPerformed

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        editDataUser();
        loadDataUser();
    }//GEN-LAST:event_btnEditActionPerformed

    private void btnHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHapusActionPerformed
        hapusDataUser();
    }//GEN-LAST:event_btnHapusActionPerformed

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        searchUser();
        btnSearch.requestFocus();
    }//GEN-LAST:event_jTextField1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel bgDataUSer;
    private javax.swing.JButton btnEdit;
    private javax.swing.JButton btnHapus;
    private javax.swing.JButton btnSearch;
    private javax.swing.JButton btnTambah;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JLabel lbDataUser;
    private javax.swing.JTable tabUSer;
    // End of variables declaration//GEN-END:variables
}
