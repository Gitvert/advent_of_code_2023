import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random

val random: Random = Random(1337)

fun day22 (lines: List<String>) {

    val bricks = parseBricks(lines)
    val settledBricks = settleBricks(bricks.sortedBy { it.start.z })
    removeBlocks(settledBricks)
}

fun removeBlocks(bricks: List<Brick>) {
    var safeToDisintegrate = 0
    var totalFallen = 0
    
    bricks.forEach { brick ->
        val settledBricks = settleBricks(bricks.filterNot { brick == it }).toMutableList()

        if (settledBricks == bricks.filterNot { brick == it }) {
            safeToDisintegrate++
        } else {
            val unchanged = settledBricks.intersect(bricks.filterNot { brick == it }.toSet()).size
            totalFallen += settledBricks.size - unchanged
        }
    }
    
    
    println("Day 22 part 1: $safeToDisintegrate")
    println("Day 22 part 2: $totalFallen")
    println()
}

fun settleBricks(bricks: List<Brick>): List<Brick> {
    var settledBricks = bricks.toMutableList()
    val changedBricks = mutableListOf<Brick>()

    while (true) {
        for (i in settledBricks.indices) {
            val minZ = min(settledBricks[i].start.z, settledBricks[i].end.z)
            if (minZ == 1) {
                changedBricks.add(settledBricks[i])
                continue
            }

            var isBlocked = false
            for (j in settledBricks.indices) {
                if (i == j) {
                    continue
                }

                if (restsOn(settledBricks[i], settledBricks[j])) {
                    isBlocked = true
                }
            }

            if (!isBlocked) {
                changedBricks.add(moveDown(settledBricks[i]))
            } else {
                changedBricks.add(settledBricks[i])
            }
        }

        if (settledBricks == changedBricks) {
            break
        }
        settledBricks = changedBricks.toMutableList()
        changedBricks.clear()
    }
    
    return settledBricks
}

fun moveDown(brick: Brick): Brick {
    return Brick(
        Point(brick.start.x, brick.start.y, brick.start.z - 1),
        Point(brick.end.x, brick.end.y, brick.end.z - 1),
        brick.id,
    )
}

fun restsOn(current: Brick, other: Brick): Boolean {
    val currentMinZ = min(current.start.z, current.end.z)
    val otherMaxZ = max(other.start.z, other.end.z)
    
    if (currentMinZ - 1 != otherMaxZ) {
        return false
    }
    
    val currentPositionsToCheck = generateAllPositionsToCheck(current)
    val otherPositionsToCheck = generateAllPositionsToCheck(other)

    return currentPositionsToCheck.any { it in otherPositionsToCheck.toSet() }
}

fun generateAllPositionsToCheck(brick: Brick): List<Pos> {
    val allPositions = mutableListOf<Pos>()
    
    if (brick.start.x == brick.end.x) {
        val from = min(brick.start.y, brick.end.y)
        val to = max(brick.start.y, brick.end.y)
        
        for (i in from..to) {
            allPositions.add(Pos(brick.start.x, i))
        }
    } else if (brick.start.y == brick.end.y) {
        val from = min(brick.start.x, brick.end.x)
        val to = max(brick.start.x, brick.end.x)

        for (i in from..to) {
            allPositions.add(Pos(i, brick.start.y))
        }
    }
    
    return allPositions
}

fun parseBricks(lines: List<String>): MutableList<Brick> {
    val bricks = mutableListOf<Brick>()
    
    lines.forEach { 
        val startString = it.split("~")[0]
        val endString = it.split("~")[1]
        val startNumbers = startString.split(",")
        val endNumbers = endString.split(",")
        val start = Point(Integer.parseInt(startNumbers[0]), Integer.parseInt(startNumbers[1]), Integer.parseInt(startNumbers[2]))
        val end = Point(Integer.parseInt(endNumbers[0]), Integer.parseInt(endNumbers[1]), Integer.parseInt(endNumbers[2]))
        bricks.add(Brick(start, end, random.nextInt()))
    }
    
    return bricks
}

data class Brick(val start: Point, val end: Point, val id: Int)

data class Point(val x: Int, val y: Int, val z: Int)