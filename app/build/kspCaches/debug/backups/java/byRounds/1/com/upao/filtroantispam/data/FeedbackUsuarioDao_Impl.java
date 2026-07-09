package com.upao.filtroantispam.data;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
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
public final class FeedbackUsuarioDao_Impl implements FeedbackUsuarioDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<FeedbackUsuarioEntity> __insertionAdapterOfFeedbackUsuarioEntity;

  public FeedbackUsuarioDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfFeedbackUsuarioEntity = new EntityInsertionAdapter<FeedbackUsuarioEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `feedback_usuario` (`numero`,`marcadoSpam`,`fecha`) VALUES (?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final FeedbackUsuarioEntity entity) {
        statement.bindString(1, entity.getNumero());
        final int _tmp = entity.getMarcadoSpam() ? 1 : 0;
        statement.bindLong(2, _tmp);
        statement.bindLong(3, entity.getFecha());
      }
    };
  }

  @Override
  public void marcar(final FeedbackUsuarioEntity feedback) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfFeedbackUsuarioEntity.insert(feedback);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public Flow<List<FeedbackUsuarioEntity>> obtenerTodos() {
    final String _sql = "SELECT * FROM feedback_usuario ORDER BY fecha DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"feedback_usuario"}, new Callable<List<FeedbackUsuarioEntity>>() {
      @Override
      @NonNull
      public List<FeedbackUsuarioEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfNumero = CursorUtil.getColumnIndexOrThrow(_cursor, "numero");
          final int _cursorIndexOfMarcadoSpam = CursorUtil.getColumnIndexOrThrow(_cursor, "marcadoSpam");
          final int _cursorIndexOfFecha = CursorUtil.getColumnIndexOrThrow(_cursor, "fecha");
          final List<FeedbackUsuarioEntity> _result = new ArrayList<FeedbackUsuarioEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final FeedbackUsuarioEntity _item;
            final String _tmpNumero;
            _tmpNumero = _cursor.getString(_cursorIndexOfNumero);
            final boolean _tmpMarcadoSpam;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfMarcadoSpam);
            _tmpMarcadoSpam = _tmp != 0;
            final long _tmpFecha;
            _tmpFecha = _cursor.getLong(_cursorIndexOfFecha);
            _item = new FeedbackUsuarioEntity(_tmpNumero,_tmpMarcadoSpam,_tmpFecha);
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
  public List<FeedbackUsuarioEntity> obtenerTodosSync() {
    final String _sql = "SELECT * FROM feedback_usuario";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfNumero = CursorUtil.getColumnIndexOrThrow(_cursor, "numero");
      final int _cursorIndexOfMarcadoSpam = CursorUtil.getColumnIndexOrThrow(_cursor, "marcadoSpam");
      final int _cursorIndexOfFecha = CursorUtil.getColumnIndexOrThrow(_cursor, "fecha");
      final List<FeedbackUsuarioEntity> _result = new ArrayList<FeedbackUsuarioEntity>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final FeedbackUsuarioEntity _item;
        final String _tmpNumero;
        _tmpNumero = _cursor.getString(_cursorIndexOfNumero);
        final boolean _tmpMarcadoSpam;
        final int _tmp;
        _tmp = _cursor.getInt(_cursorIndexOfMarcadoSpam);
        _tmpMarcadoSpam = _tmp != 0;
        final long _tmpFecha;
        _tmpFecha = _cursor.getLong(_cursorIndexOfFecha);
        _item = new FeedbackUsuarioEntity(_tmpNumero,_tmpMarcadoSpam,_tmpFecha);
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
