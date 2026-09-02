package br.com.fiap.dao;

import br.com.fiap.dto.Missao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class MissaoDAO {

    private Connection con;

    public MissaoDAO(Connection con) {
        this.con = con;
    }

    public Connection getConnection() {
        return con;
    }

    public ArrayList<Missao> ListaMissao() {
        String sql = "select * from Missoes order by id_missao";
        ArrayList<Missao> listaMissao = new ArrayList<>();
        try (PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            if (rs != null) {
                while (rs.next()) {
                    Missao missao = new Missao();
                    missao.setId(rs.getInt(1));
                    missao.setTitulo(rs.getString(2));
                    missao.setDescricao(rs.getString(3));
                    missao.setPontos(rs.getInt(4));
                    missao.setTipo(rs.getString(5));
                    listaMissao.add(missao);
                }
                return listaMissao;
            } else {
                return null;
            }
        } catch (SQLException e) {
            System.out.println("Erro de SQL: " + e.getMessage());
            return null;
        }
    }

    public String InserirMissao(Missao missao) {
        String sql = "insert into Missoes(id_missao, titulo, descricao, pontos, tipo) "
                + "values (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setInt(1, missao.getId());
            ps.setString(2, missao.getTitulo());
            ps.setString(3, missao.getDescricao());
            ps.setInt(4, missao.getPontos());
            ps.setString(5, missao.getTipo());
            if (ps.executeUpdate() > 0) {
                return "Missao inserida com sucesso!";
            } else {
                return "Erro ao inserir missao!";
            }
        } catch (SQLException e) {
            return "Erro ao inserir missao!";
        }
    }

    public String AlterarMissao(Missao missao) {
        String sql = "update Missoes set titulo=?, descricao=?, pontos=?, tipo=? "
                + "where id_missao=?";
        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setString(1, missao.getTitulo());
            ps.setString(2, missao.getDescricao());
            ps.setInt(3, missao.getPontos());
            ps.setString(4, missao.getTipo());
            ps.setInt(5, missao.getId());
            if (ps.executeUpdate() > 0) {
                return "Missao alterada com sucesso!";
            } else {
                return "Erro ao alterar missao!";
            }
        } catch (SQLException e) {
            return "Erro ao alterar missao!";
        }
    }

    public String DeletarMissao(Missao missao) {
        String sql = "delete from Missoes where id_missao=?";
        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setInt(1, missao.getId());
            if (ps.executeUpdate() > 0) {
                return "Missao deletada com sucesso!";
            } else {
                return "Erro ao deletar missao!";
            }
        } catch (SQLException e) {
            return "Erro ao deletar missao!";
        }
    }
}
