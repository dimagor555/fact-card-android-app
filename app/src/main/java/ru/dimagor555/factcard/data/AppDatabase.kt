package ru.dimagor555.factcard.data

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.dimagor555.factcard.data.factcard.FactCard
import ru.dimagor555.factcard.data.factcard.FactCardDao
import ru.dimagor555.factcard.data.file.File
import ru.dimagor555.factcard.data.file.FileDao
import ru.dimagor555.factcard.data.line.Line
import ru.dimagor555.factcard.data.line.LineDao

@Database(
    entities = [
        File::class,
        FactCard::class,
        Line::class,
    ],
    version = 4,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun fileDao(): FileDao
    abstract fun factCardDao(): FactCardDao
    abstract fun lineDao(): LineDao

    companion object {
        const val DB_NAME = "fact_cards.db"
    }
}