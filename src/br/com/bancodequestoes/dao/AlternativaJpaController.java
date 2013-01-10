/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.bancodequestoes.dao;

import br.com.bancodequestoes.dao.exceptions.NonexistentEntityException;
import br.com.bancodequestoes.model.Alternativa;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import br.com.bancodequestoes.model.Questao;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Alan
 */
public class AlternativaJpaController implements Serializable {

    public AlternativaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Alternativa alternativa) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Questao questao = alternativa.getQuestao();
            if (questao != null) {
                questao = em.getReference(questao.getClass(), questao.getId());
                alternativa.setQuestao(questao);
            }
            em.persist(alternativa);
            if (questao != null) {
                Alternativa oldRespostaOfQuestao = questao.getResposta();
                if (oldRespostaOfQuestao != null) {
                    oldRespostaOfQuestao.setQuestao(null);
                    oldRespostaOfQuestao = em.merge(oldRespostaOfQuestao);
                }
                questao.setResposta(alternativa);
                questao = em.merge(questao);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Alternativa alternativa) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Alternativa persistentAlternativa = em.find(Alternativa.class, alternativa.getId());
            Questao questaoOld = persistentAlternativa.getQuestao();
            Questao questaoNew = alternativa.getQuestao();
            if (questaoNew != null) {
                questaoNew = em.getReference(questaoNew.getClass(), questaoNew.getId());
                alternativa.setQuestao(questaoNew);
            }
            alternativa = em.merge(alternativa);
            if (questaoOld != null && !questaoOld.equals(questaoNew)) {
                questaoOld.setResposta(null);
                questaoOld = em.merge(questaoOld);
            }
            if (questaoNew != null && !questaoNew.equals(questaoOld)) {
                Alternativa oldRespostaOfQuestao = questaoNew.getResposta();
                if (oldRespostaOfQuestao != null) {
                    oldRespostaOfQuestao.setQuestao(null);
                    oldRespostaOfQuestao = em.merge(oldRespostaOfQuestao);
                }
                questaoNew.setResposta(alternativa);
                questaoNew = em.merge(questaoNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = alternativa.getId();
                if (findAlternativa(id) == null) {
                    throw new NonexistentEntityException("The alternativa with id " + id + " no longer exists.");
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
            Alternativa alternativa;
            try {
                alternativa = em.getReference(Alternativa.class, id);
                alternativa.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The alternativa with id " + id + " no longer exists.", enfe);
            }
            Questao questao = alternativa.getQuestao();
            if (questao != null) {
                questao.setResposta(null);
                questao = em.merge(questao);
            }
            em.remove(alternativa);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Alternativa> findAlternativaEntities() {
        return findAlternativaEntities(true, -1, -1);
    }

    public List<Alternativa> findAlternativaEntities(int maxResults, int firstResult) {
        return findAlternativaEntities(false, maxResults, firstResult);
    }

    private List<Alternativa> findAlternativaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Alternativa.class));
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

    public Alternativa findAlternativa(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Alternativa.class, id);
        } finally {
            em.close();
        }
    }

    public int getAlternativaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Alternativa> rt = cq.from(Alternativa.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
