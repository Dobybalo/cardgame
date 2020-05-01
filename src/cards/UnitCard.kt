package cards

import UnitType

data class UnitCard(
    val name: String,
    val originalStrength: Int,
    val currentStrength: Int,
    val type: UnitType
)