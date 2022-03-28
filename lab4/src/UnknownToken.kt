class UnknownToken(
    tag: DomainTag?,
    starting: Position?,
    following: Position?
) : Token(tag!!, starting, following) {
    override fun toString(): String {
        return "EOF " + super.toString()
    }
}
