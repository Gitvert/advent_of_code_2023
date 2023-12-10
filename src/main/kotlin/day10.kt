
val LETTER_DIRECTION_MAP = mapOf(
    Pair('|', Pipe(Pos(0, -1), Pos(0, 1))),
    Pair('-', Pipe(Pos(-1, 0), Pos(1, 0))),
    Pair('L', Pipe(Pos(0, -1), Pos(1, 0))),
    Pair('J', Pipe(Pos(0, -1), Pos(-1, 0))),
    Pair('7', Pipe(Pos(0, 1), Pos(-1, 0))),
    Pair('F', Pipe(Pos(0, 1), Pos(1, 0))),
)

fun day10 (lines: List<String>) {
    val start = findStart(lines)
    val pipes = parsePipes(lines)
    val startDirections = findStartDirections(start, lines)
    
    val steps = countSteps(startDirections, pipes, start)

    println("Day 10 part 1: $steps")
    println("Day 10 part 2: ")
    println()
}

fun countSteps(startDirections: Pair<Pos, Pos>, pipes: List<List<Pipe?>>, start: Pos): Int {
    val forward = mutableListOf(start, startDirections.first)
    val backwards = mutableListOf(start, startDirections.second)
    
    while (forward.last() != backwards.last()) {
        forward.add(findNextPipe(forward.last(), forward[forward.size - 2], pipes))
        backwards.add(findNextPipe(backwards.last(), backwards[backwards.size - 2], pipes))
    }
    
    return forward.size - 1
}

fun findNextPipe(currPos: Pos, prevPos: Pos, pipes: List<List<Pipe?>>): Pos {
    val currentPipe = pipes[currPos.y][currPos.x]!!
    
    if (currPos.x + currentPipe.from.x == prevPos.x && currPos.y + currentPipe.from.y == prevPos.y) {
        return Pos(currPos.x + currentPipe.to.x, currPos.y + currentPipe.to.y)
    }
    
    return Pos(currPos.x + currentPipe.from.x, currPos.y + currentPipe.from.y)
}

fun findStartDirections(start: Pos, lines: List<String>): Pair<Pos, Pos> {
    val startDirections = mutableListOf<Pos>()
    
    if (lines[start.y][start.x + 1] == '-' || lines[start.y][start.x + 1] == 'J' || lines[start.y][start.x + 1] == '7') {
        startDirections.add(Pos(start.x + 1, start.y))
    }
    if (lines[start.y][start.x -1] == '-' || lines[start.y][start.x -1] == 'L' || lines[start.y][start.x -1] == 'F') {
        startDirections.add(Pos(start.x - 1, start.y))
    }
    if (lines[start.y + 1][start.x] == '|' || lines[start.y + 1][start.x] == 'L' || lines[start.y + 1][start.x] == 'J') {
        startDirections.add(Pos(start.x, start.y + 1))
    }
    if (lines[start.y - 1][start.x] == '|' || lines[start.y - 1][start.x] == '7' || lines[start.y - 1][start.x] == 'F') {
        startDirections.add(Pos(start.x, start.y - 1))
    }
    
    return Pair(startDirections[0], startDirections[1])
}

fun parsePipes(lines: List<String>): List<List<Pipe?>> {
    val pipes = mutableListOf<List<Pipe?>>()
    
    lines.forEachIndexed { _, row ->
        val rowPipes = mutableListOf<Pipe?>()
        row.forEachIndexed { _, cell ->
            val pipe = LETTER_DIRECTION_MAP[cell]
            if (pipe == null) {
                rowPipes.add(null)
            } else {
                rowPipes.add(pipe)
            }
        }
        pipes.add(rowPipes)
    }
    
    return pipes
}

fun findStart(lines: List<String>): Pos {
    var start = Pos(0,0)
    
    lines.forEachIndexed { y, row -> 
        row.forEachIndexed { x, cell -> 
            if (cell == 'S') {
                start = Pos(x, y)
            }
        }
    }
    
    return start
}

data class Pipe(val from: Pos, val to: Pos)

data class Pos(val x: Int, val y: Int)