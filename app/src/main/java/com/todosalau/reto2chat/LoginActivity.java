package com.todosalau.reto2chat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.todosalau.reto2chat.model.LoginModel;
import com.todosalau.reto2chat.presenter.LoginPresenter;
import com.todosalau.reto2chat.view.LoginContract;


/**
 * Esta clase representa la actividad de inicio de sesión de la aplicación.
 */
public class LoginActivity extends AppCompatActivity implements LoginContract.View {
    // Componentes de la interfaz de usuario
    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button btnLogin;
    private TextView registerText;
    private ImageView backgroundImageView;

    // Presentador
    private LoginPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        // Iniciar el efecto de movimiento de fondo
        backgroundImageView = findViewById(R.id.backgroundImageView);
        Animation backgroundAnimation = AnimationUtils.loadAnimation(this, R.anim.background_move);
        backgroundImageView.startAnimation(backgroundAnimation);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inicialización de componentes de la interfaz de usuario
        editTextEmail = findViewById(R.id.emailEditTextLogin);
        editTextPassword = findViewById(R.id.passwordEditTextLogin);
        btnLogin = findViewById(R.id.loginButton);
        registerText = findViewById(R.id.registerText);

        // Creación del presentador
        presenter = new LoginPresenter(this, new LoginModel());

        // Configuración del listener del botón de inicio de sesión
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.loginUser();
            }
        });

        // Configuración del listener para el texto de registro
        registerText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToRegistration();
            }
        });
    }

    @Override
    public void showToast(String message) {
        // Mostrar un mensaje de tostada
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void navigateToHome() {
        // Navegar a la actividad principal después de iniciar sesión
        Intent intent = new Intent(this, ChatActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public String getEmail() {
        // Obtener el correo electrónico ingresado por el usuario
        return editTextEmail.getText().toString().trim();
    }

    @Override
    public String getPassword() {
        // Obtener la contraseña ingresada por el usuario
        return editTextPassword.getText().toString().trim();
    }

    /**
     * Método para navegar a la actividad de registro.
     */
    private void navigateToRegistration() {
        Intent intent = new Intent(LoginActivity.this, RegistroActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (backgroundImageView != null) {
            backgroundImageView.clearAnimation();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (backgroundImageView != null) {
            Animation backgroundAnimation = AnimationUtils.loadAnimation(this, R.anim.background_move);
            backgroundImageView.startAnimation(backgroundAnimation);
        }
    }
}