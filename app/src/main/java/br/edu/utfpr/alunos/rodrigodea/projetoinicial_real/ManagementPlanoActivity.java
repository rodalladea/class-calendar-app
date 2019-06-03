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

import br.edu.utfpr.alunos.rodrigodea.projetoinicial_real.persistence.Banco;
import br.edu.utfpr.alunos.rodrigodea.projetoinicial_real.model.Plano;

public class ManagementPlanoActivity extends AppCompatActivity {

    private EditText editTextNome;
    private EditText editTextIntervalo;
    private EditText editTextQuantidade;
    private EditText editTextPreco;
    private Button buttonAddPlano;
    private ListView listViewPlano;

    private int selecionado = -1;
    private ArrayAdapter<Plano> arrayAdapterPlano;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_management_plano);
        setTitle(R.string.gerenciarPlano);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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

                editTextNome.setText(Banco.arrayListPlano.get(position).getNome());
                editTextIntervalo.setText(String.valueOf(Banco.arrayListPlano.get(position).getIntervalo()));
                editTextQuantidade.setText(String.valueOf(Banco.arrayListPlano.get(position).getQuantidade()));
                editTextPreco.setText(String.valueOf(Banco.arrayListPlano.get(position).getValor()));
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
                    Banco.arrayListPlano.remove(selecionado);
                    arrayAdapterPlano.notifyDataSetChanged();
                    selecionado = -1;

                    editTextNome.setText(null);
                    editTextIntervalo.setText(null);
                    editTextQuantidade.setText(null);
                    editTextPreco.setText(null);

                    buttonAddPlano.setText(R.string.adicionar);

                    Toast.makeText(this, getString(R.string.itemExcluido), Toast.LENGTH_LONG).show();
                }

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void popularPlanos() {
        //descobrir um sort para a lista

        arrayAdapterPlano = new ArrayAdapter<Plano>(this, android.R.layout.simple_list_item_1, Banco.arrayListPlano);

        listViewPlano.setAdapter(arrayAdapterPlano);
    }

    public void addPlanoToList(View view) {
        Plano plano = new Plano();
        String toast = getString(R.string.itemAdicionado);

        plano.setNome(editTextNome.getText().toString());
        plano.setIntervalo(Integer.parseInt(editTextIntervalo.getText().toString()));
        plano.setQuantidade(Integer.parseInt(editTextQuantidade.getText().toString()));
        plano.setValor(Double.parseDouble(editTextPreco.getText().toString()));

        if (buttonAddPlano.getText().equals(getString(R.string.atualizar))) {
            Banco.arrayListPlano.remove(selecionado);
            arrayAdapterPlano.notifyDataSetChanged();
            buttonAddPlano.setText(R.string.adicionar);
            toast = getString(R.string.itemAtualizado);
        }

        Banco.arrayListPlano.add(plano);
        arrayAdapterPlano.notifyDataSetChanged();

        editTextNome.setText(null);
        editTextIntervalo.setText(null);
        editTextQuantidade.setText(null);
        editTextPreco.setText(null);

        Toast.makeText(this, toast, Toast.LENGTH_LONG).show();
    }
}
