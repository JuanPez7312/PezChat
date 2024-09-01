package com.todosalau.reto2chat.model;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginModel {
    private FirebaseAuth mAuth;

    // Constructor de la clase LoginModel
    public LoginModel() {
        // Obtener una instancia de FirebaseAuth
        mAuth = FirebaseAuth.getInstance();
    }

    // Método para iniciar sesión con correo electrónico y contraseña
    public void loginUser(String email, String password, LoginCallback callback) {
        // Iniciar sesión con el método signInWithEmailAndPassword
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Si el inicio de sesión es exitoso, obtener el usuario actual
                        FirebaseUser user = mAuth.getCurrentUser();
                        // Llamar al método onSuccess del callback
                        callback.onSuccess(user);
                    } else {
                        // Si hay un error en el inicio de sesión, llamar al método onFailure del callback
                        callback.onFailure(task.getException());
                    }
                });
    }

    // Interfaz de callback para manejar los resultados del inicio de sesión
    public interface LoginCallback {
        // Método llamado cuando el inicio de sesión es exitoso
        void onSuccess(FirebaseUser user);

        // Método llamado cuando hay un error en el inicio de sesión
        void onFailure(Exception e);
    }
}
