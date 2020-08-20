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
import java.util.ResourceBundle;

import Model.Endereco;
import Model.Usuario;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.stage.Stage;

@SuppressWarnings("restriction")
public class DetalhesUsuarios implements Initializable {

	// Instancia dos componentes do ambiente grafico
	@FXML private Button buttonSair;
	
	@FXML private TextField usuarioTF;
	@FXML private TextField senhaTF;
	@FXML private TextField cepTF;
	@FXML private TextField logradouroTF;
	@FXML private TextField complementoTF;
	@FXML private TextField bairroTF;
	@FXML private TextField localidadeTF;
	@FXML private TextField ufTF;
	@FXML private TextField unidadeTF;
	
	// Declaracao de classes para CRUD

	// Auxiliares

	/////////////////////////////////////////////////
	
	// Metodo nativo do JavaFX que é auto-executado ao renderizar a janela
	@Override
	public void initialize(URL location, final ResourceBundle resources) {

		// Evento do botao 'sair' que fecha a janela atual e abre a janela 'acervo'
		buttonSair.setTooltip(new Tooltip("Voltar para a tela Gerenciar Usuarios"));
		buttonSair.setFont(new Font("Calibri", 16));
		buttonSair.setOnAction(new EventHandler<ActionEvent>() {
			
			public void handle(ActionEvent event) {

				Parent root;
				try {
					root = FXMLLoader.load(getClass().getResource("/fxml/usuarios.fxml"), resources);
					Stage stage = new Stage();
					stage.setTitle("Acervo");
					stage.setScene(new Scene(root, 400, 500));
					stage.getIcons().add(new Image(this.getClass().getResource("/icons/logo.png").toString()));
					stage.setResizable(false);
					stage.show();
					((Node) (event.getSource())).getScene().getWindow().hide();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
	}

	public void setDetalhes(Usuario usuario, Endereco endereco) {
	
		    usuarioTF.setText(usuario.getNome());
		      senhaTF.setText(usuario.getSenha());
	   	        cepTF.setText(endereco.getCep());
		 logradouroTF.setText(endereco.getLogradouro());
		complementoTF.setText(endereco.getComplemento());
		     bairroTF.setText(endereco.getBairro());
		 localidadeTF.setText(endereco.getLocalidade());
		         ufTF.setText(endereco.getUf());
		    unidadeTF.setText(endereco.getUnidade());
	}
}
