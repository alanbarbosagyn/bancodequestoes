/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.bancodequestoes.dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author Usuario
 */
public class DAO{
    
    private static EntityManager em;

    public DAO(){
        getEmf();
    }


    public static EntityManager getEmf() {
        if(em == null){
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("BancoDeQuestoesPU");
            em = emf.createEntityManager();
        }
        return em;
    }
}
