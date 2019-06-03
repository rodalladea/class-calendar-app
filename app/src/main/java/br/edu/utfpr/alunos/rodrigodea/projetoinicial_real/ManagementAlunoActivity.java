package br.edu.utfpr.alunos.rodrigodea.projetoinicial_real;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import br.edu.utfpr.alunos.rodrigodea.projetoinicial_real.model.Aluno;
import br.edu.utfpr.alunos.rodrigodea.projetoinicial_real.persistence.Banco;

public class ManagementAlunoActivity extends AppCompatActivity {

    private EditText editTextNome;
    private EditText editTextId;
    private EditText editTextEndereco;
    private Button buttonAddAluno;
    private ListView listViewAluno;

    private int selecionado = -1;
    private ArrayAdapter<Aluno> arrayAdapterAluno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_management_aluno);
        setTitle(R.string.gerenciarAluno);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        editTextNome = findViewById(R.id.editTextNomeAluno);
        editTextId = findViewById(R.id.editTextId);
        editTextEndereco = findViewById(R.id.editTextEndereco);
        buttonAddAluno = findViewById(R.id.buttonAddAluno);
        listViewAluno = findViewById(R.id.listViewAluno);


        listViewAluno.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selecionado = position;
                buttonAddAluno.setText(R.string.atualizar);

                editTextNome.setText(Banco.arrayListAluno.get(position).getNome());
                editTextId.setText(String.valueOf(Banco.arrayListAluno.get(position).getCpf()));
                editTextEndereco.setText(String.valueOf(Banco.arrayListAluno.get(position).getEndereco()));
            }
        });

        popularAlunos();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.management_opcoes, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.itemExcluiManagement:

                if (selecionado != -1) {
                    Banco.arrayListAluno.remove(selecionado);
                    arrayAdapterAluno.notifyDataSetChanged();
                    selecionado = -1;

                    editTextNome.setText(null);
                    editTextId.setText(null);
                    editTextEndereco.setText(null);

                    buttonAddAluno.setText(R.string.adicionar);

                    Toast.makeText(this, getString(R.string.itemExcluido), Toast.LENGTH_LONG).show();
                }

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void popularAlunos() {
        //descobrir um sort para a lista

        arrayAdapterAluno = new ArrayAdapter<Aluno>(this, android.R.layout.simple_list_item_1, Banco.arrayListAluno);

        listViewAluno.setAdapter(arrayAdapterAluno);
    }

    public void addAlunoToList(View view) {
        Aluno aluno = new Aluno();
        String toast = getString(R.string.itemAdicionado);

        aluno.setNome(editTextNome.getText().toString());
        aluno.setCpf(editTextId.getText().toString());
        aluno.setEndereco(editTextEndereco.getText().toString());

        if (buttonAddAluno.getText().equals(getString(R.string.atualizar))) {
            Banco.arrayListAluno.remove(selecionado);
            arrayAdapterAluno.notifyDataSetChanged();
            buttonAddAluno.setText(R.string.adicionar);
            toast = getString(R.string.itemAtualizado);
        }

        Banco.arrayListAluno.add(aluno);
        arrayAdapterAluno.notifyDataSetChanged();

        editTextNome.setText(null);
        editTextId.setText(null);
        editTextEndereco.setText(null);

        Toast.makeText(this, toast, Toast.LENGTH_LONG).show();

    }
}
