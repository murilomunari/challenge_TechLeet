package br.com.fiap.dao;

import br.com.fiap.dto.Log;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class LogDAO {

    private Connection con;

    public LogDAO(Connection con) {
        this.con = con;
    }

    public Connection getConnection() {
        return con;
    }

    public ArrayList<Log> ListarLog() {
        String sql = "select * from LOGS order by id_log";
        ArrayList<Log> listaLog = new ArrayList<>();
        try (PreparedStatement ps = getConnection().prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            if (rs != null) {
                while (rs.next()) {
                    Log log = new Log();
                    log.setId(rs.getInt(1));
                    log.setAssunto(rs.getString(2));
                    log.setDescricao(rs.getString(3));

                    Date dataRegistro = rs.getDate(4);
                    log.setDataRegistro(dataRegistro != null ? dataRegistro.toLocalDate() : null);

                    log.setIdUsuario(rs.getInt(5));
                    log.setStatus(rs.getString(6));
                    listaLog.add(log);
                }
                return listaLog;
            } else {
                return null;
            }
        } catch (SQLException e) {
            System.out.println("Erro de SQL: " + e.getMessage());
            return null;
        }
    }

    public String InserirLog(Log log) {
        String sql = "insert into LOGS(id_log, assunto, descricao, data_registro, id_usuario, status) "
                + "values (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setInt(1, log.getId());
            ps.setString(2, log.getAssunto());
            ps.setString(3, log.getDescricao());
            ps.setDate(4, log.getDataRegistro() != null
                    ? Date.valueOf(log.getDataRegistro()) : null);
            ps.setInt(5, log.getIdUsuario());
            ps.setString(6, log.getStatus());
            if (ps.executeUpdate() > 0) {
                return "Log inserido com sucesso!";
            } else {
                return "Erro ao inserir log!";
            }
        } catch (SQLException e) {
            return "Erro ao inserir log!";
        }
    }

    public String AlterarLog(Log log) {
        String sql = "update LOGS set assunto=?, descricao=?, data_registro=?, id_usuario=?, "
                + "status=? where id_log=?";
        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setString(1, log.getAssunto());
            ps.setString(2, log.getDescricao());
            ps.setDate(3, log.getDataRegistro() != null
                    ? Date.valueOf(log.getDataRegistro()) : null);
            ps.setInt(4, log.getIdUsuario());
            ps.setString(5, log.getStatus());
            ps.setInt(6, log.getId());
            if (ps.executeUpdate() > 0) {
                return "Log alterado com sucesso!";
            } else {
                return "Erro ao alterar log!";
            }
        } catch (SQLException e) {
            return "Erro ao alterar log!";
        }
    }

    public String DeletarLog(Log log) {
        String sql = "delete from LOGS where id_log=?";
        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setInt(1, log.getId());
            if (ps.executeUpdate() > 0) {
                return "Log deletado com sucesso!";
            } else {
                return "Erro ao deletar log!";
            }
        } catch (SQLException e) {
            return "Erro ao deletar log!";
        }
    }
}
