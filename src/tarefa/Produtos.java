package tarefa;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import tarefa.Banco;
import tarefa.Produto;

public class Produtos extends JFrame {

	private static final long serialVersionUID = 1L;

	private Banco banco = new Banco();
	private String[] columns = { "ID", "DESCRIÇÃO", "DATA CADASTRO" };

	/**
	 * Create the frame.
	 */

	private JFrame frameProdutos;
	private JTextField IdField;
	private JTextField DataCadastroField;
	private JTextField descricaoField;
	private JTable table_produtos;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Produtos window = new Produtos();
					window.frameProdutos.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Produtos() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frameProdutos = new JFrame();
		frameProdutos.setAlwaysOnTop(true);
		frameProdutos.setBounds(100, 100, 530, 371);
		frameProdutos.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frameProdutos.getContentPane().setLayout(null);

		JLabel idLabel = new JLabel("ID");
		idLabel.setBounds(10, 21, 89, 14);
		frameProdutos.getContentPane().add(idLabel);

		IdField = new JTextField();
		IdField.setBounds(10, 36, 89, 20);
		frameProdutos.getContentPane().add(IdField);
		IdField.setColumns(10);

		JLabel lblDescrio = new JLabel("Descri\u00E7\u00E3o");
		lblDescrio.setBounds(109, 21, 163, 14);
		frameProdutos.getContentPane().add(lblDescrio);

		descricaoField = new JTextField();
		descricaoField.setBounds(109, 36, 163, 20);
		frameProdutos.getContentPane().add(descricaoField);
		descricaoField.setColumns(10);

		JLabel dataCadastroLabel = new JLabel("Data Cadastro");
		dataCadastroLabel.setBounds(290, 21, 214, 14);
		frameProdutos.getContentPane().add(dataCadastroLabel);

		DataCadastroField = new JTextField();
		DataCadastroField.setBounds(290, 36, 214, 20);
		frameProdutos.getContentPane().add(DataCadastroField);
		DataCadastroField.setColumns(10);

		JButton btnNovo = new JButton("Novo");
		btnNovo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				cleanFormulario();
			}
		});
		btnNovo.setBounds(10, 84, 89, 23);
		frameProdutos.getContentPane().add(btnNovo);

		JButton bntSalvar = new JButton("Salvar");
		bntSalvar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				cadastrarNovoProduto(new Produto(Integer.parseInt(IdField.getText()), descricaoField.getText(),
						DataCadastroField.getText()));
				cleanFormulario();
				iniciarTabela();
			}
		});
		bntSalvar.setBounds(109, 84, 89, 23);
		frameProdutos.getContentPane().add(bntSalvar);

		JButton btnAtualizar = new JButton("Atualizar");
		btnAtualizar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				atualizarProduto(new Produto(Integer.parseInt(IdField.getText()), descricaoField.getText(),
						DataCadastroField.getText()));
				cleanFormulario();
				iniciarTabela();
			}
		});
		btnAtualizar.setBounds(208, 84, 89, 23);
		frameProdutos.getContentPane().add(btnAtualizar);

		JButton bntExcluir = new JButton("Excluir");
		bntExcluir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				deletarProduto(Integer.parseInt(IdField.getText()));
				cleanFormulario();
				iniciarTabela();
			}
		});
		bntExcluir.setBounds(307, 84, 89, 23);
		frameProdutos.getContentPane().add(bntExcluir);

		table_produtos = new JTable();
		iniciarTabela();
		table_produtos.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				linhaSelecionada();
			}
		});
		table_produtos.setBounds(10, 132, 494, 162);
		frameProdutos.getContentPane().add(table_produtos);

	}

	private void linhaSelecionada() {
		DefaultTableModel tableModel = (DefaultTableModel) table_produtos.getModel();
		int row = table_produtos.getSelectedRow();
		if (tableModel.getValueAt(row, 0).toString() != "ID") {
			IdField.setText(tableModel.getValueAt(row, 0).toString());
			descricaoField.setText(tableModel.getValueAt(row, 1).toString());
			DataCadastroField.setText(tableModel.getValueAt(row, 2).toString());
		}

	}

	private void cleanFormulario() {
		descricaoField.setEditable(true);
		descricaoField.setText(null);
		IdField.setText(null);
		DataCadastroField.setText(null);
		descricaoField.requestFocus();
	}

	private void iniciarTabela() {

		DefaultTableModel modeloTabela = new DefaultTableModel(null, columns);
		modeloTabela.addRow(columns);
		listarProdutos().forEach(pro -> {
			modeloTabela
					.addRow(new String[] { String.valueOf(pro.getId()), pro.getDescricao(), pro.getDataCadastro() });
		});
		table_produtos.setModel(modeloTabela);
	}

	public void cadastrarNovoProduto(Produto produto) {
		try {
			banco.save(produto);
			JOptionPane.showMessageDialog(frameProdutos, "Produto salvo com sucesso");
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(frameProdutos, "Erro: " + e.getMessage());
			e.printStackTrace();
		}
	}

	public void atualizarProduto(Produto produto) {
		try {
			banco.update(produto);
			JOptionPane.showMessageDialog(frameProdutos, "Produto atualizado com sucesso");
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(frameProdutos, "Erro: " + e.getMessage());
			e.printStackTrace();
		}
	}

	public void deletarProduto(int idProduto) {
		try {
			banco.deletar(idProduto);
			JOptionPane.showMessageDialog(frameProdutos, "Produto excluido com sucesso");
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(frameProdutos, "Erro: " + e.getMessage());
			e.printStackTrace();
		}
	}

	public Produto buscarProduto(int idProduto) {
		try {
			banco.buscarPeloId(idProduto);
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(frameProdutos, "Erro: " + e.getMessage());
			e.printStackTrace();
		}
		return banco.getProduto();
	}

	public List<Produto> listarProdutos() {
		try {
			banco.listar();
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(frameProdutos, "Erro: " + e.getMessage());
			e.printStackTrace();
		}
		return banco.getProdutos();
	}

}