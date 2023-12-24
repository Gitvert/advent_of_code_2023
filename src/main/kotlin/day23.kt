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

    val nonSlipperyPath = createNonSlipperyPath(lines)
    longestHike = -1

    val dryHikes = mutableListOf(DryHike(mutableSetOf(), Pos(1, 0), Pos(0, 0), 0))

    while (dryHikes.isNotEmpty()) {
        dryWalk(nonSlipperyPath, dryHikes, maxX, maxY)
    }

    println("Day 23 part 2: $longestHike")
    println()
}

fun walk(lines: List<String>, hikes: MutableList<Hike>, maxX: Int, maxY: Int) {
    val newHikes = mutableListOf<Hike>()

    hikes.forEach { hike ->
        if (hike.steps.contains(Pos(maxX - 1, maxY)) && hike.steps.size > longestHike) {
            //printHike(lines, hike.steps)
            longestHike = hike.steps.size
            println("longest: $longestHike - hikes: ${hikes.size}")
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

fun dryWalk(lines: List<String>, hikes: MutableList<DryHike>, maxX: Int, maxY: Int) {
    val newHikes = mutableListOf<DryHike>()

    hikes.forEach { hike ->
        val localNewHikes = mutableListOf<DryHike>()

        DIRECTIONS.forEach { dir ->
            val nextStep = getNextStep(lines, hike.pos, dir, hike.lastPos, hike.crossings, maxX, maxY)
            if (nextStep != null) {
                if (nextStep == Pos(maxX - 1, maxY)) {
                    if (hike.steps + 1 > longestHike) {
                        longestHike = hike.steps + 1
                        println("longest: $longestHike - hikes: ${hikes.size}")
                    }
                } else {
                    val newHike = DryHike(hike.crossings.toMutableSet(), nextStep, hike.pos, hike.steps + 1)
                    localNewHikes.add(newHike)
                }
            }
        }
        if (localNewHikes.size > 1) {
            localNewHikes.forEach {
                it.crossings.add(hike.pos)
            }
        }

        newHikes.addAll(localNewHikes)
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

fun getNextStep(lines: List<String>, pos: Pos, dir: Pos, lastPos: Pos, crossings: MutableSet<Pos>, maxX: Int, maxY: Int): Pos? {
    if (pos == Pos(maxX - 15, maxY - 11) && dir != Pos(0, 1)) {
        return null
    }

    val nextStep = Pos(pos.x + dir.x, pos.y + dir.y)

    if (nextStep.x in 0..maxX && nextStep.y in 0..maxY && lines[nextStep.y][nextStep.x] != '#' && !crossings.contains(nextStep) && nextStep != lastPos) {
        return nextStep
    }

    return null
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

data class DryHike(val crossings: MutableSet<Pos>, val pos: Pos, val lastPos: Pos, val steps: Int)