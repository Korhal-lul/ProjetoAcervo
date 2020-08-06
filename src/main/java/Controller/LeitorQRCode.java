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
* 	bufferedImage : BufferedImage Objeto para armazenar a imagem
*   source : LuminanceSource Objeto utilizado para ser lido pelo bitmap
*   bitmap : BinaryBitmap Objeto que mapeara o QRcode
*   result : Result Objeto que recebe a informacao decodificada
* ================================
*/

package Controller;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class LeitorQRCode {

    public static String decodeQRCode(File file) throws IOException {
    	
        BufferedImage bufferedImage = ImageIO.read(file);
        
        LuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

        try {
            Result result = new MultiFormatReader().decode(bitmap);
            return result.getText();
        } catch (NotFoundException e) {
            return "Não foi encontrado um QR code na imagem";
        }
    }

}