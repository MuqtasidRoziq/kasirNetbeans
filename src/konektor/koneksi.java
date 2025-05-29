/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package konektor;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


/**
 *
 * @author muqta
 */
public class koneksi {
    
    private static final String URL = "jdbc:mysql://localhost:3306/db_crepes"; // Ganti dengan URL database Anda
    private static final String USERNAME = "root"; // Ganti dengan username database Anda
    private static final String PASSWORD = ""; // Ganti dengan password database Anda

    public static Connection getConnection() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
             System.out.println("Gagal koneksi ke database: " + e.getMessage());;
        }
        return conn;
    }
}

