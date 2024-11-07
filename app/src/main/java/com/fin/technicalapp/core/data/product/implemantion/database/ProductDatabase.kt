package com.fin.technicalapp.core.data.product.implemantion.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.fin.technicalapp.core.data.product.implemantion.database.dao.CartDao
import com.fin.technicalapp.core.data.product.implemantion.database.entity.CartItemEntity

@Database(
    entities = [
        CartItemEntity::class,
    ],
    version = 1,
)
abstract class ProductDatabase: RoomDatabase() {
    abstract fun cartDao(): CartDao

    companion object {
        private const val DATABASE_NAME = "product.db"

        @Volatile
        private var INSTANCE: ProductDatabase? = null

        @Synchronized
        fun getInstance(context: Context): ProductDatabase {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    ProductDatabase::class.java,
                    DATABASE_NAME
                )
                    .build()
            }
            return INSTANCE as ProductDatabase
        }
    }
}