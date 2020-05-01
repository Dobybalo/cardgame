import cards.ClearWeatherCard
import cards.WeatherCard

class WeatherCardSpot(val activeCards: Set<WeatherCard>) {
    fun placeCard(card: WeatherCard): WeatherCardSpot {
        val newSet = mutableSetOf<WeatherCard>()
        newSet.addAll(activeCards)
        return WeatherCardSpot(newSet)
    }

    fun placeCard(card: ClearWeatherCard): WeatherCardSpot {
        return WeatherCardSpot(emptySet())
    }

}