package Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

/*
 * A Model Class Material esta definida com anotacoes
 * do Hibernate framework, tornando esta classe uma
 * entidade do Banco de Dados
 * */

@Entity
public class Material {

	// Definicao de chave primaria da entidade
    @Id
    @SequenceGenerator(name="mat", sequenceName="matSeq", allocationSize=1)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="mat")
    @Column(nullable = false) private int    id;
    
    // Caracteristicas da entidade
    @Column(nullable = false) private String descricao;
    @Column(nullable = false) private String tipo;
    @Column(nullable = false) private int    quantAtual;

    // Contrutor
    public Material() {}

    // Metodos de encapsulamento
    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

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
}
