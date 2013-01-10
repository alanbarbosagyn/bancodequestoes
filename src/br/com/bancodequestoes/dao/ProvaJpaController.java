/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.bancodequestoes.dao;

import br.com.bancodequestoes.dao.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import br.com.bancodequestoes.model.Professor;
import br.com.bancodequestoes.model.Prova;
import br.com.bancodequestoes.model.Questao;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Alan
 */
public class ProvaJpaController implements Serializable {

    public ProvaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Prova prova) {
        if (prova.getQuestao() == null) {
            prova.setQuestao(new ArrayList<Questao>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Professor professor = prova.getProfessor();
            if (professor != null) {
                professor = em.getReference(professor.getClass(), professor.getId());
                prova.setProfessor(professor);
            }
            Collection<Questao> attachedQuestao = new ArrayList<Questao>();
            for (Questao questaoQuestaoToAttach : prova.getQuestao()) {
                questaoQuestaoToAttach = em.getReference(questaoQuestaoToAttach.getClass(), questaoQuestaoToAttach.getId());
                attachedQuestao.add(questaoQuestaoToAttach);
            }
            prova.setQuestao(attachedQuestao);
            em.persist(prova);
            if (professor != null) {
                professor.getProvas().add(prova);
                professor = em.merge(professor);
            }
            for (Questao questaoQuestao : prova.getQuestao()) {
                questaoQuestao.getProvas().add(prova);
                questaoQuestao = em.merge(questaoQuestao);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Prova prova) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Prova persistentProva = em.find(Prova.class, prova.getId());
            Professor professorOld = persistentProva.getProfessor();
            Professor professorNew = prova.getProfessor();
            Collection<Questao> questaoOld = persistentProva.getQuestao();
            Collection<Questao> questaoNew = prova.getQuestao();
            if (professorNew != null) {
                professorNew = em.getReference(professorNew.getClass(), professorNew.getId());
                prova.setProfessor(professorNew);
            }
            Collection<Questao> attachedQuestaoNew = new ArrayList<Questao>();
            for (Questao questaoNewQuestaoToAttach : questaoNew) {
                questaoNewQuestaoToAttach = em.getReference(questaoNewQuestaoToAttach.getClass(), questaoNewQuestaoToAttach.getId());
                attachedQuestaoNew.add(questaoNewQuestaoToAttach);
            }
            questaoNew = attachedQuestaoNew;
            prova.setQuestao(questaoNew);
            prova = em.merge(prova);
            if (professorOld != null && !professorOld.equals(professorNew)) {
                professorOld.getProvas().remove(prova);
                professorOld = em.merge(professorOld);
            }
            if (professorNew != null && !professorNew.equals(professorOld)) {
                professorNew.getProvas().add(prova);
                professorNew = em.merge(professorNew);
            }
            for (Questao questaoOldQuestao : questaoOld) {
                if (!questaoNew.contains(questaoOldQuestao)) {
                    questaoOldQuestao.getProvas().remove(prova);
                    questaoOldQuestao = em.merge(questaoOldQuestao);
                }
            }
            for (Questao questaoNewQuestao : questaoNew) {
                if (!questaoOld.contains(questaoNewQuestao)) {
                    questaoNewQuestao.getProvas().add(prova);
                    questaoNewQuestao = em.merge(questaoNewQuestao);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = prova.getId();
                if (findProva(id) == null) {
                    throw new NonexistentEntityException("The prova with id " + id + " no longer exists.");
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
            Prova prova;
            try {
                prova = em.getReference(Prova.class, id);
                prova.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The prova with id " + id + " no longer exists.", enfe);
            }
            Professor professor = prova.getProfessor();
            if (professor != null) {
                professor.getProvas().remove(prova);
                professor = em.merge(professor);
            }
            Collection<Questao> questao = prova.getQuestao();
            for (Questao questaoQuestao : questao) {
                questaoQuestao.getProvas().remove(prova);
                questaoQuestao = em.merge(questaoQuestao);
            }
            em.remove(prova);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Prova> findProvaEntities() {
        return findProvaEntities(true, -1, -1);
    }

    public List<Prova> findProvaEntities(int maxResults, int firstResult) {
        return findProvaEntities(false, maxResults, firstResult);
    }

    private List<Prova> findProvaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Prova.class));
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

    public Prova findProva(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Prova.class, id);
        } finally {
            em.close();
        }
    }

    public int getProvaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Prova> rt = cq.from(Prova.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
