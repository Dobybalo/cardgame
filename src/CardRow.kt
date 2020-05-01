import cards.CommandersHornCard
import cards.UnitCard
import exception.InvalidMoveException

data class CardRow(val list: List<UnitCard>,
                   val isCommandersHornActive: Boolean) {

    fun placeCard(card: UnitCard): CardRow {
        val newList = list.toMutableList()
        newList.add(card) //mutability sérül...?

        return this.copy(list = newList)
    }

    fun countStrength(): Int {
        val unitsSum = list.map { it.currentStrength }.sum()
        return if (isCommandersHornActive) unitsSum * 2 else unitsSum
    }

    fun placeCommandersHornCard(hornCard: CommandersHornCard): CardRow {
        if (isCommandersHornActive) {
            throw InvalidMoveException("You cannot place two commander's horns on the same row!")
        }
        return this.copy(isCommandersHornActive = true)
    }

    fun applyWeatherCard(): CardRow {
        //ezen a ponton már nem kell tudni milyen fajta...
        //gyengítsük le az egységeket...
        val newList = list.map { it.copy(currentStrength = 1) }
        return this.copy(list = newList)
    }

    fun applyClearWeatherCard(): CardRow {
        //állítsuk vissza az összes WEATHER OKOZTA változást!!

        val newList = list.map {it.copy(currentStrength = it.originalStrength)}
        if (isCommandersHornActive) {
            //TODO - a commander's horn-t újra "végre kell hajtani"...
        }

        return this.copy(list = newList)
    }
}