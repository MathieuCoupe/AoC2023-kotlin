import kotlin.jvm.optionals.toList

private const val DAY = "Day03"

fun Boolean.toInt() = if (this) 1 else 0

fun main() {

    fun printSchematic(schematic: Array<Array<ValidatedChar>>) {
        for (y in schematic.indices) {
            for (x in schematic[y].indices) {
                if (schematic[y][x].valided)
                    print(schematic[y][x].char)
                else
                    print(' ')
            }
            println()
        }
    }

    fun parser(input: List<String>): Array<Array<ValidatedChar>> {
        return input.stream()
            .map { it.map { c -> ValidatedChar(c, false) }.toTypedArray() }
            .toList().toTypedArray()
    }

    fun part1(input: List<String>): Int {
        val schematic = parser(input)

        // validate all digits close to a symbol
        for (y in schematic.indices) {
            for (x in schematic[y].indices) {
                val c = schematic[y][x].char
                if (!c.isDigit() && c != ('.')) {
                    // mark as validated
                    if (y + 1 in schematic.indices) {
                        if (x - 1 in schematic[y + 1].indices) schematic[y + 1][x - 1].valided = true
                        schematic[y + 1][x].valided = true
                        if (x + 1 in schematic[y + 1].indices) schematic[y + 1][x + 1].valided = true
                    }

                    if (x - 1 in schematic[y].indices) schematic[y][x - 1].valided = true
                    schematic[y][x].valided = true
                    if (x + 1 in schematic[y].indices) schematic[y][x + 1].valided = true

                    if (y - 1 in schematic.indices) {
                        if (x - 1 in schematic[y - 1].indices) schematic[y - 1][x - 1].valided = true
                        schematic[y - 1][x].valided = true
                        if (x + 1 in schematic[y - 1].indices) schematic[y - 1][x + 1].valided = true
                    }
                }
            }
        }

        var validatedNumbers = listOf<Int>()

        // extract all numbers with a validated digit
        for (y in schematic.indices) {
            var currentNumber = ""
            var currentNumberIsValidated = false

            for (x in schematic[y].indices) {
                val current = schematic[y][x]

                // inside a number
                if (current.char.isDigit()) {
                    currentNumber += current.char
                    currentNumberIsValidated = currentNumberIsValidated || current.valided
                } else {
                    // number ended
                    if (currentNumberIsValidated) {
                        validatedNumbers += currentNumber.toInt()
                    }

                    currentNumber = ""
                    currentNumberIsValidated = false
                }
            }

            // end of the line
            if (currentNumberIsValidated) {
                validatedNumbers += currentNumber.toInt()
            }
        }

        return validatedNumbers.sum()
    }

    fun extractNumber(schematic: Array<Array<ValidatedChar>>, x: Int, y: Int): String {
        var leftPart = ""
        val midPart = if (schematic[y][x].char.isDigit()) schematic[y][x].char else '.'
        var rightPart = ""

        var n = x - 1
        while (n >= 0 && schematic[y][n].char.isDigit()) {
            leftPart = schematic[y][n].char + leftPart
            n -= 1
        }

        n = x + 1
        while (n < schematic[y].size && schematic[y][n].char.isDigit()) {
            rightPart = rightPart + schematic[y][n].char
            n += 1
        }

        return leftPart + midPart + rightPart
    }

    fun part2(input: List<String>): Int {
        var gearRatios = listOf<Int>()
        val schematic = parser(input)

        // validate all digits close to a gear symbol
        for (y in schematic.indices) {
            for (x in schematic[y].indices) {
                val c = schematic[y][x].char
                if (c == '*') {
                    // check how many number are near
                    var nearNumbersCount = 0

                    if (y + 1 in schematic.indices) {
                        if (x - 1 in schematic[y + 1].indices) nearNumbersCount += schematic[y + 1][x - 1].char.isDigit().toInt()
                        nearNumbersCount += schematic[y + 1][x].char.isDigit().toInt()
                        if (x + 1 in schematic[y + 1].indices) nearNumbersCount += schematic[y + 1][x + 1].char.isDigit().toInt()
                    }

                    if (x - 1 in schematic[y].indices) nearNumbersCount += schematic[y][x - 1].char.isDigit().toInt()
                    nearNumbersCount += schematic[y][x].char.isDigit().toInt()
                    if (x + 1 in schematic[y].indices) nearNumbersCount += schematic[y][x + 1].char.isDigit().toInt()

                    if (y - 1 in schematic.indices) {
                        if (x - 1 in schematic[y - 1].indices) nearNumbersCount += schematic[y - 1][x - 1].char.isDigit().toInt()
                        nearNumbersCount += schematic[y - 1][x].char.isDigit().toInt()
                        if (x + 1 in schematic[y - 1].indices) nearNumbersCount += schematic[y - 1][x + 1].char.isDigit().toInt()
                    }

                    if (nearNumbersCount > 1) {
                        // find near numbers
                        val numbers = listOf(extractNumber(schematic, x, y - 1), extractNumber(schematic, x, y), extractNumber(schematic, x, y + 1))
                        val splitNumbers = numbers.stream().map { it.split('.') }.flatMap { it.stream().filter { it.isNotBlank() } }.map { it.toInt() }.toList()

                        if (splitNumbers.size == 2) {
                            val item = splitNumbers.stream().reduce { t, u -> u * t }.toList().get(0)
                            gearRatios += item
                        }
                    }
                }
            }
        }

        return gearRatios.sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput(DAY + "_test")
    check(part1(testInput) == 4361)

    val input = readInput(DAY)
    part1(input).println()

    val testInput_part2 = readInput(DAY + "_test")
    check(part2(testInput_part2) == 467835)

    part2(input).println()
}

data class ValidatedChar(val char: Char, var valided: Boolean)
