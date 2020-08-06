/*
* SENAI / CENTROWEG
* AIPSIN 2019/1
* MI-66
* Autor(es): Daniel Schinaider de Oliveira, 
* 	         Victor Hugo Moresco,
* 		   	 Braian Costa Zapelini, 
*            Leonardo Cech, 
* 	         Gabriel da Costa 
*
* Data: 06/08/2020
* 
* Classe de processamento e renderização da janela principal
* 
* ==================================================================
* Alteração
* 
* Data: 06/08/2020
* Responsável: Leonardo Cech
*
*====================================================================
* Documentação da Classe
* 
* Data: 06/08/2020
* Responsável: Daniel Schinaider de Oliveira
* -------------------------------------------------------
*
* ================================
* Declaração de variáveis
* 
*  buttonSair : Button  Instância da Button para Sair
*  buttonConcluir : Button Instância de Button para concluir
*  buttonConfirmar : Button Instância de Button para confirmar
*  labelItemNumero : Label Instância de Label para o item
*  labelDescricaoItemNumero : Label Instância de Label para Descrever item
*  spinnerQuantidade : Spinner<Integer> Instância de JSpinner para quantidade de item
*  
*  
*  tableViewMateriais : TableView<MateriaisRetirada> Instância da table listagem de materiais
*  tableColumnNumero : TableColumn<Object, Object>  Instância de table para coluna do numero
*  tableColumnDescricao : TableColumn<Object, Object>  Instancia de table para coluna da descricao
*  tableColumnTipo : TableColumn<Object, Object> Instancia de table para coluna de tipo
*  tableColumnQuantidadeDisponivel : TableColumn<Object, Object> Instancia de table para coluna quantidade disponivel
*  tableColumnQuantidadeDesejada : TableColumn<Object, Object> Instancia de table para coluna quantidade desejada
*  
*  export : DatabaseExcel Instancia para Dao do excel
*  dao : DaoMaterial  Instancia para Dao do material
*  daoLog : DaoUsuarioLogado  Instancia para Dao do usuario logado
*  
*   observableList : ObservableList<MateriaisRetirada>  Instancia da lista de materias de retirada
*   listMateriais : List<Material> 	 Instancia da lista de materiais
*   listRetirada : List<MateriaisRetirada>   Instancia da lista de materiais de retirada
*  
* ================================
*/

package Controller;

import Model.MateriaisRetirada;
import Model.Material;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import Dao.DaoMaterial;
import Dao.DaoUsuarioLogado;

public class Retirada implements Initializable {

	@FXML
	private Button buttonSair;
	@FXML
	private Button buttonConcluir;
	@FXML
	private Button buttonConfirmar;
	@FXML
	private Label labelItemNumero;
	@FXML
	private Label labelDescricaoItemNumero;
	@FXML
	private Spinner<Integer> spinnerQuantidade;

	@FXML
	private TableView<MateriaisRetirada> tableViewMateriais;
	@FXML
	private TableColumn<Object, Object> tableColumnNumero;
	@FXML
	private TableColumn<Object, Object> tableColumnDescricao;
	@FXML
	private TableColumn<Object, Object> tableColumnTipo;
	@FXML
	private TableColumn<Object, Object> tableColumnQuantidadeDisponivel;
	@FXML
	private TableColumn<Object, Object> tableColumnQuantidadeDesejada;

	// Declaracao de classes para CRUD
	DatabaseExcel export;
	DaoMaterial dao;
	DaoUsuarioLogado daoLog;

	// Auxiliares
	ObservableList<MateriaisRetirada> observableList;
	List<Material> listMateriais;
	List<MateriaisRetirada> listRetirada;

	/////////////////////////////////////////////////

	// Metodo nativo do JavaFX que é auto-executado ao renderizar a janela
	@Override
	public void initialize(URL location, final ResourceBundle resources) {

		// Evento do botao 'concluir' para finalizar a retirada
		buttonConcluir.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {

				export = new DatabaseExcel();
				export.generate(listRetirada);

				Parent root;

				try {

					for (int i = 0; i < listRetirada.size(); i++) {

						dao = new DaoMaterial();

						int quantAtual = 0;

						for (int j = 0; j < listMateriais.size(); j++) {

							if (listRetirada.get(i).getId() == listMateriais.get(j).getId())
								quantAtual = listMateriais.get(j).getQuantAtual()
										- listRetirada.get(i).getQuantDesejada();
						}

						Material materiaisOld = new Material();
						materiaisOld.setId(listRetirada.get(i).getId());
						materiaisOld.setDescricao(listRetirada.get(i).getDescricao());
						materiaisOld.setTipo(listRetirada.get(i).getTipo());
						materiaisOld.setQuantAtual(quantAtual);

						dao.update(materiaisOld);
					}

					root = FXMLLoader.load(getClass().getResource("/fxml/Acervo.fxml"), resources);
					Stage stage = new Stage();
					stage.setTitle("Acervo");
					stage.setScene(new Scene(root, 700, 500));
					stage.setResizable(false);
					stage.show();
					((Node) (event.getSource())).getScene().getWindow().hide();

					Alert alert = new Alert(Alert.AlertType.INFORMATION);
					alert.setTitle("MENSAGEM");
					alert.setHeaderText("EMPRESTIMO CONCLUÍDO");
					String s = "O relatório do empréstimo já foi gerado.";
					alert.setContentText(s);
					alert.show();

				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});

		/////////////////////////////////////////////////

		// Evento do botao 'Cancelar' para voltar para a janela 'Acevo'
		buttonSair.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				Parent root;
				try {
					root = FXMLLoader.load(getClass().getResource("/fxml/Acervo.fxml"), resources);
					Stage stage = new Stage();
					stage.setTitle("Acervo");
					stage.setScene(new Scene(root, 700, 500));
					stage.setResizable(false);
					stage.show();
					((Node) (event.getSource())).getScene().getWindow().hide();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});

		/////////////////////////////////////////////////

		// DEFINICOES DA TABELA DE MATERIAIS
		tableColumnNumero.setCellValueFactory(new PropertyValueFactory<>("numero"));
		tableColumnDescricao.setCellValueFactory(new PropertyValueFactory<>("descricao"));
		tableColumnTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
		tableColumnQuantidadeDisponivel.setCellValueFactory(new PropertyValueFactory<>("quantAtual"));
		tableColumnQuantidadeDesejada.setCellValueFactory(new PropertyValueFactory<>("quantDesejada"));

		tableColumnNumero.setStyle("-fx-alignment: CENTER;");
		tableColumnDescricao.setStyle("-fx-alignment: CENTER;");
		tableColumnTipo.setStyle("-fx-alignment: CENTER;");
		tableColumnQuantidadeDisponivel.setStyle("-fx-alignment: CENTER;");
		tableColumnQuantidadeDesejada.setStyle("-fx-alignment: CENTER;");

		// Evento da tabela para quando uma linha é seleicionada
		tableViewMateriais.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
			if (newSelection != null) {

				labelDescricaoItemNumero.setVisible(true);

				labelItemNumero.setText(newSelection.getNumero() + "");
				spinnerQuantidade.setDisable(false);
				buttonConfirmar.setDisable(false);

				int maxQuant = listMateriais.get(newSelection.getNumero() - 1).getQuantAtual();
				int initVal = listRetirada.get(newSelection.getNumero() - 1).getQuantDesejada();

				// Inicia o componente 'Spinner', definindo suas caracteristicas como valor
				// inicial, minimo e maximo
				SpinnerValueFactory<Integer> spinnerQuantidadeFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(
						1, maxQuant, initVal);
				this.spinnerQuantidade.setValueFactory(spinnerQuantidadeFactory);
			}
		});

		/////////////////////////////////////////////////

		// Evento do botao 'confirmar' para atualizar os valores da tabela de acordo com
		// o informado no Spinner
		buttonConfirmar.setOnAction(event -> {

			spinnerQuantidade.setDisable(true);
			buttonConfirmar.setDisable(true);

			int row = Integer.parseInt(labelItemNumero.getText()) - 1;
			int quantAtual = listMateriais.get(row).getQuantAtual();
			int quantDesejada = spinnerQuantidade.getValue();

			listRetirada.get(row).setQuantDesejada(quantDesejada);
			listRetirada.get(row).setQuantAtual(quantAtual - quantDesejada);

			observableList = FXCollections.observableArrayList(listRetirada);
			tableViewMateriais.setItems(observableList);
			tableViewMateriais.refresh();

		});
	}

	/////////////////////////////////////////////////

	/*
	 * Nome da Função: setMateriasSelecionados Retorno: Void Objetivo: Inserir
	 * materiais selecionados em uma lista
	 */
	public void setMateriaisSelecionados(List<Material> materiaisSelecionados) {

		listMateriais = materiaisSelecionados;
		listRetirada = new ArrayList<>();
		MateriaisRetirada modelRetirada;

		for (int i = 0; i < materiaisSelecionados.size(); i++) {

			modelRetirada = new MateriaisRetirada();

			modelRetirada.setId(materiaisSelecionados.get(i).getId());
			modelRetirada.setNumero(i + 1);
			modelRetirada.setDescricao(materiaisSelecionados.get(i).getDescricao());
			modelRetirada.setTipo(materiaisSelecionados.get(i).getTipo());
			modelRetirada.setQuantAtual(materiaisSelecionados.get(i).getQuantAtual() - 1);
			modelRetirada.setQuantDesejada(1);

			listRetirada.add(modelRetirada);
		}

		// Adiciona os valores na tabela
		observableList = FXCollections.observableArrayList(listRetirada);
		tableViewMateriais.setItems(observableList);
	}
}
