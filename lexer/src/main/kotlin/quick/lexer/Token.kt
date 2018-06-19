package quick.lexer

//Some shit my guy
data class Token(val type: TokenType, val value: String, val start: Position, val end: Position)