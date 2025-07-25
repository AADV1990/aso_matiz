package com.ad.base.ejb;

import com.ad.base.dao.UsuarioDAO;
import com.ad.base.modelo.Usuario;
import com.ad.base.modelo.RolUsuario;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

import java.io.Serializable;
import java.util.List;

@Stateless
public class UsuarioService implements Serializable {

    @Inject
    private UsuarioDAO usuarioDAO;

    // Obtener lista de usuarios
    public List<Usuario> listarUsuarios() {
        return usuarioDAO.listarTodos();
    }

    // Obtener lista de roles
    public List<RolUsuario> listarRoles() {
        return usuarioDAO.listarRoles();
    }

    // Guardar nuevo usuario
    public void insertar(Usuario usuario) {
        usuarioDAO.insertar(usuario);
    }

    // Actualizar usuario existente
    public void actualizar(Usuario usuario) {
        usuarioDAO.actualizar(usuario);
    }

    // Buscar por nombre de usuario (útil para login u otras validaciones)
    public Usuario buscarPorUsername(String username) {
        return usuarioDAO.findByUsername(username);
    }

    // Eliminar usuario existente
    public void eliminar(Usuario usuario) {
        usuarioDAO.eliminar(usuario);
    }

    public Usuario buscarPorId(Long id) {
        return usuarioDAO.buscarPorId(id);
    }


}
