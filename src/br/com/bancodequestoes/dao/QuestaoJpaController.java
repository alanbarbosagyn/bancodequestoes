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
import br.com.bancodequestoes.model.Alternativa;
import java.util.ArrayList;
import java.util.Collection;
import br.com.bancodequestoes.model.Prova;
import br.com.bancodequestoes.model.Questao;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Alan
 */
public class QuestaoJpaController implements Serializable {

    public QuestaoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Questao questao) {
        if (questao.getAlternativas() == null) {
            questao.setAlternativas(new ArrayList<Alternativa>());
        }
        if (questao.getProvas() == null) {
            questao.setProvas(new ArrayList<Prova>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Alternativa resposta = questao.getResposta();
            if (resposta != null) {
                resposta = em.getReference(resposta.getClass(), resposta.getId());
                questao.setResposta(resposta);
            }
            Collection<Alternativa> attachedAlternativas = new ArrayList<Alternativa>();
            for (Alternativa alternativasAlternativaToAttach : questao.getAlternativas()) {
                alternativasAlternativaToAttach = em.getReference(alternativasAlternativaToAttach.getClass(), alternativasAlternativaToAttach.getId());
                attachedAlternativas.add(alternativasAlternativaToAttach);
            }
            questao.setAlternativas(attachedAlternativas);
            Collection<Prova> attachedProvas = new ArrayList<Prova>();
            for (Prova provasProvaToAttach : questao.getProvas()) {
                provasProvaToAttach = em.getReference(provasProvaToAttach.getClass(), provasProvaToAttach.getId());
                attachedProvas.add(provasProvaToAttach);
            }
            questao.setProvas(attachedProvas);
            em.persist(questao);
            if (resposta != null) {
                Questao oldQuestaoOfResposta = resposta.getQuestao();
                if (oldQuestaoOfResposta != null) {
                    oldQuestaoOfResposta.setResposta(null);
                    oldQuestaoOfResposta = em.merge(oldQuestaoOfResposta);
                }
                resposta.setQuestao(questao);
                resposta = em.merge(resposta);
            }
            for (Alternativa alternativasAlternativa : questao.getAlternativas()) {
                Questao oldQuestaoOfAlternativasAlternativa = alternativasAlternativa.getQuestao();
                alternativasAlternativa.setQuestao(questao);
                alternativasAlternativa = em.merge(alternativasAlternativa);
                if (oldQuestaoOfAlternativasAlternativa != null) {
                    oldQuestaoOfAlternativasAlternativa.getAlternativas().remove(alternativasAlternativa);
                    oldQuestaoOfAlternativasAlternativa = em.merge(oldQuestaoOfAlternativasAlternativa);
                }
            }
            for (Prova provasProva : questao.getProvas()) {
                provasProva.getQuestao().add(questao);
                provasProva = em.merge(provasProva);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Questao questao) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Questao persistentQuestao = em.find(Questao.class, questao.getId());
            Alternativa respostaOld = persistentQuestao.getResposta();
            Alternativa respostaNew = questao.getResposta();
            Collection<Alternativa> alternativasOld = persistentQuestao.getAlternativas();
            Collection<Alternativa> alternativasNew = questao.getAlternativas();
            Collection<Prova> provasOld = persistentQuestao.getProvas();
            Collection<Prova> provasNew = questao.getProvas();
            if (respostaNew != null) {
                respostaNew = em.getReference(respostaNew.getClass(), respostaNew.getId());
                questao.setResposta(respostaNew);
            }
            Collection<Alternativa> attachedAlternativasNew = new ArrayList<Alternativa>();
            for (Alternativa alternativasNewAlternativaToAttach : alternativasNew) {
                alternativasNewAlternativaToAttach = em.getReference(alternativasNewAlternativaToAttach.getClass(), alternativasNewAlternativaToAttach.getId());
                attachedAlternativasNew.add(alternativasNewAlternativaToAttach);
            }
            alternativasNew = attachedAlternativasNew;
            questao.setAlternativas(alternativasNew);
            Collection<Prova> attachedProvasNew = new ArrayList<Prova>();
            for (Prova provasNewProvaToAttach : provasNew) {
                provasNewProvaToAttach = em.getReference(provasNewProvaToAttach.getClass(), provasNewProvaToAttach.getId());
                attachedProvasNew.add(provasNewProvaToAttach);
            }
            provasNew = attachedProvasNew;
            questao.setProvas(provasNew);
            questao = em.merge(questao);
            if (respostaOld != null && !respostaOld.equals(respostaNew)) {
                respostaOld.setQuestao(null);
                respostaOld = em.merge(respostaOld);
            }
            if (respostaNew != null && !respostaNew.equals(respostaOld)) {
                Questao oldQuestaoOfResposta = respostaNew.getQuestao();
                if (oldQuestaoOfResposta != null) {
                    oldQuestaoOfResposta.setResposta(null);
                    oldQuestaoOfResposta = em.merge(oldQuestaoOfResposta);
                }
                respostaNew.setQuestao(questao);
                respostaNew = em.merge(respostaNew);
            }
            for (Alternativa alternativasOldAlternativa : alternativasOld) {
                if (!alternativasNew.contains(alternativasOldAlternativa)) {
                    alternativasOldAlternativa.setQuestao(null);
                    alternativasOldAlternativa = em.merge(alternativasOldAlternativa);
                }
            }
            for (Alternativa alternativasNewAlternativa : alternativasNew) {
                if (!alternativasOld.contains(alternativasNewAlternativa)) {
                    Questao oldQuestaoOfAlternativasNewAlternativa = alternativasNewAlternativa.getQuestao();
                    alternativasNewAlternativa.setQuestao(questao);
                    alternativasNewAlternativa = em.merge(alternativasNewAlternativa);
                    if (oldQuestaoOfAlternativasNewAlternativa != null && !oldQuestaoOfAlternativasNewAlternativa.equals(questao)) {
                        oldQuestaoOfAlternativasNewAlternativa.getAlternativas().remove(alternativasNewAlternativa);
                        oldQuestaoOfAlternativasNewAlternativa = em.merge(oldQuestaoOfAlternativasNewAlternativa);
                    }
                }
            }
            for (Prova provasOldProva : provasOld) {
                if (!provasNew.contains(provasOldProva)) {
                    provasOldProva.getQuestao().remove(questao);
                    provasOldProva = em.merge(provasOldProva);
                }
            }
            for (Prova provasNewProva : provasNew) {
                if (!provasOld.contains(provasNewProva)) {
                    provasNewProva.getQuestao().add(questao);
                    provasNewProva = em.merge(provasNewProva);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = questao.getId();
                if (findQuestao(id) == null) {
                    throw new NonexistentEntityException("The questao with id " + id + " no longer exists.");
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
            Questao questao;
            try {
                questao = em.getReference(Questao.class, id);
                questao.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The questao with id " + id + " no longer exists.", enfe);
            }
            Alternativa resposta = questao.getResposta();
            if (resposta != null) {
                resposta.setQuestao(null);
                resposta = em.merge(resposta);
            }
            Collection<Alternativa> alternativas = questao.getAlternativas();
            for (Alternativa alternativasAlternativa : alternativas) {
                alternativasAlternativa.setQuestao(null);
                alternativasAlternativa = em.merge(alternativasAlternativa);
            }
            Collection<Prova> provas = questao.getProvas();
            for (Prova provasProva : provas) {
                provasProva.getQuestao().remove(questao);
                provasProva = em.merge(provasProva);
            }
            em.remove(questao);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Questao> findQuestaoEntities() {
        return findQuestaoEntities(true, -1, -1);
    }

    public List<Questao> findQuestaoEntities(int maxResults, int firstResult) {
        return findQuestaoEntities(false, maxResults, firstResult);
    }

    private List<Questao> findQuestaoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Questao.class));
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

    public Questao findQuestao(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Questao.class, id);
        } finally {
            em.close();
        }
    }

    public int getQuestaoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Questao> rt = cq.from(Questao.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
