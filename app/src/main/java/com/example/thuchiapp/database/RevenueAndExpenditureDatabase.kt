package com.example.thuchiapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.thuchiapp.R
import com.example.thuchiapp.dao.PendingJarsDao
import com.example.thuchiapp.dao.SubcategoriesDao
import com.example.thuchiapp.entity.PendingJars
import com.example.thuchiapp.entity.Subcategories
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Database(entities = [PendingJars::class, Subcategories::class], version = 1, exportSchema = false)
abstract class RevenueAndExpenditureDatabase : RoomDatabase() {
    abstract fun pendingJarsDao(): PendingJarsDao
    abstract fun subcategoriesDao(): SubcategoriesDao

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
                            insertSampleData(database.pendingJarsDao(), database.subcategoriesDao(), context)
                        }
                        initialized = true
                    }
                }
            }
        }

        private suspend fun insertSampleData(pendingJarsDao: PendingJarsDao,subcategoriesDao: SubcategoriesDao, context: Context) {

            val pendingJarsList = listOf(
                PendingJars(title = context.getString(R.string.eat_and_drink), amount = "", iconResource = R.drawable.ic_cake_30, type = "Spending", percent = 30, order = 1),
                PendingJars(title = context.getString(R.string.living_expenses), amount = "", iconResource = R.drawable.ic_living_room_30, type = "Spending", percent = 15, order = 2),
                PendingJars(title = context.getString(R.string.transportation), amount = "", iconResource = R.drawable.ic_car_30, type = "Spending", percent = 5, order = 3),
                PendingJars(title = context.getString(R.string.clothing), amount = "", iconResource = R.drawable.ic_clothes_30, type = "Spending", percent = 5, order = 4),
                PendingJars(title = context.getString(R.string.entertainment), amount = "", iconResource = R.drawable.ic_vacation_30, type = "Spending", percent = 5, order = 5),
                PendingJars(title = context.getString(R.string.children_expenses), amount = "", iconResource = R.drawable.ic_children_30, type = "Spending", percent = 0, order = 6),
                PendingJars(title = context.getString(R.string.gifts_and_events), amount = "", iconResource = R.drawable.ic_gift_30, type = "Spending", percent = 5, order = 7),
                PendingJars(title = context.getString(R.string.housing), amount = "", iconResource = R.drawable.ic_house_30, type = "Spending", percent = 20, order = 8),
                PendingJars(title = context.getString(R.string.healthcare), amount = "", iconResource = R.drawable.ic_health_30, type = "Spending", percent = 5, order = 9),
                PendingJars(title = context.getString(R.string.personal_expenses), amount = "", iconResource = R.drawable.ic_education_30, type = "Spending", percent = 5, order = 10),
                PendingJars(title = context.getString(R.string.other_expenses), amount = "", iconResource = R.drawable.ic_violin_30, type = "Spending", percent = 5, order = 11),

                PendingJars(title = context.getString(R.string.salary), amount = "", iconResource = R.drawable.ic_benefit_30, type = "Income", percent = 0, order = 12),
                PendingJars(title = context.getString(R.string.bonus), amount = "", iconResource = R.drawable.ic_reward_30, type = "Income", percent = 0, order = 13),
                PendingJars(title = context.getString(R.string.part_time_job), amount = "", iconResource = R.drawable.ic_office_30, type = "Income", percent = 0, order = 14),
                PendingJars(title = context.getString(R.string.business), amount = "", iconResource = R.drawable.ic_stock_share_30, type = "Income", percent = 0, order = 15),
                PendingJars(title = context.getString(R.string.investment), amount = "", iconResource = R.drawable.ic_invest_30, type = "Income", percent = 0, order = 16),
                PendingJars(title = context.getString(R.string.gift), amount = "", iconResource = R.drawable.ic_payroll_30, type = "Income", percent = 0, order = 17),
                PendingJars(title = context.getString(R.string.interest), amount = "", iconResource = R.drawable.ic_growing_money_30, type = "Income", percent = 0, order = 18),
                PendingJars(title = context.getString(R.string.other_expenses), amount = "", iconResource = R.drawable.ic_money_30, type = "Income", percent = 0, order = 19),
            )

            val subcategoriesList = listOf(
                Subcategories(subcategory = context.getString(R.string.have_breakfast), order = 1, categoryCode = 1),
                Subcategories(subcategory = context.getString(R.string.having_lunch), order = 2, categoryCode = 1),
                Subcategories(subcategory = context.getString(R.string.have_dinner), order = 3, categoryCode = 1),
                Subcategories(subcategory = context.getString(R.string.snack), order = 4, categoryCode = 1),
                Subcategories(subcategory = context.getString(R.string.coffee), order = 5, categoryCode = 1),
                Subcategories(subcategory = context.getString(R.string.party), order = 6, categoryCode = 1),
                Subcategories(subcategory = context.getString(R.string.other_expenses), order = 7, categoryCode = 1),

                Subcategories(subcategory = context.getString(R.string.television), order = 1, categoryCode = 2),
                Subcategories(subcategory = context.getString(R.string.internet), order = 2, categoryCode = 2),
                Subcategories(subcategory = context.getString(R.string.water), order = 3, categoryCode = 2),
                Subcategories(subcategory = context.getString(R.string.electricity), order = 4, categoryCode = 2),
                Subcategories(subcategory = context.getString(R.string.phone), order = 5, categoryCode = 2),
                Subcategories(subcategory = context.getString(R.string.market), order = 6, categoryCode = 2),
                Subcategories(subcategory = context.getString(R.string.gas), order = 7, categoryCode = 2),
                Subcategories(subcategory = context.getString(R.string.other_expenses), order = 8, categoryCode = 2),

                Subcategories(subcategory = context.getString(R.string.fuel), order = 1, categoryCode = 3),
                Subcategories(subcategory = context.getString(R.string.taxi), order = 2, categoryCode = 3),
                Subcategories(subcategory = context.getString(R.string.car_insurance), order = 3, categoryCode = 3),
                Subcategories(subcategory = context.getString(R.string.car_repair), order = 4, categoryCode = 3),
                Subcategories(subcategory = context.getString(R.string.tax_fees), order = 5, categoryCode = 3),
                Subcategories(subcategory = context.getString(R.string.other_expenses), order = 6, categoryCode = 3),

                Subcategories(subcategory = context.getString(R.string.clothes), order = 1, categoryCode = 4),
                Subcategories(subcategory = context.getString(R.string.shoes), order = 2, categoryCode = 4),
                Subcategories(subcategory = context.getString(R.string.accessory), order = 3, categoryCode = 4),
                Subcategories(subcategory = context.getString(R.string.other_expenses), order = 4, categoryCode = 4),

                Subcategories(subcategory = context.getString(R.string.tourism), order = 1, categoryCode = 5),
                Subcategories(subcategory = context.getString(R.string.beautify), order = 2, categoryCode = 5),
                Subcategories(subcategory = context.getString(R.string.cosmetics), order = 3, categoryCode = 5),
                Subcategories(subcategory = context.getString(R.string.entertainment), order = 4, categoryCode = 5),
                Subcategories(subcategory = context.getString(R.string.watch_a_movie), order = 5, categoryCode = 5),
                Subcategories(subcategory = context.getString(R.string.other_expenses), order = 6, categoryCode = 5),

                Subcategories(subcategory = context.getString(R.string.tuition), order = 1, categoryCode = 6),
                Subcategories(subcategory = context.getString(R.string.school_tools), order = 2, categoryCode = 6),
                Subcategories(subcategory = context.getString(R.string.milk_diapers), order = 3, categoryCode = 6),
                Subcategories(subcategory = context.getString(R.string.eat_out), order = 4, categoryCode = 6),
                Subcategories(subcategory = context.getString(R.string.skin), order = 5, categoryCode = 6),
                Subcategories(subcategory = context.getString(R.string.health), order = 6, categoryCode = 6),
                Subcategories(subcategory = context.getString(R.string.miscellaneous_expenses), order = 7, categoryCode = 6),
                Subcategories(subcategory = context.getString(R.string.toy), order = 8, categoryCode = 6),
                Subcategories(subcategory = context.getString(R.string.other_expenses), order = 9, categoryCode = 6),

                Subcategories(subcategory = context.getString(R.string.donate), order = 1, categoryCode = 7),
                Subcategories(subcategory = context.getString(R.string.wedding), order = 2, categoryCode = 7),
                Subcategories(subcategory = context.getString(R.string.funeral), order = 3, categoryCode = 7),
                Subcategories(subcategory = context.getString(R.string.visit), order = 4, categoryCode = 7),
                Subcategories(subcategory = context.getString(R.string.charity), order = 5, categoryCode = 7),
                Subcategories(subcategory = context.getString(R.string.other_expenses), order = 6, categoryCode = 7),

                Subcategories(subcategory = context.getString(R.string.shopping), order = 1, categoryCode = 8),
                Subcategories(subcategory = context.getString(R.string.fix), order = 2, categoryCode = 8),
                Subcategories(subcategory = context.getString(R.string.rent_a_house), order = 3, categoryCode = 8),
                Subcategories(subcategory = context.getString(R.string.house_help), order = 4, categoryCode = 8),
                Subcategories(subcategory = context.getString(R.string.other_expenses), order = 5, categoryCode = 8),

                Subcategories(subcategory = context.getString(R.string.medical_examination), order = 1, categoryCode = 9),
                Subcategories(subcategory = context.getString(R.string.medication), order = 2, categoryCode = 9),
                Subcategories(subcategory = context.getString(R.string.sports), order = 3, categoryCode = 9),
                Subcategories(subcategory = context.getString(R.string.insurance), order = 4, categoryCode = 9),
                Subcategories(subcategory = context.getString(R.string.other_expenses), order = 5, categoryCode = 9),

                Subcategories(subcategory = context.getString(R.string.exchange), order = 1, categoryCode = 10),
                Subcategories(subcategory = context.getString(R.string.study), order = 2, categoryCode = 10),
                Subcategories(subcategory = context.getString(R.string.other_expenses), order = 3, categoryCode = 10),

                Subcategories(subcategory = context.getString(R.string.lending), order = 1, categoryCode = 11),
                Subcategories(subcategory = context.getString(R.string.invest), order = 2, categoryCode = 11),
                Subcategories(subcategory = context.getString(R.string.trading), order = 3, categoryCode = 11),
                Subcategories(subcategory = context.getString(R.string.surcharge), order = 4, categoryCode = 11),
                Subcategories(subcategory = context.getString(R.string.other_expenses), order = 5, categoryCode = 11),
            )

            withContext(Dispatchers.IO) {
                pendingJarsDao.insertPendingJars(pendingJarsList)
                subcategoriesDao.insertSubcategories(subcategoriesList)
            }
        }
    }
}
