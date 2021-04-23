package ru.dimagor555.factcard.data

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.dimagor555.factcard.data.factcard.FactCard
import ru.dimagor555.factcard.data.factcard.FactCardDao
import ru.dimagor555.factcard.data.line.Line
import ru.dimagor555.factcard.data.line.LineDao
import javax.inject.Inject

class FileCache @Inject constructor(
    private val factCardDao: FactCardDao,
    private val lineDao: LineDao,
) {
    val factCards = ArrayList<FactCard>()
    val lines = ArrayList<Line>()

    private val factCardsStates: MutableMap<Long, CacheState> = HashMap()
    private val linesStates: MutableMap<Line, CacheState> = HashMap()

    suspend fun loadFileById(fileId: Long) {
        factCards.clear()
        lines.clear()

        factCards += factCardDao.getFactCardsByFileId(fileId)
        lines += lineDao.getLinesByFileId(fileId)

        factCardsStates.clear()
        linesStates.clear()

        factCards.forEach { factCardsStates += it.id to CacheState.NOT_CHANGED }
        lines.forEach { linesStates += it to CacheState.NOT_CHANGED }

        coroutineScope {
            launch {
                while (true) {
                    delay(SYNCHRONIZATION_INTERVAL)
                    synchronizeCacheWithDb()
                }
            }
        }
    }

    fun onFactCardDeleted(card: FactCard) {
        factCards -= card
        factCardsStates[card.id] = CacheState.DELETED
    }

    fun onFactCardChanged(card: FactCard) {
        factCards.map {
            if (it.id == card.id) card
            else it
        }
        factCardsStates[card.id] = CacheState.CHANGED
    }

    fun onLineDeleted(line: Line) {
        lines -= line
        linesStates[line] = CacheState.DELETED
    }

    private suspend fun synchronizeCacheWithDb() {
        factCardsStates.filterValues { it == CacheState.DELETED }
            .forEach { factCardDao.deleteFactCardById(it.key) }

        factCardsStates.filterValues { it == CacheState.CHANGED }
            .forEach { state ->
                val changedCard = factCards.find { it.id == state.key }
                changedCard?.let { factCardDao.insertOrUpdateFactCard(it) }
            }

        linesStates.filterValues { it == CacheState.DELETED }
            .forEach { lineDao.deleteLine(it.key) }
    }

    companion object {
        const val SYNCHRONIZATION_INTERVAL = 5000L
    }

    enum class CacheState {
        NOT_CHANGED, CHANGED, DELETED
    }
}