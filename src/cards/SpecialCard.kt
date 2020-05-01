package cards

import CardRow

abstract class SpecialCard {

    //visszaad egy új állapotot

    abstract fun applyEffect(): CardRow //majd Board-ot kell visszaadjon(??)

}