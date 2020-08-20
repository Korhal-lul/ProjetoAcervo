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

import java.net.URL;
import java.util.ResourceBundle;

import Model.Endereco;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

@SuppressWarnings("restriction")
public class DetalhesCriarUsuarios implements Initializable {

	// Instancia dos componentes do ambiente grafico
	@FXML private Button buttonFechar;
	
	@FXML private TextField cepCriarTF;
	@FXML private TextField logradouroCriarTF;
	@FXML private TextField complementoCriarTF;
	@FXML private TextField bairroCriarTF;
	@FXML private TextField localidadeCriarTF;
	@FXML private TextField ufCriarTF;
	@FXML private TextField unidadeCriarTF;

	/////////////////////////////////////////////////
	
	// Metodo nativo do JavaFX que é auto-executado ao renderizar a janela
	@Override
	public void initialize(URL location, final ResourceBundle resources) {

	}
	
	@FXML
	public void handleCloseButtonAction(ActionEvent event) {
	    ((Stage)(((Button)event.getSource()).getScene().getWindow())).close();
	}

	public void validar(String cep) throws Exception {
		
		Endereco endereco = ValidaCEP.buscaEnderecoPelo(cep);
		setEnderecoCriarUsuario(endereco);
	}
	
	public void setEnderecoCriarUsuario(Endereco endereco) {
		
 	        cepCriarTF.setText(endereco.getCep());
	 logradouroCriarTF.setText(endereco.getLogradouro());
	     bairroCriarTF.setText(endereco.getBairro());
	 localidadeCriarTF.setText(endereco.getLocalidade());
	         ufCriarTF.setText(endereco.getUf());
	         
	}
}
