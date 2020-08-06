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
* 
* ================================
*/

package Controller;

import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import Dao.DaoMaterial;
import Dao.DaoUsuarioLogado;
import Model.Material;
import Model.Relatorio;
import Model.UsuarioLogado;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Relatorios implements Initializable {

	// Instancia dos componentes do ambiente grafico
	@FXML private Button buttonSair;
	@FXML private Button buttonAbrirDiretorio;
	@FXML private Button buttonDesmarcar;
	@FXML private Button buttonExcluir;
	@FXML private Button buttonAbrir;
	@FXML private Button buttonBuscar;
	@FXML private Button buttonDevolver;
	@FXML private TextField textFieldBuscar;

	@FXML private TableView<Relatorio> tableViewRelatorios;
	@FXML private TableColumn<Relatorio, String> tableColumnRelatorios;

	// Declaracao de classes para CRUD
	DaoMaterial dao;
	DaoUsuarioLogado daoLog;
	
	// Auxiliares
	List<Relatorio> relatoriosList = new ArrayList<>();
	List<Relatorio> buscaList = new ArrayList<>();
	Relatorio modelRelatorio = new Relatorio();
	Relatorio arquivoSelecionado;
	private String diretorio;
	private UsuarioLogado usuario;
	
	/////////////////////////////////////////////////
	
	// Metodo nativo do JavaFX que é auto-executado ao renderizar a janela
	@Override
	public void initialize(URL location, final ResourceBundle resources) {
		
		usuario = getUsuario();
		
		relatoriosList = getCaminhos(relatoriosList);
	
		if (usuario.isAdmin()) {
			buttonExcluir.setVisible(true);
			buttonAbrirDiretorio.setVisible(true);
		}
		else {
			buttonExcluir.setVisible(false);
			buttonAbrirDiretorio.setVisible(false);
		}

		/////////////////////////////////////////////////
		
		// Evento do botao 'sair' que fecha a janela atual e abre a janela 'acervo'
		buttonSair.setTooltip(new Tooltip("Voltar à janela Acervo"));
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
		
		// Evento do botao 'Abrir diretorio'
		buttonAbrirDiretorio.setTooltip(new Tooltip("Abrir pasta de relatórios"));
		buttonAbrirDiretorio.setFont(new Font("Calibri", 16));
		buttonAbrirDiretorio.setOnAction(new EventHandler<ActionEvent>() {

			public void handle(ActionEvent event) {

				// Abrir diretorio no explorador de arquivos do sistema
				try {
					Desktop.getDesktop().open(new File(diretorio));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		
		/////////////////////////////////////////////////
		
		// Evento do botao 'buscar' que busca e apresenta na tabela os resultados de acordo
		// com a informação informada
		buttonBuscar.setTooltip(new Tooltip("Buscar por relatórios"));
		buttonBuscar.setFont(new Font("Calibri", 16));
		buttonBuscar.setDisable(true);
		buttonBuscar.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
					
				desmarcarTudo();
				
				// buscar no List
				buscaList = new ArrayList<>();
				relatoriosList = new ArrayList<>();
				relatoriosList = getCaminhos(relatoriosList);
				
				int resultados = 0;
				for (int i = 0; i < relatoriosList.size(); i++) {
					
					if (relatoriosList.get(i).getArquivo().contains(textFieldBuscar.getText())) {
						resultados++;	
						buscaList.add(relatoriosList.get(i));
					}
				}
				
				if (resultados > 0) preencherTabela(buscaList);
				else preencherTabela(relatoriosList);
			}
		});
		
		/////////////////////////////////////////////////
		
		textFieldBuscar.setDisable(true);
		textFieldBuscar.setTooltip(new Tooltip("*Clique no botão de busca com este campo vazio para listar todos os relatórios"));
		
		/////////////////////////////////////////////////
		
		// DEFINICOES DA TABELA
		tableColumnRelatorios.setCellValueFactory(new PropertyValueFactory<>("arquivo"));
		preencherTabela(relatoriosList);
		
		// Evento da tabela para quando uma linha é selecionada
		tableViewRelatorios.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
			if (newSelection != null) {
				
				buttonDevolver.setDisable(true);
				
				String arquivo = tableViewRelatorios.getSelectionModel().getSelectedItems().get(0).getArquivo();
				
				arquivoSelecionado = new Relatorio();
				arquivoSelecionado.setArquivo(arquivo);
				
				habilitarbotoes();
			}
		});

		/////////////////////////////////////////////////
		
		// Evento do botao 'Desmarcar' que remove a linha que possa estar selecionada na tabela
		buttonDesmarcar.setTooltip(new Tooltip("Remover marcação da linha selecionada na tabela"));
		buttonDesmarcar.setFont(new Font("Calibri", 12));
		buttonDesmarcar.setOnAction(event -> { desmarcarTudo(); });
		
		/////////////////////////////////////////////////
		
		// Evento do botao 'Excluir relatório' que remove do BD o relatório selecioado
		buttonExcluir.setTooltip(new Tooltip("Excluir relatório selecionado"));
		buttonExcluir.setFont(new Font("Calibri", 16));
		buttonExcluir.setOnAction(event -> {
			
			try {
				// Excluir arquivo do diretorio
				for (int i = 0; i < relatoriosList.size(); i++) {
					
					if (relatoriosList.get(i).getArquivo().equals(arquivoSelecionado.getArquivo())) {
						
						relatoriosList.remove(i);
						
						File arquivo = new File(diretorio + "\\" + arquivoSelecionado.getArquivo());
						arquivo.delete();
						
						// Apresenta ao usuario uma mensagem de conclusao
						Alert alert = new Alert(AlertType.INFORMATION);
			            alert.setTitle("MENSAGEM");
			            alert.setHeaderText(null);
			            alert.setContentText("Relatório removido com sucesso: \n" +  diretorio + "/" + arquivoSelecionado.getArquivo());
			            alert.showAndWait();
			            
			            break;
					}
				}
			} catch (Exception e) {
				// Apresenta ao usuario uma mensagem de erro
				Alert alert = new Alert(AlertType.ERROR);
	            alert.setTitle("MENSAGEM");
	            alert.setHeaderText(null);
	            alert.setContentText("O relatório não pode ser excluído pois esta aberto em outro programa.");
	            alert.showAndWait();
			}
            
			preencherTabela(relatoriosList);
            desmarcarTudo();
            
            
		});
		
		/////////////////////////////////////////////////
		
		// Evento do botao 'Abrir relatório' que abre no Excel o relatorio selecionado
		buttonAbrir.setTooltip(new Tooltip("Abrir relatório selecionado"));
		buttonAbrir.setFont(new Font("Calibri", 16));
		buttonAbrir.setOnAction(new EventHandler<ActionEvent>() {

			public void handle(ActionEvent event) {
				
				// Abrir arquivo pelo caminho
				File file = new File(diretorio + "\\" + arquivoSelecionado.getArquivo()); 
				
				try { Desktop.getDesktop().open(file); } 
				catch (IOException e) { e.printStackTrace(); }
			}
		});
		
		/////////////////////////////////////////////////
		
		// Evento do botao 'Marcar como devolvido' que atualiza o BD com os materiais devolv
		buttonDevolver.setTooltip(new Tooltip("Marcar relatório selecionado como devolvido"));
		buttonDevolver.setFont(new Font("Calibri", 16));
		buttonDevolver.setOnAction(new EventHandler<ActionEvent>() {

			public void handle(ActionEvent event) {
				
				for (int i = 0; i < relatoriosList.size(); i++) {
					
					if (relatoriosList.get(i).getArquivo().equals(arquivoSelecionado.getArquivo())) {
						
						String novoNome = relatoriosList.get(i).getArquivo().replace("EM ABERTO", "DEVOLVIDO");
						relatoriosList.get(i).setArquivo(novoNome);
						
						i = relatoriosList.size();// parar o loop quando encontrar o arquivo
						
						File arquivo = new File(diretorio + "\\" + arquivoSelecionado.getArquivo());
						
						File novoArquivo = new File(diretorio + "\\" + novoNome); 
						
						arquivo.renameTo(novoArquivo);
						arquivoSelecionado.setArquivo(novoArquivo.toString());
					}
				}
				preencherTabela(relatoriosList);
				desmarcarTudo();
				
				// Ler o arquivo .xls e devolver os itens ao BD
				try { lerArquivo(diretorio + "\\" + arquivoSelecionado.getArquivo()); }
				catch (IOException e) { e.printStackTrace(); }
			}
		});
	}
	
	/////////////////////////////////////////////////
	
	public List<Relatorio> getCaminhos(List<Relatorio> list) {
		
		// Mostra caminhos dos arquivos do diretorio
		File directory = new File("relatorios/");
		File[] contents = directory.listFiles();
		
		for ( File f : contents) {
			
			String diretorio = f.getAbsolutePath();
			
			String[] caminho = diretorio.split("relatorios");
			
			this.diretorio = caminho[0].concat("relatorios");
			
			modelRelatorio = new Relatorio();
			modelRelatorio.setArquivo(caminho[1].replace("\\", ""));
			
			if (usuario.isAdmin()) list.add(modelRelatorio);
			else if (modelRelatorio.getArquivo().contains(usuario.getNome())) list.add(modelRelatorio);
		}
		
		return list;
	}
	
	/////////////////////////////////////////////////
	
	public void preencherTabela(List<Relatorio> list) {
			
		ObservableList<Relatorio> observableList = FXCollections.observableArrayList(list);
		
		if (observableList.size() > 0) {
			textFieldBuscar.setDisable(false);
			buttonBuscar.setDisable(false);
		}
		else {
			textFieldBuscar.setDisable(true);
			buttonBuscar.setDisable(true);
		}
		
		tableViewRelatorios.setItems(observableList);
		tableViewRelatorios.refresh();
	}
	
	/////////////////////////////////////////////////
	
	@SuppressWarnings("unchecked")
	public UsuarioLogado getUsuario() {
	    
		daoLog = new DaoUsuarioLogado();
		
	 	UsuarioLogado usuario = new UsuarioLogado();
	 	usuario = daoLog.select((Class<UsuarioLogado>) usuario.getClass());
	 	
	 	return usuario;
	}
	
	/////////////////////////////////////////////////
	
	public void desmarcarTudo() {
		// Acao de'desmarcar tudo' 
		tableViewRelatorios.getSelectionModel().clearSelection();
		buttonDesmarcar.setDisable(true);
		buttonExcluir.setDisable(true);
		buttonAbrir.setDisable(true);
		buttonDevolver.setDisable(true);
	}
	
	/////////////////////////////////////////////////
	
	public void habilitarbotoes() {
		buttonDesmarcar.setDisable(false);
		buttonExcluir.setDisable(false);
		buttonAbrir.setDisable(false);
		// O botao devolver so eh habilitado se o relatorio esta em aberto
		if (arquivoSelecionado.getArquivo().contains("EM ABERTO")) buttonDevolver.setDisable(false);
	}
	
	/////////////////////////////////////////////////
	
	@SuppressWarnings("unchecked")
	public void lerArquivo(String arquivo) throws IOException{
		
		try{
			// Abrindo o arquivo e recuperando a planilha
			FileInputStream file = new FileInputStream(new File(arquivo));
			HSSFWorkbook workbook = new HSSFWorkbook(file);
			HSSFSheet sheet = workbook.getSheetAt(0);
	
			List<Material> materialList = new ArrayList<>();
	
			Iterator<Row> rowIterator = sheet.rowIterator();
			
			while (rowIterator.hasNext()) {
				
				Row row = rowIterator.next();
		
				// Descantando a primeira linha com o header
				if(row.getRowNum() == 0) continue; 
		
				Iterator<Cell> cellIterator = row.cellIterator();
				Material modelMaterial = new Material();
				materialList.add(modelMaterial);
				
				while (cellIterator.hasNext()) {
					
					Cell cell = cellIterator.next();
					
					switch (cell.getColumnIndex()) {
						case 0:
							modelMaterial.setId((int) cell.getNumericCellValue());
							break;
						case 3:
							modelMaterial.setQuantAtual((int) cell.getNumericCellValue());
							break;
					}
				}
			}
	
			for (Material modelMaterial : materialList) {
				
				dao = new DaoMaterial();
				
				Material materialAtual = dao.buscarPorId((Class<Material>) modelMaterial.getClass(), modelMaterial.getId());
				
				materialAtual.setQuantAtual(materialAtual.getQuantAtual() + modelMaterial.getQuantAtual());
				
				dao.update(materialAtual);
			}
			
			file.close();
			workbook.close();
				
		} catch (FileNotFoundException e) { e.printStackTrace(); }
    }
}
