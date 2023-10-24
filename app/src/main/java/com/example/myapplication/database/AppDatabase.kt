package com.example.myapplication.database

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.myapplication.entities.dao.CinemaDao
import com.example.myapplication.entities.dao.OrderDao
import com.example.myapplication.entities.dao.OrderSessionCrossRefDao
import com.example.myapplication.entities.dao.SessionDao
import com.example.myapplication.entities.dao.UserDao
import com.example.myapplication.entities.dao.UserSessionCrossRefDao
import com.example.myapplication.entities.model.Cinema
import com.example.myapplication.entities.model.LocalDateTimeConverter
import com.example.myapplication.entities.model.Order
import com.example.myapplication.entities.model.OrderSessionCrossRef
import com.example.myapplication.entities.model.Session
import com.example.myapplication.entities.model.User
import com.example.myapplication.entities.model.UserSessionCrossRef
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDateTime
import java.io.ByteArrayOutputStream

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
                val session1 = Session(1, LocalDateTime.now(), cinema1.uid, 120)
                val session2 = Session(2, LocalDateTime.now(), cinema2.uid, 110)
                val session3 = Session(3, LocalDateTime.now(), cinema3.uid, 100)
                sessionDao.insert(session1)
                sessionDao.insert(session2)
                sessionDao.insert(session3)
                // OrderSessionCrossRef для связи заказов с сеансами
                val orderSessionCrossRefDao = database.orderSessionCrossRefDao()
                if (session1.uid != null && session2.uid != null && session3.uid != null) {
                    val orderSessionCrossRef1 = OrderSessionCrossRef(order1.uid, session3.uid, 5)
                    val orderSessionCrossRef2 = OrderSessionCrossRef(order1.uid, session2.uid, 10)
                    val orderSessionCrossRef3 = OrderSessionCrossRef(order2.uid, session2.uid, 6)
                    val orderSessionCrossRef4 = OrderSessionCrossRef(order3.uid, session1.uid, 10)
                    val orderSessionCrossRef5 = OrderSessionCrossRef(order3.uid, session3.uid, 16)
                    val orderSessionCrossRef6 = OrderSessionCrossRef(order4.uid, session3.uid, 2)
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