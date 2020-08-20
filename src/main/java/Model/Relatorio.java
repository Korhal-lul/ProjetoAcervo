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
* Model Class Relatorio, utilizada como classe auxiliar, nao sendo uma entidade do Banco de Dados
* 
* ===============================
* Alteração
* 
* Data: 06/08/2020
* Responsável: Gabriel da Costa
*
* Documentação da Classe
* -------------------------------------------------------
*
* ================================
* Declaração de variáveis
* 
*  arquivo : String variavel utilizada para armazenar o nome do relatorio
*  
* ================================
*/

package Model;

public class Relatorio {

	// Variaveis
	private String arquivo;
	
	// Metodos implementados
	public String getArquivo() {
		return arquivo;
	}
	public void setArquivo(String arquivo) {
		this.arquivo = arquivo;
	}
   
}
