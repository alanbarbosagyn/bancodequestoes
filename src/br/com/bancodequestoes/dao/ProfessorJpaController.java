/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.bancodequestoes.dao;

import br.com.bancodequestoes.dao.exceptions.NonexistentEntityException;
import br.com.bancodequestoes.model.Professor;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import br.com.bancodequestoes.model.Prova;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Alan
 */
public class ProfessorJpaController implements Serializable {

    public ProfessorJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Professor professor) {
        if (professor.getProvas() == null) {
            professor.setProvas(new ArrayList<Prova>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Prova> attachedProvas = new ArrayList<Prova>();
            for (Prova provasProvaToAttach : professor.getProvas()) {
                provasProvaToAttach = em.getReference(provasProvaToAttach.getClass(), provasProvaToAttach.getId());
                attachedProvas.add(provasProvaToAttach);
            }
            professor.setProvas(attachedProvas);
            em.persist(professor);
            for (Prova provasProva : professor.getProvas()) {
                Professor oldProfessorOfProvasProva = provasProva.getProfessor();
                provasProva.setProfessor(professor);
                provasProva = em.merge(provasProva);
                if (oldProfessorOfProvasProva != null) {
                    oldProfessorOfProvasProva.getProvas().remove(provasProva);
                    oldProfessorOfProvasProva = em.merge(oldProfessorOfProvasProva);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Professor professor) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Professor persistentProfessor = em.find(Professor.class, professor.getId());
            Collection<Prova> provasOld = persistentProfessor.getProvas();
            Collection<Prova> provasNew = professor.getProvas();
            Collection<Prova> attachedProvasNew = new ArrayList<Prova>();
            for (Prova provasNewProvaToAttach : provasNew) {
                provasNewProvaToAttach = em.getReference(provasNewProvaToAttach.getClass(), provasNewProvaToAttach.getId());
                attachedProvasNew.add(provasNewProvaToAttach);
            }
            provasNew = attachedProvasNew;
            professor.setProvas(provasNew);
            professor = em.merge(professor);
            for (Prova provasOldProva : provasOld) {
                if (!provasNew.contains(provasOldProva)) {
                    provasOldProva.setProfessor(null);
                    provasOldProva = em.merge(provasOldProva);
                }
            }
            for (Prova provasNewProva : provasNew) {
                if (!provasOld.contains(provasNewProva)) {
                    Professor oldProfessorOfProvasNewProva = provasNewProva.getProfessor();
                    provasNewProva.setProfessor(professor);
                    provasNewProva = em.merge(provasNewProva);
                    if (oldProfessorOfProvasNewProva != null && !oldProfessorOfProvasNewProva.equals(professor)) {
                        oldProfessorOfProvasNewProva.getProvas().remove(provasNewProva);
                        oldProfessorOfProvasNewProva = em.merge(oldProfessorOfProvasNewProva);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = professor.getId();
                if (findProfessor(id) == null) {
                    throw new NonexistentEntityException("The professor with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Long id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Professor professor;
            try {
                professor = em.getReference(Professor.class, id);
                professor.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The professor with id " + id + " no longer exists.", enfe);
            }
            Collection<Prova> provas = professor.getProvas();
            for (Prova provasProva : provas) {
                provasProva.setProfessor(null);
                provasProva = em.merge(provasProva);
            }
            em.remove(professor);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Professor> findProfessorEntities() {
        return findProfessorEntities(true, -1, -1);
    }

    public List<Professor> findProfessorEntities(int maxResults, int firstResult) {
        return findProfessorEntities(false, maxResults, firstResult);
    }

    private List<Professor> findProfessorEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Professor.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Professor findProfessor(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Professor.class, id);
        } finally {
            em.close();
        }
    }

    public int getProfessorCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Professor> rt = cq.from(Professor.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
