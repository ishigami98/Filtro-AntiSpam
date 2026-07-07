package com.upao.filtroantispam.data;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Class;
import java.lang.Integer;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.Collections;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class ReporteLocalDao_Impl implements ReporteLocalDao {
  private final RoomDatabase __db;

  private final SharedSQLiteStatement __preparedStmtOfIncrementar;

  public ReporteLocalDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__preparedStmtOfIncrementar = new SharedSQLiteStatement(__db) {
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
  public void incrementar(final String numero) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfIncrementar.acquire();
    int _argIndex = 1;
    _stmt.bindString(_argIndex, numero);
    try {
      __db.beginTransaction();
      try {
        _stmt.executeInsert();
        __db.setTransactionSuccessful();
      } finally {
        __db.endTransaction();
      }
    } finally {
      __preparedStmtOfIncrementar.release(_stmt);
    }
  }

  @Override
  public Integer obtenerCantidad(final String numero) {
    final String _sql = "SELECT cantidad FROM reportes_locales WHERE numero = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, numero);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final Integer _result;
      if (_cursor.moveToFirst()) {
        if (_cursor.isNull(0)) {
          _result = null;
        } else {
          _result = _cursor.getInt(0);
        }
      } else {
        _result = null;
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
