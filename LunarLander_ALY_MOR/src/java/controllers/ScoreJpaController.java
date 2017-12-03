/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import controllers.exceptions.NonexistentEntityException;
import controllers.exceptions.PreexistingEntityException;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import model.Configuracion;
import model.Score;
import model.Usuario;

/**
 *
 * @author tuno
 */
public class ScoreJpaController implements Serializable {

    public ScoreJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Score score) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Configuracion idConfiguracion = score.getIdConfiguracion();
            if (idConfiguracion != null) {
                idConfiguracion = em.getReference(idConfiguracion.getClass(), idConfiguracion.getIdConfiguracion());
                score.setIdConfiguracion(idConfiguracion);
            }
            Usuario idUsuario = score.getIdUsuario();
            if (idUsuario != null) {
                idUsuario = em.getReference(idUsuario.getClass(), idUsuario.getIdUsuario());
                score.setIdUsuario(idUsuario);
            }
            em.persist(score);
            if (idConfiguracion != null) {
                idConfiguracion.getScoreList().add(score);
                idConfiguracion = em.merge(idConfiguracion);
            }
            if (idUsuario != null) {
                idUsuario.getScoreList().add(score);
                idUsuario = em.merge(idUsuario);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findScore(score.getIdPuntuacion()) != null) {
                throw new PreexistingEntityException("Score " + score + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Score score) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Score persistentScore = em.find(Score.class, score.getIdPuntuacion());
            Configuracion idConfiguracionOld = persistentScore.getIdConfiguracion();
            Configuracion idConfiguracionNew = score.getIdConfiguracion();
            Usuario idUsuarioOld = persistentScore.getIdUsuario();
            Usuario idUsuarioNew = score.getIdUsuario();
            if (idConfiguracionNew != null) {
                idConfiguracionNew = em.getReference(idConfiguracionNew.getClass(), idConfiguracionNew.getIdConfiguracion());
                score.setIdConfiguracion(idConfiguracionNew);
            }
            if (idUsuarioNew != null) {
                idUsuarioNew = em.getReference(idUsuarioNew.getClass(), idUsuarioNew.getIdUsuario());
                score.setIdUsuario(idUsuarioNew);
            }
            score = em.merge(score);
            if (idConfiguracionOld != null && !idConfiguracionOld.equals(idConfiguracionNew)) {
                idConfiguracionOld.getScoreList().remove(score);
                idConfiguracionOld = em.merge(idConfiguracionOld);
            }
            if (idConfiguracionNew != null && !idConfiguracionNew.equals(idConfiguracionOld)) {
                idConfiguracionNew.getScoreList().add(score);
                idConfiguracionNew = em.merge(idConfiguracionNew);
            }
            if (idUsuarioOld != null && !idUsuarioOld.equals(idUsuarioNew)) {
                idUsuarioOld.getScoreList().remove(score);
                idUsuarioOld = em.merge(idUsuarioOld);
            }
            if (idUsuarioNew != null && !idUsuarioNew.equals(idUsuarioOld)) {
                idUsuarioNew.getScoreList().add(score);
                idUsuarioNew = em.merge(idUsuarioNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = score.getIdPuntuacion();
                if (findScore(id) == null) {
                    throw new NonexistentEntityException("The score with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Score score;
            try {
                score = em.getReference(Score.class, id);
                score.getIdPuntuacion();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The score with id " + id + " no longer exists.", enfe);
            }
            Configuracion idConfiguracion = score.getIdConfiguracion();
            if (idConfiguracion != null) {
                idConfiguracion.getScoreList().remove(score);
                idConfiguracion = em.merge(idConfiguracion);
            }
            Usuario idUsuario = score.getIdUsuario();
            if (idUsuario != null) {
                idUsuario.getScoreList().remove(score);
                idUsuario = em.merge(idUsuario);
            }
            em.remove(score);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Score> findScoreEntities() {
        return findScoreEntities(true, -1, -1);
    }

    public List<Score> findScoreEntities(int maxResults, int firstResult) {
        return findScoreEntities(false, maxResults, firstResult);
    }

    private List<Score> findScoreEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Score.class));
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

    public Score findScore(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Score.class, id);
        } finally {
            em.close();
        }
    }

    public int getScoreCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Score> rt = cq.from(Score.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
