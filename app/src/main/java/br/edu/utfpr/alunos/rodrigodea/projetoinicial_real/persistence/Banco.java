package br.edu.utfpr.alunos.rodrigodea.projetoinicial_real.persistence;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import br.edu.utfpr.alunos.rodrigodea.projetoinicial_real.model.Aluno;
import br.edu.utfpr.alunos.rodrigodea.projetoinicial_real.model.Aula;
import br.edu.utfpr.alunos.rodrigodea.projetoinicial_real.model.Plano;

@Database(entities = {Aluno.class, Aula.class, Plano.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class Banco extends RoomDatabase {

    public abstract AlunoDao alunoDao();

    public abstract AulaDao aulaDao();

    public abstract PlanoDao planoDao();

    private static Banco instance;

    public static Banco getBanco(final Context context) {

        if (instance == null) {
            synchronized (Banco.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context, Banco.class, "calendario.db")
                            .allowMainThreadQueries().build();
                }
            }
        }

        return instance;
    }

}
