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
* Classe de dados do material
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
* Data: 06/08/2020
* Responsável: Braian Costa Zapelini
*
* ================================
* Declaração de variáveis
* id : int Identificador único do item
* descricao : String Descricao do item
* tipo : String Tipo do item
* quantAtual : int Quantidade atual de itens no estoque
* ================================
*/

package Model;

// IMPORTAÇÕES DE BIBLIOTECAS

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
 */

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

    // Construtor
    public Material() {}

    // Metodos de encapsulamento
    // GETS E SETS
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
