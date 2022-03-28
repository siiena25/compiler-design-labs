class Scanner(program: String, private val compiler: Compiler) {
    private var cur: Position = Position(program)

    fun nextToken(): Token {
        while (!cur.isEOF) {
            while (cur.isWhitespace) {
                cur = cur.next()
            }

            val token: Token? = when (cur.code) {
                '-'.code -> {
                    if (cur.next().isNumber) {
                        readNumber(cur)
                    } else {
                        readOperation(cur)
                    }
                }
                '<'.code -> {
                    readOperation(cur)
                }
                else ->
                    if (cur.isNumber) {
                        readNumber(cur)
                    } else if (cur.isLatin) {
                        readIdent(cur)
                    } else {
                        readOperation(cur)
                    }
            }

            if (token == null || token.tag === DomainTag.UNKNOWN) {
                cur = cur.next()
            } else {
                cur = token.coords.following
                return token
            }
        }
        return UnknownToken(DomainTag.END_OF_PROGRAM, cur, cur)
    }

    private fun readIdent(cur: Position): Token? {
        val sb = StringBuilder()
        var p = cur
        return if (p.isVowel) {
            while (p.isLatin) {
                sb.append(Character.toChars(p.code))
                p = p.next()
            }
            compiler.addName(sb.toString())
            IdentToken("CODE " + compiler.getNameCodes()[sb.toString()].toString(), cur, p)
        } else {
            sb.append(Character.toChars(p.code))
            compiler.addMessage(cur, "$sb: Label must start only with vowel and contains only latin letters")
            null
        }
    }

    private fun readNumber(cur: Position): Token? {
        val sb = StringBuilder()
        var p = cur
        if (p.isMinus) {
            sb.append(Character.toChars(p.code))
            p = p.next()
        }
        if (p.isNumber) {
            sb.append(Character.toChars(p.code))
            while (p.next().isNumber) {
                sb.append(Character.toChars(p.next().code))
                p = p.next()
            }
            return NumberToken(sb.toString().toLong(), cur, p.next())
        }
        sb.append(Character.toChars(p.code))
        compiler.addMessage(cur, "$sb: Number must contains only decimal number and minus at the beginning")
        return null
    }

    private fun readOperation(cur: Position): Token? {
        val sb = StringBuilder()
        var p = cur
        sb.append(Character.toChars(p.code))
        if (p.isMinus) {
            p = p.next()
            if (p.isMinus) {
                sb.append(Character.toChars(p.code))
                p = p.next()
                return OperationToken(sb.toString(), cur, p)
            }
        } else if (p.isLessSign) {
            p = p.next()
            if (p.isEqual) {
                sb.append(Character.toChars(p.code))
                p = p.next()
                return OperationToken(sb.toString(), cur, p)
            }
            return OperationToken(sb.toString(), cur, p)
        }
        compiler.addMessage(cur, "$sb: Operations must be like --, <, <=")
        return null
    }
}
