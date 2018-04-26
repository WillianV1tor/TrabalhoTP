package com.example.willian.movieme;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

/* TODO
    deixar a interface mais amigável para o usuário
    colocar a activity na frente, como uma dialog (desejável)
 */

public class AddFilmeActivity extends Activity {

    private String s; //para converter de long para string
    private static DataSnapshot data;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_filme);

        // Recupera os componentes da activity //
        final EditText nome = findViewById(R.id.addFilme_nome);
        final EditText genero = findViewById(R.id.addFilme_genero);
        final EditText diretor = findViewById(R.id.addFilme_diretor);
        final EditText ano = findViewById(R.id.addFilme_ano);
        final RadioGroup radioGroup = findViewById(R.id.addFilme_radioGroup);
        Button btn = findViewById(R.id.addFilme_btnAdd);

        // Quando clicar no botão //

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(AddFilmeActivity.this, "lalal", Toast.LENGTH_SHORT).show();
                Toast.makeText(AddFilmeActivity.this, Login.getUsuario(), Toast.LENGTH_SHORT).show();
                // Verifica se os campos obrigatórios estão preenchidos //

                if (TextUtils.isEmpty(nome.getText())){
                    //Toast.makeText(AddFilmeActivity.this, AddFilmeActivity.this.getString(R.string.toastNomeObrigatorio), Toast.LENGTH_SHORT).show();
                    nome.setError(getString(R.string.toastNomeObrigatorio));
                }else if (TextUtils.isEmpty(genero.getText())){
                    //Toast.makeText(AddFilmeActivity.this, AddFilmeActivity.this.getString(R.string.toastGeneroObrigatorio), Toast.LENGTH_SHORT).show();
                    genero.setError(getString(R.string.toastGeneroObrigatorio));
                }else if (TextUtils.isEmpty(ano.getText())){
                    //Toast.makeText(AddFilmeActivity.this, AddFilmeActivity.this.getString(R.string.toastAnoObrigatorio), Toast.LENGTH_SHORT).show();
                    ano.setError(getString(R.string.toastAnoObrigatorio));

                    // Diferente para RadioGroup, não está funcionando //
                }else if (radioGroup.getCheckedRadioButtonId() == -1){ // Nenhum RadioButton selecionado
                    Toast.makeText(AddFilmeActivity.this, AddFilmeActivity.this.getString(R.string.toastFaixaObrigatoria), Toast.LENGTH_SHORT).show();
                }else {


                    // Para definir a faixa etária
                    int selecetedBtnId = radioGroup.getCheckedRadioButtonId();
                    RadioButton radioBtn = findViewById(selecetedBtnId);
                    // String que contém o texto do botão escolhido //
                    String faixaEtaria = radioBtn.getText().toString();

                    long id = 0;

                    switch (faixaEtaria) {
                        case "L":
                            id = R.drawable.livre;
                            break;
                        case "10":
                            id = R.drawable.dez;
                            break;
                        case "12":
                            id = R.drawable.doze;
                            break;
                        case "14":
                            id = R.drawable.catorze;
                            break;
                        case "16":
                            id = R.drawable.dezesseis;
                            break;
                        case "18":
                            id = R.drawable.dezoito;
                            break;
                    }
                    // photoId precisa ser final
                    final long photoId = id;

                    // Adiciona filme na lista //
                    Listagem l = new Listagem(nome.getText().toString(), genero.getText().toString(), diretor.getText().toString(), photoId, ano.getText().toString());
                    ListagemActivity.AddFilme(l);

                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference("Usuarios");


                    s = String.valueOf(Login.getQuantidadeFilmes()); //converte long para string pois o .child não aceita long

                    myRef.child(Login.getUsuario()).child(s).child("Nome").setValue(nome.getText().toString());
                    myRef.child(Login.getUsuario()).child(s).child("Diretor").setValue(diretor.getText().toString());
                    myRef.child(Login.getUsuario()).child(s).child("Ano").setValue(ano.getText().toString());
                    myRef.child(Login.getUsuario()).child(s).child("photoId").setValue(photoId);
                    myRef.child(Login.getUsuario()).child(s).child("Genero").setValue(genero.getText().toString());

                    Login.setQuantidadeFilmes(Login.getQuantidadeFilmes()+1);

                    // Volta para a activity principal //
                    Intent intent = new Intent(AddFilmeActivity.this, ListagemActivity.class);
                    finish();
                    startActivity(intent);
                }
            }
        });
    }
}
