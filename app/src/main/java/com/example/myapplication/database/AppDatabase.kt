package com.example.myapplication.database

import android.content.Context
import android.graphics.Bitmap
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
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
import com.example.myapplication.database.remotekeys.dao.RemoteKeysDao
import com.example.myapplication.database.remotekeys.model.RemoteKeys
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDateTime
import java.io.ByteArrayOutputStream

@Database(
    entities = [
        Cinema::class,
        Session::class,
        Order::class,
        OrderSessionCrossRef::class,
        User::class,
        UserSessionCrossRef::class,
        RemoteKeys::class
    ],
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
    abstract fun remoteKeysDao(): RemoteKeysDao

    companion object {
        private const val DB_NAME: String = "pmy-db"

        @Volatile
        private var INSTANCE: AppDatabase? = null

        private suspend fun populateDatabase() {
            INSTANCE?.let { database ->
                // Users
                val userDao = database.userDao()
                val user1 = User(1, "login", "password")
                userDao.insert(user1)
                /*// Cinemas
                val cinemaDao = database.cinemaDao()
                val cinema1 =
                    Cinema(1, "a", "Desc1", createColoredImage(android.graphics.Color.BLUE), 2023)
                val cinema2 =
                    Cinema(2, "b", "Desc2", createColoredImage(android.graphics.Color.GREEN), 2023)
                val cinema3 =
                    Cinema(3, "c", "Desc3", createColoredImage(android.graphics.Color.RED), 2023)
                val cinema4 =
                    Cinema(4, "d", "Desc4", createColoredImage(android.graphics.Color.CYAN), 2023)
                cinemaDao.insert(cinema1)
                cinemaDao.insert(cinema2)
                cinemaDao.insert(cinema3)
                cinemaDao.insert(cinema4)

                for (i in 5..20) {
                    val cinema = Cinema(
                        uid = i,
                        name = generateCinemaName(i),
                        description = "Description $i",
                        image = createColoredImage(getRandomColorInt()),
                        year = 2023
                    )
                    cinemaDao.insert(cinema)
                }

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
                    val orderSessionCrossRef1 =
                        OrderSessionCrossRef(order1.uid, session3.uid, 150.0, 5)
                    val orderSessionCrossRef2 =
                        OrderSessionCrossRef(order1.uid, session2.uid, 300.0, 10)
                    val orderSessionCrossRef3 =
                        OrderSessionCrossRef(order2.uid, session2.uid, 350.0, 6)
                    val orderSessionCrossRef4 =
                        OrderSessionCrossRef(order3.uid, session1.uid, 250.0, 10)
                    val orderSessionCrossRef5 =
                        OrderSessionCrossRef(order3.uid, session3.uid, 150.0, 16)
                    val orderSessionCrossRef6 =
                        OrderSessionCrossRef(order4.uid, session3.uid, 150.0, 2)
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
                userSessionCrossRefDao.insert(userSessionCrossRef2)*/
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

        private fun getRandomColorInt(): Int {
            val red = (0..255).random()
            val green = (0..255).random()
            val blue = (0..255).random()
            return (0xFF shl 24) or (red shl 16) or (green shl 8) or blue
        }

        private fun generateCinemaName(index: Int): String {
            val base = 'a'.code
            val alphabetSize = 26
            val sb = StringBuilder()
            var remainder = index
            do {
                val letter = (remainder % alphabetSize + base).toChar()
                sb.insert(0, letter)
                remainder /= alphabetSize
            } while (remainder > 0)
            return sb.toString()
        }
    }
}