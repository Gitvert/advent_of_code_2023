import kotlin.math.max

fun day16 (lines: List<String>) {
    val beams = if (lines[0][0] == '\\') {
        mutableListOf(Beam(0, 0, 0, 1))
    } else {
        mutableListOf(Beam(0, 0, 1, 0))
    }
    val part1 = findVisitedPositions(lines, beams, Pos(0, 0))
    
    println("Day 16 part 1: $part1")
    
    val part2 = max(max(max(enterFromLeft(lines), enterFromRight(lines)), enterFromTop(lines)), enterFromBottom(lines))
    
    println("Day 16 part 2: $part2")
    println()
}

fun enterFromLeft(lines: List<String>): Int {
    var highestVisited = Int.MIN_VALUE
    
    for (y in lines.indices) {
        val beams = mutableListOf<Beam>()
        
        when (lines[y][0]) {
            '\\' -> beams.add(Beam(0, y, 0, 1))
            '/' -> beams.add(Beam(0, y, 0, -1))
            '|' -> {
                beams.add(Beam(0, y, 0, 1))
                beams.add(Beam(0, y, 0, -1))
            }
            else -> beams.add(Beam(0, y, 1, 0))
        }
        
        val visited = findVisitedPositions(lines, beams, Pos(0, y))
        if (visited > highestVisited) {
            highestVisited = visited
        }
    }
    
    return highestVisited
}

fun enterFromRight(lines: List<String>): Int {
    var highestVisited = Int.MIN_VALUE

    for (y in lines.indices) {
        val beams = mutableListOf<Beam>()

        when (lines[y][lines[0].indices.last]) {
            '\\' -> beams.add(Beam(lines[0].indices.last, y, 0, -1))
            '/' -> beams.add(Beam(lines[0].indices.last, y, 0, 1))
            '|' -> {
                beams.add(Beam(lines[0].indices.last, y, 0, 1))
                beams.add(Beam(lines[0].indices.last, y, 0, -1))
            }
            else -> beams.add(Beam(lines[0].indices.last, y, -1, 0))
        }

        val visited = findVisitedPositions(lines, beams, Pos(lines[0].indices.last, y))
        if (visited > highestVisited) {
            highestVisited = visited
        }
    }

    return highestVisited
}

fun enterFromTop(lines: List<String>): Int {
    var highestVisited = Int.MIN_VALUE

    for (x in lines[0].indices) {
        val beams = mutableListOf<Beam>()

        when (lines[0][x]) {
            '\\' -> beams.add(Beam(x, 0, 1, 0))
            '/' -> beams.add(Beam(x, 0, -1, 0))
            '-' -> {
                beams.add(Beam(x, 0, 1, 0))
                beams.add(Beam(x, 0, -1, 0))
            }
            else -> beams.add(Beam(x, 0, 0, 1))
        }

        val visited = findVisitedPositions(lines, beams, Pos(x, 0))
        if (visited > highestVisited) {
            highestVisited = visited
        }
    }

    return highestVisited
}

fun enterFromBottom(lines: List<String>): Int {
    var highestVisited = Int.MIN_VALUE

    for (x in lines[0].indices) {
        val beams = mutableListOf<Beam>()

        when (lines[lines.indices.last][x]) {
            '\\' -> beams.add(Beam(x, lines.indices.last, -1, 0))
            '/' -> beams.add(Beam(x, lines.indices.last, 1, 0))
            '-' -> {
                beams.add(Beam(x, lines.indices.last, 1, 0))
                beams.add(Beam(x, lines.indices.last, -1, 0))
            }
            else -> beams.add(Beam(x, lines.indices.last, 0, -1))
        }

        val visited = findVisitedPositions(lines, beams, Pos(x, lines.indices.last))
        if (visited > highestVisited) {
            highestVisited = visited
        }
    }

    return highestVisited
}

fun findVisitedPositions(lines: List<String>, beams: MutableList<Beam>, startPos: Pos): Int {
    val visitedPositions = mutableSetOf(startPos)

    var stepsWithoutNewVisitedPositions = 0

    while (stepsWithoutNewVisitedPositions < 10) {
        val visitedBefore = visitedPositions.size
        step(lines, beams, visitedPositions)
        val visitedAfter = visitedPositions.size

        if (visitedBefore == visitedAfter) {
            stepsWithoutNewVisitedPositions++
        } else {
            stepsWithoutNewVisitedPositions = 0
        }
    }

    return visitedPositions.filter { it.x >= 0 && it.y >= 0 && it.x < lines[0].length && it.y < lines.size }.size
}

fun step(lines: List<String>, beams: MutableList<Beam>, visitedPositions: MutableSet<Pos>) {
    val newBeams = mutableListOf<Beam>()
    val removedBeams = mutableListOf<Beam>()
    
    beams.forEach { 
        it.xPos += it.xDir
        it.yPos += it.yDir
        
        if (stillInContraption(it, lines)) {
            val cell = lines[it.yPos][it.xPos]
            when (cell) {
                '.' -> {}
                '\\' -> {
                    if (it.xDir == 1) {
                        it.xDir = 0
                        it.yDir = 1
                    } else if (it.xDir == -1) {
                        it.xDir = 0
                        it.yDir = -1
                    } else if (it.yDir == 1) {
                        it.yDir = 0
                        it.xDir = 1
                    } else if (it.yDir == -1) {
                        it.yDir = 0
                        it.xDir = -1
                    }
                }
                '/' -> {
                    if (it.xDir == 1) {
                        it.xDir = 0
                        it.yDir = -1
                    } else if (it.xDir == -1) {
                        it.xDir = 0
                        it.yDir = 1
                    } else if (it.yDir == 1) {
                        it.yDir = 0
                        it.xDir = -1
                    } else if (it.yDir == -1) {
                        it.yDir = 0
                        it.xDir = 1
                    }
                }
                '-' -> {
                    if (it.xDir == 0) {
                        it.yDir = 0
                        it.xDir = 1
                        newBeams.add(Beam(it.xPos, it.yPos, -1, 0))
                    }
                }
                '|' -> {
                    if (it.yDir == 0) {
                        it.xDir = 0
                        it.yDir = 1
                        newBeams.add(Beam(it.xPos, it.yPos, 0, -1))
                    }
                }
            }
        } else {
            removedBeams.add(it)
        }
    }
    
    beams.removeAll(removedBeams)
    beams.addAll(newBeams.filter { !beams.contains(it) })
    
    beams.forEach { 
        visitedPositions.add(Pos(it.xPos, it.yPos))
    }
}

fun stillInContraption(beam: Beam, lines: List<String>): Boolean {
    val xRange = lines[0].indices
    val yRange = lines.indices
    
    return xRange.contains(beam.xPos) && yRange.contains(beam.yPos)
}

data class Beam(var xPos: Int, var yPos: Int, var xDir: Int, var yDir: Int)