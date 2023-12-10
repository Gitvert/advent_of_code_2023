
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
    
    val loop = findLoop(startDirections, pipes, start)
    
    val newMap = buildNewMap(lines, loop)

    markEnclosedTiles(newMap, loop)

    printPipeMap(newMap)
    
    val enclosedTilesCount = newMap.flatten().count { it == 'I' }

    println("Day 10 part 2: $enclosedTilesCount")
    println()
}

fun markEnclosedTiles(newMap: List<MutableList<Char>>, loop: List<Pos>) {
    val startDirection = Pos(loop[1].x - loop[0].x, loop[1].y - loop[0].y)
    var currentDirection = startDirection
    var lastDirection = Pos(loop.first().x - loop.last().x, loop.first().y - loop.last().y)
    var searchDirection: Pos
    var flipSearchDirection = true
    var hasTurnedOnce = false
    
    for (i in loop.indices) {
        if (i == 0 || i == loop.size - 1) {
            continue
        }
        if (loop[i - 1].x != loop[i + 1].x && loop[i - 1].y != loop[i + 1].y) {
            lastDirection = currentDirection
            if (currentDirection == startDirection && hasTurnedOnce) {
                flipSearchDirection = !flipSearchDirection
            }
            hasTurnedOnce = true
        }
        
        currentDirection = Pos(loop[i + 1].x - loop[i].x, loop[i + 1].y - loop[i].y)
        
        searchDirection = if (flipSearchDirection) {
            Pos(lastDirection.x * -1, lastDirection.y * -1)
        } else {
            lastDirection
        }
        
        try {
            if (newMap[loop[i].y + searchDirection.y][loop[i].x + searchDirection.x] != '*') {
                newMap[loop[i].y + searchDirection.y][loop[i].x + searchDirection.x] = 'I'

                printPipeMap(newMap)
            }
        } catch (_: IndexOutOfBoundsException) {
            //println(e)
        }
    }
}

fun buildNewMap(lines: List<String>, loop: List<Pos>): List<MutableList<Char>> {
    val newMap = mutableListOf<MutableList<Char>>()
    
    lines.forEachIndexed { y, row ->
        val line = mutableListOf<Char>()
        row.forEachIndexed { x, _ ->
            if (loop.contains(Pos(x,y))) {
                line.add('*')
            } else {
                line.add('.')
            }
        }
        newMap.add(line)
    }
    
    return newMap
}

fun findLoop(startDirections: Pair<Pos, Pos>, pipes: List<List<Pipe?>>, start: Pos): List<Pos> {
    val forward = mutableListOf(start, startDirections.first)
    val backwards = mutableListOf(start, startDirections.second)
    
    while (forward.last() != start) {
        forward.add(findNextPipe(forward.last(), forward[forward.size - 2], pipes))
        backwards.add(findNextPipe(backwards.last(), backwards[backwards.size - 2], pipes))
        
        if (forward.last() == backwards.last() && forward.last() != start) {
            println("Day 10 part 1: ${forward.size - 1}")
        }
    }

    forward.removeLast()

    return forward
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
    
    try {
        if (lines[start.y][start.x + 1] == '-' || lines[start.y][start.x + 1] == 'J' || lines[start.y][start.x + 1] == '7') {
            startDirections.add(Pos(start.x + 1, start.y))
        }
    } catch (_: IndexOutOfBoundsException) {
        
    }
    try {
        if (lines[start.y][start.x - 1] == '-' || lines[start.y][start.x - 1] == 'L' || lines[start.y][start.x - 1] == 'F') {
            startDirections.add(Pos(start.x - 1, start.y))
        }
    } catch (_: IndexOutOfBoundsException) {

    }
    try {
        if (lines[start.y + 1][start.x] == '|' || lines[start.y + 1][start.x] == 'L' || lines[start.y + 1][start.x] == 'J') {
            startDirections.add(Pos(start.x, start.y + 1))
        }
    } catch (_: IndexOutOfBoundsException) {

    }
    try {
        if (lines[start.y - 1][start.x] == '|' || lines[start.y - 1][start.x] == '7' || lines[start.y - 1][start.x] == 'F') {
            startDirections.add(Pos(start.x, start.y - 1))
        }
    } catch (_: IndexOutOfBoundsException) {

    }
    
    return Pair(startDirections[0], startDirections[1])
}

fun parsePipes(lines: List<String>): List<List<Pipe?>> {
    val pipes = mutableListOf<List<Pipe?>>()
    
    lines.forEach { row ->
        val rowPipes = mutableListOf<Pipe?>()
        row.forEach { cell ->
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

fun printPipeMap(newMap: List<MutableList<Char>>) {
    newMap.forEach { row ->
        row.forEach { cell -> 
            print(cell)
        }
        println()
    }
    println()
}

data class Pipe(val from: Pos, val to: Pos)

data class Pos(val x: Int, val y: Int)