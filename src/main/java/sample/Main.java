package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {
	
	// Metodo que inicia o JavaFX assim que o metodo main eh rodado
    @Override
    public void start(Stage primaryStage) throws Exception{
    	
    	// Chamada para renderizacao da janela de login
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/Login.fxml"));
        primaryStage.setTitle("Projeto Acervo");
        primaryStage.setScene(new Scene(root, 400, 500));
        primaryStage.setResizable(false);
        primaryStage.getIcons().add(new Image(this.getClass().getResource("/icons/logo.png").toString()));
        primaryStage.show();
    }

    //-----------------------------------------------
    
    // Metodo principal, no qual a aplicacao sera iniciada
    public static void main(String[] args) { launch(args); }
}
