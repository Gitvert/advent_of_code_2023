val DIRECTIONS = mutableListOf(Pos(1, 0), Pos(-1, 0), Pos(0, 1), Pos(0, -1))
var longestHike = -1

fun day23 (lines: List<String>) {
    val hikes = mutableListOf(Hike(mutableSetOf(Pos(1, 0))))
    val maxX = lines[0].indices.last
    val maxY = lines.indices.last

    var totalSteps = 1
    var stepsHistory = -1

    while (totalSteps != stepsHistory) {
        stepsHistory = totalSteps
        walk(lines, hikes, maxX, maxY)

        totalSteps = hikes.sumOf { it.steps.size }
    }

    longestHike--

    println("Day 23 part 1: $longestHike")

    val nonSlipperyPath = createNonSlipperyPath(lines)
    longestHike = -1

    totalSteps = 1
    stepsHistory = -1
    hikes.add(Hike(mutableSetOf(Pos(1, 0))))

    while (totalSteps != stepsHistory) {
        stepsHistory = totalSteps
        walk(nonSlipperyPath, hikes, maxX, maxY)

        totalSteps = hikes.sumOf { it.steps.size }
    }

    longestHike--

    println("Day 23 part 2: $longestHike")
    println()
}

fun walk(lines: List<String>, hikes: MutableList<Hike>, maxX: Int, maxY: Int) {
    val newHikes = mutableListOf<Hike>()

    hikes.forEach { hike ->
        if (hike.steps.contains(Pos(maxX - 1, maxY)) && hike.steps.size > longestHike) {
            //printHike(lines, hike.steps)
            longestHike = hike.steps.size
            println(longestHike)
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
    if (pos == Pos(maxX - 1, maxY - 1) && dir != Pos(0, 1)) {
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

fun createNonSlipperyPath(lines: List<String>): MutableList<String> {
    val path = mutableListOf<String>()

    lines.forEach { line ->
        path.add(line.replace(">", ".").replace("v", "."))
    }

    return path
}

fun printHike(lines: List<String>, steps: MutableSet<Pos>) {
    for (y in lines.indices) {
        for (x in lines[0].indices) {
            if (steps.contains(Pos(x, y))) {
                print('O')
            } else {
                print(lines[y][x])
            }
        }
        println()
    }
    println()
}

data class Hike(val steps: MutableSet<Pos>)