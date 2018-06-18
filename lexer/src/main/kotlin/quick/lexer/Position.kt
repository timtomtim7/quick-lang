package quick.lexer

//Representation of a point in code
data class Position(val line: Int, val column: Int) {
    override fun toString(): String {
        return "($line:$column)"
    }
}