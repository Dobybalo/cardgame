import cards.ClearWeatherCard
import cards.CommandersHornCard
import cards.UnitCard
import cards.WeatherCard

data class Board(val boardHalfA: BoardHalf, //TODO - valszeg az lenne a jó, ha PRIVATE-ok lennének a változók....
//            private val boardHalfB: BoardHalf,
                 private val weatherCardSpot: WeatherCardSpot) {

    fun placeCard(/*player: Player,*/ card: UnitCard): Board {
        boardHalfA.placeCard(card) //magát azt a logikát, hogy egy lapot máshova HOVA tudsz letenni, azt inkább frontenden oldjuk meg, minthogy mindig vissza kelljen kérdezni a szervernek...
        //igazából kell, hogy minden szerver oldalon reprezentálva legyen? Majd kiderül...
        return Board(boardHalfA.placeCard(card), weatherCardSpot)
    }

    fun placeCard(hornCard: CommandersHornCard, whichRow: UnitType): Board {
        return Board(boardHalfA.placeCommandersHornCard(hornCard, whichRow), weatherCardSpot)
    }

    fun placeCard(weatherCard: WeatherCard): Board {
        return Board(boardHalfA.placeWeatherCard(weatherCard), weatherCardSpot.placeCard(weatherCard))
    }

    fun placeCard(clearWeatherCard: ClearWeatherCard): Board {
        return Board(boardHalfA.placeClearWeatherCard(clearWeatherCard), weatherCardSpot.placeCard(clearWeatherCard))
    }
}