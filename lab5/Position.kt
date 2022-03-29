package laba5

class Position internal constructor(private val text: String) : Comparable<Position?> {
    var line: Int
    var pos = 1
    var index: Int
    val currentPos: Int
        get() = if (index == text.length) {
            -1
        } else {
            Character.codePointAt(text.toCharArray(), index)
        }

    init {
        line = pos
        index = 0
    }

    private val isNewLine: Boolean
        get() = if (index == text.length) {
            true
        } else {
            if ('\r' == text[index] && index + 1 < text.length) '\n' == text[index + 1] else '\n' == text[index]
        }

    operator fun next(): Position {
        if (index < text.length) {
            if (isNewLine) {
                if ('\r' == text[index]) {
                    ++index
                }
                ++line
                pos = 1
            } else {
                if (Character.isHighSurrogate(text[index])) {
                    ++index
                }
                ++pos
            }
            ++index
        }
        return this
    }

    override operator fun compareTo(other: Position?): Int {
        return index.compareTo(other!!.index)
    }

    override fun toString(): String {
        return "($line, $pos)"
    }

    fun copy(): Position {
        val tmp = Position(text)
        tmp.pos = pos
        tmp.line = line
        tmp.index = index
        return tmp
    }
}
