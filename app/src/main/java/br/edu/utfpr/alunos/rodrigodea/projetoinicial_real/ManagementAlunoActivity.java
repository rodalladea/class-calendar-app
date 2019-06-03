package br.edu.utfpr.alunos.rodrigodea.projetoinicial_real;

import android.content.DialogInterface;
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

import java.util.List;

import br.edu.utfpr.alunos.rodrigodea.projetoinicial_real.model.Aluno;
import br.edu.utfpr.alunos.rodrigodea.projetoinicial_real.persistence.Banco;
import br.edu.utfpr.alunos.rodrigodea.projetoinicial_real.utils.GUIUtils;

public class ManagementAlunoActivity extends AppCompatActivity {

    private EditText editTextNome;
    private EditText editTextId;
    private EditText editTextEndereco;
    private Button buttonAddAluno;
    private ListView listViewAluno;

    private int selecionado = -1;
    private ArrayAdapter<Aluno> arrayAdapterAluno;
    private List<Aluno> listAluno;
    private Aluno aluno = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_management_aluno);
        setTitle(R.string.gerenciarAluno);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);

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

                aluno = (Aluno) parent.getItemAtPosition(position);

                editTextNome.setText(aluno.getNome());
                editTextId.setText(String.valueOf(aluno.getCpf()));
                editTextEndereco.setText(String.valueOf(aluno.getEndereco()));
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
                    String mensagem = getString(R.string.deseja_apagar)
                            + "\n" + aluno.getNome();

                    DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            switch(which){
                                case DialogInterface.BUTTON_POSITIVE:

                                    Banco banco = Banco.getBanco(ManagementAlunoActivity.this);

                                    banco.alunoDao().delete(aluno);
                                    listAluno.remove(aluno);
                                    aluno = null;

                                    arrayAdapterAluno.notifyDataSetChanged();
                                    selecionado = -1;

                                    editTextNome.setText(null);
                                    editTextId.setText(null);
                                    editTextEndereco.setText(null);

                                    buttonAddAluno.setText(R.string.adicionar);

                                    Toast.makeText(ManagementAlunoActivity.this,
                                            getString(R.string.itemExcluido), Toast.LENGTH_LONG).show();

                                    break;
                                case DialogInterface.BUTTON_NEGATIVE:

                                    break;
                            }
                        }
                    };

                    GUIUtils.confirmaAcao(this, mensagem, listener);

                }

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void popularAlunos() {
        //descobrir um sort para a lista
        Banco banco = Banco.getBanco(ManagementAlunoActivity.this);
        listAluno = banco.alunoDao().queryForAll();

        arrayAdapterAluno = new ArrayAdapter<Aluno>(this, android.R.layout.simple_list_item_1, listAluno);

        listViewAluno.setAdapter(arrayAdapterAluno);
    }

    public void addAlunoToList(View view) {

        final Banco banco = Banco.getBanco(ManagementAlunoActivity.this);

        final Aluno aluno = new Aluno();

        if (this.aluno != null) {
            aluno.setId(this.aluno.getId());
        }
        aluno.setNome(editTextNome.getText().toString());
        aluno.setCpf(editTextId.getText().toString());
        aluno.setEndereco(editTextEndereco.getText().toString());

        if (buttonAddAluno.getText().equals(getString(R.string.atualizar))) {
            String mensagem = getString(R.string.deseja_atualizar)
                    + "\n" + aluno.getNome();

            DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    switch(which){
                        case DialogInterface.BUTTON_POSITIVE:

                            banco.alunoDao().update(aluno);
                            listAluno = banco.alunoDao().queryForAll();

                            arrayAdapterAluno.notifyDataSetChanged();
                            buttonAddAluno.setText(R.string.adicionar);

                            editTextNome.setText(null);
                            editTextId.setText(null);
                            editTextEndereco.setText(null);

                            Toast.makeText(ManagementAlunoActivity.this,
                                    getString(R.string.itemAtualizado), Toast.LENGTH_LONG).show();

                            selecionado = -1;
                            ManagementAlunoActivity.this.aluno = null;

                            break;
                        case DialogInterface.BUTTON_NEGATIVE:

                            break;
                    }
                }
            };

            GUIUtils.confirmaAcao(this, mensagem, listener);


        } else {

            banco.alunoDao().insert(aluno);
            listAluno = banco.alunoDao().queryForAll();
            arrayAdapterAluno.notifyDataSetChanged();

            editTextNome.setText(null);
            editTextId.setText(null);
            editTextEndereco.setText(null);

            Toast.makeText(this, getString(R.string.itemAdicionado), Toast.LENGTH_LONG).show();
        }

    }
}
