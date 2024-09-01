package com.todosalau.reto2chat.presenter;

import com.todosalau.reto2chat.model.UserModel;

//[Interfaz para el presentador de la lista de usuarios]
public interface UserListPresenter {
    void loadUsers(); // Método para cargar la lista de usuarios
    UserModel findUserByEmail(String email);//Buscar un usuario de la lista por Mail
}
