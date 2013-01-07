/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.bancodequestoes.dao;

import br.com.bancodequestoes.model.Usuario;
import java.util.Calendar;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

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
    
    
    public Usuario buscar(String nome) throws Exception{
        EntityManager em = DAO.getEmf();
        
        try{
            em.getTransaction().begin();
            
            Query query = em.createNamedQuery("Usuario.busca");
            
            query.setParameter(1,nome);
            
            List<Usuario> listaUsuario = query.getResultList();
            
            em.getTransaction().commit();
            
            Usuario u = null;
            
            for(Usuario usuario : listaUsuario){
                u = usuario;
            }
            
            return u;
        }catch(Exception e){
            em.getTransaction().rollback();
            throw new Exception("Usuário não encontrado!");
        }
    }
    
    
    public static void main(String args[]) throws Exception {
        UsuarioDAO dao = new UsuarioDAO();

        Usuario usuario = dao.buscar("alan");
        
        System.out.println(usuario.getNome());
        
    }
}
