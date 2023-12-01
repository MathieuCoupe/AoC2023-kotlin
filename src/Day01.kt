fun main() {
    fun part1(input: List<String>): Int {
        return input.stream()
            .map { s -> s.toCharArray().filter { it.isDigit() } }
            .map { (it.first().digitToInt() * 10 + it.last().digitToInt()) }
            .toList().sum()
    }

    fun replaceInString(input: String): String {
        var replacement = input.first().toString()

        if(input.startsWith("one")) replacement = "1"
        if(input.startsWith("two")) replacement = "2"
        if(input.startsWith("three")) replacement = "3"
        if(input.startsWith("four")) replacement = "4"
        if(input.startsWith("five")) replacement = "5"
        if(input.startsWith("six")) replacement = "6"
        if(input.startsWith("seven")) replacement = "7"
        if(input.startsWith("eight")) replacement = "8"
        if(input.startsWith("nine")) replacement = "9"

        if (input.length > 1)
            return replacement + replaceInString(input.substring(1))

        return replacement
    }

    fun part2(input: List<String>): Int {
        val updated = input.stream()
            .map { replaceInString(it) }
            .toList()

        return part1(updated)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    check(part1(testInput) == 142)

    val input = readInput("Day01")
    part1(input).println()

    val testInput_part2 = readInput("Day01_part2_test")
    check(part2(testInput_part2) == 281)
    part2(input).println()
}
