package com.todosalau.reto2chat;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.todosalau.reto2chat.adapter.MessageAdapter;
import com.todosalau.reto2chat.adapter.UserAdapter;
import com.todosalau.reto2chat.model.MessageModel;
import com.todosalau.reto2chat.model.UserModel;
import com.todosalau.reto2chat.presenter.ChatPresenter;
import com.todosalau.reto2chat.presenter.ChatPresenterImpl;
import com.todosalau.reto2chat.presenter.UserListPresenter;
import com.todosalau.reto2chat.presenter.UserListPresenterImpl;
import com.todosalau.reto2chat.view.ChatContract;
import com.todosalau.reto2chat.view.UserListContract;

import java.util.ArrayList;
import java.util.List;

/**
 * Esta clase representa la actividad de chat de la aplicación.
 */
public class ChatActivity extends AppCompatActivity implements UserListContract, ChatContract {
    // Variables de la interfaz de usuario
    private ListView conversationsListView;
    private EditText messageEditText;
    private Button sendButton;
    private ListView listView;
    private EditText searchEmailEditText;
    private Button searchUserButton;
    private TextView textViewMiddleTitle;
    private CardView listViewChatUsuarios;
    private LinearLayout messageInputLayout;

    // Variables de Firebase
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    // Variables de presentador
    private ChatPresenter chatPresenter;
    private UserListPresenter presenter;

    // Lista de usuarios y modelos de usuario
    private List<UserModel> usersList = new ArrayList<>();
    private UserModel user1;
    private UserModel user2;
    private UserModel currUserModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chat);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inicialización de Firebase
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        // Inicialización de modelos de usuario
        user1 = new UserModel();
        user2 = new UserModel();

        // Configuración de la interfaz de búsqueda de usuarios
        listView = findViewById(R.id.usersListView);
        searchEmailEditText = findViewById(R.id.searchEmailEditText);
        searchUserButton = findViewById(R.id.searchUserButton);

        // Configuración de la interfaz de chat
        textViewMiddleTitle = findViewById(R.id.textViewMiddleTitle);
        listViewChatUsuarios = findViewById(R.id.listViewChatUsuarios);
        messageInputLayout = findViewById(R.id.messageInputLayout);
        conversationsListView = findViewById(R.id.conversationsListView);
        messageEditText = findViewById(R.id.messageEditText);
        sendButton = findViewById(R.id.sendButton);

        // Inicialización de presentadores
        presenter = new UserListPresenterImpl(this);
        chatPresenter = new ChatPresenterImpl(this);

        // Cargar usuarios
        presenter.loadUsers();

        // Configuración del botón de búsqueda de usuarios
        searchUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchUser();
            }
        });

        // Configuración del OnClickListener para el botón de enviar mensaje
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });
    }

    /**
     * Método para mostrar la interfaz de chat.
     */
    public void showChatInterface() {
        textViewMiddleTitle.setVisibility(View.VISIBLE);
        listViewChatUsuarios.setVisibility(View.VISIBLE);
        messageInputLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void showConversations(List<MessageModel> conversations) {
        // Usar un adaptador personalizado para mostrar las conversaciones en el ListView
        MessageAdapter adapter = new MessageAdapter(this, conversations);
        conversationsListView.setAdapter(adapter);
    }

    @Override
    public void showMessageSentConfirmation() {
        // Mostrar un mensaje o realizar alguna acción al enviar el mensaje
        Toast.makeText(this, "Mensaje enviado correctamente", Toast.LENGTH_SHORT).show();
    }

    @Override
    public String getCurrenUserMail() {
        return currentUser.getEmail();
    }

    @Override
    public String getCurrentUserModelName() {
        return currUserModel.getName();
    }

    @Override
    public void displayUsers(List<UserModel> users) {
        // Guardar la lista de usuarios cargados
        usersList = users;
    }

    @Override
    public void showError(String message) {
        // Mostrar un mensaje de error
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setCurrUserModel(UserModel userModel) {
        currUserModel = userModel;
    }

    /**
     * Método para buscar usuarios por correo electrónico.
     */
    private void searchUser() {
        String emailToSearch = searchEmailEditText.getText().toString().trim();
        List<UserModel> foundUsers = new ArrayList<>();

        // Buscar usuarios cuyo correo coincida con el correo ingresado
        for (UserModel user : usersList) {
            //if (user.getEmail().equalsIgnoreCase(emailToSearch)) {
            if (user.getEmail().startsWith(emailToSearch)) {
                foundUsers.add(user);
            }
        }

        if (!foundUsers.isEmpty()) {
            // Listener para el botón de chatear con el usuario encontrado
            UserAdapter.ChatButtonClickListener listener = new UserAdapter.ChatButtonClickListener() {
                @Override
                public void onChatButtonClick(UserModel user) {
                    handleChatButtonClick(user);
                }
            };

            // Si se encuentran usuarios, mostrarlos en el ListView
            UserAdapter adapter = new UserAdapter(this, foundUsers, listener);
            listView.setAdapter(adapter);
        } else {
            // Si no se encuentran usuarios, mostrar un mensaje "no encontrado"
            Toast.makeText(this, "No encontrado", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Método para manejar el clic en el botón de chat.
     *
     * @param user El usuario con el que se desea chatear.
     */
    private void handleChatButtonClick(UserModel user) {
        if (currentUser != null) {
            // Obtener información del usuario actual
            user1.setUserId(currentUser.getUid());
            user1.setEmail(currentUser.getEmail());
            user1.setName(currentUser.getDisplayName());
            user2 = user;
            chatPresenter.loadConversations(user1, user2);
            Toast.makeText(ChatActivity.this, "Ok, allá vamos!", Toast.LENGTH_SHORT).show();
            // Mostrar la interfaz de chat
            showChatInterface();
        } else {
            // El usuario no está autenticado, realizar alguna acción como mostrar un mensaje de error
        }
    }

    /**
     * Método para enviar un mensaje.
     */
    private void sendMessage() {
        // Obtener el mensaje del campo de texto
        String message = messageEditText.getText().toString().trim();

        // Verificar si el mensaje no está vacío
        if (!message.isEmpty()) {
            // Llamar al método para enviar el mensaje
            chatPresenter.sendMessage(message, user1, user2);
            // Limpiar el campo de texto después de enviar el mensaje
            messageEditText.setText("");
        } else {
            // Mostrar un mensaje de error si el campo de texto está vacío
            Toast.makeText(ChatActivity.this, "Por favor ingresa un mensaje", Toast.LENGTH_SHORT).show();
        }
    }
}