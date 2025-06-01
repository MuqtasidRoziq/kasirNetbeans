/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package konektor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class koneksi {
<<<<<<< HEAD

    private static final String URL = "jdbc:mysql://localhost:3306/db_crepes1"; // Ganti dengan URL database Anda
=======
    
    private static final String URL = "jdbc:mysql://localhost:3306/db_crepes"; // Ganti dengan URL database Anda
>>>>>>> 3657f06d6cccc3380163847faa05e890966a32b6
    private static final String USERNAME = "root"; // Ganti dengan username database Anda
    private static final String PASSWORD = ""; // Ganti dengan password database Anda

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            System.out.println("Gagal koneksi ke database: " + e.getMessage());
            return null;
        }
    }

    public static void main(String[] args) {
        Connection conn = koneksi.getConnection();
        if (conn != null) {
            System.out.println("Koneksi berhasil!");
        } else {
            System.out.println("Koneksi gagal!");
        }
    }

}
