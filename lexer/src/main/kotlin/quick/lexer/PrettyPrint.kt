package quick.lexer

object PrettyPrint {
	fun print(source: String, tokens: List<Token>) {
		var index = 0

		for(token in tokens) {
			val before = source.subSequence(index, token.start.index)
			val content = source.subSequence(token.start.index, token.end.index)
			val color = getColor(token)
			print(before)
			print(color)
			print(content)

			index = token.end.index
		}
	}

	@Suppress("IMPLICIT_CAST_TO_ANY")
	fun getColor(token: Token): String {
		return when(token.type) {
			is TokenType.Literal -> ANSI.Text.RED
			is TokenType.Keyword -> ANSI.Text.BLUE
			TokenType.NewLine -> ANSI.Text.RESET
			TokenType.Identifier -> {
				if(token.value.first().isUpperCase())
					ANSI.Decoration.BOLD
				else
					ANSI.Text.GRAY
			}
			TokenType.Symbol -> ANSI.Text.WHITE
		}.toString()
	}
}