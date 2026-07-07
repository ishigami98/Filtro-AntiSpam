package com.upao.filtroantispam.data;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
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
public final class NumeroBloqueadoDao_Impl implements NumeroBloqueadoDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<NumeroBloqueadoEntity> __insertionAdapterOfNumeroBloqueadoEntity;

  private final EntityDeletionOrUpdateAdapter<NumeroBloqueadoEntity> __deletionAdapterOfNumeroBloqueadoEntity;

  public NumeroBloqueadoDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfNumeroBloqueadoEntity = new EntityInsertionAdapter<NumeroBloqueadoEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `numeros_bloqueados` (`numero`,`fechaAgregado`,`motivo`,`esDemo`) VALUES (?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final NumeroBloqueadoEntity entity) {
        statement.bindString(1, entity.getNumero());
        statement.bindLong(2, entity.getFechaAgregado());
        if (entity.getMotivo() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getMotivo());
        }
        final int _tmp = entity.getEsDemo() ? 1 : 0;
        statement.bindLong(4, _tmp);
      }
    };
    this.__deletionAdapterOfNumeroBloqueadoEntity = new EntityDeletionOrUpdateAdapter<NumeroBloqueadoEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `numeros_bloqueados` WHERE `numero` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final NumeroBloqueadoEntity entity) {
        statement.bindString(1, entity.getNumero());
      }
    };
  }

  @Override
  public void agregar(final NumeroBloqueadoEntity numero) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfNumeroBloqueadoEntity.insert(numero);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void eliminar(final NumeroBloqueadoEntity numero) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __deletionAdapterOfNumeroBloqueadoEntity.handle(numero);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public Flow<List<NumeroBloqueadoEntity>> obtenerTodos() {
    final String _sql = "SELECT * FROM numeros_bloqueados ORDER BY fechaAgregado DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"numeros_bloqueados"}, new Callable<List<NumeroBloqueadoEntity>>() {
      @Override
      @NonNull
      public List<NumeroBloqueadoEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfNumero = CursorUtil.getColumnIndexOrThrow(_cursor, "numero");
          final int _cursorIndexOfFechaAgregado = CursorUtil.getColumnIndexOrThrow(_cursor, "fechaAgregado");
          final int _cursorIndexOfMotivo = CursorUtil.getColumnIndexOrThrow(_cursor, "motivo");
          final int _cursorIndexOfEsDemo = CursorUtil.getColumnIndexOrThrow(_cursor, "esDemo");
          final List<NumeroBloqueadoEntity> _result = new ArrayList<NumeroBloqueadoEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final NumeroBloqueadoEntity _item;
            final String _tmpNumero;
            _tmpNumero = _cursor.getString(_cursorIndexOfNumero);
            final long _tmpFechaAgregado;
            _tmpFechaAgregado = _cursor.getLong(_cursorIndexOfFechaAgregado);
            final String _tmpMotivo;
            if (_cursor.isNull(_cursorIndexOfMotivo)) {
              _tmpMotivo = null;
            } else {
              _tmpMotivo = _cursor.getString(_cursorIndexOfMotivo);
            }
            final boolean _tmpEsDemo;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfEsDemo);
            _tmpEsDemo = _tmp != 0;
            _item = new NumeroBloqueadoEntity(_tmpNumero,_tmpFechaAgregado,_tmpMotivo,_tmpEsDemo);
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

  @Override
  public boolean estaBloqueado(final String numero) {
    final String _sql = "SELECT EXISTS(SELECT 1 FROM numeros_bloqueados WHERE numero = ?)";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, numero);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final boolean _result;
      if (_cursor.moveToFirst()) {
        final int _tmp;
        _tmp = _cursor.getInt(0);
        _result = _tmp != 0;
      } else {
        _result = false;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
