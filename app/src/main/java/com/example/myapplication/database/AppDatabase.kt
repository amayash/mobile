package com.example.myapplication.database

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import androidx.room.Database
import androidx.room.TypeConverters
import com.example.myapplication.database.entities.dao.CinemaDao
import com.example.myapplication.database.entities.dao.OrderDao
import com.example.myapplication.database.entities.dao.OrderSessionCrossRefDao
import com.example.myapplication.database.entities.dao.SessionDao
import com.example.myapplication.database.entities.dao.UserDao
import com.example.myapplication.database.entities.dao.UserSessionCrossRefDao
import com.example.myapplication.database.entities.model.Cinema
import com.example.myapplication.database.entities.model.LocalDateTimeConverter
import com.example.myapplication.database.entities.model.Order
import com.example.myapplication.database.entities.model.OrderSessionCrossRef
import com.example.myapplication.database.entities.model.Session
import com.example.myapplication.database.entities.model.User
import com.example.myapplication.database.entities.model.UserSessionCrossRef
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDateTime
import java.io.ByteArrayOutputStream
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [Cinema::class, Session::class, Order::class,
        OrderSessionCrossRef::class, User::class, UserSessionCrossRef::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(LocalDateTimeConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cinemaDao(): CinemaDao
    abstract fun sessionDao(): SessionDao
    abstract fun orderDao(): OrderDao
    abstract fun orderSessionCrossRefDao(): OrderSessionCrossRefDao
    abstract fun userDao(): UserDao
    abstract fun userSessionCrossRefDao(): UserSessionCrossRefDao

    companion object {
        private const val DB_NAME: String = "pmy-db"

        @Volatile
        private var INSTANCE: AppDatabase? = null

        private suspend fun populateDatabase() {
            INSTANCE?.let { database ->
                // Users
                val userDao = database.userDao()
                val user1 = User(1, "Login", "password")
                val user2 = User(2, "Login123", "password123")
                userDao.insert(user1)
                userDao.insert(user2)
                // Cinemas
                val cinemaDao = database.cinemaDao()
                val cinema1 = Cinema(1, "BLUE 1", "Desc1", createColoredImage(Color.BLUE), 2023)
                val cinema2 = Cinema(2, "GREEN 2", "Desc2", createColoredImage(Color.GREEN), 2023)
                val cinema3 = Cinema(3, "RED 3", "Desc3", createColoredImage(Color.RED), 2023)
                cinemaDao.insert(cinema1)
                cinemaDao.insert(cinema2)
                cinemaDao.insert(cinema3)
                // Orders
                val orderDao = database.orderDao()
                val order1 = Order(1, 1)
                val order2 = Order(2, 1)
                val order3 = Order(3, 1)
                val order4 = Order(4, 1)
                orderDao.insert(order1)
                orderDao.insert(order2)
                orderDao.insert(order3)
                orderDao.insert(order4)
                // Sessions
                val sessionDao = database.sessionDao()
                val session1 = Session(1, LocalDateTime.now(), 150.0, 120, cinema1.uid)
                val session2 = Session(2, LocalDateTime.now(), 200.0, 110, cinema2.uid)
                val session3 = Session(3, LocalDateTime.now(), 300.0, 100, cinema3.uid)
                val session4 = Session(4, LocalDateTime.now(), 450.0, 200, cinema1.uid)
                sessionDao.insert(session1)
                sessionDao.insert(session2)
                sessionDao.insert(session3)
                sessionDao.insert(session4)
                // OrderSessionCrossRef для связи заказов с сеансами
                val orderSessionCrossRefDao = database.orderSessionCrossRefDao()
                if (session1.uid != null && session2.uid != null && session3.uid != null) {
                    val orderSessionCrossRef1 = OrderSessionCrossRef(order1.uid, session3.uid, 150.0, 5)
                    val orderSessionCrossRef2 = OrderSessionCrossRef(order1.uid, session2.uid, 300.0, 10)
                    val orderSessionCrossRef3 = OrderSessionCrossRef(order2.uid, session2.uid, 350.0, 6)
                    val orderSessionCrossRef4 = OrderSessionCrossRef(order3.uid, session1.uid, 250.0, 10)
                    val orderSessionCrossRef5 = OrderSessionCrossRef(order3.uid, session3.uid, 150.0, 16)
                    val orderSessionCrossRef6 = OrderSessionCrossRef(order4.uid, session3.uid, 150.0, 2)
                    orderSessionCrossRefDao.insert(orderSessionCrossRef1)
                    orderSessionCrossRefDao.insert(orderSessionCrossRef2)
                    orderSessionCrossRefDao.insert(orderSessionCrossRef3)
                    orderSessionCrossRefDao.insert(orderSessionCrossRef4)
                    orderSessionCrossRefDao.insert(orderSessionCrossRef5)
                    orderSessionCrossRefDao.insert(orderSessionCrossRef6)
                }
                // UserSessions
                val userSessionCrossRefDao = database.userSessionCrossRefDao()
                val userSessionCrossRef1 = UserSessionCrossRef(1, 1, 5)
                val userSessionCrossRef2 = UserSessionCrossRef(1, 3, 15)
                userSessionCrossRefDao.insert(userSessionCrossRef1)
                userSessionCrossRefDao.insert(userSessionCrossRef2)
            }
        }

        fun getInstance(appContext: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    appContext,
                    AppDatabase::class.java,
                    DB_NAME
                )
                    .addCallback(object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            CoroutineScope(Dispatchers.IO).launch {
                                populateDatabase()
                            }
                        }
                    })
                    .build()
                    .also { INSTANCE = it }
            }
        }

        private fun createColoredImage(color: Int): ByteArray {
            val width = 100
            val height = 100

            val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            bmp.eraseColor(color)

            val stream = ByteArrayOutputStream()
            bmp.compress(Bitmap.CompressFormat.PNG, 100, stream)

            return stream.toByteArray()
        }
    }
}