import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths

object Main {
    @Throws(IOException::class)
    @JvmStatic
    fun main(args: Array<String>) {
        val compiler = Compiler()
        val program = String(Files.readAllBytes(Paths.get("src/example.txt")))
        val scanner = Scanner(program, compiler)
        while (true) {
            val token: Token = scanner.nextToken()
            if (token.tag !== DomainTag.END_OF_PROGRAM) {
                print(token.tag.name + " " + token.coords.toString() + ": ")
                when (token) {
                    is IdentToken -> print(token.value)
                    is NumberToken -> print(token.value)
                    is OperationToken -> print(token.value)
                }
                println(";")
            } else {
                println(token.tag.name + " " + token.coords.toString() + ";")
                break
            }
        }
        if (compiler.errors()) {
            println()
            compiler.outputMessages()
        }
        println(compiler.getNameCodes())
    }
}