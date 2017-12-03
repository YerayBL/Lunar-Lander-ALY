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
        if (usuario.getScoreList() == null) {
            usuario.setScoreList(new ArrayList<Score>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Score> attachedScoreList = new ArrayList<Score>();
            for (Score scoreListScoreToAttach : usuario.getScoreList()) {
                scoreListScoreToAttach = em.getReference(scoreListScoreToAttach.getClass(), scoreListScoreToAttach.getIdPuntuacion());
                attachedScoreList.add(scoreListScoreToAttach);
            }
            usuario.setScoreList(attachedScoreList);
            em.persist(usuario);
            for (Score scoreListScore : usuario.getScoreList()) {
                Usuario oldIdUsuarioOfScoreListScore = scoreListScore.getIdUsuario();
                scoreListScore.setIdUsuario(usuario);
                scoreListScore = em.merge(scoreListScore);
                if (oldIdUsuarioOfScoreListScore != null) {
                    oldIdUsuarioOfScoreListScore.getScoreList().remove(scoreListScore);
                    oldIdUsuarioOfScoreListScore = em.merge(oldIdUsuarioOfScoreListScore);
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
            List<Score> scoreListOld = persistentUsuario.getScoreList();
            List<Score> scoreListNew = usuario.getScoreList();
            List<String> illegalOrphanMessages = null;
            for (Score scoreListOldScore : scoreListOld) {
                if (!scoreListNew.contains(scoreListOldScore)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Score " + scoreListOldScore + " since its idUsuario field is not nullable.");
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
            usuario.setScoreList(scoreListNew);
            usuario = em.merge(usuario);
            for (Score scoreListNewScore : scoreListNew) {
                if (!scoreListOld.contains(scoreListNewScore)) {
                    Usuario oldIdUsuarioOfScoreListNewScore = scoreListNewScore.getIdUsuario();
                    scoreListNewScore.setIdUsuario(usuario);
                    scoreListNewScore = em.merge(scoreListNewScore);
                    if (oldIdUsuarioOfScoreListNewScore != null && !oldIdUsuarioOfScoreListNewScore.equals(usuario)) {
                        oldIdUsuarioOfScoreListNewScore.getScoreList().remove(scoreListNewScore);
                        oldIdUsuarioOfScoreListNewScore = em.merge(oldIdUsuarioOfScoreListNewScore);
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
            List<Score> scoreListOrphanCheck = usuario.getScoreList();
            for (Score scoreListOrphanCheckScore : scoreListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Usuario (" + usuario + ") cannot be destroyed since the Score " + scoreListOrphanCheckScore + " in its scoreList field has a non-nullable idUsuario field.");
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
