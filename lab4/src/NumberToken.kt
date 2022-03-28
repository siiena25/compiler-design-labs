open class NumberToken(
    val value: Long,
    starting: Position?,
    following: Position?
) : Token(DomainTag.NUMBER, starting, following) {
    override fun toString(): String {
        return "NUMBER " + super.toString() + ": " + value.toString()
    }
}
