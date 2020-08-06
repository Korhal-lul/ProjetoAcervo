package Model;

/*
 * A Model Class MateriaisRetirada eh
 * utilizada como classe auxiliar, nao
 * sendo uma entidade do Banco de Dados
 * */

public class MateriaisRetirada {

    private int    id;
    private int    numero;
    private String descricao;
    private String tipo;
    private int    quantAtual;
    private int    quantDesejada;

    public MateriaisRetirada() {}

    public MateriaisRetirada(int numero, String descricao, String tipo, int quantAtual, int quantDesejada) {
        this.setNumero(numero);
        this.setDescricao(descricao);
        this.setTipo(tipo);
        this.setQuantAtual(quantAtual);
        this.setQuantDesejada(quantDesejada);
    }

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
