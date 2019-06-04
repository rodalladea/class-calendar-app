package br.edu.utfpr.alunos.rodrigodea.projetoinicial_real;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.edu.utfpr.alunos.rodrigodea.projetoinicial_real.model.Aula;
import br.edu.utfpr.alunos.rodrigodea.projetoinicial_real.persistence.Banco;

public class MainActivity extends AppCompatActivity {

    private Button buttonNewClass;
    private ListView listDataAula;
    private ConstraintLayout layout;

    private ArrayAdapter<Aula> arrayAdapterAula;
    public static List<Aula> listAula = new ArrayList<>();

    public static String DATA = "DATA";
    public static String ARQUIVO = "br.edu.utfpr.alunos.rodrigodea.projetoinicial_real.PREFERENCIA";
    public static String COR = "COR";

    public static int CORWHITE = Color.rgb(188, 255, 248);
    public static int CORDARK = Color.rgb(0, 87, 75);
    public int OPCAO = CORWHITE;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonNewClass = findViewById(R.id.buttonNewClass);
        listDataAula = findViewById(R.id.listDataAula);
        layout = findViewById(R.id.layoutPrincipal);

        listDataAula.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Banco banco = Banco.getBanco(MainActivity.this);

                pessoasComAulas(banco.aulaDao().queryForAll().get(position).getData());

            }
        });

        popularDatas();
        lerPreferencia();

    }

    @Override
    protected void onResume() {
        super.onResume();

        popularDatas();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_opcoes, menu);

        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        MenuItem item = menu.findItem(R.id.itemCheckTema);

        if (OPCAO == CORDARK) {
            item.setChecked(true);
        } else {
            item.setChecked(false);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.itemGerenciarAluno:
                Intent intentAluno = new Intent(this, ManagementAlunoActivity.class);

                startActivity(intentAluno);

                return true;

            case R.id.itemGerenciarPlano:
                Intent intentPlano = new Intent(this, ManagementPlanoActivity.class);

                startActivity(intentPlano);

                return true;

            case R.id.itemCheckTema:
                item.setChecked(!item.isChecked());
                if (item.isChecked())
                    salvarPreferencia(CORDARK);
                else
                    salvarPreferencia(CORWHITE);

                return true;

            case R.id.itemInformacao:
                Intent intentInformacao = new Intent(this, InformationActivity.class);

                startActivity(intentInformacao);

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void lerPreferencia() {
        SharedPreferences sharedPreferences = getSharedPreferences(ARQUIVO, Context.MODE_PRIVATE);

        OPCAO = sharedPreferences.getInt(COR, OPCAO);

        mudaFundo();

    }

    private void salvarPreferencia(int valor) {
        SharedPreferences sharedPreferences = getSharedPreferences(ARQUIVO, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt(COR, valor);

        editor.commit();

        OPCAO = valor;

        mudaFundo();
    }

    private void mudaFundo() {
        layout.setBackgroundColor(OPCAO);
    }

    public void pessoasComAulas(Date data) {
        Intent intent = new Intent(this, ClassInADayActivity.class);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        intent.putExtra(DATA, dateFormat.format(data));

        startActivity(intent);
    }

    public void novaAula(View view) {
        Intent intent = new Intent(this, AddClassActivity.class);

        startActivity(intent);
    }

    public void popularDatas() {
        //descobrir um sort para a lista
        //deixar com que quando tenha duas ou mais aulas no mesmo dia exiba somente uma vez a data
        Banco banco = Banco.getBanco(MainActivity.this);
        List<Aula> listAux = banco.aulaDao().queryForAll();
        listAula = new ArrayList<>();

        SimpleDateFormat dateF = new SimpleDateFormat("dd/MM/yyyy");

        for (int i = 0; i < listAux.size(); i++) {
            if (listAula.isEmpty()) {
                listAula.add(listAux.get(i));
            } else {
                int aux = 0;
                for (int j = 0; j < listAula.size(); j++) {
                    if (dateF.format(listAula.get(j).getData()).equals(dateF.format(listAux.get(i).getData()))) {
                        aux++;
                    }
                }
                if (aux == 0) {
                    listAula.add(listAux.get(i));
                }
            }
        }

        arrayAdapterAula = new ArrayAdapter<Aula>(this, android.R.layout.simple_list_item_1, listAula) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);

                TextView text1 = (TextView) view.findViewById(android.R.id.text1);

                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                text1.setText(dateFormat.format(listAula.get(position).getData()));

                return view;
            }
        };

        listDataAula.setAdapter(arrayAdapterAula);
    }


}
