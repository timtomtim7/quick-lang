package quick.lexer

object ColorizedPrinter {
	fun print(source: String, tokens: List<Token>, error: Token? = null, offset: Int? = null) {
		if (tokens.isEmpty())
			return
		val start = offset ?: -tokens.first().start.index
		var index = 0

		for (token in tokens) {
			print(source.subSequence(index, token.start.index + start))
			if (token == error) {
				print(ANSI.Text.BRIGHT_RED)
				print(ANSI.Decoration.UNDERLINE)
			} else {
				print(getColor(token))
			}
			print(source.subSequence(token.start.index + start, token.end.index + start))
			print(ANSI.Text.RESET)

			index = token.end.index + start
		}

		print(source.subSequence(index, source.length))
	}


	fun printError(fileName: String, source: String, tokens: List<Token>, errorToken: Token, message: String = "") {
		val start = errorToken.start

		val lineBeforeTokens = getTokens(tokens, start.line - 1)
		val (lineBeforeIndex, lineBeforeSource) = getLine(source, start.line - 1)

		val lineExactTokens = getTokens(tokens, start.line)
		val (lineExactIndex, lineExactSource) = getLine(source, start.line)

		val lineAfterTokens = getTokens(tokens, start.line + 1)
		val (lineAfterIndex, lineAfterSource) = getLine(source, start.line + 1)

		val color = ANSI.Text.GRAY + ANSI.Decoration.BOLD
		println("${ANSI.Text.BRIGHT_RED}Error at $fileName($fileName:${start.line + 1}): $message")

		val lineNumberPattern = "$color%3d | ${ANSI.Text.RESET}"
		val arrowOffset = errorToken.end.column
		val arrowOffsetString = if (arrowOffset == 0) "" else arrowOffset.toString()
		val arrowPattern = "%n%s    | %s%${arrowOffsetString}s"

		print(lineNumberPattern.format(start.line))
		print(lineBeforeSource, lineBeforeTokens, null, -lineBeforeIndex)
		println()

		print(lineNumberPattern.format(start.line + 1))
		print(lineExactSource, lineExactTokens, errorToken, -lineExactIndex)
		println(arrowPattern.format(color, ANSI.Text.BRIGHT_RED.toString(), "^".repeat(errorToken.length)))

		print(lineNumberPattern.format(start.line + 2))
		print(lineAfterSource, lineAfterTokens, null, -lineAfterIndex)
		println()

		println(ANSI.Text.RESET)
	}

	private fun getTokens(tokens: List<Token>, line: Int): List<Token> {
		return tokens.filter { it.start.line == line && it.type != TokenType.NewLine }
	}

	private fun getSource(source: String, first: Token, last: Token): String {
		return source.substring(first.start.index, last.end.index)
	}

	private fun getSource(source: String, tokens: List<Token>): String {
		return if (tokens.isEmpty()) ""
		else getSource(source, tokens.first(), tokens.last())
	}

	private fun getLine(source: String, line: Int): Pair<Int, String> {
		var index = 0
		var currentLine = 0
		var startIndex = if (line == 0) 0 else -1

		while (index < source.length) {
			if (source[index++] == '\n') {
				currentLine++
				if (startIndex != -1) {
					return startIndex to source.substring(startIndex, index - 1)
				}
				if (currentLine == line) {
					startIndex = index
				}
			}
		}

		return if (startIndex != -1)
			startIndex to source.substring(startIndex)
		else
			0 to source
	}

	private fun getColor(token: Token): String {
		return when (token.type) {
			is TokenType.Literal -> ANSI.Text.BRIGHT_GREEN
			is TokenType.Keyword -> ANSI.Text.GREEN
			TokenType.NewLine -> ANSI.Text.RESET
			TokenType.Identifier -> {
				if (token.value.first().isUpperCase())
					ANSI.Text.BRIGHT_CYAN
				else
					ANSI.Text.BLUE
			}
			TokenType.Symbol -> ANSI.Text.WHITE
		}.toString()
	}
}