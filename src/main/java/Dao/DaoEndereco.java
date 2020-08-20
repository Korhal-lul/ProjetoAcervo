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
 * CRUD utilizados com base nos metodos genericos para manipulacao dos dados do usuario
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

import Model.Endereco;

import java.util.List;

/*
 * A Dao Class DaoUsuario possui alguns metodos CRUD
 * utilizados com base nos metodos genericos da
 * DaoGeneric, na qual possui os comandos do
 * Hibernate Framework para transmitir e/ou
 * receber dados do MySQL
 * */

public class DaoEndereco {

    // Cria o Objeto dao para realizar chamadas de metodos genericos
    public DaoGeneric<Endereco> dao = new DaoGeneric<>();

    /*
     * Nome da Função: select Retorno: Usuario Objetivo: Listagem do banco de dados
     */

    public Endereco select(int id, Class<Endereco> entity) {
        return dao.select(id, entity);
    }

    /*
     * Nome da Função: insert
     * Retorno: void
     * Objetivo: inserir no banco de dados
     */

    // Metodo INSERT do MySQL
    public void insert(Endereco objeto) {
        dao.insert(objeto);
    }

    /*
     * Nome da Função: insert
     * Retorno: void
     * Objetivo: Receber Objeto com as informacoes a serem editadas obtem do BD os dados atuais
     * respectivos ao requerido ID e manda o objeto com as novas informacoes ao BD
     */

    public void update(Endereco enderecoOld) {

        @SuppressWarnings("unchecked")
        Endereco enderecoNew = dao.select(enderecoOld.getId(), (Class<Endereco>) enderecoOld.getClass());

        enderecoNew.setCep(enderecoOld.getCep());
        enderecoNew.setLogradouro(enderecoOld.getLogradouro());
        enderecoNew.setComplemento(enderecoOld.getComplemento());
        enderecoNew.setLocalidade(enderecoOld.getLocalidade());
        enderecoNew.setUnidade(enderecoOld.getUnidade());

        dao.update(enderecoNew);
    }

    /*
     * Nome da Função: delete
     * Retorno: void
     * Objetivo: deleta dado do banco de dados
     */

    @SuppressWarnings("unchecked")
    public void delete(Endereco enderecos, int id) {

        enderecos = dao.select(id, (Class<Endereco>) enderecos.getClass());
        dao.delete(enderecos);
    }

    /*
     * Nome da Função: listar
     * Retorno: List<Usuario>
     * Objetivo: listar dados que sera chamado na TableView do JavaFX
     */

    public List<Endereco> listar(Class<Endereco> usuarios) {
        return dao.listar(usuarios);
    }
    /*
     * Nome da Função: buscar
     * Retorno: List<Usuario>
     * Objetivo: Metodo de busca que retorna uma lista com os resultados requeridos
     */

    public List<Endereco> buscar(Class<Endereco> enderecos, String buscado) {
        return dao.buscar(enderecos, "nome", buscado);
    }
}