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
* ===============================
* Alteração
* 
* Data: 06/08/2020
* Responsável: Leonardo Cech
*
* Documentação da Classe
* -------------------------------------------------------
*
* ================================
* Declaração de variáveis
* 	 tableViewMateriais : TableView<Material> Objeto de instância da tabela de materiais
* 		  tableColumnId : TableColumn<Material, Integer> Objeto de instância de coluna Id da tabela de materiais
*  tableColumnDescricao : TableColumn<Material, String>  Objeto de instância de coluna Descrição da tabela de materiais
* 	    tableColumnTipo : TableColumn<Material, String>  Objeto de instância de coluna  da tabela de materiais
* tableColumnQuantidade : TableColumn<Material, Integer> Objeto de instância de coluna  da tabela de materiais
* 
* 		       buttonSair : Button 	Botão 'sair' que fecha a janela atual e abre a janela 'login'
* 		 buttonRelatorios : Button 	Botão 'relatorios' que fecha a janela atual e abre o diretório de relatórios
* buttonCadastrarMaterial : Button 	Evento do botão 'cadastrar material' que fecha a janela atual e abre a janela 'CadastroMaterial'
* 		    buttonUsuario : Button	Evento do botão 'cadastrar usuario' que fecha a janela atual e abre a janela 'CadastroFuncionario'
* 			 buttonQrCode : Button	Evento do botão 'QR code' abre uma tela para selecionar um arquivo caso não tiver webcam
* 		  buttonEmprestar : Button	Evento do botão 'emprestar' que fecha a janela atual e abre a janela 'Retirada'
* 	  buttonDesmarcarTudo : Button	Evento do botão 'Desmarcar tudo' que remove todas as linhas que possam estar selecionadas da tabela
* 		    buttonExcluir : Button	Evento do botão 'Desmarcar tudo' que remove todas as linhas que possam estar selecionadas da tabela
* 			 buttonEditar : Button	Evento do botão 'Editar material' que edita no BD o(s) material(ais) selecionado(s)
* 			 buttonBuscar : Button	Evento do botão 'buscar' que busca e apresenta na tabela os resultados de acordo com a descrição buscada
* 			 labelUsuario : Label	Elemento gráfico para apresentar a mensagem "Olá, (nome do usuário logado)"
*		  textFieldBuscar : TextField
* ================================
*/

package Controller;

// IMPORTAÇÕES DE BIBLIOTECAS

// Importações de bibliotecas nativas do JAVA
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

// Importação de biblioteca externa via Github
import com.github.sarxos.webcam.Webcam;

// Importações de Classes do projeto
import Dao.DaoMaterial;
import Dao.DaoUsuarioLogado;
import Model.Material;
import Model.UsuarioLogado;

// Importações de bibliotecas gráficas do JavaFX
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class Acervo implements Initializable {

	// Instâncias dos componentes do ambiente gráfico
	@FXML private Button buttonSair;
	@FXML private Button buttonRelatorios;
	@FXML private Button buttonCadastrarMaterial;
	@FXML private Button buttonUsuario;
	@FXML private Button buttonQrCode;
	@FXML private Button buttonEmprestar;
	@FXML private Button buttonDesmarcarTudo;
	@FXML private Button buttonExcluir;
	@FXML private Button buttonEditar;
	@FXML private Button buttonBuscar;
	@FXML private Label labelUsuario;
	@FXML private TextField textFieldBuscar;

	// Instâncias da tabela na janela principal 'Acervo'
	@FXML private TableView<Material> tableViewMateriais;
	@FXML private TableColumn<Material, Integer> tableColumnId;
	@FXML private TableColumn<Material, String> tableColumnDescricao;
	@FXML private TableColumn<Material, String> tableColumnTipo;
	@FXML private TableColumn<Material, Integer> tableColumnQuantidade;

	// Declaração de objetos de classes para CRUD
	DaoMaterial dao;
	DaoUsuarioLogado daoLog;

	// Arrays, objetos e variáveis auxiliares
	List<Material> materialList = new ArrayList<>();
	List<Material> materialListValidos = new ArrayList<>(); 
	Material modelMaterial = new Material();
	private UsuarioLogado usuario;
	private int contarConcluido;
	
	/////////////////////////////////////////////////
	
	// Metodo nativo do JavaFX que é auto-executado ao renderizar a janela
	@SuppressWarnings("unchecked") @Override
	public void initialize(URL location, final ResourceBundle resources) {
		
		// Obter se o usuario eh administrador
		usuario = getUsuario();
		labelUsuario.setText("Olá, " + usuario.getNome() + ".");
		
		if (usuario.isAdmin()) {
			buttonCadastrarMaterial.setVisible(true);
			buttonUsuario.setVisible(true);
			buttonExcluir.setVisible(true);
			buttonEditar.setVisible(true);
		}
		else {
			buttonCadastrarMaterial.setVisible(false);
			buttonUsuario.setVisible(false);
			buttonExcluir.setVisible(false);
			buttonEditar.setVisible(false);
		}

		/////////////////////////////////////////////////
		
		// Evento do botao 'sair' que fecha a janela atual e abre a janela 'login'
		buttonSair.setTooltip(new Tooltip("Encerrar sessão e sair para tela de login"));
		buttonSair.setFont(new Font("Calibri", 16));
		buttonSair.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				
				Parent root;
				try {
					
					// Remove do BD os vestigios do ultimo usuario logado
			    		daoLog = new DaoUsuarioLogado();
			    		daoLog.delete();
					
					root = FXMLLoader.load(getClass().getResource("/fxml/Login.fxml"), resources);
					Stage stage = new Stage();
					stage.setTitle("Acervo");
					stage.setScene(new Scene(root, 400, 500));
					stage.setResizable(false);
					stage.getIcons().add(new Image(this.getClass().getResource("/icons/logo.png").toString()));
					stage.show();
					((Node) (event.getSource())).getScene().getWindow().hide();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		
		/////////////////////////////////////////////////
		
		// Evento do botao 'relatorios' que fecha a janela atual e abre o diretorio de relaotorios
		buttonRelatorios.setTooltip(new Tooltip("Relatórios"));
		buttonRelatorios.setFont(new Font("Calibri", 16));
		buttonRelatorios.setOnAction(new EventHandler<ActionEvent>() {
			
			public void handle(ActionEvent event) {
				
				Parent root;
				try {
					root = FXMLLoader.load(getClass().getResource("/fxml/Relatorios.fxml"), resources);
					Stage stage = new Stage();
					stage.setTitle("Relatórios");
					stage.setScene(new Scene(root, 400, 500));
					stage.setResizable(false);
					stage.getIcons().add(new Image(this.getClass().getResource("/icons/logo.png").toString()));
					stage.show();
					((Node) (event.getSource())).getScene().getWindow().hide();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});

		/////////////////////////////////////////////////	
		
		// Evento do botao 'cadastrar material' que fecha a janela atual e abre a janela
		// 'CadastroMaterial'
		buttonCadastrarMaterial.setTooltip(new Tooltip("Cadastrar novos materiais"));
		buttonCadastrarMaterial.setFont(new Font("Calibri", 16));
		buttonCadastrarMaterial.setOnAction(new EventHandler<ActionEvent>() {
			
			// Intanciando variaves auxiliares para validacao
			private boolean descricaoTxt = true, tipoTxt = true;

			public void handle(ActionEvent event) { // Instancia do metodo de evento

				// Instanciando objeto auxiliar
				Material novoMaterial = new Material();

				//--------------------------------
				// Criando dialogo
				Dialog<Material> dialogo = new Dialog<>();
				dialogo.setTitle("Novo material");
				dialogo.setHeaderText("Por favor, preencha os campos:");
				
				//--------------------------------
				// Criando janela grafica a partir do dialogo
				Stage stage = (Stage) dialogo.getDialogPane().getScene().getWindow();
				stage.getIcons().add(new Image(this.getClass().getResource("/icons/criar_material.png").toString()));
				
				// Definicao de fonte utilizado no dialogo
				Font fonte = new Font("Calibri", 16.0);
				
				// Define os tipos de botoes do dialogo
				ButtonType buttonCadastrarMaterial = new ButtonType("Cadastrar", ButtonData.OK_DONE);
				dialogo.getDialogPane().getButtonTypes().addAll(buttonCadastrarMaterial, ButtonType.CANCEL);

				//==============================
		        // Criando base da janela de dialogo
				GridPane grid = new GridPane();
				grid.setHgap(30);
				grid.setVgap(5);
				grid.setPadding(new Insets(10, 30, 10, 30));

				//==============================
		        // Criando componentes
				TextField descricao = new TextField();
				descricao.setPromptText("Descrição");
				descricao.setPrefWidth(250);
				descricao.setPrefHeight(37);
				descricao.setFont(fonte);
				
				//--------------------------------
				
				TextField tipo = new TextField();
				tipo.setPromptText("Tipo");
				tipo.setPrefWidth(100);
				tipo.setPrefHeight(37);
				tipo.setFont(fonte);
				
				//--------------------------------
				Spinner<Integer> quantidade = new Spinner<Integer>();
				SpinnerValueFactory<Integer> spinnerQuantidadeFactory = new SpinnerValueFactory.
						IntegerSpinnerValueFactory( 0, 100000, 1);
		        quantidade.setValueFactory(spinnerQuantidadeFactory);
		        tipo.setPrefWidth(100);
		        quantidade.setPrefHeight(37);

		        //--------------------------------
		        
		        Label labelDescricao = new Label("Insira a descrição:");
		        Label labelTipo = new Label("Insira o tipo:");
		        Label labelQuantidade = new Label("Insira a quantidade:");
		        
		        labelDescricao.setFont(fonte);
		        labelTipo.setFont(fonte);
		        labelQuantidade.setFont(fonte);
		        
		        //==============================
		        // Adicionando componentes na janela de dialogo
		        
				grid.add(labelDescricao, 0, 0);
				grid.add(descricao, 0, 1, 2, 1);
				grid.add(labelTipo, 0, 2);
				grid.add(tipo, 0, 3);
				grid.add(labelQuantidade, 1, 2);
				grid.add(quantidade, 1, 3);
				
				//==============================

				// Habilita ou desabilita o botao 'cadastrar' de acordo com o preenchimento dos campos
				Node buttonCad = dialogo.getDialogPane().lookupButton(buttonCadastrarMaterial);
				buttonCad.setDisable(true);
				
				//////////////////////////////////////////////////////////////////////////
				
				descricao.textProperty().addListener((observable, oldValue, newValue) -> {
					
					// booleano para bloquear o botao 'cadastrar' caso nao haja texto ou ultrapasse 200 caracteres
					this.descricaoTxt = descricao.getText().length() == 0 || descricao.getText().length() > 200; 
					
					try { // Validacao de entrada de texto em TextField
						
						if (!this.descricaoTxt) { // Se possuir texto
							
							// Remove qualquer caracter especial permitindo apenas letras maiusculas e minisculas
							// caracteres numericos de 0 a 9 e espacos
							descricao.setText(descricao.getText().replaceAll("[^a-zA-Z0-9\\u0020]", ""));
							
							// Quando houver espacos repetidos deixar apenas 1 espaco no lugar
							descricao.setText(descricao.getText().replaceAll("\\u0020{2,}", " "));
							
							descricao.setText(descricao.getText().toUpperCase()); // Passa os caracteres para maiusculo
						}
						
					} catch (Exception e) { // Tratamento para evitar excecoes
						System.out.println("Caracter inválido neste TextField");
					}
					// Desabilita o botao se os campos estiverem vazios ou com entradas invalidas
					buttonCad.setDisable(this.tipoTxt || this.descricaoTxt); 
				});
				
				//////////////////////////////////////////////////////////////////////////
				
				tipo.textProperty().addListener((observable, oldValue, newValue) -> {
					
					// booleano para bloquear o botao 'cadastrar' caso nao haja texto ou ultrapasse 200 caracteres
					this.tipoTxt = tipo.getText().length() == 0 || tipo.getText().length() > 200; 
					
					try { // Validacao de entrada de texto em TextField
						
						if (!this.tipoTxt) { // Se possuir texto
							
							// Remove qualquer caracter especial permitindo apenas letras maiusculas e minisculas
							// caracteres numericos de 0 a 9 e espacos
							tipo.setText(tipo.getText().replaceAll("[^a-zA-Z0-9\\u0020]", ""));
							
							// Quando houver espacos repetidos deixar apenas 1 espaco no lugar
							tipo.setText(tipo.getText().replaceAll("\\u0020{2,}", " "));
							
							tipo.setText(tipo.getText().toUpperCase()); // Passa os caracteres para maiusculo
						}
						
					} catch (Exception e) { // Tratamento para evitar excecoes
						System.out.println("Caracter inválido neste TextField");
					}
					
					// Desabilita o botao se os campos estiverem vazios ou com entradas invalidas
					buttonCad.setDisable(this.tipoTxt || this.descricaoTxt); 
				});

				//////////////////////////////////////////////////////////////////////////
				
				// Obtendo os dados entrados e fechando a janela de dialogo
				
				dialogo.getDialogPane().setContent(grid); // Obtendo valores entrados

				Platform.runLater(() -> descricao.requestFocus());

				contarConcluido = 0; // Zera a variavel auxiliar
				dialogo.setResultConverter(dialogButton -> {
					
					// Atribuindo valores entrados ao objeto auxiliar quando o 
					// botao 'cadastrar' for pressionado
				    if (dialogButton == buttonCadastrarMaterial) { contarConcluido++;
				    	
				    	// o metodo trim() remove espacos no inicio e fim do texto, se houver
				    	novoMaterial.setDescricao(descricao.getText().trim()); 
				    	novoMaterial.setTipo(tipo.getText().trim());
				    	novoMaterial.setQuantAtual(Integer.parseInt(quantidade.getValue().toString()));
				    	
				        return novoMaterial;
				    }
				    return null;
				});
				
				Optional<Material> result = dialogo.showAndWait();

				// Manda o requerimento para cadastro no BD
				result.ifPresent(material -> {
					
					// Valida se o novo material ja nao existe no BD
					Material materialModel = new Material();
					List<Material> materiaisBD = dao.listar((Class<Material>) materialModel.getClass());
					
					// Variaveis auxiliares
					boolean materialValido = true; 
					
					for (Material materiais : materiaisBD) {
						
						// Se ja existir um material com mesma descricao e tipo, nao sera editado
						if (material.getDescricao().equals(materiais.getDescricao()) &&
							material.getTipo().equals(materiais.getTipo())) {
							
							Alert alert = new Alert(AlertType.WARNING);
				            alert.setTitle("MENSAGEM");
				            alert.setHeaderText(null);
				            alert.setContentText("A descrição e tipo do material a ser cadastrado já existe.\n"
				            				   + "O cadastro não será concluído.");
				            alert.showAndWait();
				            
				            materialValido = false;
				            contarConcluido--;
				            break;
						}
					}
					
					if (materialValido) dao.insert(material);
				});
				
				preencherTabela(); // Atualizar tabela com os novos valores
				
				// Caso alguma oparacao seja feita
				if (contarConcluido > 0) {
					// Apresenta ao usuario uma mensagem de conclusao
					Alert alert = new Alert(AlertType.INFORMATION);
	                alert.setTitle("MENSAGEM");
	                alert.setHeaderText(null);
	                alert.setContentText("Cadastro concluído");
	                alert.showAndWait();
				}
			}
		});

		/////////////////////////////////////////////////
		
		// Evento do botao 'cadastrar usuario' que fecha a janela atual e abre a janela
		// 'CadastroFuncionario'
		buttonUsuario.setTooltip(new Tooltip("Gerenciar usuários"));
		buttonUsuario.setFont(new Font("Calibri", 16));
		buttonUsuario.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {

				Parent root;
				try {
					root = FXMLLoader.load(getClass().getResource("/fxml/Usuarios.fxml"), resources);
					Stage stage = new Stage();
					stage.setTitle("Gerenciar usuários");
					stage.setScene(new Scene(root, 400, 500));
					stage.setResizable(false);
					stage.getIcons().add(new Image(this.getClass().getResource("/icons/logo.png").toString()));
					stage.show();
					((Node) (event.getSource())).getScene().getWindow().hide();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});

		/////////////////////////////////////////////////
		
		// Evento do botao 'emprestar' que fecha a janela atual e abre a janela
		// 'Retirada'
		buttonEmprestar.setTooltip(new Tooltip("Emprestar os materiais selecionados"));
		buttonEmprestar.setFont(new Font("Calibri", 16));
		buttonEmprestar.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {

				ObservableList<Material> selectedItems = tableViewMateriais.getSelectionModel().getSelectedItems();
				List<Material> materiaisSelecionados = new ArrayList<>();

				for (Material material : materialList) {
					for (int j = 0; j < selectedItems.size(); j++) {

						if (material.getId() == selectedItems.get(j).getId()) {
							materiaisSelecionados.add(material);
							j = selectedItems.size();
						}
					}
				}

				try {
					FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Retirada.fxml"), resources);

					Parent root = loader.load();

					// Manda o List materiais selecionados para a Controller Retirada
					Retirada retirada = loader.getController();
					retirada.setMateriaisSelecionados(materiaisSelecionados);

					Stage stage = new Stage();
					stage.setTitle("Retirada de materiais");
					stage.setScene(new Scene(root, 700, 500));
					stage.setResizable(false);
					stage.getIcons().add(new Image(this.getClass().getResource("/icons/logo.png").toString()));
					stage.show();

					((Node) (event.getSource())).getScene().getWindow().hide();

				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		});
		
		/////////////////////////////////////////////////
		
		// Evento do botao 'buscar' que busca e apresenta na tabela os resultados de acordo
		// com a descrição buscada
		buttonBuscar.setTooltip(new Tooltip("Buscar materiais por descrição"));
		buttonBuscar.setFont(new Font("Calibri", 16));
		buttonBuscar.setDisable(true);
		buttonBuscar.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				
				desmarcarTudo();
				
				// AQUI SE CHAMA O METODO PARA COLOCAR OS DADOS NO ARRAYLIST materialList
				dao = new DaoMaterial();
				materialList = dao.buscar((Class<Material>) modelMaterial.getClass(), textFieldBuscar.getText());
				
				if (materialList.size() > 0) preencherTabela(materialList);
				else {
					preencherTabela();
					
					// Apresenta ao usuario uma mensagem de conclusao
					Alert alert = new Alert(AlertType.INFORMATION);
	                alert.setTitle("MENSAGEM");
	                alert.setHeaderText(null);
	                alert.setContentText("Nenhuma correspondência!");
	                alert.showAndWait();
				}
			}
		});
		
		/////////////////////////////////////////////////
		
		textFieldBuscar.setDisable(true);
		textFieldBuscar.textProperty().addListener((observable, oldValue, newValue) -> {
			
			textFieldBuscar.setText(textFieldBuscar.getText().toUpperCase());
		});
		textFieldBuscar.setTooltip(new Tooltip("*Clique no botão de busca com este campo vazio para listar todos os materiais"));
		
		/////////////////////////////////////////////////
		
		// DEFINICOES DA TABELA DE MATERIAIS
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColumnDescricao.setCellValueFactory(new PropertyValueFactory<>("descricao"));
		tableColumnTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
		tableColumnQuantidade.setCellValueFactory(new PropertyValueFactory<>("quantAtual"));
		
		tableViewMateriais.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

		tableColumnId.setStyle("-fx-alignment: CENTER;");
		tableColumnDescricao.setStyle("-fx-alignment: CENTER;");
		tableColumnTipo.setStyle("-fx-alignment: CENTER;");
		tableColumnQuantidade.setStyle("-fx-alignment: CENTER;");

		preencherTabela();
		
		// Evento da tabela para quando uma linha é selecionada
		tableViewMateriais.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
			if (newSelection != null) {
				
				habilitarbotoes();
				
				if (usuario.isAdmin()) { // Se o usuario for administrador
					
					ObservableList<Material> selectedItems = tableViewMateriais.getSelectionModel().getSelectedItems();
					
					int invalidos = 0;
					for (int i = 0 ; i < selectedItems.size(); i++) {
						if (selectedItems.get(i).getQuantAtual() == 0) invalidos++; 
					}
					// Se houver algum item selecionado com a quantidade 0, 
					// o botao emprestrar eh desabilitado
					if (invalidos > 0) buttonEmprestar.setDisable(true);
					else buttonEmprestar.setDisable(false);
				}
			}
		});
		
		/////////////////////////////////////////////////

		// Evento do botao 'Desmarcar tudo' que remove todas as linhas que possam estar
		// selecionadas da tabela
		buttonDesmarcarTudo.setTooltip(new Tooltip("Remover marcação de todas as linhas selecionadas na tabela"));
		buttonDesmarcarTudo.setFont(new Font("Calibri", 16));
		buttonDesmarcarTudo.setOnAction(event -> { desmarcarTudo(); });
		
		/////////////////////////////////////////////////
		
		// Evento do botao 'Excluir material' que remove do BD o(s) material(ais) selecioado(s)
		buttonExcluir.setTooltip(new Tooltip("Excluir materiais selecionados"));
		buttonExcluir.setFont(new Font("Calibri", 16));
		buttonExcluir.setOnAction(event -> {
			
			// Pega todos os dados das linhas selecionadas e joga em uma List
			ObservableList<Material> selectedItems = tableViewMateriais.getSelectionModel().getSelectedItems();
			List<Material> materiaisSelecionados = new ArrayList<>();

			for (Material material : materialList) {
				for (int j = 0; j < selectedItems.size(); j++) {

					if (material.getId() == selectedItems.get(j).getId()) {
						materiaisSelecionados.add(material);
						j = selectedItems.size();
					}
				}
			}
			
			// Executa o comando delete do BD
			for (int i = 0; i < materiaisSelecionados.size(); i++) {
				
				dao.delete(modelMaterial, materiaisSelecionados.get(i).getId());
				
				for (int j = 0; j < materialList.size(); j++) {
					
					if (materialList.get(j).getId() == materiaisSelecionados.get(i).getId()) {
						materialList.remove(j);
					}
				}
			}			
			
			preencherTabela();
            desmarcarTudo();
            
            // Apresenta ao usuario uma mensagem de conclusao
			Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("MENSAGEM");
            alert.setHeaderText(null);
            alert.setContentText(materiaisSelecionados.size() == 1 ? 
		       				     "Exclusão concluída: 1 material excluído." :
		       				     "Exclusões concluídas: " + materiaisSelecionados.size() + " materiais excluídos.");
            alert.showAndWait();
		});
		
		/////////////////////////////////////////////////
		
		// Evento do botao 'Editar material' que edita no BD o(s) material(ais) selecionado(s)
		buttonEditar.setTooltip(new Tooltip("Editar materiais selecionados"));
		buttonEditar.setFont(new Font("Calibri", 16));
		buttonEditar.setOnAction(new EventHandler<ActionEvent>() {
			
			private boolean descricaoTxt = true, tipoTxt = true;

			public void handle(ActionEvent event) {
				
				ObservableList<Material> selectedItems = tableViewMateriais.getSelectionModel().getSelectedItems();
				List<Material> materiaisSelecionados = new ArrayList<>();

				for (Material material : materialList) {
					for (int j = 0; j < selectedItems.size(); j++) {

						if (material.getId() == selectedItems.get(j).getId()) {
							materiaisSelecionados.add(material);
							j = selectedItems.size();
						}
					}
				}
				
				contarConcluido = 0; // Zera o contador auxilizar
				
				for (Material material : materiaisSelecionados) {
					
					// Instanciando objeto auxiliar
					Material novoMaterial = new Material();

					//--------------------------------
					// Criando dialogo
					Dialog<Material> dialogo = new Dialog<>();
					dialogo.setTitle("Editar material");
					dialogo.setHeaderText("Por favor, edite o(s) campo(s):");
					
					//--------------------------------
					// Criando janela grafica a partir do dialogo
					Stage stage = (Stage) dialogo.getDialogPane().getScene().getWindow();
					stage.getIcons().add(new Image(this.getClass().getResource("/icons/editar.png").toString()));
					
					// Definicao de fonte utilizado no dialogo
					Font fonte = new Font("Calibri", 16.0);
					
					// Define os tipos de botoes do dialogo
					ButtonType buttonEditarMaterial = new ButtonType("Editar", ButtonData.OK_DONE);
		
					dialogo.getDialogPane().getButtonTypes().addAll(buttonEditarMaterial, ButtonType.CANCEL);
	
					//==============================
					// Criando base da janela de dialogo
					GridPane grid = new GridPane();
					grid.setHgap(30);
					grid.setVgap(5);
					grid.setPadding(new Insets(10, 30, 10, 30));
	
					//==============================
					// Criando componentes
					TextField descricao = new TextField();
					descricao.setPromptText("Descrição");
					descricao.setText(material.getDescricao());
					descricao.setPrefWidth(250);
					descricao.setPrefHeight(37);
					descricao.setFont(fonte);
					
					//--------------------------------
					
					TextField tipo = new TextField();
					tipo.setPromptText("Tipo");
					tipo.setText(material.getTipo());
					tipo.setPrefWidth(100);
					tipo.setPrefHeight(37);
					tipo.setFont(fonte);
					
					//--------------------------------
					
					Spinner<Integer> quantidade = new Spinner<Integer>();
					SpinnerValueFactory<Integer> spinnerQuantidadeFactory = new SpinnerValueFactory.
							IntegerSpinnerValueFactory( 0, 100000, material.getQuantAtual());
			        quantidade.setValueFactory(spinnerQuantidadeFactory);
			        tipo.setPrefWidth(100);
			        quantidade.setPrefHeight(37);
	
			        //--------------------------------
			        
			        Label labelItem = new Label("Código do item: " + material.getId());
			        Label labelDescricao = new Label("Editar descrição:");
			        Label labelTipo = new Label("Editar tipo:");
			        Label labelQuantidade = new Label("Editar quantidade:");
			        
			        labelItem.setFont(fonte);
			        labelDescricao.setFont(fonte);
			        labelTipo.setFont(fonte);
			        labelQuantidade.setFont(fonte);
			        
			        //==============================
			        // Adicionando componentes na janela de dialogo
			        
			       	grid.add(labelItem, 0, 0);
					grid.add(labelDescricao, 0, 1);
					grid.add(descricao, 0, 2, 2, 2);
					grid.add(labelTipo, 0, 4);
					grid.add(tipo, 0, 5);
					grid.add(labelQuantidade, 1, 4);
					grid.add(quantidade, 1, 5);
	
					//==============================
					
					// Habilita ou desabilita o botao 'editar' de acordo com o preenchimento dos campos
					Node buttonEdit = dialogo.getDialogPane().lookupButton(buttonEditarMaterial);
					
					//////////////////////////////////////////////////////////////////////////
					
					descricao.textProperty().addListener((observable, oldValue, newValue) -> {
						
						// booleano para bloquear o botao 'editar' caso nao haja texto ou ultrapasse 200 caracteres
						this.descricaoTxt = descricao.getText().length() == 0 || descricao.getText().length() > 200; 
						
						try { // Validacao de entrada de texto em TextField
							
							if (!this.descricaoTxt) { // Se possuir texto
								
								// Remove qualquer caracter especial permitindo apenas letras maiusculas e minisculas
								// caracteres numericos de 0 a 9 e espacos
								descricao.setText(descricao.getText().replaceAll("[^a-zA-Z0-9\\u0020]", ""));
								
								// Quando houver espacos repetidos deixar apenas 1 espaco no lugar
								descricao.setText(descricao.getText().replaceAll("\\u0020{2,}", " "));
							}
							
						} catch (Exception e) { // Tratamento para evitar excecoes
							System.out.println("Caracter inválido neste TextField");
						}
						
						// Desabilita o botao se os campos estiverem vazios ou com entradas invalidas
						buttonEdit.setDisable(this.tipoTxt || this.descricaoTxt);
					});
					
					//////////////////////////////////////////////////////////////////////////
					
					tipo.textProperty().addListener((observable, oldValue, newValue) -> {
						
						// booleano para bloquear o botao 'editar' caso nao haja texto ou ultrapasse 200 caracteres
						this.tipoTxt = tipo.getText().length() == 0 || tipo.getText().length() > 200; 
						
						try { // Validacao de entrada de texto em TextField
							
							if (!this.tipoTxt) { // Se possuir texto
								
								// Remove qualquer caracter especial permitindo apenas letras maiusculas e minisculas
								// caracteres numericos de 0 a 9 e espacos
								tipo.setText(tipo.getText().replaceAll("[^a-zA-Z0-9\\u0020]", ""));
								
								// Quando houver espacos repetidos deixar apenas 1 espaco no lugar
								tipo.setText(tipo.getText().replaceAll("\\u0020{2,}", " "));
							}
							
						} catch (Exception e) { // Tratamento para evitar excecoes
							System.out.println("Caracter inválido neste TextField");
						}
						
						// Desabilita o botao se os campos estiverem vazios ou com entradas invalidas
						buttonEdit.setDisable(this.tipoTxt || this.descricaoTxt);
					});
	
					//////////////////////////////////////////////////////////////////////////
						
					//Obtendo os dados entrados e fechando a janela de dialogo
					dialogo.getDialogPane().setContent(grid);
	
					Platform.runLater(() -> descricao.requestFocus());
	
					dialogo.setResultConverter(dialogButton -> {
					    
						// Atribuindo valores entrados ao objeto auxiliar quando o 
						// botao 'cadastrar' for pressionado
						if (dialogButton == buttonEditarMaterial) { contarConcluido++;
					    	
						// o metodo trim() remove espacos no inicio e fim do texto, se houver
					    	novoMaterial.setId(material.getId());
					    	novoMaterial.setDescricao(descricao.getText().trim().toUpperCase());
					    	novoMaterial.setTipo(tipo.getText().trim().toUpperCase());
					    	novoMaterial.setQuantAtual(Integer.parseInt(quantidade.getValue().toString()));
					    	
					        return novoMaterial;
					    }
					    return null;
					});
	
					Optional<Material> result = dialogo.showAndWait();
	
					result.ifPresent(materialEditado -> {
						
						// Valida se o novo material ja nao existe no BD
						Material materialModel = new Material();
						List<Material> materiaisBD = dao.listar((Class<Material>) materialModel.getClass());
						
						// Variaveis auxiliares
						boolean materialValido = true; 

						for (Material materiais : materiaisBD) {
							
							// Se ja existir um material com mesma descricao e tipo, nao sera editado
							if (materialEditado.getId() != materiais.getId() &&
								materialEditado.getDescricao().equals(materiais.getDescricao()) &&
								materialEditado.getTipo().equals(materiais.getTipo())) {
								
								Alert alert = new Alert(AlertType.WARNING);
					            alert.setTitle("MENSAGEM");
					            alert.setHeaderText(null);
					            alert.setContentText("A descrição e tipo do material a ser editado já existe.\n"
					            				   + "Esta edição não será concluída.");
					            alert.showAndWait();
					            
					            materialValido = false;
					            contarConcluido--;
					            break;
							}
						}
						
						if (materialValido) dao.update(materialEditado);
					});
				}
				
				preencherTabela();
				desmarcarTudo();
				
				// Caso alguma oparacao seja feita
				if (contarConcluido > 0) {
					// Apresenta ao usuario uma mensagem de conclusao
					Alert alert = new Alert(AlertType.INFORMATION);
	                		alert.setTitle("MENSAGEM");
	                		alert.setHeaderText(null);
	                		alert.setContentText(contarConcluido == 1 ? 
				       				     "Edição concluída: 1 material editado." :
				       				     "Edições concluídas: " + contarConcluido + " materiais editados.");
	                alert.showAndWait();
				}
			}
		});
				
		/////////////////////////////////////////////////

		// Evento do botao 'QR code' abre uma tela para selecionar um arquivo caso nao
		// tiver webcam
		/* ================================
		* buttonQrCode Event
	 	* Retorno: void
		* Objetivo: Evento do botao 'QR code' abre o dispositivo padrao de entrada de video caso nao tiver, 
		* abre uma tela para selecionar um arquivo de imagem
		* Parâmetros input: Event
		* Parâmetros output: Void
	 	* ================================
		*/
		buttonQrCode.setTooltip(new Tooltip("Selecionar materiais por Código QR"));
		buttonQrCode.setFont(new Font("Calibri", 16));
		buttonQrCode.setOnAction(event -> {
			
			Webcam webcam = Webcam.getDefault();

			if (webcam == null) {
				
				FileChooser fileChooser = new FileChooser();
				Stage stage = new Stage();
				File selectedFile = fileChooser.showOpenDialog(stage);
				
				try {
					
					int contador = 0;
					int id = Integer.parseInt(LeitorQRCode.decodeQRCode(selectedFile));
					
					for(int i = 0; i < tableViewMateriais.getItems().size(); i++) {
						
						if (tableViewMateriais.getItems().get(i).getId() == id) { contador++;
							tableViewMateriais.getSelectionModel().select(i);
						}
					}
					
					if (contador == 0) {
						Alert alert = new Alert(Alert.AlertType.WARNING);
						alert.setTitle("MENSAGEM DO SISTEMA");
						alert.setHeaderText("SEM RESULTADOS	");
						String s ="Nenhum resultado obtido com o código QR fornecido. "
								+ "Verifique se o código QR corresponde a um código cadastrado. "
								+ "Caso seja válido, o material pode não possuir unidades disponíveis.";
						alert.setContentText(s);
						alert.show();
					}
					
				} catch (IOException e) {
					try {
						System.out.println(LeitorQRCode.decodeQRCode(selectedFile));
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}	
				
			} else {
				
				ObservableList<Material> materiais = tableViewMateriais.getItems();
				@SuppressWarnings("unused")
				Camera camera = new Camera(tableViewMateriais, materiais);
			}

		});
	}
	
	/////////////////////////////////////////////////
	
	@SuppressWarnings("unchecked")
	public void preencherTabela() {
		
		dao = new DaoMaterial();
		materialList = dao.listar((Class<Material>) modelMaterial.getClass());
		
		ObservableList<Material> observableList;
		
		materialListValidos = new ArrayList<>();
		
		for (Material material : materialList) {
			if (material.getQuantAtual() > 0) materialListValidos.add(material);
		}
		
		
		buttonQrCode.setDisable(true);
		buttonBuscar.setDisable(true);
		textFieldBuscar.setDisable(true);
		tableViewMateriais.setDisable(true);
		
		
		if (materialListValidos.size() > 0) {
			buttonQrCode.setDisable(false);
			tableViewMateriais.setDisable(false);
			textFieldBuscar.setDisable(false);
			buttonBuscar.setDisable(false);
		}
		
		if (usuario.isAdmin()) observableList = FXCollections.observableArrayList(materialList);
		else observableList = FXCollections.observableArrayList(materialListValidos);
		
		tableViewMateriais.setItems(observableList);
		tableViewMateriais.refresh();
	}
	
	/////////////////////////////////////////////////
	/*
	* preencherTabela(List<Material> list)
	* Retorno: void
	* Objetivo: Preenche a tabela com os itens selecionados da tabela de materiais
	* Parâmetro de entrada:
	* 			list: tipo Material (representa a lista de materias selecionados)
	* Parâmetro de saida:
	* 			tableViewMateriais.setItems(observableList) : tipo Tabela (seta os itens dentro da tabela)
	*/

	public void preencherTabela(List<Material> list) {
		
		ObservableList<Material> observableList;
		
		materialListValidos = new ArrayList<>();
		
		for (Material material : list) {
			if (material.getQuantAtual() > 0) materialListValidos.add(material);
		}
		
		
		buttonQrCode.setDisable(true);
		buttonBuscar.setDisable(true);
		textFieldBuscar.setDisable(true);
		tableViewMateriais.setDisable(true);
		
		
		if (materialListValidos.size() > 0) {
			buttonQrCode.setDisable(false);
			tableViewMateriais.setDisable(false);
			textFieldBuscar.setDisable(false);
			buttonBuscar.setDisable(false);
		}
		
		if (usuario.isAdmin()) observableList = FXCollections.observableArrayList(materialList);
		else observableList = FXCollections.observableArrayList(materialListValidos);
		
		tableViewMateriais.setItems(observableList);
		tableViewMateriais.refresh();
	}

	/////////////////////////////////////////////////
	
	/*
	* Nome da Função: MensagemNotFound
	* Retorno: Void
	* Objetivo: Apresentar mensagem caso nao seja encontrado o código QR informado
	* Parâmetro de entrada:
	* Parâmetro de saida:
	*									s: tipo String(Mensagem de erro a ser exibida)
	*									Alert: tipo alert (Janela de alerta a erro a ser exibido para o usuário)	
	*/
	public void mensagemNotFound() {
		Alert alert = new Alert(Alert.AlertType.WARNING);
		alert.setTitle("MENSAGEM DO SISTEMA");
		alert.setHeaderText("SEM RESULTADOS	");
		String s ="Nenhum resultado obtido com o código QR fornecido. "
				+ "Verifique se o código QR corresponde a um código cadastrado. "
				+ "Caso seja válido, o material pode não possuir unidades disponíveis.";
		alert.setContentText(s);
		alert.show();
	}
	
	/////////////////////////////////////////////////
	
	/*
	getUsuario()
	retorno: UsuarioLogado
	objetivo: capturar o usuario que realizou o login, retornando os dados do mesmo
	Parametro de entrada: 
						getUsuario: tipo UsuarioLogado
	Parametro de saida:
						usuario: tipo UsuarioLogado
	*/

	@SuppressWarnings("unchecked")
	public UsuarioLogado getUsuario() {
	    
		daoLog = new DaoUsuarioLogado();
		
	 	UsuarioLogado usuario = new UsuarioLogado();
	 	usuario = daoLog.select((Class<UsuarioLogado>) usuario.getClass());
	 	
	 	return usuario;
	}
	
	/////////////////////////////////////////////////
	/*
	desmarcarTudo()
	Retorno: void
	Objetivo: Desmarcar todas as opções de itens selecionados na tabela
	*/
	
	public void desmarcarTudo() { // Acao de'desmarcar tudo' 
		tableViewMateriais.getSelectionModel().clearSelection();
		buttonDesmarcarTudo.setDisable(true);
		buttonEmprestar.setDisable(true);
		buttonExcluir.setDisable(true);
		buttonEditar.setDisable(true);
	}
	
	/////////////////////////////////////////////////
	/*
	habilitarbotoes()
	retorno: void
	objetivo: habilitar os botões quando um produto estiver selecionado na tabela
	*/
	
	public void habilitarbotoes() {
		buttonDesmarcarTudo.setDisable(false);
		buttonEmprestar.setDisable(false);
		buttonExcluir.setDisable(false);
		buttonEditar.setDisable(false);
	}
}