/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.bancodequestoes.dao;

import br.com.bancodequestoes.model.Assunto;
import javax.persistence.EntityManager;

/**
 *
 * @author Usuario
 */
public class AssuntoDAO {

    public AssuntoDAO() {
    }


    public void adicionar(Assunto a) {
        EntityManager em = DAO.getEmf();

        try {
            em.getTransaction().begin();

            em.persist(a);

            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
        }
    }

    public void editar(Long id) {
        EntityManager em = DAO.getEmf();

        try {
            em.getTransaction().begin();
            
            Assunto a = em.find(Assunto.class, id);

            em.getTransaction().commit();

        } catch (Exception e) {
        }
    }
    
    public void excluir(Assunto a){
        EntityManager em = DAO.getEmf();

        try {
            em.getTransaction().begin();

            em.remove(a);

            em.getTransaction().commit();

        } catch (Exception e) {
        }
    }

}
