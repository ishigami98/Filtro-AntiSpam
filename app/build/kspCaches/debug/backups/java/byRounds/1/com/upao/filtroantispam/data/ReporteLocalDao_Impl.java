package com.upao.filtroantispam.data;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class ReporteLocalDao_Impl implements ReporteLocalDao {
  private final RoomDatabase __db;

  private final SharedSQLiteStatement __preparedStmtOfIncrementarExacto;

  public ReporteLocalDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__preparedStmtOfIncrementarExacto = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "\n"
                + "        INSERT INTO reportes_locales(numero, cantidad) VALUES(?, 1)\n"
                + "        ON CONFLICT(numero) DO UPDATE SET cantidad = cantidad + 1\n"
                + "        ";
        return _query;
      }
    };
  }

  @Override
  public void incrementarExacto(final String numeroExacto) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfIncrementarExacto.acquire();
    int _argIndex = 1;
    _stmt.bindString(_argIndex, numeroExacto);
    try {
      __db.beginTransaction();
      try {
        _stmt.executeInsert();
        __db.setTransactionSuccessful();
      } finally {
        __db.endTransaction();
      }
    } finally {
      __preparedStmtOfIncrementarExacto.release(_stmt);
    }
  }

  @Override
  public List<ReporteLocalEntity> obtenerTodosSync() {
    final String _sql = "SELECT * FROM reportes_locales";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfNumero = CursorUtil.getColumnIndexOrThrow(_cursor, "numero");
      final int _cursorIndexOfCantidad = CursorUtil.getColumnIndexOrThrow(_cursor, "cantidad");
      final List<ReporteLocalEntity> _result = new ArrayList<ReporteLocalEntity>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final ReporteLocalEntity _item;
        final String _tmpNumero;
        _tmpNumero = _cursor.getString(_cursorIndexOfNumero);
        final int _tmpCantidad;
        _tmpCantidad = _cursor.getInt(_cursorIndexOfCantidad);
        _item = new ReporteLocalEntity(_tmpNumero,_tmpCantidad);
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
