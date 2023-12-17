var steps = 0
fun day17 (lines: List<String>) {
    val map = parseFactoryMap(lines)
    val maxX = map[0].indices.last
    val maxY = map.indices.last
    var states = mutableSetOf(CrucibleState(0, Pos(0, 0), Pos(0, 0), Pos(0, 0), 0, 0, mutableMapOf()))
    var endStates = mutableSetOf<CrucibleState>()
    var leastHeat = findBaselineHeat(map, maxX, maxY)

    while (states.isNotEmpty()) {
        states = makeAllValidMoves(map, states, maxX, maxY, leastHeat)
        
        endStates.addAll(states.filter { it.position.x == maxX && it.position.y == maxY })
        if (endStates.isNotEmpty()) {
            leastHeat = endStates.minOf { it.heatLoss }
        }
        endStates = endStates.filter { it.heatLoss <= leastHeat }.toMutableSet()
        states = states.filterNot { it.position.x == maxX && it.position.y == maxY }.toMutableSet()
    }
    
    
    println("Day 17 part 1: $leastHeat")
    println("Day 17 part 2: ")
    println()
}

fun makeAllValidMoves(map: List<List<Int>>, states: MutableSet<CrucibleState>, maxX: Int, maxY: Int, leastHeat: Int): MutableSet<CrucibleState> {
    steps++
    val newStates = mutableSetOf<CrucibleState>()
    
    states.forEach { state ->
        val allDirections = mutableListOf(
            CrucibleState(state.heatLoss, Pos(state.position.x - 1, state.position.y), Pos(-1, 0), state.direction, state.stepsInSameDirection, state.steps + 1, state.visitsPerPosition.toMutableMap()),
            CrucibleState(state.heatLoss, Pos(state.position.x + 1, state.position.y), Pos(1, 0), state.direction, state.stepsInSameDirection, state.steps + 1, state.visitsPerPosition.toMutableMap()),
            CrucibleState(state.heatLoss, Pos(state.position.x, state.position.y + 1), Pos(0, 1), state.direction, state.stepsInSameDirection, state.steps + 1, state.visitsPerPosition.toMutableMap()),
            CrucibleState(state.heatLoss, Pos(state.position.x, state.position.y - 1), Pos(0, -1), state.direction, state.stepsInSameDirection, state.steps + 1, state.visitsPerPosition.toMutableMap()),
        )
        
        val validPositions = allDirections
            .filter { it.position.x >= 0 && it.position.y >= 0 && it.position.x <= maxX && it.position.y <= maxY }
            .filterNot { it.position.x in 40..99 && it.position.y in 40..99 }
        
        validPositions.forEach {
            it.stepsInSameDirection = if (it.direction == it.lastDirection) {
                it.stepsInSameDirection + 1
            } else {
                1
            }

            it.heatLoss += map[it.position.y][it.position.x]
            
            if (it.visitsPerPosition.containsKey(it.position)) {
                it.visitsPerPosition[it.position] = it.visitsPerPosition[it.position]!! + 1
            } else {
                it.visitsPerPosition[it.position] = 1
            }
        }
        
        val validStates = validPositions.filter {
            it.stepsInSameDirection < 4 
                    && it.heatLoss < leastHeat 
                    && !oppositeDirection(it.direction, it.lastDirection)
                    && (if (it.steps < 20) {
                        it.position.x + it.position.y > it.steps / 2
                    } else {
                        (it.position.x + it.position.y) / steps.toDouble() > 0.8
                    })
                    && it.visitsPerPosition.values.max() <= 1
        }        
        
        val minHeatLoss = if (validStates.isNotEmpty()) {
            validStates.minOf { it.heatLoss }
        } else {
            0
        }
        
        val validHeatLoss = validStates.filter { it.heatLoss - minHeatLoss < 20 }
        
        newStates.addAll(validHeatLoss)
    }
    
    return newStates
}

fun findBaselineHeat(map: List<List<Int>>, maxX: Int, maxY: Int): Int {
    var currentHeat = 0
    var currentPos = Pos(0, 0)
    var moveX = true
    
    while (currentPos.x != maxX && currentPos.y != maxY) {
        currentPos = if (moveX) {
            Pos(currentPos.x + 1, currentPos.y)
        } else {
            Pos(currentPos.x, currentPos.y + 1)
        }
        
        moveX = !moveX
        currentHeat += map[currentPos.y][currentPos.x]
    }
    
    return currentHeat
}

fun oppositeDirection(left: Pos, right: Pos): Boolean {
    return left.x * -1 == right.x && left.y * -1 == right.y
}

fun parseFactoryMap(lines: List<String>): List<List<Int>> {
    val map = mutableListOf<MutableList<Int>>()
    
    for (y in lines.indices) {
        val row = mutableListOf<Int>()
        for (x in lines[0].indices) {
            row.add(Integer.parseInt(lines[y][x].toString()))
        }
        map.add(row)
    }
    
    return map
}

data class CrucibleState(
    var heatLoss: Int,
    var position: Pos, 
    var direction: Pos, 
    var lastDirection: Pos,
    var stepsInSameDirection: Int, 
    var steps: Int,
    val visitsPerPosition: MutableMap<Pos, Int>
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is CrucibleState) return false

        if (heatLoss != other.heatLoss) return false
        if (position != other.position) return false
        if (direction != other.direction) return false
        if (lastDirection != other.lastDirection) return false
        if (stepsInSameDirection != other.stepsInSameDirection) return false
        if (steps != other.steps) return false

        return true
    }

    override fun hashCode(): Int {
        var result = heatLoss
        result = 31 * result + position.hashCode()
        result = 31 * result + direction.hashCode()
        result = 31 * result + lastDirection.hashCode()
        result = 31 * result + stepsInSameDirection
        result = 31 * result + steps
        return result
    }
}