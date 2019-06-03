package br.edu.utfpr.alunos.rodrigodea.projetoinicial_real;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import br.edu.utfpr.alunos.rodrigodea.projetoinicial_real.model.Aula;
import br.edu.utfpr.alunos.rodrigodea.projetoinicial_real.persistence.Banco;

public class DetailsClassActivity extends AppCompatActivity {

    private EditText enderecoAula;
    private EditText editTextTopic;
    private CheckBox checkBoxPago;
    private EditText editTextData;
    private ImageButton imageButtonData;
    private EditText editTextTime;
    private ImageButton imageButtonTime;
    private Button buttonAtualizaAula;
    private TextView textViewPrecoPagar;

    DatePickerDialog dpd;
    TimePickerDialog tpd;
    Aula aula = null;
    int aulaAntiga = 0;

    private String hora;
    private String nome;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_class);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if (bundle != null) {
            id = bundle.getString(ClassInADayActivity.ID);
            nome = bundle.getString(ClassInADayActivity.NOME);
            hora = bundle.getString(ClassInADayActivity.HORA);
        }

        setTitle(nome + " - " + hora);

        enderecoAula = findViewById(R.id.editTextEnderecoAula);
        editTextTopic = findViewById(R.id.editTextTopicAula);
        checkBoxPago = findViewById(R.id.checkBoxPagoAula);
        editTextData = findViewById(R.id.editTextDataAula);
        imageButtonData = findViewById(R.id.imageButtonDataAula);
        editTextTime = findViewById(R.id.editTextHoraAula);
        imageButtonTime = findViewById(R.id.imageButtonHoraAula);
        buttonAtualizaAula = findViewById(R.id.buttonAtualizarAula);
        textViewPrecoPagar = findViewById(R.id.textViewPrecoPagar);


        //DATEPICKER--------------------------------------------
        imageButtonData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();

                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                dpd = new DatePickerDialog(DetailsClassActivity.this, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        editTextData.setText(dayOfMonth + "/" + (month+1) + "/" + year);
                    }

                }, year, month, day);

                dpd.show();

                buttonAtualizaAula.setEnabled(true);
            }
        });
        //DATEPICKER--------------------------------------------

        //TIMEPICKER--------------------------------------------
        imageButtonTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();

                tpd = new TimePickerDialog(DetailsClassActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        editTextTime.setText(hourOfDay + ":" + minute);
                    }
                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);

                tpd.show();

                buttonAtualizaAula.setEnabled(true);
            }
        });
        //TIMEPICKER--------------------------------------------

        buttonAtualizaAula.setEnabled(false);

        popularCampos();
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
                Banco.arrayListAula.remove(aulaAntiga);
                finish();

                Toast.makeText(this, getString(R.string.itemExcluido), Toast.LENGTH_LONG).show();

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void popularCampos() {
        int i = 0;

        for (Aula a : Banco.arrayListAula) {
            if (a.getId() == Integer.parseInt(id)) {
                aula = a;
                aulaAntiga = i;
            }

            i++;
        }

        SimpleDateFormat dateFormatData = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat dateFormatHora = new SimpleDateFormat("HH:mm");


        enderecoAula.setText(aula.getAluno().getEndereco());
        editTextTopic.setText(aula.getMateria());
        checkBoxPago.setChecked(aula.isPago());
        editTextData.setText(dateFormatData.format(aula.getData()));
        editTextTime.setText(dateFormatHora.format(aula.getData()));
        textViewPrecoPagar.setText(String.valueOf(aula.getPlano().getValor()/aula.getPlano().getQuantidade()));

        enderecoAula.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus)
                    buttonAtualizaAula.setEnabled(true);
            }
        });

        editTextTopic.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus)
                    buttonAtualizaAula.setEnabled(true);
            }
        });

        checkBoxPago.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                buttonAtualizaAula.setEnabled(true);
            }
        });

        editTextData.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus)
                    buttonAtualizaAula.setEnabled(true);
            }
        });

        editTextTime.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus)
                    buttonAtualizaAula.setEnabled(true);
            }
        });

    }

    public void alterarDadosAula(View view) throws ParseException {

        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy'T'HH:mm:ss");
        Date data = format.parse(editTextData.getText().toString() + "T" + editTextTime.getText().toString() + ":00");

        aula.setMateria(editTextTopic.getText().toString());
        aula.setData(data);
        aula.getAluno().setEndereco(enderecoAula.getText().toString());
        aula.setPago(checkBoxPago.isChecked());

        Banco.arrayListAula.remove(aulaAntiga);
        Banco.arrayListAula.add(aula);

        Toast.makeText(this, getString(R.string.itemAtualizado), Toast.LENGTH_LONG).show();

        finish();
    }

    @Override
    public void onBackPressed() {

        Toast.makeText(this, getString(R.string.semMudanca), Toast.LENGTH_LONG).show();

        finish();
    }
}
