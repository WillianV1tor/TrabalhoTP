package com.example.willian.movieme;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ShareActionProvider;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ListagemActivity extends Activity {
    private static List<Listagem> listagens = new ArrayList<>(); // entrada de dados (implementar banco de dados)

    private DatabaseReference mDatabase;
    //private String s; //para converter de long para string
    private String nome;
    private String genero;
    private String diretor;
    private Integer photoId;
    private String ano;
    private long quantidadeFilhos;
    private static DataSnapshot data;
    private static boolean primeira_vez = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listagem);

        ListView listaDeFilmes = (ListView) findViewById(R.id.lista);
        final ListagemAdapter adapter = new ListagemAdapter(listagens, this);

        listaDeFilmes.setAdapter(adapter);

        // Adiciona filmes anteriores //

        mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("Usuarios").child(Login.getUsuario()).getChildrenCount() > 0 && primeira_vez) {// se o usuario tiver um filme registrado no banco de dados, vai recuperar os dados
                    for (long i = 0; i < dataSnapshot.child("Usuarios").child(Login.getUsuario()).getChildrenCount(); i++) {
                        if(dataSnapshot.child("Usuarios").child(Login.getUsuario()).child(String.valueOf(i)).exists()) {
                            nome = (String) dataSnapshot.child("Usuarios").child(Login.getUsuario()).child(String.valueOf(i)).child("Nome").getValue();
                            genero = (String) dataSnapshot.child("Usuarios").child(Login.getUsuario()).child(String.valueOf(i)).child("Genero").getValue();
                            diretor = (String) dataSnapshot.child("Usuarios").child(Login.getUsuario()).child(String.valueOf(i)).child("Diretor").getValue();
                            ano = (String) dataSnapshot.child("Usuarios").child(Login.getUsuario()).child(String.valueOf(i)).child("Ano").getValue();
                            photoId = (int) (long) dataSnapshot.child("Usuarios").child(Login.getUsuario()).child(String.valueOf(i)).child("photoId").getValue();

                            Listagem l = new Listagem(nome, genero, diretor, photoId, ano);
                            ListagemActivity.AddFilme(l);
                        }
                    }
                    // Atualiza a página //

                    Intent intent = new Intent(ListagemActivity.this, ListagemActivity.class);
                    finish();
                    startActivity(intent);

                }
                data = dataSnapshot;
                primeira_vez = false;
                    Login.setQuantidadeFilmes(dataSnapshot.child("Usuarios").child(Login.getUsuario()).getChildrenCount());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("TAG", databaseError.getMessage());
            }
        });


        // Código para remover e compartilhar filme com swipe //
        SwipeDismissListViewTouchListener touchListener =
                new SwipeDismissListViewTouchListener(
                        listaDeFilmes,
                        new SwipeDismissListViewTouchListener.DismissCallbacks() {
                            @Override
                            public boolean canDismiss(int position) {
                                return true;
                            }
                            // Se for para a direita deleta //
                            @Override
                            public void onDismissRight(ListView listView, int[] reverseSortedPositions) {
                                for (final int position : reverseSortedPositions) {

                                    //Criar Dialog quando o usuário usar o swipe
                                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(ListagemActivity.this);

                                    alertBuilder.setIcon(R.drawable.ic_launcher_foreground);
                                    alertBuilder.setTitle(getString(R.string.dialogTitle));
                                    alertBuilder.setMessage(getString(R.string.dialogMessage));

                                    alertBuilder.setPositiveButton(getString(R.string.dialogPositive), new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            listagens.remove(position);
                                            adapter.notifyDataSetChanged();
                                            //deletar na db
                                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                                            DatabaseReference myRef = database.getReference("Usuarios");

                                            //pega o id do ultimo filme da db
                                            quantidadeFilhos = data.child("Usuarios").child(Login.getUsuario()).getChildrenCount() - 1;

                                            if (quantidadeFilhos > 0) {

                                                //passa os dados do último filme para serem armazenados em variáveis
                                                nome = (String) data.child("Usuarios").child(Login.getUsuario()).child(String.valueOf(quantidadeFilhos)).child("Nome").getValue();
                                                genero = (String) data.child("Usuarios").child(Login.getUsuario()).child(String.valueOf(quantidadeFilhos)).child("Genero").getValue();
                                                diretor = (String) data.child("Usuarios").child(Login.getUsuario()).child(String.valueOf(quantidadeFilhos)).child("Diretor").getValue();
                                                ano = (String) data.child("Usuarios").child(Login.getUsuario()).child(String.valueOf(quantidadeFilhos)).child("Ano").getValue();
                                                photoId = (int) (long) data.child("Usuarios").child(Login.getUsuario()).child(String.valueOf(quantidadeFilhos)).child("photoId").getValue();

                                                myRef.child(Login.getUsuario()).child(String.valueOf(quantidadeFilhos)).setValue(null);//deleta o último filme

                                                //adiciona o ultimo filme no lugar do filme deletado
                                                myRef.child(Login.getUsuario()).child(String.valueOf(position)).child("Nome").setValue(nome);
                                                myRef.child(Login.getUsuario()).child(String.valueOf(position)).child("Diretor").setValue(diretor);
                                                myRef.child(Login.getUsuario()).child(String.valueOf(position)).child("Ano").setValue(ano);
                                                myRef.child(Login.getUsuario()).child(String.valueOf(position)).child("photoId").setValue(photoId);
                                                myRef.child(Login.getUsuario()).child(String.valueOf(position)).child("Genero").setValue(genero);

                                            }
                                            else{
                                                myRef.child(Login.getUsuario()).child(String.valueOf(position)).setValue(null);//deleta o filme que foi selecionado pelo usuário
                                            }

                                            //diminui em 1 a quantidade de filmes
                                            Login.setQuantidadeFilmes(Login.getQuantidadeFilmes() - 1);


                                            Intent intent = new Intent(ListagemActivity.this, ListagemActivity.class);
                                            finish();
                                            startActivity(intent);

                                            Toast.makeText(getBaseContext(), getString(R.string.toastFilmeRemovido), Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                    alertBuilder.setNegativeButton(getString(R.string.dialogNegative), new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {

                                        }
                                    });

                                    AlertDialog dialog = alertBuilder.create();
                                    dialog.show();

                                }
                            }
                            // Se for para a esquerda compartilha //
                            @Override
                            public void onDismissLeft(ListView listView, int[] reverseSortedPositions){
                                for (final int position : reverseSortedPositions) {
                                    Listagem filme = listagens.get(position);
                                    Intent intent = new Intent(Intent.ACTION_SEND);
                                    intent.setType("text/*");
                                    intent.putExtra(Intent.EXTRA_SUBJECT, filme.getNome());
                                    intent.putExtra(Intent.EXTRA_TEXT, filme.getAno() + "\n" + filme.getDiretor() + "\n" + filme.getGenero());
                                    startActivity(Intent.createChooser(intent, "Share via"));
                                }
                            }
                        });
        listaDeFilmes.setOnTouchListener(touchListener);

        // Quando pressionar o botão  de adicionar //

        Button btnAdd = findViewById(R.id.listagem_btnAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListagemActivity.this, AddFilmeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK); // Limpa activity
                finish();
                startActivity(intent);
            }
        });

    }

    public List<Listagem> EntradaTeste() {

        List<Listagem> listagens = new ArrayList<>();

        Listagem filme1 = new Listagem("Jurassic Park", "Ficção", "Steven Spielberg", 10, "1993");

        Listagem filme2 = new Listagem("Munique", "Drama", "Steven Spielberg", 16, "2006");

        Listagem filme3 = new Listagem("O Terminal", "Comédia, Drama", "Steven Spielberg", 12, "2004");

        Listagem filme4 = new Listagem("Jurassic Park", "Ficção", "Steven Spielberg", 10, "1993");

        Listagem filme5 = new Listagem("Munique", "Drama", "Steven Spielberg", 16, "2006");

        Listagem filme6 = new Listagem("O Terminal", "Comédia, Drama", "Steven Spielberg", 12, "2004");

        listagens.add(filme1);
        listagens.add(filme2);
        listagens.add(filme3);
        listagens.add(filme4);
        listagens.add(filme5);
        listagens.add(filme6);

        return listagens;
    }

    public static void AddFilme(Listagem l) {
        listagens.add(l);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        // Share Provider //
        MenuItem shareItem = menu.findItem(R.id.action_share);
        ShareActionProvider shareProvider = (ShareActionProvider) shareItem.getActionProvider();
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/*");
        intent.putExtra(Intent.EXTRA_TEXT, "Texto para Compartilhar");

        // exibe a intent
        shareProvider.setShareIntent(intent);

        return true;
    }
}