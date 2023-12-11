import kotlin.math.abs

const val EXPANSION_TIMES = 999999L

fun day11 (lines: List<String>) {
    day11part1(lines)
    day11part2(lines)
}

fun day11part1(lines: List<String>) {
    val expanded = expandUniverse(lines)
    val positions = findGalaxyPositions(expanded)
    val totalManhattan = findManhattanDistance(positions)

    println("Day 11 part 1: $totalManhattan")
}

fun day11part2(lines: List<String>) {
    val positions = findGalaxyPositions(lines)
    val emptyRows = findEmptyRows(lines)
    val emptyCols = findEmptyCols(lines)
    
    val totalManhattan = findManhattanDistanceWithExpansions(positions, emptyRows, emptyCols)
    
    println("Day 11 part 2: $totalManhattan")
    println()
}

fun findManhattanDistanceWithExpansions(positions: List<GalaxyPosition>, emptyRows: List<Int>, emptyCols: List<Int>): Long {
    var totalManhattan = 0L

    for (i in positions.indices) {
        for (j in i+1..<positions.size) {

            val manhattan = abs(positions[j].x - positions[i].x) + abs(positions[j].y - positions[i].y)
            val crossedEmptyRows = emptyRows.filter { positions[i].y < it && positions[j].y > it }.size
            var crossedEmptyCols = 0

            crossedEmptyCols = if (positions[i].x > positions[j].x) {
                emptyCols.filter { positions[j].x < it && positions[i].x > it}.size
            } else {
                emptyCols.filter { positions[i].x < it && positions[j].x > it}.size
            }
            
            totalManhattan += (manhattan + (crossedEmptyRows * EXPANSION_TIMES) + (crossedEmptyCols * EXPANSION_TIMES))
        }
    }
    
    return totalManhattan
}

fun findEmptyRows(lines: List<String>): List<Int> {
    val emptyRows = mutableListOf<Int>()
    
    lines.forEachIndexed { index, line ->
        if  (!line.contains("#")) {
            emptyRows.add(index)
        }
    }
    
    return emptyRows
}

fun findEmptyCols(lines: List<String>): List<Int> {
    val emptyCols = mutableListOf<Int>()
    val galaxyColCount = mutableListOf<Int>()

    lines.forEach { _ ->
        galaxyColCount.add(0)
    }

    lines.forEach { line ->
        line.forEachIndexed { index, cell ->
            if (cell == '#') {
                galaxyColCount[index]++
            }
        }
    }
    
    galaxyColCount.forEachIndexed { index, it -> 
        if (it == 0) {
            emptyCols.add(index)
        }
    }
    
    return emptyCols
}

fun findManhattanDistance(positions: List<GalaxyPosition>): Int {
    var totalManhattan = 0

    for (i in positions.indices) {
        for (j in i+1..<positions.size) {
            
            val manhattan = abs(positions[j].x - positions[i].x) + abs(positions[j].y - positions[i].y)
            totalManhattan += manhattan
        }
    }
    
    return totalManhattan
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