package br.edu.utfpr.alunos.rodrigodea.projetoinicial_real.persistence;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import br.edu.utfpr.alunos.rodrigodea.projetoinicial_real.model.Aula;

@Dao
public interface AulaDao {

    @Insert
    long insert(Aula aula);

    @Delete
    void delete(Aula aula);

    @Update
    void update(Aula aula);

    @Query("SELECT * FROM aulas WHERE id = :id")
    Aula queryForId(long id);

    @Query("SELECT * FROM aulas ORDER BY data ASC")
    List<Aula> queryForAll();
}
