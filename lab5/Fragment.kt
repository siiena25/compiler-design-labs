package lab5_denis

class Fragment(private val starting: Position, val following: Position) {
    override fun toString(): String {
        return "$starting-$following"
    }
}
