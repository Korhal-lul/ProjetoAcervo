package Model;

import javax.persistence.*;

/*
 * A Model Class Material esta definida com anotacoes
 * do Hibernate framework, tornando esta classe uma
 * entidade do Banco de Dados
 * */

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
