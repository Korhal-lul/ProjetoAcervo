package Controller;

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

    public static Endereco buscaEnderecoPelo(String cep) throws Exception {
    	
        String urlWeb = webService + cep + "/json";

        try {
            URL url = new URL(urlWeb);
            HttpURLConnection conexao = (HttpURLConnection) url.openConnection();

            if (conexao.getResponseCode() != codigoSucesso)
                throw new RuntimeException("HTTP error code : " + conexao.getResponseCode());

            BufferedReader resposta = new BufferedReader(new InputStreamReader(conexao.getInputStream(), "UTF-8"));
            String jsonEmString = converteJsonEmString(resposta);

            Gson gson = new Gson();
            Endereco endereco = gson.fromJson(jsonEmString, Endereco.class);

            return endereco;
            
        } catch (Exception e) {
            throw new Exception("ERRO: " + e);
        }
    }
    
    public static String converteJsonEmString(BufferedReader buffereReader) throws IOException {
        String resposta, jsonEmString = "";
        while ((resposta = buffereReader.readLine()) != null) {
            jsonEmString += resposta;
        }
        return jsonEmString;
    }
} 