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
*   usuario: UsuarioLogado
* directory: String Armazena o diretório do projeto onde os relatórios são criados
*  fileName: String Armazena o nome do arquivo que será modificado ou acessado
* 
* ================================
*/

package Controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;

import Dao.DaoUsuarioLogado;
import Model.MateriaisRetirada;
import Model.UsuarioLogado;

public class DatabaseExcel {

	static DaoUsuarioLogado daoLog;
	
	private static UsuarioLogado usuario = getUsuario();
	private static String directory = "relatorios\\";
	private static String fileName;

	/////////////////////////////////////////////////
	/*
	generate()
	Parâmetros de entrada:
				List<MateriaisRetirada> listRetirada
	Retorno: void
	Objetivo: Gerar arquivo .xls de relatório
	*/
	
	public void generate(List<MateriaisRetirada> listRetirada) {
		
		fileName = directory + getData() + " [EM ABERTO] ( " + usuario.getNome() + " ).xls";
		
		File diretorio = new File(directory); 
		
		if (!diretorio.exists()) { diretorio.mkdirs(); }
		
		File file = new File(fileName);
		
		if (!file.exists()) {
			
			HSSFWorkbook workbook = new HSSFWorkbook();
			HSSFSheet sheet = workbook.createSheet("Retirada");
			
			// Definindo alguns padroes de layout
			sheet.setDefaultColumnWidth(20);
			sheet.setDefaultRowHeight((short) 400);
			
			// Configurando estilos de células (Cores, alinhamento, formatação, etc..)
			int rownum = 0, cellnum = 0;
			Cell cell;
			Row row;
			
			CellStyle headerStyle = workbook.createCellStyle();
			headerStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
			headerStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
			headerStyle.setAlignment(CellStyle.ALIGN_CENTER);
			headerStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
			Font fonte = workbook.createFont();
	        fonte.setBoldweight(Font.BOLDWEIGHT_BOLD);
	        fonte.setColor(IndexedColors.WHITE.getIndex());
	        headerStyle.setFont(fonte);
			
			// Configurando Header
			row = sheet.createRow(rownum++);
			cell = row.createCell(cellnum++);
			cell.setCellStyle(headerStyle);
			cell.setCellValue("Código");

			cell = row.createCell(cellnum++);
			cell.setCellStyle(headerStyle);
			cell.setCellValue("Descrição");

			cell = row.createCell(cellnum++);
			cell.setCellStyle(headerStyle);
			cell.setCellValue("Tipo");

			cell = row.createCell(cellnum++);
			cell.setCellStyle(headerStyle);
			cell.setCellValue("Quant. Retirada");
			
			try (FileOutputStream out = new FileOutputStream(fileName)) {
				workbook.write(out);
				out.close();
				workbook.close();
				
			} catch (FileNotFoundException e) {
				System.out.println(e);
				System.out.println("Arquivo Excel não criado!");
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		} 
		
		List<MateriaisRetirada> listBD = new ArrayList<>();
		
		try {

			FileInputStream arquivo = new FileInputStream(new File(DatabaseExcel.fileName));
			@SuppressWarnings("resource")
			HSSFWorkbook workbook = new HSSFWorkbook(arquivo);
			HSSFSheet sheet = workbook.getSheetAt(0);

			Iterator<Row> rowIterator = sheet.iterator();

			// Enquanto houver linhas
			while (rowIterator.hasNext()) {

				Row row = rowIterator.next(); // Obtendo a linha
				@SuppressWarnings("unused")
				Iterator<Cell> cellIterator = row.cellIterator(); // Obtendo o iterador da linha

				MateriaisRetirada produto = new MateriaisRetirada();
				listBD.add(produto);

			}

			if (listBD.size() == 0) {
				System.out.println("Nenhum material encontrado!");
			} else {

				int rownum = listBD.size();
				int cellnum = 0;
				Cell cell;
				Row row;

				// Configurando estilos de células (Cores, alinhamento, formatação, etc..)
				CellStyle textStyle = workbook.createCellStyle();
				textStyle.setAlignment(CellStyle.ALIGN_CENTER);
				textStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);

				// Adicionando os dados dos produtos na planilha
				for (MateriaisRetirada materiais : listRetirada) {
					row = sheet.createRow(rownum++);
					cellnum = 0;

					cell = row.createCell(cellnum++);
					cell.setCellStyle(textStyle);
					cell.setCellValue(materiais.getId());

					cell = row.createCell(cellnum++);
					cell.setCellStyle(textStyle);
					cell.setCellValue(materiais.getDescricao());

					cell = row.createCell(cellnum++);
					cell.setCellStyle(textStyle);
					cell.setCellValue(materiais.getTipo());

					cell = row.createCell(cellnum++);
					cell.setCellStyle(textStyle);
					cell.setCellValue(materiais.getQuantDesejada());
				}

				// Escrevendo o arquivo em disco
				FileOutputStream out = new FileOutputStream(new File(DatabaseExcel.fileName));
				workbook.write(out);
				out.close();
				
			}
			
			arquivo.close();

		} catch (FileNotFoundException e) {
			System.out.println("Arquivo Excel não encontrado!");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	/////////////////////////////////////////////////	
	/*
	getData()
	Retorno: String
	Objetivo: Obter a data, hora, minutos e segundos da máquina local
	*/
	private static String getData() {
		
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH'h 'mm'min 'ss' seg'");
	    Date date = new Date();
	    return dateFormat.format(date);
	}
	
	/////////////////////////////////////////////////
	/*
	getUsuario()
	Retorno: UsuarioLogado
	Objetivo: Obter informação do usuário logado na sessão
	*/
	@SuppressWarnings("unchecked")
	public static UsuarioLogado getUsuario() {
	    
		daoLog = new DaoUsuarioLogado();
		
	 	UsuarioLogado usuario = new UsuarioLogado();
	 	usuario = daoLog.select((Class<UsuarioLogado>) usuario.getClass());
	 	
	 	return usuario;
	}

}