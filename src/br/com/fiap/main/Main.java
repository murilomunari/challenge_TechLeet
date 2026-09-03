package br.com.fiap.main;

import br.com.fiap.dao.AvatarDAO;
import br.com.fiap.dao.CodigoDAO;
import br.com.fiap.dao.ConnectionFactory;
import br.com.fiap.dao.InventarioDAO;
import br.com.fiap.dao.ItemDAO;
import br.com.fiap.dao.LogDAO;
import br.com.fiap.dao.MissaoDAO;
import br.com.fiap.dao.MissaoUsuarioDAO;
import br.com.fiap.dao.ParceriaDAO;
import br.com.fiap.dao.UsuarioDAO;
import br.com.fiap.dto.Avatar;
import br.com.fiap.dto.Codigo;
import br.com.fiap.dto.Inventario;
import br.com.fiap.dto.Item;
import br.com.fiap.dto.Log;
import br.com.fiap.dto.Missao;
import br.com.fiap.dto.MissaoUsuario;
import br.com.fiap.dto.Parceria;
import br.com.fiap.dto.Usuario;

import javax.swing.JOptionPane;
import java.sql.Connection;
import java.time.LocalDate;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        Connection con = ConnectionFactory.getConnection();

        if (con == null) {
            JOptionPane.showMessageDialog(null, "Não foi possível conectar ao banco de dados!");
            return;
        }

        UsuarioDAO usuarioDAO = new UsuarioDAO(con);
        AvatarDAO avatarDAO = new AvatarDAO(con);
        ItemDAO itemDAO = new ItemDAO(con);
        InventarioDAO inventarioDAO = new InventarioDAO(con);
        MissaoDAO missaoDAO = new MissaoDAO(con);
        MissaoUsuarioDAO missaoUsuarioDAO = new MissaoUsuarioDAO(con);
        ParceriaDAO parceriaDAO = new ParceriaDAO(con);
        CodigoDAO codigoDAO = new CodigoDAO(con);
        LogDAO logDAO = new LogDAO(con);

        boolean continuar = true;
        int idUsuario = 0;
        int idAvatar = 0;
        int idInventario = 0;
        int idMissaoUsuario = 0;
        int idCodigo = 0;
        int idLog = 0;
        String auxiliar;

        ArrayList<Usuario> usuariosCadastrados = usuarioDAO.listaTodos();
        if (usuariosCadastrados != null) {
            for (Usuario usuarioCadastrado : usuariosCadastrados) {
                if (usuarioCadastrado.getId() > idUsuario) {
                    idUsuario = usuarioCadastrado.getId();
                }
            }
        }

        ArrayList<Avatar> avataresCadastrados = avatarDAO.ListarAvatar();
        if (avataresCadastrados != null) {
            for (Avatar avatarCadastrado : avataresCadastrados) {
                if (avatarCadastrado.getId() > idAvatar) {
                    idAvatar = avatarCadastrado.getId();
                }
            }
        }

        ArrayList<Inventario> inventariosCadastrados = inventarioDAO.ListarInventario();
        if (inventariosCadastrados != null) {
            for (Inventario inventarioCadastrado : inventariosCadastrados) {
                if (inventarioCadastrado.getId() > idInventario) {
                    idInventario = inventarioCadastrado.getId();
                }
            }
        }

        ArrayList<MissaoUsuario> missoesUsuarioCadastradas = missaoUsuarioDAO.ListarMissaoUsuario();
        if (missoesUsuarioCadastradas != null) {
            for (MissaoUsuario missaoUsuarioCadastrada : missoesUsuarioCadastradas) {
                if (missaoUsuarioCadastrada.getId() > idMissaoUsuario) {
                    idMissaoUsuario = missaoUsuarioCadastrada.getId();
                }
            }
        }

        ArrayList<Codigo> codigosCadastrados = codigoDAO.ListarCodigo();
        if (codigosCadastrados != null) {
            for (Codigo codigoCadastrado : codigosCadastrados) {
                if (codigoCadastrado.getId() > idCodigo) {
                    idCodigo = codigoCadastrado.getId();
                }
            }
        }

        ArrayList<Log> logsCadastrados = logDAO.ListarLog();
        if (logsCadastrados != null) {
            for (Log logCadastrado : logsCadastrados) {
                if (logCadastrado.getId() > idLog) {
                    idLog = logCadastrado.getId();
                }
            }
        }

        JOptionPane.showMessageDialog(null, "Bem-vindo ao TechLeet - Sistema de Gamificação!");

        while (continuar) {
            try {
                Usuario usuario = null;

                while (usuario == null && continuar) {
                    String menuAcesso = "=== ACESSO ===\n\n"
                            + "1. Cadastrar usuário\n"
                            + "2. Entrar\n"
                            + "3. Encerrar programa";

                    auxiliar = JOptionPane.showInputDialog(menuAcesso);
                    int opcaoAcesso = Integer.parseInt(auxiliar);

                    if (opcaoAcesso == 1) {
                        String email = JOptionPane.showInputDialog("Digite seu email:");
                        String senha = JOptionPane.showInputDialog("Digite sua senha:");
                        auxiliar = JOptionPane.showInputDialog("Digite o ID do avatar associado ao usuário:");
                        int avatarUsuario = Integer.parseInt(auxiliar);
                        boolean emailCadastrado = false;

                        usuariosCadastrados = usuarioDAO.listaTodos();
                        if (usuariosCadastrados != null) {
                            for (Usuario usuarioCadastrado : usuariosCadastrados) {
                                if (usuarioCadastrado.getEmail().equalsIgnoreCase(email)) {
                                    emailCadastrado = true;
                                }
                            }
                        }

                        if (emailCadastrado) {
                            JOptionPane.showMessageDialog(null, "Esse email já está cadastrado!");
                        } else {
                            idUsuario = idUsuario + 1;
                            usuario = new Usuario(idUsuario, email, senha, 0, avatarUsuario);
                            String resultado = usuarioDAO.InserirUsuario(usuario);
                            JOptionPane.showMessageDialog(null, resultado);

                            if (resultado.contains("sucesso")) {
                                idLog = idLog + 1;
                                Log logCadastro = new Log(idLog, "CADASTRO",
                                        "Usuário cadastrado: " + email,
                                        LocalDate.now(), usuario.getId(), "ok");
                                logDAO.InserirLog(logCadastro);
                            } else {
                                usuario = null;
                            }
                        }
                    } else if (opcaoAcesso == 2) {
                        String email = JOptionPane.showInputDialog("Digite seu email:");
                        String senha = JOptionPane.showInputDialog("Digite sua senha:");

                        usuariosCadastrados = usuarioDAO.listaTodos();
                        if (usuariosCadastrados != null) {
                            for (Usuario usuarioCadastrado : usuariosCadastrados) {
                                if (usuarioCadastrado.getEmail().equalsIgnoreCase(email)
                                        && usuarioCadastrado.getSenha().equals(senha)) {
                                    usuario = usuarioCadastrado;
                                }
                            }
                        }

                        if (usuario == null) {
                            JOptionPane.showMessageDialog(null, "Email ou senha inválidos!");
                        } else {
                            idLog = idLog + 1;
                            Log logLogin = new Log(idLog, "SESSAO", "Login realizado",
                                    LocalDate.now(), usuario.getId(), "ok");
                            logDAO.InserirLog(logLogin);
                            JOptionPane.showMessageDialog(null, "Login realizado com sucesso!");
                        }
                    } else if (opcaoAcesso == 3) {
                        continuar = false;
                    } else {
                        JOptionPane.showMessageDialog(null, "Opção inválida!");
                    }
                }

                if (usuario != null) {
                    boolean continuarMenu = true;

                    while (continuarMenu) {
                        String menuPrincipal = "=== MENU PRINCIPAL ===\n\n"
                                + "Usuário: " + usuario.getEmail() + "\n"
                                + "Pontos: " + usuario.getPontos() + "\n\n"
                                + "1. Criar Avatar\n"
                                + "2. Equipar item no Avatar\n"
                                + "3. Fazer Missão\n"
                                + "4. Gastar Pontos\n"
                                + "5. Ver Informações\n"
                                + "6. Deletar Usuário\n"
                                + "7. Sair";

                        auxiliar = JOptionPane.showInputDialog(menuPrincipal);
                        int opcaoMenu = Integer.parseInt(auxiliar);

                        if (opcaoMenu == 1) {
                            Avatar avatar = null;
                            avataresCadastrados = avatarDAO.ListarAvatar();

                            if (avataresCadastrados != null) {
                                for (Avatar avatarCadastrado : avataresCadastrados) {
                                    if (avatarCadastrado.getIdUsuario() == usuario.getId()) {
                                        avatar = avatarCadastrado;
                                    }
                                }
                            }

                            if (avatar != null) {
                                JOptionPane.showMessageDialog(null,
                                        "Você já possui o avatar '" + avatar.getNome() + "'!");
                            } else {
                                String nomeAvatar = JOptionPane.showInputDialog("Digite o nome do seu Avatar:");
                                idAvatar = idAvatar + 1;
                                avatar = new Avatar(idAvatar, nomeAvatar,
                                        0, 0, 0, 0, 0, 0, usuario.getId());

                                String resultado = avatarDAO.InserirAvatar(avatar);
                                JOptionPane.showMessageDialog(null, resultado);

                                if (resultado.contains("sucesso")) {
                                    idLog = idLog + 1;
                                    Log logAvatar = new Log(idLog, "AVATAR",
                                            "Avatar criado: " + nomeAvatar,
                                            LocalDate.now(), usuario.getId(), "ok");
                                    logDAO.InserirLog(logAvatar);
                                }
                            }
                        } else if (opcaoMenu == 2) {
                            Avatar avatar = null;
                            avataresCadastrados = avatarDAO.ListarAvatar();

                            if (avataresCadastrados != null) {
                                for (Avatar avatarCadastrado : avataresCadastrados) {
                                    if (avatarCadastrado.getIdUsuario() == usuario.getId()) {
                                        avatar = avatarCadastrado;
                                    }
                                }
                            }

                            if (avatar == null) {
                                JOptionPane.showMessageDialog(null, "Crie um avatar primeiro! (Opção 1)");
                            } else {
                                ArrayList<Item> itens = itemDAO.ListarItem();
                                ArrayList<Item> itensEquipaveis = new ArrayList<>();
                                String menuItens = "Escolha um item para equipar:\n\n";

                                if (itens != null) {
                                    for (Item item : itens) {
                                        String tipo = item.getTipo();
                                        if (tipo != null && (tipo.equalsIgnoreCase("cabelo")
                                                || tipo.equalsIgnoreCase("roupa_cima_int")
                                                || tipo.equalsIgnoreCase("roupa_cima_ext")
                                                || tipo.equalsIgnoreCase("roupa_baixo")
                                                || tipo.equalsIgnoreCase("calcado")
                                                || tipo.equalsIgnoreCase("acessorio"))) {
                                            itensEquipaveis.add(item);
                                            menuItens = menuItens + itensEquipaveis.size() + ". "
                                                    + item.getNome() + " - " + item.getTipo() + "\n";
                                        }
                                    }
                                }

                                if (itensEquipaveis.isEmpty()) {
                                    JOptionPane.showMessageDialog(null, "Não há itens cadastrados no banco!");
                                } else {
                                    auxiliar = JOptionPane.showInputDialog(menuItens);
                                    int opcaoItem = Integer.parseInt(auxiliar);

                                    if (opcaoItem >= 1 && opcaoItem <= itensEquipaveis.size()) {
                                        Item itemEscolhido = itensEquipaveis.get(opcaoItem - 1);
                                        avatar.equiparItem(itemEscolhido);
                                        String resultado = avatarDAO.AlterarAvatar(avatar);

                                        if (resultado.contains("sucesso")) {
                                            boolean possuiItem = false;
                                            inventariosCadastrados = inventarioDAO.ListarInventario();

                                            if (inventariosCadastrados != null) {
                                                for (Inventario inventario : inventariosCadastrados) {
                                                    if (inventario.getIdUsuario() == usuario.getId()
                                                            && inventario.getIdItem() == itemEscolhido.getId()) {
                                                        possuiItem = true;
                                                    }
                                                }
                                            }

                                            if (!possuiItem) {
                                                idInventario = idInventario + 1;
                                                Inventario inventario = new Inventario(idInventario,
                                                        usuario.getId(), itemEscolhido.getId(),
                                                        "personalizacao", LocalDate.now());
                                                inventarioDAO.InserirInventario(inventario);
                                            }

                                            idLog = idLog + 1;
                                            Log logItem = new Log(idLog, "EQUIPAR",
                                                    "Item equipado: " + itemEscolhido.getNome(),
                                                    LocalDate.now(), usuario.getId(), "ok");
                                            logDAO.InserirLog(logItem);
                                            JOptionPane.showMessageDialog(null, "Item equipado com sucesso!");
                                        } else {
                                            JOptionPane.showMessageDialog(null, resultado);
                                        }
                                    } else {
                                        JOptionPane.showMessageDialog(null, "Opção inválida!");
                                    }
                                }
                            }
                        } else if (opcaoMenu == 3) {
                            ArrayList<Missao> missoes = missaoDAO.ListaMissao();

                            if (missoes == null || missoes.isEmpty()) {
                                JOptionPane.showMessageDialog(null, "Não há missões cadastradas no banco!");
                            } else {
                                String menuMissoes = "Escolha uma missão:\n\n";
                                for (int i = 0; i < missoes.size(); i++) {
                                    Missao missao = missoes.get(i);
                                    menuMissoes = menuMissoes + (i + 1) + ". " + missao.getTitulo()
                                            + " (" + missao.getPontos() + " pontos)\n";
                                }

                                auxiliar = JOptionPane.showInputDialog(menuMissoes);
                                int opcaoMissao = Integer.parseInt(auxiliar);

                                if (opcaoMissao >= 1 && opcaoMissao <= missoes.size()) {
                                    Missao missao = missoes.get(opcaoMissao - 1);
                                    boolean concluida = false;
                                    missoesUsuarioCadastradas = missaoUsuarioDAO.ListarMissaoUsuario();

                                    if (missoesUsuarioCadastradas != null) {
                                        for (MissaoUsuario registro : missoesUsuarioCadastradas) {
                                            if (registro.getIdUsuario() == usuario.getId()
                                                    && registro.getIdMissao() == missao.getId()
                                                    && registro.getStatus().equalsIgnoreCase("concluida")) {
                                                concluida = true;
                                            }
                                        }
                                    }

                                    if (concluida) {
                                        JOptionPane.showMessageDialog(null, "Você já concluiu essa missão!");
                                    } else {
                                        idMissaoUsuario = idMissaoUsuario + 1;
                                        MissaoUsuario registro = new MissaoUsuario(idMissaoUsuario,
                                                usuario.getId(), missao.getId(), "pendente",
                                                null, null, null);
                                        registro.iniciar();
                                        registro.concluir();
                                        registro.setDataFim(LocalDate.now());

                                        String resultado = missaoUsuarioDAO.InserirMissaoUsuario(registro);
                                        if (resultado.contains("sucesso")) {
                                            usuario.adicionarPontos(missao.getPontos());
                                            usuarioDAO.AlterarUsuario(usuario);

                                            idLog = idLog + 1;
                                            Log logMissao = new Log(idLog, "MISSAO",
                                                    "Missão concluída: " + missao.getTitulo()
                                                            + " (+" + missao.getPontos() + " pts)",
                                                    LocalDate.now(), usuario.getId(), "ok");
                                            logDAO.InserirLog(logMissao);

                                            JOptionPane.showMessageDialog(null,
                                                    "Missão concluída!\n\nVocê ganhou "
                                                            + missao.getPontos() + " pontos!\nTotal: "
                                                            + usuario.getPontos() + " pontos");
                                        } else {
                                            JOptionPane.showMessageDialog(null, resultado);
                                        }
                                    }
                                } else {
                                    JOptionPane.showMessageDialog(null, "Opção inválida!");
                                }
                            }
                        } else if (opcaoMenu == 4) {
                            ArrayList<Item> itens = itemDAO.ListarItem();
                            ArrayList<Item> itensDisponiveis = new ArrayList<>();
                            inventariosCadastrados = inventarioDAO.ListarInventario();
                            String menuCompra = "Escolha um item para comprar:\n\n";

                            if (itens != null) {
                                for (Item item : itens) {
                                    boolean possuiItem = false;
                                    if (inventariosCadastrados != null) {
                                        for (Inventario inventario : inventariosCadastrados) {
                                            if (inventario.getIdUsuario() == usuario.getId()
                                                    && inventario.getIdItem() == item.getId()) {
                                                possuiItem = true;
                                            }
                                        }
                                    }

                                    if (item.getValorPontos() > 0 && !possuiItem) {
                                        itensDisponiveis.add(item);
                                        menuCompra = menuCompra + itensDisponiveis.size() + ". "
                                                + item.getNome() + " (" + item.getValorPontos()
                                                + " pontos)\n";
                                    }
                                }
                            }

                            if (itensDisponiveis.isEmpty()) {
                                JOptionPane.showMessageDialog(null, "Não há itens disponíveis para compra!");
                            } else {
                                auxiliar = JOptionPane.showInputDialog(menuCompra);
                                int opcaoCompra = Integer.parseInt(auxiliar);

                                if (opcaoCompra >= 1 && opcaoCompra <= itensDisponiveis.size()) {
                                    Item itemComprado = itensDisponiveis.get(opcaoCompra - 1);

                                    if (usuario.getPontos() < itemComprado.getValorPontos()) {
                                        JOptionPane.showMessageDialog(null, "Você não tem pontos suficientes!");
                                    } else {
                                        ArrayList<Parceria> parcerias = parceriaDAO.ListarParceria();
                                        ArrayList<Parceria> parceriasAtivas = new ArrayList<>();
                                        String menuParcerias = "Escolha uma parceria:\n\n";

                                        if (parcerias != null) {
                                            for (Parceria parceria : parcerias) {
                                                if (parceria.getStatus().equalsIgnoreCase("ativa")) {
                                                    parceriasAtivas.add(parceria);
                                                    menuParcerias = menuParcerias + parceriasAtivas.size()
                                                            + ". " + parceria.getNome() + "\n";
                                                }
                                            }
                                        }

                                        if (parceriasAtivas.isEmpty()) {
                                            JOptionPane.showMessageDialog(null, "Não há parcerias ativas!");
                                        } else {
                                            auxiliar = JOptionPane.showInputDialog(menuParcerias);
                                            int opcaoParceria = Integer.parseInt(auxiliar);

                                            if (opcaoParceria >= 1
                                                    && opcaoParceria <= parceriasAtivas.size()) {
                                                Parceria parceria = parceriasAtivas.get(opcaoParceria - 1);
                                                idCodigo = idCodigo + 1;
                                                Codigo codigo = new Codigo(idCodigo,
                                                        "COD" + idCodigo + "-" + usuario.getId(),
                                                        "ativo", LocalDate.now().plusDays(30), null,
                                                        itemComprado.getId(), parceria.getId());
                                                codigo.resgatar();

                                                usuario.gastarPontos(itemComprado.getValorPontos());
                                                usuarioDAO.AlterarUsuario(usuario);
                                                codigoDAO.InserirCodigo(codigo);

                                                idInventario = idInventario + 1;
                                                Inventario inventario = new Inventario(idInventario,
                                                        usuario.getId(), itemComprado.getId(),
                                                        "compra", LocalDate.now());
                                                inventarioDAO.InserirInventario(inventario);

                                                idLog = idLog + 1;
                                                Log logCompra = new Log(idLog, "COMPRA",
                                                        "Item comprado: " + itemComprado.getNome()
                                                                + " (-" + itemComprado.getValorPontos()
                                                                + " pts)",
                                                        LocalDate.now(), usuario.getId(), "ok");
                                                logDAO.InserirLog(logCompra);

                                                JOptionPane.showMessageDialog(null,
                                                        "Item comprado com sucesso!\n\nCódigo: "
                                                                + codigo.getCodigoResgate()
                                                                + "\nParceria: " + parceria.getNome()
                                                                + "\nSaldo: " + usuario.getPontos()
                                                                + " pontos");
                                            } else {
                                                JOptionPane.showMessageDialog(null, "Opção inválida!");
                                            }
                                        }
                                    }
                                } else {
                                    JOptionPane.showMessageDialog(null, "Opção inválida!");
                                }
                            }
                        } else if (opcaoMenu == 5) {
                            Avatar avatar = null;
                            avataresCadastrados = avatarDAO.ListarAvatar();
                            ArrayList<Item> itens = itemDAO.ListarItem();
                            inventariosCadastrados = inventarioDAO.ListarInventario();
                            missoesUsuarioCadastradas = missaoUsuarioDAO.ListarMissaoUsuario();

                            if (avataresCadastrados != null) {
                                for (Avatar avatarCadastrado : avataresCadastrados) {
                                    if (avatarCadastrado.getIdUsuario() == usuario.getId()) {
                                        avatar = avatarCadastrado;
                                    }
                                }
                            }

                            String info = "=== SUAS INFORMAÇÕES ===\n\n";
                            info = info + "ID: " + usuario.getId() + "\n";
                            info = info + "EMAIL: " + usuario.getEmail() + "\n";
                            info = info + "PONTOS: " + usuario.getPontos() + "\n\n";

                            if (avatar == null) {
                                info = info + "AVATAR: Não criado\n";
                            } else {
                                info = info + "AVATAR: " + avatar.getNome() + "\n";
                            }

                            info = info + "\nINVENTÁRIO:\n";
                            int quantidadeItens = 0;
                            if (inventariosCadastrados != null) {
                                for (Inventario inventario : inventariosCadastrados) {
                                    if (inventario.getIdUsuario() == usuario.getId()) {
                                        String nomeItem = "Item #" + inventario.getIdItem();
                                        if (itens != null) {
                                            for (Item item : itens) {
                                                if (item.getId() == inventario.getIdItem()) {
                                                    nomeItem = item.getNome();
                                                }
                                            }
                                        }
                                        info = info + "  - " + nomeItem + " ("
                                                + inventario.getOrigem() + ")\n";
                                        quantidadeItens = quantidadeItens + 1;
                                    }
                                }
                            }

                            if (quantidadeItens == 0) {
                                info = info + "  Nenhum item adquirido.\n";
                            }

                            int missoesConcluidas = 0;
                            if (missoesUsuarioCadastradas != null) {
                                for (MissaoUsuario registro : missoesUsuarioCadastradas) {
                                    if (registro.getIdUsuario() == usuario.getId()
                                            && registro.getStatus().equalsIgnoreCase("concluida")) {
                                        missoesConcluidas = missoesConcluidas + 1;
                                    }
                                }
                            }

                            info = info + "\nMISSÕES CONCLUÍDAS: " + missoesConcluidas;
                            JOptionPane.showMessageDialog(null, info);
                        } else if (opcaoMenu == 6) {
                            String confirmar = JOptionPane.showInputDialog(
                                    "Tem certeza que deseja deletar seu usuário?\n\n"
                                            + "1. Sim\n"
                                            + "2. Não");
                            int opcaoDeletar = Integer.parseInt(confirmar);

                            if (opcaoDeletar == 1) {
                                String resultado = usuarioDAO.DeletarUsuario(usuario);
                                JOptionPane.showMessageDialog(null, resultado);

                                if (resultado.contains("sucesso")) {
                                    continuarMenu = false;
                                }
                            } else if (opcaoDeletar != 2) {
                                JOptionPane.showMessageDialog(null, "Opção inválida!");
                            }
                        } else if (opcaoMenu == 7) {
                            String menuSaida = "O que você deseja fazer?\n\n"
                                    + "1. Encerrar programa\n"
                                    + "2. Entrar com outro usuário";
                            auxiliar = JOptionPane.showInputDialog(menuSaida);
                            int opcaoSaida = Integer.parseInt(auxiliar);

                            if (opcaoSaida == 1) {
                                idLog = idLog + 1;
                                Log logFim = new Log(idLog, "SESSAO", "Sessão encerrada",
                                        LocalDate.now(), usuario.getId(), "ok");
                                logDAO.InserirLog(logFim);
                                continuarMenu = false;
                                continuar = false;
                            } else if (opcaoSaida == 2) {
                                continuarMenu = false;
                            } else {
                                JOptionPane.showMessageDialog(null, "Opção inválida!");
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, "Opção inválida!");
                        }
                    }
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Erro: Digite um número válido!");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Erro: " + e.getMessage());
            }
        }

        ConnectionFactory.closeConnection(con);
        JOptionPane.showMessageDialog(null, "Obrigado por usar o TechLeet!");
    }
}
