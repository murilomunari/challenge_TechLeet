package br.com.fiap.dao;

import br.com.fiap.bean.Inventario;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class InventarioDAO {

    private Connection con;

    public InventarioDAO(Connection con) {
        this.con = con;
    }

    public Connection getConnection() {
        return con;
    }

    public ArrayList<Inventario> ListarInventario() {
        String sql = "select * from Inventario order by id_inventario";
        ArrayList<Inventario> inventarios = new ArrayList<>();

        try (PreparedStatement ps = getConnection().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs != null) {
                while (rs.next()) {
                    Inventario inventario = new Inventario();
                    inventario.setId(rs.getInt(1));
                    inventario.setIdUsuario(rs.getInt(2));
                    inventario.setIdItem(rs.getInt(3));
                    inventario.setOrigem(rs.getString(4));

                    Date dataAquisicao = rs.getDate(5);
                    inventario.setDataAquisicao(dataAquisicao != null
                            ? dataAquisicao.toLocalDate() : null);

                    inventarios.add(inventario);
                }
                return inventarios;
            } else {
                return null;
            }
        } catch (SQLException e) {
            System.out.println("Erro de SQL: " + e.getMessage());
            return null;
        }
    }

    public String InserirInventario(Inventario inventario) {
        String sql = "insert into Inventario(id_inventario, id_usuario, id_item, origem, "
                + "data_aquisicao) values (?, ?, ?, ?, ?)";

        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setInt(1, inventario.getId());
            ps.setInt(2, inventario.getIdUsuario());
            ps.setInt(3, inventario.getIdItem());
            ps.setString(4, inventario.getOrigem());
            ps.setDate(5, inventario.getDataAquisicao() != null
                    ? Date.valueOf(inventario.getDataAquisicao()) : null);

            if (ps.executeUpdate() > 0) {
                return "Inventario inserido com sucesso!";
            } else {
                return "Erro ao inserir inventario!";
            }
        } catch (SQLException e) {
            return "Erro ao inserir inventario!";
        }
    }

    public String AlterarInventario(Inventario inventario) {
        String sql = "update Inventario set id_usuario=?, id_item=?, origem=?, data_aquisicao=? "
                + "where id_inventario=?";

        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setInt(1, inventario.getIdUsuario());
            ps.setInt(2, inventario.getIdItem());
            ps.setString(3, inventario.getOrigem());
            ps.setDate(4, inventario.getDataAquisicao() != null
                    ? Date.valueOf(inventario.getDataAquisicao()) : null);
            ps.setInt(5, inventario.getId());

            if (ps.executeUpdate() > 0) {
                return "Inventario alterado com sucesso!";
            } else {
                return "Erro ao alterar inventario!";
            }
        } catch (SQLException e) {
            return "Erro ao alterar inventario!";
        }
    }

    public String DeletarInventario(Inventario inventario) {
        String sql = "delete from Inventario where id_inventario=?";

        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setInt(1, inventario.getId());
            if (ps.executeUpdate() > 0) {
                return "Inventario deletado com sucesso!";
            } else {
                return "Erro ao deletar inventario!";
            }
        } catch (SQLException e) {
            return "Erro ao deletar inventario!";
        }
    }

}
