package quick.lexer

class TextIterator(private val src: List<String>) : Iterator<Char> {

    var pos: Position = Position(0, 0)

    var current: Char = 0.toChar()
        private set

    var currentLine: String = ""
        private set

    override fun hasNext(): Boolean {
        return pos.line < src.size && pos.column < src[pos.line].length
    }

    fun hasPrevious(): Boolean {
        return pos.line > 0 || pos.column > 0
    }

    override fun next(): Char {
        if (!hasNext())
            throw NoSuchElementException("thurs nuh mur fuckin STUFF MATE")

        currentLine = src[pos.line]
        current = currentLine[pos.column]

        pos = if (pos.column + 1 >= currentLine.length)
            Position(pos.line + 1, 0)
        else
            Position(pos.line, pos.column + 1)
        return current
    }

    fun previous(): Char {
        if (!hasPrevious())
            throw NoSuchElementException("thurs nuhthin BACKWARS EITHER MATE")

        pos = if (pos.column - 1 < 0)
            Position(pos.line - 1, src[pos.line - 1].length - 1)
        else
            Position(pos.line, pos.column - 1)

        val line = src[pos.line]
        current = line[pos.column]
        currentLine = line
        return current
    }

    inline fun takeWhile(body: (Char) -> Boolean): String {
        val builder = StringBuilder()

        while (hasNext() && body(next())) {
            builder.append(current)
        }
        if (hasPrevious())
            previous()
        return builder.toString()
    }
}