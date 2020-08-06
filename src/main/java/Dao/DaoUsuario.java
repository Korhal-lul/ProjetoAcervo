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

    // Metodo SELECT do MySQL
    public Usuario select (int id, Class<Usuario> entity) {
        return dao.select(id, entity);
    }

    // Metodo INSERT do MySQL
    public void insert (Usuario objeto) {
        dao.insert(objeto);
    }
    
    // Metodo UPDATE do MySQL
    // Recebe Objeto com as informacoes a serem editadas
    // obtem do BD os dados atuais respectivos ao requerido ID
    // e manda o objeto com as novas informacoes ao BD
    public void update (Usuario usuarioOld) {

    	@SuppressWarnings("unchecked")
		Usuario usuarioNew = dao.select(usuarioOld.getId(), (Class<Usuario>) usuarioOld.getClass());

    	usuarioNew.setNome(usuarioOld.getNome());
    	usuarioNew.setSenha(usuarioOld.getSenha());
    	usuarioNew.setAdmin(usuarioOld.isAdmin());

        dao.update(usuarioNew);
    }

    // Metodo DELETE do MySQL
    // Recebe o ID para excluir do BD
    @SuppressWarnings("unchecked")
	public void delete (Usuario usuarios, int id) {

        usuarios = dao.select(id, (Class<Usuario>) usuarios.getClass());
        dao.delete(usuarios);
    }

    // Metodo para listar dados que sera chamado na TableView do JavaFX
    public List<Usuario> listar (Class<Usuario> usuarios) {
        return dao.listar(usuarios);
    }
    
    // Metodo de busca que retorna uma lista com os resultados requeridos
    public List<Usuario> buscar (Class<Usuario> usuarios, String buscado) {
    	return dao.buscar(usuarios, "nome", buscado);
    }
}
