package laba5

import java.util.*


internal class Automata(private var program: String, private val table: Array<IntArray>) {
    private val messages: SortedMap<Position?, String?> = TreeMap<Position?, String?>()
    private val pos: Position = Position(program)
    private var state: Int = 0
    private var err = false

    private fun getCode(c: Char): Int {
        return when (c) {
            'd' -> 0
            'e' -> 1
            'f' -> 2
            'r' -> 3
            't' -> 4
            'u' -> 5
            'n' -> 6
            else -> if (c in '0'..'9') {
                7
            } else if (c == ':') {
                8
            } else if (c == ')') {
                9
            } else if (c == '(') {
                10
            } else if (c == '"') {
                11
            } else if (c in 'a'..'z' || c in 'A'..'Z') {
                12
            } else {
                if (' ' != c && '\n' != c && '\r' != c && '\t' != c) {
                    -1
                } else {
                    13
                }
            }
        }
    }

    private fun getStateName(state: Int): DomainTag {
        return when (state) {
            1, 2, 3, 4, 5, 6, 7, 8 -> DomainTag.IDENT
            9 -> DomainTag.KEYWORD
            10 -> DomainTag.IDENT
            11 -> DomainTag.NUMBER
            12, 13, 14 -> DomainTag.OPERATION
            15 -> DomainTag.PRECOMMENT1
            16 -> DomainTag.PRECOMMENT2
            17 -> DomainTag.PRECOMMENT3
            18 -> DomainTag.PRECOMMENT4
            19 -> DomainTag.PRECOMMENT5
            20 -> DomainTag.COMMENT
            21 -> DomainTag.WHITESPACE
            else -> DomainTag.ERROR
        }
    }

    fun nextToken(): Token? {
        return if (pos.currentPos == -1) {
            val posCopy = pos.copy()
            Token(DomainTag.EOF, "", "", posCopy, posCopy)
        } else {
            while (-1 != pos.currentPos) {
                val word = StringBuilder()
                val parse = StringBuilder()
                state = 0
                var finalState = false
                val start = pos.copy()
                while (-1 != pos.currentPos) {
                    val currChar = program[pos.index]
                    val jumpCode = getCode(currChar)
                    if (-1 == jumpCode) {
                        if (!err) {
                            messages[pos.copy()] = "Unexpected characters"
                            err = true
                        }
                        break
                    }
                    err = false
                    parse.append("(").append(state).append(")->")
                    if (currChar == '\n') {
                        parse.append("[\\n]->")
                    } else {
                        parse.append("[").append(currChar).append("]->")
                    }
                    val nextState = table[state][jumpCode]
                    if (-1 == nextState) {
                        finalState = true
                        parse.append("(-1)\n")
                        break
                    }
                    word.append(currChar)
                    state = nextState
                    pos.next()
                }
                if (finalState) {
                    return Token(
                        getStateName(state),
                        word.toString().replace("\n".toRegex(), " "),
                        parse.toString(),
                        start,
                        pos
                    )
                }
                if (pos.currentPos == -1) {
                    parse.append("(-1)\n")
                    if (getStateName(state) != DomainTag.PRECOMMENT1
                        && getStateName(state) != DomainTag.PRECOMMENT2
                        && getStateName(state) != DomainTag.PRECOMMENT3) {
                        return Token(
                            getStateName(state),
                            word.toString().replace("\n".toRegex(), " "),
                            parse.toString(),
                            start,
                            pos
                        )
                    }
                    messages[pos.copy()] = "EOF found, but '\"\"\"' expected"
                    return Token(
                        DomainTag.COMMENT,
                        word.toString().replace("\n".toRegex(), " "),
                        parse.toString(),
                        start,
                        pos
                    )
                }
                pos.next()
            }
            null
        }
    }

    fun outputMessages() {
        println("\nMessages:")
        messages.entries.forEach {
            println("ERROR (${it.key!!.line}, ${it.key!!.pos}): ${it.value}")
        }
    }
}