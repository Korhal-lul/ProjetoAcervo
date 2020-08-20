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
* Data: 20/08/2020
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
* 
* 	 buttonFechar : Button Botão 'fechar' fechará a tela de validação do CEP inserido
* 	 cepCriarTF : TextField
* 	 logradouroCriarTF : TextField
* 	 complementoCriarTF : TextField
* 	 bairroCriarTF : TextField
* 	 localidadeCriarTF : TextField
* 	 ufCriarTF : TextField
* 	 unidadeCriarTF : TextField
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

	/////////////////////////////////////////////////
	/*
	* validar(String cep)
	* Retorno: void
	* Objetivo: Valida a busca pelo CEP inserido no JFrame
	* Parâmetro de entrada:
	* 			cep: tipo String (representa o CEP inserido pelo usuário)
	* Parâmetro de saida:
	* 			setEnderecoCriarUsuario(endereco) : metodo (envia endereco buscado atraves do cep)
	*/
	public void validar(String cep) throws Exception {
		
		Endereco endereco = ValidaCEP.buscaEnderecoPelo(cep);
		setEnderecoCriarUsuario(endereco);
	}
	
	/////////////////////////////////////////////////
	/*
	* setEnderecoCriarUsuario(Endereco endereco)
	* Retorno: void
	* Objetivo: Ao buscar o CEP no método 'validar()', preenche os campos com as informações pesquisadas
	* Parâmetro de entrada:
	* 			endereco: tipo Endereco (representa o objeto da classe 'Endereco')		
	*/
	public void setEnderecoCriarUsuario(Endereco endereco) {
		
 	        cepCriarTF.setText(endereco.getCep());
	 logradouroCriarTF.setText(endereco.getLogradouro());
	     bairroCriarTF.setText(endereco.getBairro());
	 localidadeCriarTF.setText(endereco.getLocalidade());
	         ufCriarTF.setText(endereco.getUf());
	         
	}
}
