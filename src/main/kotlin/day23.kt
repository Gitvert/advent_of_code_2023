import java.util.*

val DIRECTIONS = mutableListOf(Pos(1, 0), Pos(-1, 0), Pos(0, 1), Pos(0, -1))
var longestHike = -1

fun day23 (lines: List<String>) {
    val hikes = mutableListOf(Hike(mutableSetOf(Pos(1, 0))))
    val maxX = lines[0].indices.last
    val maxY = lines.indices.last

    while (hikes.isNotEmpty()) {
        walk(lines, hikes, maxX, maxY)
    }

    longestHike--

    println("Day 23 part 1: $longestHike")
    
    val intersections = findIntersections(lines, maxX, maxY)
    val paths = findPaths(lines, maxX, maxY, intersections)
    
    dfs(paths, maxX, maxY)

    println("Day 23 part 2: $longestHike")
    println()
}

fun walk(lines: List<String>, hikes: MutableList<Hike>, maxX: Int, maxY: Int) {
    val newHikes = mutableListOf<Hike>()

    hikes.forEach { hike ->
        if (hike.steps.contains(Pos(maxX - 1, maxY)) && hike.steps.size > longestHike) {
            longestHike = hike.steps.size
        }
        val currentPos = hike.steps.last()

        DIRECTIONS.forEach { dir ->
            val nextSteps = getNextSteps(lines, currentPos, dir, hike.steps, maxX, maxY)
            if (nextSteps.isNotEmpty()) {
                val newHike = Hike(hike.steps.toMutableSet())
                newHike.steps.addAll(nextSteps)
                newHikes.add(newHike)
            }
        }
    }

    hikes.clear()
    hikes.addAll(newHikes)
}

fun getNextSteps(lines: List<String>, pos: Pos, dir: Pos, steps: MutableSet<Pos>, maxX: Int, maxY: Int): List<Pos> {
    if (pos == Pos(maxX - 15, maxY - 11) && dir != Pos(0, 1)) {
        return listOf()
    }

    val nextStep = Pos(pos.x + dir.x, pos.y + dir.y)

    if (nextStep.x in 0..maxX && nextStep.y in 0..maxY && lines[nextStep.y][nextStep.x] != '#' && !steps.contains(nextStep)) {
        val cell = lines[nextStep.y][nextStep.x]
        return when (cell) {
            '>' -> {
                if (!steps.contains(Pos(nextStep.x + 1, nextStep.y))) {
                    listOf(nextStep, Pos(nextStep.x + 1, nextStep.y))
                } else {
                    listOf()
                }

            }
            'v' -> {
                if (!steps.contains(Pos(nextStep.x, nextStep.y + 1))) {
                    listOf(nextStep, Pos(nextStep.x, nextStep.y + 1))
                } else {
                    listOf()
                }

            }
            else -> listOf(nextStep)
        }
    }

    return listOf()
}

fun dfs(paths: MutableMap<Pos, List<Pair<Pos, Int>>>, maxX: Int, maxY: Int) {
    val target = Pos(maxX - 1, maxY)
    val stack = ArrayDeque(listOf<DryHike>())
    stack.addLast(DryHike(mutableSetOf(), Pos(1, 0), 0))
    
    while (stack.isNotEmpty()) {
        val current = stack.removeLast()
        val currentIntersection = paths[current.pos]!!
        
        if (current.pos == target) {
            if (current.steps > longestHike) {
                longestHike = current.steps
                continue
            }
        }
        
        if (currentIntersection.map { it.first }.any { it == target }) {
            val intersection = currentIntersection.find { it.first == target }!!
            val newVisited = current.visited.toMutableSet()
            newVisited.add(intersection.first)
            stack.addLast(DryHike(newVisited, intersection.first, current.steps + intersection.second))
            continue
        }
        
        currentIntersection.forEach { intersection ->
            if (!current.visited.contains(intersection.first)) {
                val newVisited = current.visited.toMutableSet()
                newVisited.add(intersection.first)
                stack.addLast(DryHike(newVisited, intersection.first, current.steps + intersection.second))
            }
        }
    }
}

fun bfs(lines: List<String>, maxX: Int, maxY: Int, start: Pos, intersections: Set<Pos>): MutableList<Pair<Pos, Int>> {
    val queue: Queue<Path> = LinkedList()
    val paths = mutableListOf<Pair<Pos, Int>>()

    val visited = mutableListOf(start)
    queue.add(Path(start, 0))

    while(queue.isNotEmpty()) {
        val current = queue.peek()
        queue.remove()

        DIRECTIONS.forEach { dir ->
            val posToCheck = Pos(current.pos.x + dir.x, current.pos.y + dir.y)
            
            if (posToCheck != start && intersections.contains(posToCheck)) {
                paths.add(Pair(posToCheck, current.steps + 1))
            } else if (isValid(lines, maxX, maxY, posToCheck, visited)) {
                visited.add(posToCheck)
                queue.add(Path(posToCheck, current.steps + 1))
            }
        }
    }
    
    return paths
}

fun isValid(lines: List<String>, maxX: Int, maxY: Int, pos: Pos, visited: MutableList<Pos>): Boolean {
    if (visited.contains(pos)) {
        return false
    }

    return isWalkable(lines, pos, maxX, maxY)
}


fun findPaths(lines: List<String>, maxX: Int, maxY: Int, intersections: Set<Pos>): MutableMap<Pos, List<Pair<Pos, Int>>> {
    val paths = mutableMapOf<Pos, List<Pair<Pos, Int>>>()

    intersections.forEach { intersection ->
        paths[intersection] = bfs(lines, maxX, maxY, intersection, intersections)
    }
    
    return paths
}

fun findIntersections(lines: List<String>, maxX: Int, maxY: Int): MutableSet<Pos> {
    val intersections = mutableSetOf(Pos(1, 0), Pos(maxX - 1, maxY))
    
    for (y in lines.indices) {
        for (x in lines[0].indices) {
            if (lines[y][x] == '#') {
                continue
            }
            var walkableDirections = 0
            DIRECTIONS.forEach { dir ->
                if (isWalkable(lines, Pos(x + dir.x, y + dir.y), maxX, maxY)) {
                    walkableDirections++
                }
            }
            
            if (walkableDirections > 2) {
                intersections.add(Pos(x, y))
            }
        }
    }
    
    return intersections
}

fun isWalkable(lines: List<String>, pos: Pos, maxX: Int, maxY: Int): Boolean {
    return pos.x in 0..maxX && pos.y in 0..maxY && lines[pos.y][pos.x] != '#'
}

data class Path(val pos: Pos, val steps: Int)

data class Hike(val steps: MutableSet<Pos>)

data class DryHike(val visited: MutableSet<Pos>, val pos: Pos, val steps: Int)