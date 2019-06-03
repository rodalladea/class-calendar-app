package br.edu.utfpr.alunos.rodrigodea.projetoinicial_real;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TimePicker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import br.edu.utfpr.alunos.rodrigodea.projetoinicial_real.model.Aluno;
import br.edu.utfpr.alunos.rodrigodea.projetoinicial_real.model.Aula;
import br.edu.utfpr.alunos.rodrigodea.projetoinicial_real.persistence.Banco;
import br.edu.utfpr.alunos.rodrigodea.projetoinicial_real.model.Plano;

public class AddClassActivity extends AppCompatActivity {

    private EditText editTextTopic;
    private Spinner spinnerPlano;
    private CheckBox checkBoxPago;
    private EditText editTextData;
    private ImageButton imageButtonData;
    private EditText editTextTime;
    private ImageButton imageButtonTime;
    private Spinner spinnerAluno;
    private Button buttonAddClass;

    private DatePickerDialog dpd;
    private TimePickerDialog tpd;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_class);
        setTitle(R.string.newClass);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        editTextTopic = findViewById(R.id.editTextTopic);
        spinnerPlano = findViewById(R.id.spinnerPlano);
        checkBoxPago = findViewById(R.id.checkBoxPago);
        editTextData = findViewById(R.id.editTextDate);
        imageButtonData = findViewById(R.id.imageButtonDate);
        editTextTime = findViewById(R.id.editTextTime);
        imageButtonTime = findViewById(R.id.imageButtonTime);
        spinnerAluno = findViewById(R.id.spinnerAluno);
        buttonAddClass = findViewById(R.id.buttonAddClass);

        //DATEPICKER--------------------------------------------
        imageButtonData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();

                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                dpd = new DatePickerDialog(AddClassActivity.this, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        editTextData.setText(dayOfMonth + "/" + (month+1) + "/" + year);
                    }

                }, year, month, day);

                dpd.show();
            }
        });
        //DATEPICKER--------------------------------------------

        //TIMEPICKER--------------------------------------------
        imageButtonTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();

                tpd = new TimePickerDialog(AddClassActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        editTextTime.setText(hourOfDay + ":" + minute);
                    }
                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);

                tpd.show();
            }
        });
        //TIMEPICKER--------------------------------------------

        popularSpinners();

    }

    private void popularSpinners(){

        ArrayAdapter<Aluno> adapterAluno = new ArrayAdapter<Aluno>(this, android.R.layout.simple_list_item_1, Banco.arrayListAluno);
        ArrayAdapter<Plano> adapterPlano = new ArrayAdapter<Plano>(this, android.R.layout.simple_list_item_1, Banco.arrayListPlano);

        spinnerAluno.setAdapter(adapterAluno);
        spinnerPlano.setAdapter(adapterPlano);
    }

    public void addClass(View view) throws ParseException {

        Plano plano;
        Date data;
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy'T'HH:mm:ss");

        plano = (Plano) spinnerPlano.getSelectedItem();
        data = format.parse(editTextData.getText().toString() + "T" + editTextTime.getText().toString() + ":00");

        for(int i = 0; i < plano.getQuantidade(); i++) {
            Aula aula = new Aula();

            aula.setId(Banco.id);
            Banco.id++;
            aula.setMateria(editTextTopic.getText().toString());
            aula.setData(data);
            aula.setPago(checkBoxPago.isChecked());
            aula.setAluno((Aluno) spinnerAluno.getSelectedItem());
            aula.setPlano(plano);

            calendar.setTime(data);
            calendar.add(Calendar.DATE, plano.getIntervalo());
            if (calendar.DAY_OF_WEEK == 1)
                calendar.add(Calendar.DATE, 1);
            data = calendar.getTime();

            Banco.arrayListAula.add(aula);

            finish();
        }

    }

}
