fun day7 (lines: List<String>) {
    day7part1(lines)
    day7part2(lines)
}

fun day7part2(lines: List<String>) {
    val cards = parseCamelCards(lines)

    cards.forEach { it.handStrength = findHandStrength(replaceJokers(it)) }

    cards.forEach {
        it.cards = it.cards
            .replace("A", "Z")
            .replace("K", "Y")
            .replace("Q", "X")
            .replace("J", "1")
            .replace("T", "U")
    }

    var sum = 0L

    cards.sortedWith(compareBy({ it.handStrength }, { it.cards })).forEachIndexed { index, card ->
        sum += (index + 1) * card.bid
    }

    println("Day 7 part 2: $sum")
    println() 
}

fun day7part1(lines: List<String>) {
    val cards = parseCamelCards(lines)
    
    cards.forEach { it.handStrength = findHandStrength(it) }

    cards.forEach {
        it.cards = it.cards
            .replace("A", "Z")
            .replace("K", "Y")
            .replace("Q", "X")
            .replace("J", "V")
            .replace("T", "U")
    }

    var sum = 0L

    cards.sortedWith(compareBy({ it.handStrength }, { it.cards })).forEachIndexed { index, card ->
        sum += (index + 1) * card.bid
    }

    println("Day 7 part 1: $sum")
}

fun findHandStrength(hand: CamelCardsHand): Int {
    val sortedCardOccurrences = hand.cards.groupingBy { it }.eachCount().entries.map { it.value.toString()[0] }.sortedDescending()
    
    if (sortedCardOccurrences[0] == '5') {
        return 7
    } else if (sortedCardOccurrences[0] == '4') {
        return 6
    } else if (sortedCardOccurrences[0] == '3') {
        return if (sortedCardOccurrences[1] == '2') {
            5
        } else {
            4
        }
    } else if (sortedCardOccurrences[0] == '2') {
        return if (sortedCardOccurrences[1] == '2') {
            3
        } else {
            2
        }
    } else {
        return 1
    }
}

fun replaceJokers(hand: CamelCardsHand): CamelCardsHand {
    val sortedCardOccurrences = hand.cards.groupingBy { it }.eachCount().entries.map { it.value.toString() + it.key.toString() }.sortedDescending()
    
    if (hand.cards.contains("J")) {
        for (i in sortedCardOccurrences.indices) {
            if (!sortedCardOccurrences[i].contains("J")) {
                return CamelCardsHand(
                    hand.cards.replace("J", sortedCardOccurrences[i][1].toString()),
                    hand.bid,
                    0
                )
            }
        }
    }
    
    return hand
}

fun parseCamelCards(lines: List<String>): List<CamelCardsHand> {
    val cards = mutableListOf<CamelCardsHand>()
    
    lines.forEach { 
        cards.add(CamelCardsHand(it.split(" ")[0], it.split(" ")[1].toLong(), 0))
    }
    
    return cards
}

data class CamelCardsHand(var cards: String, val bid: Long, var handStrength: Int)