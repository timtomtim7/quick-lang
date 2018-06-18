package quick.lexer

object Lexer {

    fun lex(lines: List<String>): List<Token> {
        val iterator = TextIterator(lines)
        val result = ArrayList<Token>()

        while (iterator.hasNext()) {

            if(iterator.currentLine.isBlank()) {
                iterator.next()
                continue
            }

            iterator.takeWhile(Char::isWhitespace)

            var token: Token? = null

            for (type in TokenType.types) {
                val pos = iterator.pos

                token = try {
                    type.parse(iterator)
                } catch (ex: LexerException) {
                    iterator.pos = pos
                    continue
                }
                break
            }

            if(token == null) {
                println(iterator.pos)
                throw LexerException()
            }

            result.add(token)
        }
        return result
    }

}