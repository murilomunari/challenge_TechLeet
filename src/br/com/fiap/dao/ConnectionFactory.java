package br.com.fiap.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {
    public static Connection getConnection(){
        Connection con = null;
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            String url = "jdbc:oracle:thin:@oracle.fiap.com.br:1521:ORCL";
            final String user = "rm569602";
            final String password = "200101";
            con = DriverManager.getConnection(url, user, password);
            System.out.println("Conectado com sucesso!");
        } catch (ClassNotFoundException e) {
            System.out.println("Erro: A classe de conexão não foi encontrada!\n" + e.getMessage());
        } catch (SQLException e) {
            System.out.println("Erro de sql!\n" + e.getMessage());
        } catch (Exception e) {
            System.out.println("Erro: \n" + e.getMessage());
        }
        return con;
    }
    public static void closeConnection(Connection con){
        try {
            con.close();
            System.out.println("Conectado com sucesso!");
        } catch (SQLException e) {
            System.out.println("Erro: \n" + e.getMessage());
        } catch (Exception e) {
            System.out.println("Erro: \n" + e.getMessage());
        }
    }
}