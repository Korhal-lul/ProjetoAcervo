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
* ===============================
* Alteração
* 
* Data: 06/08/2020
* Responsável: Leonardo Cech
*
* Documentação da Classe
* -------------------------------------------------------
*
* ================================
* Declaração de variáveis
* dao : DaoGeneric<UsuarioLogado> Objeto dao para realizar chamadas de metodos genericos
* ================================
*/

package Dao;

// IMPORTAÇÕES DE BIBLIOTECAS

import Model.UsuarioLogado;

/*
 * A Dao Class DaoUsuarioLogado possui alguns metodos CRUD
 * utilizados com base nos metodos genericos da
 * DaoGeneric, na qual possui os comandos do 
 * Hibernate Framework para transmitir e/ou 
 * receber dados do MySQL
 * */

public class DaoUsuarioLogado {

    public DaoGeneric<UsuarioLogado> dao = new DaoGeneric<>();

	/////////////////////////////////////////////////
	/*
	select (Class<UsuarioLogado> entity)
	retorno: UsuarioLogado
	objetivo: Obtem o usuario logado
	*/
    
    public UsuarioLogado select (Class<UsuarioLogado> entity) { return dao.selectUsuarioLogado(entity); }

	/////////////////////////////////////////////////
	/*
	insert (UsuarioLogado objeto)
	retorno: void
	objetivo: Definir usuario logado em cada sessao
	*/

    public void insert (UsuarioLogado objeto) { dao.insert(objeto); }
    
	/////////////////////////////////////////////////
	/*
	delete()
	retorno: void
	objetivo: Remover o usuario logado para nova sessao
	*/

	public void delete() { dao.deleteUsuarioLogado(); }
}
