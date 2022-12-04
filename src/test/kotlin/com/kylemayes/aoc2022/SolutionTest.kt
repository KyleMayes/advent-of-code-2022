package com.kylemayes.aoc2022

import com.kylemayes.aoc2022.common.ResourceInput
import com.kylemayes.aoc2022.common.Solution
import com.kylemayes.aoc2022.day1.Day1
import com.kylemayes.aoc2022.day2.Day2
import com.kylemayes.aoc2022.day3.Day3
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
    )

    @ParameterizedTest
    @MethodSource("solutionsParameters")
    fun <I> solutions(resource: String, solution: Solution<I>, part1: Any, part2: Any) {
        val input = ResourceInput(resource)
        assertEquals(part1, solution.solvePart1(solution.parse(input)))
        assertEquals(part2, solution.solvePart2(solution.parse(input)))
    }
}
