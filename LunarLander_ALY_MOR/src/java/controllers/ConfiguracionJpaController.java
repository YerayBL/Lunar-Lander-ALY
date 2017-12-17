/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import controllers.exceptions.NonexistentEntityException;
import controllers.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import model.Puntuacion;
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
        if (configuracion.getPuntuacionList() == null) {
            configuracion.setPuntuacionList(new ArrayList<Puntuacion>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Puntuacion> attachedPuntuacionList = new ArrayList<Puntuacion>();
            for (Puntuacion puntuacionListPuntuacionToAttach : configuracion.getPuntuacionList()) {
                puntuacionListPuntuacionToAttach = em.getReference(puntuacionListPuntuacionToAttach.getClass(), puntuacionListPuntuacionToAttach.getIdPuntuacion());
                attachedPuntuacionList.add(puntuacionListPuntuacionToAttach);
            }
            configuracion.setPuntuacionList(attachedPuntuacionList);
            em.persist(configuracion);
            for (Puntuacion puntuacionListPuntuacion : configuracion.getPuntuacionList()) {
                Configuracion oldIdConfiguracionOfPuntuacionListPuntuacion = puntuacionListPuntuacion.getIdConfiguracion();
                puntuacionListPuntuacion.setIdConfiguracion(configuracion);
                puntuacionListPuntuacion = em.merge(puntuacionListPuntuacion);
                if (oldIdConfiguracionOfPuntuacionListPuntuacion != null) {
                    oldIdConfiguracionOfPuntuacionListPuntuacion.getPuntuacionList().remove(puntuacionListPuntuacion);
                    oldIdConfiguracionOfPuntuacionListPuntuacion = em.merge(oldIdConfiguracionOfPuntuacionListPuntuacion);
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

    public void edit(Configuracion configuracion) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Configuracion persistentConfiguracion = em.find(Configuracion.class, configuracion.getIdConfiguracion());
            List<Puntuacion> puntuacionListOld = persistentConfiguracion.getPuntuacionList();
            List<Puntuacion> puntuacionListNew = configuracion.getPuntuacionList();
            List<Puntuacion> attachedPuntuacionListNew = new ArrayList<Puntuacion>();
            for (Puntuacion puntuacionListNewPuntuacionToAttach : puntuacionListNew) {
                puntuacionListNewPuntuacionToAttach = em.getReference(puntuacionListNewPuntuacionToAttach.getClass(), puntuacionListNewPuntuacionToAttach.getIdPuntuacion());
                attachedPuntuacionListNew.add(puntuacionListNewPuntuacionToAttach);
            }
            puntuacionListNew = attachedPuntuacionListNew;
            configuracion.setPuntuacionList(puntuacionListNew);
            configuracion = em.merge(configuracion);
            for (Puntuacion puntuacionListOldPuntuacion : puntuacionListOld) {
                if (!puntuacionListNew.contains(puntuacionListOldPuntuacion)) {
                    puntuacionListOldPuntuacion.setIdConfiguracion(null);
                    puntuacionListOldPuntuacion = em.merge(puntuacionListOldPuntuacion);
                }
            }
            for (Puntuacion puntuacionListNewPuntuacion : puntuacionListNew) {
                if (!puntuacionListOld.contains(puntuacionListNewPuntuacion)) {
                    Configuracion oldIdConfiguracionOfPuntuacionListNewPuntuacion = puntuacionListNewPuntuacion.getIdConfiguracion();
                    puntuacionListNewPuntuacion.setIdConfiguracion(configuracion);
                    puntuacionListNewPuntuacion = em.merge(puntuacionListNewPuntuacion);
                    if (oldIdConfiguracionOfPuntuacionListNewPuntuacion != null && !oldIdConfiguracionOfPuntuacionListNewPuntuacion.equals(configuracion)) {
                        oldIdConfiguracionOfPuntuacionListNewPuntuacion.getPuntuacionList().remove(puntuacionListNewPuntuacion);
                        oldIdConfiguracionOfPuntuacionListNewPuntuacion = em.merge(oldIdConfiguracionOfPuntuacionListNewPuntuacion);
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

    public void destroy(String id) throws NonexistentEntityException {
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
            List<Puntuacion> puntuacionList = configuracion.getPuntuacionList();
            for (Puntuacion puntuacionListPuntuacion : puntuacionList) {
                puntuacionListPuntuacion.setIdConfiguracion(null);
                puntuacionListPuntuacion = em.merge(puntuacionListPuntuacion);
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
