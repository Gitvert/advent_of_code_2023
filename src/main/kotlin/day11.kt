import kotlin.math.abs

fun day11 (lines: List<String>) {
    val expanded = expandUniverse(lines)
    val positions = findGalaxyPositions(expanded)
    val totalManhattan = findManhattanDistance(positions)
    
    println("Day 11 part 1: $totalManhattan")
    println("Day 11 part 2: ")
    println()
}

fun findManhattanDistance(positions: List<GalaxyPosition>): Int {
    var totalManhattan = 0
    
    for (i in positions.indices) {
        for (j in positions.indices) {
            if (i == j) {
                continue
            }
            
            val manhattan = abs(positions[j].x - positions[i].x) + abs(positions[j].y - positions[i].y)
            totalManhattan += manhattan
        }
    }
    
    return totalManhattan / 2
}

fun findGalaxyPositions(expanded: List<String>): List<GalaxyPosition> {
    val positions = mutableListOf<GalaxyPosition>()
    
    expanded.forEachIndexed { y, row -> 
        row.forEachIndexed { x, cell -> 
            if (cell == '#') {
                positions.add(GalaxyPosition(x, y))
            }
        }
    }

    return positions
}

fun expandUniverse(lines: List<String>): List<String> {
    val expanded = mutableListOf<String>()
    val galaxyColCount = mutableListOf<Int>()
    
    lines.forEach {line ->
        galaxyColCount.add(0)
        expanded.add(line)
        if  (!line.contains("#")) {
            expanded.add(line)
        }
    }
    
    lines.forEach { line -> 
        line.forEachIndexed { index, cell -> 
            if (cell == '#') {
                galaxyColCount[index]++
            }
        }
    }
    
    expanded.forEachIndexed {i, line ->
        var newLine = ""
        
        line.forEachIndexed { j, cell ->
            newLine += cell
            if (galaxyColCount[j] == 0) {
                newLine += '.'
            }
        }
        
        expanded[i] = newLine
    }
    
    return expanded
}

data class GalaxyPosition(val x: Int, val y: Int)