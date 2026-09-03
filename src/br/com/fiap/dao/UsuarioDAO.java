package br.com.fiap.dao;

import br.com.fiap.bean.Usuario;

import java.sql.Connection;
import java.sql.Date;
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
        String sql = "select * from USUARIOS order by id_usuario";
        ArrayList<Usuario> listaUsuarios = new ArrayList<>();
        try(PreparedStatement ps = getConnection().prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            if (rs != null) {
                while (rs.next()) {
                    Usuario usuario = new Usuario();
                    usuario.setId(rs.getInt(1));
                    usuario.setNome(rs.getString(2));
                    usuario.setEmail(rs.getString(3));
                    usuario.setSenha(rs.getString(4));

                    Date dataNascimento = rs.getDate(5);
                    usuario.setDataNascimento(dataNascimento != null
                            ? dataNascimento.toLocalDate() : null);

                    usuario.setPontos(rs.getInt(6));
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
        String sql = "insert into USUARIOS(id_usuario, nome, email, senha, data_nascimento, pontos) "
                + "values (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, usuario.getId());
            ps.setString(2, usuario.getNome());
            ps.setString(3, usuario.getEmail());
            ps.setString(4, usuario.getSenha());
            ps.setDate(5, Date.valueOf(usuario.getDataNascimento()));
            ps.setInt(6, usuario.getPontos());
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
        String sql = "update USUARIOS set nome=?, email=?, senha=?, data_nascimento=?, pontos=? "
                + "where id_usuario=?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, usuario.getNome());
            ps.setString(2, usuario.getEmail());
            ps.setString(3, usuario.getSenha());
            ps.setDate(4, Date.valueOf(usuario.getDataNascimento()));
            ps.setInt(5, usuario.getPontos());
            ps.setInt(6, usuario.getId());
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
        String sql = "delete from USUARIOS where id_usuario=?";
        boolean autoCommitOriginal;

        try {
            autoCommitOriginal = con.getAutoCommit();
            con.setAutoCommit(false);

            deletarRegistrosRelacionados(usuario.getId());

            try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
                ps.setInt(1, usuario.getId());
                if (ps.executeUpdate() > 0) {
                    con.commit();
                    return "Usuario deletado com sucesso!";
                }
            }

            con.rollback();
            return "Erro ao deletar usuario!";
        } catch (SQLException e) {
            try {
                con.rollback();
            } catch (SQLException rollbackException) {
                e.addSuppressed(rollbackException);
            }
            return "Erro ao deletar usuario!";
        } finally {
            try {
                if (con != null && !con.isClosed()) {
                    con.setAutoCommit(true);
                }
            } catch (SQLException e) {
                System.out.println("Erro ao restaurar a conexao: " + e.getMessage());
            }
        }
    }

    private void deletarRegistrosRelacionados(int idUsuario) throws SQLException {
        String[] comandos = {
                "delete from AVATARES where id_usuario=?",
                "delete from INVENTARIO where id_usuario=?",
                "delete from LOGS where id_usuario=?",
                "delete from MISSOES_USUARIO where id_usuario=?"
        };

        for (String comando : comandos) {
            try (PreparedStatement ps = getConnection().prepareStatement(comando)) {
                ps.setInt(1, idUsuario);
                ps.executeUpdate();
            }
        }
    }
}
