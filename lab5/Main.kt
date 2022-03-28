package lab5_denis

import java.io.File
import java.util.*

//ns - numbers
//ls - letters
//ws - whitespace symbols

object Main {
    val table = arrayOf(
                             // code    0    1   2   3   4   5   6   7   8   9  10  11  12  13
                             // char    d    e   f   r   t   u   n  ns   :   )   (   "  ls  ws
        /*START*/            intArrayOf( 1, 10, 10,  4, 10, 10, 10, 11, 12, 13, 14, 15, 10, 19),
        /*IDENT_1 d*/        intArrayOf(10,  2, 10, 10, 10, 10, 10, 10, -1, -1, -1, 15, 10, -1),
        /*IDENT_2 e*/        intArrayOf(10, 10,  9, 10, 10, 10, 10, 10, -1, -1, -1, 15, 10, -1),
        /*IDENT_3 f*/        intArrayOf(10, 10, 10,  8, 10, 10, 10, 10, -1, -1, -1, 15, 10, -1),
        /*IDENT_4 r*/        intArrayOf(10,  5, 10, 10, 10, 10, 10, 10, -1, -1, -1, 15, 10, -1),
        /*IDENT_5 e*/        intArrayOf(10, 10, 10, 10,  6, 10, 10, 10, -1, -1, -1, -1, 10, -1),
        /*IDENT_6 t*/        intArrayOf(10, 10, 10, 10, 10,  7, 10, 10, -1, -1, -1, 15, 10, -1),
        /*IDENT_7 u*/        intArrayOf(10, 10, 10,  8, 10, 10, 10, 10, -1, -1, -1, 15, 10, -1),
        /*IDENT_8 r*/        intArrayOf(10, 10, 10, 10, 10, 10,  9, 10, -1, -1, -1, 15, 10, -1),
        /*KEYWORD_9*/        intArrayOf(10, 10, 10, 10, 10, 10, 10, 10, -1, -1, -1, 15, 10, -1),
        /*IDENT_10*/         intArrayOf(10, 10, 10, 10, 10, 10, 10, 10, -1, -1, -1, -1, 10, -1),
        /*NUMBER_11*/        intArrayOf(-1, -1, -1, -1, -1, -1, -1, 11, -1, -1, -1, -1, -1, -1),
        /*OPERATION_12*/     intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1),
        /*OPERATION_13*/     intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1),
        /*OPERATION_14*/     intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1),
        /*PRECOMMENT1_15*/   intArrayOf(15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 16, 15, 15),
        /*PRECOMMENT2_16*/   intArrayOf(15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 17, 15, 15),
        /*PRECOMMENT3_17*/   intArrayOf(15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 18, 15, 15),
        /*COMMENT_18*/       intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1),
        /*WHITESPACE_19*/    intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 19)
    )

    @JvmStatic
    fun main(args: Array<String>) {
        val text = StringBuilder()
        val scanner = Scanner(File("src/example.txt"))
        var i = 1
        while (scanner.hasNextLine()) {
            val line = scanner.nextLine()
            text.append(line).append("\n")
            ++i
        }
        text.deleteCharAt(text.length - 1)
        val auto = Automata(text.toString(), table)
        println("\nTokens:")
        var t = auto.nextToken()
        while (null != t) {
            if (t.tag == DomainTag.EOF) {
                println(t.toString())
                break
            }
            if (t.tag != DomainTag.WHITESPACE) {
                println(t.toString())
            }
            t = auto.nextToken()
        }
        auto.outputMessages()
    }
}
