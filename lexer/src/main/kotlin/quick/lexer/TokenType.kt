package quick.lexer

private typealias Result = Pair<TokenType, String>

sealed class TokenType {
	abstract fun parse(text: TextIterator): Result

	companion object {
		val values = Keyword.values + Literal.values + listOf(NewLine, Symbol, Identifier)
	}

	override fun toString(): String {
		return javaClass.simpleName
	}

	object NewLine : TokenType() {
		override fun parse(text: TextIterator): Result {
			val result = text.takeWhile { it in "\n\r" }
			if (result.isEmpty())
				throw LexerException()

			return this to result
		}
	}

	object Identifier : TokenType() {
		override fun parse(text: TextIterator): Result {
			val first = text.next()

			if (!first.isLetter())
				throw LexerException()

			val value = first + text.takeWhile(Char::isLetterOrDigit)

			return this to value
		}
	}

	object Symbol : TokenType() {

		private val symbols = "(){}[]=-+;:/*,.^&|!"

		override fun parse(text: TextIterator): Result {
			val symbol = text.next()
			if (symbol !in symbols)
				throw LexerException()

			return this to symbol.toString()

		}

	}

	sealed class Literal : TokenType() {
		companion object {
			val values = setOf(
					Boolean,
					String,
					Int,
					Float
			)
		}

		object Boolean : Literal() {
			override fun parse(text: TextIterator): Result {
				val value = text.takeWhile(Char::isLetterOrDigit)

				if (value != "true" && value != "false")
					throw LexerException()

				return this to value
			}

		}

		object String : Literal() {
			override fun parse(text: TextIterator): Result {
				val first = text.next()

				if (first != '"')
					throw LexerException()

				val value = StringBuilder()

				var isEscaped = false

				while (true) {
					val next = text.next()

					if (!isEscaped) {
						if (next == '"')
							break
						if (next == '\\')
							isEscaped = true
					}

					value.append(next)
				}

				return this to value.toString()
			}

		}

		object Int : Literal() {
			override fun parse(text: TextIterator): Result {
				val first = text.next()

				if (first != '-' && !first.isDigit())
					throw LexerException()

				val value = first + text.takeWhile { it.isDigit() }
				return this to value
			}

		}

		object Float : Literal() {
			override fun parse(text: TextIterator): Result {
				val value = text.takeWhile { it in "-+e." || it.isDigit() }

				try {
					value.toDouble()
				} catch (ex: NumberFormatException) {
					throw LexerException()
				}

				if (text.next() != 'f') {
					text.previous()
					try {
						value.toLong()

						return Int to value
					} catch (ex: NumberFormatException) {
					}
				}

				return this to value
			}
		}
	}

	sealed class Keyword(val str: String) : TokenType() {

		override fun parse(text: TextIterator): Result {
			var index = 0
			while (text.hasNext() && index < str.length) {
				if (text.next() != str[index++]) {
					throw LexerException()
				}
			}

			if (index != str.length)
				throw LexerException()

			return this to str
		}

		companion object {
			val values = setOf(
					Return,
					If,
					Public,
					Private,
					Protected,
					New,
					Mutable,
					Infer
			)
		}

		object Return : Keyword("return")
		object If : Keyword("if")
		object Public : Keyword("public")
		object Private : Keyword("private")
		object Protected : Keyword("protected")
		object New : Keyword("new")
		object Mutable : Keyword("mutable")
		object Infer : Keyword("infer")
	}


}




