package br.edu.utfpr.alunos.rodrigodea.projetoinicial_real.persistence;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import br.edu.utfpr.alunos.rodrigodea.projetoinicial_real.model.Aluno;

@Dao
public interface AlunoDao {

    @Insert
    long insert(Aluno aluno);

    @Delete
    void delete(Aluno aluno);

    @Update
    void update(Aluno aluno);

    @Query("SELECT * FROM alunos WHERE id = :id")
    Aluno queryForId(long id);

    @Query("SELECT * FROM alunos ORDER BY nome ASC")
    List<Aluno> queryForAll();
}
