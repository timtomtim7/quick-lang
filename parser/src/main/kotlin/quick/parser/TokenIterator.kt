package quick.parser

import quick.lexer.Token
import quick.lexer.TokenType

class TokenIterator(val tokens: List<Token>) : Iterator<Token> {

	var currentIndex = 0
	lateinit var currentToken: Token

	override fun hasNext(): Boolean {
		return currentIndex < tokens.size
	}

	fun hasPrevious(): Boolean {
		return currentIndex > 0
	}

	fun previous(): Token {
		if (!hasPrevious())
			throw NoSuchElementException()
		currentToken = tokens[--currentIndex]
		return currentToken
	}

	override fun next(): Token {
		if (!hasNext())
			throw NoSuchElementException()

		currentToken = tokens[currentIndex++]
		return currentToken
	}

	inline fun optional(body: (Token) -> Boolean): Token? {
		if (!hasNext())
			throw NoSuchElementException()

		val next = next()
		if (body(next))
			return next

		previous()
		return null
	}

	@JvmName("optionalTyped")
	inline fun <reified T : TokenType> optional(body: (Token) -> Boolean = { true }): Token? {
		return optional { it.type is T && body(it) }
	}

	@JvmName("optionalTyped")
	inline fun <reified T : TokenType> optional(value: String): Token? {
		return optional<T> { it.value == value }
	}

	fun identifier() = optional<TokenType.Identifier>()
	fun symbol(char: Char? = null) = optional<TokenType.Symbol> { char == null || it.value[0] == char }
	fun literal() = optional<TokenType.Literal>()
}