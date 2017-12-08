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
import model.Puntuacion;

/**
 *
 * @author tuno
 */
public class PuntuacionJpaController implements Serializable {

    public PuntuacionJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Puntuacion puntuacion) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(puntuacion);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findPuntuacion(puntuacion.getIdPuntuacion()) != null) {
                throw new PreexistingEntityException("Puntuacion " + puntuacion + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Puntuacion puntuacion) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            puntuacion = em.merge(puntuacion);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = puntuacion.getIdPuntuacion();
                if (findPuntuacion(id) == null) {
                    throw new NonexistentEntityException("The puntuacion with id " + id + " no longer exists.");
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
            Puntuacion puntuacion;
            try {
                puntuacion = em.getReference(Puntuacion.class, id);
                puntuacion.getIdPuntuacion();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The puntuacion with id " + id + " no longer exists.", enfe);
            }
            em.remove(puntuacion);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Puntuacion> findPuntuacionEntities() {
        return findPuntuacionEntities(true, -1, -1);
    }

    public List<Puntuacion> findPuntuacionEntities(int maxResults, int firstResult) {
        return findPuntuacionEntities(false, maxResults, firstResult);
    }

    private List<Puntuacion> findPuntuacionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Puntuacion.class));
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

    public Puntuacion findPuntuacion(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Puntuacion.class, id);
        } finally {
            em.close();
        }
    }

    public int getPuntuacionCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Puntuacion> rt = cq.from(Puntuacion.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
