package com.ad.base.ejb;
import com.ad.base.dto.UsuarioDTO;
import com.ad.excepciones.ContrasenaIncorrectaException;
import com.ad.excepciones.UsuarioNoEncontradoException;
import com.ad.base.modelo.Usuario;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import org.mindrot.jbcrypt.BCrypt;
import com.ad.base.dao.UsuarioDAO;

@Stateless
public class LoginService {

    @Inject
    private UsuarioDAO usuarioDAO;

    public UsuarioDTO autenticar(String username, String password) {
        Usuario usuarioEntity = usuarioDAO.findByUsername(username);

        if (usuarioEntity == null) {
            throw new UsuarioNoEncontradoException("El usuario no existe");
        }

        if (!BCrypt.checkpw(password, usuarioEntity.getPassword())) {
            throw new ContrasenaIncorrectaException("La contraseña es incorrecta");
        }

        String nombre = usuarioEntity.getPersona().getNombres();
        String apellido = usuarioEntity.getPersona().getApellidos();
        return new UsuarioDTO(usuarioEntity.getUsername(), nombre, apellido);
    }
}
