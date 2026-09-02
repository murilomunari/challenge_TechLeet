package br.com.fiap.dao;

import br.com.fiap.dto.Parceria;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ParceriaDAO {

    private Connection con;

    public ParceriaDAO(Connection con) {
        this.con = con;
    }

    public Connection getConnection() {
        return con;
    }

    public ArrayList<Parceria> ListarParceria() {
        String sql = "select * from Parcerias order by id_parceria";
        ArrayList<Parceria> listaParceria = new ArrayList<>();
        try (PreparedStatement ps = getConnection().prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            if (rs != null) {
                while (rs.next()) {
                    Parceria parceria = new Parceria();
                    parceria.setId(rs.getInt(1));
                    parceria.setTipo(rs.getString(2));
                    parceria.setNome(rs.getString(3));
                    parceria.setStatus(rs.getString(4));
                    parceria.setCustoMensal(rs.getBigDecimal(5));

                    Date dataInicio = rs.getDate(6);
                    parceria.setDataInicio(dataInicio != null ? dataInicio.toLocalDate() : null);

                    Date dataFim = rs.getDate(7);
                    parceria.setDataFim(dataFim != null ? dataFim.toLocalDate() : null);

                    listaParceria.add(parceria);
                }
                return listaParceria;
            } else {
                return null;
            }
        } catch (SQLException e) {
            System.out.println("Erro de SQL: " + e.getMessage());
            return null;
        }
    }

    public String InserirParceria(Parceria parceria) {
        String sql = "insert into Parcerias(id_parceria, tipo, nome, status, custo_mensal, "
                + "data_inicio, data_fim) values (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setInt(1, parceria.getId());
            ps.setString(2, parceria.getTipo());
            ps.setString(3, parceria.getNome());
            ps.setString(4, parceria.getStatus());
            ps.setBigDecimal(5, parceria.getCustoMensal());
            ps.setDate(6, parceria.getDataInicio() != null
                    ? Date.valueOf(parceria.getDataInicio()) : null);
            ps.setDate(7, parceria.getDataFim() != null
                    ? Date.valueOf(parceria.getDataFim()) : null);
            if (ps.executeUpdate() > 0) {
                return "Parceria inserida com sucesso!";
            } else {
                return "Erro ao inserir parceria!";
            }
        } catch (SQLException e) {
            return "Erro ao inserir parceria!";
        }
    }

    public String AlterarParceria(Parceria parceria) {
        String sql = "update Parcerias set tipo=?, nome=?, status=?, custo_mensal=?, "
                + "data_inicio=?, data_fim=? where id_parceria=?";
        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setString(1, parceria.getTipo());
            ps.setString(2, parceria.getNome());
            ps.setString(3, parceria.getStatus());
            ps.setBigDecimal(4, parceria.getCustoMensal());
            ps.setDate(5, parceria.getDataInicio() != null
                    ? Date.valueOf(parceria.getDataInicio()) : null);
            ps.setDate(6, parceria.getDataFim() != null
                    ? Date.valueOf(parceria.getDataFim()) : null);
            ps.setInt(7, parceria.getId());
            if (ps.executeUpdate() > 0) {
                return "Parceria alterada com sucesso!";
            } else {
                return "Erro ao alterar parceria!";
            }
        } catch (SQLException e) {
            return "Erro ao alterar parceria!";
        }
    }

    public String DeletarParceria(Parceria parceria) {
        String sql = "delete from Parcerias where id_parceria=?";
        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setInt(1, parceria.getId());
            if (ps.executeUpdate() > 0) {
                return "Parceria deletada com sucesso!";
            } else {
                return "Erro ao deletar parceria!";
            }
        } catch (SQLException e) {
            return "Erro ao deletar parceria!";
        }
    }
}
