package br.com.fiap.dao;

import br.com.fiap.dto.Codigo;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CodigoDAO {

    private Connection con;

    public CodigoDAO(Connection con) {
        this.con = con;
    }

    public Connection getConnection() {
        return con;
    }

    public ArrayList<Codigo> ListarCodigo() {
        String sql = "select * from Codigos order by id_codigo";
        ArrayList<Codigo> listaCodigo = new ArrayList<>();
        try (PreparedStatement ps = getConnection().prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            if (rs != null) {
                while (rs.next()) {
                    Codigo codigo = new Codigo();
                    codigo.setId(rs.getInt(1));
                    codigo.setCodigoResgate(rs.getString(2));
                    codigo.setStatus(rs.getString(3));

                    Date dataValidade = rs.getDate(4);
                    codigo.setDataValidade(dataValidade != null ? dataValidade.toLocalDate() : null);

                    Date dataResgate = rs.getDate(5);
                    codigo.setDataResgate(dataResgate != null ? dataResgate.toLocalDate() : null);

                    codigo.setIdItem(rs.getInt(6));
                    codigo.setIdParceria(rs.getInt(7));
                    listaCodigo.add(codigo);
                }
                return listaCodigo;
            } else {
                return null;
            }
        } catch (SQLException e) {
            System.out.println("Erro de SQL: " + e.getMessage());
            return null;
        }
    }

    public String InserirCodigo(Codigo codigo) {
        String sql = "insert into Codigos(id_codigo, codigo_resgate, status, data_validade, "
                + "data_resgate, Itens_id_item, Parcerias_id_parceria) values (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setInt(1, codigo.getId());
            ps.setString(2, codigo.getCodigoResgate());
            ps.setString(3, codigo.getStatus());
            ps.setDate(4, codigo.getDataValidade() != null
                    ? Date.valueOf(codigo.getDataValidade()) : null);
            ps.setDate(5, codigo.getDataResgate() != null
                    ? Date.valueOf(codigo.getDataResgate()) : null);
            ps.setInt(6, codigo.getIdItem());
            ps.setInt(7, codigo.getIdParceria());
            if (ps.executeUpdate() > 0) {
                return "Codigo inserido com sucesso!";
            } else {
                return "Erro ao inserir codigo!";
            }
        } catch (SQLException e) {
            return "Erro ao inserir codigo!";
        }
    }

    public String AlterarCodigo(Codigo codigo) {
        String sql = "update Codigos set codigo_resgate=?, status=?, data_validade=?, "
                + "data_resgate=?, Itens_id_item=?, Parcerias_id_parceria=? where id_codigo=?";
        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setString(1, codigo.getCodigoResgate());
            ps.setString(2, codigo.getStatus());
            ps.setDate(3, codigo.getDataValidade() != null
                    ? Date.valueOf(codigo.getDataValidade()) : null);
            ps.setDate(4, codigo.getDataResgate() != null
                    ? Date.valueOf(codigo.getDataResgate()) : null);
            ps.setInt(5, codigo.getIdItem());
            ps.setInt(6, codigo.getIdParceria());
            ps.setInt(7, codigo.getId());
            if (ps.executeUpdate() > 0) {
                return "Codigo alterado com sucesso!";
            } else {
                return "Erro ao alterar codigo!";
            }
        } catch (SQLException e) {
            return "Erro ao alterar codigo!";
        }
    }

    public String DeletarCodigo(Codigo codigo) {
        String sql = "delete from Codigos where id_codigo=?";
        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setInt(1, codigo.getId());
            if (ps.executeUpdate() > 0) {
                return "Codigo deletado com sucesso!";
            } else {
                return "Erro ao deletar codigo!";
            }
        } catch (SQLException e) {
            return "Erro ao deletar codigo!";
        }
    }
}
