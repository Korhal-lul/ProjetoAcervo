package Dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import Util.HibernateUtil;

/*
 * A Dao Class DaoGeneric possui os metodos CRUD
 * utilizados com os comandos do Hibernate Framework
 * para transmitir e/ou receber dados do MySQL
 * */

public class DaoGeneric<E> {
	
	// Instancia o entity manager para poder executar os comandos sql
	private static EntityManager entityManager = HibernateUtil.getEntityManager();
	
	// Metodo de busca atraves da entidade e do id especificados
	public E select(int id, Class<E> entity) {
		
		E e = entityManager.find(entity, id);
		
		return e;
	}
	
	// Metodo para buscar qual eh o usuario logado e se ele eh admin
	@SuppressWarnings("unchecked")
	public E selectUsuarioLogado(Class<E> entity) {
		
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		
		List<E> lista = entityManager.createQuery("FROM " + entity.getName()).getResultList();

		transaction.commit();
		
		E result = lista.get(0);
		
		return result;
	}
	
	// Metodo para cadastrar dados na entidade especificada
	public void insert(E entity) {
		
		// O transaction eh o responsavel por estabelecer a conexao para a execucao do comando sql
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		
		entityManager.persist(entity);
		
		transaction.commit();
	}
	
	// Metodo para editar dados na entidade especificada
	public E update(E entity) {
		
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		
		E updated = entityManager.merge(entity);
		
		transaction.commit();
		
		return updated;
	}
	
	// Metodo para excluir dados na entidade especificada
	public void delete(E entity) {
		
		Object id = HibernateUtil.getPrimaryKey(entity); 
				
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		
		entityManager.createNativeQuery("DELETE FROM " + entity.getClass().getSimpleName().toLowerCase() +
								" WHERE id = " + id).executeUpdate();
		
		transaction.commit();
	}
	
	// Metodo DELETE do SQL
	public void deleteUsuarioLogado() {
		
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		
		entityManager.createNativeQuery("DELETE FROM usuarioLogado WHERE id > 0").executeUpdate();
		
		transaction.commit();
	}
	
	
	// Metodo para listar todos os dados na entidade especificada
	// Metodo SELECT * FROM do MySQL
	@SuppressWarnings("unchecked")
	public List<E> listar(Class<E> entity) {
		
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		
		List<E> lista = entityManager.createQuery("FROM " + entity.getName()).getResultList();

		transaction.commit();
		
		return lista;
	}
	
	// Metodo para buscar todos os dados na entidade especificada com a informacao especificada
	@SuppressWarnings("unchecked")
	public List<E> buscar(Class<E> entity, String coluna, String buscado) {
		
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		
		List<E> lista = entityManager.createQuery("FROM " + entity.getName() + " WHERE " + coluna + " LIKE '%" + buscado + "%'").getResultList();

		transaction.commit();
		
		return lista;
	}
	
	// Metodo para buscar todos os dados na entidade especificada com o ID especificado
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