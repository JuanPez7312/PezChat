package com.todosalau.reto2chat.presenter;

import static android.content.ContentValues.TAG;

import android.util.Log;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.todosalau.reto2chat.model.MessageModel;
import com.todosalau.reto2chat.model.UserModel;
import com.todosalau.reto2chat.view.ChatContract;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

// Implementación del presentador de la vista de chat
public class ChatPresenterImpl implements ChatPresenter {
    private ChatContract view;
    private UserModel user1;
    private UserModel user2;
    private String conversationId;

    // Constructor del presentador que recibe una vista (ChatContract)
    public ChatPresenterImpl(ChatContract view) {
        this.view = view;
    }

    @Override
    public void loadConversations(UserModel user1, UserModel user2) {
        this.user1 = user1;
        this.user2 = user2;

        // Determina el ID de la conversación basado en los emails de los usuarios
        if (user1.getEmail().compareTo(user2.getEmail()) < 0) {
            conversationId = user1.getEmail() + "_" + user2.getEmail();
        } else {
            conversationId = user2.getEmail() + "_" + user1.getEmail();
        }

        // Consulta Firebase Firestore para cargar los mensajes de la conversación
        FirebaseFirestore.getInstance().collection("conversations")
                .document(conversationId)
                .collection("messages")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .addSnapshotListener((queryDocumentSnapshots, e) -> {
                    if (e != null) {
                        Log.e(TAG, "Error al cargar las conversaciones: ", e);
                        return;
                    }

                    List<MessageModel> conversationMessages = new ArrayList<>();
                    if (queryDocumentSnapshots != null) {
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            MessageModel message = document.toObject(MessageModel.class);
                            conversationMessages.add(message);
                        }
                    }

                    // Muestra los mensajes en la vista
                    if (view != null) {
                        view.showConversations(conversationMessages);
                    }
                });
    }

    @Override
    public void sendMessage(String messageText, UserModel user1, UserModel user2) {
        if (user1 != null && user2 != null) {
            // Genera el ID de la conversación
            String conversationId = generateConversationId(user1.getEmail(), user2.getEmail());

            // Referencia a la colección de mensajes en Firebase Firestore
            CollectionReference messagesRef = FirebaseFirestore.getInstance()
                    .collection("conversations")
                    .document(conversationId)
                    .collection("messages");

            // Crea un objeto MessageModel con los datos del mensaje
            MessageModel message = createMessageModel(user1, messageText);

            // Agrega el mensaje a la colección en Firebase Firestore
            messagesRef.add(message)
                    .addOnSuccessListener(documentReference -> {
                        // Notifica a la vista que el mensaje se ha enviado correctamente
                        view.showMessageSentConfirmation();
                    })
                    .addOnFailureListener(e -> {
                        Log.e(TAG, "Error sending message: " + e.getMessage());
                        // Maneja el fallo de envío del mensaje
                    });

            // Actualiza los usuarios con el ID del mensaje enviado
            updateUserWithMessageId(conversationId, message.getMessageId(), user1, user2);
            updateUserWithMessageId(conversationId, message.getMessageId(), user2, user1);
        } else {
            Log.e(TAG, "User1 or User2 is null");
        }
    }

    // Genera el ID de la conversación basado en los emails de los usuarios
    private String generateConversationId(String email1, String email2) {
        return email1.compareTo(email2) < 0 ? email1 + "_" + email2 : email2 + "_" + email1;
    }

    // Crea un objeto MessageModel con los datos del mensaje
    private MessageModel createMessageModel(UserModel user, String messageText) {
        String messageId = UUID.randomUUID().toString();
        String senderId = user.getUserId();
        String senderName = view.getCurrentUserModelName();
        String senderEmail = user.getEmail();
        Date timestamp = new Date(); // O utiliza FieldValue.serverTimestamp() si prefieres que Firestore establezca la marca de tiempo
        return new MessageModel(messageId, messageText, senderId, senderName, timestamp, senderEmail);
    }

    // Actualiza los usuarios con el ID del mensaje enviado
    private void updateUserWithMessageId(String conversationId, String messageId, UserModel user, UserModel otherUser) {
        FirebaseFirestore.getInstance().collection("usuarios")
                .whereEqualTo("email", user.getEmail())
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        UserModel userFromFirestore = documentSnapshot.toObject(UserModel.class);
                        if (userFromFirestore != null) {
                            if (userFromFirestore.getUserId() != null) {
                                updateUserData(userFromFirestore.getUserId(), conversationId, messageId);
                            } else {
                                Log.e(TAG, "userId is null for user: " + userFromFirestore.getEmail());
                            }
                        } else {
                            Log.e(TAG, "User not found in usuarios collection");
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error getting user from usuarios collection: " + e.getMessage());
                });
    }

    // Actualiza los datos del usuario con el ID del mensaje
    private void updateUserData(String userId, String conversationId, String messageId) {
        FirebaseFirestore.getInstance().collection("usuarios").document(userId)
                .update("messageIds." + conversationId, messageId)
                .addOnSuccessListener(aVoid -> Log.d(TAG, "MessageId added to user"))
                .addOnFailureListener(e -> Log.e(TAG, "Error updating document: " + e.getMessage()));
    }
}
