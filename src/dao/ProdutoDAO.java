package dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import connection.ConexaoDB;
import model.Produto;

public class ProdutoDAO {

	private void validarProduto(Produto produto) {
		if (produto == null) {
			throw new IllegalArgumentException("O produto não pode ser nulo!");
		}
		if (produto.getPreco() == null || produto.getPreco().compareTo(BigDecimal.ZERO) < 0) {
			throw new IllegalArgumentException("O preço não pode ser nulo ou negativo!");
		}

		if (produto.getQuantidade() < 0) {
			throw new IllegalArgumentException("A quantidade não pode ser negativa!");
		}

		if (produto.getNome() == null || produto.getNome().isBlank()) {
			throw new IllegalArgumentException("O nome do produto não pode estar vazio!");
		}
	}

	public void cadastrar(Produto produto) throws SQLException {

		validarProduto(produto);
		String sql = "INSERT INTO produto(nome,descricao,preco,quantidade) VALUES(?,?,?,?)";

		try (Connection conn = ConexaoDB.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setString(1, produto.getNome());
			stmt.setString(2, produto.getDescricao());
			stmt.setBigDecimal(3, produto.getPreco());
			stmt.setInt(4, produto.getQuantidade());

			stmt.executeUpdate();
		}
	}

	public List<Produto> listar() throws SQLException {

		List<Produto> produto = new ArrayList<>();

		String sql = " SELECT * FROM produto";

		try (Connection conn = ConexaoDB.conectar();
				PreparedStatement stmt = conn.prepareStatement(sql);
				ResultSet rs = stmt.executeQuery()) {

			while (rs.next()) {
				Produto produto1 = new Produto();

				produto1.setId(rs.getInt("id"));
				produto1.setNome(rs.getString("nome"));
				produto1.setDescricao(rs.getString("descricao"));
				produto1.setPreco(rs.getBigDecimal("preco"));
				produto1.setQuantidade(rs.getInt("quantidade"));
				produto1.setDataCadastro(rs.getObject("data_cadastro", LocalDateTime.class));

				produto.add(produto1);

			}
		}

		return produto;
	}

	public Produto buscarPorId(int id) throws SQLException {

		String sql = "SELECT * FROM produto WHERE id = ?";

		try (Connection conn = ConexaoDB.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setInt(1, id);

			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					Produto produto2 = new Produto();
					produto2.setId(rs.getInt("id"));
					produto2.setNome(rs.getString("nome"));
					produto2.setDescricao(rs.getString("descricao"));
					produto2.setPreco(rs.getBigDecimal("preco"));
					produto2.setQuantidade(rs.getInt("quantidade"));
					produto2.setDataCadastro(rs.getObject("data_cadastro", LocalDateTime.class));
					return produto2;

				}

			}
		}

		return null;

	}

	public void atualizar(Produto produto) throws SQLException {

		validarProduto(produto);
		String sql = "UPDATE produto SET nome = ?, descricao = ?, preco = ?, quantidade = ? WHERE id = ?";

		try (Connection conn = ConexaoDB.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setString(1, produto.getNome());
			stmt.setString(2, produto.getDescricao());
			stmt.setBigDecimal(3, produto.getPreco());
			stmt.setInt(4, produto.getQuantidade());
			stmt.setInt(5, produto.getId());

			stmt.executeUpdate();
		}
	}

	public boolean excluir(int id) throws SQLException {
		String sql = "DELETE FROM produto WHERE id = ?";

		try (Connection conn = ConexaoDB.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setInt(1, id);

			int resultado = stmt.executeUpdate();

			if (resultado > 0) {
				return true;
			} else {
				return false;
			}

		}
	}

}
