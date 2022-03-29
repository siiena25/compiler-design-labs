package laba5

class Token internal constructor(
    val tag: DomainTag,
    private val image: String,
    private val parse: String,
    starting: Position?,
    following: Position?
) {
    private val coords: Fragment = Fragment(starting!!, following!!)

    override fun toString(): String {
        return "$tag $coords: $image\n$parse"
    }

}
