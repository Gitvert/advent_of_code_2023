fun day9 (lines: List<String>) {
    val valueHistories = parseOasisInput(lines)
    
    var sum = 0L
    var sum2 = 0L
    
    valueHistories.forEach { 
        sum += findExtrapolatedValue(it)
        sum2 += findBackwardsExtrapolatedValue(it)
    }    
    
    println("Day 9 part 1: $sum")
    println("Day 9 part 2: $sum2")
    println()
}

fun findBackwardsExtrapolatedValue(numbers: List<Long>): Long {
    val sequences = buildExtrapolatedTree(numbers)
    
    val reversedSequences = sequences.map { it.reversed().toMutableList() }

    for (i in 0..<reversedSequences.size-1) {
        reversedSequences[i + 1].add(reversedSequences[i + 1].last() - reversedSequences[i].last())
    }

    return reversedSequences.last().last()
}

fun findExtrapolatedValue(numbers: List<Long>): Long {
    val sequences = buildExtrapolatedTree(numbers)

    for (i in 0..<sequences.size-1) {
        sequences[i + 1].add(sequences[i + 1].last() + sequences[i].last())
    }

    return sequences.last().last()
}

fun buildExtrapolatedTree(numbers: List<Long>): List<MutableList<Long>> {
    val sequences = mutableListOf<MutableList<Long>>()

    sequences.add(numbers.toMutableList())

    while (!sequences.last().all { it == 0L }) {
        val nextSequence = mutableListOf<Long>()

        for (i in 0..<sequences.last().size-1) {
            nextSequence.add(sequences.last()[i+1] - sequences.last()[i])
        }

        sequences.add(nextSequence)
    }

    sequences.removeLast()

    return sequences.reversed()
}

fun parseOasisInput(lines: List<String>): List<List<Long>> {
    return lines.map { line -> line.split(" ").map { it.toLong() } }
}