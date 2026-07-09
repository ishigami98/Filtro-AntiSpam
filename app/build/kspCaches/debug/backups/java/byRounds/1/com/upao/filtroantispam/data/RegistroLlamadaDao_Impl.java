package com.upao.filtroantispam.data;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class RegistroLlamadaDao_Impl implements RegistroLlamadaDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<RegistroLlamadaEntity> __insertionAdapterOfRegistroLlamadaEntity;

  private final EntityDeletionOrUpdateAdapter<RegistroLlamadaEntity> __deletionAdapterOfRegistroLlamadaEntity;

  private final EntityDeletionOrUpdateAdapter<RegistroLlamadaEntity> __updateAdapterOfRegistroLlamadaEntity;

  private final SharedSQLiteStatement __preparedStmtOfPodarAntiguos;

  public RegistroLlamadaDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfRegistroLlamadaEntity = new EntityInsertionAdapter<RegistroLlamadaEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `registro_llamadas` (`id`,`numero`,`fechaHora`,`bloqueada`,`probabilidadSpam`,`categoria`,`motivo`,`prefijoPais`,`esDemo`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final RegistroLlamadaEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getNumero());
        statement.bindLong(3, entity.getFechaHora());
        final int _tmp = entity.getBloqueada() ? 1 : 0;
        statement.bindLong(4, _tmp);
        statement.bindDouble(5, entity.getProbabilidadSpam());
        statement.bindString(6, entity.getCategoria());
        if (entity.getMotivo() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getMotivo());
        }
        if (entity.getPrefijoPais() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getPrefijoPais());
        }
        final int _tmp_1 = entity.getEsDemo() ? 1 : 0;
        statement.bindLong(9, _tmp_1);
      }
    };
    this.__deletionAdapterOfRegistroLlamadaEntity = new EntityDeletionOrUpdateAdapter<RegistroLlamadaEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `registro_llamadas` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final RegistroLlamadaEntity entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfRegistroLlamadaEntity = new EntityDeletionOrUpdateAdapter<RegistroLlamadaEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `registro_llamadas` SET `id` = ?,`numero` = ?,`fechaHora` = ?,`bloqueada` = ?,`probabilidadSpam` = ?,`categoria` = ?,`motivo` = ?,`prefijoPais` = ?,`esDemo` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final RegistroLlamadaEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getNumero());
        statement.bindLong(3, entity.getFechaHora());
        final int _tmp = entity.getBloqueada() ? 1 : 0;
        statement.bindLong(4, _tmp);
        statement.bindDouble(5, entity.getProbabilidadSpam());
        statement.bindString(6, entity.getCategoria());
        if (entity.getMotivo() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getMotivo());
        }
        if (entity.getPrefijoPais() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getPrefijoPais());
        }
        final int _tmp_1 = entity.getEsDemo() ? 1 : 0;
        statement.bindLong(9, _tmp_1);
        statement.bindLong(10, entity.getId());
      }
    };
    this.__preparedStmtOfPodarAntiguos = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM registro_llamadas WHERE id NOT IN (SELECT id FROM registro_llamadas ORDER BY fechaHora DESC LIMIT 300)";
        return _query;
      }
    };
  }

  @Override
  public void insertar(final RegistroLlamadaEntity registro) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfRegistroLlamadaEntity.insert(registro);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void eliminar(final RegistroLlamadaEntity registro) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __deletionAdapterOfRegistroLlamadaEntity.handle(registro);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void actualizar(final RegistroLlamadaEntity registro) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __updateAdapterOfRegistroLlamadaEntity.handle(registro);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void podarAntiguos() {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfPodarAntiguos.acquire();
    try {
      __db.beginTransaction();
      try {
        _stmt.executeUpdateDelete();
        __db.setTransactionSuccessful();
      } finally {
        __db.endTransaction();
      }
    } finally {
      __preparedStmtOfPodarAntiguos.release(_stmt);
    }
  }

  @Override
  public Flow<List<RegistroLlamadaEntity>> obtenerTodos() {
    final String _sql = "SELECT * FROM registro_llamadas ORDER BY fechaHora DESC LIMIT 300";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"registro_llamadas"}, new Callable<List<RegistroLlamadaEntity>>() {
      @Override
      @NonNull
      public List<RegistroLlamadaEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfNumero = CursorUtil.getColumnIndexOrThrow(_cursor, "numero");
          final int _cursorIndexOfFechaHora = CursorUtil.getColumnIndexOrThrow(_cursor, "fechaHora");
          final int _cursorIndexOfBloqueada = CursorUtil.getColumnIndexOrThrow(_cursor, "bloqueada");
          final int _cursorIndexOfProbabilidadSpam = CursorUtil.getColumnIndexOrThrow(_cursor, "probabilidadSpam");
          final int _cursorIndexOfCategoria = CursorUtil.getColumnIndexOrThrow(_cursor, "categoria");
          final int _cursorIndexOfMotivo = CursorUtil.getColumnIndexOrThrow(_cursor, "motivo");
          final int _cursorIndexOfPrefijoPais = CursorUtil.getColumnIndexOrThrow(_cursor, "prefijoPais");
          final int _cursorIndexOfEsDemo = CursorUtil.getColumnIndexOrThrow(_cursor, "esDemo");
          final List<RegistroLlamadaEntity> _result = new ArrayList<RegistroLlamadaEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final RegistroLlamadaEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpNumero;
            _tmpNumero = _cursor.getString(_cursorIndexOfNumero);
            final long _tmpFechaHora;
            _tmpFechaHora = _cursor.getLong(_cursorIndexOfFechaHora);
            final boolean _tmpBloqueada;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfBloqueada);
            _tmpBloqueada = _tmp != 0;
            final double _tmpProbabilidadSpam;
            _tmpProbabilidadSpam = _cursor.getDouble(_cursorIndexOfProbabilidadSpam);
            final String _tmpCategoria;
            _tmpCategoria = _cursor.getString(_cursorIndexOfCategoria);
            final String _tmpMotivo;
            if (_cursor.isNull(_cursorIndexOfMotivo)) {
              _tmpMotivo = null;
            } else {
              _tmpMotivo = _cursor.getString(_cursorIndexOfMotivo);
            }
            final String _tmpPrefijoPais;
            if (_cursor.isNull(_cursorIndexOfPrefijoPais)) {
              _tmpPrefijoPais = null;
            } else {
              _tmpPrefijoPais = _cursor.getString(_cursorIndexOfPrefijoPais);
            }
            final boolean _tmpEsDemo;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfEsDemo);
            _tmpEsDemo = _tmp_1 != 0;
            _item = new RegistroLlamadaEntity(_tmpId,_tmpNumero,_tmpFechaHora,_tmpBloqueada,_tmpProbabilidadSpam,_tmpCategoria,_tmpMotivo,_tmpPrefijoPais,_tmpEsDemo);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
