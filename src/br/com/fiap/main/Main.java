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

    private static Connection con;
    private static UsuarioDAO usuarioDAO;
    private static AvatarDAO avatarDAO;
    private static ItemDAO itemDAO;
    private static InventarioDAO inventarioDAO;
    private static MissaoDAO missaoDAO;
    private static MissaoUsuarioDAO missaoUsuarioDAO;
    private static ParceriaDAO parceriaDAO;
    private static CodigoDAO codigoDAO;
    private static LogDAO logDAO;
    private static boolean continuarPrograma = true;

    public static void main(String[] args) {
        con = ConnectionFactory.getConnection();

        if (con == null) {
            JOptionPane.showMessageDialog(null,
                    "Nao foi possivel conectar ao banco de dados.",
                    "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        usuarioDAO = new UsuarioDAO(con);
        avatarDAO = new AvatarDAO(con);
        itemDAO = new ItemDAO(con);
        inventarioDAO = new InventarioDAO(con);
        missaoDAO = new MissaoDAO(con);
        missaoUsuarioDAO = new MissaoUsuarioDAO(con);
        parceriaDAO = new ParceriaDAO(con);
        codigoDAO = new CodigoDAO(con);
        logDAO = new LogDAO(con);

        JOptionPane.showMessageDialog(null,
                "Bem-vindo ao TechLeet - Sistema de Gamificacao!");

        while (continuarPrograma) {
            Usuario usuario = menuAcesso();
            if (usuario != null) {
                menuPrincipal(usuario);
            }
        }

        ConnectionFactory.closeConnection(con);
    }

    private static Usuario menuAcesso() {
        Integer opcao = lerOpcao("=== ACESSO ===\n\n"
                + "1. Cadastrar usuario\n"
                + "2. Entrar\n"
                + "3. Encerrar programa", 1, 3);

        if (opcao == null || opcao == 3) {
            continuarPrograma = false;
            return null;
        }
        if (opcao == 1) {
            return cadastrarUsuario();
        }
        return entrar();
    }

    private static Usuario cadastrarUsuario() {
        String email = lerTexto("Digite seu email:");
        if (email == null) {
            return null;
        }
        String senha = lerTexto("Digite sua senha:");
        if (senha == null) {
            return null;
        }

        ArrayList<Usuario> usuarios = usuarioDAO.listaTodos();
        if (usuarios != null) {
            for (Usuario cadastrado : usuarios) {
                if (cadastrado.getEmail().equalsIgnoreCase(email)) {
                    JOptionPane.showMessageDialog(null, "Esse email ja esta cadastrado.");
                    return null;
                }
            }
        }

        Integer idAvatar = lerInteiro("Digite o ID do avatar associado ao usuario:\n"
                + "Use um ID de avatar existente no banco.");
        if (idAvatar == null) {
            return null;
        }

        Usuario usuario = new Usuario(proximoIdUsuario(usuarios), email, senha, 0, idAvatar);
        String resultado = usuarioDAO.InserirUsuario(usuario);
        JOptionPane.showMessageDialog(null, resultado);

        if (!foiSucesso(resultado)) {
            return null;
        }

        inserirLog(usuario.getId(), "CADASTRO", "Usuario cadastrado: " + email, "ok");
        return usuario;
    }

    private static Usuario entrar() {
        String email = lerTexto("Digite seu email:");
        if (email == null) {
            return null;
        }
        String senha = lerTexto("Digite sua senha:");
        if (senha == null) {
            return null;
        }

        ArrayList<Usuario> usuarios = usuarioDAO.listaTodos();
        if (usuarios != null) {
            for (Usuario usuario : usuarios) {
                if (usuario.getEmail().equalsIgnoreCase(email)
                        && usuario.getSenha().equals(senha)) {
                    inserirLog(usuario.getId(), "SESSAO", "Login realizado", "ok");
                    JOptionPane.showMessageDialog(null, "Login realizado com sucesso!");
                    return usuario;
                }
            }
        }

        JOptionPane.showMessageDialog(null, "Email ou senha invalidos.");
        return null;
    }

    private static void menuPrincipal(Usuario usuario) {
        boolean continuarMenu = true;

        while (continuarPrograma && continuarMenu) {
            Integer opcao = lerOpcao("=== MENU PRINCIPAL ===\n\n"
                    + "Usuario: " + usuario.getEmail() + "\n"
                    + "Pontos: " + usuario.getPontos() + "\n\n"
                    + "1. Criar Avatar\n"
                    + "2. Equipar item no Avatar\n"
                    + "3. Fazer Missao\n"
                    + "4. Gastar Pontos\n"
                    + "5. Ver Informacoes\n"
                    + "6. Sair", 1, 6);

            if (opcao == null) {
                continue;
            }

            if (opcao == 1) {
                criarAvatar(usuario);
            } else if (opcao == 2) {
                equiparItem(usuario);
            } else if (opcao == 3) {
                fazerMissao(usuario);
            } else if (opcao == 4) {
                gastarPontos(usuario);
            } else if (opcao == 5) {
                mostrarInformacoes(usuario);
            } else {
                Integer opcaoSaida = lerOpcao("O que voce deseja fazer?\n\n"
                        + "1. Encerrar programa\n"
                        + "2. Entrar com outro usuario\n"
                        + "3. Voltar", 1, 3);

                if (opcaoSaida != null && opcaoSaida == 1) {
                    inserirLog(usuario.getId(), "SESSAO", "Sessao encerrada", "ok");
                    continuarPrograma = false;
                    continuarMenu = false;
                } else if (opcaoSaida != null && opcaoSaida == 2) {
                    inserirLog(usuario.getId(), "SESSAO", "Logout realizado", "ok");
                    continuarMenu = false;
                }
            }
        }
    }

    private static void criarAvatar(Usuario usuario) {
        ArrayList<Avatar> avatares = avatarDAO.ListarAvatar();
        Avatar avatarAtual = localizarAvatar(usuario, avatares);

        if (avatarAtual != null) {
            JOptionPane.showMessageDialog(null,
                    "Voce ja possui o avatar '" + avatarAtual.getNome() + "'.\n"
                            + "Use a opcao 2 para equipa-lo.");
            return;
        }

        String nome = lerTexto("Digite o nome do seu Avatar:");
        if (nome == null) {
            return;
        }

        Avatar avatar = new Avatar(proximoIdAvatar(avatares), nome,
                0, 0, 0, 0, 0, 0, usuario.getId());
        String resultado = avatarDAO.InserirAvatar(avatar);
        JOptionPane.showMessageDialog(null, resultado);

        if (foiSucesso(resultado)) {
            inserirLog(usuario.getId(), "AVATAR", "Avatar criado: " + nome, "ok");
        }
    }

    private static void equiparItem(Usuario usuario) {
        Avatar avatar = localizarAvatar(usuario, avatarDAO.ListarAvatar());
        if (avatar == null) {
            JOptionPane.showMessageDialog(null, "Crie um avatar primeiro! (Opcao 1)");
            return;
        }

        ArrayList<Item> todosItens = itemDAO.ListarItem();
        ArrayList<Item> itensEquipaveis = new ArrayList<>();
        if (todosItens != null) {
            for (Item item : todosItens) {
                if (tipoEquipavel(item.getTipo())) {
                    itensEquipaveis.add(item);
                }
            }
        }

        Item item = selecionarItem(itensEquipaveis, "Escolha o item que deseja equipar:");
        if (item == null) {
            return;
        }

        try {
            avatar.equiparItem(item);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
            return;
        }

        String resultado = avatarDAO.AlterarAvatar(avatar);
        if (!foiSucesso(resultado)) {
            JOptionPane.showMessageDialog(null, resultado);
            return;
        }

        ArrayList<Inventario> inventarios = inventarioDAO.ListarInventario();
        if (!usuarioPossuiItem(usuario.getId(), item.getId(), inventarios)) {
            Inventario inventario = new Inventario(proximoIdInventario(inventarios),
                    usuario.getId(), item.getId(), "personalizacao", LocalDate.now());
            inventarioDAO.InserirInventario(inventario);
        }

        inserirLog(usuario.getId(), "EQUIPAR", "Item equipado: " + item.getNome(), "ok");
        JOptionPane.showMessageDialog(null, "Item equipado com sucesso!");
    }

    private static void fazerMissao(Usuario usuario) {
        ArrayList<Missao> missoes = missaoDAO.ListaMissao();
        Missao missao = selecionarMissao(missoes);
        if (missao == null) {
            return;
        }

        ArrayList<MissaoUsuario> missoesUsuario = missaoUsuarioDAO.ListarMissaoUsuario();
        if (missaoConcluida(usuario.getId(), missao.getId(), missoesUsuario)) {
            JOptionPane.showMessageDialog(null, "Voce ja concluiu essa missao.");
            return;
        }

        MissaoUsuario registro = new MissaoUsuario(
                proximoIdMissaoUsuario(missoesUsuario), usuario.getId(), missao.getId(),
                "pendente", null, null, null);
        registro.iniciar();
        registro.concluir();
        registro.setDataFim(LocalDate.now());

        int pontosAnteriores = usuario.getPontos();
        usuario.adicionarPontos(missao.getPontos());

        String resultadoMissao = missaoUsuarioDAO.InserirMissaoUsuario(registro);
        boolean salvo = false;

        if (foiSucesso(resultadoMissao)) {
            String resultadoUsuario = usuarioDAO.AlterarUsuario(usuario);
            if (foiSucesso(resultadoUsuario)) {
                salvo = true;
            } else {
                JOptionPane.showMessageDialog(null, resultadoUsuario);
            }
        } else {
            JOptionPane.showMessageDialog(null, resultadoMissao);
        }

        if (!salvo) {
            usuario.setPontos(pontosAnteriores);
            return;
        }

        inserirLog(usuario.getId(), "MISSAO",
                "Missao concluida: " + missao.getTitulo()
                        + " (+" + missao.getPontos() + " pts)", "ok");
        JOptionPane.showMessageDialog(null,
                "Missao concluida!\n\nVoce ganhou " + missao.getPontos()
                        + " pontos!\nTotal: " + usuario.getPontos() + " pontos");
    }

    private static void gastarPontos(Usuario usuario) {
        ArrayList<Item> itens = itemDAO.ListarItem();
        ArrayList<Item> itensDisponiveis = new ArrayList<>();
        ArrayList<Inventario> inventarios = inventarioDAO.ListarInventario();

        if (itens != null) {
            for (Item item : itens) {
                if (item.getValorPontos() > 0
                        && !usuarioPossuiItem(usuario.getId(), item.getId(), inventarios)) {
                    itensDisponiveis.add(item);
                }
            }
        }

        Item item = selecionarItem(itensDisponiveis, "Escolha o item que deseja comprar:");
        if (item == null) {
            return;
        }
        if (usuario.getPontos() < item.getValorPontos()) {
            inserirLog(usuario.getId(), "COMPRA",
                    "Compra recusada: pontos insuficientes", "falha");
            JOptionPane.showMessageDialog(null, "Voce nao tem pontos suficientes!");
            return;
        }

        Parceria parceria = selecionarParceriaAtiva(parceriaDAO.ListarParceria());
        if (parceria == null) {
            return;
        }

        ArrayList<Codigo> codigos = codigoDAO.ListarCodigo();
        int idCodigo = proximoIdCodigo(codigos);
        Codigo codigo = new Codigo(idCodigo, "COD" + idCodigo + "-" + usuario.getId(),
                "ativo", LocalDate.now().plusDays(30), null, item.getId(), parceria.getId());
        codigo.resgatar();

        Inventario inventario = new Inventario(proximoIdInventario(inventarios),
                usuario.getId(), item.getId(), "compra", LocalDate.now());

        int pontosAnteriores = usuario.getPontos();
        usuario.gastarPontos(item.getValorPontos());

        String resultadoUsuario = usuarioDAO.AlterarUsuario(usuario);
        boolean salvo = false;

        if (foiSucesso(resultadoUsuario)) {
            String resultadoCodigo = codigoDAO.InserirCodigo(codigo);
            if (foiSucesso(resultadoCodigo)) {
                String resultadoInventario = inventarioDAO.InserirInventario(inventario);
                if (foiSucesso(resultadoInventario)) {
                    salvo = true;
                } else {
                    JOptionPane.showMessageDialog(null, resultadoInventario);
                }
            } else {
                JOptionPane.showMessageDialog(null, resultadoCodigo);
            }
        } else {
            JOptionPane.showMessageDialog(null, resultadoUsuario);
        }

        if (!salvo) {
            usuario.setPontos(pontosAnteriores);
            return;
        }

        inserirLog(usuario.getId(), "COMPRA",
                "Item comprado: " + item.getNome()
                        + " (-" + item.getValorPontos() + " pts)", "ok");
        JOptionPane.showMessageDialog(null,
                "Item comprado com sucesso!\n\nCodigo: " + codigo.getCodigoResgate()
                        + "\nParceria: " + parceria.getNome()
                        + "\nSaldo: " + usuario.getPontos() + " pontos");
    }

    private static void mostrarInformacoes(Usuario usuario) {
        Avatar avatar = localizarAvatar(usuario, avatarDAO.ListarAvatar());
        ArrayList<Item> itens = itemDAO.ListarItem();
        ArrayList<Inventario> inventarios = inventarioDAO.ListarInventario();
        ArrayList<MissaoUsuario> missoesUsuario = missaoUsuarioDAO.ListarMissaoUsuario();

        StringBuilder info = new StringBuilder("=== SUAS INFORMACOES ===\n\n");
        info.append("ID: ").append(usuario.getId()).append('\n');
        info.append("EMAIL: ").append(usuario.getEmail()).append('\n');
        info.append("PONTOS: ").append(usuario.getPontos()).append("\n\n");

        if (avatar == null) {
            info.append("AVATAR: Nao criado\n");
        } else {
            info.append("AVATAR: ").append(avatar.getNome()).append('\n');
            adicionarEquipado(info, "Cabelo", avatar.getIdCabelo(), itens);
            adicionarEquipado(info, "Roupa interna", avatar.getIdRoupaCimaInt(), itens);
            adicionarEquipado(info, "Roupa externa", avatar.getIdRoupaCimaExt(), itens);
            adicionarEquipado(info, "Roupa de baixo", avatar.getIdRoupaBaixo(), itens);
            adicionarEquipado(info, "Calcado", avatar.getIdCalcado(), itens);
            adicionarEquipado(info, "Acessorio", avatar.getIdAcessorio(), itens);
        }

        info.append("\nINVENTARIO:\n");
        int quantidadeItens = 0;
        if (inventarios != null) {
            for (Inventario inventario : inventarios) {
                if (inventario.getIdUsuario() == usuario.getId()) {
                    Item item = localizarItem(inventario.getIdItem(), itens);
                    info.append("  - ")
                            .append(item != null ? item.getNome() : "Item #" + inventario.getIdItem())
                            .append(" (").append(inventario.getOrigem()).append(")\n");
                    quantidadeItens++;
                }
            }
        }
        if (quantidadeItens == 0) {
            info.append("  Nenhum item adquirido.\n");
        }

        int concluidas = 0;
        if (missoesUsuario != null) {
            for (MissaoUsuario registro : missoesUsuario) {
                if (registro.getIdUsuario() == usuario.getId()
                        && "concluida".equalsIgnoreCase(registro.getStatus())) {
                    concluidas++;
                }
            }
        }
        info.append("\nMISSOES CONCLUIDAS: ").append(concluidas);

        JOptionPane.showMessageDialog(null, info.toString());
    }

    private static Item selecionarItem(ArrayList<Item> itens, String titulo) {
        if (itens == null || itens.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Nao ha itens disponiveis no banco.");
            return null;
        }

        StringBuilder menu = new StringBuilder(titulo).append("\n\n");
        for (int i = 0; i < itens.size(); i++) {
            Item item = itens.get(i);
            menu.append(i + 1).append(". ").append(item.getNome())
                    .append(" [").append(item.getTipo()).append("] - ")
                    .append(item.getValorPontos()).append(" pontos\n");
        }
        menu.append(itens.size() + 1).append(". Cancelar");

        Integer opcao = lerOpcao(menu.toString(), 1, itens.size() + 1);
        if (opcao == null || opcao == itens.size() + 1) {
            return null;
        }
        return itens.get(opcao - 1);
    }

    private static Missao selecionarMissao(ArrayList<Missao> missoes) {
        if (missoes == null || missoes.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Nao ha missoes cadastradas no banco.");
            return null;
        }

        StringBuilder menu = new StringBuilder("Escolha uma missao:\n\n");
        for (int i = 0; i < missoes.size(); i++) {
            Missao missao = missoes.get(i);
            menu.append(i + 1).append(". ").append(missao.getTitulo())
                    .append(" (").append(missao.getPontos()).append(" pontos)\n");
        }
        menu.append(missoes.size() + 1).append(". Cancelar");

        Integer opcao = lerOpcao(menu.toString(), 1, missoes.size() + 1);
        if (opcao == null || opcao == missoes.size() + 1) {
            return null;
        }
        return missoes.get(opcao - 1);
    }

    private static Parceria selecionarParceriaAtiva(ArrayList<Parceria> parcerias) {
        ArrayList<Parceria> ativas = new ArrayList<>();
        LocalDate hoje = LocalDate.now();

        if (parcerias != null) {
            for (Parceria parceria : parcerias) {
                boolean dentroDoPeriodo = (parceria.getDataInicio() == null
                        || !hoje.isBefore(parceria.getDataInicio()))
                        && (parceria.getDataFim() == null
                        || !hoje.isAfter(parceria.getDataFim()));
                if ("ativa".equalsIgnoreCase(parceria.getStatus()) && dentroDoPeriodo) {
                    ativas.add(parceria);
                }
            }
        }

        if (ativas.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Nao ha parcerias ativas no banco.");
            return null;
        }

        StringBuilder menu = new StringBuilder("Escolha uma parceria:\n\n");
        for (int i = 0; i < ativas.size(); i++) {
            menu.append(i + 1).append(". ").append(ativas.get(i).getNome()).append('\n');
        }
        menu.append(ativas.size() + 1).append(". Cancelar");

        Integer opcao = lerOpcao(menu.toString(), 1, ativas.size() + 1);
        if (opcao == null || opcao == ativas.size() + 1) {
            return null;
        }
        return ativas.get(opcao - 1);
    }

    private static Avatar localizarAvatar(Usuario usuario, ArrayList<Avatar> avatares) {
        if (avatares != null) {
            for (Avatar avatar : avatares) {
                if (avatar.getIdUsuario() == usuario.getId()
                        || avatar.getId() == usuario.getIdAvatar()) {
                    return avatar;
                }
            }
        }
        return null;
    }

    private static Item localizarItem(int idItem, ArrayList<Item> itens) {
        if (itens != null) {
            for (Item item : itens) {
                if (item.getId() == idItem) {
                    return item;
                }
            }
        }
        return null;
    }

    private static void adicionarEquipado(StringBuilder info, String nome,
                                           int idItem, ArrayList<Item> itens) {
        if (idItem > 0) {
            Item item = localizarItem(idItem, itens);
            info.append("  - ").append(nome).append(": ")
                    .append(item != null ? item.getNome() : "Item #" + idItem).append('\n');
        }
    }

    private static boolean tipoEquipavel(String tipo) {
        return tipo != null && (tipo.equalsIgnoreCase("cabelo")
                || tipo.equalsIgnoreCase("roupa_cima_int")
                || tipo.equalsIgnoreCase("roupa_cima_ext")
                || tipo.equalsIgnoreCase("roupa_baixo")
                || tipo.equalsIgnoreCase("calcado")
                || tipo.equalsIgnoreCase("acessorio"));
    }

    private static boolean usuarioPossuiItem(int idUsuario, int idItem,
                                              ArrayList<Inventario> inventarios) {
        if (inventarios != null) {
            for (Inventario inventario : inventarios) {
                if (inventario.getIdUsuario() == idUsuario
                        && inventario.getIdItem() == idItem) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean missaoConcluida(int idUsuario, int idMissao,
                                            ArrayList<MissaoUsuario> registros) {
        if (registros != null) {
            for (MissaoUsuario registro : registros) {
                if (registro.getIdUsuario() == idUsuario
                        && registro.getIdMissao() == idMissao
                        && "concluida".equalsIgnoreCase(registro.getStatus())) {
                    return true;
                }
            }
        }
        return false;
    }

    private static void inserirLog(int idUsuario, String assunto,
                                   String descricao, String status) {
        ArrayList<Log> logs = logDAO.ListarLog();
        Log log = new Log(proximoIdLog(logs), assunto, descricao,
                LocalDate.now(), idUsuario, status);
        logDAO.InserirLog(log);
    }

    private static boolean foiSucesso(String resultado) {
        return resultado != null && resultado.toLowerCase().contains("com sucesso");
    }

    private static Integer lerOpcao(String mensagem, int minimo, int maximo) {
        while (true) {
            String texto = JOptionPane.showInputDialog(mensagem);
            if (texto == null) {
                return null;
            }
            try {
                int opcao = Integer.parseInt(texto.trim());
                if (opcao >= minimo && opcao <= maximo) {
                    return opcao;
                }
            } catch (NumberFormatException ignored) {
                // Exibe a mesma mensagem para letras e numeros fora do intervalo.
            }
            JOptionPane.showMessageDialog(null, "Opcao invalida!");
        }
    }

    private static Integer lerInteiro(String mensagem) {
        while (true) {
            String texto = JOptionPane.showInputDialog(mensagem);
            if (texto == null) {
                return null;
            }
            try {
                return Integer.parseInt(texto.trim());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Digite um numero valido!");
            }
        }
    }

    private static String lerTexto(String mensagem) {
        while (true) {
            String texto = JOptionPane.showInputDialog(mensagem);
            if (texto == null) {
                return null;
            }
            texto = texto.trim();
            if (!texto.isEmpty()) {
                return texto;
            }
            JOptionPane.showMessageDialog(null, "O campo nao pode ficar vazio.");
        }
    }

    private static int proximoIdUsuario(ArrayList<Usuario> lista) {
        int maior = 0;
        if (lista != null) {
            for (Usuario objeto : lista) {
                maior = Math.max(maior, objeto.getId());
            }
        }
        return maior + 1;
    }

    private static int proximoIdAvatar(ArrayList<Avatar> lista) {
        int maior = 0;
        if (lista != null) {
            for (Avatar objeto : lista) {
                maior = Math.max(maior, objeto.getId());
            }
        }
        return maior + 1;
    }

    private static int proximoIdInventario(ArrayList<Inventario> lista) {
        int maior = 0;
        if (lista != null) {
            for (Inventario objeto : lista) {
                maior = Math.max(maior, objeto.getId());
            }
        }
        return maior + 1;
    }

    private static int proximoIdMissaoUsuario(ArrayList<MissaoUsuario> lista) {
        int maior = 0;
        if (lista != null) {
            for (MissaoUsuario objeto : lista) {
                maior = Math.max(maior, objeto.getId());
            }
        }
        return maior + 1;
    }

    private static int proximoIdCodigo(ArrayList<Codigo> lista) {
        int maior = 0;
        if (lista != null) {
            for (Codigo objeto : lista) {
                maior = Math.max(maior, objeto.getId());
            }
        }
        return maior + 1;
    }

    private static int proximoIdLog(ArrayList<Log> lista) {
        int maior = 0;
        if (lista != null) {
            for (Log objeto : lista) {
                maior = Math.max(maior, objeto.getId());
            }
        }
        return maior + 1;
    }

}
