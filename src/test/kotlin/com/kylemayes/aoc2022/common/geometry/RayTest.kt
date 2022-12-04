package com.kylemayes.aoc2022.common.geometry

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class RayTest {
    @Test
    fun points() {
        val get = { a: Point, b: Point -> Ray(a, b).points().take(3).toList() }

        assertEquals(
            listOf(Point(0, 0), Point(1, 0), Point(2, 0)),
            get(Point(0, 0), Point(1, 0)),
        )

        assertEquals(
            listOf(Point(0, 0), Point(-1, 0), Point(-2, 0)),
            get(Point(0, 0), Point(-1, 0)),
        )

        assertEquals(
            listOf(Point(0, 0), Point(0, 1), Point(0, 2)),
            get(Point(0, 0), Point(0, 1)),
        )

        assertEquals(
            listOf(Point(0, 0), Point(0, -1), Point(0, -2)),
            get(Point(0, 0), Point(0, -1)),
        )

        assertEquals(
            listOf(Point(0, 0), Point(3, 1), Point(6, 2)),
            get(Point(0, 0), Point(6, 2)),
        )

        assertEquals(
            listOf(Point(0, 0), Point(-3, 1), Point(-6, 2)),
            get(Point(0, 0), Point(-6, 2)),
        )

        assertEquals(
            listOf(Point(0, 0), Point(3, -1), Point(6, -2)),
            get(Point(0, 0), Point(6, -2)),
        )

        assertEquals(
            listOf(Point(0, 0), Point(-3, -1), Point(-6, -2)),
            get(Point(0, 0), Point(-6, -2)),
        )
    }

    @Test
    fun stringify() {
        val ray = Ray(Point(0, 0), Point(2, 2))
        assertEquals("Ray((0, 0) → (1, 1) [45.0°])", ray.toString())
    }
}
