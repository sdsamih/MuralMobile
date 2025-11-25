package com.example.muralmobile.activities.signup;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.muralmobile.R;
import com.example.muralmobile.activities.LoginActivity;
import com.example.muralmobile.utils.Mask;
import com.google.android.material.textfield.TextInputEditText;

import java.util.InputMismatchException;

public class SignUpCPFActivity extends AppCompatActivity {
    private Button button_next;
    private ImageButton button_back;
    private TextInputEditText input_cpf;
    private String name;
    private String email;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up_cpf);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent receivedIntent = getIntent();
        if (receivedIntent != null) {
            email = receivedIntent.getStringExtra("EMAIL");
            name = receivedIntent.getStringExtra("NAME");
            password = receivedIntent.getStringExtra("PASSWORD");
        }

        input_cpf = findViewById(R.id.input_cpf);
        button_next = findViewById(R.id.button_createAccount);
        button_back = findViewById(R.id.button_back);

        input_cpf.addTextChangedListener(Mask.insert("###.###.###-##", input_cpf));

        button_next.setOnClickListener(v -> {
            String cpfMask = input_cpf.getText().toString().trim();
            String cpf = Mask.unmask(cpfMask);

            if(cpf.isEmpty()) {
                input_cpf.setError("CPF is required!");
                input_cpf.requestFocus();
                return;
            }

            if(cpf.length() < 11) {
                input_cpf.setError("Incomplete CPF");
                input_cpf.requestFocus();
                return;
            }

            if (!CPFValidator.isCPF(cpf)) {
                input_cpf.setError("Invalid CPF");
                input_cpf.requestFocus();
                return;
            }

            Toast.makeText(SignUpCPFActivity.this, "Account created successfully! (not yet)", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(SignUpCPFActivity.this, LoginActivity.class);
            startActivity(intent);
        });

        button_back.setOnClickListener(v -> {
            getOnBackPressedDispatcher().onBackPressed();
        });
    }

    public static class CPFValidator {

        public static boolean isCPF(String CPF) {
            CPF = CPF.replaceAll("[^0-9]", "");

            if (CPF.length() != 11 || CPF.matches("(\\d)\\1{10}")) {
                return false;
            }

            char dig10, dig11;
            int sm, i, r, num, peso;

            try {
                sm = 0;
                peso = 10;
                for (i = 0; i < 9; i++) {
                    num = (int) (CPF.charAt(i) - 48);
                    sm = sm + (num * peso);
                    peso = peso - 1;
                }

                r = 11 - (sm % 11);
                if ((r == 10) || (r == 11))
                    dig10 = '0';
                else
                    dig10 = (char) (r + 48);

                sm = 0;
                peso = 11;
                for (i = 0; i < 10; i++) {
                    num = (int) (CPF.charAt(i) - 48);
                    sm = sm + (num * peso);
                    peso = peso - 1;
                }

                r = 11 - (sm % 11);
                if ((r == 10) || (r == 11))
                    dig11 = '0';
                else
                    dig11 = (char) (r + 48);

                if ((dig10 == CPF.charAt(9)) && (dig11 == CPF.charAt(10)))
                    return(true);
                else
                    return(false);

            } catch (InputMismatchException erro) {
                return(false);
            }
        }
    }
}