package quick.lexer

object Lexer {

	fun lex(source: String): List<Token> {
		val iterator = TextIterator(source)
		val result = ArrayList<Token>()

		while (iterator.hasNext()) {
			iterator.takeWhile { it.isWhitespace() && it != '\n' }

			var token: Token? = null
//			println("Parsing @${iterator.position}:${iterator.position.index} '${iterator.current}' = ${iterator.current?.toInt()}")

			for (parser in TokenType.values) {
				val position = iterator.position

				token = try {
					val (type, value) = parser.parse(iterator)
					Token(type, value, position, iterator.position)
				} catch (ex: LexerException) {
					iterator.position = position
					continue
				}
				break
			}

			if (token == null) {
				println(iterator.position)
				println("'${iterator.next()}' (${iterator.current!!.toInt()})")
				throw LexerException()
			}

			result.add(token)
		}
		return result
	}

}