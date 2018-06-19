package quick.lexer

//Representation of a point in code
data class Position(val index: Int, val line: Int, val column: Int) : Comparable<Position> {
    override fun toString(): String {
		return "(${line + 1}:${column + 1})"
	}

	override fun compareTo(other: Position): Int {
		return index.compareTo(other.index)
    }
}