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
import java.util.HashMap;
import java.util.HashSet;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;

public class Main {

    private final UsuarioDAO usuarioDAO;
    private final AvatarDAO avatarDAO;
    private final ItemDAO itemDAO;
    private final InventarioDAO inventarioDAO;
    private final MissaoDAO missaoDAO;
    private final MissaoUsuarioDAO missaoUsuarioDAO;
    private final LogDAO logDAO;

    private final HashSet<String> modelosEquipaveis = new HashSet<>();
    private final HashMap<Integer, String> tiposMissao = new HashMap<>();
    private final HashMap<Integer, String> modelosItem = new HashMap<>();
    private final HashMap<Integer, String> tiposItem = new HashMap<>();

    private boolean continuar = true;
    private int idUsuario;
    private int idAvatar;
    private int idItem;
    private int idInventario;
    private int idMissao;
    private int idMissaoUsuario;
    private int idLog;

    public Main(Connection con) {
        usuarioDAO = new UsuarioDAO(con);
        avatarDAO = new AvatarDAO(con);
        itemDAO = new ItemDAO(con);
        inventarioDAO = new InventarioDAO(con);
        missaoDAO = new MissaoDAO(con);
        missaoUsuarioDAO = new MissaoUsuarioDAO(con);
        logDAO = new LogDAO(con);

        configurarColecoes();
        carregarUltimosIds();
    }

    public static void main(String[] args) {
        Connection con = ConnectionFactory.getConnection();

        if (con == null) {
            JOptionPane.showMessageDialog(null, "Não foi possível conectar ao banco de dados!");
            return;
        }

        try {
            new Main(con).executar();
        } finally {
            ConnectionFactory.closeConnection(con);
            JOptionPane.showMessageDialog(null, "Obrigado por usar o TechLeet!");
        }
    }

    private void configurarColecoes() {
        modelosEquipaveis.add("CABELO");
        modelosEquipaveis.add("ROUPA DE CIMA INTERNA");
        modelosEquipaveis.add("ROUPA DE CIMA EXTERNA");
        modelosEquipaveis.add("ROUPA DE BAIXO");
        modelosEquipaveis.add("CALCADO");
        modelosEquipaveis.add("ACESSORIO");

        tiposMissao.put(1, "DIARIA");
        tiposMissao.put(2, "SEMANAL");
        tiposMissao.put(3, "ESPECIAL");

        modelosItem.put(1, "CABELO");
        modelosItem.put(2, "ROUPA DE CIMA INTERNA");
        modelosItem.put(3, "ROUPA DE CIMA EXTERNA");
        modelosItem.put(4, "ROUPA DE BAIXO");
        modelosItem.put(5, "CALCADO");
        modelosItem.put(6, "ACESSORIO");

        tiposItem.put(1, "NORMAL");
        tiposItem.put(2, "EXCLUSIVO");
    }

    private void carregarUltimosIds() {
        idUsuario = maiorId(usuarioDAO.listaTodos(), Usuario::getId);
        idAvatar = maiorId(avatarDAO.ListarAvatar(), Avatar::getId);
        idInventario = maiorId(inventarioDAO.ListarInventario(), Inventario::getId);
        idItem = maiorId(itemDAO.ListarItem(), Item::getId);
        idMissao = maiorId(missaoDAO.ListaMissao(), Missao::getId);
        idMissaoUsuario = maiorId(missaoUsuarioDAO.ListarMissaoUsuario(), MissaoUsuario::getId);
        idLog = maiorId(logDAO.ListarLog(), Log::getId);
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

    private void executar() {
        JOptionPane.showMessageDialog(null, "Bem-vindo ao TechLeet - Sistema de Gamificação!");

        while (continuar) {
            try {
                Usuario usuario = acessar();
                if (usuario != null) {
                    executarMenuPrincipal(usuario);
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
    }

    private Usuario acessar() {
        Usuario usuario = null;

        while (usuario == null && continuar) {
            String menuAcesso = "=== ACESSO ===\n\n"
                    + "1. Cadastrar usuário\n"
                    + "2. Entrar\n"
                    + "3. Encerrar programa";
            int opcaoAcesso = lerInteiro(menuAcesso);

            switch (opcaoAcesso) {
                case 1:
                    usuario = cadastrarUsuario();
                    break;
                case 2:
                    usuario = entrar();
                    break;
                case 3:
                    continuar = false;
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "Opção inválida!");
            }
        }

        return usuario;
    }

    private Usuario cadastrarUsuario() {
        String nome = JOptionPane.showInputDialog("Digite seu nome:");
        String email = JOptionPane.showInputDialog("Digite seu email:");
        String senha = JOptionPane.showInputDialog("Digite sua senha:");
        LocalDate dataNascimento = LocalDate.parse(JOptionPane.showInputDialog(
                "Digite sua data de nascimento (AAAA-MM-DD):"));

        if (dataNascimento.isAfter(LocalDate.now())) {
            throw new UsuarioException("A data de nascimento não pode estar no futuro.");
        }

        boolean emailCadastrado = listaSegura(usuarioDAO.listaTodos()).stream()
                .anyMatch(usuario -> usuario.getEmail().equalsIgnoreCase(email));

        if (emailCadastrado) {
            JOptionPane.showMessageDialog(null, "Esse email já está cadastrado!");
            return null;
        }

        Usuario usuario = new Usuario(++idUsuario, nome, email, senha, dataNascimento, 0);
        String resultado = usuarioDAO.InserirUsuario(usuario);
        JOptionPane.showMessageDialog(null, resultado);

        if (!resultado.contains("sucesso")) {
            return null;
        }

        registrarLog("CADASTRO", "Usuário cadastrado: " + email, usuario);
        return usuario;
    }

    private Usuario entrar() {
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

        registrarLog("SESSAO", "Login realizado", usuario);
        JOptionPane.showMessageDialog(null, "Login realizado com sucesso!");
        return usuario;
    }

    private void executarMenuPrincipal(Usuario usuario) {
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
            int opcaoMenu = lerInteiro(menuPrincipal);

            switch (opcaoMenu) {
                case 1:
                    criarAvatar(usuario);
                    break;
                case 2:
                    equiparItem(usuario);
                    break;
                case 3:
                    fazerMissao(usuario);
                    break;
                case 4:
                    gastarPontos(usuario);
                    break;
                case 5:
                    mostrarInformacoes(usuario);
                    break;
                case 6:
                    continuarMenu = !deletarUsuario(usuario);
                    break;
                case 7:
                    criarMissao(usuario);
                    break;
                case 8:
                    criarItem(usuario);
                    break;
                case 9:
                    continuarMenu = processarSaida(usuario);
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "Opção inválida!");
            }
        }
    }

    private void criarAvatar(Usuario usuario) {
        Avatar avatar = buscarAvatar(usuario);

        if (avatar != null) {
            JOptionPane.showMessageDialog(null,
                    "Você já possui o avatar '" + avatar.getNome() + "'!");
            return;
        }

        String nomeAvatar = JOptionPane.showInputDialog("Digite o nome do seu Avatar:");
        avatar = new Avatar(++idAvatar, nomeAvatar,
                0, 0, 0, 0, 0, 0, usuario.getId());

        String resultado = avatarDAO.InserirAvatar(avatar);
        JOptionPane.showMessageDialog(null, resultado);

        if (resultado.contains("sucesso")) {
            registrarLog("AVATAR", "Avatar criado: " + nomeAvatar, usuario);
        }
    }

    private Avatar buscarAvatar(Usuario usuario) {
        return listaSegura(avatarDAO.ListarAvatar()).stream()
                .filter(avatar -> avatar.getIdUsuario() == usuario.getId())
                .findFirst()
                .orElse(null);
    }

    private void equiparItem(Usuario usuario) {
        Avatar avatar = buscarAvatar(usuario);

        if (avatar == null) {
            JOptionPane.showMessageDialog(null, "Crie um avatar primeiro! (Opção 1)");
            return;
        }

        HashSet<Integer> idsItensPossuidos = listaSegura(inventarioDAO.ListarInventario()).stream()
                .filter(inventario -> inventario.getIdUsuario() == usuario.getId())
                .map(Inventario::getIdItem)
                .collect(Collectors.toCollection(HashSet::new));

        ArrayList<Item> itensEquipaveis = listaSegura(itemDAO.ListarItem()).stream()
                .filter(item -> idsItensPossuidos.contains(item.getId()))
                .filter(this::modeloEquipavel)
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
        int opcaoItem = lerInteiro("Escolha um item para equipar:\n\n" + opcoes);

        if (opcaoItem < 1 || opcaoItem > itensEquipaveis.size()) {
            JOptionPane.showMessageDialog(null, "Opção inválida!");
            return;
        }

        Item itemEscolhido = itensEquipaveis.get(opcaoItem - 1);
        avatar.equiparItem(itemEscolhido);
        String resultado = avatarDAO.AlterarAvatar(avatar);

        if (resultado.contains("sucesso")) {
            registrarLog("EQUIPAR", "Item equipado: " + itemEscolhido.getNome(), usuario);
            JOptionPane.showMessageDialog(null, "Item equipado com sucesso!");
        } else {
            JOptionPane.showMessageDialog(null, resultado);
        }
    }

    private boolean modeloEquipavel(Item item) {
        return item.getModelo() != null
                && modelosEquipaveis.contains(item.getModelo().toUpperCase());
    }

    private void fazerMissao(Usuario usuario) {
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
        int opcaoMissao = lerInteiro("Escolha uma missão:\n\n" + opcoes);

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
        MissaoUsuario registro = new MissaoUsuario(++idMissaoUsuario,
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
        registrarLog("MISSAO",
                "Missão concluída: " + missao.getTitulo()
                        + " (+" + missao.getPontos() + " pts)", usuario);

        JOptionPane.showMessageDialog(null,
                "Missão concluída!\n\nVocê ganhou "
                        + missao.getPontos() + " pontos!\nTotal: "
                        + usuario.getPontos() + " pontos");
    }

    private LocalDate calcularDataFim(LocalDate dataInicio, String tipoMissao) {
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

    private void gastarPontos(Usuario usuario) {
        HashSet<Integer> idsItensPossuidos = listaSegura(inventarioDAO.ListarInventario()).stream()
                .filter(inventario -> inventario.getIdUsuario() == usuario.getId())
                .map(Inventario::getIdItem)
                .collect(Collectors.toCollection(HashSet::new));

        ArrayList<Item> itensDisponiveis = listaSegura(itemDAO.ListarItem()).stream()
                .filter(item -> item.getValorPontos() > 0)
                .filter(item -> !idsItensPossuidos.contains(item.getId()))
                .collect(Collectors.toCollection(ArrayList::new));

        if (itensDisponiveis.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Não há itens disponíveis para compra!");
            return;
        }

        String opcoes = "";
        for (int indice = 0; indice < itensDisponiveis.size(); indice++) {
            Item item = itensDisponiveis.get(indice);
            opcoes = opcoes + (indice + 1) + ". " + item.getNome()
                    + " (" + item.getValorPontos() + " pontos)\n";
        }
        int opcaoCompra = lerInteiro("Escolha um item para comprar:\n\n" + opcoes);

        if (opcaoCompra < 1 || opcaoCompra > itensDisponiveis.size()) {
            JOptionPane.showMessageDialog(null, "Opção inválida!");
            return;
        }

        Item itemComprado = itensDisponiveis.get(opcaoCompra - 1);
        if (usuario.getPontos() < itemComprado.getValorPontos()) {
            JOptionPane.showMessageDialog(null, "Você não tem pontos suficientes!");
            return;
        }

        usuario.gastarPontos(itemComprado.getValorPontos());
        usuarioDAO.AlterarUsuario(usuario);

        Inventario inventario = new Inventario(++idInventario,
                usuario.getId(), itemComprado.getId(), "COMPRA", LocalDate.now());
        inventarioDAO.InserirInventario(inventario);

        registrarLog("COMPRA",
                "Item comprado: " + itemComprado.getNome()
                        + " (-" + itemComprado.getValorPontos() + " pts)", usuario);

        JOptionPane.showMessageDialog(null,
                "Item comprado com sucesso!\n\nSaldo: "
                        + usuario.getPontos() + " pontos");
    }

    private void mostrarInformacoes(Usuario usuario) {
        Avatar avatar = buscarAvatar(usuario);
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
                + "DATA DE NASCIMENTO: " + usuario.getDataNascimento() + "\n"
                + "PONTOS: " + usuario.getPontos() + "\n\n"
                + "AVATAR: " + nomeAvatar + "\n"
                + "\nINVENTÁRIO:\n" + linhasInventario
                + "\nMISSÕES CONCLUÍDAS: " + missoesConcluidas;

        JOptionPane.showMessageDialog(null, info);
    }

    private boolean deletarUsuario(Usuario usuario) {
        int opcaoDeletar = lerInteiro(
                "Tem certeza que deseja deletar seu usuário?\n\n"
                        + "1. Sim\n"
                        + "2. Não");

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

    private void criarMissao(Usuario usuario) {
        String titulo = JOptionPane.showInputDialog("Digite o título da missão:");
        String descricao = JOptionPane.showInputDialog("Digite a descrição da missão:");
        int pontosMissao = lerInteiro("Digite a quantidade de pontos da missão:");

        String menuTipoMissao = "Escolha o tipo da missão:\n\n"
                + "1. Diária\n"
                + "2. Semanal\n"
                + "3. Especial";
        String tipoMissao = tiposMissao.get(lerInteiro(menuTipoMissao));

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
            Missao novaMissao = new Missao(++idMissao, titulo, descricao,
                    pontosMissao, tipoMissao);
            String resultado = missaoDAO.InserirMissao(novaMissao);
            JOptionPane.showMessageDialog(null, resultado);

            if (resultado.contains("sucesso")) {
                registrarLog("CRIAR MISSAO", "Missão criada: " + titulo, usuario);
            }
        }
    }

    private void criarItem(Usuario usuario) {
        String nomeItem = JOptionPane.showInputDialog("Digite o nome do item:");

        String menuModelo = "Escolha o modelo do item:\n\n"
                + "1. Cabelo\n"
                + "2. Roupa de cima interna\n"
                + "3. Roupa de cima externa\n"
                + "4. Roupa de baixo\n"
                + "5. Calçado\n"
                + "6. Acessório";
        String modeloItem = modelosItem.get(lerInteiro(menuModelo));

        int valorPontos = lerInteiro("Digite o valor do item em pontos:");

        String menuTipoItem = "Escolha o tipo do item:\n\n"
                + "1. Normal\n"
                + "2. Exclusivo";
        String tipoItem = tiposItem.get(lerInteiro(menuTipoItem));

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
            Item novoItem = new Item(++idItem, nomeItem, modeloItem,
                    valorPontos, tipoItem);
            String resultado = itemDAO.InserirItem(novoItem);
            JOptionPane.showMessageDialog(null, resultado);

            if (resultado.contains("sucesso")) {
                registrarLog("CRIAR ITEM", "Item criado: " + nomeItem, usuario);
            }
        }
    }

    private boolean processarSaida(Usuario usuario) {
        int opcaoSaida = lerInteiro(
                "O que você deseja fazer?\n\n"
                        + "1. Encerrar programa\n"
                        + "2. Entrar com outro usuário");

        switch (opcaoSaida) {
            case 1:
                registrarLog("SESSAO", "Sessão encerrada", usuario);
                continuar = false;
                return false;
            case 2:
                return false;
            default:
                JOptionPane.showMessageDialog(null, "Opção inválida!");
                return true;
        }
    }

    private void registrarLog(String acao, String descricao, Usuario usuario) {
        Log log = new Log(++idLog, acao, descricao,
                LocalDate.now(), usuario.getId(), "SUCESSO");
        logDAO.InserirLog(log);
    }

    private int lerInteiro(String mensagem) {
        return Integer.parseInt(JOptionPane.showInputDialog(mensagem));
    }
}
