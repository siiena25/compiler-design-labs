package lab5_denis

class Token internal constructor(val tag: DomainTag, image: String, parse: String, starting: Position?, following: Position?) {
    private val coords: Fragment
    private val image: String
    private val parse: String

    init {
        coords = Fragment(starting!!, following!!)
        this.image = image
        this.parse = parse
    }

    override fun toString(): String {
        return "$tag $coords: $image\n$parse"
    }

}
