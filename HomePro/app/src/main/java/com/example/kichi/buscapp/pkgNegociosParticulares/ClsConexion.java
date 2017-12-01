package com.example.kichi.buscapp.pkgNegociosParticulares;

import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by Kichi on 29/08/2017.
 */

public class ClsConexion {
    public static Connection ConnectionHelper() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection connection = null;
        String ConnectionURL = null;
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            //ConnectionURL = "jdbc:jtds:sqlserver://192.168.2.106;port=1433;databaseName=homepro;user=sa;password=123;";
            //ConnectionURL = "jdbc:jtds:sqlserver://192.168.1.4;port=1433;databaseName=homepro;user=sa;password=123;";
            ConnectionURL = "jdbc:jtds:sqlserver://homeproserver.database.windows.net;port=1433;databaseName=HomePro;user=kichitano;password=Cerebro123;";
            connection = DriverManager.getConnection(ConnectionURL);
        } catch (SQLException se) {
            Log.e("ERROR", se.getMessage());
        } catch (ClassNotFoundException e) {
            Log.e("ERROR", e.getMessage());
        } catch (Exception e) {
            Log.e("ERROR", e.getMessage());
        }
        return connection;
    }
    public static Connection getConnection(){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection connection = null;
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            //String con = "jdbc:jtds:sqlserver://192.168.2.106;port=1433;databaseName=homepro;user=sa;password=123;";
            //String con = "jdbc:jtds:sqlserver://192.168.1.4;port=1433;databaseName=homepro;user=sa;password=123;";
            String con = "jdbc:jtds:sqlserver://homeproserver.database.windows.net;port=1433;databaseName=HomePro;user=kichitano;password=Cerebro123;";
            connection = DriverManager.getConnection(con);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }
}
