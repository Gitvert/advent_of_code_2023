fun day5 (lines: List<String>) {
    
    val seeds = parseSeeds(lines)
    val seedToSoil = parseAlmanacEntry(lines, "seed-to-soil")
    val soilToFertilizer = parseAlmanacEntry(lines, "soil-to-fertilizer")
    val fertilizerToWater = parseAlmanacEntry(lines, "fertilizer-to-water")
    val waterToLight = parseAlmanacEntry(lines, "water-to-light")
    val lightToTemperature = parseAlmanacEntry(lines, "light-to-temperature")
    val temperatureToHumidity = parseAlmanacEntry(lines, "temperature-to-humidity")
    val humidityToLocation = parseAlmanacEntry(lines, "humidity-to-location")
    
    val locations = seeds.map { 
        seedToLocation(
            it,
            seedToSoil,
            soilToFertilizer,
            fertilizerToWater,
            waterToLight,
            lightToTemperature,
            temperatureToHumidity,
            humidityToLocation,
        )
    }
    
    println("Day 5 part 1: ${locations.min()}")
    
    val ranges = parseSeedRanges(lines)
    
    var location = 0L
    
    while (location < Long.MAX_VALUE) {
        val seed = locationToSeed(
            location,
            seedToSoil,
            soilToFertilizer,
            fertilizerToWater,
            waterToLight,
            lightToTemperature,
            temperatureToHumidity,
            humidityToLocation,
        )
        
        for (i in ranges.indices) {
            if (seed >= ranges[i].first && seed <= ranges[i].second) {
                println("Day 5 part 2: $location")
                println()
                return
            }
        }
        
        location++
    }
}

fun seedToLocation(
    seed: Long,
    seedToSoil: List<AlmanacEntry>,
    soilToFertilizer: List<AlmanacEntry>,
    fertilizerToWater: List<AlmanacEntry>,
    waterToLight: List<AlmanacEntry>,
    lightToTemperature: List<AlmanacEntry>,
    temperatureToHumidity: List<AlmanacEntry>,
    humidityToLocation: List<AlmanacEntry>
): Long {
    val soil = findDestination(seedToSoil, seed)
    val fertilizer = findDestination(soilToFertilizer, soil)
    val water = findDestination(fertilizerToWater, fertilizer)
    val light = findDestination(waterToLight, water)
    val temperature = findDestination(lightToTemperature, light)
    val humidity = findDestination(temperatureToHumidity, temperature)

    return findDestination(humidityToLocation, humidity)
}

fun findDestination(destinationMap: List<AlmanacEntry>, source: Long): Long {
    destinationMap.forEach { 
        if (it.sourceRangeStart <= source && (it.sourceRangeStart + it.rangeLength - 1) >= source ) {
            return source + (it.destinationRangeStart - it.sourceRangeStart)
        }
    }
    
    return source
}

fun locationToSeed(
    location: Long,
    seedToSoil: List<AlmanacEntry>,
    soilToFertilizer: List<AlmanacEntry>,
    fertilizerToWater: List<AlmanacEntry>,
    waterToLight: List<AlmanacEntry>,
    lightToTemperature: List<AlmanacEntry>,
    temperatureToHumidity: List<AlmanacEntry>,
    humidityToLocation: List<AlmanacEntry>
): Long { 
    val humidity = findSource(humidityToLocation, location)
    val temperature = findSource(temperatureToHumidity, humidity)
    val light = findSource(lightToTemperature, temperature)
    val water = findSource(waterToLight, light)
    val fertilizer = findSource(fertilizerToWater, water)
    val soil = findSource(soilToFertilizer, fertilizer)
    
    return findSource(seedToSoil, soil)
}

fun findSource(destinationMap: List<AlmanacEntry>, destination: Long): Long {
    destinationMap.forEach { 
        if (it.destinationRangeStart <= destination && (it.destinationRangeStart + it.rangeLength - 1 >= destination)) {
            return destination + (it.sourceRangeStart - it.destinationRangeStart)
        }
    }
    
    return destination
}

fun parseSeeds(lines: List<String>): List<Long> {
    return lines[0].split(": ")[1].split(" ").map { it.toLong() }
}

fun parseSeedRanges(lines: List<String>): List<Pair<Long, Long>> {
    val numbers = lines[0].split(": ")[1].split(" ").map { it.toLong() }
    val ranges = mutableListOf<Pair<Long, Long>>()
    
    for (i in numbers.indices step 2) {
        ranges.add(Pair(numbers[i], numbers[i] + numbers[i+1] - 1))
    }
    
    return ranges
}

fun parseAlmanacEntry(lines: List<String>, identifier: String): List<AlmanacEntry> {
    var startFound = false
    val seedToSoil = mutableListOf<AlmanacEntry>()
    
    lines.forEach { 
        if (startFound && it.isNotBlank()) {
            seedToSoil.add(AlmanacEntry(
                it.split(" ")[0].toLong(),
                it.split(" ")[1].toLong(),
                it.split(" ")[2].toLong(),
            ))
        }
        
        if (startFound && it.isBlank()) {
            return seedToSoil
        }
        
        if (it.contains(identifier)) {
            startFound = true
        }
    }
    
    return seedToSoil
}

data class AlmanacEntry(val destinationRangeStart: Long, val sourceRangeStart: Long, val rangeLength: Long)