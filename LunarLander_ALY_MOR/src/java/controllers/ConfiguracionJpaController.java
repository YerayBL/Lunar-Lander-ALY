/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import controllers.exceptions.IllegalOrphanException;
import controllers.exceptions.NonexistentEntityException;
import controllers.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import model.Score;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import model.Configuracion;

/**
 *
 * @author tuno
 */
public class ConfiguracionJpaController implements Serializable {

    public ConfiguracionJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Configuracion configuracion) throws PreexistingEntityException, Exception {
        if (configuracion.getScoreList() == null) {
            configuracion.setScoreList(new ArrayList<Score>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Score> attachedScoreList = new ArrayList<Score>();
            for (Score scoreListScoreToAttach : configuracion.getScoreList()) {
                scoreListScoreToAttach = em.getReference(scoreListScoreToAttach.getClass(), scoreListScoreToAttach.getIdPuntuacion());
                attachedScoreList.add(scoreListScoreToAttach);
            }
            configuracion.setScoreList(attachedScoreList);
            em.persist(configuracion);
            for (Score scoreListScore : configuracion.getScoreList()) {
                Configuracion oldIdConfiguracionOfScoreListScore = scoreListScore.getIdConfiguracion();
                scoreListScore.setIdConfiguracion(configuracion);
                scoreListScore = em.merge(scoreListScore);
                if (oldIdConfiguracionOfScoreListScore != null) {
                    oldIdConfiguracionOfScoreListScore.getScoreList().remove(scoreListScore);
                    oldIdConfiguracionOfScoreListScore = em.merge(oldIdConfiguracionOfScoreListScore);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findConfiguracion(configuracion.getIdConfiguracion()) != null) {
                throw new PreexistingEntityException("Configuracion " + configuracion + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Configuracion configuracion) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Configuracion persistentConfiguracion = em.find(Configuracion.class, configuracion.getIdConfiguracion());
            List<Score> scoreListOld = persistentConfiguracion.getScoreList();
            List<Score> scoreListNew = configuracion.getScoreList();
            List<String> illegalOrphanMessages = null;
            for (Score scoreListOldScore : scoreListOld) {
                if (!scoreListNew.contains(scoreListOldScore)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Score " + scoreListOldScore + " since its idConfiguracion field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Score> attachedScoreListNew = new ArrayList<Score>();
            for (Score scoreListNewScoreToAttach : scoreListNew) {
                scoreListNewScoreToAttach = em.getReference(scoreListNewScoreToAttach.getClass(), scoreListNewScoreToAttach.getIdPuntuacion());
                attachedScoreListNew.add(scoreListNewScoreToAttach);
            }
            scoreListNew = attachedScoreListNew;
            configuracion.setScoreList(scoreListNew);
            configuracion = em.merge(configuracion);
            for (Score scoreListNewScore : scoreListNew) {
                if (!scoreListOld.contains(scoreListNewScore)) {
                    Configuracion oldIdConfiguracionOfScoreListNewScore = scoreListNewScore.getIdConfiguracion();
                    scoreListNewScore.setIdConfiguracion(configuracion);
                    scoreListNewScore = em.merge(scoreListNewScore);
                    if (oldIdConfiguracionOfScoreListNewScore != null && !oldIdConfiguracionOfScoreListNewScore.equals(configuracion)) {
                        oldIdConfiguracionOfScoreListNewScore.getScoreList().remove(scoreListNewScore);
                        oldIdConfiguracionOfScoreListNewScore = em.merge(oldIdConfiguracionOfScoreListNewScore);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = configuracion.getIdConfiguracion();
                if (findConfiguracion(id) == null) {
                    throw new NonexistentEntityException("The configuracion with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Configuracion configuracion;
            try {
                configuracion = em.getReference(Configuracion.class, id);
                configuracion.getIdConfiguracion();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The configuracion with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Score> scoreListOrphanCheck = configuracion.getScoreList();
            for (Score scoreListOrphanCheckScore : scoreListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Configuracion (" + configuracion + ") cannot be destroyed since the Score " + scoreListOrphanCheckScore + " in its scoreList field has a non-nullable idConfiguracion field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(configuracion);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Configuracion> findConfiguracionEntities() {
        return findConfiguracionEntities(true, -1, -1);
    }

    public List<Configuracion> findConfiguracionEntities(int maxResults, int firstResult) {
        return findConfiguracionEntities(false, maxResults, firstResult);
    }

    private List<Configuracion> findConfiguracionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Configuracion.class));
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

    public Configuracion findConfiguracion(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Configuracion.class, id);
        } finally {
            em.close();
        }
    }

    public int getConfiguracionCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Configuracion> rt = cq.from(Configuracion.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
