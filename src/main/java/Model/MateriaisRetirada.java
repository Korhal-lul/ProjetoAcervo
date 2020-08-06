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
* Model Class MateriaisRetirada, utilizada como classe auxiliar, nao sendo uma entidade do Banco de Dados
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
* id 			: int		Codigo identificador do item
* numero 		: int		Quantidade do item
* descricao 	: String	Descricao do item
* tipo 			: String	Tipo do item
* quantAtual	: int		Quantidade atual de itens em estoque
* quantDesejada	: int		Quantidade que sera retirada
*  
* ================================
*/

package Model;

public class MateriaisRetirada {

	// Variaveis
    private int    id;
    private int    numero;
    private String descricao;
    private String tipo;
    private int    quantAtual;
    private int    quantDesejada;

    // Construtores, com e sem parametros
    public MateriaisRetirada() {}

    public MateriaisRetirada(int numero, String descricao, String tipo, int quantAtual, int quantDesejada) {
        this.setNumero(numero);
        this.setDescricao(descricao);
        this.setTipo(tipo);
        this.setQuantAtual(quantAtual);
        this.setQuantDesejada(quantDesejada);
    }

    // Metodos implementados
    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setNumero(int numero) { this.numero = numero; }

    public int getNumero() { return numero; }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setQuantAtual(int quantAtual) {
        this.quantAtual = quantAtual;
    }

    public int getQuantAtual() {
        return quantAtual;
    }

    public void setQuantDesejada(int quantDesejada) { this.quantDesejada = quantDesejada; }
    public int getQuantDesejada() { return quantDesejada; }
}
