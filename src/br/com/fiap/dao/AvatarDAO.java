package br.com.fiap.dao;

import br.com.fiap.dto.Avatar;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class AvatarDAO {
    private Connection con;

    public AvatarDAO(Connection con) {
        this.con = con;
    }

    public Connection getConnection() {
        return con;
    }

    public ArrayList<Avatar> ListarAvatar() {
        String sql = "select * from Avatares order by id_avatar";
        ArrayList<Avatar> avatares = new ArrayList<>();

        try (PreparedStatement ps = getConnection().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Avatar avatar = new Avatar();
                avatar.setId(rs.getInt("id_avatar"));
                avatar.setNome(rs.getString("nome"));
                avatar.setIdCabelo(rs.getInt("id_cabelo"));
                avatar.setIdRoupaCimaInt(rs.getInt("id_roupa_cima_int"));
                avatar.setIdRoupaCimaExt(rs.getInt("id_roupa_cima_ext"));
                avatar.setIdRoupaBaixo(rs.getInt("id_roupa_baixo"));
                avatar.setIdCalcado(rs.getInt("id_calcado"));
                avatar.setIdAcessorio(rs.getInt("id_acessorio"));
                avatar.setIdUsuario(rs.getInt("id_usuario"));
                avatares.add(avatar);
            }
            return avatares;
        } catch (SQLException e) {
            System.out.println("Erro de SQL: " + e.getMessage());
            return null;
        }
    }

    public String InserirAvatar(Avatar avatar) {
        String sql = "insert into Avatares(id_avatar, nome, id_cabelo, id_roupa_cima_int, "
                + "id_roupa_cima_ext, id_roupa_baixo, id_calcado, id_acessorio, id_usuario) "
                + "values (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setInt(1, avatar.getId());
            ps.setString(2, avatar.getNome());
            ps.setInt(3, avatar.getIdCabelo());
            ps.setInt(4, avatar.getIdRoupaCimaInt());
            ps.setInt(5, avatar.getIdRoupaCimaExt());
            ps.setInt(6, avatar.getIdRoupaBaixo());
            ps.setInt(7, avatar.getIdCalcado());
            ps.setInt(8, avatar.getIdAcessorio());
            ps.setInt(9, avatar.getIdUsuario());

            if (ps.executeUpdate() > 0) {
                return "Avatar inserido com sucesso!";
            }
            return "Erro ao inserir avatar!";
        } catch (SQLException e) {
            return "Erro ao inserir avatar!";
        }
    }

    public String AlterarAvatar(Avatar avatar) {
        String sql = "update Avatares set nome=?, id_cabelo=?, id_roupa_cima_int=?, "
                + "id_roupa_cima_ext=?, id_roupa_baixo=?, id_calcado=?, id_acessorio=?, "
                + "id_usuario=? where id_avatar=?";

        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setString(1, avatar.getNome());
            ps.setInt(2, avatar.getIdCabelo());
            ps.setInt(3, avatar.getIdRoupaCimaInt());
            ps.setInt(4, avatar.getIdRoupaCimaExt());
            ps.setInt(5, avatar.getIdRoupaBaixo());
            ps.setInt(6, avatar.getIdCalcado());
            ps.setInt(7, avatar.getIdAcessorio());
            ps.setInt(8, avatar.getIdUsuario());
            ps.setInt(9, avatar.getId());

            if (ps.executeUpdate() > 0) {
                return "Avatar alterado com sucesso!";
            }
            return "Avatar nao encontrado!";
        } catch (SQLException e) {
            return "Erro ao alterar avatar!";
        }
    }

    public String DeletarAvatar(Avatar avatar) {
        String sql = "delete from Avatares where id_avatar=?";

        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setInt(1, avatar.getId());
            if (ps.executeUpdate() > 0) {
                return "Avatar deletado com sucesso!";
            }
            return "Avatar nao encontrado!";
        } catch (SQLException e) {
            return "Erro ao deletar avatar!";
        }
    }
}
