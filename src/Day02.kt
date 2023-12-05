import kotlin.math.max

fun main() {
    fun parseLine(line: String): List<DiceSet> {
        return emptyList()
    }

    fun parse(input: List<String>): List<Game> {
        return input.stream()
            .map { gameString ->
                val parts = gameString.split(':')
                val gameId = parts[0].substring("Game ".length).toInt()
                val sets = parts[1].split(';').stream()
                    .map {
                        val diceSet = DiceSet()
                        it.split(',').forEach {
                            val setParts = it.trim().split(' ')
                            when (setParts[1]) {
                                "red" -> diceSet.red = setParts[0].toInt()
                                "green" -> diceSet.green = setParts[0].toInt()
                                "blue" -> diceSet.blue = setParts[0].toInt()
                            }
                        }

                        diceSet
                    }.toList()

                Game(gameId, sets)
            }
            .toList()
    }

    fun part1(input: List<String>): Int {
        val maxDices = DiceSet(red = 12, green = 13, blue = 14)
        val games = parse(input)

        return games.stream()
            .filter { game ->
                // count how many games are impossible
                val impossibleGames = game.sets.stream().filter { set -> !set.possible(limits = maxDices) }.count()
                impossibleGames.toInt() == 0
            }
            .map { it.id }
            .toList().sum()
    }

    fun part2(input: List<String>): Int {
        val games = parse(input)

        return games.stream().map {
            val minimumDices = DiceSet()

            it.sets.stream().forEach { set ->
                minimumDices.red = max(set.red, minimumDices.red)
                minimumDices.green = max(set.green, minimumDices.green)
                minimumDices.blue = max(set.blue, minimumDices.blue)
            }

            // The power of a set of cubes is equal to the numbers of red, green, and blue cubes multiplied together
            minimumDices.red * minimumDices.blue * minimumDices.green
        }.toList().sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test")
    check(part1(testInput) == 8)

    val input = readInput("Day02")
    part1(input).println()

    check(part2(testInput) == 2286)
    part2(input).println()


}

data class DiceSet(var red: Int = 0, var green: Int = 0, var blue: Int = 0) {
    fun possible(limits: DiceSet): Boolean {
        return limits.red >= red
                && limits.blue >= blue
                && limits.green >= green
    }
}

data class Game(val id: Int, val sets: List<DiceSet>)
