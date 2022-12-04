// SPDX-License-Identifier: Apache-2.0

package com.kylemayes.aoc2022.day2

import com.kylemayes.aoc2022.common.Input
import com.kylemayes.aoc2022.common.ResourceInput
import com.kylemayes.aoc2022.common.Solution
import com.kylemayes.aoc2022.common.readLines
import com.kylemayes.aoc2022.common.solve

val wins = mapOf('A' to 'Y', 'B' to 'Z', 'C' to 'X')
val draws = mapOf('A' to 'X', 'B' to 'Y', 'C' to 'Z')
val loses = mapOf('A' to 'Z', 'B' to 'X', 'C' to 'Y')
val scores = mapOf('X' to 1, 'Y' to 2, 'Z' to 3)

typealias ParsedInput = List<Pair<Char, Char>>

class Day2 : Solution<ParsedInput> {
    override fun parse(input: Input) = input
        .readLines()
        .map {
            val list = it.split(" ")
            list[0][0] to list[1][0]
        }

    override fun solvePart1(input: ParsedInput) = input.sumOf {
        scores[it.second]!! + when (it.second) {
            wins[it.first]!! -> 6
            draws[it.first]!! -> 3
            else -> 0
        }
    }

    override fun solvePart2(input: ParsedInput) = input.sumOf {
        when (it.second) {
            'X' -> scores[loses[it.first]!!]!!
            'Y' -> 3 + scores[draws[it.first]!!]!!
            'Z' -> 6 + scores[wins[it.first]!!]!!
            else -> 0
        }
    }
}

fun main() = solve({ Day2() }, ResourceInput("day2.txt"))
