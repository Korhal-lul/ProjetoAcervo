/*
 * SENAI / CENTROWEG
 * AIPSIN 2019/1
 * MI-66
 * Autor(es): Daniel Schinaider de Oliveira,
 * 	         Victor Hugo Moresco,
 * 		   	 Braian Costa Zapelini,
 *           Leonardo Cech,
 * 	         Gabriel da Costa
 *
 * Data: 06/08/2020
 *
 * Model Classe Material esta definida com anotacoes
 * do Hibernate framework, tornando esta classe uma
 * entidade do Banco de Dados
 *
 * ===============================
 * Alteração
 *
 * Data: 06/08/2020
 * Responsável: Victor Hugo Moresco
 *
 * Documentação da Classe
 * -------------------------------------------------------
 *
 * ================================
 * Declaração de variáveis
 *  id : int Inteiro para identificador unico
 *  nome : String Nome do usuario
 *  admin : boolean Boolean pra definir as propriedades
 * ================================
 */

package Model;

import javax.persistence.*;

@Entity
@Table(name = "UsuarioLogado")
public class UsuarioLogado {

	// Definicao de chave primaria da entidade
    @Id
    @SequenceGenerator(name="userLog", sequenceName="userLogSeq", allocationSize=1)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="userLog")
    
    // Caracteristicas da entidade
    private int id;
    private String nome;
    private boolean admin;

    // Metodos de encapsulamento
    public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

	public boolean isAdmin() {
		return admin;
	}

	public void setAdmin(boolean admin) {
		this.admin = admin;
	}
}
