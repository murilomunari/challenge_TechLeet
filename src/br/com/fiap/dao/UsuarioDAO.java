package br.com.fiap.dao;

import br.com.fiap.dto.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class UsuarioDAO {

    private Connection con;

    public UsuarioDAO(Connection con) {
        this.con = con;
    }

    public Connection getConnection() {
        return con;
    }

    public ArrayList<Usuario> listaTodos() {
        String sql = "select * from Usuarios order by id_usuario";
        ArrayList<Usuario> listaUsuarios = new ArrayList<>();
        try(PreparedStatement ps = getConnection().prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            if (rs != null) {
                while (rs.next()) {
                    Usuario usuario = new Usuario();
                    usuario.setId(rs.getInt(1));
                    usuario.setEmail(rs.getString(2));
                    usuario.setSenha(rs.getString(3));
                    usuario.setPontos(rs.getInt(4));
                    usuario.setIdAvatar(rs.getInt(5));
                    listaUsuarios.add(usuario);
                }
                return listaUsuarios;
            } else {
                return null;
            }
        } catch (SQLException e) {
            System.out.println("Erro de sql: " + e.getMessage());
            return null;
        }
    }

    public String InserirUsuario(Usuario usuario) {
        String sql = "insert into Usuarios(id_usuario, email, senha, pontos, id_avatar) values (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, usuario.getId());
            ps.setString(2, usuario.getEmail());
            ps.setString(3, usuario.getSenha());
            ps.setInt(4, usuario.getPontos());
            ps.setInt(5, usuario.getIdAvatar());
            if (ps.executeUpdate() > 0) {
                return "Usuario inserido com sucesso!";
            } else {
                return "Erro ao inserir usuario!";
            }
        } catch (SQLException e) {
            return "Erro ao inserir usuario!";
        }
    }

    public String AlterarUsuario(Usuario usuario) {
        String sql = "update Usuarios set email=?, senha=?, pontos=? where id_usuario=?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, usuario.getEmail());
            ps.setString(2, usuario.getSenha());
            ps.setInt(3, usuario.getPontos());
            ps.setInt(4, usuario.getId());
            if (ps.executeUpdate() > 0) {
                return "Usuario alterado com sucesso!";
            } else {
                return "Erro ao alterar usuario!";
            }
        } catch (SQLException e) {
            return "Erro ao alterar usuario!";
        }
    }

    public String DeletarUsuario(Usuario usuario) {
        String sql = "delete from Usuarios where id_usuario=?";
        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setInt(1, usuario.getId());
            if (ps.executeUpdate() > 0) {
                return "Usuario deletado com sucesso!";
            } else {
                return "Erro ao deletar usuario!";
            }
        } catch (SQLException e) {
            return "Erro ao deletar usuario!";
        }
    }
}
