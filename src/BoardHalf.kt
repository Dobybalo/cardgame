import cards.ClearWeatherCard
import cards.CommandersHornCard
import cards.UnitCard
import cards.WeatherCard

data class BoardHalf(val meleeRow: CardRow,
                val rangedRow: CardRow,
                val siegeRow: CardRow) {

    fun placeCard(card: UnitCard): BoardHalf =
        when(card.type) {
            UnitType.MELEE -> BoardHalf(meleeRow.placeCard(card), rangedRow, siegeRow)
            UnitType.RANGED -> BoardHalf(meleeRow, rangedRow.placeCard(card), siegeRow)
            UnitType.SIEGE -> BoardHalf(meleeRow, rangedRow, siegeRow.placeCard(card))
        }

    fun placeCommandersHornCard(hornCard: CommandersHornCard, whichRow: UnitType): BoardHalf =
        when (whichRow) {
            UnitType.MELEE -> BoardHalf(meleeRow.placeCommandersHornCard(hornCard), rangedRow, siegeRow)
            UnitType.RANGED -> BoardHalf(meleeRow, rangedRow.placeCommandersHornCard(hornCard), siegeRow)
            UnitType.SIEGE -> BoardHalf(meleeRow, rangedRow, siegeRow.placeCommandersHornCard(hornCard))
        }

    fun placeWeatherCard(weatherCard: WeatherCard): BoardHalf =
        when(weatherCard.affectsType) {
            UnitType.MELEE -> BoardHalf(meleeRow.applyWeatherCard(), rangedRow, siegeRow)
            UnitType.RANGED -> BoardHalf(meleeRow, rangedRow.applyWeatherCard(), siegeRow)
            UnitType.SIEGE -> BoardHalf(meleeRow, rangedRow, siegeRow.applyWeatherCard())
        }

    fun placeClearWeatherCard(clearWeatherCard: ClearWeatherCard): BoardHalf {
        val newMeleeRow = meleeRow.applyClearWeatherCard()
        val newRangedRow = rangedRow.applyClearWeatherCard()
        val newSiegeRow = siegeRow.applyClearWeatherCard()
        return BoardHalf(newMeleeRow, newRangedRow, newSiegeRow)
    }

    //TODO - most EBBEN az osztályban van annak a logikája, hogy mit kell csinálni az egyes lapok kijátszása esetén...
    // jó lenne megcsinálni, hogy ezek az információk a kártyák SAJÁT osztályában szerepeljenek...így a bővíthetőség is megoldható
}