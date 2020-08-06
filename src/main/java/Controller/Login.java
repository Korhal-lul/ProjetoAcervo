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
* Classe de login do usuário no sistema
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
* textFieldUsuario : TextField 
* textFieldSenha : TextField
* buttonEntrar : Button Botão 'entrar' permite ao usuário sua entrada para a página principal do sistema caso não ocorra nenhum erro
* ================================
*/

package Controller;

// IMPORTAÇÕES DE BIBLIOTECAS

import Model.Usuario;
import Model.UsuarioLogado;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import Dao.DaoUsuario;
import Dao.DaoUsuarioLogado;

public class Login implements Initializable {

	// Declaracao dos componente utilizados na janela
    @FXML private TextField textFieldUsuario;
    @FXML private TextField textFieldSenha;
    @FXML private Button    buttonEntrar;

    // Declaracao de classes para CRUD
    DaoUsuario dao;
    DaoUsuarioLogado daoLog;
    
    // Auxiliares
    List<Usuario> usuarios = new ArrayList<Usuario>();
    Usuario modelUsuario = new Usuario();
    
    /////////////////////////////////////////////////
    
    // Metodo nativo do JavaFX que é auto-executado ao renderizar a janela
    @SuppressWarnings("unchecked")
	@Override
    public void initialize(URL location, ResourceBundle resources) {

        // Aqui os dados sao obtidos do BD
    	dao = new DaoUsuario();
    	usuarios = dao.listar((Class<Usuario>) modelUsuario.getClass());
    	
    	/////////////////////////////////////////////////
    	
    	// Caso nao haja usuarios cadastrados, cria o usuario root padrao
    	if (usuarios.size() == 0) {
    		
    		modelUsuario = new Usuario();
    		modelUsuario.setNome("ADMIN");
    		modelUsuario.setSenha("AcervoAdmin2020");
    		modelUsuario.setAdmin(true);
    		dao.insert(modelUsuario);
    		usuarios = dao.listar((Class<Usuario>) modelUsuario.getClass());
    	}
    	
    	/////////////////////////////////////////////////
    	
    	textFieldUsuario.textProperty().addListener((observable, oldValue, newValue) -> {
    		textFieldUsuario.setText(textFieldUsuario.getText().toUpperCase());
    	});
    		
    	/////////////////////////////////////////////////	
    	
    	// O botao 'Entrar' verifica se o usuario e senha informados nos campos
    	// da janela de login estao cadastrados no BD, se estiver, uma sessao eh
    	// iniciada com o usuario logado
    	buttonEntrar.setDefaultButton(true);
        buttonEntrar.setOnAction(new EventHandler<ActionEvent>() {
        	
            public void handle(ActionEvent event) {

            	int lenUsuario = textFieldUsuario.getLength();
            	int lenSenha = textFieldSenha.getLength();
            	
                if ( lenUsuario == 0 || lenSenha == 0) {

                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("ATENÇÃO");
                    alert.setHeaderText("INSIRA SEUS DADOS!");
                    String a = "seu nome de usuário";
                    String b = "sua senha";
                    String c =  a + " e " + b;
                    String s = "Por favor, insira " + (lenUsuario == 0 && lenSenha == 0 ? c : 
                    								  (lenUsuario > 0 && lenSenha == 0 ? b: a)) + ".";
                    alert.setContentText(s);
                    alert.show();
                }
                else {
                	
                	int contadorUsuario = 0, contadorSenha = 0;

                    for (int i = 0; i < usuarios.size(); i++) {

                        if (textFieldUsuario.getText().equals(usuarios.get(i).getNome())) {
                        		
                        	if (textFieldSenha.getText().equals(usuarios.get(i).getSenha())) {
                        		
                        		try { 
                        			 
                        			// Chamada para renderizacao da janela Principal
                        			UsuarioLogado usuarioLogado = new UsuarioLogado();
                        			usuarioLogado.setNome(usuarios.get(i).getNome());
                        			usuarioLogado.setAdmin(usuarios.get(i).isAdmin());
                        			setUsuario(usuarioLogado);
                        			
                        			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Acervo.fxml"), resources);
                        			Parent root = (Parent) loader.load();
                        			Parent node = (Parent) event.getSource();
                        			setEventHandler(node);
                        			
                        			Stage stage = new Stage();
                        			stage.setTitle("Acervo");
                        			stage.setScene(new Scene(root, 700, 500));
                        			stage.setResizable(false);
                        			stage.getIcons().add(new Image(this.getClass().getResource("/icons/logo.png").toString()));
                        			stage.show();

                        			((Node) (event.getSource())).getScene().getWindow().hide();

                        		} catch (IOException e) {
                        			e.printStackTrace();
                        		}
                        	}
                        	else contadorSenha++;
                        }
                        else contadorUsuario++;
                        
                    }
                    
                    // Validacoes
                    if (contadorUsuario == usuarios.size()) {
                    	Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    	alert.setTitle("MENSAGEM");
                    	alert.setHeaderText("USUÁRIO NÃO ENCONTRADO!");
                    	String s ="Verifique o nome de usuário informado e tente novamente.";
                    	alert.setContentText(s);
                    	alert.show();
                    }
                    else if (contadorSenha > 0) {
                    	Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    	alert.setTitle("MENSAGEM");
                    	alert.setHeaderText("SENHA INCORRETA!");
                    	String s ="Reinsira a senha e tente novamente.";
                    	alert.setContentText(s);
                    	alert.show();
                    }
                    
                }
            }
        });
    }

    /////////////////////////////////////////////////
    /*
	setUsuario(UsuarioLogado usuarioLogado)
	retorno: void
	objetivo: Define o usuário logado na sessão
	*/
    
	public void setUsuario(UsuarioLogado usuarioLogado) {
   
    	daoLog = new DaoUsuarioLogado();
    	daoLog.delete();
    	daoLog.insert(usuarioLogado);
    }
    
	/////////////////////////////////////////////////
	
	// Evento para a tecla 'Enter' servir para pressionar o
	// botao 'Entrar'
    private void setEventHandler(Node node) {
    	node.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ENTER) {
               buttonEntrar.fire();
               event.consume(); 
            }
        });
    }
    
}