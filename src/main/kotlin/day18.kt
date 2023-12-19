import java.util.*

const val X_SIZE = 340
const val Y_SIZE = 270

fun day18 (lines: List<String>) {
    
    val instructions = parseDigInstructions(lines)
    val trenchMap: Array<Array<Char>> = Array(Y_SIZE) { Array(X_SIZE) {'.'} }
    
    digTrench(instructions, trenchMap)
    bfs(trenchMap)
    
    val trenchSize = trenchMap.flatten().count { it == '.' || it == '#' }
    
    println("Day 18 part 1: $trenchSize")
    println("Day 18 part 2: ")
    println()
}

fun digTrench(instructions: List<DigInstruction>, trenchMap: Array<Array<Char>>) {
    var currentPos = Pos(130, 240)
    
    instructions.forEach { instruction -> 
        val direction = when(instruction.dir) {
            'U' ->  { Pos(0, -1) }
            'D' ->  { Pos(0, 1) }
            'L' ->  { Pos(-1, 0) }
            else ->  { Pos(1, 0) }
        }
        
        for (i in 1..instruction.length) {
            currentPos = Pos(currentPos.x + direction.x, currentPos.y + direction.y)
            trenchMap[currentPos.y][currentPos.x] = '#'
        }
    }
}

fun bfs(trenchMap: Array<Array<Char>>) {
    val start = Pos(0,0)
    val queue: Queue<Pos> = LinkedList()
    trenchMap[start.y][start.x] = '*'

    val visited = mutableListOf(start)
    queue.add(start)

    while(queue.isNotEmpty()) {
        val current = queue.peek()
        queue.remove()

        for (i in 0..3) {
            val posToCheck = Pos(current.x + D_ROW[i], current.y + D_COL[i])
            if (isValid(trenchMap, posToCheck, visited)) {
                visited.add(posToCheck)
                queue.add(posToCheck)
                trenchMap[posToCheck.y][posToCheck.x] = '*'
            }
        }
    }
}

fun isValid(trenchMap: Array<Array<Char>>, pos: Pos, visited: List<Pos>): Boolean {
    if (pos.x < 0 || pos.y < 0 || pos.x >= trenchMap[0].size || pos.y >= trenchMap.size) {
        return false
    }

    if (visited.contains(pos)) {
        return false
    }

    if (trenchMap[pos.y][pos.x] == '#') {
        return false
    }

    return true
}

fun parseDigInstructions(lines: List<String>): List<DigInstruction> {
    return lines.map { 
        DigInstruction(it.split(" ")[0][0], Integer.parseInt(it.split(" ")[1]), it.split(" ")[2])
    }
}

data class DigInstruction(val dir: Char, val length: Int, val color: String)