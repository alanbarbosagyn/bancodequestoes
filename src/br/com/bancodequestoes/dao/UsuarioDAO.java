/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.bancodequestoes.dao;

import br.com.bancodequestoes.model.Usuario;
import javax.persistence.EntityManager;

/**
 *
 * @author Usuario
 */
public class UsuarioDAO {
    public void adicionar(Usuario a){
        EntityManager em = DAO.getEmf();
        
        try{
            em.getTransaction().begin();
            
            em.persist(a);
            
            em.getTransaction().commit();
        }catch(Exception e){
            em.getTransaction().rollback();
        }
    }
}
