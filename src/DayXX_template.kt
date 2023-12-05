private const val DAY = "DayXX"

fun main() {
    fun part1(input: List<String>): Int {
        return input.size
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput(DAY + "_test")
    check(part1(testInput) == 1)

    val input = readInput(DAY)
    part1(input).println()

    val testInput_part2 = readInput(DAY + "_part2_test")
    check(part2(testInput_part2) == 281)
    part2(input).println()
}
