package com.todosalau.reto2chat.presenter;

import com.google.firebase.auth.FirebaseUser;
import com.todosalau.reto2chat.model.LoginModel;
import com.todosalau.reto2chat.view.LoginContract;

// Presentador para la funcionalidad de inicio de sesión
public class LoginPresenter {
    private LoginContract.View view; // Vista asociada al presentador
    private LoginModel model; // Modelo asociado al presentador

    // Constructor que recibe la vista y el modelo
    public LoginPresenter(LoginContract.View view, LoginModel model) {
        this.view = view;
        this.model = model;
    }

    // Método para iniciar sesión
    public void loginUser() {
        // Obtener el email y la contraseña ingresados por el usuario desde la vista
        String email = view.getEmail();
        String password = view.getPassword();

        // Validar que el email y la contraseña no estén vacíos
        if (email.isEmpty() || password.isEmpty()) {
            view.showToast("Por favor, completa todos los campos");
            return;
        }

        // Llamar al método del modelo para iniciar sesión, pasando un callback para manejar el resultado
        model.loginUser(email, password, new LoginModel.LoginCallback() {
            @Override
            // Método llamado en caso de éxito al iniciar sesión
            public void onSuccess(FirebaseUser user) {
                view.showToast("Inicio de sesión exitoso"); // Mostrar mensaje de éxito en la vista
                view.navigateToHome(); // Navegar a la pantalla de inicio en la vista
            }

            @Override
            // Método llamado en caso de fallo al iniciar sesión
            public void onFailure(Exception e) {
                view.showToast("Error de inicio de sesión: " + e.getMessage()); // Mostrar mensaje de error en la vista
            }
        });
    }
}
