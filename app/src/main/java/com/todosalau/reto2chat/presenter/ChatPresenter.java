package com.todosalau.reto2chat.presenter;

import com.todosalau.reto2chat.model.UserModel;

// Esta interfaz define las operaciones que puede realizar el presentador del chat
public interface ChatPresenter {
    // Método para cargar las conversaciones entre dos usuarios
    // @param user1 El primer usuario
    // @param user2 El segundo usuario
    void loadConversations(UserModel user1, UserModel user2);

    // Método para enviar un mensaje entre dos usuarios
    // @param messageText El texto del mensaje
    // @param user1 El primer usuario
    // @param user2 El segundo usuario
    void sendMessage(String messageText, UserModel user1, UserModel user2);


}
