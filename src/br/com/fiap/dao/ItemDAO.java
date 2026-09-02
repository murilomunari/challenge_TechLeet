package br.com.fiap.dao;

import br.com.fiap.dto.Item;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ItemDAO {

    private Connection con;

    public ItemDAO(Connection con) {
        this.con = con;
    }

    public Connection getConnection() {
        return con;
    }

    public ArrayList<Item> ListarItem() {
        String sql = "select * from Itens order by id_item";
        ArrayList<Item> items = new ArrayList<>();

        try (PreparedStatement ps = getConnection().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Item item = new Item();
                item.setId(rs.getInt("id_item"));
                item.setNome(rs.getString("nome"));
                item.setModelo(rs.getString("modelo_item"));
                item.setValorPontos(rs.getInt("valor_pontos"));
                item.setTipo(rs.getString("tipo"));
                items.add(item);
            }
            return items;
        } catch (SQLException e) {
            System.out.println("Erro de SQL: " + e.getMessage());
            return null;
        }
    }

    public String InserirItem(Item item) {
        String sql = "insert into Itens(id_item, nome, modelo_item, valor_pontos, tipo) "
                + "values (?, ?, ?, ?, ?)";

        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setInt(1, item.getId());
            ps.setString(2, item.getNome());
            ps.setString(3, item.getModelo());
            ps.setInt(4, item.getValorPontos());
            ps.setString(5, item.getTipo());

            if (ps.executeUpdate() > 0) {
                return "Item inserido com sucesso!";
            }
            return "Erro ao inserir item!";
        } catch (SQLException e) {
            return "Erro ao inserir item!";
        }
    }

    public String AlterarItem(Item item) {
        String sql = "update Itens set nome=?, modelo_item=?, valor_pontos=?, tipo=? "
                + "where id_item=?";

        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setString(1, item.getNome());
            ps.setString(2, item.getModelo());
            ps.setInt(3, item.getValorPontos());
            ps.setString(4, item.getTipo());
            ps.setInt(5, item.getId());

            if (ps.executeUpdate() > 0) {
                return "Item alterado com sucesso!";
            }
            return "Item nao encontrado!";
        } catch (SQLException e) {
            return "Erro ao alterar item!";
        }
    }

    public String DeletarItem(Item item) {
        String sql = "delete from Itens where id_item=?";

        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setInt(1, item.getId());
            if (ps.executeUpdate() > 0) {
                return "Item deletado com sucesso!";
            }
            return "Item nao encontrado!";
        } catch (SQLException e) {
            return "Erro ao deletar item!";
        }
    }
}
