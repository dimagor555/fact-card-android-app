package ru.dimagor555.factcard.data

import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.dimagor555.factcard.data.factcard.FactCard
import ru.dimagor555.factcard.data.factcard.FactCardDao
import ru.dimagor555.factcard.data.line.Line
import ru.dimagor555.factcard.data.line.LineDao
import javax.inject.Inject
import kotlin.properties.Delegates
import kotlin.random.Random

@ViewModelScoped
class FileCache @Inject constructor(
    private val factCardDao: FactCardDao,
    private val lineDao: LineDao,
) {
    var currFileId by Delegates.notNull<Long>()

    val factCards = ArrayList<FactCard>()
    val lines = ArrayList<Line>()

    private val factCardsStates: MutableMap<Long, CacheState> = HashMap()
    private val linesStates: MutableMap<Line, CacheState> = HashMap()

    suspend fun loadFileById(fileId: Long) {
        currFileId = fileId

        factCards.clear()
        lines.clear()

        factCards += factCardDao.getFactCardsByFileId(fileId)
        lines += lineDao.getLinesByFileId(fileId)

        factCardsStates.clear()
        linesStates.clear()

        factCards.forEach { factCardsStates += it.idFactCard to CacheState.NOT_CHANGED }
        lines.forEach { linesStates += it to CacheState.NOT_CHANGED }

        with(CoroutineScope(currentCoroutineContext())) {
            launch {
                while (true) {
                    delay(SYNCHRONIZATION_INTERVAL)
                    synchronizeCacheWithDb()
                }
            }
        }
    }

    fun onFactCardCreate(x: Int, y: Int) {
        val card = FactCard(
            idFactCard = generateRandomUniqueId(),
            fileId = currFileId,
            positionX = x,
            positionY = y,
        )
        factCards += card
        factCardsStates[card.idFactCard] = CacheState.CHANGED
    }

    private fun generateRandomUniqueId(): Long {
        val random = Random(System.currentTimeMillis())
        while (true) {
            val id = random.nextLong()
            if (!factCardsStates.containsKey(id)) {
                return id
            }
        }
    }

    fun onFactCardDeleted(card: FactCard) {
        factCards -= card
        factCardsStates[card.idFactCard] = CacheState.DELETED

        linesStates.filter { it.key.firstCardId == card.idFactCard || it.key.secondCardId == card.idFactCard }
            .forEach {
                lines -= it.key
                linesStates[it.key] = CacheState.DELETED
            }
    }

    fun onFactCardChanged(card: FactCard) {
        factCardsStates[card.idFactCard] = CacheState.CHANGED
    }

    fun onLineCreated(
        firstCardId: Long, secondCardId: Long,
        firstCardPoint: Int, secondCardPoint: Int
    ) {
        if (!lineAlreadyExists(firstCardId, secondCardId, firstCardPoint, secondCardPoint)) {
            val createdLine = Line(
                                    fileId = currFileId,
                                    firstCardId = firstCardId,
                                    secondCardId = secondCardId,
                                    firstPointId = firstCardPoint,
                                    secondPointId = secondCardPoint
                              )
            lines += createdLine
            linesStates[createdLine] = CacheState.CHANGED
        }
    }

    private fun lineAlreadyExists(
        firstCardId: Long,
        secondCardId: Long,
        firstCardPoint: Int,
        secondCardPoint: Int
    ): Boolean {
        lines.find {
            areCardIdsSame(it, firstCardId, secondCardId) &&
                    arePointIdsTheSame(it, firstCardPoint, secondCardPoint)
        }?.let { return true }

        return false
    }

    private fun areCardIdsSame(line: Line, firstCardId: Long, secondCardId: Long) =
        line.firstCardId == firstCardId && line.secondCardId == secondCardId ||
                line.firstCardId == secondCardId && line.secondCardId == firstCardId

    private fun arePointIdsTheSame(line: Line, firstCardPoint: Int, secondCardPoint: Int) =
        line.firstPointId == firstCardPoint && line.secondPointId == secondCardPoint ||
                line.firstPointId == secondCardPoint && line.secondPointId == firstCardPoint

    fun onLineDeleted(line: Line) {
        lines -= line
        linesStates[line] = CacheState.DELETED
    }

    suspend fun synchronizeCacheWithDb() {
        factCardsStates.filterValues { it == CacheState.DELETED }
            .forEach { factCardDao.deleteFactCardById(it.key) }

        factCardsStates.filterValues { it == CacheState.CHANGED }
            .forEach { state ->
                val changedCard = factCards.find { it.idFactCard == state.key }
                changedCard?.let { factCardDao.insertOrUpdateFactCard(it) }
            }

        linesStates.filterValues { it == CacheState.DELETED }
            .forEach { lineDao.deleteLine(it.key) }

        linesStates.filterValues { it == CacheState.CHANGED }
            .forEach { lineDao.insertOrUpdateLine(it.key) }

        factCardsStates.iterator().forEach { it.setValue(CacheState.NOT_CHANGED) }
        linesStates.iterator().forEach { it.setValue(CacheState.NOT_CHANGED) }
    }

    companion object {
        const val SYNCHRONIZATION_INTERVAL = 1000L
    }

    enum class CacheState {
        NOT_CHANGED, CHANGED, DELETED
    }
}