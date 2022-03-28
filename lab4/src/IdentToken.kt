open class IdentToken(
    val value: String,
    starting: Position?,
    following: Position?
) : Token(DomainTag.IDENT, starting, following) {
    override fun toString(): String {
        return "IDENT " + super.toString() + ": $value"
    }
}
