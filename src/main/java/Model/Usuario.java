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
* Classe de dados do usuário
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
* nome : String Nome do usuário
* senha : String Senha do usuário
* admin : boolean Valor booleano para verificação se o usuário irá ser um admin ou se já é um admin
* ================================
*/

package Model;

// IMPORTAÇÕES DE BIBLIOTECAS

import javax.persistence.*;

/*
 * A Model Class Usuario esta definida com anotacoes
 * do Hibernate framework, tornando esta classe uma
 * entidade do Banco de Dados
 * */

@Entity
@Table(name = "Usuario")
public class Usuario {

	// Definicao de chave primaria da entidade
    @Id
    @SequenceGenerator(name="user", sequenceName="userSeq", allocationSize=1)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="user")
    private int id;
    
    // Caracteristicas da entidade
    @Column(nullable = false) private String nome;
    @Column(nullable = false) private String senha;
    @Column(nullable = false) private boolean admin;

    // Metodos de encapsulamento
    // GETS E SETS
    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getSenha() {
        return senha;
    }

	public boolean isAdmin() {
		return admin;
	}

	public void setAdmin(boolean admin) {
		this.admin = admin;
	}
}
