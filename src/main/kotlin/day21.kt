fun day21 (lines: List<String>) {
    part1(lines)
    part2(lines)
}

fun part1(lines: List<String>) {
    val start = findStartPosition(lines)
    val currentPositions = mutableSetOf(start)
    val maxX = lines[0].indices.last
    val maxY = lines.indices.last

    for (i in 1..64) {
        takeAllPossibleSteps(lines, currentPositions, maxX, maxY)
    }

    println("Day 21 part 1: ${currentPositions.size}")
}

fun part2(lines: List<String>) {
    val extendedMap = extendMap(lines)
    //printGarden(extendedMap, mutableSetOf())

    val start = findStartPosition(extendedMap)
    println(start)
    val currentPositions = mutableSetOf(start)
    val maxX = extendedMap[0].indices.last
    val maxY = extendedMap.indices.last

    
    //Repeats at 65 + 131 * n
    for (i in 1..65) {
        takeAllPossibleSteps(extendedMap, currentPositions, maxX, maxY)
        //println("${currentPositions.size}")
    }
    
    //No of clustered rocks contained can be given by formula: (4 * n * n) - (4 * n) + 1
    printGarden(extendedMap, currentPositions)

    println("Day 21 part 2: ${currentPositions.size}")
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
    if (position.x in 0..maxX && position.y in 0..maxY/* && lines[position.y][position.x] != '#'*/) {
        newPositions.add(position)
    }
}

fun printGarden(lines: List<String>, currentPositions: MutableSet<Pos>) {
    for (y in lines.indices) {
        for (x in lines[0].indices) {
            if (currentPositions.contains(Pos(x, y))) {
                print('O')
            } else {
                print(lines[y][x])
            }
        }
        println()
    }
}

fun extendMap(lines: List<String>): List<String> {
    val newMap = mutableListOf<String>()
    var newLine = ""

    for (i in 1..9) {
        lines.forEach { line ->
            for (j in 1..9) {
                line.forEach{ cell ->
                    newLine += if (cell == 'S' && (i != 5 || j != 5)) {
                        '.'
                    } else {
                        cell
                    }
                }
            }
            newMap.add(newLine)
            newLine = ""
        }
    }
    
    return newMap
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