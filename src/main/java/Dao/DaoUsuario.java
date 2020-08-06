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
* Classe de processamento e renderização da janela principal
* 
* =============================================================
* Alteração
* 
* Data: 06/08/2020
* Responsável: Leonardo Cech
*===============================================================
* Documentação da Classe
*  
*  Data: 06/08/2020
*  Responsável: Daniel Schinaider de Oliveira
*  
* ================================================================
* Declaração de variáveis
*  dao : DaoGeneric<Usuario> Instancia para Dao Generic
*  
* 
* ================================================================
*/

package Dao;

import Model.Usuario;

import java.util.List;

/*
 * A Dao Class DaoUsuario possui alguns metodos CRUD
 * utilizados com base nos metodos genericos da
 * DaoGeneric, na qual possui os comandos do 
 * Hibernate Framework para transmitir e/ou 
 * receber dados do MySQL
 * */

public class DaoUsuario {

	// Cria o Objeto dao para realizar chamadas de metodos genericos
	public DaoGeneric<Usuario> dao = new DaoGeneric<>();

	/*
	 * Nome da Função: select Retorno: Usuario Objetivo: Listagem do banco de dados
	 */

	public Usuario select(int id, Class<Usuario> entity) {
		return dao.select(id, entity);
	}

	/*
	 * Nome da Função: insert 
	 * Retorno: void 
	 * Objetivo: inserir no banco de dados
	 */

	// Metodo INSERT do MySQL
	public void insert(Usuario objeto) {
		dao.insert(objeto);
	}

	/*
	 * Nome da Função: insert 
	 * Retorno: void 
	 * Objetivo: Receber Objeto com as informacoes a serem editadas obtem do BD os dados atuais
	 * respectivos ao requerido ID e manda o objeto com as novas informacoes ao BD
	 */

	public void update(Usuario usuarioOld) {

		@SuppressWarnings("unchecked")
		Usuario usuarioNew = dao.select(usuarioOld.getId(), (Class<Usuario>) usuarioOld.getClass());

		usuarioNew.setNome(usuarioOld.getNome());
		usuarioNew.setSenha(usuarioOld.getSenha());
		usuarioNew.setAdmin(usuarioOld.isAdmin());

		dao.update(usuarioNew);
	}

	/*
	 * Nome da Função: delete 
	 * Retorno: void 
	 * Objetivo: deleta dado do banco de dados
	 */

	@SuppressWarnings("unchecked")
	public void delete(Usuario usuarios, int id) {

		usuarios = dao.select(id, (Class<Usuario>) usuarios.getClass());
		dao.delete(usuarios);
	}

	
	/*
	 * Nome da Função: listar 
	 * Retorno: List<Usuario> 
	 * Objetivo: listar dados que sera chamado na TableView do JavaFX
	 */
	
	public List<Usuario> listar(Class<Usuario> usuarios) {
		return dao.listar(usuarios);
	}
	/*
	 * Nome da Função: buscar 
	 * Retorno: List<Usuario> 
	 * Objetivo: Metodo de busca que retorna uma lista com os resultados requeridos
	 */
	
	public List<Usuario> buscar(Class<Usuario> usuarios, String buscado) {
		return dao.buscar(usuarios, "nome", buscado);
	}
}
