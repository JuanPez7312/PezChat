package com.todosalau.reto2chat.view;

import com.todosalau.reto2chat.model.MessageModel;

import java.util.List;

/**
 * Interfaz que define las operaciones disponibles para la vista del chat.
 */
public interface ChatContract {
    /**
     * Muestra la lista de conversaciones en la interfaz de usuario.
     * @param conversations Lista de modelos de mensajes que representan las conversaciones.
     */
    void showConversations(List<MessageModel> conversations); // Puedes cambiar el tipo de datos según lo que contenga una conversación

    /**
     * Muestra una confirmación de que el mensaje ha sido enviado correctamente.
     */
    void showMessageSentConfirmation();

    /**
     * Recupera el nombre del usuario actual
     * @return Nombre del userModel actual
     */
    String getCurrentUserModelName();
}
