package Dao;

import Model.Material;

import java.util.List;

/*
 * A Dao Class DaoMaterial possui alguns metodos CRUD
 * utilizados com base nos metodos genericos da
 * DaoGeneric, na qual possui os comandos do 
 * Hibernate Framework para transmitir e/ou 
 * receber dados do MySQL
 * */

public class DaoMaterial {

	// Cria o Objeto dao para realizar chamadas de metodos genericos
    public DaoGeneric<Material> dao = new DaoGeneric<>();

    // Metodo SELECT do MySQL
    public Material select (int id, Class<Material> entity) {
        return dao.select(id, entity);
    }

    // Metodo INSERT do MySQL
    public void insert (Material objeto) {
        dao.insert(objeto);
    }

    // Metodo UPDATE do MySQL
    // Recebe Objeto com as informacoes a serem editadas
    // obtem do BD os dados atuais respectivos ao requerido ID
    // e manda o objeto com as novas informacoes ao BD
    public void update (Material materiaisOld) {

        @SuppressWarnings("unchecked")
		Material materiaisNew = dao.select(materiaisOld.getId(), (Class<Material>) materiaisOld.getClass());

        materiaisNew.setDescricao(materiaisOld.getDescricao());
        materiaisNew.setTipo(materiaisOld.getTipo());
        materiaisNew.setQuantAtual(materiaisOld.getQuantAtual());

        dao.update(materiaisNew);
    }

    // Metodo DELETE do MySQL
    // Recebe o ID para excluir do BD
    @SuppressWarnings("unchecked")
	public void delete (Material materiais, int id) {

        materiais = dao.select(id, (Class<Material>) materiais.getClass());
        dao.delete(materiais);
    }

    // Metodo para listar dados que sera chamado na TableView do JavaFX
    public List<Material> listar (Class<Material> materiais) {
        return dao.listar(materiais);
    }
    
    // Metodo de busca que retorna uma lista com os resultados requeridos
    public List<Material> buscar (Class<Material> materiais, String buscado) {
    	return dao.buscar(materiais, "descricao", buscado);
    }
    
    // Metodo de busca que retorna um objeto com os resultado requeridos por ID
    public Material buscarPorId (Class<Material> materiais, int buscado) {
    	return dao.buscar(materiais, buscado);
    }
}
