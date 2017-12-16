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
import model.Puntuacion;
import model.Usuario;

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
            Configuracion idConfiguracion = puntuacion.getIdConfiguracion();
            if (idConfiguracion != null) {
                idConfiguracion = em.getReference(idConfiguracion.getClass(), idConfiguracion.getIdConfiguracion());
                puntuacion.setIdConfiguracion(idConfiguracion);
            }
            Usuario idUsuario = puntuacion.getIdUsuario();
            if (idUsuario != null) {
                idUsuario = em.getReference(idUsuario.getClass(), idUsuario.getIdUsuario());
                puntuacion.setIdUsuario(idUsuario);
            }
            em.persist(puntuacion);
            if (idConfiguracion != null) {
                idConfiguracion.getPuntuacionList().add(puntuacion);
                idConfiguracion = em.merge(idConfiguracion);
            }
            if (idUsuario != null) {
                idUsuario.getPuntuacionList().add(puntuacion);
                idUsuario = em.merge(idUsuario);
            }
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
            Puntuacion persistentPuntuacion = em.find(Puntuacion.class, puntuacion.getIdPuntuacion());
            Configuracion idConfiguracionOld = persistentPuntuacion.getIdConfiguracion();
            Configuracion idConfiguracionNew = puntuacion.getIdConfiguracion();
            Usuario idUsuarioOld = persistentPuntuacion.getIdUsuario();
            Usuario idUsuarioNew = puntuacion.getIdUsuario();
            if (idConfiguracionNew != null) {
                idConfiguracionNew = em.getReference(idConfiguracionNew.getClass(), idConfiguracionNew.getIdConfiguracion());
                puntuacion.setIdConfiguracion(idConfiguracionNew);
            }
            if (idUsuarioNew != null) {
                idUsuarioNew = em.getReference(idUsuarioNew.getClass(), idUsuarioNew.getIdUsuario());
                puntuacion.setIdUsuario(idUsuarioNew);
            }
            puntuacion = em.merge(puntuacion);
            if (idConfiguracionOld != null && !idConfiguracionOld.equals(idConfiguracionNew)) {
                idConfiguracionOld.getPuntuacionList().remove(puntuacion);
                idConfiguracionOld = em.merge(idConfiguracionOld);
            }
            if (idConfiguracionNew != null && !idConfiguracionNew.equals(idConfiguracionOld)) {
                idConfiguracionNew.getPuntuacionList().add(puntuacion);
                idConfiguracionNew = em.merge(idConfiguracionNew);
            }
            if (idUsuarioOld != null && !idUsuarioOld.equals(idUsuarioNew)) {
                idUsuarioOld.getPuntuacionList().remove(puntuacion);
                idUsuarioOld = em.merge(idUsuarioOld);
            }
            if (idUsuarioNew != null && !idUsuarioNew.equals(idUsuarioOld)) {
                idUsuarioNew.getPuntuacionList().add(puntuacion);
                idUsuarioNew = em.merge(idUsuarioNew);
            }
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
            Configuracion idConfiguracion = puntuacion.getIdConfiguracion();
            if (idConfiguracion != null) {
                idConfiguracion.getPuntuacionList().remove(puntuacion);
                idConfiguracion = em.merge(idConfiguracion);
            }
            Usuario idUsuario = puntuacion.getIdUsuario();
            if (idUsuario != null) {
                idUsuario.getPuntuacionList().remove(puntuacion);
                idUsuario = em.merge(idUsuario);
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
