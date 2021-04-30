package ru.dimagor555.factcard.ui.drawfile.canvas.render

object FactCardRenderModel {
    const val WIDTH = 400
    const val HEIGHT = 100
    const val BORDER_WIDTH = 5F
    const val CARD_BORDER_RADIUS = 30F

    const val TEXT_WIDTH = WIDTH - CARD_BORDER_RADIUS * 2
    const val MAX_TEXT_HEIGHT = HEIGHT - BORDER_WIDTH * 2

    const val POINT_SIZE = 20
    val POINTS: Array<Pair<Int, Int>> = arrayOf(
        0 to 0,
        1 * WIDTH / 4 - POINT_SIZE / 2 to 0,
        2 * WIDTH / 4 - POINT_SIZE / 2 to 0,
        3 * WIDTH / 4 - POINT_SIZE / 2 to 0,
        WIDTH - POINT_SIZE to 0,

        WIDTH - POINT_SIZE to HEIGHT / 2 - POINT_SIZE / 2,
        WIDTH - POINT_SIZE to HEIGHT - POINT_SIZE,

        3 * WIDTH / 4 - POINT_SIZE / 2 to HEIGHT - POINT_SIZE,
        2 * WIDTH / 4 - POINT_SIZE / 2 to HEIGHT - POINT_SIZE,
        1 * WIDTH / 4 - POINT_SIZE / 2 to HEIGHT - POINT_SIZE,

        0 to HEIGHT - POINT_SIZE,
        0 to HEIGHT / 2 - POINT_SIZE / 2,
    )
}