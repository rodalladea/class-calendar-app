package br.edu.utfpr.alunos.rodrigodea.projetoinicial_real;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import br.edu.utfpr.alunos.rodrigodea.projetoinicial_real.model.Aluno;
import br.edu.utfpr.alunos.rodrigodea.projetoinicial_real.model.Aula;
import br.edu.utfpr.alunos.rodrigodea.projetoinicial_real.persistence.Banco;
import br.edu.utfpr.alunos.rodrigodea.projetoinicial_real.utils.DataUtils;
import br.edu.utfpr.alunos.rodrigodea.projetoinicial_real.utils.GUIUtils;

public class ClassInADayActivity extends AppCompatActivity {

    private ListView listViewAlunoDia;
    private ArrayAdapter<Aula> arrayAdapterAlunoDia;
    private Aula aula;

    private String data;
    public static String ID = "ID";
    public static String NOME = "NOME";
    public static String HORA = "HORA";

    private static List<Aula> listAlunosDia = new ArrayList<>();
    private static List<Aluno> listAlunoForClass = new ArrayList<>();

    private ActionMode actionMode;
    private int posicao = -1;
    private View viewSelecionada;

    private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.class_opcoes, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem menuItem) {

            switch (menuItem.getItemId()) {
                case R.id.classOpcoesAtualizar:
                    atualizar(posicao);
                    return true;

                case R.id.classOpcoesExcluir:
                    excluir(aula);
                    return true;

                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            if (viewSelecionada != null) {
                viewSelecionada.setBackgroundColor(Color.TRANSPARENT);
            }

            actionMode = null;
            viewSelecionada = null;

            listViewAlunoDia.setEnabled(true);
        }
    };

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
                atualizar(position);
            }
        });

        listViewAlunoDia.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        listViewAlunoDia.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (actionMode != null) {
                    return false;
                }

                posicao = position;
                aula = (Aula) parent.getItemAtPosition(position);

                view.setBackgroundColor(Color.LTGRAY);

                viewSelecionada = view;

                listViewAlunoDia.setEnabled(false);

                actionMode = startSupportActionMode(mActionModeCallback);

                return true;
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
        listAlunoForClass = new ArrayList<>();

        Banco banco = Banco.getBanco(ClassInADayActivity.this);


        for (Aula aula : banco.aulaDao().queryForAll()) {
            if (DataUtils.formatDateToString(aula.getData()).equals(data)) {
                listAlunosDia.add(aula);
                listAlunoForClass.add(banco.alunoDao().queryForId(aula.getAluno()));
            }
        }

        if (listAlunosDia.size() == 0)
            finish();

        //descobrir um sort para a lista

        arrayAdapterAlunoDia = new ArrayAdapter<Aula>(this, android.R.layout.simple_list_item_1, listAlunosDia) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);

                TextView text1 = (TextView) view.findViewById(android.R.id.text1);

                text1.setText(listAlunoForClass.get(position).getNome() + " - " + DataUtils.formatDateToString(listAlunosDia.get(position).getData()));

                return view;
            }
        };

        listViewAlunoDia.setAdapter(arrayAdapterAlunoDia);
    }


    public void atualizar(int position) {
        Banco banco = Banco.getBanco(ClassInADayActivity.this);

        Intent intentAluno = new Intent(getApplicationContext(), DetailsClassActivity.class);

        intentAluno.putExtra(ID, String.valueOf(listAlunosDia.get(position).getId()));
        intentAluno.putExtra(NOME, banco.alunoDao().queryForId(listAlunosDia.get(position).getAluno()).getNome());
        intentAluno.putExtra(HORA, DataUtils.formatDateToHour(listAlunosDia.get(position).getData()));

        startActivity(intentAluno);
    }

    public void excluir(final Aula aula) {

        final Banco banco = Banco.getBanco(ClassInADayActivity.this);

        String mensagem = getString(R.string.deseja_apagar)
                + "\n" + banco.alunoDao().queryForId(aula.getAluno()) + " - "
                + DataUtils.formatDateToString(aula.getData());

        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                switch(which){
                    case DialogInterface.BUTTON_POSITIVE:

                        banco.aulaDao().delete(aula);
                        listAlunosDia.remove(aula);

                        arrayAdapterAlunoDia.notifyDataSetChanged();

                        Toast.makeText(ClassInADayActivity.this,
                                getString(R.string.itemExcluido), Toast.LENGTH_LONG).show();

                        finish();

                        break;
                    case DialogInterface.BUTTON_NEGATIVE:

                        break;
                }
            }
        };

        GUIUtils.confirmaAcao(this, mensagem, listener);
    }
}
