// SPDX-License-Identifier: Apache-2.0

package com.kylemayes.aoc2022.day3

import com.kylemayes.aoc2022.common.Input
import com.kylemayes.aoc2022.common.ResourceInput
import com.kylemayes.aoc2022.common.Solution
import com.kylemayes.aoc2022.common.readLines
import com.kylemayes.aoc2022.common.solve

typealias ParsedInput = List<List<Char>>

class Day3 : Solution<ParsedInput> {
    override fun parse(input: Input) = input
        .readLines()
        .map { it.toCharArray().toList() }

    override fun solvePart1(input: ParsedInput) = input.sumOf {
        val middle = it.size / 2
        val first = it.subList(0, middle).toSet()
        val second = it.subList(middle, it.size).toSet()
        getPriority(first.intersect(second).first())
    }

    override fun solvePart2(input: ParsedInput) = input
        .chunked(3)
        .sumOf { group ->
            getPriority(
                group
                    .map { it.toSet() }
                    .reduce { acc, r -> acc.intersect(r) }
                    .first()
            )
        }

    private fun getPriority(item: Char): Int = if (item.isLowerCase()) {
        1 + (item.code - 'a'.code)
    } else {
        27 + (item.code - 'A'.code)
    }
}

fun main() = solve({ Day3() }, ResourceInput("day3.txt"))
