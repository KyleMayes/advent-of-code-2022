package com.kylemayes.aoc2022.common.geometry

/** A 2D rectangle. */
data class Rectangle(val topLeft: Point, val bottomRight: Point) {
    val width = (bottomRight.x - topLeft.x) + 1
    val height = (bottomRight.y - topLeft.y) + 1
    val area = width * height

    init {
        assert(topLeft.x <= bottomRight.x)
        assert(topLeft.y <= bottomRight.y)
    }

    /** Returns whether the supplied point is inside this rectangle. */
    fun contains(point: Point): Boolean =
        point.x >= topLeft.x && point.x <= bottomRight.x &&
            point.y >= topLeft.y && point.y <= bottomRight.y

    /** Returns a sequence of the points inside this rectangle. */
    fun points() = sequence {
        for (y in topLeft.y..bottomRight.y) {
            for (x in topLeft.x..bottomRight.x) {
                yield(Point(x, y))
            }
        }
    }

    /** Returns the neighbors of a point in this tile. */
    fun neighbors(point: Point, diagonal: Boolean = true): List<Point> {
        assert(contains(point)) { "$point outside of bounds ($this)." }
        return point.neighbors(diagonal).filter { contains(it) }
    }

    override fun toString() = "Rectangle($topLeft → $bottomRight)"
}
