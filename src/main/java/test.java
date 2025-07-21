import org.mindrot.jbcrypt.BCrypt;

public class test {
    public static void main(String[] args) {
        String passwordPlano = "admin123";

        // Hash copiado directamente desde tu base de datos:
        String hashBD = "$2a$12$1cuO8spjRXrwM/TJK8gOPu18w5ua/tw3Ja/05OBhQd9J5zKNUEd.O";


        boolean coincide = BCrypt.checkpw(passwordPlano, hashBD);

        System.out.println("¿Coincide la contraseña? " + coincide);
    }
}
