/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.bancodequestoes.dao;

import br.com.bancodequestoes.dao.exceptions.NonexistentEntityException;
import br.com.bancodequestoes.model.Coordenador;
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
public class CoordenadorJpaController implements Serializable {

    public CoordenadorJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Coordenador coordenador) {
        if (coordenador.getProvas() == null) {
            coordenador.setProvas(new ArrayList<Prova>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Prova> attachedProvas = new ArrayList<Prova>();
            for (Prova provasProvaToAttach : coordenador.getProvas()) {
                provasProvaToAttach = em.getReference(provasProvaToAttach.getClass(), provasProvaToAttach.getId());
                attachedProvas.add(provasProvaToAttach);
            }
            coordenador.setProvas(attachedProvas);
            em.persist(coordenador);
            for (Prova provasProva : coordenador.getProvas()) {
                br.com.bancodequestoes.model.Professor oldProfessorOfProvasProva = provasProva.getProfessor();
                provasProva.setProfessor(coordenador);
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

    public void edit(Coordenador coordenador) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Coordenador persistentCoordenador = em.find(Coordenador.class, coordenador.getId());
            Collection<Prova> provasOld = persistentCoordenador.getProvas();
            Collection<Prova> provasNew = coordenador.getProvas();
            Collection<Prova> attachedProvasNew = new ArrayList<Prova>();
            for (Prova provasNewProvaToAttach : provasNew) {
                provasNewProvaToAttach = em.getReference(provasNewProvaToAttach.getClass(), provasNewProvaToAttach.getId());
                attachedProvasNew.add(provasNewProvaToAttach);
            }
            provasNew = attachedProvasNew;
            coordenador.setProvas(provasNew);
            coordenador = em.merge(coordenador);
            for (Prova provasOldProva : provasOld) {
                if (!provasNew.contains(provasOldProva)) {
                    provasOldProva.setProfessor(null);
                    provasOldProva = em.merge(provasOldProva);
                }
            }
            for (Prova provasNewProva : provasNew) {
                if (!provasOld.contains(provasNewProva)) {
                    Coordenador oldProfessorOfProvasNewProva = (Coordenador) provasNewProva.getProfessor();
                    provasNewProva.setProfessor(coordenador);
                    provasNewProva = em.merge(provasNewProva);
                    if (oldProfessorOfProvasNewProva != null && !oldProfessorOfProvasNewProva.equals(coordenador)) {
                        oldProfessorOfProvasNewProva.getProvas().remove(provasNewProva);
                        oldProfessorOfProvasNewProva = em.merge(oldProfessorOfProvasNewProva);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = coordenador.getId();
                if (findCoordenador(id) == null) {
                    throw new NonexistentEntityException("The coordenador with id " + id + " no longer exists.");
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
            Coordenador coordenador;
            try {
                coordenador = em.getReference(Coordenador.class, id);
                coordenador.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The coordenador with id " + id + " no longer exists.", enfe);
            }
            Collection<Prova> provas = coordenador.getProvas();
            for (Prova provasProva : provas) {
                provasProva.setProfessor(null);
                provasProva = em.merge(provasProva);
            }
            em.remove(coordenador);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Coordenador> findCoordenadorEntities() {
        return findCoordenadorEntities(true, -1, -1);
    }

    public List<Coordenador> findCoordenadorEntities(int maxResults, int firstResult) {
        return findCoordenadorEntities(false, maxResults, firstResult);
    }

    private List<Coordenador> findCoordenadorEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Coordenador.class));
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

    public Coordenador findCoordenador(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Coordenador.class, id);
        } finally {
            em.close();
        }
    }

    public int getCoordenadorCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Coordenador> rt = cq.from(Coordenador.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
