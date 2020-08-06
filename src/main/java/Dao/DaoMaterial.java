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
 * A Dao Class DaoMaterial possui alguns metodos CRUD utilizados com base nos metodos genericos da
 * DaoGeneric, na qual possui os comandos do Hibernate Framework para transmitir e/ou receber dados do MySQL
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
 *  dao : DaoGeneric<Material> Objeto utilizado para realizar chamadas de metodos genericos
 * ================================
 */

package Dao;

import Model.Material;

import java.util.List;

public class DaoMaterial {

	// Cria o Objeto dao para realizar chamadas de metodos genericos
    public DaoGeneric<Material> dao = new DaoGeneric<>();

    /* ================================
     * select
     * Retorno: Material
     * Objetivo: Metodo SELECT do MySQL
     * Parâmetros input: int id, Class<Material> entity
     * Parâmetros output: Material
     * ================================
     */
    public Material select (int id, Class<Material> entity) {
        return dao.select(id, entity);
    }

    /* ================================
     * insert
     * Retorno: void
     * Objetivo: Metodo INSERT do MySQL
     * Parâmetros input: Material objeto
     * Parâmetros output: void
     * ================================
     */
    public void insert (Material objeto) {
        dao.insert(objeto);
    }

    /* ================================
     * update
     * Retorno: void
     * Objetivo: Metodo UPDATE do MySQL, recebe Objeto com as informacoes a serem editadas,
     * obtem do BD os dados atuais respectivos ao requerido ID e manda o objeto com as novas informacoes ao BD
     * Parâmetros input: Material materiaisOld
     * Parâmetros output: void
     * ================================
     */
    public void update (Material materiaisOld) {

        @SuppressWarnings("unchecked")
		Material materiaisNew = dao.select(materiaisOld.getId(), (Class<Material>) materiaisOld.getClass());

        materiaisNew.setDescricao(materiaisOld.getDescricao());
        materiaisNew.setTipo(materiaisOld.getTipo());
        materiaisNew.setQuantAtual(materiaisOld.getQuantAtual());

        dao.update(materiaisNew);
    }

    /* ================================
     * delete
     * Retorno: void
     * Objetivo: Metodo DELETE do MySQL, recebe o ID para excluir do BD
     * Parâmetros input: Material materiais, int id
     * Parâmetros output: void
     * ================================
     */

    @SuppressWarnings("unchecked")
	public void delete (Material materiais, int id) {

        materiais = dao.select(id, (Class<Material>) materiais.getClass());
        dao.delete(materiais);
    }

    /* ================================
     * listar
     * Retorno: List<Material>
     * Objetivo: Metodo para listar dados que sera chamado na TableView do JavaFX
     * Parâmetros input: Class<Material> materiais
     * Parâmetros output: List<Material>
     * ================================
     */
    public List<Material> listar (Class<Material> materiais) {
        return dao.listar(materiais);
    }

    /* ================================
     * buscar
     * Retorno: List<Material>
     * Objetivo: Metodo de busca que retorna uma lista com os resultados requeridos
     * Parâmetros input: Class<Material> materiais, String buscado
     * Parâmetros output: List<Material>
     * ================================
     */
    public List<Material> buscar (Class<Material> materiais, String buscado) {
    	return dao.buscar(materiais, "descricao", buscado);
    }

    /* ================================
     * buscarPorId
     * Retorno: Material
     * Objetivo: Metodo de busca que retorna um objeto com os resultado requeridos por ID
     * Parâmetros input: Class<Material> materiais, int buscado
     * Parâmetros output: Material
     * ================================
     */
    public Material buscarPorId (Class<Material> materiais, int buscado) {
    	return dao.buscar(materiais, buscado);
    }
}
