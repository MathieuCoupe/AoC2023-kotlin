private const val DAY = "Day07"

fun main() {
    val cardValues: Map<Char, Int> = mapOf(
        'A' to 14,
        'K' to 13,
        'Q' to 12,
        'T' to 11,
        '9' to 10,
        '8' to 9,
        '7' to 8,
        '6' to 7,
        '5' to 6,
        '4' to 5,
        '3' to 4,
        '2' to 3,
        'J' to 2
    )

    fun parse(input: List<String>): List<Handv2> {
        return input.map {
            // 32T3K 765
            Handv2(cards = it.substring(0, 5), bid = it.substring(6).trim().toInt(), originalCards = it.substring(0, 5))
        }.toList()
    }

    fun compareCards(hand1: Handv2, hand2: Handv2): Int {
        if (hand1.type() == hand2.type()) {
            for (index in IntRange(0, 4)) {
                if (hand1.originalCards[index] != hand2.originalCards[index]) {
                    return cardValues.getOrDefault(hand1.originalCards[index], 0) - cardValues.getOrDefault(hand2.originalCards[index], 0)
                }
            }
        }

        return hand1.type().ordinal - hand2.type().ordinal
    }

    fun enhanceHand(hand: Handv2): Handv2 {
        if (hand.cards.contains("J")) {
            // replace each J by another card
            val bestHand = cardValues.keys.asSequence().filter { it != 'J' }.map {
                val temporaryCards = hand.cards.replaceFirst('J', it)

                if (temporaryCards.contains('J')) {
                    return@map enhanceHand(Handv2(cards = temporaryCards, bid = hand.bid, originalCards = hand.originalCards))
                }

                return@map Handv2(cards = hand.cards.replace('J', it), bid = hand.bid, originalCards = hand.originalCards)
            }.sortedWith { o1, o2 -> compareCards(o1, o2) }.toList().last()

            if (bestHand.cards.contains("J")) {
                return enhanceHand(bestHand)
            }

            println("Hand $hand (${hand.type()}) improved into $bestHand (${bestHand.type()})")
            return bestHand
        }

        // unmodified hand
        return hand
    }

    fun part2(input: List<String>): Long {
        val hands = parse(input)
        var result = 0L

        hands
            // replace joker with best card
            .map { hand ->
                if (hand.cards.contains('J')) {
                    println("Hand initial value $hand")
                    val bestHand = enhanceHand(hand)
                    println("Final initial value $bestHand")
                    bestHand
                } else
                    hand
            }
            .sortedWith { o1, o2 -> compareCards(o1, o2) }.forEachIndexed { index, hand ->
                println("Hand $hand (${hand.type()}) at ${index + 1}")
                result += hand.bid * (index + 1)
            }

        println("Result = $result")
        return result
    }

    // basic check on hand type
    check(Handv2("AAAAA", 0, "AAAAA").type() == HandType.FiveOfAKind)
    check(Handv2("AA8AA", 0, "AA8AA").type() == HandType.FourOfAKind)
    check(Handv2("23332", 0, "23332").type() == HandType.FullHouse)
    check(Handv2("TTT98", 0, "TTT98").type() == HandType.ThreeOfAKind)
    check(Handv2("23432", 0, "23432").type() == HandType.TwoPair)
    check(Handv2("A23A4", 0, "A23A4").type() == HandType.OnePair)
    check(Handv2("23456", 0, "23456").type() == HandType.HighCard)

    // test if implementation meets criteria from the description, like:
    val testInput = readInput(DAY + "_test")
    val input = readInput(DAY)

    check(part2(testInput) == 5905L)
    part2(input).println()
}

data class Handv2(val cards: String, val bid: Int, val originalCards: String) {
    fun type(): HandType {
        val groups = cards.toCharArray().groupBy { it }.map { it.value.first() to it.value.size }.sortedBy { -it.second }

        return when (groups.size) {
            // Five identical cards
            1 -> HandType.FiveOfAKind

            // Four identical cards or FullHouse
            2 -> if (groups[0].second == 4) return HandType.FourOfAKind else HandType.FullHouse

            // Three cards or two pairs
            3 -> if (groups[0].second == 3) return HandType.ThreeOfAKind else HandType.TwoPair

            4 -> return HandType.OnePair

            else -> HandType.HighCard
        }
    }
}

