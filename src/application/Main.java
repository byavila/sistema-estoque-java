package application;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import dao.ProdutoDAO;
import model.Produto;

public class Main {
	private static void exibirMenu() {
		System.out.println("===== SISTEMA DE ESTOQUE =====");
		System.out.println("");
		System.out.println("1 - Cadastrar produto");
		System.out.println("2 - Listar produtos");
		System.out.println("3 - Buscar produto por ID");
		System.out.println("4 - Atualizar produto");
		System.out.println("5 - Excluir produto");
		System.out.println("0 - Sair");
		System.out.println();

		System.out.println("Escolha uma opção: ");
	}

	private static void listarProdutos(ProdutoDAO dao) throws SQLException {

		List<Produto> lista = dao.listar();

		for (Produto produto : lista) {
			System.out.println(produto);
		}

	}

	private static void buscarProduto(ProdutoDAO dao, Scanner sc) throws SQLException {

		System.out.println("Digite o ID do produto: ");
		int digitado = sc.nextInt();

		Produto resultadoBusca = dao.buscarPorId(digitado);

		if (resultadoBusca != null) {
			System.out.println(resultadoBusca);
		} else {
			System.out.println("Produto não encontrado!");
		}
	}

	private static void cadastrarProduto(ProdutoDAO dao, Scanner sc) throws SQLException {
		sc.nextLine();

		System.out.println("Digite o nome do produto: ");
		String nomeProduto = sc.nextLine();

		System.out.println("Digite a descrição do produto: ");
		String descricaoProduto = sc.nextLine();

		System.out.println("Digite o preço do produto: ");
		BigDecimal precoProduto = sc.nextBigDecimal();

		System.out.println("Digite a quantidade do produto: ");
		int quantidadeProduto = sc.nextInt();

		Produto produto = new Produto(nomeProduto, descricaoProduto, precoProduto, quantidadeProduto);

		dao.cadastrar(produto);
		System.out.println("Produto cadastrado com sucesso!");
	}

	private static void atualizarProduto(ProdutoDAO dao, Scanner sc) throws SQLException {
		System.out.println("Digite o id do produto que deseja atualizar: ");
		int digitado = sc.nextInt();

		Produto atualizar = dao.buscarPorId(digitado);

		if (atualizar == null) {
			System.out.println("Produto não encontrado!");
			return;
		}

		sc.nextLine();
		System.out.println("Digite o novo nome: ");
		String novoNome = sc.nextLine();
		System.out.println("Digite a nova descrição: ");
		String novaDescricao = sc.nextLine();
		System.out.println("Digite o novo preço: ");
		BigDecimal novoPreco = sc.nextBigDecimal();
		System.out.println("Digite a nova quantidade: ");
		int novaQuantidade = sc.nextInt();

		atualizar.setNome(novoNome);
		atualizar.setDescricao(novaDescricao);
		atualizar.setPreco(novoPreco);
		atualizar.setQuantidade(novaQuantidade);

		dao.atualizar(atualizar);

		System.out.println("Produto atualizado com sucesso!");
	}

	private static void excluirProduto(ProdutoDAO dao, Scanner sc) throws SQLException {
		System.out.println("Digite o id do produto que deseja excluir: ");
		int digitado = sc.nextInt();

		boolean excluido = dao.excluir(digitado);

		if (excluido) {
			System.out.println("Produto excluido com sucesso!");
		} else {
			System.out.println("Produto não encontrado!");
		}
	}

	public static void main(String[] args) {

		Scanner sc = new Scanner(System.in);

		int opcao = -1;
		ProdutoDAO dao = new ProdutoDAO();
		do {
			exibirMenu();
			try {
				opcao = sc.nextInt();

				switch (opcao) {
				case 1:
					cadastrarProduto(dao, sc);
					break;
				case 2:
					listarProdutos(dao);
					break;
				case 3:
					buscarProduto(dao, sc);
					break;
				case 4:
					atualizarProduto(dao, sc);
					break;
				case 5:
					excluirProduto(dao, sc);
					break;
				case 0:
					System.out.println("Programa encerrado");
					break;
				default:
					System.out.println("invalido");
					break;
				}

			} catch (InputMismatchException i) {
				System.out.println("Entrada inválida! Digite um número: ");
				sc.nextLine();
				opcao = -1;
			} catch (SQLException e) {
				System.out.println("Erro no banco de dados!");
			} catch (IllegalArgumentException i) {
				System.out.println(i.getMessage());
			}

		} while (opcao != 0);
		sc.close();
	}

}
