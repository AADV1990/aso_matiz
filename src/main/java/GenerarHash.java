import org.mindrot.jbcrypt.BCrypt;

public class GenerarHash {
    public static void main(String[] args) {
        String passwordPlano = "admin123";
        String hashGenerado = BCrypt.hashpw(passwordPlano, BCrypt.gensalt(12));
        System.out.println("Hash generado: " + hashGenerado);
    }
}
