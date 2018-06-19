package quick.parser

import quick.lexer.Token

object Parser {
	fun parse(tokens: List<Token>) {
		val iterator = TokenIterator(tokens)

//		iterator.optionalTyped<TokenType.Identifier>()
	}
}