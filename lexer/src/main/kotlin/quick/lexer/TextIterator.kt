package quick.lexer

class TextIterator(private val source: String) : Iterator<Char> {

	private var column: Int = 0
	private var line: Int = 0
	private var index: Int = 0

	var position: Position
		get() = Position(index, line, column)
		set(value) {
			this.index = value.index
			this.line = value.line
			this.column = value.column
		}

	val current: Char?
		get() = source.getOrNull(index - 1)

	override fun hasNext(): Boolean {
		return index < source.length
	}

	fun hasPrevious(): Boolean {
		return index > 0
	}

	override fun next(): Char {
		if (!hasNext())
			throw NoSuchElementException()
		val value = source[index++]

		if (current == '\n') {
			line++
			column = 0
		} else {
			column++
		}

		return value
	}

	fun previous(): Char {
		if (!hasPrevious())
			throw NoSuchElementException()

		val value = source[--index]

		if (column == 0) {
			line--

			column = 0
			var i = index
			while (i > 0 && source[--i] != '\n')
				column++
		} else {
			column--
		}

		return value
	}

	inline fun takeWhile(body: (Char) -> Boolean): String {
		val builder = StringBuilder()

		while(hasNext()) {
			if(body(next())) {
				builder.append(current!!)
			}else{
				previous()
				break
			}
		}

		return builder.toString()
	}
}