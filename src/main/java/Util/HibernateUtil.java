package Util;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class HibernateUtil {
	
	private static EntityManagerFactory factory = null;
	
	static { init(); } // Gatilho para executar metodo init padrao do sistema
	
	private static void init() {
		
		try {
			
			if (factory == null) // Caso nao haja uma entidade de gerenciamento definida, crie
				factory = Persistence.createEntityManagerFactory("ProjetoAcervo");
			
		} catch (Exception e) {
			// Caso ocorra algum erro durante a tentativa, apresente a mensagem
			System.out.println("Sistema: Erro ao conectar com a Base de Dados");
		}
	}
	
	// Encapsulamento da entidade de gerenciamento
	public static EntityManager getEntityManager() {
		
		return factory.createEntityManager();
	}
	
	// Metodo para identificacao de entidades por chave primaria
	public static Object getPrimaryKey(Object entity) {
		
		return factory.getPersistenceUnitUtil().getIdentifier(entity);
	}
}
