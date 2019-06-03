package br.edu.utfpr.alunos.rodrigodea.projetoinicial_real;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import br.edu.utfpr.alunos.rodrigodea.projetoinicial_real.model.Aula;
import br.edu.utfpr.alunos.rodrigodea.projetoinicial_real.persistence.Banco;

public class ClassInADayActivity extends AppCompatActivity {

    private ListView listViewAlunoDia;
    private ArrayAdapter<Aula> arrayAdapterAlunoDia;

    private String data;
    public static String ID = "ID";
    public static String NOME = "NOME";
    public static String HORA = "HORA";
    ArrayList<Aula> listAlunosDia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_in_aday);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if (bundle != null) {
            data = bundle.getString(MainActivity.DATA);
        }

        setTitle(getString(R.string.tituloActivityAlunosNoDia) + data);

        listViewAlunoDia = findViewById(R.id.listViewAlunoDia);

        listViewAlunoDia.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intentAluno = new Intent(getApplicationContext(), DetailsClassActivity.class);

                SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");

                intentAluno.putExtra(ID, String.valueOf(listAlunosDia.get(position).getId()));
                intentAluno.putExtra(NOME, listAlunosDia.get(position).getAluno().getNome());
                intentAluno.putExtra(HORA, dateFormat.format(listAlunosDia.get(position).getData()));

                startActivity(intentAluno);
            }
        });

        popularListaAlunosDia();
    }

    @Override
    protected void onResume() {
        super.onResume();

        popularListaAlunosDia();
    }

    public void popularListaAlunosDia() {
        listAlunosDia = new ArrayList<>();

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        for (Aula aula : Banco.arrayListAula) {
            if (dateFormat.format(aula.getData()).equals(data))
                listAlunosDia.add(aula);
        }

        if (listAlunosDia.size() == 0)
            finish();

        //descobrir um sort para a lista

        arrayAdapterAlunoDia = new ArrayAdapter<Aula>(this, android.R.layout.simple_list_item_1, listAlunosDia);

        listViewAlunoDia.setAdapter(arrayAdapterAlunoDia);
    }


}