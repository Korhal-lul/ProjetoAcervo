package Dao;

import Model.UsuarioLogado;

/*
 * A Dao Class DaoUsuarioLogado possui alguns metodos CRUD
 * utilizados com base nos metodos genericos da
 * DaoGeneric, na qual possui os comandos do 
 * Hibernate Framework para transmitir e/ou 
 * receber dados do MySQL
 * */

public class DaoUsuarioLogado {

	// Cria o Objeto dao para realizar chamadas de metodos genericos
    public DaoGeneric<UsuarioLogado> dao = new DaoGeneric<>();

    // Obtem o usuario logado
    public UsuarioLogado select (Class<UsuarioLogado> entity) { return dao.selectUsuarioLogado(entity); }

    // Definir usuario logado em cada sessao
    public void insert (UsuarioLogado objeto) { dao.insert(objeto); }

    // remover o usuario logado para nova sessao
	public void delete() { dao.deleteUsuarioLogado(); }
}
