package br.edu.utfpr.alunos.rodrigodea.projetoinicial_real.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;



@Entity (tableName = "aulas",
         foreignKeys = {@ForeignKey(entity = Aluno.class,
                                    parentColumns = "id",
                                    childColumns = "aluno"),
                        @ForeignKey(entity = Plano.class,
                                    parentColumns = "id",
                                    childColumns = "plano")})
public class Aula {

    @PrimaryKey (autoGenerate = true)
    private long id;

    private Date data; //
    private String materia; //
    private boolean pago; //

    @ColumnInfo (index = true)
    private Aluno aluno; //

    @ColumnInfo (index = true)
    private Plano plano; //

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getMateria() {
        return materia;
    }

    public void setMateria(String materia) {
        this.materia = materia;
    }

    public boolean isPago() {
        return pago;
    }

    public void setPago(boolean pago) {
        this.pago = pago;
    }

    public Aluno getAluno() {
        return aluno;
    }

    public void setAluno(Aluno aluno) {
        this.aluno = aluno;
    }

    public Plano getPlano() {
        return plano;
    }

    public void setPlano(Plano plano) {
        this.plano = plano;
    }

    @Override
    public String toString() {
        DateFormat dateFormat = new SimpleDateFormat("HH:mm");

        return this.getAluno().getNome() + " - " + dateFormat.format(this.getData());
    }
}
