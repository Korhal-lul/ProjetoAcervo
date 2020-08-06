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
* Classe de gerenciamento dos usuários
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
* Data: 06/08/2020
* Responsável: Braian Costa Zapelini
*
* ================================
* Declaração de variáveis
* 	 tableViewUsuarios : TableView<Usuario> Objeto de instância da tabela de usuarios
* 	 tableColumnId : TableColumn<Usuario, Integer> Objeto de instância da coluna 'id' da tabela de usuarios
* 	 tableColumnNome : TableColumn<Usuario, String> Objeto de instância da coluna 'nome' da tabela de usuarios
* 	 tableColumnAdmin : TableColumn<Usuario, Boolean> Objeto de instância da coluna 'admin' da tabela de usuarios
* 
* 	 buttonSair : Button Botão 'sair' que fecha a janela atual e abre a janela 'login'
* 	 buttonCadastrarUsuario : Button Botão 'cadastrar' que realiza o cadastro do usuario no banco de dados
* 	 buttonDesmarcarTudo : Button Botão 'desmarcar' que desmarca todas as opções selecionadas na tabela
* 	 buttonExcluir : Button Botão 'excluir' que exclui um(s) determinado(s) item(s) da tabela
* 	 buttonEditar : Button Botão 'editar' que abrirá uma nova guia para poder modificar seus dados
* 	 buttonBuscar : Button Botão 'buscar' irá buscar um determinado item solicidado
* 	 textFieldBuscar : TextField
* ================================
*/

package Controller;

// IMPORTAÇÕES DE BIBLIOTECAS

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import Dao.DaoUsuario;
import Dao.DaoUsuarioLogado;
import Model.Usuario;
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
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class GerenciarUsuarios implements Initializable {

	// Instancia dos componentes do ambiente grafico
	@FXML private Button buttonSair;
	@FXML private Button buttonCadastrarUsuario;
	@FXML private Button buttonDesmarcarTudo;
	@FXML private Button buttonExcluir;
	@FXML private Button buttonEditar;
	@FXML private Button buttonBuscar;
	@FXML private TextField textFieldBuscar;

	@FXML private TableView<Usuario> tableViewUsuarios;
	@FXML private TableColumn<Usuario, Integer> tableColumnId;
	@FXML private TableColumn<Usuario, String> tableColumnNome;
	@FXML private TableColumn<Usuario, Boolean> tableColumnAdmin;

	// Declaracao de classes para CRUD
	DaoUsuario dao;
	DaoUsuarioLogado daoLog;

	// Auxiliares
	List<Usuario> usuarioList = new ArrayList<>();
	Usuario modelUsuario = new Usuario();
	private int contarConcluido;

	/////////////////////////////////////////////////
	
	// Metodo nativo do JavaFX que é auto-executado ao renderizar a janela
	@SuppressWarnings("unchecked")
	@Override
	public void initialize(URL location, final ResourceBundle resources) {

		// Evento do botao 'sair' que fecha a janela atual e abre a janela 'acervo'
		buttonSair.setTooltip(new Tooltip("Voltar para a tela Acervo"));
		buttonSair.setFont(new Font("Calibri", 16));
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

		// Evento do botao 'cadastrar usuario'
		buttonCadastrarUsuario.setTooltip(new Tooltip("Cadastrar novo usuário"));
		buttonCadastrarUsuario.setFont(new Font("Calibri", 16));
		buttonCadastrarUsuario.setOnAction(new EventHandler<ActionEvent>() {
			
			private boolean nomeTxt = true, senhaTxt = true;

			public void handle(ActionEvent event) {

				// Instanciando objeto auxiliar
				Usuario novoUsuario = new Usuario();

				//--------------------------------
				// Criando dialogo
				Dialog<Usuario> dialogo = new Dialog<>();
				dialogo.setTitle("Novo usuário");
				dialogo.setHeaderText("Por favor, preencha os campos:");

				//--------------------------------
				// Criando janela grafica a partir do dialogo
				Stage stage = (Stage) dialogo.getDialogPane().getScene().getWindow();
				stage.getIcons().add(new Image(this.getClass().getResource("/icons/criar_usuario.png").toString()));

				// Definicao de fonte utilizado no dialogo
				Font fonte = new Font("Calibri", 16.0);

				// Define os tipos de botoes do dialogo
				ButtonType buttonCadastrarUsuario = new ButtonType("Cadastrar", ButtonData.OK_DONE);
				dialogo.getDialogPane().getButtonTypes().addAll(buttonCadastrarUsuario, ButtonType.CANCEL);

				//==============================
				// Criando base da janela de dialogo
				GridPane grid = new GridPane();
				grid.setHgap(30);
				grid.setVgap(5);
				grid.setPadding(new Insets(10, 30, 10, 30));

				//==============================
				// Criando componentes
				
				TextField nome = new TextField();
				nome.setPromptText("Nome de usuário");
				nome.setPrefWidth(250);
				nome.setPrefHeight(37);
				nome.setFont(fonte);
				
				//--------------------------------
				
				TextField senha = new TextField();
				senha.setPromptText("Senha");
				senha.setPrefWidth(250);
				senha.setPrefHeight(37);
				senha.setFont(fonte);
				
				//--------------------------------
				
				CheckBox admin = new CheckBox("Administrador");
				HBox hboxAdmin = new HBox(admin);
				
				//--------------------------------
				
		        Label labelNome = new Label("Insira o nome de usuário:");
		        Label labelSenha = new Label("Insira a senha:");
		        
		        labelNome.setFont(fonte);
		        labelSenha.setFont(fonte);
		        
		        //==============================
		     	// Adicionando componentes na janela de dialogo
		        
				grid.add(labelNome, 0, 0);
				grid.add(nome, 0, 1, 2, 1);
				grid.add(labelSenha, 0, 2);
				grid.add(senha, 0, 3, 2, 3);
				grid.add(hboxAdmin, 0, 8);

				//==============================
				
				// Habilita ou desabilita o botao 'cadastrar' de acordo com o preenchimento dos campos
				Node buttonCad = dialogo.getDialogPane().lookupButton(buttonCadastrarUsuario);
				buttonCad.setDisable(true);

				nome.textProperty().addListener((observable, oldValue, newValue) -> {
					
					// booleano para bloquear o botao 'editar' caso nao haja texto ou ultrapasse 200 caracteres
					this.nomeTxt = nome.getText().length() == 0 || nome.getText().length() > 200; 
					
					try { // Validacao de entrada de texto em TextField
						
						if (!this.nomeTxt) { // Se possuir texto
							
							// Remove qualquer caracter especial permitindo apenas letras maiusculas e minisculas
							// caracteres numericos de 0 a 9 e espacos
							nome.setText(nome.getText().replaceAll("[^a-zA-Z0-9\\u0020]", ""));
							
							// Quando houver espacos repetidos deixar apenas 1 espaco no lugar
							nome.setText(nome.getText().replaceAll("\\u0020{2,}", " "));
							
							nome.setText(nome.getText().toUpperCase()); // Passa os caracteres para maiusculas
						}
						
					} catch (Exception e) { // Tratamento para evitar excecoes
						System.out.println("Caracter inválido neste TextField");
					}
					
					// Desabilita o botao se os campos estiverem vazios ou com entradas invalidas
					buttonCad.setDisable(!(this.senhaTxt == false && this.nomeTxt == false));
				});
				
				//////////////////////////////////////////////////////////////////////////
				
				senha.textProperty().addListener((observable, oldValue, newValue) -> {
				
					// booleano para saber se a senha possui menos de 4 ou mais de 20 caracteres, bloqueando o botao 'cadastrar'
					this.senhaTxt = senha.getText().length() < 4 || senha.getText().length() > 20; 
					
					// Desabilita o botao se os campos estiverem vazios ou com entradas invalidas
					buttonCad.setDisable(this.senhaTxt || this.nomeTxt);
				});

				//////////////////////////////////////////////////////////////////////////
				
				// Obtendo os dados entrados e fechando a janela de dialogo
				
				dialogo.getDialogPane().setContent(grid);

				Platform.runLater(() -> nome.requestFocus());

				contarConcluido = 0; // Zera a variavel auxiliar
				dialogo.setResultConverter(dialogButton -> {
					
					// Atribuindo valores entrados ao objeto auxiliar quando o 
					// botao 'cadastrar' for pressionado
				    if (dialogButton == buttonCadastrarUsuario) { contarConcluido++;
				    	
				    	// o metodo trim() remove espacos no inicio e fim do texto, se houver
				    	novoUsuario.setNome(nome.getText().trim());
				    	novoUsuario.setSenha(senha.getText());
				    	novoUsuario.setAdmin(admin.isSelected());
				    	
				        return novoUsuario;
				    }
				    return null;
				});

				Optional<Usuario> result = dialogo.showAndWait();

				// Manda o requerimento para cadastro no BD
				result.ifPresent(usuario -> {
					
					// Valida se o novo material ja nao existe no BD
					Usuario usuarioModel = new Usuario();
					List<Usuario> usuarioBD = dao.listar((Class<Usuario>) usuarioModel.getClass());
					
					// Variaveis auxiliares
					boolean usuarioValido = true; 
					
					for (Usuario usuarios : usuarioBD) {
						
						// Se ja existir um material com mesma descricao e tipo, nao sera editado
						if (usuario.getId() != usuarios.getId() &&
							usuario.getNome().equals(usuarios.getNome())){
							
							Alert alert = new Alert(AlertType.WARNING);
				            alert.setTitle("MENSAGEM");
				            alert.setHeaderText(null);
				            alert.setContentText("O usuário já existe.\n"
				            				   + "O cadastro não será concluído.");
				            alert.showAndWait();
				            
				            usuarioValido = false;
				            contarConcluido--;
				            break;
						}
					}
					
					if (usuarioValido) dao.insert(usuario);
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
		
		// Evento do botao 'buscar' que busca e apresenta na tabela os resultados de acordo
		// com o nome de usuário informado
		buttonBuscar.setTooltip(new Tooltip("Buscar usuários por nome de usuário"));
		buttonBuscar.setFont(new Font("Calibri", 16));
		buttonBuscar.setDisable(true);
		buttonBuscar.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				
				desmarcarTudo();
				
				// AQUI SE CHAMA O METODO PARA COLOCAR OS DADOS NO ARRAYLIST materialList
				dao = new DaoUsuario();
				usuarioList = dao.buscar((Class<Usuario>) modelUsuario.getClass(), textFieldBuscar.getText());
				
				if (usuarioList.size() > 0) preencherTabela(usuarioList);
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
		textFieldBuscar.setTooltip(new Tooltip("*Clique no botão de busca com este campo vazio para listar todos os usuários"));
		textFieldBuscar.textProperty().addListener((observable, oldValue, newValue) -> {
			
			textFieldBuscar.setText(textFieldBuscar.getText().toUpperCase());
		});
		
		/////////////////////////////////////////////////

		// DEFINICOES DA TABELA DE MATERIAIS
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColumnNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
		tableColumnAdmin.setCellValueFactory(new PropertyValueFactory<>("admin"));
		
		tableViewUsuarios.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

		tableColumnId.setStyle("-fx-alignment: CENTER;");
		tableColumnNome.setStyle("-fx-alignment: CENTER;");
		tableColumnAdmin.setStyle("-fx-alignment: CENTER;");

		preencherTabela();
		
		// Evento da tabela para quando uma linha é selecionada
		tableViewUsuarios.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
			
			if (newSelection != null) {
				
				habilitarbotoes();
				
				ObservableList<Usuario> selectedItems = tableViewUsuarios.getSelectionModel().getSelectedItems();
				
				// Desabilita os botoes 'excluir' e 'editar' caso o admin root do sistema
				// esteja selecionado
				for (int i = 0 ; i < selectedItems.size(); i++) {
					if (selectedItems.get(i).getNome().equals("ADMIN") &&
						selectedItems.get(i).isAdmin()) {
						buttonEditar.setDisable(true);
						buttonExcluir.setDisable(true);
						break;
					} 
					
				}
			}
		});

		/////////////////////////////////////////////////
		
		// Evento do botao 'Desmarcar tudo' que remove todas as linhas que possam estar
		// selecionadas da tabela
		buttonDesmarcarTudo.setTooltip(new Tooltip("Remover marcação de todas as linhas selecionadas na tabela"));
		buttonDesmarcarTudo.setFont(new Font("Calibri", 12));
		buttonDesmarcarTudo.setOnAction(event -> { desmarcarTudo(); });
		
		/////////////////////////////////////////////////
		
		// Evento do botao 'Excluir material' que remove do BD o(s) material(ais) selecioado(s)
		buttonExcluir.setTooltip(new Tooltip("Excluir usuário(s) selecionado(s)"));
		buttonExcluir.setFont(new Font("Calibri", 16));
		buttonExcluir.setOnAction(event -> {
			
			// Pega todos os dados das linhas selecionadas e joga em uma List
			ObservableList<Usuario> selectedItems = tableViewUsuarios.getSelectionModel().getSelectedItems();
			List<Usuario> usuariosSelecionados = new ArrayList<>();

			for (Usuario usuario : usuarioList) {
				for (int j = 0; j < selectedItems.size(); j++) {

					if (usuario.getId() == selectedItems.get(j).getId()) {
						usuariosSelecionados.add(usuario);
						j = selectedItems.size();
					}
				}
			}
			
			// Executa o comando delete do BD
			for (int i = 0; i < usuariosSelecionados.size(); i++) {
				
				dao.delete(modelUsuario, usuariosSelecionados.get(i).getId());
				
				for (int j = 0; j < usuarioList.size(); j++) {
					
					if (usuarioList.get(j).getId() == usuariosSelecionados.get(i).getId()) {
						usuarioList.remove(j);
					}
				}
			}			
			
			// Atualiza a tabela que passara a nao conter os item excluidos
            tableViewUsuarios.setItems(FXCollections.observableArrayList(usuarioList));
            tableViewUsuarios.refresh();
            
            desmarcarTudo();
            
            // Apresenta ao usuario uma mensagem de conclusao
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("MENSAGEM");
            alert.setHeaderText(null);
            alert.setContentText(usuariosSelecionados.size() == 1 ? 
	       				     "Exclusão concluída: 1 usuário excluído." :
	       				     "Exclusões concluídas: " + usuariosSelecionados.size() + " usuários excluídos.");
            alert.showAndWait();
		});
		
		/////////////////////////////////////////////////
		
		// Evento do botao 'Editar usuario' que edita no BD o(s) usuario(s) selecioado(s)
		buttonEditar.setTooltip(new Tooltip("Editar usuários selecionados"));
		buttonEditar.setFont(new Font("Calibri", 16));
		buttonEditar.setOnAction(new EventHandler<ActionEvent>() {
			
			private boolean nomeTxt = true, senhaTxt = true;

			public void handle(ActionEvent event) {
				
				// Instanciando List auxiliar
				ObservableList<Usuario> selectedItems = tableViewUsuarios.getSelectionModel().getSelectedItems();
				List<Usuario> usuariosSelecionados = new ArrayList<>();

				// Obtendo usuarios selecionados
				for (Usuario usuario : usuarioList) {
					for (int j = 0; j < selectedItems.size(); j++) {

						if (usuario.getId() == selectedItems.get(j).getId()) {
							usuariosSelecionados.add(usuario);
							j = selectedItems.size();
						}
					}
				}
				
				contarConcluido = 0; // Zera a variavel auxiliar
				// Para cada usuário, execute o metodo
				for (Usuario usuario : usuariosSelecionados) {
					
					// Instanciando objeto auxiliar
					Usuario novoUsuario = new Usuario();
					
					//--------------------------------
					// Criando dialogo
					Dialog<Usuario> dialogo = new Dialog<>();
					dialogo.setTitle("Editar usuário");
					dialogo.setHeaderText("Por favor, edite o(s) campo(s):");
					
					//--------------------------------
					// Criando janela grafica a partir do dialogo
					Stage stage = (Stage) dialogo.getDialogPane().getScene().getWindow();
					stage.getIcons().add(new Image(this.getClass().getResource("/icons/editar.png").toString()));
					
					// Definicao de fonte utilizado no dialogo
					Font fonte = new Font("Calibri", 16.0);
					
					// Define os tipos de botoes do dialogo
					ButtonType buttonEditarUsuario = new ButtonType("Editar", ButtonData.OK_DONE);
		
					dialogo.getDialogPane().getButtonTypes().addAll(buttonEditarUsuario, ButtonType.CANCEL);
	
					//==============================
					// Criando base da janela de dialogo
					GridPane grid = new GridPane();
					grid.setHgap(30);
					grid.setVgap(5);
					grid.setPadding(new Insets(10, 30, 10, 30));
	
					//==============================
					// Criando componentes
					TextField nome = new TextField();
					nome.setPromptText("Nome de usuário");
					nome.setText(usuario.getNome());
					nome.setPrefWidth(250);
					nome.setPrefHeight(37);
					nome.setFont(fonte);
					
					//--------------------------------
					
					TextField senha = new TextField();
					senha.setPromptText("Senha");
					senha.setText(usuario.getSenha());
					senha.setPrefWidth(250);
					senha.setPrefHeight(37);
					senha.setFont(fonte);
					
					//--------------------------------
					
					CheckBox admin = new CheckBox("Administrador");
					admin.setSelected(usuario.isAdmin());
					HBox hboxAdmin = new HBox(admin);
					
					//--------------------------------
					
			        Label labelUsuario = new Label("Código do usuário: " + usuario.getId());
			        Label labelNome = new Label("Editar nome de usuário:");
			        Label labelSenha = new Label("Editar senha:");
			        
			        labelUsuario.setFont(fonte);
			        labelNome.setFont(fonte);
			        labelSenha.setFont(fonte);
			        
			        //==============================
			        // Adicionando componentes na janela de dialogo
			        
			        grid.add(labelUsuario, 0, 0);
					grid.add(labelNome, 0, 1);
					grid.add(nome, 0, 2, 2, 2);
					grid.add(labelSenha, 0, 4);
					grid.add(senha, 0, 5, 2, 5);
					grid.add(hboxAdmin, 0, 11);
	
					//==============================
					
					// Habilita ou desabilita o botao 'editar' de acordo com o preenchimento dos campos
					Node buttonEdit = dialogo.getDialogPane().lookupButton(buttonEditarUsuario);
					
					//////////////////////////////////////////////////////////////////////////
					
					nome.textProperty().addListener((observable, oldValue, newValue) -> {
						
						// booleano para bloquear o botao 'editar' caso nao haja texto ou ultrapasse 200 caracteres
						this.nomeTxt = nome.getText().length() == 0 || nome.getText().length() > 200; 
						
						try { // Validacao de entrada de texto em TextField
							
							if (!this.nomeTxt) { // Se possuir texto
								
								// Remove qualquer caracter especial permitindo apenas letras maiusculas e minisculas
								// caracteres numericos de 0 a 9 e espacos
								nome.setText(nome.getText().replaceAll("[^a-zA-Z0-9\\u0020]", ""));
								
								// Quando houver espacos repetidos deixar apenas 1 espaco no lugar
								nome.setText(nome.getText().replaceAll("\\u0020{2,}", " "));
								
								nome.setText(nome.getText().toUpperCase()); // Passa os caracteres para maiusculo
							}
							
						} catch (Exception e) { // Tratamento para evitar excecoes
							System.out.println("Caracter inválido neste TextField");
						}
	
						// Desabilita o botao se os campos estiverem vazios ou com entradas invalidas
						buttonEdit.setDisable(this.senhaTxt || this.nomeTxt);
					});

					//////////////////////////////////////////////////////////////////////////

					senha.textProperty().addListener((observable, oldValue, newValue) -> {

						// booleano para saber se a senha possui menos de 4 ou mais de 20 caracteres, bloqueando o botao 'cadastrar'
						this.senhaTxt = senha.getText().length() < 4 || senha.getText().length() > 20; 
						
						// Desabilita o botao se os campos estiverem vazios ou com entradas invalidas
						buttonEdit.setDisable(this.senhaTxt || this.nomeTxt);
					});
	
					//////////////////////////////////////////////////////////////////////////
					
					//Obtendo os dados entrados e fechando a janela de dialogo
					dialogo.getDialogPane().setContent(grid);
	
					Platform.runLater(() -> nome.requestFocus());
	
					dialogo.setResultConverter(dialogButton -> {
						
						// Atribuindo valores entrados ao objeto auxiliar quando o 
						// botao 'editar' for pressionado
					    if (dialogButton == buttonEditarUsuario) { contarConcluido++;
					    	
					    	novoUsuario.setId(usuario.getId());
					    	novoUsuario.setNome(nome.getText().trim());
					    	novoUsuario.setSenha(senha.getText());
					    	novoUsuario.setAdmin(admin.isSelected());
					    	
					        return novoUsuario;
					    }
					    return null;
					});
	
					Optional<Usuario> result = dialogo.showAndWait();
	
					// Manda o requerimento para edicao no BD
					result.ifPresent(usuarioEditado -> {
						
						// Valida se o novo material ja nao existe no BD
						Usuario usuarioModel = new Usuario();
						List<Usuario> usuarioBD = dao.listar((Class<Usuario>) usuarioModel.getClass());
						
						// Variaveis auxiliares
						boolean usuarioValido = true; 
						
						for (Usuario usuarios : usuarioBD) {
							
							// Se ja existir um material com mesma descricao e tipo, nao sera editado
							if (usuarioEditado.getId() != usuarios.getId() &&
								usuarioEditado.getNome().equals(usuarios.getNome())){
								
								Alert alert = new Alert(AlertType.WARNING);
					            alert.setTitle("MENSAGEM");
					            alert.setHeaderText(null);
					            alert.setContentText("O nome de usuário já existe.\n"
					            				   + "A edição não será concluída.");
					            alert.showAndWait();
					            
					            usuarioValido = false;
					            contarConcluido--;
					            break;
							}
						}
						
						if (usuarioValido) dao.update(usuarioEditado);
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
	                				     "Edição concluída: 1 usuário editado." :
	                				     "Edições concluídas: " + contarConcluido + " usuários editados.");
	                alert.showAndWait();
				}
			}
		});
	}			
	
	/////////////////////////////////////////////////
	
	@SuppressWarnings("unchecked")
	public void preencherTabela() {
		
		dao = new DaoUsuario();
		usuarioList = dao.listar((Class<Usuario>) modelUsuario.getClass());
		
		ObservableList<Usuario> observableList = FXCollections.observableArrayList(usuarioList);
		
		if (observableList.size() > 0) {
			textFieldBuscar.setDisable(false);
			buttonBuscar.setDisable(false);
		}
		else {
			textFieldBuscar.setDisable(true);
			buttonBuscar.setDisable(true);
		}
		
		tableViewUsuarios.setItems(observableList);
		tableViewUsuarios.refresh();
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
	
	public void preencherTabela(List<Usuario> list) {
		
		ObservableList<Usuario> observableList = FXCollections.observableArrayList(list);
		
		if (observableList.size() > 0) {
			textFieldBuscar.setDisable(false);
			buttonBuscar.setDisable(false);
		}
		else {
			textFieldBuscar.setDisable(true);
			buttonBuscar.setDisable(true);
		}
		
		tableViewUsuarios.setItems(observableList);
		tableViewUsuarios.refresh();
	}
	
	/////////////////////////////////////////////////
	/*
	desmarcarTudo()
	Retorno: void
	Objetivo: Desmarcar todas as opções de itens selecionados na tabela
	*/
	
	public void desmarcarTudo() {
		// Acao de'desmarcar tudo' 
		tableViewUsuarios.getSelectionModel().clearSelection();
		buttonDesmarcarTudo.setDisable(true);
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
		buttonExcluir.setDisable(false);
		buttonEditar.setDisable(false);
	}
}
