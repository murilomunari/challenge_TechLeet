package br.com.fiap.main;

import br.com.fiap.dao.ConnectionFactory;
import br.com.fiap.dao.UsuarioDAO;
import br.com.fiap.dto.Usuario;

import java.sql.Connection;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {

        Connection con = ConnectionFactory.getConnection();


        // ==================================================
        // USUARIO
        // ==================================================

        UsuarioDAO usuarioDAO = new UsuarioDAO(con);

        Usuario usuario = new Usuario();

        // INSERT
        usuario.setId(1);
        usuario.setEmail("rm569602@fiap.com.br");
        usuario.setSenha("SenhaTeste");
        usuario.setPontos(500);
        usuario.setIdAvatar(1);

        System.out.println(usuarioDAO.InserirUsuario(usuario));


        // SELECT
        ArrayList<Usuario> resultado = usuarioDAO.listaTodos();

        if (resultado != null) {
            for (Usuario u : resultado) {
                System.out.println("\nId: " + u.getId());
                System.out.println("Email: " + u.getEmail());
                System.out.println("Senha: " + u.getSenha());
                System.out.println("Pontos: " + u.getPontos());
                System.out.println("Id do avatar: " + u.getIdAvatar());
            }
        } else {
            System.out.println("Tabela não encontrada ou vazia!");
        }


        // UPDATE
        usuario.setEmail("testeUpdate@fiap.com.br");
        usuario.setSenha("SenhaDois");
        usuario.setPontos(300);

        System.out.println(usuarioDAO.AlterarUsuario(usuario));


        // DELETE
        usuario.setId(1);

        System.out.println(usuarioDAO.DeletarUsuario(usuario));


        // ==================================================
        // OUTRA CLASSE
        // ==================================================

        // ...


        ConnectionFactory.closeConnection(con);
    }
}

