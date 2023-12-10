import java.util.*

val LETTER_DIRECTION_MAP = mapOf(
    Pair('|', Pipe(Pos(0, -1), Pos(0, 1))),
    Pair('-', Pipe(Pos(-1, 0), Pos(1, 0))),
    Pair('L', Pipe(Pos(0, -1), Pos(1, 0))),
    Pair('J', Pipe(Pos(0, -1), Pos(-1, 0))),
    Pair('7', Pipe(Pos(0, 1), Pos(-1, 0))),
    Pair('F', Pipe(Pos(0, 1), Pos(1, 0))),
)

val D_COL = listOf(-1, 0, 1, 0)
val D_ROW = listOf(0, 1, 0, -1)

var startPipe: Char? = null

fun day10 (lines: List<String>) {
    val start = findStart(lines)
    val pipes = parsePipes(lines)
    val startDirections = findStartDirections(start, lines)
    
    val loop = findLoop(startDirections, pipes, start)
    
    val newMap = buildNewMap(lines, loop)
    val expandedMap = expandMap(newMap)
    
    bfs(expandedMap)
    
    val part2 = expandedMap.flatten().count { it == '.' }

    println("Day 10 part 2: $part2")
    println()
}

fun expandMap(newMap: List<MutableList<Char>>): MutableList<MutableList<Char>> {
    val expandedMap = mutableListOf<MutableList<Char>>()

    newMap.forEachIndexed { y, row ->
        val line = mutableListOf<Char>()
        line.add('*')
        row.forEachIndexed { x, cell ->
            line.add(cell)
            line.add('*')
        }
        expandedMap.add(line)
        expandedMap.add(line.map { '*' }.toMutableList())
    }
    
    expandedMap.forEachIndexed { y, row ->
        row.forEachIndexed { x, cell -> 
            if (LETTER_DIRECTION_MAP.keys.contains(cell)) {
                when(cell) {
                    '-' -> {
                        expandedMap[y][x + 1] = 'P'
                        expandedMap[y][x - 1] = 'P'
                    }
                    '|' -> {
                        expandedMap[y + 1][x] = 'P'
                        expandedMap[y - 1][x] = 'P'
                    }
                    'F' -> {
                        expandedMap[y][x + 1] = 'P'
                        expandedMap[y + 1][x] = 'P'
                    }
                    'L' -> {
                        expandedMap[y][x + 1] = 'P'
                        expandedMap[y - 1][x] = 'P'
                    }
                    '7' -> {
                        expandedMap[y][x - 1] = 'P'
                        expandedMap[y + 1][x] = 'P'
                    }
                    'J' -> {
                        expandedMap[y][x - 1] = 'P'
                        expandedMap[y - 1][x] = 'P'
                    }
                }
            }
        }
    }
    
    return expandedMap
}
fun buildNewMap(lines: List<String>, loop: List<Pos>): List<MutableList<Char>> {
    val newMap = mutableListOf<MutableList<Char>>()
    
    lines.forEachIndexed { y, row ->
        val line = mutableListOf<Char>()
        row.forEachIndexed { x, cell ->
            if (loop.contains(Pos(x,y))) {
                if (cell == 'S') {
                    line.add(startPipe!!)
                } else {
                    line.add(cell)
                }
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
    val directions = mutableListOf<String>()
    
    try {
        if (lines[start.y][start.x + 1] == '-' || lines[start.y][start.x + 1] == 'J' || lines[start.y][start.x + 1] == '7') {
            startDirections.add(Pos(start.x + 1, start.y))
            directions.add("EAST")
        }
    } catch (_: IndexOutOfBoundsException) {
        
    }
    try {
        if (lines[start.y][start.x - 1] == '-' || lines[start.y][start.x - 1] == 'L' || lines[start.y][start.x - 1] == 'F') {
            startDirections.add(Pos(start.x - 1, start.y))
            directions.add("WEST")
        }
    } catch (_: IndexOutOfBoundsException) {

    }
    try {
        if (lines[start.y + 1][start.x] == '|' || lines[start.y + 1][start.x] == 'L' || lines[start.y + 1][start.x] == 'J') {
            startDirections.add(Pos(start.x, start.y + 1))
            directions.add("SOUTH")
        }
    } catch (_: IndexOutOfBoundsException) {

    }
    try {
        if (lines[start.y - 1][start.x] == '|' || lines[start.y - 1][start.x] == '7' || lines[start.y - 1][start.x] == 'F') {
            startDirections.add(Pos(start.x, start.y - 1))
            directions.add("NORTH")
        }
    } catch (_: IndexOutOfBoundsException) {

    }
    
    if (directions.contains("WEST") && directions.contains("NORTH")) {
        startPipe = 'J'
    } else if (directions.contains("WEST") && directions.contains("SOUTH")) {
        startPipe = '7'
    } else if (directions.contains("EAST") && directions.contains("NORTH")) {
        startPipe = 'L'
    } else if (directions.contains("EAST") && directions.contains("SOUTH")) {
        startPipe = 'F'
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

fun isValid(map: List<MutableList<Char>>, pos: Pos, visited: List<Pos>): Boolean {
    if (pos.x < 0 || pos.y < 0 || pos.x >= map[0].size || pos.y >= map.size) {
        return false
    }
    
    if (visited.contains(pos)) {
        return false
    }
    
    if (LETTER_DIRECTION_MAP.keys.contains(map[pos.y][pos.x]) || map[pos.y][pos.x] == 'P') {
        return false
    }
    
    return true
}

fun bfs(map: List<MutableList<Char>>) {
    val start = Pos(0,0)
    val queue: Queue<Pos> = LinkedList()

    val visited = mutableListOf(start)
    queue.add(start)
    
    while(queue.isNotEmpty()) {
        val current = queue.peek()
        queue.remove()
        
        for (i in 0..3) {
            val posToCheck = Pos(current.x + D_ROW[i], current.y + D_COL[i])
            if (isValid(map, posToCheck, visited)) {
                visited.add(posToCheck)
                queue.add(posToCheck)
                map[posToCheck.y][posToCheck.x] = '*'
            }
        }
    }
}

data class Pipe(val from: Pos, val to: Pos)

data class Pos(val x: Int, val y: Int)