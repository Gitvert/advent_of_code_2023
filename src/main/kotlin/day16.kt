fun day16 (lines: List<String>) {
    val beams = if (lines[0][0] == '\\') {
        mutableListOf(Beam(0, 0, 0, 1))
    } else {
        mutableListOf(Beam(0, 0, 1, 0))
    }
    val visitedPositions = mutableSetOf(Pos(0, 0))
    
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
    
    val validVisitedPositions = visitedPositions.filter { it.x >= 0 && it.y >= 0 && it.x < lines[0].length && it.y < lines.size }
    
    println("Day 16 part 1: ${validVisitedPositions.size}")
    
    println("Day 16 part 2: ")
    println()
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