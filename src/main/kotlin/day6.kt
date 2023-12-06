fun day6 (lines: List<String>) {
    
    val races = parseRaces(lines)
    var part1 = 1L
    
    races.forEach { 
        part1 *= getWaysToWin(it)
    }
    
    val longRace = parseRace(lines)
    val part2 = getWaysToWin(longRace)

    println("Day 6 part 1: $part1")
    println("Day 6 part 2: $part2")
    println()
}

fun getWaysToWin(race: Race): Int {
    val distances = mutableListOf<Long>()
    
    for (i in 0..<race.time) {
        val distance = (race.time - i) * i

        distances.add(distance)
    }
    
    return distances.filter { it > race.recordDistance }.count()
}

fun parseRace(lines: List<String>): Race {
    val time = lines[0].replace("\\s+".toRegex(), "").split(":")[1].split(" ").map { it.toLong() }[0]
    val distance = lines[1].replace("\\s+".toRegex(), "").split(":")[1].split(" ").map { it.toLong() }[0]
    
    return Race(time, distance)
}

fun parseRaces(lines: List<String>): List<Race> {
    val times = lines[0].replace("\\s+".toRegex(), " ").split(": ")[1].split(" ").map { it.toLong() }
    val distances = lines[1].replace("\\s+".toRegex(), " ").split(": ")[1].split(" ").map { it.toLong() }
    
    val races = mutableListOf<Race>()
    
    for (i in times.indices) {
        races.add(Race(times[i], distances[i]))
    }
    
    return races
}

data class Race (val time: Long, val recordDistance: Long)