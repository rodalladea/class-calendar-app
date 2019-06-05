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

import br.edu.utfpr.alunos.rodrigodea.projetoinicial_real.model.Plano;
import br.edu.utfpr.alunos.rodrigodea.projetoinicial_real.persistence.Banco;
import br.edu.utfpr.alunos.rodrigodea.projetoinicial_real.utils.GUIUtils;

public class ManagementPlanoActivity extends AppCompatActivity {

    private EditText editTextNome;
    private EditText editTextIntervalo;
    private EditText editTextQuantidade;
    private EditText editTextPreco;
    private Button buttonAddPlano;
    private ListView listViewPlano;

    private int selecionado = -1;
    private ArrayAdapter<Plano> arrayAdapterPlano;
    private List<Plano> listPlano;
    private Plano plano = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_management_plano);
        setTitle(R.string.gerenciarPlano);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);

        editTextNome = findViewById(R.id.editTextNomePlano);
        editTextIntervalo = findViewById(R.id.editTextIntervalo);
        editTextQuantidade = findViewById(R.id.editTextQuantidade);
        editTextPreco = findViewById(R.id.editTextPreco);
        buttonAddPlano = findViewById(R.id.buttonAddPlano);
        listViewPlano = findViewById(R.id.listViewPlano);

        listViewPlano.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selecionado = position;
                buttonAddPlano.setText(R.string.atualizar);

                plano = (Plano) parent.getItemAtPosition(position);

                editTextNome.setText(plano.getNome());
                editTextIntervalo.setText(String.valueOf(plano.getIntervalo()));
                editTextQuantidade.setText(String.valueOf(plano.getQuantidade()));
                editTextPreco.setText(String.valueOf(plano.getValor()));
            }
        });

        popularPlanos();
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
                            + "\n" + plano.getNome();

                    DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch(which){
                                case DialogInterface.BUTTON_POSITIVE:

                                    Banco banco = Banco.getBanco(ManagementPlanoActivity.this);

                                    banco.planoDao().delete(plano);
                                    listPlano.remove(plano);
                                    plano = null;

                                    arrayAdapterPlano.notifyDataSetChanged();
                                    selecionado = -1;

                                    editTextNome.setText(null);
                                    editTextIntervalo.setText(null);
                                    editTextQuantidade.setText(null);
                                    editTextPreco.setText(null);

                                    buttonAddPlano.setText(R.string.adicionar);

                                    Toast.makeText(ManagementPlanoActivity.this,
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

    private void popularPlanos() {
        //descobrir um sort para a lista
        Banco banco = Banco.getBanco(ManagementPlanoActivity.this);
        listPlano = banco.planoDao().queryForAll();

        arrayAdapterPlano = new ArrayAdapter<Plano>(this, android.R.layout.simple_list_item_1, listPlano);

        listViewPlano.setAdapter(arrayAdapterPlano);
    }

    public void addPlanoToList(View view) {

        final Banco banco = Banco.getBanco(ManagementPlanoActivity.this);

        final Plano plano = new Plano();

        if (this.plano != null) {
            plano.setId(this.plano.getId());
        }

        String nome = GUIUtils.validaCampoTexto(this, editTextNome,
                R.string.materiaVazio);
        if (nome == null)
            return;

        String intervalo = GUIUtils.validaCampoTexto(this, editTextIntervalo,
                R.string.materiaVazio);
        if (intervalo == null)
            return;

        String quantidade = GUIUtils.validaCampoTexto(this, editTextQuantidade,
                R.string.materiaVazio);
        if (quantidade == null)
            return;

        String valor = GUIUtils.validaCampoTexto(this, editTextPreco,
                R.string.materiaVazio);
        if (valor == null)
            return;

        plano.setNome(nome);
        plano.setIntervalo(Integer.parseInt(intervalo));
        plano.setQuantidade(Integer.parseInt(quantidade));
        plano.setValor(Double.parseDouble(valor));

        if (buttonAddPlano.getText().equals(getString(R.string.atualizar))) {
            String mensagem = getString(R.string.deseja_atualizar)
                    + "\n" + plano.getNome();

            DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    switch(which){
                        case DialogInterface.BUTTON_POSITIVE:

                            banco.planoDao().update(plano);
                            listPlano.remove(selecionado);
                            listPlano.add(selecionado, plano);

                            arrayAdapterPlano.notifyDataSetChanged();
                            buttonAddPlano.setText(R.string.adicionar);

                            editTextNome.setText(null);
                            editTextIntervalo.setText(null);
                            editTextQuantidade.setText(null);
                            editTextPreco.setText(null);

                            Toast.makeText(ManagementPlanoActivity.this,
                                    getString(R.string.itemAtualizado), Toast.LENGTH_LONG).show();

                            selecionado = -1;
                            ManagementPlanoActivity.this.plano = null;

                            break;
                        case DialogInterface.BUTTON_NEGATIVE:

                            break;
                    }
                }
            };

            GUIUtils.confirmaAcao(this, mensagem, listener);


        } else {

            banco.planoDao().insert(plano);
            listPlano.add(plano);
            arrayAdapterPlano.notifyDataSetChanged();

            editTextNome.setText(null);
            editTextIntervalo.setText(null);
            editTextQuantidade.setText(null);
            editTextPreco.setText(null);

            Toast.makeText(ManagementPlanoActivity.this,
                    getString(R.string.itemAdicionado), Toast.LENGTH_LONG).show();
        }

    }
}
