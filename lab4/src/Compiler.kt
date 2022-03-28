class Compiler {
    private val messages: MutableList<Message> = ArrayList()
    private val nameCodes: MutableMap<String, Int> = HashMap()
    private val names: MutableList<String> = ArrayList()

    fun addName(name: String): Int {
        return if (nameCodes.containsKey(name)) {
            nameCodes[name]!!
        } else {
            val code = names.size
            names.add(name)
            nameCodes[name] = code
            code
        }
    }

    fun getNames(): MutableList<String> {
        return names
    }

    fun getNameCodes(): MutableMap<String, Int> {
        return nameCodes
    }

    fun errors(): Boolean {
        return messages.size > 0
    }

    fun getName(code: Int): String {
        return names[code]
    }

    fun addMessage(c: Position?, text: String?) {
        messages.add(Message(text!!, c!!))
    }

    fun outputMessages() {
        for (m in messages) {
            println("Error " + m.position + ": " + m.text)
        }
    }

    fun getScanner(program: String?): Scanner {
        return Scanner(program!!, this)
    }
}