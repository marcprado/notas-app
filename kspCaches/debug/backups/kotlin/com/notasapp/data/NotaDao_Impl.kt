package com.notasapp.`data`

import androidx.room.EntityDeleteOrUpdateAdapter
import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import javax.`annotation`.processing.Generated
import kotlin.Boolean
import kotlin.Int
import kotlin.Long
import kotlin.String
import kotlin.Suppress
import kotlin.Unit
import kotlin.collections.List
import kotlin.collections.MutableList
import kotlin.collections.mutableListOf
import kotlin.reflect.KClass
import kotlinx.coroutines.flow.Flow

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class NotaDao_Impl(
  __db: RoomDatabase,
) : NotaDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfNota: EntityInsertAdapter<Nota>

  private val __deleteAdapterOfNota: EntityDeleteOrUpdateAdapter<Nota>

  private val __updateAdapterOfNota: EntityDeleteOrUpdateAdapter<Nota>
  init {
    this.__db = __db
    this.__insertAdapterOfNota = object : EntityInsertAdapter<Nota>() {
      protected override fun createQuery(): String =
          "INSERT OR REPLACE INTO `notas` (`id`,`titulo`,`contenido`,`categoria`,`prioridad`,`completada`,`tieneFoto`,`fotoUri`,`fechaCreacion`,`sincronizado`,`idServidor`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: Nota) {
        statement.bindLong(1, entity.id.toLong())
        statement.bindText(2, entity.titulo)
        statement.bindText(3, entity.contenido)
        statement.bindText(4, entity.categoria)
        statement.bindText(5, entity.prioridad)
        val _tmp: Int = if (entity.completada) 1 else 0
        statement.bindLong(6, _tmp.toLong())
        val _tmp_1: Int = if (entity.tieneFoto) 1 else 0
        statement.bindLong(7, _tmp_1.toLong())
        statement.bindText(8, entity.fotoUri)
        statement.bindLong(9, entity.fechaCreacion)
        val _tmp_2: Int = if (entity.sincronizado) 1 else 0
        statement.bindLong(10, _tmp_2.toLong())
        val _tmpIdServidor: Long? = entity.idServidor
        if (_tmpIdServidor == null) {
          statement.bindNull(11)
        } else {
          statement.bindLong(11, _tmpIdServidor)
        }
      }
    }
    this.__deleteAdapterOfNota = object : EntityDeleteOrUpdateAdapter<Nota>() {
      protected override fun createQuery(): String = "DELETE FROM `notas` WHERE `id` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: Nota) {
        statement.bindLong(1, entity.id.toLong())
      }
    }
    this.__updateAdapterOfNota = object : EntityDeleteOrUpdateAdapter<Nota>() {
      protected override fun createQuery(): String =
          "UPDATE OR ABORT `notas` SET `id` = ?,`titulo` = ?,`contenido` = ?,`categoria` = ?,`prioridad` = ?,`completada` = ?,`tieneFoto` = ?,`fotoUri` = ?,`fechaCreacion` = ?,`sincronizado` = ?,`idServidor` = ? WHERE `id` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: Nota) {
        statement.bindLong(1, entity.id.toLong())
        statement.bindText(2, entity.titulo)
        statement.bindText(3, entity.contenido)
        statement.bindText(4, entity.categoria)
        statement.bindText(5, entity.prioridad)
        val _tmp: Int = if (entity.completada) 1 else 0
        statement.bindLong(6, _tmp.toLong())
        val _tmp_1: Int = if (entity.tieneFoto) 1 else 0
        statement.bindLong(7, _tmp_1.toLong())
        statement.bindText(8, entity.fotoUri)
        statement.bindLong(9, entity.fechaCreacion)
        val _tmp_2: Int = if (entity.sincronizado) 1 else 0
        statement.bindLong(10, _tmp_2.toLong())
        val _tmpIdServidor: Long? = entity.idServidor
        if (_tmpIdServidor == null) {
          statement.bindNull(11)
        } else {
          statement.bindLong(11, _tmpIdServidor)
        }
        statement.bindLong(12, entity.id.toLong())
      }
    }
  }

  public override suspend fun insertNota(nota: Nota): Long = performSuspending(__db, false, true) {
      _connection ->
    val _result: Long = __insertAdapterOfNota.insertAndReturnId(_connection, nota)
    _result
  }

  public override suspend fun deleteNota(nota: Nota): Unit = performSuspending(__db, false, true) {
      _connection ->
    __deleteAdapterOfNota.handle(_connection, nota)
  }

  public override suspend fun updateNota(nota: Nota): Unit = performSuspending(__db, false, true) {
      _connection ->
    __updateAdapterOfNota.handle(_connection, nota)
  }

  public override fun getAllNotas(): Flow<List<Nota>> {
    val _sql: String = "SELECT * FROM notas ORDER BY fechaCreacion DESC"
    return createFlow(__db, false, arrayOf("notas")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfTitulo: Int = getColumnIndexOrThrow(_stmt, "titulo")
        val _columnIndexOfContenido: Int = getColumnIndexOrThrow(_stmt, "contenido")
        val _columnIndexOfCategoria: Int = getColumnIndexOrThrow(_stmt, "categoria")
        val _columnIndexOfPrioridad: Int = getColumnIndexOrThrow(_stmt, "prioridad")
        val _columnIndexOfCompletada: Int = getColumnIndexOrThrow(_stmt, "completada")
        val _columnIndexOfTieneFoto: Int = getColumnIndexOrThrow(_stmt, "tieneFoto")
        val _columnIndexOfFotoUri: Int = getColumnIndexOrThrow(_stmt, "fotoUri")
        val _columnIndexOfFechaCreacion: Int = getColumnIndexOrThrow(_stmt, "fechaCreacion")
        val _columnIndexOfSincronizado: Int = getColumnIndexOrThrow(_stmt, "sincronizado")
        val _columnIndexOfIdServidor: Int = getColumnIndexOrThrow(_stmt, "idServidor")
        val _result: MutableList<Nota> = mutableListOf()
        while (_stmt.step()) {
          val _item: Nota
          val _tmpId: Int
          _tmpId = _stmt.getLong(_columnIndexOfId).toInt()
          val _tmpTitulo: String
          _tmpTitulo = _stmt.getText(_columnIndexOfTitulo)
          val _tmpContenido: String
          _tmpContenido = _stmt.getText(_columnIndexOfContenido)
          val _tmpCategoria: String
          _tmpCategoria = _stmt.getText(_columnIndexOfCategoria)
          val _tmpPrioridad: String
          _tmpPrioridad = _stmt.getText(_columnIndexOfPrioridad)
          val _tmpCompletada: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfCompletada).toInt()
          _tmpCompletada = _tmp != 0
          val _tmpTieneFoto: Boolean
          val _tmp_1: Int
          _tmp_1 = _stmt.getLong(_columnIndexOfTieneFoto).toInt()
          _tmpTieneFoto = _tmp_1 != 0
          val _tmpFotoUri: String
          _tmpFotoUri = _stmt.getText(_columnIndexOfFotoUri)
          val _tmpFechaCreacion: Long
          _tmpFechaCreacion = _stmt.getLong(_columnIndexOfFechaCreacion)
          val _tmpSincronizado: Boolean
          val _tmp_2: Int
          _tmp_2 = _stmt.getLong(_columnIndexOfSincronizado).toInt()
          _tmpSincronizado = _tmp_2 != 0
          val _tmpIdServidor: Long?
          if (_stmt.isNull(_columnIndexOfIdServidor)) {
            _tmpIdServidor = null
          } else {
            _tmpIdServidor = _stmt.getLong(_columnIndexOfIdServidor)
          }
          _item =
              Nota(_tmpId,_tmpTitulo,_tmpContenido,_tmpCategoria,_tmpPrioridad,_tmpCompletada,_tmpTieneFoto,_tmpFotoUri,_tmpFechaCreacion,_tmpSincronizado,_tmpIdServidor)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getNotasByCategoria(categoria: String): Flow<List<Nota>> {
    val _sql: String = "SELECT * FROM notas WHERE categoria = ? ORDER BY fechaCreacion DESC"
    return createFlow(__db, false, arrayOf("notas")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, categoria)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfTitulo: Int = getColumnIndexOrThrow(_stmt, "titulo")
        val _columnIndexOfContenido: Int = getColumnIndexOrThrow(_stmt, "contenido")
        val _columnIndexOfCategoria: Int = getColumnIndexOrThrow(_stmt, "categoria")
        val _columnIndexOfPrioridad: Int = getColumnIndexOrThrow(_stmt, "prioridad")
        val _columnIndexOfCompletada: Int = getColumnIndexOrThrow(_stmt, "completada")
        val _columnIndexOfTieneFoto: Int = getColumnIndexOrThrow(_stmt, "tieneFoto")
        val _columnIndexOfFotoUri: Int = getColumnIndexOrThrow(_stmt, "fotoUri")
        val _columnIndexOfFechaCreacion: Int = getColumnIndexOrThrow(_stmt, "fechaCreacion")
        val _columnIndexOfSincronizado: Int = getColumnIndexOrThrow(_stmt, "sincronizado")
        val _columnIndexOfIdServidor: Int = getColumnIndexOrThrow(_stmt, "idServidor")
        val _result: MutableList<Nota> = mutableListOf()
        while (_stmt.step()) {
          val _item: Nota
          val _tmpId: Int
          _tmpId = _stmt.getLong(_columnIndexOfId).toInt()
          val _tmpTitulo: String
          _tmpTitulo = _stmt.getText(_columnIndexOfTitulo)
          val _tmpContenido: String
          _tmpContenido = _stmt.getText(_columnIndexOfContenido)
          val _tmpCategoria: String
          _tmpCategoria = _stmt.getText(_columnIndexOfCategoria)
          val _tmpPrioridad: String
          _tmpPrioridad = _stmt.getText(_columnIndexOfPrioridad)
          val _tmpCompletada: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfCompletada).toInt()
          _tmpCompletada = _tmp != 0
          val _tmpTieneFoto: Boolean
          val _tmp_1: Int
          _tmp_1 = _stmt.getLong(_columnIndexOfTieneFoto).toInt()
          _tmpTieneFoto = _tmp_1 != 0
          val _tmpFotoUri: String
          _tmpFotoUri = _stmt.getText(_columnIndexOfFotoUri)
          val _tmpFechaCreacion: Long
          _tmpFechaCreacion = _stmt.getLong(_columnIndexOfFechaCreacion)
          val _tmpSincronizado: Boolean
          val _tmp_2: Int
          _tmp_2 = _stmt.getLong(_columnIndexOfSincronizado).toInt()
          _tmpSincronizado = _tmp_2 != 0
          val _tmpIdServidor: Long?
          if (_stmt.isNull(_columnIndexOfIdServidor)) {
            _tmpIdServidor = null
          } else {
            _tmpIdServidor = _stmt.getLong(_columnIndexOfIdServidor)
          }
          _item =
              Nota(_tmpId,_tmpTitulo,_tmpContenido,_tmpCategoria,_tmpPrioridad,_tmpCompletada,_tmpTieneFoto,_tmpFotoUri,_tmpFechaCreacion,_tmpSincronizado,_tmpIdServidor)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getNotasByPrioridad(prioridad: String): Flow<List<Nota>> {
    val _sql: String = "SELECT * FROM notas WHERE prioridad = ? ORDER BY fechaCreacion DESC"
    return createFlow(__db, false, arrayOf("notas")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, prioridad)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfTitulo: Int = getColumnIndexOrThrow(_stmt, "titulo")
        val _columnIndexOfContenido: Int = getColumnIndexOrThrow(_stmt, "contenido")
        val _columnIndexOfCategoria: Int = getColumnIndexOrThrow(_stmt, "categoria")
        val _columnIndexOfPrioridad: Int = getColumnIndexOrThrow(_stmt, "prioridad")
        val _columnIndexOfCompletada: Int = getColumnIndexOrThrow(_stmt, "completada")
        val _columnIndexOfTieneFoto: Int = getColumnIndexOrThrow(_stmt, "tieneFoto")
        val _columnIndexOfFotoUri: Int = getColumnIndexOrThrow(_stmt, "fotoUri")
        val _columnIndexOfFechaCreacion: Int = getColumnIndexOrThrow(_stmt, "fechaCreacion")
        val _columnIndexOfSincronizado: Int = getColumnIndexOrThrow(_stmt, "sincronizado")
        val _columnIndexOfIdServidor: Int = getColumnIndexOrThrow(_stmt, "idServidor")
        val _result: MutableList<Nota> = mutableListOf()
        while (_stmt.step()) {
          val _item: Nota
          val _tmpId: Int
          _tmpId = _stmt.getLong(_columnIndexOfId).toInt()
          val _tmpTitulo: String
          _tmpTitulo = _stmt.getText(_columnIndexOfTitulo)
          val _tmpContenido: String
          _tmpContenido = _stmt.getText(_columnIndexOfContenido)
          val _tmpCategoria: String
          _tmpCategoria = _stmt.getText(_columnIndexOfCategoria)
          val _tmpPrioridad: String
          _tmpPrioridad = _stmt.getText(_columnIndexOfPrioridad)
          val _tmpCompletada: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfCompletada).toInt()
          _tmpCompletada = _tmp != 0
          val _tmpTieneFoto: Boolean
          val _tmp_1: Int
          _tmp_1 = _stmt.getLong(_columnIndexOfTieneFoto).toInt()
          _tmpTieneFoto = _tmp_1 != 0
          val _tmpFotoUri: String
          _tmpFotoUri = _stmt.getText(_columnIndexOfFotoUri)
          val _tmpFechaCreacion: Long
          _tmpFechaCreacion = _stmt.getLong(_columnIndexOfFechaCreacion)
          val _tmpSincronizado: Boolean
          val _tmp_2: Int
          _tmp_2 = _stmt.getLong(_columnIndexOfSincronizado).toInt()
          _tmpSincronizado = _tmp_2 != 0
          val _tmpIdServidor: Long?
          if (_stmt.isNull(_columnIndexOfIdServidor)) {
            _tmpIdServidor = null
          } else {
            _tmpIdServidor = _stmt.getLong(_columnIndexOfIdServidor)
          }
          _item =
              Nota(_tmpId,_tmpTitulo,_tmpContenido,_tmpCategoria,_tmpPrioridad,_tmpCompletada,_tmpTieneFoto,_tmpFotoUri,_tmpFechaCreacion,_tmpSincronizado,_tmpIdServidor)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getNotasPendientes(): Flow<List<Nota>> {
    val _sql: String = "SELECT * FROM notas WHERE completada = 0 ORDER BY fechaCreacion DESC"
    return createFlow(__db, false, arrayOf("notas")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfTitulo: Int = getColumnIndexOrThrow(_stmt, "titulo")
        val _columnIndexOfContenido: Int = getColumnIndexOrThrow(_stmt, "contenido")
        val _columnIndexOfCategoria: Int = getColumnIndexOrThrow(_stmt, "categoria")
        val _columnIndexOfPrioridad: Int = getColumnIndexOrThrow(_stmt, "prioridad")
        val _columnIndexOfCompletada: Int = getColumnIndexOrThrow(_stmt, "completada")
        val _columnIndexOfTieneFoto: Int = getColumnIndexOrThrow(_stmt, "tieneFoto")
        val _columnIndexOfFotoUri: Int = getColumnIndexOrThrow(_stmt, "fotoUri")
        val _columnIndexOfFechaCreacion: Int = getColumnIndexOrThrow(_stmt, "fechaCreacion")
        val _columnIndexOfSincronizado: Int = getColumnIndexOrThrow(_stmt, "sincronizado")
        val _columnIndexOfIdServidor: Int = getColumnIndexOrThrow(_stmt, "idServidor")
        val _result: MutableList<Nota> = mutableListOf()
        while (_stmt.step()) {
          val _item: Nota
          val _tmpId: Int
          _tmpId = _stmt.getLong(_columnIndexOfId).toInt()
          val _tmpTitulo: String
          _tmpTitulo = _stmt.getText(_columnIndexOfTitulo)
          val _tmpContenido: String
          _tmpContenido = _stmt.getText(_columnIndexOfContenido)
          val _tmpCategoria: String
          _tmpCategoria = _stmt.getText(_columnIndexOfCategoria)
          val _tmpPrioridad: String
          _tmpPrioridad = _stmt.getText(_columnIndexOfPrioridad)
          val _tmpCompletada: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfCompletada).toInt()
          _tmpCompletada = _tmp != 0
          val _tmpTieneFoto: Boolean
          val _tmp_1: Int
          _tmp_1 = _stmt.getLong(_columnIndexOfTieneFoto).toInt()
          _tmpTieneFoto = _tmp_1 != 0
          val _tmpFotoUri: String
          _tmpFotoUri = _stmt.getText(_columnIndexOfFotoUri)
          val _tmpFechaCreacion: Long
          _tmpFechaCreacion = _stmt.getLong(_columnIndexOfFechaCreacion)
          val _tmpSincronizado: Boolean
          val _tmp_2: Int
          _tmp_2 = _stmt.getLong(_columnIndexOfSincronizado).toInt()
          _tmpSincronizado = _tmp_2 != 0
          val _tmpIdServidor: Long?
          if (_stmt.isNull(_columnIndexOfIdServidor)) {
            _tmpIdServidor = null
          } else {
            _tmpIdServidor = _stmt.getLong(_columnIndexOfIdServidor)
          }
          _item =
              Nota(_tmpId,_tmpTitulo,_tmpContenido,_tmpCategoria,_tmpPrioridad,_tmpCompletada,_tmpTieneFoto,_tmpFotoUri,_tmpFechaCreacion,_tmpSincronizado,_tmpIdServidor)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getNotaById(id: Int): Nota? {
    val _sql: String = "SELECT * FROM notas WHERE id = ?"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, id.toLong())
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfTitulo: Int = getColumnIndexOrThrow(_stmt, "titulo")
        val _columnIndexOfContenido: Int = getColumnIndexOrThrow(_stmt, "contenido")
        val _columnIndexOfCategoria: Int = getColumnIndexOrThrow(_stmt, "categoria")
        val _columnIndexOfPrioridad: Int = getColumnIndexOrThrow(_stmt, "prioridad")
        val _columnIndexOfCompletada: Int = getColumnIndexOrThrow(_stmt, "completada")
        val _columnIndexOfTieneFoto: Int = getColumnIndexOrThrow(_stmt, "tieneFoto")
        val _columnIndexOfFotoUri: Int = getColumnIndexOrThrow(_stmt, "fotoUri")
        val _columnIndexOfFechaCreacion: Int = getColumnIndexOrThrow(_stmt, "fechaCreacion")
        val _columnIndexOfSincronizado: Int = getColumnIndexOrThrow(_stmt, "sincronizado")
        val _columnIndexOfIdServidor: Int = getColumnIndexOrThrow(_stmt, "idServidor")
        val _result: Nota?
        if (_stmt.step()) {
          val _tmpId: Int
          _tmpId = _stmt.getLong(_columnIndexOfId).toInt()
          val _tmpTitulo: String
          _tmpTitulo = _stmt.getText(_columnIndexOfTitulo)
          val _tmpContenido: String
          _tmpContenido = _stmt.getText(_columnIndexOfContenido)
          val _tmpCategoria: String
          _tmpCategoria = _stmt.getText(_columnIndexOfCategoria)
          val _tmpPrioridad: String
          _tmpPrioridad = _stmt.getText(_columnIndexOfPrioridad)
          val _tmpCompletada: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfCompletada).toInt()
          _tmpCompletada = _tmp != 0
          val _tmpTieneFoto: Boolean
          val _tmp_1: Int
          _tmp_1 = _stmt.getLong(_columnIndexOfTieneFoto).toInt()
          _tmpTieneFoto = _tmp_1 != 0
          val _tmpFotoUri: String
          _tmpFotoUri = _stmt.getText(_columnIndexOfFotoUri)
          val _tmpFechaCreacion: Long
          _tmpFechaCreacion = _stmt.getLong(_columnIndexOfFechaCreacion)
          val _tmpSincronizado: Boolean
          val _tmp_2: Int
          _tmp_2 = _stmt.getLong(_columnIndexOfSincronizado).toInt()
          _tmpSincronizado = _tmp_2 != 0
          val _tmpIdServidor: Long?
          if (_stmt.isNull(_columnIndexOfIdServidor)) {
            _tmpIdServidor = null
          } else {
            _tmpIdServidor = _stmt.getLong(_columnIndexOfIdServidor)
          }
          _result =
              Nota(_tmpId,_tmpTitulo,_tmpContenido,_tmpCategoria,_tmpPrioridad,_tmpCompletada,_tmpTieneFoto,_tmpFotoUri,_tmpFechaCreacion,_tmpSincronizado,_tmpIdServidor)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getCount(): Int {
    val _sql: String = "SELECT COUNT(*) FROM notas"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _result: Int
        if (_stmt.step()) {
          val _tmp: Int
          _tmp = _stmt.getLong(0).toInt()
          _result = _tmp
        } else {
          _result = 0
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getCountByPrioridad(prioridad: String): Int {
    val _sql: String = "SELECT COUNT(*) FROM notas WHERE prioridad = ?"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, prioridad)
        val _result: Int
        if (_stmt.step()) {
          val _tmp: Int
          _tmp = _stmt.getLong(0).toInt()
          _result = _tmp
        } else {
          _result = 0
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun marcarCompletada(id: Int, completada: Boolean) {
    val _sql: String = "UPDATE notas SET completada = ? WHERE id = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        val _tmp: Int = if (completada) 1 else 0
        _stmt.bindLong(_argIndex, _tmp.toLong())
        _argIndex = 2
        _stmt.bindLong(_argIndex, id.toLong())
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun deleteCompletadas() {
    val _sql: String = "DELETE FROM notas WHERE completada = 1"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public companion object {
    public fun getRequiredConverters(): List<KClass<*>> = emptyList()
  }
}
