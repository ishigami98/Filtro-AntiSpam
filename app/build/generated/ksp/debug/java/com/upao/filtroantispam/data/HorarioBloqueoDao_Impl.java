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
public final class HorarioBloqueoDao_Impl implements HorarioBloqueoDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<HorarioBloqueoEntity> __insertionAdapterOfHorarioBloqueoEntity;

  private final EntityDeletionOrUpdateAdapter<HorarioBloqueoEntity> __deletionAdapterOfHorarioBloqueoEntity;

  private final EntityDeletionOrUpdateAdapter<HorarioBloqueoEntity> __updateAdapterOfHorarioBloqueoEntity;

  public HorarioBloqueoDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfHorarioBloqueoEntity = new EntityInsertionAdapter<HorarioBloqueoEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `horarios_bloqueo` (`id`,`minutoInicio`,`minutoFin`,`activo`) VALUES (nullif(?, 0),?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final HorarioBloqueoEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getMinutoInicio());
        statement.bindLong(3, entity.getMinutoFin());
        final int _tmp = entity.getActivo() ? 1 : 0;
        statement.bindLong(4, _tmp);
      }
    };
    this.__deletionAdapterOfHorarioBloqueoEntity = new EntityDeletionOrUpdateAdapter<HorarioBloqueoEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `horarios_bloqueo` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final HorarioBloqueoEntity entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfHorarioBloqueoEntity = new EntityDeletionOrUpdateAdapter<HorarioBloqueoEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `horarios_bloqueo` SET `id` = ?,`minutoInicio` = ?,`minutoFin` = ?,`activo` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final HorarioBloqueoEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getMinutoInicio());
        statement.bindLong(3, entity.getMinutoFin());
        final int _tmp = entity.getActivo() ? 1 : 0;
        statement.bindLong(4, _tmp);
        statement.bindLong(5, entity.getId());
      }
    };
  }

  @Override
  public void agregar(final HorarioBloqueoEntity horario) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfHorarioBloqueoEntity.insert(horario);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void eliminar(final HorarioBloqueoEntity horario) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __deletionAdapterOfHorarioBloqueoEntity.handle(horario);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void actualizar(final HorarioBloqueoEntity horario) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __updateAdapterOfHorarioBloqueoEntity.handle(horario);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public Flow<List<HorarioBloqueoEntity>> obtenerTodos() {
    final String _sql = "SELECT * FROM horarios_bloqueo ORDER BY minutoInicio";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"horarios_bloqueo"}, new Callable<List<HorarioBloqueoEntity>>() {
      @Override
      @NonNull
      public List<HorarioBloqueoEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfMinutoInicio = CursorUtil.getColumnIndexOrThrow(_cursor, "minutoInicio");
          final int _cursorIndexOfMinutoFin = CursorUtil.getColumnIndexOrThrow(_cursor, "minutoFin");
          final int _cursorIndexOfActivo = CursorUtil.getColumnIndexOrThrow(_cursor, "activo");
          final List<HorarioBloqueoEntity> _result = new ArrayList<HorarioBloqueoEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final HorarioBloqueoEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final int _tmpMinutoInicio;
            _tmpMinutoInicio = _cursor.getInt(_cursorIndexOfMinutoInicio);
            final int _tmpMinutoFin;
            _tmpMinutoFin = _cursor.getInt(_cursorIndexOfMinutoFin);
            final boolean _tmpActivo;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfActivo);
            _tmpActivo = _tmp != 0;
            _item = new HorarioBloqueoEntity(_tmpId,_tmpMinutoInicio,_tmpMinutoFin,_tmpActivo);
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
  public List<HorarioBloqueoEntity> obtenerActivos() {
    final String _sql = "SELECT * FROM horarios_bloqueo WHERE activo = 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfMinutoInicio = CursorUtil.getColumnIndexOrThrow(_cursor, "minutoInicio");
      final int _cursorIndexOfMinutoFin = CursorUtil.getColumnIndexOrThrow(_cursor, "minutoFin");
      final int _cursorIndexOfActivo = CursorUtil.getColumnIndexOrThrow(_cursor, "activo");
      final List<HorarioBloqueoEntity> _result = new ArrayList<HorarioBloqueoEntity>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final HorarioBloqueoEntity _item;
        final long _tmpId;
        _tmpId = _cursor.getLong(_cursorIndexOfId);
        final int _tmpMinutoInicio;
        _tmpMinutoInicio = _cursor.getInt(_cursorIndexOfMinutoInicio);
        final int _tmpMinutoFin;
        _tmpMinutoFin = _cursor.getInt(_cursorIndexOfMinutoFin);
        final boolean _tmpActivo;
        final int _tmp;
        _tmp = _cursor.getInt(_cursorIndexOfActivo);
        _tmpActivo = _tmp != 0;
        _item = new HorarioBloqueoEntity(_tmpId,_tmpMinutoInicio,_tmpMinutoFin,_tmpActivo);
        _result.add(_item);
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
