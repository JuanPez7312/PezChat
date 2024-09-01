package com.todosalau.reto2chat.view;

/**
 * Interfaz que define las operaciones disponibles para la vista de inicio de sesión.
 */
public interface LoginContract {
    /**
     * Interfaz que define las operaciones disponibles para la vista de inicio de sesión.
     */
    interface View {
        /**
         * Muestra un mensaje de notificación en la interfaz de usuario.
         *
         * @param message Mensaje a mostrar.
         */
        void showToast(String message);

        /**
         * Navega a la pantalla principal de la aplicación.
         */
        void navigateToHome();

        /**
         * Obtiene el correo electrónico ingresado por el usuario.
         *
         * @return Correo electrónico ingresado.
         */
        String getEmail();

        /**
         * Obtiene la contraseña ingresada por el usuario.
         *
         * @return Contraseña ingresada.
         */
        String getPassword();
    }
}
