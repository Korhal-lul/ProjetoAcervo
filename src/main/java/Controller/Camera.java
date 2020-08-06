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
* 	 executor: Executor       Instância do Objeto da Classe Executor
* 	      tbl: TableView      Instância do Objeto da Classe TableView
*      webcam: WebCam         Instância do Objeto da Classe WebCam
*       panel: WebcamPanel    Instância do Objeto da Classe WebcamPanel
*   materiais: List<Material> Instância List da Classe Material;
* ================================
*/
package Controller;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import javax.swing.JFrame;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.WebcamResolution;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;

import Model.Material;
import javafx.scene.control.TableView;

@SuppressWarnings("serial")
public class Camera extends JFrame implements Runnable, ThreadFactory {

	private Executor executor = Executors.newSingleThreadExecutor(this);
	
	@SuppressWarnings("rawtypes")
	private TableView tbl = null;
	private List<Material> materiais = null;

	private Webcam webcam = null;
	private WebcamPanel panel = null;

	/////////////////////////////////////////////////
	/*
	Camera(TableView table, List<Material> materialList)
	retorno: void
	Parâmetro de entrada: 
				TableView table, List<Material> materialList
	objetivo: Acionar a câmera principal conectada à máquina
	*/
	
	public Camera(@SuppressWarnings("rawtypes") TableView table, List<Material> materialList) {
		
		tbl = table;
		materiais = materialList;

		setLayout(new FlowLayout());
		setTitle("QRcode Scanner");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		Dimension size = WebcamResolution.QVGA.getSize();

		WindowListener exitListener = new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				webcam.close();
			}
		};
		addWindowListener(exitListener);
		
		webcam = Webcam.getWebcams().get(0);
		webcam.setViewSize(size);

		panel = new WebcamPanel(webcam);
		panel.setPreferredSize(size);
		panel.setFPSDisplayed(true);

		add(panel);

		pack();
		setVisible(true);

		executor.execute(this);
	}

	/////////////////////////////////////////////////
	/*
	run()
	retorno: void
	objetivo: Exêcutar a câmera
	*/
	@Override
	public void run() {

		do {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			Result result = null;
			BufferedImage image = null;

			if (webcam.isOpen()) {

				if ((image = webcam.getImage()) == null) {
					continue;
				}

				LuminanceSource source = new BufferedImageLuminanceSource(image);
				BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

				try {
					result = new MultiFormatReader().decode(bitmap);
				} catch (NotFoundException e) {
					// fall thru, it means there is no QR code in image
				}
			}
			
			if (result != null) {
				
				int id = Integer.parseInt(result.getText());
				
				for(int i = 0; i < tbl.getItems().size(); i++) {
					
					if(materiais.get(i).getId() == id) {
						tbl.getSelectionModel().select(i);
					}
				}
			}
			
			

		} while (true);
	}
	
	/////////////////////////////////////////////////
	/*
	newThread(Runnable r)
	Parâmetro de entrada:
					Runnable r
	retorno: Thread t
	objetivo: Captura de frames da câmera
	*/
	@Override
	public Thread newThread(Runnable r) {
		Thread t = new Thread(r, "example-runner");
		t.setDaemon(true);
		return t;
	}

}
