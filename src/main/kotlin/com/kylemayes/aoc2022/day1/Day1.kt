// SPDX-License-Identifier: Apache-2.0

package com.kylemayes.aoc2022.day1

import com.kylemayes.aoc2022.common.Input
import com.kylemayes.aoc2022.common.ResourceInput
import com.kylemayes.aoc2022.common.Solution
import com.kylemayes.aoc2022.common.readGroups
import com.kylemayes.aoc2022.common.solve

typealias ParsedInput = List<List<Int>>

class Day1 : Solution<ParsedInput> {
    override fun parse(input: Input) = input
        .readGroups()
        .map { g -> g.map { it.toInt() } }

    override fun solvePart1(input: ParsedInput) = input
        .maxOf { it.sum() }

    override fun solvePart2(input: ParsedInput) = input
        .map { it.sum() }
        .sortedDescending()
        .take(3)
        .sum()
}

fun main() = solve({ Day1() }, ResourceInput("day1.txt"))
