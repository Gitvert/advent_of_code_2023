fun day21 (lines: List<String>) {
    val start = findStartPosition(lines)
    val currentPositions = mutableSetOf(start)
    val maxX = lines[0].indices.last
    val maxY = lines.indices.last
    
    for (i in 1..64) {
        takeAllPossibleSteps(lines, currentPositions, maxX, maxY)
    }

    println("Day 21 part 1: ${currentPositions.size}")
    println("Day 21 part 2: ")
    println()
}

fun takeAllPossibleSteps(lines: List<String>, currentPositions: MutableSet<Pos>, maxX: Int, maxY: Int) {
    val newPositions = mutableSetOf<Pos>()
    
    while (currentPositions.isNotEmpty()) {
        val position = currentPositions.first()
        currentPositions.remove(position)
        
        addPositionIfValid(Pos(position.x + 1, position.y), lines, newPositions, maxX, maxY)
        addPositionIfValid(Pos(position.x - 1, position.y), lines, newPositions, maxX, maxY)
        addPositionIfValid(Pos(position.x, position.y + 1), lines, newPositions, maxX, maxY)
        addPositionIfValid(Pos(position.x, position.y - 1), lines, newPositions, maxX, maxY)
    }
    
    currentPositions.addAll(newPositions)
}

fun addPositionIfValid(position: Pos, lines: List<String>, newPositions: MutableSet<Pos>, maxX: Int, maxY: Int) {
    if (position.x in 0..maxX && position.y in 0..maxY && lines[position.y][position.x] != '#') {
        newPositions.add(position)
    }
}

fun printGarden(lines: List<String>, currentPositions: MutableSet<Pos>) {
    for (y in lines.indices) {
        for (x in lines[0].indices) {
            if (currentPositions.contains(Pos(x, y))) {
                print('O')
            } else {
                print('.')
            }
        }
        println()
    }
}

fun findStartPosition(lines: List<String>): Pos {
    lines.forEachIndexed { y, line -> 
        line.forEachIndexed { x, cell -> 
            if (cell == 'S') {
                return Pos(x, y)
            }
        }
    }
    
    return Pos(0, 0)
}