package com.ad.base.dao;

import com.ad.base.modelo.RolUsuario;
import com.ad.base.modelo.Usuario;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.List;



@Stateless
public class UsuarioDAO {

    @PersistenceContext
    private EntityManager em;

    public Usuario findByUsername(String username) {
        try {
            return em.createQuery("SELECT u FROM Usuario u WHERE u.username = :username", Usuario.class)
                    .setParameter("username", username)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    public void insertar(Usuario usuario) {
        em.persist(usuario);
    }

    public void actualizar(Usuario usuario) {
        em.merge(usuario);
    }

    public void eliminar(Usuario usuario) {
        try {
            Usuario manejado = em.merge(usuario);
            em.remove(manejado);
        } catch (Exception e) {
            // Log y manejo de errores
            System.err.println("Error eliminando usuario: " + e.getMessage());
            throw e;
        }
    }



    public List<Usuario> listarTodos() {
        return em.createQuery(
                        "SELECT u FROM Usuario u LEFT JOIN FETCH u.rol LEFT JOIN FETCH u.persona",
                        Usuario.class)
                .getResultList();
    }


    public List<RolUsuario> listarRoles() {
        return em.createQuery("SELECT r FROM RolUsuario r", RolUsuario.class)
                .getResultList();
    }

    public Usuario buscarPorId(Long id) {
        return em.find(Usuario.class, id);
    }

}
