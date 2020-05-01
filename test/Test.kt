import cards.ClearWeatherCard
import cards.CommandersHornCard
import cards.UnitCard
import cards.WeatherCard
import exception.InvalidMoveException
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class Test {

    private val meleeCard = UnitCard("Zoltan Chivay", 5, 5, UnitType.MELEE)
    private val rangedCard = UnitCard("Síle of Tansarville", 5, 5, UnitType.RANGED)
    private val siegeCard = UnitCard("Catapult", 8, 8, UnitType.SIEGE)

    private val commandersHornCard = CommandersHornCard()
    private val bitingFrostCard = WeatherCard(UnitType.MELEE) //TODO - lehet nem jó így...
    private val impenetrableFogCard = WeatherCard(UnitType.RANGED)
    private val torrentialRainCard = WeatherCard(UnitType.SIEGE)
    private val clearWeatherCard =
        ClearWeatherCard() //TODO - nagy kérdés: a clear weather weather-nek számít-e?!

    private fun initBoard(): Board {
        val meleeRow = CardRow(emptyList(), false)
        val rangedRow = CardRow(emptyList(), false)
        val siegeRow = CardRow(emptyList(), false)

        val boardHalfA = BoardHalf(meleeRow, rangedRow, siegeRow)
        return Board(boardHalfA, WeatherCardSpot(emptySet()))
    }

    @Test
    fun `place melee on board`() {
        val board = initBoard()
        val boardAfterStep = board.placeCard(meleeCard)
        Assertions.assertTrue(boardAfterStep.boardHalfA.meleeRow.list.contains(meleeCard))
    }

    @Test
    fun `place melee, ranged and siege on board`() {
        val board = initBoard()
        val boardAfterStep = board
            .placeCard(meleeCard)
            .placeCard(rangedCard)
            .placeCard(siegeCard)

        Assertions.assertTrue(
            boardAfterStep.boardHalfA.meleeRow.list.size == 1 &&
            boardAfterStep.boardHalfA.meleeRow.list.contains(meleeCard) &&
            boardAfterStep.boardHalfA.rangedRow.list.size == 1 &&
            boardAfterStep.boardHalfA.rangedRow.list.contains(rangedCard) &&
            boardAfterStep.boardHalfA.siegeRow.list.size == 1 &&
            boardAfterStep.boardHalfA.siegeRow.list.contains(siegeCard)
        )
    }

    @Test
    fun `count strength of rows`() {
        //various cases: one card in melee, more cards in melee...
        // one in melee, two in ranged, zero in siege... etc.
        val board = initBoard()
        val boardAfterStep = board
            .placeCard(meleeCard)
            .placeCard(rangedCard)
            .placeCard(siegeCard)

        Assertions.assertEquals(5, boardAfterStep.boardHalfA.meleeRow.countStrength())
        Assertions.assertEquals(5, boardAfterStep.boardHalfA.rangedRow.countStrength())
        Assertions.assertEquals(8, boardAfterStep.boardHalfA.siegeRow.countStrength())
    }

    @Test
    fun `count strength of empty rows`() {
        //various cases: one card in melee, more cards in melee...
        // one in melee, two in ranged, zero in siege... etc.
        val board = initBoard()
        val boardAfterStep = board

        Assertions.assertEquals(0, boardAfterStep.boardHalfA.meleeRow.countStrength())
        Assertions.assertEquals(0, boardAfterStep.boardHalfA.rangedRow.countStrength())
        Assertions.assertEquals(0, boardAfterStep.boardHalfA.siegeRow.countStrength())
    }

    @Test
    fun `count strength - 2 melee, 1 ranged, 0 siege`() {
        val board = initBoard()
        val boardAfterStep = board
            .placeCard(meleeCard)
            .placeCard(meleeCard)
            .placeCard(rangedCard)

        Assertions.assertEquals(10, boardAfterStep.boardHalfA.meleeRow.countStrength())
        Assertions.assertEquals(5, boardAfterStep.boardHalfA.rangedRow.countStrength())
        Assertions.assertEquals(0, boardAfterStep.boardHalfA.siegeRow.countStrength())
    }

    @Test
    fun `commanders horn doubles melee`() {
        val board = initBoard()
        val boardAfterStep = board
            .placeCard(meleeCard)
            .placeCard(rangedCard)
            .placeCard(commandersHornCard, UnitType.MELEE)

        Assertions.assertEquals(10, boardAfterStep.boardHalfA.meleeRow.countStrength())
        Assertions.assertEquals(5, boardAfterStep.boardHalfA.rangedRow.countStrength())
        Assertions.assertEquals(0, boardAfterStep.boardHalfA.siegeRow.countStrength())
    }

    @Test
    fun `commanders horn doubles melee and ranged`() {
        val board = initBoard()
        val boardAfterStep = board
            .placeCard(meleeCard)
            .placeCard(rangedCard)
            .placeCard(commandersHornCard, UnitType.MELEE)
            .placeCard(commandersHornCard, UnitType.RANGED)

        Assertions.assertEquals(10, boardAfterStep.boardHalfA.meleeRow.countStrength())
        Assertions.assertEquals(10, boardAfterStep.boardHalfA.rangedRow.countStrength())
        Assertions.assertEquals(0, boardAfterStep.boardHalfA.siegeRow.countStrength())
    }

    @Test
    fun `no two commanders horn on same row`() {
        val board = initBoard()
        val boardAfterStep = board
            .placeCard(meleeCard)
            .placeCard(rangedCard)
            .placeCard(commandersHornCard, UnitType.MELEE)

        Assertions.assertThrows(InvalidMoveException::class.java) {
            boardAfterStep.placeCard(commandersHornCard, UnitType.MELEE)
        }
    }

    @Test
    fun `BitingFrost weather card affects melee`() {
        val board = initBoard()
        val boardAfterStep = board
            .placeCard(meleeCard)
            .placeCard(bitingFrostCard)

        Assertions.assertEquals(1, boardAfterStep.boardHalfA.meleeRow.countStrength())
    }

    @Test
    fun `BitingFrost weather card affects melee - more melee cards`() {
        val board = initBoard()
        val boardAfterStep = board
            .placeCard(meleeCard)
            .placeCard(meleeCard)
            .placeCard(bitingFrostCard)

        Assertions.assertEquals(2, boardAfterStep.boardHalfA.meleeRow.countStrength())
    }

    @Test
    fun `Impenetrable Fog weather card affects ranged`() {
        val board = initBoard()
        val boardAfterStep = board
            .placeCard(rangedCard)
            .placeCard(impenetrableFogCard)

        Assertions.assertEquals(1, boardAfterStep.boardHalfA.rangedRow.countStrength())
    }

    @Test
    fun `Torrential Rain weather card affects siege`() {
        val board = initBoard()
        val boardAfterStep = board
            .placeCard(siegeCard)
            .placeCard(torrentialRainCard)

        Assertions.assertEquals(1, boardAfterStep.boardHalfA.siegeRow.countStrength())
    }

    @Test
    fun `Clear Weather clears Biting Frost`() {
        val board = initBoard()
        val boardAfterStep = board
            .placeCard(meleeCard)
            .placeCard(bitingFrostCard)
            .placeCard(clearWeatherCard)

        Assertions.assertEquals(5, boardAfterStep.boardHalfA.meleeRow.countStrength())
    }

    //TODO - commander's horn is TÉNYLEGESEN változtassa meg a unitok erejét!!

    //TODO - clear weather card
    //TODO - weather after same type weather...

    //TODO - combine commander's horn and weather...
    //TODO - több melee lap...
    //TODO - másik játékost is befolyásolja...
    //TODO - több weather card együtt...
}