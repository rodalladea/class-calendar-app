package br.edu.utfpr.alunos.rodrigodea.projetoinicial_real.persistence;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import br.edu.utfpr.alunos.rodrigodea.projetoinicial_real.model.Plano;

@Dao
public interface PlanoDao {

    @Insert
    long insert(Plano plano);

    @Delete
    void delete(Plano plano);

    @Update
    void update(Plano plano);

    @Query("SELECT * FROM planos WHERE id = :id")
    Plano queryForId(long id);

    @Query("SELECT * FROM planos ORDER BY nome ASC")
    List<Plano> queryForAll();
}
