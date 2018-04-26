package com.example.willian.movieme;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CadastrarActivity extends Activity {

    private static final String TAG = "CustomAuthActivity";
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar);

        mAuth = FirebaseAuth.getInstance();

        //login ou Cadastrar

        final EditText cadusuario = findViewById(R.id.lbl_cadusuario);
        final EditText cadsenha = findViewById(R.id.lbl_cadsenha);
        Button btn_cadastrar = findViewById(R.id.btn_cadastrar);

        //quando clicar no botão para cadastrar
        btn_cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.createUserWithEmailAndPassword(cadusuario.getText().toString(), cadsenha.getText().toString())
                        .addOnCompleteListener(CadastrarActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Cadastrar sucesso
                                    Log.d(TAG, "createUserWithEmail:success");
                                    Toast.makeText(CadastrarActivity.this, "Usuário criado.", Toast.LENGTH_SHORT).show();
                                    //se estiver tudo certo vai pra tela de login
                                    Intent intent = new Intent(CadastrarActivity.this, LogarActivity.class);
                                    finish();
                                    startActivity(intent);
                                } else {
                                    // em caso de falha, mostra um toast
                                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(CadastrarActivity.this, "Falha na autenticação.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }

}
