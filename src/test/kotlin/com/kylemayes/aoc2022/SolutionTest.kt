package com.kylemayes.aoc2022

import com.kylemayes.aoc2022.common.ResourceInput
import com.kylemayes.aoc2022.common.Solution
import com.kylemayes.aoc2022.day1.Day1
import com.kylemayes.aoc2022.day10.Day10
import com.kylemayes.aoc2022.day2.Day2
import com.kylemayes.aoc2022.day3.Day3
import com.kylemayes.aoc2022.day4.Day4
import com.kylemayes.aoc2022.day5.Day5
import com.kylemayes.aoc2022.day6.Day6
import com.kylemayes.aoc2022.day7.Day7
import com.kylemayes.aoc2022.day8.Day8
import com.kylemayes.aoc2022.day9.Day9
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SolutionTest {
    private fun solutionsParameters(): Stream<Arguments> = Stream.of(
        Arguments.of("day1.txt", Day1(), 67016, 200116),
        Arguments.of("day2.txt", Day2(), 10310, 14859),
        Arguments.of("day3.txt", Day3(), 8109, 2738),
        Arguments.of("day4.txt", Day4(), 582, 893),
        Arguments.of("day5.txt", Day5(), "GRTSWNJHH", "QLFQDBBHM"),
        Arguments.of("day6.txt", Day6(), 1282, 3513),
        Arguments.of("day7.txt", Day7(), 1449447L, 8679207L),
        Arguments.of("day8.txt", Day8(), 1676, 313200),
        Arguments.of("day9.txt", Day9(), 6023, 2533),
        Arguments.of("day10.txt", Day10(), 13860,
            "###..####.#..#.####..##....##..##..###..\n" +
            "#..#....#.#..#.#....#..#....#.#..#.#..#.\n" +
            "#..#...#..####.###..#.......#.#....###..\n" +
            "###...#...#..#.#....#.##....#.#....#..#.\n" +
            "#.#..#....#..#.#....#..#.#..#.#..#.#..#.\n" +
            "#..#.####.#..#.#.....###..##...##..###.."),
    )

    @ParameterizedTest
    @MethodSource("solutionsParameters")
    fun <I> solutions(resource: String, solution: Solution<I>, part1: Any, part2: Any) {
        val input = ResourceInput(resource)
        assertEquals(part1, solution.solvePart1(solution.parse(input)))
        assertEquals(part2, solution.solvePart2(solution.parse(input)))
    }
}
