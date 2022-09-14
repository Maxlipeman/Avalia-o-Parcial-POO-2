package tarefa;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import tarefa.Produto;

public class Banco {
	private List<Produto> produtos;
	private Produto produto;
	private Connection con;

	public Banco() {
		produtos = new ArrayList<Produto>();
		produto = new Produto();
		try {
			conexao();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void conexao() throws Exception {
		if (con != null)
			return;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			throw new Exception("Drive não enconotrado.");
		}

		String url = "jdbc:mysql://localhost:3306/db_pedido";
		con = DriverManager.getConnection(url, "root", "");
	}

	public void disconnect() throws Exception {
		if (con != null) {
			try {
				con.close();
			} catch (SQLException e) {
				throw new Exception("Não foi possivel fechar a conexão");
			}
		}

	}

	public void save(Produto produto) throws SQLException {
		String insertSql = "insert into produto (idproduto , descricao , data_cadastro) values ( ? , ? ,? )  ";
		PreparedStatement insert = con.prepareStatement(insertSql);

        insert.setLong(1, produto.getId());
        insert.setString(2, produto.getDescricao());
        insert.setString(3, produto.getDataCadastro());
        insert.executeUpdate();
        insert.close();
        
	}
	
	public void update(Produto produto) throws SQLException {

		String updateSql = "update produto set descricao= ?, data_cadastro= ? where idproduto=?";
		PreparedStatement update = con.prepareStatement(updateSql);

		update.setString(1, produto.getDescricao());
        update.setString(2, produto.getDataCadastro());
        update.setLong(3, produto.getId());
        update.executeUpdate();
        update.close();
	}
	
	public void deletar(int produto) throws SQLException {
		
		String deleteSql = "delete from produto where idproduto = ?";
		PreparedStatement update = con.prepareStatement(deleteSql);

		update.setLong(1, produto);
        update.executeUpdate();
        update.close();
	}
	
	public void buscarPeloId(int idProduto) throws SQLException {
		
		String findByIdSql = "select idproduto, descricao, data_cadastro from produto where idproduto = "+idProduto;
		Statement selectStatement = con.createStatement();

		ResultSet results = (ResultSet) selectStatement.executeQuery(findByIdSql);

		while (results.next()) {
			int id = results.getInt("idproduto");
			String descricao = results.getString("descricao");
			String dataCadastro = results.getString("data_cadastro");

			produto = new Produto(id, descricao, dataCadastro);

		}
		results.close();
		selectStatement.close();

	}
	
	public void listar() throws SQLException {
		produtos.clear();

		String sql = "select * from produto order by descricao";

		Statement selectStatement = con.createStatement();

		ResultSet results = (ResultSet) selectStatement.executeQuery(sql);

		while (results.next()) {
			int id = results.getInt("idproduto");
			String descricao = results.getString("descricao");
			String dataCadastro = results.getString("data_cadastro");

			Produto produto = new Produto(id, descricao, dataCadastro);
			addProduto(produto);

		}

		results.close();
		selectStatement.close();

	}

	public void addProduto(Produto person) {
		produtos.add(person);
	}

	public void removeProduto(Produto index) {
		produtos.remove(index);

	}

	public List<Produto> getProdutos() {
		return produtos;
	}
	
	public Produto getProduto() {
		return produto;
	}

}