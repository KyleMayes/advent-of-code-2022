// SPDX-License-Identifier: Apache-2.0

package com.kylemayes.aoc2022.day10

import com.kylemayes.aoc2022.common.Input
import com.kylemayes.aoc2022.common.ResourceInput
import com.kylemayes.aoc2022.common.Solution
import com.kylemayes.aoc2022.common.StringInput
import com.kylemayes.aoc2022.common.geometry.Point
import com.kylemayes.aoc2022.common.geometry.Tile
import com.kylemayes.aoc2022.common.readLines
import com.kylemayes.aoc2022.common.solve
import kotlin.math.absoluteValue

val signalCycles = listOf(20, 60, 100, 140, 180, 220)

sealed class Instruction(val cycles: Int) {
    object Noop : Instruction(1)
    data class Addx(val amount: Int) : Instruction(2)
}

typealias ParsedInput = List<Instruction>

class Day10 : Solution<ParsedInput> {
    override fun parse(input: Input) = input
        .readLines()
        .map {
            if (it == "noop") {
                Instruction.Noop
            } else {
                Instruction.Addx(it.split(" ")[1].toInt())
            }
        }

    override fun solvePart1(input: ParsedInput): Int {
        var x = 1
        var cycle = 1
        var strength = 0

        for (instruction in input) {
            val before = x

            cycle += instruction.cycles
            if (instruction is Instruction.Addx) {
                x += instruction.amount
            }

            val signalCycle = signalCycles.find { it > cycle - instruction.cycles && it <= cycle }
            if (signalCycle != null) {
                val value = if (signalCycle == cycle) { x } else { before }
                strength += signalCycle * value
            }
        }

        return strength
    }

    override fun solvePart2(input: ParsedInput): String {
        val screen = Tile(40, 6, List(40 * 6) { false })

        var x = 1
        var cycle = 0

        for (instruction in input) {
            for (now in cycle until cycle + instruction.cycles) {
                if ((x - (now % screen.bounds.width)).absoluteValue < 2) {
                    screen[Point(now % screen.bounds.width, now / screen.bounds.width)] = true
                }
            }

            cycle += instruction.cycles
            if (instruction is Instruction.Addx) {
                x += instruction.amount
            }
        }

        return screen.render { if (it) { '#' } else { '.' } }
    }
}

fun main() = solve({ Day10() }, ResourceInput("day10.txt"))
