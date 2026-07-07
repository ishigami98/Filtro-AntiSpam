package com.upao.filtroantispam.data;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class FiltroDatabase_Impl extends FiltroDatabase {
  private volatile RegistroLlamadaDao _registroLlamadaDao;

  private volatile NumeroBloqueadoDao _numeroBloqueadoDao;

  private volatile HorarioBloqueoDao _horarioBloqueoDao;

  private volatile ReporteLocalDao _reporteLocalDao;

  private volatile FeedbackUsuarioDao _feedbackUsuarioDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(4) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `registro_llamadas` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `numero` TEXT NOT NULL, `fechaHora` INTEGER NOT NULL, `bloqueada` INTEGER NOT NULL, `probabilidadSpam` REAL NOT NULL, `categoria` TEXT NOT NULL, `motivo` TEXT, `prefijoPais` TEXT, `esDemo` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `numeros_bloqueados` (`numero` TEXT NOT NULL, `fechaAgregado` INTEGER NOT NULL, `motivo` TEXT, `esDemo` INTEGER NOT NULL, PRIMARY KEY(`numero`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `horarios_bloqueo` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `minutoInicio` INTEGER NOT NULL, `minutoFin` INTEGER NOT NULL, `activo` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `reportes_locales` (`numero` TEXT NOT NULL, `cantidad` INTEGER NOT NULL, PRIMARY KEY(`numero`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `feedback_usuario` (`numero` TEXT NOT NULL, `marcadoSpam` INTEGER NOT NULL, `fecha` INTEGER NOT NULL, PRIMARY KEY(`numero`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '39dff07d629b778db101b5826fe09346')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `registro_llamadas`");
        db.execSQL("DROP TABLE IF EXISTS `numeros_bloqueados`");
        db.execSQL("DROP TABLE IF EXISTS `horarios_bloqueo`");
        db.execSQL("DROP TABLE IF EXISTS `reportes_locales`");
        db.execSQL("DROP TABLE IF EXISTS `feedback_usuario`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsRegistroLlamadas = new HashMap<String, TableInfo.Column>(9);
        _columnsRegistroLlamadas.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRegistroLlamadas.put("numero", new TableInfo.Column("numero", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRegistroLlamadas.put("fechaHora", new TableInfo.Column("fechaHora", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRegistroLlamadas.put("bloqueada", new TableInfo.Column("bloqueada", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRegistroLlamadas.put("probabilidadSpam", new TableInfo.Column("probabilidadSpam", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRegistroLlamadas.put("categoria", new TableInfo.Column("categoria", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRegistroLlamadas.put("motivo", new TableInfo.Column("motivo", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRegistroLlamadas.put("prefijoPais", new TableInfo.Column("prefijoPais", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRegistroLlamadas.put("esDemo", new TableInfo.Column("esDemo", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysRegistroLlamadas = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesRegistroLlamadas = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoRegistroLlamadas = new TableInfo("registro_llamadas", _columnsRegistroLlamadas, _foreignKeysRegistroLlamadas, _indicesRegistroLlamadas);
        final TableInfo _existingRegistroLlamadas = TableInfo.read(db, "registro_llamadas");
        if (!_infoRegistroLlamadas.equals(_existingRegistroLlamadas)) {
          return new RoomOpenHelper.ValidationResult(false, "registro_llamadas(com.upao.filtroantispam.data.RegistroLlamadaEntity).\n"
                  + " Expected:\n" + _infoRegistroLlamadas + "\n"
                  + " Found:\n" + _existingRegistroLlamadas);
        }
        final HashMap<String, TableInfo.Column> _columnsNumerosBloqueados = new HashMap<String, TableInfo.Column>(4);
        _columnsNumerosBloqueados.put("numero", new TableInfo.Column("numero", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsNumerosBloqueados.put("fechaAgregado", new TableInfo.Column("fechaAgregado", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsNumerosBloqueados.put("motivo", new TableInfo.Column("motivo", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsNumerosBloqueados.put("esDemo", new TableInfo.Column("esDemo", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysNumerosBloqueados = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesNumerosBloqueados = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoNumerosBloqueados = new TableInfo("numeros_bloqueados", _columnsNumerosBloqueados, _foreignKeysNumerosBloqueados, _indicesNumerosBloqueados);
        final TableInfo _existingNumerosBloqueados = TableInfo.read(db, "numeros_bloqueados");
        if (!_infoNumerosBloqueados.equals(_existingNumerosBloqueados)) {
          return new RoomOpenHelper.ValidationResult(false, "numeros_bloqueados(com.upao.filtroantispam.data.NumeroBloqueadoEntity).\n"
                  + " Expected:\n" + _infoNumerosBloqueados + "\n"
                  + " Found:\n" + _existingNumerosBloqueados);
        }
        final HashMap<String, TableInfo.Column> _columnsHorariosBloqueo = new HashMap<String, TableInfo.Column>(4);
        _columnsHorariosBloqueo.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHorariosBloqueo.put("minutoInicio", new TableInfo.Column("minutoInicio", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHorariosBloqueo.put("minutoFin", new TableInfo.Column("minutoFin", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHorariosBloqueo.put("activo", new TableInfo.Column("activo", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysHorariosBloqueo = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesHorariosBloqueo = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoHorariosBloqueo = new TableInfo("horarios_bloqueo", _columnsHorariosBloqueo, _foreignKeysHorariosBloqueo, _indicesHorariosBloqueo);
        final TableInfo _existingHorariosBloqueo = TableInfo.read(db, "horarios_bloqueo");
        if (!_infoHorariosBloqueo.equals(_existingHorariosBloqueo)) {
          return new RoomOpenHelper.ValidationResult(false, "horarios_bloqueo(com.upao.filtroantispam.data.HorarioBloqueoEntity).\n"
                  + " Expected:\n" + _infoHorariosBloqueo + "\n"
                  + " Found:\n" + _existingHorariosBloqueo);
        }
        final HashMap<String, TableInfo.Column> _columnsReportesLocales = new HashMap<String, TableInfo.Column>(2);
        _columnsReportesLocales.put("numero", new TableInfo.Column("numero", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReportesLocales.put("cantidad", new TableInfo.Column("cantidad", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysReportesLocales = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesReportesLocales = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoReportesLocales = new TableInfo("reportes_locales", _columnsReportesLocales, _foreignKeysReportesLocales, _indicesReportesLocales);
        final TableInfo _existingReportesLocales = TableInfo.read(db, "reportes_locales");
        if (!_infoReportesLocales.equals(_existingReportesLocales)) {
          return new RoomOpenHelper.ValidationResult(false, "reportes_locales(com.upao.filtroantispam.data.ReporteLocalEntity).\n"
                  + " Expected:\n" + _infoReportesLocales + "\n"
                  + " Found:\n" + _existingReportesLocales);
        }
        final HashMap<String, TableInfo.Column> _columnsFeedbackUsuario = new HashMap<String, TableInfo.Column>(3);
        _columnsFeedbackUsuario.put("numero", new TableInfo.Column("numero", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFeedbackUsuario.put("marcadoSpam", new TableInfo.Column("marcadoSpam", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFeedbackUsuario.put("fecha", new TableInfo.Column("fecha", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysFeedbackUsuario = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesFeedbackUsuario = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoFeedbackUsuario = new TableInfo("feedback_usuario", _columnsFeedbackUsuario, _foreignKeysFeedbackUsuario, _indicesFeedbackUsuario);
        final TableInfo _existingFeedbackUsuario = TableInfo.read(db, "feedback_usuario");
        if (!_infoFeedbackUsuario.equals(_existingFeedbackUsuario)) {
          return new RoomOpenHelper.ValidationResult(false, "feedback_usuario(com.upao.filtroantispam.data.FeedbackUsuarioEntity).\n"
                  + " Expected:\n" + _infoFeedbackUsuario + "\n"
                  + " Found:\n" + _existingFeedbackUsuario);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "39dff07d629b778db101b5826fe09346", "559f2a8e6ba04778cd8fa3271b2d08f2");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "registro_llamadas","numeros_bloqueados","horarios_bloqueo","reportes_locales","feedback_usuario");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `registro_llamadas`");
      _db.execSQL("DELETE FROM `numeros_bloqueados`");
      _db.execSQL("DELETE FROM `horarios_bloqueo`");
      _db.execSQL("DELETE FROM `reportes_locales`");
      _db.execSQL("DELETE FROM `feedback_usuario`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(RegistroLlamadaDao.class, RegistroLlamadaDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(NumeroBloqueadoDao.class, NumeroBloqueadoDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(HorarioBloqueoDao.class, HorarioBloqueoDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(ReporteLocalDao.class, ReporteLocalDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(FeedbackUsuarioDao.class, FeedbackUsuarioDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public RegistroLlamadaDao registroLlamadaDao() {
    if (_registroLlamadaDao != null) {
      return _registroLlamadaDao;
    } else {
      synchronized(this) {
        if(_registroLlamadaDao == null) {
          _registroLlamadaDao = new RegistroLlamadaDao_Impl(this);
        }
        return _registroLlamadaDao;
      }
    }
  }

  @Override
  public NumeroBloqueadoDao numeroBloqueadoDao() {
    if (_numeroBloqueadoDao != null) {
      return _numeroBloqueadoDao;
    } else {
      synchronized(this) {
        if(_numeroBloqueadoDao == null) {
          _numeroBloqueadoDao = new NumeroBloqueadoDao_Impl(this);
        }
        return _numeroBloqueadoDao;
      }
    }
  }

  @Override
  public HorarioBloqueoDao horarioBloqueoDao() {
    if (_horarioBloqueoDao != null) {
      return _horarioBloqueoDao;
    } else {
      synchronized(this) {
        if(_horarioBloqueoDao == null) {
          _horarioBloqueoDao = new HorarioBloqueoDao_Impl(this);
        }
        return _horarioBloqueoDao;
      }
    }
  }

  @Override
  public ReporteLocalDao reporteLocalDao() {
    if (_reporteLocalDao != null) {
      return _reporteLocalDao;
    } else {
      synchronized(this) {
        if(_reporteLocalDao == null) {
          _reporteLocalDao = new ReporteLocalDao_Impl(this);
        }
        return _reporteLocalDao;
      }
    }
  }

  @Override
  public FeedbackUsuarioDao feedbackUsuarioDao() {
    if (_feedbackUsuarioDao != null) {
      return _feedbackUsuarioDao;
    } else {
      synchronized(this) {
        if(_feedbackUsuarioDao == null) {
          _feedbackUsuarioDao = new FeedbackUsuarioDao_Impl(this);
        }
        return _feedbackUsuarioDao;
      }
    }
  }
}
