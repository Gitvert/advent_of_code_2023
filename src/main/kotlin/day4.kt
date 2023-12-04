fun day4 (lines: List<String>) {
    day4part1(lines)
    day4part2(lines)

    
    println()
}

fun day4part2(lines: List<String>) {
    val winsPerCard = mutableMapOf<Int, Int>()
    val noOfCards = mutableListOf<Int>()
    
    lines.forEachIndexed { index, line ->
        winsPerCard[index] = findMatches(line)
        noOfCards.add(1)
    }
    
    for (i in lines.indices) {
        for (j in 0..<noOfCards[i]) {
            for (k in 1..winsPerCard[i]!!) {
                noOfCards[i + k]++
            }
        }
    }
    
    val noOfCardsSum = noOfCards.sum()
    
    println("Day 4 part 2: $noOfCardsSum")
}

fun day4part1(lines: List<String>) {
    var points = 0

    lines.forEach { line ->
        val noOfWinningNumbers = findMatches(line)
        var cardScore = 0

        if (noOfWinningNumbers > 0) {
            cardScore = 1

            for (i in 1..<noOfWinningNumbers) {
                cardScore*=2
            }
        }

        points += cardScore
    }

    println("Day 4 part 1: $points")
}

fun findMatches(card: String): Int {
    val winningNumbers = card.replace("  ", " ").split(": ")[1].split(" | ")[0].split(" ").map { Integer.parseInt(it) }
    val myNumbers = card.replace("  ", " ").split(": ")[1].split(" | ")[1].split(" ").map { Integer.parseInt(it) }

    val myWinningNumbers = winningNumbers.intersect(myNumbers.toSet())
    
    return myWinningNumbers.count()
}