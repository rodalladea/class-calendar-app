package br.edu.utfpr.alunos.rodrigodea.projetoinicial_real.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

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
    private int aluno; //

    @ColumnInfo (index = true)
    private int plano; //

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

    public int getAluno() {
        return aluno;
    }

    public void setAluno(int aluno) {
        this.aluno = aluno;
    }

    public int getPlano() {
        return plano;
    }

    public void setPlano(int plano) {
        this.plano = plano;
    }
}
