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
* Data: 20/08/2020
* Responsável: Braian Costa Zapelini
*
* ================================
* Declaração de variáveis
* 
* 	webService    : Tipo String Armazena a url para consulta de CEP
* 	codigoSucesso : Tipo int Variável para validação da conexão 
* 	 
* ================================
*/

package Controller;

//IMPORTAÇÕES DE BIBLIOTECAS

import com.google.gson.Gson;

import Model.Endereco;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ValidaCEP {

	static String webService = "http://viacep.com.br/ws/";
	static int codigoSucesso = 200;

		/////////////////////////////////////////////////
		/*
		* buscaEnderecoPelo(String cep)
		* Retorno: Object
		* Objetivo: buscar endereco atraves de cep informado
		* Parâmetro de entrada:
		* 			cep: tipo String (representa o CEP inserido pelo usuário)
		* Parâmetro de saida:
		* 			endereco : tipo Object (representa o retorno de dados apresentado pelo cep)
		*/
	public static Endereco buscaEnderecoPelo(String cep) throws Exception {
		// Variavel para utilizar url retornando cep
		String urlWeb = webService + cep + "/json";

		try {

			URL url = new URL(urlWeb);
			// Manda o link do webservice
			HttpURLConnection conexao = (HttpURLConnection) url.openConnection();

			// Condicao para verificar conexao
			if (conexao.getResponseCode() != codigoSucesso)
				throw new RuntimeException("HTTP error code : " + conexao.getResponseCode());
			// Resposta armazena os dados json temporariamente
			BufferedReader resposta = new BufferedReader(new InputStreamReader(conexao.getInputStream(), "UTF-8"));
			// Variavel para converter json em String
			String jsonEmString = converteJsonEmString(resposta);

			Gson gson = new Gson();
			// Endereco recebe os dados da classe endereco e da string json
			Endereco endereco = gson.fromJson(jsonEmString, Endereco.class);

			return endereco;

		} catch (Exception e) {
			throw new Exception("ERRO: " + e);
		}
	}
		/////////////////////////////////////////////////
		/*
		* converteJsonEmString(String cep)
		* Retorno: String
		* Objetivo: converter Json para uma String
		* Parâmetro de entrada:
		* 			resposta : tipo String (responsável por armazenar os dados lidos)
		* 			cep: tipo String (representa o CEP inserido pelo usuário)
		*/

	// Metodo para converter Json em String
	public static String converteJsonEmString(BufferedReader buffereReader) throws IOException {
		String resposta, jsonEmString = "";
		// Loop para ler as linhas do json e armazenalar em uma String enquando nao
		// estiver nulo
		while ((resposta = buffereReader.readLine()) != null) {
			jsonEmString += resposta;
		}
		return jsonEmString;
	}
}