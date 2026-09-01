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
        String sql = "select * from Usuario from id_usuario";
        ArrayList<Usuario> listaUsuarios = new ArrayList<>();
        try(PreparedStatement ps = getConnection().prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Usuario usuario = new Usuario();
                usuario.setId(rs.getInt("id_usuario"));
                usuario.setEmail(rs.getString("email"));
                usuario.setSenha(rs.getString("senha"));
                usuario.setPontos(rs.getInt("pontos"));
                usuario.setIdAvatar(rs.getInt("id_avatar"));
                listaUsuarios.add(usuario);
            }
            return listaUsuarios;
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
            ps.executeUpdate();
            return "Usuario inserido com sucesso";
        } catch (SQLException e) {
            return "Erro ao inserir usuario";
        }
    }
}
