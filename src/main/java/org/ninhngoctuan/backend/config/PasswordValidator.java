package org.ninhngoctuan.backend.config;

public class PasswordValidator {

    public static boolean isValidPassword(String password) {
        // Kiểm tra mật khẩu chứa ít nhất một chữ hoa, một chữ thường và một chữ số
        String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+$";
        return password.matches(regex);
    }

    public static void main(String[] args) {
        String password1 = "Password123";
        String password2 = "password";
        String password3 = "PASSWORD123";

        System.out.println("Password1 is valid: " + isValidPassword(password1)); // true
        System.out.println("Password2 is valid: " + isValidPassword(password2)); // false
        System.out.println("Password3 is valid: " + isValidPassword(password3)); // false
    }
}
