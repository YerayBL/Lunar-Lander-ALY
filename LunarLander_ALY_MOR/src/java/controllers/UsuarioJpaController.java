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
import model.Puntuacion;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import model.Usuario;

/**
 *
 * @author tuno
 */
public class UsuarioJpaController implements Serializable {

    public UsuarioJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Usuario usuario) throws PreexistingEntityException, Exception {
        if (usuario.getPuntuacionList() == null) {
            usuario.setPuntuacionList(new ArrayList<Puntuacion>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Puntuacion> attachedPuntuacionList = new ArrayList<Puntuacion>();
            for (Puntuacion puntuacionListPuntuacionToAttach : usuario.getPuntuacionList()) {
                puntuacionListPuntuacionToAttach = em.getReference(puntuacionListPuntuacionToAttach.getClass(), puntuacionListPuntuacionToAttach.getIdPuntuacion());
                attachedPuntuacionList.add(puntuacionListPuntuacionToAttach);
            }
            usuario.setPuntuacionList(attachedPuntuacionList);
            em.persist(usuario);
            for (Puntuacion puntuacionListPuntuacion : usuario.getPuntuacionList()) {
                Usuario oldIdUsuarioOfPuntuacionListPuntuacion = puntuacionListPuntuacion.getIdUsuario();
                puntuacionListPuntuacion.setIdUsuario(usuario);
                puntuacionListPuntuacion = em.merge(puntuacionListPuntuacion);
                if (oldIdUsuarioOfPuntuacionListPuntuacion != null) {
                    oldIdUsuarioOfPuntuacionListPuntuacion.getPuntuacionList().remove(puntuacionListPuntuacion);
                    oldIdUsuarioOfPuntuacionListPuntuacion = em.merge(oldIdUsuarioOfPuntuacionListPuntuacion);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findUsuario(usuario.getIdUsuario()) != null) {
                throw new PreexistingEntityException("Usuario " + usuario + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Usuario usuario) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usuario persistentUsuario = em.find(Usuario.class, usuario.getIdUsuario());
            List<Puntuacion> puntuacionListOld = persistentUsuario.getPuntuacionList();
            List<Puntuacion> puntuacionListNew = usuario.getPuntuacionList();
            List<String> illegalOrphanMessages = null;
            for (Puntuacion puntuacionListOldPuntuacion : puntuacionListOld) {
                if (!puntuacionListNew.contains(puntuacionListOldPuntuacion)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Puntuacion " + puntuacionListOldPuntuacion + " since its idUsuario field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Puntuacion> attachedPuntuacionListNew = new ArrayList<Puntuacion>();
            for (Puntuacion puntuacionListNewPuntuacionToAttach : puntuacionListNew) {
                puntuacionListNewPuntuacionToAttach = em.getReference(puntuacionListNewPuntuacionToAttach.getClass(), puntuacionListNewPuntuacionToAttach.getIdPuntuacion());
                attachedPuntuacionListNew.add(puntuacionListNewPuntuacionToAttach);
            }
            puntuacionListNew = attachedPuntuacionListNew;
            usuario.setPuntuacionList(puntuacionListNew);
            usuario = em.merge(usuario);
            for (Puntuacion puntuacionListNewPuntuacion : puntuacionListNew) {
                if (!puntuacionListOld.contains(puntuacionListNewPuntuacion)) {
                    Usuario oldIdUsuarioOfPuntuacionListNewPuntuacion = puntuacionListNewPuntuacion.getIdUsuario();
                    puntuacionListNewPuntuacion.setIdUsuario(usuario);
                    puntuacionListNewPuntuacion = em.merge(puntuacionListNewPuntuacion);
                    if (oldIdUsuarioOfPuntuacionListNewPuntuacion != null && !oldIdUsuarioOfPuntuacionListNewPuntuacion.equals(usuario)) {
                        oldIdUsuarioOfPuntuacionListNewPuntuacion.getPuntuacionList().remove(puntuacionListNewPuntuacion);
                        oldIdUsuarioOfPuntuacionListNewPuntuacion = em.merge(oldIdUsuarioOfPuntuacionListNewPuntuacion);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = usuario.getIdUsuario();
                if (findUsuario(id) == null) {
                    throw new NonexistentEntityException("The usuario with id " + id + " no longer exists.");
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
            Usuario usuario;
            try {
                usuario = em.getReference(Usuario.class, id);
                usuario.getIdUsuario();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The usuario with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Puntuacion> puntuacionListOrphanCheck = usuario.getPuntuacionList();
            for (Puntuacion puntuacionListOrphanCheckPuntuacion : puntuacionListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Usuario (" + usuario + ") cannot be destroyed since the Puntuacion " + puntuacionListOrphanCheckPuntuacion + " in its puntuacionList field has a non-nullable idUsuario field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(usuario);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Usuario> findUsuarioEntities() {
        return findUsuarioEntities(true, -1, -1);
    }

    public List<Usuario> findUsuarioEntities(int maxResults, int firstResult) {
        return findUsuarioEntities(false, maxResults, firstResult);
    }

    private List<Usuario> findUsuarioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Usuario.class));
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

    public Usuario findUsuario(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Usuario.class, id);
        } finally {
            em.close();
        }
    }

    public int getUsuarioCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Usuario> rt = cq.from(Usuario.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
