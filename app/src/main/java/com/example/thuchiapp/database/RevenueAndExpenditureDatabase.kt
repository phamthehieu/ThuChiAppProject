package com.example.thuchiapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.thuchiapp.R
import com.example.thuchiapp.dao.PendingJarsDao
import com.example.thuchiapp.entity.PendingJars
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Database(entities = [PendingJars::class], version = 1, exportSchema = false)
abstract class RevenueAndExpenditureDatabase : RoomDatabase() {
    abstract fun pendingJarsDao(): PendingJarsDao

    companion object {
        private var INSTANCE: RevenueAndExpenditureDatabase? = null
        private var initialized = false

        fun getDataBase(context: Context, scope: CoroutineScope): RevenueAndExpenditureDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    RevenueAndExpenditureDatabase::class.java,
                    "revenue_and_expenditure_database"
                )
                    .addCallback(DatabaseCallback(scope, context))
                    .fallbackToDestructiveMigration()  // Thêm để đảm bảo tạo lại DB khi cần
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback(
            private val scope: CoroutineScope,
            private val context: Context // Thêm context để lấy chuỗi từ resource
        ) : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    if (!initialized) {
                        scope.launch(Dispatchers.IO) {
                            insertSampleData(database.pendingJarsDao(), context)
                        }
                        initialized = true
                    }
                }
            }
        }

        private suspend fun insertSampleData(pendingJarsDao: PendingJarsDao, context: Context) {

            val pendingJarsList = listOf(
                PendingJars(title = context.getString(R.string.eat_and_drink), amount = "", iconResource = R.drawable.ic_cake_30, type = "Spending"),
                PendingJars(title = context.getString(R.string.living_expenses), amount = "", iconResource = R.drawable.ic_living_room_30, type = "Spending"),
                PendingJars(title = context.getString(R.string.transportation), amount = "", iconResource = R.drawable.ic_car_30, type = "Spending"),
                PendingJars(title = context.getString(R.string.clothing), amount = "", iconResource = R.drawable.ic_clothes_30, type = "Spending"),
                PendingJars(title = context.getString(R.string.entertainment), amount = "", iconResource = R.drawable.ic_vacation_30, type = "Spending"),
                PendingJars(title = context.getString(R.string.children_expenses), amount = "", iconResource = R.drawable.ic_children_30, type = "Spending"),
                PendingJars(title = context.getString(R.string.gifts_and_events), amount = "", iconResource = R.drawable.ic_gift_30, type = "Spending"),
                PendingJars(title = context.getString(R.string.housing), amount = "", iconResource = R.drawable.ic_house_30, type = "Spending"),
                PendingJars(title = context.getString(R.string.healthcare), amount = "", iconResource = R.drawable.ic_health_30, type = "Spending"),
                PendingJars(title = context.getString(R.string.personal_expenses), amount = "", iconResource = R.drawable.ic_education_30, type = "Spending"),
                PendingJars(title = context.getString(R.string.other_expenses), amount = "", iconResource = R.drawable.ic_violin_30, type = "Spending"),
                PendingJars(title = context.getString(R.string.salary), amount = "", iconResource = R.drawable.ic_benefit_30, type = "Income"),
                PendingJars(title = context.getString(R.string.bonus), amount = "", iconResource = R.drawable.ic_reward_30, type = "Income"),
                PendingJars(title = context.getString(R.string.part_time_job), amount = "", iconResource = R.drawable.ic_office_30, type = "Income"),
                PendingJars(title = context.getString(R.string.business), amount = "", iconResource = R.drawable.ic_stock_share_30, type = "Income"),
                PendingJars(title = context.getString(R.string.investment), amount = "", iconResource = R.drawable.ic_invest_30, type = "Income"),
                PendingJars(title = context.getString(R.string.gift), amount = "", iconResource = R.drawable.ic_payroll_30, type = "Income"),
                PendingJars(title = context.getString(R.string.interest), amount = "", iconResource = R.drawable.ic_growing_money_30, type = "Income"),
                PendingJars(title = context.getString(R.string.other_expenses), amount = "", iconResource = R.drawable.ic_money_30, type = "Income"),
            )

            withContext(Dispatchers.IO) {
                pendingJarsDao.insertPendingJars(pendingJarsList)
            }
        }
    }
}
