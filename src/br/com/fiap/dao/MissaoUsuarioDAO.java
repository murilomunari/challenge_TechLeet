package br.com.fiap.dao;

import br.com.fiap.bean.MissaoUsuario;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class MissaoUsuarioDAO {

    private Connection con;

    public MissaoUsuarioDAO(Connection con) {
        this.con = con;
    }

    public Connection getConnection() {
        return con;
    }

    public ArrayList<MissaoUsuario> ListarMissaoUsuario() {
        String sql = "select * from Missoes_Usuario order by id_missao_usuario";
        ArrayList<MissaoUsuario> listaMissaoUsuario = new ArrayList<>();
        try (PreparedStatement ps = getConnection().prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            if (rs != null) {
                while (rs.next()) {
                    MissaoUsuario missaoUsuario = new MissaoUsuario();
                    missaoUsuario.setId(rs.getInt(1));
                    missaoUsuario.setStatus(rs.getString(2));

                    Date dataRealizacao = rs.getDate(3);
                    missaoUsuario.setDataRealizacao(dataRealizacao != null
                            ? dataRealizacao.toLocalDate() : null);

                    missaoUsuario.setIdUsuario(rs.getInt(4));
                    missaoUsuario.setIdMissao(rs.getInt(5));

                    Date dataInicio = rs.getDate(6);
                    missaoUsuario.setDataInicio(dataInicio != null ? dataInicio.toLocalDate() : null);

                    Date dataFim = rs.getDate(7);
                    missaoUsuario.setDataFim(dataFim != null ? dataFim.toLocalDate() : null);

                    listaMissaoUsuario.add(missaoUsuario);
                }
                return listaMissaoUsuario;
            } else {
                return null;
            }
        } catch (SQLException e) {
            System.out.println("Erro de SQL: " + e.getMessage());
            return null;
        }
    }

    public String InserirMissaoUsuario(MissaoUsuario missaoUsuario) {
        String sql = "insert into Missoes_Usuario(id_missao_usuario, status, data_realizacao, "
                + "id_usuario, id_missao, data_inicio, data_fim) values (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setInt(1, missaoUsuario.getId());
            ps.setString(2, missaoUsuario.getStatus());
            ps.setDate(3, missaoUsuario.getDataRealizacao() != null
                    ? Date.valueOf(missaoUsuario.getDataRealizacao()) : null);
            ps.setInt(4, missaoUsuario.getIdUsuario());
            ps.setInt(5, missaoUsuario.getIdMissao());
            ps.setDate(6, missaoUsuario.getDataInicio() != null
                    ? Date.valueOf(missaoUsuario.getDataInicio()) : null);
            ps.setDate(7, missaoUsuario.getDataFim() != null
                    ? Date.valueOf(missaoUsuario.getDataFim()) : null);
            if (ps.executeUpdate() > 0) {
                return "Missao do usuario inserida com sucesso!";
            } else {
                return "Erro ao inserir missao do usuario!";
            }
        } catch (SQLException e) {
            return "Erro ao inserir missao do usuario!";
        }
    }

    public String AlterarMissaoUsuario(MissaoUsuario missaoUsuario) {
        String sql = "update Missoes_Usuario set status=?, data_realizacao=?, id_usuario=?, "
                + "id_missao=?, data_inicio=?, data_fim=? where id_missao_usuario=?";
        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setString(1, missaoUsuario.getStatus());
            ps.setDate(2, missaoUsuario.getDataRealizacao() != null
                    ? Date.valueOf(missaoUsuario.getDataRealizacao()) : null);
            ps.setInt(3, missaoUsuario.getIdUsuario());
            ps.setInt(4, missaoUsuario.getIdMissao());
            ps.setDate(5, missaoUsuario.getDataInicio() != null
                    ? Date.valueOf(missaoUsuario.getDataInicio()) : null);
            ps.setDate(6, missaoUsuario.getDataFim() != null
                    ? Date.valueOf(missaoUsuario.getDataFim()) : null);
            ps.setInt(7, missaoUsuario.getId());
            if (ps.executeUpdate() > 0) {
                return "Missao do usuario alterada com sucesso!";
            } else {
                return "Erro ao alterar missao do usuario!";
            }
        } catch (SQLException e) {
            return "Erro ao alterar missao do usuario!";
        }
    }

    public String DeletarMissaoUsuario(MissaoUsuario missaoUsuario) {
        String sql = "delete from Missoes_Usuario where id_missao_usuario=?";
        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setInt(1, missaoUsuario.getId());
            if (ps.executeUpdate() > 0) {
                return "Missao do usuario deletada com sucesso!";
            } else {
                return "Erro ao deletar missao do usuario!";
            }
        } catch (SQLException e) {
            return "Erro ao deletar missao do usuario!";
        }
    }
}
