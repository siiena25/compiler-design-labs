abstract class Token protected constructor(
    val tag: DomainTag,
    starting: Position?,
    following: Position?
) {
    val coords = Fragment(starting!!, following!!)

    override fun toString(): String {
        return coords.toString()
    }
}
