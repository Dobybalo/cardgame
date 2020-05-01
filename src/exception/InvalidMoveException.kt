package exception

import java.lang.IllegalArgumentException

class InvalidMoveException(s: String): IllegalArgumentException(s)