//package impl.utils;
//
//import org.mindrot.jbcrypt.BCrypt;
//
//public class PasswordUtils {
//
//    /**
//     * Cifra una contraseña usando BCrypt.
//     *
//     * @param password La contraseña a cifrar.
//     * @return La contraseña cifrada.
//     */
//    public static String hashPassword(String password) {
//        return BCrypt.hashpw(password, BCrypt.gensalt());
//    }
//
//    /**
//     * Verifica si una contraseña coincide con el hash.
//     *
//     * @param password La contraseña en texto plano.
//     * @param hashedPassword El hash de la contraseña almacenada.
//     * @return true si coinciden, false de lo contrario.
//     */
//    public static boolean verifyPassword(String password, String hashedPassword) {
//        return BCrypt.checkpw(password, hashedPassword);
//    }
//}
