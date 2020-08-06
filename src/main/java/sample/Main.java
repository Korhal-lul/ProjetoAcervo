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
 * =============================================================
 * Alteração
 *
 * Data: 06/08/2020
 * Responsável: Leonardo Cech
 *===============================================================
 * Documentação da Classe
 *
 *  Data: 06/08/2020
 *  Responsável: Daniel Schinaider de Oliveira
 *
 * ================================================================
 * Declaração de variáveis
 * ================================================================
 */

package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

    /*
     * Nome da Função: start
     * Retorno: void
     * Objetivo: inicia o JavaFX assim que o metodo main eh rodado
     */

    @Override
    public void start(Stage primaryStage) throws Exception {

        // Chamada para renderizacao da janela de login
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/Login.fxml"));
        primaryStage.setTitle("Projeto Acervo");
        primaryStage.setScene(new Scene(root, 400, 500));
        primaryStage.setResizable(false);
        primaryStage.getIcons().add(new Image(this.getClass().getResource("/icons/logo.png").toString()));
        primaryStage.show();
    }

    // -----------------------------------------------

    /*
     * Nome da Função: main
     * Retorno: void
     * Objetivo: Inicia a aplicacao
     */

    // Metodo
    public static void main(String[] args) {
        launch(args);
    }
}