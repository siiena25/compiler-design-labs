open class OperationToken(
    val value: String,
    starting: Position?,
    following: Position?
) : Token(DomainTag.OPERATION, starting, following) {
    override fun toString(): String {
        return "OPERATION " + super.toString() + ": " + value
    }
}
