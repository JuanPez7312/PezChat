package com.todosalau.reto2chat.model;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

public class RegistroModel {
    private FirebaseAuth mAuth;     // Instancia de FirebaseAuth para la autenticación de Firebase
    private FirebaseFirestore db;   // Instancia de FirebaseFirestore para la base de datos Firestore
    private DatabaseReference database;  // Referencia a la base de datos en tiempo real de Firebase

    // Constructor para inicializar las instancias de FirebaseAuth, FirebaseFirestore y DatabaseReference
    public RegistroModel() {
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        database = FirebaseDatabase.getInstance().getReference();
    }

    // Método para registrar un nuevo usuario con correo electrónico y contraseña
    public void registerUser(String email, String password, RegistroCallback callback) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            callback.onSuccess(user);
                        }
                    } else {
                        callback.onFailure(task.getException());
                    }
                });
    }

    // Método para almacenar los datos del usuario en Firestore
    public void storeUserData(FirebaseUser user, Map<String, Object> userData, RegistroCallback callback) {
        db.collection("usuarios")
                .document(user.getUid())
                .set(userData)
                .addOnSuccessListener(aVoid -> callback.onSuccess(null))
                .addOnFailureListener(callback::onFailure);
    }

    // Método para guardar los datos del usuario en la base de datos en tiempo real de Firebase
    public void guardarUsuario(UserModel user) {
        DatabaseReference usersRef = database.child("users");
        usersRef.child(user.getUserId()).setValue(user)
                .addOnSuccessListener(aVoid -> {
                    // Usuario guardado con éxito en Firebase Realtime Database
                })
                .addOnFailureListener(e -> {
                    // Manejar errores si no se pudo guardar el usuario
                });
    }

    // Interfaz de callback para manejar los resultados de los métodos asincrónicos
    public interface RegistroCallback {
        void onSuccess(Object result);  // Método llamado cuando la operación tiene éxito
        void onFailure(Exception e);    // Método llamado cuando la operación falla
    }
}
