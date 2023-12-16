fun day15 (lines: List<String>) {
    val initializationSequence = parseInitializationSequence(lines)
    val sum = initializationSequence.sumOf { calculateHashValue(it) }
    println("Day 15 part 1: $sum")
    
    val lenses = parseLenses(lines)
    val boxes = mutableListOf<MutableList<Lens>>()
    
    for (i in 0..255) {
        boxes.add(mutableListOf())    
    }
    
    lenses.forEach { lens -> 
        val hash = calculateHashValue(lens.label)
        if (lens.operation == '-') {
            removeLens(boxes, hash, lens)
        } else {
            addLens(boxes, hash, lens)
        }
    }
    
    val totalFocusingPower = boxes.mapIndexed { index, box -> 
        calculateFocusingPower(box, index)
    }.sum()
    
    println("Day 15 part 2: $totalFocusingPower")
    println()
}

fun calculateFocusingPower(lenses: List<Lens>, boxNumber: Int): Int {
    return lenses.mapIndexed { index, lens ->
        (boxNumber + 1) * (index + 1) * lens.focalLength
    }.sum()
}

fun addLens( boxes: MutableList<MutableList<Lens>>, hash: Int, lens: Lens) {
    if (boxes[hash].map { it.label }.contains(lens.label)) {
        boxes[hash].replaceAll { 
            if (it.label == lens.label) {
                lens
            } else {
                it
            }
        }
    } else {
        boxes[hash].add(lens)
    }
}

fun removeLens( boxes: MutableList<MutableList<Lens>>, hash: Int, lens: Lens) {
    if (boxes[hash].map { it.label }.contains(lens.label)) {
        boxes[hash].removeIf { it.label == lens.label }
    }
}

fun calculateHashValue(characters: String): Int {
    var currentValue = 0
    
    characters.forEach { 
        currentValue += it.code
        currentValue *= 17
        currentValue %= 256
    }
    
    return currentValue
}

fun parseLenses(lines: List<String>): List<Lens> {
    return lines[0].split(",").map { 
        if (it.contains("-")) {
            Lens(it.split("-")[0], '-', 0)
        } else {
            Lens(it.split("=")[0], '=', Integer.parseInt(it.split("=")[1]))
        }
    }
}

fun parseInitializationSequence(lines: List<String>): List<String> {
    return lines[0].split(",")
}

data class Lens(val label: String, val operation: Char, val focalLength: Int)