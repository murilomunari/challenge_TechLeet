package br.com.fiap.main;

import br.com.fiap.dao.AvatarDAO;
import br.com.fiap.dao.ConnectionFactory;
import br.com.fiap.dao.InventarioDAO;
import br.com.fiap.dao.ItemDAO;
import br.com.fiap.dao.LogDAO;
import br.com.fiap.dao.MissaoDAO;
import br.com.fiap.dao.MissaoUsuarioDAO;
import br.com.fiap.dao.UsuarioDAO;
import br.com.fiap.dto.Avatar;
import br.com.fiap.dto.AvatarException;
import br.com.fiap.dto.Inventario;
import br.com.fiap.dto.Item;
import br.com.fiap.dto.Log;
import br.com.fiap.dto.Missao;
import br.com.fiap.dto.MissaoUsuario;
import br.com.fiap.dto.MissaoUsuarioException;
import br.com.fiap.dto.Usuario;
import br.com.fiap.dto.UsuarioException;

import javax.swing.JOptionPane;
import java.sql.Connection;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
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
        LogDAO logDAO = new LogDAO(con);

        boolean continuar = true;
        int idUsuario = 0;
        int idAvatar = 0;
        int idItem = 0;
        int idInventario = 0;
        int idMissao = 0;
        int idMissaoUsuario = 0;
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

        ArrayList<Item> itensCadastrados = itemDAO.ListarItem();
        if (itensCadastrados != null) {
            for (Item itemCadastrado : itensCadastrados) {
                if (itemCadastrado.getId() > idItem) {
                    idItem = itemCadastrado.getId();
                }
            }
        }

        ArrayList<Missao> missoesCadastradas = missaoDAO.ListaMissao();
        if (missoesCadastradas != null) {
            for (Missao missaoCadastrada : missoesCadastradas) {
                if (missaoCadastrada.getId() > idMissao) {
                    idMissao = missaoCadastrada.getId();
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
                        String nome = JOptionPane.showInputDialog("Digite seu nome:");
                        String email = JOptionPane.showInputDialog("Digite seu email:");
                        String senha = JOptionPane.showInputDialog("Digite sua senha:");
                        auxiliar = JOptionPane.showInputDialog(
                                "Digite sua data de nascimento (AAAA-MM-DD):");
                        LocalDate dataNascimento = LocalDate.parse(auxiliar);

                        if (dataNascimento.isAfter(LocalDate.now())) {
                            throw new UsuarioException("A data de nascimento nao pode estar no futuro.");
                        }
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
                            usuario = new Usuario(idUsuario, nome, email, senha, dataNascimento, 0);
                            String resultado = usuarioDAO.InserirUsuario(usuario);
                            JOptionPane.showMessageDialog(null, resultado);

                            if (resultado.contains("sucesso")) {
                                idLog = idLog + 1;
                                Log logCadastro = new Log(idLog, "CADASTRO",
                                        "Usuário cadastrado: " + email,
                                        LocalDate.now(), usuario.getId(), "SUCESSO");
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
                                    LocalDate.now(), usuario.getId(), "SUCESSO");
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
                                + "7. Criar Missão\n"
                                + "8. Criar Item\n"
                                + "9. Sair";

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
                                            LocalDate.now(), usuario.getId(), "SUCESSO");
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
                                inventariosCadastrados = inventarioDAO.ListarInventario();
                                ArrayList<Item> itensEquipaveis = new ArrayList<>();
                                String menuItens = "Escolha um item para equipar:\n\n";

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

                                        String modelo = item.getModelo();
                                        boolean modeloEquipavel = modelo != null
                                                && (modelo.equalsIgnoreCase("CABELO")
                                                || modelo.equalsIgnoreCase("ROUPA DE CIMA INTERNA")
                                                || modelo.equalsIgnoreCase("ROUPA DE CIMA EXTERNA")
                                                || modelo.equalsIgnoreCase("ROUPA DE BAIXO")
                                                || modelo.equalsIgnoreCase("CALCADO")
                                                || modelo.equalsIgnoreCase("ACESSORIO"));

                                        if (possuiItem && modeloEquipavel) {
                                            itensEquipaveis.add(item);
                                            menuItens = menuItens + itensEquipaveis.size() + ". "
                                                    + item.getNome() + " - " + item.getModelo() + "\n";
                                        }
                                    }
                                }

                                if (itensEquipaveis.isEmpty()) {
                                    JOptionPane.showMessageDialog(null,
                                            "Você não possui itens disponíveis para equipar!");
                                } else {
                                    auxiliar = JOptionPane.showInputDialog(menuItens);
                                    int opcaoItem = Integer.parseInt(auxiliar);

                                    if (opcaoItem >= 1 && opcaoItem <= itensEquipaveis.size()) {
                                        Item itemEscolhido = itensEquipaveis.get(opcaoItem - 1);
                                        avatar.equiparItem(itemEscolhido);
                                        String resultado = avatarDAO.AlterarAvatar(avatar);

                                        if (resultado.contains("sucesso")) {
                                            idLog = idLog + 1;
                                            Log logItem = new Log(idLog, "EQUIPAR",
                                                    "Item equipado: " + itemEscolhido.getNome(),
                                                    LocalDate.now(), usuario.getId(), "SUCESSO");
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
                                                    && registro.getDataRealizacao() != null) {
                                                concluida = true;
                                            }
                                        }
                                    }

                                    if (concluida) {
                                        JOptionPane.showMessageDialog(null, "Você já concluiu essa missão!");
                                    } else {
                                        idMissaoUsuario = idMissaoUsuario + 1;
                                        LocalDate dataInicio = LocalDate.now();
                                        LocalDate dataFim;
                                        if ("DIARIA".equalsIgnoreCase(missao.getTipo())) {
                                            dataFim = dataInicio;
                                        } else if ("SEMANAL".equalsIgnoreCase(missao.getTipo())) {
                                            dataFim = dataInicio.plusDays(7);
                                        } else {
                                            dataFim = dataInicio.plusDays(30);
                                        }

                                        MissaoUsuario registro = new MissaoUsuario(idMissaoUsuario,
                                                usuario.getId(), missao.getId(), "DISPONIVEL",
                                                null, dataInicio, dataFim);
                                        registro.concluir();

                                        String resultado = missaoUsuarioDAO.InserirMissaoUsuario(registro);
                                        if (resultado.contains("sucesso")) {
                                            usuario.adicionarPontos(missao.getPontos());
                                            usuarioDAO.AlterarUsuario(usuario);

                                            idLog = idLog + 1;
                                            Log logMissao = new Log(idLog, "MISSAO",
                                                    "Missão concluída: " + missao.getTitulo()
                                                            + " (+" + missao.getPontos() + " pts)",
                                                    LocalDate.now(), usuario.getId(), "SUCESSO");
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
                                        usuario.gastarPontos(itemComprado.getValorPontos());
                                        usuarioDAO.AlterarUsuario(usuario);

                                        idInventario = idInventario + 1;
                                        Inventario inventario = new Inventario(idInventario,
                                                usuario.getId(), itemComprado.getId(),
                                                "COMPRA", LocalDate.now());
                                        inventarioDAO.InserirInventario(inventario);

                                        idLog = idLog + 1;
                                        Log logCompra = new Log(idLog, "COMPRA",
                                                "Item comprado: " + itemComprado.getNome()
                                                        + " (-" + itemComprado.getValorPontos()
                                                        + " pts)",
                                                LocalDate.now(), usuario.getId(), "SUCESSO");
                                        logDAO.InserirLog(logCompra);

                                        JOptionPane.showMessageDialog(null,
                                                "Item comprado com sucesso!\n\nSaldo: "
                                                        + usuario.getPontos() + " pontos");
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
                            info = info + "NOME: " + usuario.getNome() + "\n";
                            info = info + "EMAIL: " + usuario.getEmail() + "\n";
                            info = info + "DATA DE NASCIMENTO: " + usuario.getDataNascimento() + "\n";
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
                                            && registro.getDataRealizacao() != null) {
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
                            String titulo = JOptionPane.showInputDialog("Digite o título da missão:");
                            String descricao = JOptionPane.showInputDialog("Digite a descrição da missão:");
                            auxiliar = JOptionPane.showInputDialog("Digite a quantidade de pontos da missão:");
                            int pontosMissao = Integer.parseInt(auxiliar);

                            String menuTipoMissao = "Escolha o tipo da missão:\n\n"
                                    + "1. Diária\n"
                                    + "2. Semanal\n"
                                    + "3. Especial";
                            auxiliar = JOptionPane.showInputDialog(menuTipoMissao);
                            int opcaoTipoMissao = Integer.parseInt(auxiliar);
                            String tipoMissao = null;

                            if (opcaoTipoMissao == 1) {
                                tipoMissao = "DIARIA";
                            } else if (opcaoTipoMissao == 2) {
                                tipoMissao = "SEMANAL";
                            } else if (opcaoTipoMissao == 3) {
                                tipoMissao = "ESPECIAL";
                            }

                            if (titulo == null || titulo.trim().isEmpty()
                                    || descricao == null || descricao.trim().isEmpty()) {
                                JOptionPane.showMessageDialog(null,
                                        "O título e a descrição devem ser informados!");
                            } else if (pontosMissao < 0) {
                                JOptionPane.showMessageDialog(null,
                                        "Os pontos da missão não podem ser negativos!");
                            } else if (tipoMissao == null) {
                                JOptionPane.showMessageDialog(null, "Tipo de missão inválido!");
                            } else {
                                idMissao = idMissao + 1;
                                Missao novaMissao = new Missao(idMissao, titulo, descricao,
                                        pontosMissao, tipoMissao);
                                String resultado = missaoDAO.InserirMissao(novaMissao);
                                JOptionPane.showMessageDialog(null, resultado);

                                if (resultado.contains("sucesso")) {
                                    idLog = idLog + 1;
                                    Log logMissaoCriada = new Log(idLog, "CRIAR MISSAO",
                                            "Missão criada: " + titulo,
                                            LocalDate.now(), usuario.getId(), "SUCESSO");
                                    logDAO.InserirLog(logMissaoCriada);
                                }
                            }
                        } else if (opcaoMenu == 8) {
                            String nomeItem = JOptionPane.showInputDialog("Digite o nome do item:");

                            String menuModelo = "Escolha o modelo do item:\n\n"
                                    + "1. Cabelo\n"
                                    + "2. Roupa de cima interna\n"
                                    + "3. Roupa de cima externa\n"
                                    + "4. Roupa de baixo\n"
                                    + "5. Calçado\n"
                                    + "6. Acessório";
                            auxiliar = JOptionPane.showInputDialog(menuModelo);
                            int opcaoModelo = Integer.parseInt(auxiliar);
                            String modeloItem = null;

                            if (opcaoModelo == 1) {
                                modeloItem = "CABELO";
                            } else if (opcaoModelo == 2) {
                                modeloItem = "ROUPA DE CIMA INTERNA";
                            } else if (opcaoModelo == 3) {
                                modeloItem = "ROUPA DE CIMA EXTERNA";
                            } else if (opcaoModelo == 4) {
                                modeloItem = "ROUPA DE BAIXO";
                            } else if (opcaoModelo == 5) {
                                modeloItem = "CALCADO";
                            } else if (opcaoModelo == 6) {
                                modeloItem = "ACESSORIO";
                            }

                            auxiliar = JOptionPane.showInputDialog("Digite o valor do item em pontos:");
                            int valorPontos = Integer.parseInt(auxiliar);

                            String menuTipoItem = "Escolha o tipo do item:\n\n"
                                    + "1. Normal\n"
                                    + "2. Exclusivo";
                            auxiliar = JOptionPane.showInputDialog(menuTipoItem);
                            int opcaoTipoItem = Integer.parseInt(auxiliar);
                            String tipoItem = null;

                            if (opcaoTipoItem == 1) {
                                tipoItem = "NORMAL";
                            } else if (opcaoTipoItem == 2) {
                                tipoItem = "EXCLUSIVO";
                            }

                            if (nomeItem == null || nomeItem.trim().isEmpty()) {
                                JOptionPane.showMessageDialog(null, "O nome do item deve ser informado!");
                            } else if (modeloItem == null) {
                                JOptionPane.showMessageDialog(null, "Modelo de item inválido!");
                            } else if (valorPontos < 0) {
                                JOptionPane.showMessageDialog(null,
                                        "O valor do item não pode ser negativo!");
                            } else if (tipoItem == null) {
                                JOptionPane.showMessageDialog(null, "Tipo de item inválido!");
                            } else {
                                idItem = idItem + 1;
                                Item novoItem = new Item(idItem, nomeItem, modeloItem,
                                        valorPontos, tipoItem);
                                String resultado = itemDAO.InserirItem(novoItem);
                                JOptionPane.showMessageDialog(null, resultado);

                                if (resultado.contains("sucesso")) {
                                    idLog = idLog + 1;
                                    Log logItemCriado = new Log(idLog, "CRIAR ITEM",
                                            "Item criado: " + nomeItem,
                                            LocalDate.now(), usuario.getId(), "SUCESSO");
                                    logDAO.InserirLog(logItemCriado);
                                }
                            }
                        } else if (opcaoMenu == 9) {
                            String menuSaida = "O que você deseja fazer?\n\n"
                                    + "1. Encerrar programa\n"
                                    + "2. Entrar com outro usuário";
                            auxiliar = JOptionPane.showInputDialog(menuSaida);
                            int opcaoSaida = Integer.parseInt(auxiliar);

                            if (opcaoSaida == 1) {
                                idLog = idLog + 1;
                                Log logFim = new Log(idLog, "SESSAO", "Sessão encerrada",
                                        LocalDate.now(), usuario.getId(), "SUCESSO");
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
            } catch (DateTimeParseException e) {
                JOptionPane.showMessageDialog(null,
                        "Erro: Digite a data no formato AAAA-MM-DD!");
            } catch (AvatarException | MissaoUsuarioException | UsuarioException e) {
                JOptionPane.showMessageDialog(null, "Erro de validação: " + e.getMessage());
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Erro: " + e.getMessage());
            }
        }

        ConnectionFactory.closeConnection(con);
        JOptionPane.showMessageDialog(null, "Obrigado por usar o TechLeet!");
    }
}
