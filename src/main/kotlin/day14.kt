fun day14 (lines: List<String>) {
    val rocks = parseRocks(lines)
    var tiltedRocks = tiltNorth(rocks)
    var lastTilt = mutableListOf<MutableList<Rock>>()
    
    while (tiltedRocks != lastTilt) {
        lastTilt = tiltedRocks
        
        tiltedRocks = tiltNorth(lastTilt)
    }

    var totalLoad = 0L

    tiltedRocks.reversed().forEachIndexed { index, rows ->
        rows.forEach {
            if (it.rock == 'O') {
                totalLoad += index + 1
            }
        }
    }

    println("Day 14 part 1: $totalLoad")

    val loop = findLoop(rocks)
    
    val correctIndex = (1000000000 - loop.values.first()[0]) % (loop.values.first()[1] - loop.values.first()[0])
    totalLoad = 0L
    loop.keys.toMutableList()[correctIndex - 1].reversed().forEachIndexed { index, rows ->
        rows.forEach {
            if (it.rock == 'O') {
                totalLoad += index + 1
            }
        }
    }

    println("Day 14 part 2: $totalLoad")
    println()
}

fun findLoop(rocks: MutableList<MutableList<Rock>>): Map<MutableList<MutableList<Rock>>, MutableList<Int>> {
    var tiltedRocks = rocks
    var lastTilt: MutableList<MutableList<Rock>>
    val storedRockFormations = mutableMapOf<MutableList<MutableList<Rock>>, MutableList<Int>>()

    for (i in 0..999999999) {
        
        //NORTH
        tiltedRocks = tiltNorth(tiltedRocks)
        lastTilt = mutableListOf()

        while (tiltedRocks != lastTilt) {
            lastTilt = tiltedRocks

            tiltedRocks = tiltNorth(lastTilt)
        }

        //WEST
        tiltedRocks = rotateLeft(tiltedRocks)
        tiltedRocks = tiltNorth(tiltedRocks)
        lastTilt = mutableListOf()

        while (tiltedRocks != lastTilt) {
            lastTilt = tiltedRocks

            tiltedRocks = tiltNorth(lastTilt)
        }

        //SOUTH
        tiltedRocks = rotateLeft(tiltedRocks)
        tiltedRocks = tiltNorth(tiltedRocks)
        lastTilt = mutableListOf()

        while (tiltedRocks != lastTilt) {
            lastTilt = tiltedRocks

            tiltedRocks = tiltNorth(lastTilt)
        }

        //EAST
        tiltedRocks = rotateLeft(tiltedRocks)
        tiltedRocks = tiltNorth(tiltedRocks)
        lastTilt = mutableListOf()

        while (tiltedRocks != lastTilt) {
            lastTilt = tiltedRocks

            tiltedRocks = tiltNorth(lastTilt)
        }
        tiltedRocks = rotateLeft(tiltedRocks)

        val rockString = convertToString(tiltedRocks)

        val indicesList = if (storedRockFormations.containsKey(tiltedRocks)) {
            storedRockFormations[tiltedRocks]!!
        } else {
            mutableListOf()
        }
        
        if (indicesList.size == 2) {
            return storedRockFormations.filter { it.value.size >= 2 }
        }

        indicesList.add(i)
        storedRockFormations[tiltedRocks] = indicesList
    }
    
    return storedRockFormations
}

fun tiltNorth(rocks: MutableList<MutableList<Rock>>): MutableList<MutableList<Rock>> {
    val tiltedRocks = mutableListOf<MutableList<Rock>>()
    
    for (y in rocks.indices) {
        val newRow = mutableListOf<Rock>()
        for (x in rocks[y].indices) {
            if (y == 0) {
                newRow.add(rocks[y][x])
            } else {
                if (tiltedRocks[y-1][x].rock == '.' && rocks[y][x].rock == 'O') {
                    tiltedRocks[y-1][x] = Rock('O')
                    newRow.add(Rock('.'))
                } else {
                    newRow.add(rocks[y][x])
                }
            }
        }
        tiltedRocks.add(newRow)
    }
    
    return tiltedRocks
}

fun convertToString(rocks: MutableList<MutableList<Rock>>): String {
    var rockString = ""
    
    rocks.forEach { rock ->
        rock.forEach { 
            rockString += it.toString()
        }
    }
    
    return rockString
}

fun rotateLeft(rocks: MutableList<MutableList<Rock>>): MutableList<MutableList<Rock>> {
    val rotated = mutableListOf<MutableList<Rock>>()

    for (y in rocks.indices) {
        val newRow = mutableListOf<Rock>()
        for (x in rocks[y].indices) {
            newRow.add(rocks[y][x])
        }
        rotated.add(newRow)
    }

    for (y in rocks.indices) {
        for (x in rocks.indices) {
            val temp = rocks[y][x]
            rotated[y][x] = rocks[x][y]
            rotated[x][y] = temp
        }
    }
    
    return rotated.map { it.reversed().toMutableList() }.toMutableList()
}

fun parseRocks(lines: List<String>): MutableList<MutableList<Rock>> {
    val rocks = mutableListOf<MutableList<Rock>>()

    for (y in lines.indices) {
        val newRow = mutableListOf<Rock>()
        for (x in lines[y].indices) {
            newRow.add(Rock(lines[y][x]))
        }
        rocks.add(newRow)
    }
    
    return rocks
}

data class Rock(val rock: Char)