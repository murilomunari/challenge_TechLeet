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
import br.com.fiap.dto.AvatarException;
import br.com.fiap.dto.Codigo;
import br.com.fiap.dto.Inventario;
import br.com.fiap.dto.Item;
import br.com.fiap.dto.Log;
import br.com.fiap.dto.Missao;
import br.com.fiap.dto.MissaoUsuario;
import br.com.fiap.dto.MissaoUsuarioException;
import br.com.fiap.dto.Parceria;
import br.com.fiap.dto.Usuario;
import br.com.fiap.dto.UsuarioException;

import javax.swing.JOptionPane;
import java.math.BigDecimal;
import java.sql.Connection;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) {
        Connection con = ConnectionFactory.getConnection();

        if (con == null) {
            JOptionPane.showMessageDialog(null, "Não foi possível conectar ao banco de dados!");
            return;
        }

        try {
            UsuarioDAO usuarioDAO = new UsuarioDAO(con);
            AvatarDAO avatarDAO = new AvatarDAO(con);
            ItemDAO itemDAO = new ItemDAO(con);
            InventarioDAO inventarioDAO = new InventarioDAO(con);
            MissaoDAO missaoDAO = new MissaoDAO(con);
            MissaoUsuarioDAO missaoUsuarioDAO = new MissaoUsuarioDAO(con);
            LogDAO logDAO = new LogDAO(con);
            ParceriaDAO parceriaDAO = new ParceriaDAO(con);
            CodigoDAO codigoDAO = new CodigoDAO(con);

            cadastrarDadosIniciais(itemDAO, parceriaDAO, codigoDAO);
            executar(usuarioDAO, avatarDAO, itemDAO, inventarioDAO,
                    missaoDAO, missaoUsuarioDAO, logDAO);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Ocorreu um erro na aplicação: " + e.getMessage());
        }

        ConnectionFactory.closeConnection(con);
        JOptionPane.showMessageDialog(null, "Obrigado por usar o TechLeet!");
    }

    private static void cadastrarDadosIniciais(ItemDAO itemDAO,
                                                ParceriaDAO parceriaDAO,
                                                CodigoDAO codigoDAO) {
        ArrayList<Item> itens = itemDAO.ListarItem();
        ArrayList<Parceria> parcerias = parceriaDAO.ListarParceria();
        ArrayList<Codigo> codigos = codigoDAO.ListarCodigo();

        if (itens == null || parcerias == null || codigos == null) {
            System.out.println("Não foi possível verificar os dados iniciais.");
            return;
        }

        ArrayList<Item> itensIniciais = new ArrayList<>();
        itensIniciais.add(new Item(0, "Cabelo Básico", "CABELO", 0, "NORMAL"));
        itensIniciais.add(new Item(0, "Camiseta Básica", "ROUPA DE CIMA INTERNA", 0, "NORMAL"));
        itensIniciais.add(new Item(0, "Jaqueta Básica", "ROUPA DE CIMA EXTERNA", 0, "NORMAL"));
        itensIniciais.add(new Item(0, "Calça Básica", "ROUPA DE BAIXO", 0, "NORMAL"));
        itensIniciais.add(new Item(0, "Tênis Básico", "CALCADO", 0, "NORMAL"));
        itensIniciais.add(new Item(0, "Pulseira Básica", "ACESSORIO", 0, "NORMAL"));
        itensIniciais.add(new Item(0, "Cabelo Neon", "CABELO", 60, "NORMAL"));
        itensIniciais.add(new Item(0, "Camiseta Tech", "ROUPA DE CIMA INTERNA", 80, "NORMAL"));
        itensIniciais.add(new Item(0, "Jaqueta Gamer", "ROUPA DE CIMA EXTERNA", 120, "NORMAL"));
        itensIniciais.add(new Item(0, "Calça Cargo", "ROUPA DE BAIXO", 90, "NORMAL"));
        itensIniciais.add(new Item(0, "Tênis Neon", "CALCADO", 100, "NORMAL"));
        itensIniciais.add(new Item(0, "Óculos Digital", "ACESSORIO", 70, "NORMAL"));

        for (Item itemInicial : itensIniciais) {
            boolean itemJaCadastrado = itens.stream()
                    .anyMatch(item -> item.getNome().equalsIgnoreCase(itemInicial.getNome()));

            if (!itemJaCadastrado) {
                itemInicial.setId(maiorId(itens, Item::getId) + 1);
                String resultado = itemDAO.InserirItem(itemInicial);
                System.out.println(resultado);

                if (resultado.contains("sucesso")) {
                    itens.add(itemInicial);
                }
            }
        }

        Item itemCodigo = null;
        for (Item item : itens) {
            if ("Boné TechLeet".equalsIgnoreCase(item.getNome())) {
                itemCodigo = item;
            }
        }

        if (itemCodigo == null) {
            int idItem = maiorId(itens, Item::getId) + 1;
            itemCodigo = new Item(idItem, "Boné TechLeet",
                    "ACESSORIO", 0, "EXCLUSIVO");
            String resultadoItem = itemDAO.InserirItem(itemCodigo);
            System.out.println(resultadoItem);

            if (!resultadoItem.contains("sucesso")) {
                return;
            }
        }

        Parceria parceriaCodigo = null;
        for (Parceria parceria : parcerias) {
            if ("TechStore".equalsIgnoreCase(parceria.getNome())) {
                parceriaCodigo = parceria;
            }
        }

        if (parceriaCodigo == null) {
            int idParceria = maiorId(parcerias, Parceria::getId) + 1;
            parceriaCodigo = new Parceria(idParceria, "TechStore",
                    "MARCA", "ATIVA", new BigDecimal("1000.00"),
                    LocalDate.now(), LocalDate.now().plusYears(1));
            String resultadoParceria = parceriaDAO.InserirParceria(parceriaCodigo);
            System.out.println(resultadoParceria);

            if (!resultadoParceria.contains("sucesso")) {
                return;
            }
        }

        Codigo codigoInicial = null;
        for (Codigo codigo : codigos) {
            if ("TECHLEET2026".equalsIgnoreCase(codigo.getCodigoResgate())) {
                codigoInicial = codigo;
            }
        }

        if (codigoInicial == null) {
            int idCodigo = maiorId(codigos, Codigo::getId) + 1;
            codigoInicial = new Codigo(idCodigo, "TECHLEET2026",
                    "DISPONIVEL", LocalDate.now().plusYears(1), null,
                    itemCodigo.getId(), parceriaCodigo.getId());
            System.out.println(codigoDAO.InserirCodigo(codigoInicial));
        }

        System.out.println("\n=== DADOS INICIAIS CADASTRADOS ===");

        itens = itemDAO.ListarItem();
        if (itens != null) {
            for (Item item : itens) {
                if ("Boné TechLeet".equalsIgnoreCase(item.getNome())) {
                    System.out.println("\nItem: " + item.getNome());
                    System.out.println("Modelo: " + item.getModelo());
                    System.out.println("Tipo: " + item.getTipo());
                }
            }
        }

        parcerias = parceriaDAO.ListarParceria();
        if (parcerias != null) {
            for (Parceria parceria : parcerias) {
                if ("TechStore".equalsIgnoreCase(parceria.getNome())) {
                    System.out.println("\nParceria: " + parceria.getNome());
                    System.out.println("Tipo: " + parceria.getTipo());
                    System.out.println("Status: " + parceria.getStatus());
                }
            }
        }

        codigos = codigoDAO.ListarCodigo();
        if (codigos != null) {
            for (Codigo codigo : codigos) {
                if ("TECHLEET2026".equalsIgnoreCase(codigo.getCodigoResgate())) {
                    System.out.println("\nCódigo: " + codigo.getCodigoResgate());
                    System.out.println("Status: " + codigo.getStatus());
                    System.out.println("Validade: " + codigo.getDataValidade());
                }
            }
        }
    }

    private static <T> int maiorId(ArrayList<T> registros, ToIntFunction<T> obterId) {
        return listaSegura(registros).stream()
                .mapToInt(obterId)
                .max()
                .orElse(0);
    }

    private static <T> ArrayList<T> listaSegura(ArrayList<T> registros) {
        return registros == null ? new ArrayList<>() : registros;
    }

    private static void executar(UsuarioDAO usuarioDAO, AvatarDAO avatarDAO,
                                 ItemDAO itemDAO, InventarioDAO inventarioDAO,
                                 MissaoDAO missaoDAO, MissaoUsuarioDAO missaoUsuarioDAO,
                                 LogDAO logDAO) {
        JOptionPane.showMessageDialog(null, "Bem-vindo ao TechLeet - Sistema de Gamificação!");

        boolean continuar = true;
        while (continuar) {
            try {
                Usuario usuario = acessar(usuarioDAO, avatarDAO, itemDAO,
                        inventarioDAO, logDAO);
                if (usuario != null) {
                    continuar = !executarMenuPrincipal(usuario, usuarioDAO, avatarDAO,
                            itemDAO, inventarioDAO, missaoDAO, missaoUsuarioDAO, logDAO);
                } else {
                    continuar = false;
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Erro: Digite um número válido!");
            } catch (DateTimeParseException e) {
                JOptionPane.showMessageDialog(null,
                        "Erro: Digite a data no formato DD-MM-AAAA!");
            } catch (AvatarException | MissaoUsuarioException | UsuarioException e) {
                JOptionPane.showMessageDialog(null, "Erro de validação: " + e.getMessage());
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Erro: " + e.getMessage());
            }
        }
    }

    private static Usuario acessar(UsuarioDAO usuarioDAO, AvatarDAO avatarDAO,
                                   ItemDAO itemDAO, InventarioDAO inventarioDAO,
                                   LogDAO logDAO) {
        Usuario usuario = null;

        while (usuario == null) {
            String menuAcesso = "=== ACESSO ===\n\n"
                    + "1. Cadastrar usuário\n"
                    + "2. Entrar\n"
                    + "3. Encerrar programa";
            int opcaoAcesso = Integer.parseInt(JOptionPane.showInputDialog(menuAcesso));

            switch (opcaoAcesso) {
                case 1:
                    usuario = cadastrarUsuario(usuarioDAO, logDAO);
                    break;
                case 2:
                    usuario = entrar(usuarioDAO, logDAO);
                    break;
                case 3:
                    return null;
                default:
                    JOptionPane.showMessageDialog(null, "Opção inválida!");
            }
        }

        ArrayList<Inventario> inventarios = inventarioDAO.ListarInventario();
        ArrayList<Item> itens = itemDAO.ListarItem();

        if (inventarios != null && itens != null) {
            int idUsuario = usuario.getId();
            HashSet<Integer> idsItensPossuidos = inventarios.stream()
                    .filter(inventario -> inventario.getIdUsuario() == idUsuario)
                    .map(Inventario::getIdItem)
                    .collect(Collectors.toCollection(HashSet::new));

            Avatar avatar = buscarAvatar(usuario, avatarDAO);
            if (avatar != null) {
                idsItensPossuidos.add(avatar.getIdCabelo());
                idsItensPossuidos.add(avatar.getIdRoupaCimaInt());
                idsItensPossuidos.add(avatar.getIdRoupaCimaExt());
                idsItensPossuidos.add(avatar.getIdRoupaBaixo());
                idsItensPossuidos.add(avatar.getIdCalcado());
                idsItensPossuidos.add(avatar.getIdAcessorio());
            }

            HashSet<String> nomesItensPadrao = new HashSet<>();
            nomesItensPadrao.add("Cabelo Básico");
            nomesItensPadrao.add("Camiseta Básica");
            nomesItensPadrao.add("Jaqueta Básica");
            nomesItensPadrao.add("Calça Básica");
            nomesItensPadrao.add("Tênis Básico");
            nomesItensPadrao.add("Pulseira Básica");

            int itensRecebidos = 0;
            for (Item item : itens) {
                if (nomesItensPadrao.contains(item.getNome())
                        && !idsItensPossuidos.contains(item.getId())) {
                    int idInventario = maiorId(inventarios, Inventario::getId) + 1;
                    Inventario novoItemInventario = new Inventario(idInventario,
                            idUsuario, item.getId(), "RESGATE", LocalDate.now());
                    String resultado = inventarioDAO.InserirInventario(novoItemInventario);

                    if (resultado.contains("sucesso")) {
                        inventarios.add(novoItemInventario);
                        idsItensPossuidos.add(item.getId());
                        itensRecebidos++;
                    }
                }
            }

            if (itensRecebidos > 0) {
                JOptionPane.showMessageDialog(null,
                        "Você recebeu " + itensRecebidos + " itens do kit inicial!");
            }
        }

        return usuario;
    }

    private static Usuario cadastrarUsuario(UsuarioDAO usuarioDAO, LogDAO logDAO) {
        String nome = JOptionPane.showInputDialog("Digite seu nome:");
        String email = JOptionPane.showInputDialog("Digite seu email:");
        String senha = JOptionPane.showInputDialog("Digite sua senha:");
        LocalDate dataNascimento = LocalDate.parse(
                JOptionPane.showInputDialog("Digite sua data de nascimento (DD-MM-AAAA):"),
                DateTimeFormatter.ofPattern("dd-MM-yyyy"));

        if (dataNascimento.isAfter(LocalDate.now())) {
            throw new UsuarioException("A data de nascimento não pode estar no futuro.");
        }

        boolean emailCadastrado = listaSegura(usuarioDAO.listaTodos()).stream()
                .anyMatch(usuario -> usuario.getEmail().equalsIgnoreCase(email));

        if (emailCadastrado) {
            JOptionPane.showMessageDialog(null, "Esse email já está cadastrado!");
            return null;
        }

        int idUsuario = maiorId(usuarioDAO.listaTodos(), Usuario::getId) + 1;
        Usuario usuario = new Usuario(idUsuario, nome, email, senha, dataNascimento, 20);
        String resultado = usuarioDAO.InserirUsuario(usuario);
        JOptionPane.showMessageDialog(null, resultado);

        if (!resultado.contains("sucesso")) {
            return null;
        }

        JOptionPane.showMessageDialog(null,
                "Sua conta foi criada com 20 pontos iniciais!");
        registrarLog(logDAO, "CADASTRO", "Usuário cadastrado: " + email, usuario);
        return usuario;
    }

    private static Usuario entrar(UsuarioDAO usuarioDAO, LogDAO logDAO) {
        String email = JOptionPane.showInputDialog("Digite seu email:");
        String senha = JOptionPane.showInputDialog("Digite sua senha:");

        Usuario usuario = listaSegura(usuarioDAO.listaTodos()).stream()
                .filter(cadastrado -> cadastrado.getEmail().equalsIgnoreCase(email))
                .filter(cadastrado -> cadastrado.getSenha().equals(senha))
                .findFirst()
                .orElse(null);

        if (usuario == null) {
            JOptionPane.showMessageDialog(null, "Email ou senha inválidos!");
            return null;
        }

        registrarLog(logDAO, "SESSAO", "Login realizado", usuario);
        JOptionPane.showMessageDialog(null, "Login realizado com sucesso!");
        return usuario;
    }

    private static boolean executarMenuPrincipal(Usuario usuario, UsuarioDAO usuarioDAO,
                                                 AvatarDAO avatarDAO, ItemDAO itemDAO,
                                                 InventarioDAO inventarioDAO, MissaoDAO missaoDAO,
                                                 MissaoUsuarioDAO missaoUsuarioDAO,
                                                 LogDAO logDAO) {
        while (true) {
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
            int opcaoMenu = Integer.parseInt(JOptionPane.showInputDialog(menuPrincipal));

            switch (opcaoMenu) {
                case 1:
                    criarAvatar(usuario, avatarDAO, logDAO);
                    break;
                case 2:
                    equiparItem(usuario, avatarDAO, itemDAO, inventarioDAO, logDAO);
                    break;
                case 3:
                    fazerMissao(usuario, usuarioDAO, missaoDAO, missaoUsuarioDAO, logDAO);
                    break;
                case 4:
                    gastarPontos(usuario, usuarioDAO, itemDAO, inventarioDAO, logDAO);
                    break;
                case 5:
                    mostrarInformacoes(usuario, avatarDAO, itemDAO,
                            inventarioDAO, missaoUsuarioDAO);
                    break;
                case 6:
                    if (deletarUsuario(usuario, usuarioDAO)) {
                        return false;
                    }
                    break;
                case 7:
                    criarMissao(usuario, missaoDAO, logDAO);
                    break;
                case 8:
                    criarItem(usuario, itemDAO, logDAO);
                    break;
                case 9:
                    int opcaoSaida = Integer.parseInt(JOptionPane.showInputDialog(
                            "O que você deseja fazer?\n\n"
                                    + "1. Encerrar programa\n"
                                    + "2. Entrar com outro usuário"));

                    if (opcaoSaida == 1) {
                        registrarLog(logDAO, "SESSAO", "Sessão encerrada", usuario);
                        return true;
                    } else if (opcaoSaida == 2) {
                        return false;
                    } else {
                        JOptionPane.showMessageDialog(null, "Opção inválida!");
                    }
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "Opção inválida!");
            }
        }
    }

    private static void criarAvatar(Usuario usuario, AvatarDAO avatarDAO, LogDAO logDAO) {
        Avatar avatar = buscarAvatar(usuario, avatarDAO);

        if (avatar != null) {
            JOptionPane.showMessageDialog(null,
                    "Você já possui o avatar '" + avatar.getNome() + "'!");
            return;
        }

        String nomeAvatar = JOptionPane.showInputDialog("Digite o nome do seu Avatar:");
        int idAvatar = maiorId(avatarDAO.ListarAvatar(), Avatar::getId) + 1;
        avatar = new Avatar(idAvatar, nomeAvatar,
                0, 0, 0, 0, 0, 0, usuario.getId());

        String resultado = avatarDAO.InserirAvatar(avatar);
        JOptionPane.showMessageDialog(null, resultado);

        if (resultado.contains("sucesso")) {
            registrarLog(logDAO, "AVATAR", "Avatar criado: " + nomeAvatar, usuario);
        }
    }

    private static Avatar buscarAvatar(Usuario usuario, AvatarDAO avatarDAO) {
        return listaSegura(avatarDAO.ListarAvatar()).stream()
                .filter(avatar -> avatar.getIdUsuario() == usuario.getId())
                .findFirst()
                .orElse(null);
    }

    private static void equiparItem(Usuario usuario, AvatarDAO avatarDAO,
                                    ItemDAO itemDAO, InventarioDAO inventarioDAO,
                                    LogDAO logDAO) {
        Avatar avatar = buscarAvatar(usuario, avatarDAO);

        if (avatar == null) {
            JOptionPane.showMessageDialog(null, "Crie um avatar primeiro! (Opção 1)");
            return;
        }

        ArrayList<Inventario> inventariosUsuario =
                listaSegura(inventarioDAO.ListarInventario()).stream()
                .filter(inventario -> inventario.getIdUsuario() == usuario.getId())
                .collect(Collectors.toCollection(ArrayList::new));

        HashSet<Integer> idsItensPossuidos = inventariosUsuario.stream()
                .map(Inventario::getIdItem)
                .collect(Collectors.toCollection(HashSet::new));

        HashSet<String> modelosEquipaveis = new HashSet<>();
        modelosEquipaveis.add("CABELO");
        modelosEquipaveis.add("ROUPA DE CIMA INTERNA");
        modelosEquipaveis.add("ROUPA DE CIMA EXTERNA");
        modelosEquipaveis.add("ROUPA DE BAIXO");
        modelosEquipaveis.add("CALCADO");
        modelosEquipaveis.add("ACESSORIO");

        ArrayList<Item> itensEquipaveis = listaSegura(itemDAO.ListarItem()).stream()
                .filter(item -> idsItensPossuidos.contains(item.getId()))
                .filter(item -> item.getModelo() != null
                        && modelosEquipaveis.contains(item.getModelo().toUpperCase()))
                .collect(Collectors.toCollection(ArrayList::new));

        if (itensEquipaveis.isEmpty()) {
            JOptionPane.showMessageDialog(null,
                    "Você não possui itens disponíveis para equipar!");
            return;
        }

        String opcoes = "";
        for (int indice = 0; indice < itensEquipaveis.size(); indice++) {
            Item item = itensEquipaveis.get(indice);
            opcoes = opcoes + (indice + 1) + ". "
                    + item.getNome() + " - " + item.getModelo() + "\n";
        }
        int opcaoItem = Integer.parseInt(JOptionPane.showInputDialog(
                "Escolha um item para equipar:\n\n" + opcoes));

        if (opcaoItem < 1 || opcaoItem > itensEquipaveis.size()) {
            JOptionPane.showMessageDialog(null, "Opção inválida!");
            return;
        }

        Item itemEscolhido = itensEquipaveis.get(opcaoItem - 1);
        Inventario itemNoInventario = inventariosUsuario.stream()
                .filter(inventario -> inventario.getIdItem() == itemEscolhido.getId())
                .findFirst()
                .orElse(null);
        avatar.equiparItem(itemEscolhido);
        String resultado = avatarDAO.AlterarAvatar(avatar);

        if (resultado.contains("sucesso") && itemNoInventario != null) {
            String resultadoInventario = inventarioDAO.DeletarInventario(itemNoInventario);

            if (!resultadoInventario.contains("sucesso")) {
                JOptionPane.showMessageDialog(null, resultadoInventario);
                return;
            }

            registrarLog(logDAO, "EQUIPAR", "Item equipado: " + itemEscolhido.getNome(), usuario);
            JOptionPane.showMessageDialog(null, "Item equipado com sucesso!");
        } else {
            JOptionPane.showMessageDialog(null, resultado);
        }
    }

    private static void fazerMissao(Usuario usuario, UsuarioDAO usuarioDAO,
                                    MissaoDAO missaoDAO,
                                    MissaoUsuarioDAO missaoUsuarioDAO,
                                    LogDAO logDAO) {
        ArrayList<Missao> missoes = listaSegura(missaoDAO.ListaMissao());

        if (missoes.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Não há missões cadastradas no banco!");
            return;
        }

        String opcoes = "";
        for (int indice = 0; indice < missoes.size(); indice++) {
            Missao missao = missoes.get(indice);
            opcoes = opcoes + (indice + 1) + ". " + missao.getTitulo()
                    + " (" + missao.getPontos() + " pontos)\n";
        }
        int opcaoMissao = Integer.parseInt(JOptionPane.showInputDialog(
                "Escolha uma missão:\n\n" + opcoes));

        if (opcaoMissao < 1 || opcaoMissao > missoes.size()) {
            JOptionPane.showMessageDialog(null, "Opção inválida!");
            return;
        }

        Missao missao = missoes.get(opcaoMissao - 1);
        boolean concluida = listaSegura(missaoUsuarioDAO.ListarMissaoUsuario()).stream()
                .anyMatch(registro -> registro.getIdUsuario() == usuario.getId()
                        && registro.getIdMissao() == missao.getId()
                        && registro.getDataRealizacao() != null);

        if (concluida) {
            JOptionPane.showMessageDialog(null, "Você já concluiu essa missão!");
            return;
        }

        LocalDate dataInicio = LocalDate.now();
        LocalDate dataFim = calcularDataFim(dataInicio, missao.getTipo());
        int idMissaoUsuario = maiorId(missaoUsuarioDAO.ListarMissaoUsuario(),
                MissaoUsuario::getId) + 1;
        MissaoUsuario registro = new MissaoUsuario(idMissaoUsuario,
                usuario.getId(), missao.getId(), "DISPONIVEL",
                null, dataInicio, dataFim);
        registro.concluir();

        String resultado = missaoUsuarioDAO.InserirMissaoUsuario(registro);
        if (!resultado.contains("sucesso")) {
            JOptionPane.showMessageDialog(null, resultado);
            return;
        }

        usuario.adicionarPontos(missao.getPontos());
        usuarioDAO.AlterarUsuario(usuario);
        registrarLog(logDAO, "MISSAO",
                "Missão concluída: " + missao.getTitulo()
                        + " (+" + missao.getPontos() + " pts)", usuario);

        JOptionPane.showMessageDialog(null,
                "Missão concluída!\n\nVocê ganhou "
                        + missao.getPontos() + " pontos!\nTotal: "
                        + usuario.getPontos() + " pontos");
    }

    private static LocalDate calcularDataFim(LocalDate dataInicio, String tipoMissao) {
        String tipo = tipoMissao == null ? "" : tipoMissao.toUpperCase();

        switch (tipo) {
            case "DIARIA":
                return dataInicio;
            case "SEMANAL":
                return dataInicio.plusDays(7);
            default:
                return dataInicio.plusDays(30);
        }
    }

    private static void gastarPontos(Usuario usuario, UsuarioDAO usuarioDAO,
                                     ItemDAO itemDAO, InventarioDAO inventarioDAO,
                                     LogDAO logDAO) {
        ArrayList<Item> itensDisponiveis = listaSegura(itemDAO.ListarItem()).stream()
                .filter(item -> item.getValorPontos() > 0)
                .collect(Collectors.toCollection(ArrayList::new));

        if (itensDisponiveis.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Não há itens disponíveis para compra!");
            return;
        }

        String opcoes = "";
        for (Item item : itensDisponiveis) {
            opcoes = opcoes + "ID " + item.getId() + " - " + item.getNome()
                    + " (" + item.getValorPontos() + " pontos)\n";
        }
        int idItemEscolhido = Integer.parseInt(JOptionPane.showInputDialog(
                "Seus pontos: " + usuario.getPontos() + "\n\n"
                        + "Digite o ID do item que deseja resgatar:\n\n" + opcoes));

        Item itemComprado = itensDisponiveis.stream()
                .filter(item -> item.getId() == idItemEscolhido)
                .findFirst()
                .orElse(null);

        if (itemComprado == null) {
            JOptionPane.showMessageDialog(null, "ID de item inválido!");
            return;
        }
        if (usuario.getPontos() < itemComprado.getValorPontos()) {
            JOptionPane.showMessageDialog(null, "Você não tem pontos suficientes!");
            return;
        }

        usuario.gastarPontos(itemComprado.getValorPontos());
        usuarioDAO.AlterarUsuario(usuario);

        int idInventario = maiorId(inventarioDAO.ListarInventario(), Inventario::getId) + 1;
        Inventario inventario = new Inventario(idInventario,
                usuario.getId(), itemComprado.getId(), "COMPRA", LocalDate.now());
        inventarioDAO.InserirInventario(inventario);

        registrarLog(logDAO, "COMPRA",
                "Item comprado: " + itemComprado.getNome()
                        + " (-" + itemComprado.getValorPontos() + " pts)", usuario);

        JOptionPane.showMessageDialog(null,
                "Item comprado com sucesso!\n\nSaldo: "
                        + usuario.getPontos() + " pontos");
    }

    private static void mostrarInformacoes(Usuario usuario, AvatarDAO avatarDAO,
                                           ItemDAO itemDAO, InventarioDAO inventarioDAO,
                                           MissaoUsuarioDAO missaoUsuarioDAO) {
        Avatar avatar = buscarAvatar(usuario, avatarDAO);
        ArrayList<Item> itens = listaSegura(itemDAO.ListarItem());
        ArrayList<Inventario> inventarios = listaSegura(inventarioDAO.ListarInventario());
        ArrayList<MissaoUsuario> missoesUsuario =
                listaSegura(missaoUsuarioDAO.ListarMissaoUsuario());

        HashMap<Integer, Item> itensPorId = itens.stream()
                .collect(Collectors.toMap(
                        Item::getId,
                        item -> item,
                        (itemExistente, itemDuplicado) -> itemExistente,
                        HashMap::new));

        ArrayList<Inventario> inventarioUsuario = inventarios.stream()
                .filter(inventario -> inventario.getIdUsuario() == usuario.getId())
                .collect(Collectors.toCollection(ArrayList::new));

        String linhasInventario = inventarioUsuario.stream()
                .map(inventario -> {
                    Item item = itensPorId.get(inventario.getIdItem());
                    String nomeItem = item == null
                            ? "Item #" + inventario.getIdItem()
                            : item.getNome();
                    return "  - " + nomeItem + " (" + inventario.getOrigem() + ")\n";
                })
                .collect(Collectors.joining());

        if (linhasInventario.isEmpty()) {
            linhasInventario = "  Nenhum item adquirido.\n";
        }

        long missoesConcluidas = missoesUsuario.stream()
                .filter(registro -> registro.getIdUsuario() == usuario.getId())
                .filter(registro -> registro.getDataRealizacao() != null)
                .count();

        String nomeAvatar = avatar == null ? "Não criado" : avatar.getNome();
        String info = "=== SUAS INFORMAÇÕES ===\n\n"
                + "ID: " + usuario.getId() + "\n"
                + "NOME: " + usuario.getNome() + "\n"
                + "EMAIL: " + usuario.getEmail() + "\n"
                + "DATA DE NASCIMENTO: "
                + usuario.getDataNascimento().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))
                + "\n"
                + "PONTOS: " + usuario.getPontos() + "\n\n"
                + "AVATAR: " + nomeAvatar + "\n"
                + "\nINVENTÁRIO:\n" + linhasInventario
                + "\nMISSÕES CONCLUÍDAS: " + missoesConcluidas;

        JOptionPane.showMessageDialog(null, info);
    }

    private static boolean deletarUsuario(Usuario usuario, UsuarioDAO usuarioDAO) {
        int opcaoDeletar = Integer.parseInt(JOptionPane.showInputDialog(
                "Tem certeza que deseja deletar seu usuário?\n\n"
                        + "1. Sim\n"
                        + "2. Não"));

        switch (opcaoDeletar) {
            case 1:
                String resultado = usuarioDAO.DeletarUsuario(usuario);
                JOptionPane.showMessageDialog(null, resultado);
                return resultado.contains("sucesso");
            case 2:
                return false;
            default:
                JOptionPane.showMessageDialog(null, "Opção inválida!");
                return false;
        }
    }

    private static void criarMissao(Usuario usuario, MissaoDAO missaoDAO, LogDAO logDAO) {
        HashMap<Integer, String> tiposMissao = new HashMap<>();
        tiposMissao.put(1, "DIARIA");
        tiposMissao.put(2, "SEMANAL");
        tiposMissao.put(3, "ESPECIAL");

        String titulo = JOptionPane.showInputDialog("Digite o título da missão:");
        String descricao = JOptionPane.showInputDialog("Digite a descrição da missão:");
        int pontosMissao = Integer.parseInt(JOptionPane.showInputDialog(
                "Digite a quantidade de pontos da missão:"));

        String menuTipoMissao = "Escolha o tipo da missão:\n\n"
                + "1. Diária\n"
                + "2. Semanal\n"
                + "3. Especial";
        String tipoMissao = tiposMissao.get(Integer.parseInt(
                JOptionPane.showInputDialog(menuTipoMissao)));

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
            int idMissao = maiorId(missaoDAO.ListaMissao(), Missao::getId) + 1;
            Missao novaMissao = new Missao(idMissao, titulo, descricao,
                    pontosMissao, tipoMissao);
            String resultado = missaoDAO.InserirMissao(novaMissao);
            JOptionPane.showMessageDialog(null, resultado);

            if (resultado.contains("sucesso")) {
                registrarLog(logDAO, "CRIAR MISSAO", "Missão criada: " + titulo, usuario);
            }
        }
    }

    private static void criarItem(Usuario usuario, ItemDAO itemDAO, LogDAO logDAO) {
        HashMap<Integer, String> modelosItem = new HashMap<>();
        modelosItem.put(1, "CABELO");
        modelosItem.put(2, "ROUPA DE CIMA INTERNA");
        modelosItem.put(3, "ROUPA DE CIMA EXTERNA");
        modelosItem.put(4, "ROUPA DE BAIXO");
        modelosItem.put(5, "CALCADO");
        modelosItem.put(6, "ACESSORIO");

        HashMap<Integer, String> tiposItem = new HashMap<>();
        tiposItem.put(1, "NORMAL");
        tiposItem.put(2, "EXCLUSIVO");

        String nomeItem = JOptionPane.showInputDialog("Digite o nome do item:");

        String menuModelo = "Escolha o modelo do item:\n\n"
                + "1. Cabelo\n"
                + "2. Roupa de cima interna\n"
                + "3. Roupa de cima externa\n"
                + "4. Roupa de baixo\n"
                + "5. Calçado\n"
                + "6. Acessório";
        String modeloItem = modelosItem.get(Integer.parseInt(
                JOptionPane.showInputDialog(menuModelo)));

        int valorPontos = Integer.parseInt(JOptionPane.showInputDialog(
                "Digite o valor do item em pontos:"));

        String menuTipoItem = "Escolha o tipo do item:\n\n"
                + "1. Normal\n"
                + "2. Exclusivo";
        String tipoItem = tiposItem.get(Integer.parseInt(
                JOptionPane.showInputDialog(menuTipoItem)));

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
            int idItem = maiorId(itemDAO.ListarItem(), Item::getId) + 1;
            Item novoItem = new Item(idItem, nomeItem, modeloItem,
                    valorPontos, tipoItem);
            String resultado = itemDAO.InserirItem(novoItem);
            JOptionPane.showMessageDialog(null, resultado);

            if (resultado.contains("sucesso")) {
                registrarLog(logDAO, "CRIAR ITEM", "Item criado: " + nomeItem, usuario);
            }
        }
    }

    private static void registrarLog(LogDAO logDAO, String acao,
                                     String descricao, Usuario usuario) {
        int idLog = maiorId(logDAO.ListarLog(), Log::getId) + 1;
        Log log = new Log(idLog, acao, descricao,
                LocalDate.now(), usuario.getId(), "SUCESSO");
        logDAO.InserirLog(log);
    }

}
