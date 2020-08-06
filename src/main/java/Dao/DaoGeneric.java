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
 * A Dao Class DaoGeneric possui os metodos CRUD
 * utilizados com os comandos do Hibernate Framework
 * para transmitir e/ou receber dados do MySQL
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
 * entityManager : EntityManager Objeto utilizado para executar comandos do sql
 * ================================
 */
package Dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import Util.HibernateUtil;

public class DaoGeneric<E> {

	private static EntityManager entityManager = HibernateUtil.getEntityManager();

	/* ================================
	 * select
	 * Retorno: E
	 * Objetivo: Metodo de busca atraves da entidade e do id especificados
	 * Parâmetros input: int id, Class<E> entity
	 * Parâmetros output: E
	 * ================================
	 */
	public E select(int id, Class<E> entity) {
		
		E e = entityManager.find(entity, id);
		
		return e;
	}

	/* ================================
	 * selectUsuarioLogado
	 * Retorno: E
	 * Objetivo: Metodo para buscar qual eh o usuario logado e se ele eh admin
	 * Parâmetros input: Class<E> entity
	 * Parâmetros output: E
	 * ================================
	 */
	@SuppressWarnings("unchecked")
	public E selectUsuarioLogado(Class<E> entity) {
		
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		
		List<E> lista = entityManager.createQuery("FROM " + entity.getName()).getResultList();

		transaction.commit();
		
		E result = lista.get(0);
		
		return result;
	}

	/* ================================
	 * insert
	 * Retorno: void
	 * Objetivo: Metodo para cadastrar dados na entidade especificada
	 * Parâmetros input: E entity
	 * Parâmetros output: void
	 * ================================
	 */
	public void insert(E entity) {
		
		// O transaction eh o responsavel por estabelecer a conexao para a execucao do comando sql
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		
		entityManager.persist(entity);
		
		transaction.commit();
	}

	/* ================================
	 * update
	 * Retorno: E
	 * Objetivo: Metodo para editar dados na entidade especificada
	 * Parâmetros input: E entity
	 * Parâmetros output: E
	 * ================================
	 */
	public E update(E entity) {
		
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		
		E updated = entityManager.merge(entity);
		
		transaction.commit();
		
		return updated;
	}

	/* ================================
	 * delete
	 * Retorno: void
	 * Objetivo: Metodo para excluir dados na entidade especificada
	 * Parâmetros input: E entity
	 * Parâmetros output: void
	 * ================================
	 */
	public void delete(E entity) {
		
		Object id = HibernateUtil.getPrimaryKey(entity); 
				
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		
		entityManager.createNativeQuery("DELETE FROM " + entity.getClass().getSimpleName().toLowerCase() +
								" WHERE id = " + id).executeUpdate();
		
		transaction.commit();
	}

	/* ================================
	 * deleteUsuarioLogado
	 * Retorno: void
	 * Objetivo: Metodo DELETE do SQL
	 * Parâmetros input: void
	 * Parâmetros output: void
	 * ================================
	 */
	public void deleteUsuarioLogado() {
		
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		
		entityManager.createNativeQuery("DELETE FROM usuarioLogado WHERE id > 0").executeUpdate();
		
		transaction.commit();
	}

	/* ================================
	 * listar
	 * Retorno: List<E>
	 * Objetivo: Metodo para listar todos os dados na entidade especificada
	 * Parâmetros input: Class<E> entity
	 * Parâmetros output: List<E>
	 * ================================
	 */
	// Metodo SELECT * FROM do MySQL
	@SuppressWarnings("unchecked")
	public List<E> listar(Class<E> entity) {
		
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		
		List<E> lista = entityManager.createQuery("FROM " + entity.getName()).getResultList();

		transaction.commit();
		
		return lista;
	}

	/* ================================
	 * buscar
	 * Retorno: List<E>
	 * Objetivo: Metodo para buscar todos os dados na entidade especificada com a informacao especificada
	 * Parâmetros input: Class<E> entity, String coluna, String buscado
	 * Parâmetros output: List<E>
	 * ================================
	 */
	@SuppressWarnings("unchecked")
	public List<E> buscar(Class<E> entity, String coluna, String buscado) {
		
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		
		List<E> lista = entityManager.createQuery("FROM " + entity.getName() + " WHERE " + coluna + " LIKE '%" + buscado + "%'").getResultList();

		transaction.commit();
		
		return lista;
	}

	/* ================================
	 * buscar
	 * Retorno: E
	 * Objetivo: Metodo para buscar todos os dados na entidade especificada com o ID especificado
	 * Parâmetros input: Class<E> entity, int buscado
	 * Parâmetros output: E
	 * ================================
	 */
	@SuppressWarnings("unchecked")
	public E buscar(Class<E> entity, int buscado) {
		
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		
		List<E> lista = entityManager.createQuery("FROM " + entity.getName() + " WHERE id = " + buscado).getResultList();

		E model = lista.get(0);
		
		transaction.commit();
		
		return model;
	}
}