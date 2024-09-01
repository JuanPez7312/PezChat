package com.todosalau.reto2chat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import com.todosalau.reto2chat.R;
import com.todosalau.reto2chat.model.UserModel;

public class UserAdapter extends ArrayAdapter<UserModel> {
    private Context context;
    private List<UserModel> users;

    // Listener personalizado para manejar el evento de clic en el botón "Chatear"
    private ChatButtonClickListener chatButtonClickListener;

    // Constructor que toma un contexto, una lista de usuarios y un listener para el botón "Chatear"
    public UserAdapter(Context context, List<UserModel> users, ChatButtonClickListener listener) {
        super(context, 0, users);
        this.context = context;
        this.users = users;
        this.chatButtonClickListener = listener;
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Obtener el usuario en la posición actual
        UserModel user = getItem(position);

        // Verificar si la vista existente se puede reutilizar, de lo contrario, inflar la nueva vista
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_user, parent, false);
        }

        // Referencias a las vistas del layout
        TextView textViewName = convertView.findViewById(R.id.textViewName);
        TextView textViewEmail = convertView.findViewById(R.id.textViewEmail);
        Button buttonChat = convertView.findViewById(R.id.buttonChat);

        // Configurar el contenido de las vistas con los datos del usuario
        if (user != null) {
            textViewName.setText(user.getName());  // Mostrar el nombre del usuario
            textViewEmail.setText(user.getEmail()); // Mostrar el correo electrónico del usuario

            // Configurar el listener del botón "Chatear"
            buttonChat.setOnClickListener(v -> {
                // Llamar al listener personalizado cuando se haga clic en el botón "Chatear"
                if (chatButtonClickListener != null) {
                    chatButtonClickListener.onChatButtonClick(user);
                }
            });
        }

        // Retornar la vista configurada
        return convertView;
    }

    // Interfaz para definir el listener personalizado para el botón "Chatear"
    public interface ChatButtonClickListener {
        void onChatButtonClick(UserModel user);
    }
}