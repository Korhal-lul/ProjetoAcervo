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
* Classe de conexão do back-end com o banco de dados
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
* Data: 06/08/2020
* Responsável: Braian Costa Zapelini
*
* ================================
* Declaração de variáveis
* factory : EntityManagerFactory Recebe null somente para instânciar / ser inicializada
* ================================
*/

package Util;

// IMPORTAÇÕES DE BIBLIOTECAS

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class HibernateUtil {
	
	private static EntityManagerFactory factory = null;
	
	// Gatilho para executar metodo init padrao do sistema
	static { init(); } 
	
	private static void init() {
		
		try {
			
			// Caso nao haja uma entidade de gerenciamento definida, crie
			if (factory == null)
				factory = Persistence.createEntityManagerFactory("ProjetoAcervo");

			
		} catch (Exception e) {
			
			// Caso ocorra algum erro durante a tentativa, apresente a mensagem
			System.out.println("Sistema: Erro ao conectar com a Base de Dados");
		}
	}
	
	/////////////////////////////////////////////////
	/*
	getEntityManager()
	retorno: EntityManager
	objetivo: Encapsulamento da entidade de gerenciamento
	*/
	
	public static EntityManager getEntityManager() {
		
		return factory.createEntityManager();
	}
	
	/////////////////////////////////////////////////
	/*
	getPrimaryKey(Object entity)
	retorno: Object
	objetivo: Metodo para identificacao de entidades por chave primaria
	*/

	public static Object getPrimaryKey(Object entity) {
		
		return factory.getPersistenceUnitUtil().getIdentifier(entity);
	}
}
