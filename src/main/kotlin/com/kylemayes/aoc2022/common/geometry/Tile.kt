package com.kylemayes.aoc2022.common.geometry

import org.apache.commons.lang3.builder.EqualsBuilder
import org.apache.commons.lang3.builder.HashCodeBuilder

/** A dense, finite 2D grid. */
class Tile<T> : Cloneable {
    val bounds: Rectangle

    private val cells: MutableList<T>

    constructor(width: Int, height: Int, cells: List<T>) {
        assert(width > 0 && height > 0)
        assert(cells.size == width * height)
        bounds = Rectangle(Point(0, 0), Point(width - 1, height - 1))
        this.cells = cells.toMutableList()
    }

    constructor(tile: List<List<T>>) {
        assert(tile.isNotEmpty() && tile[0].isNotEmpty())

        val width = tile[0].size
        val height = tile.size
        assert(tile.all { it.size == width })

        bounds = Rectangle(Point(0, 0), Point(width - 1, height - 1))
        this.cells = MutableList(width * height) { tile[it / width][it % width] }
    }

    constructor(tile: List<List<T>>, default: (point: Point) -> T) {
        assert(tile.isNotEmpty() && tile[0].isNotEmpty())

        val width = tile.maxOf { it.size }
        val height = tile.size

        bounds = Rectangle(Point(0, 0), Point(width - 1, height - 1))
        this.cells = MutableList(width * height) {
            val point = Point(it % width, it / width)
            tile.getOrNull(point.y)?.getOrNull(point.x) ?: default(point)
        }
    }

    constructor(width: Int, height: Int, cell: (point: Point) -> T) : this(
        width,
        height,
        MutableList(width * height) { cell(Point(it % width, it / width)) }
    )

    /** Returns this tile as a field. */
    fun toField(): Field<T> = HashMap(entries().all().associate { it })

    // ===========================================
    // Read / Write
    // ===========================================

    fun getOrDefault(point: Point, default: T): T {
        return cells.getOrElse((point.y * bounds.width) + point.x) { default }
    }

    operator fun get(point: Point): T {
        assert(bounds.contains(point)) { "$point outside of tile bounds ($bounds)." }
        return cells[(point.y * bounds.width) + point.x]
    }

    operator fun set(point: Point, value: T) {
        assert(bounds.contains(point)) { "$point outside of tile bounds ($bounds)." }
        cells[(point.y * bounds.width) + point.x] = value
    }

    /** Returns the neighbors of an entry in this tile. */
    fun neighbors(point: Point, diagonal: Boolean = true): List<Pair<Point, T>> =
        bounds.neighbors(point, diagonal).map { it to this[it] }

    /** Writes the entries from a tile onto this tile. */
    fun blit(offset: Point, other: Tile<T>) {
        for ((point, value) in other.entries().all()) {
            this[point + offset] = value
        }
    }

    /** Returns this tile as a string where each row is a line and each value is a character. */
    fun render(render: (value: T) -> Char): String =
        values().rows().joinToString("\n") { it.joinToString("") { v -> render(v).toString() } }

    // ===========================================
    // Vectors
    // ===========================================

    fun values() = TileValues(bounds, cells)
    fun entries() = TileEntries(values())

    // ===========================================
    // Transforms
    // ===========================================

    /** Maps this tile into a new tile. */
    fun <U> map(transform: (T) -> U): Tile<U> =
        Tile(bounds.width, bounds.height, cells.map(transform))

    /** Merges this tile and the supplied tile into a new tile horizontally. */
    fun mergeX(right: Tile<T>): Tile<T> {
        assert(bounds.height == right.bounds.height) { "Tile heights are not equal." }
        val cells = mutableListOf<T>()
        values().rows().zip(right.values().rows()).forEach { cells.addAll(it.first); cells.addAll(it.second) }
        val width = bounds.width + right.bounds.width
        return Tile(width, bounds.height, cells)
    }

    /** Merges this tile and the supplied tile into a new tile vertically. */
    fun mergeY(bottom: Tile<T>): Tile<T> {
        assert(bounds.width == bottom.bounds.width) { "Tile widths are not equal." }
        val height = bounds.height + bottom.bounds.height
        return Tile(bounds.width, height, cells + bottom.cells)
    }

    /** Flips this tile into a new tile horizontally. */
    fun flipX(): Tile<T> = Tile(
        bounds.width,
        bounds.height,
        values().rows().map { it.reversed() }.flatten(),
    )

    /** Flips this tile into a new tile vertically. */
    fun flipY(): Tile<T> = Tile(
        bounds.width,
        bounds.height,
        values().rows().reversed().flatten(),
    )

    /** Rotates this tile into a new tile clockwise. */
    @Suppress("NAME_SHADOWING")
    fun rotateCW(degrees: Int): Tile<T> {
        val degrees = ((degrees % 360) + 360) % 360
        assert(degrees % 90 == 0) { "Rotation angle is not a multiple of 90° ($degrees°)" }

        if (degrees == 0) {
            return clone()
        }

        val (width, height) = if (degrees == 180) {
            Pair(bounds.width, bounds.height)
        } else {
            Pair(bounds.height, bounds.width)
        }

        val transform = when (degrees) {
            90 -> { p: Point -> Point(p.y, width - p.x - 1) }
            180 -> { p: Point -> Point(width - p.x - 1, height - p.y - 1) }
            270 -> { p: Point -> Point(p.y, p.x) }
            else -> throw IllegalStateException("Unreachable.")
        }

        return Tile(width, height) { this[transform(it)] }
    }

    /** Returns a part of this tile as a new tile. */
    fun crop(crop: Rectangle): Tile<T> {
        assert(bounds.contains(crop.topLeft) && bounds.contains(crop.bottomRight))
        assert(crop.width > 0 && crop.height > 0)
        return Tile(crop.width, crop.height) { this[it + crop.topLeft] }
    }

    // ===========================================
    // Other
    // ===========================================

    public override fun clone() = Tile(bounds.width, bounds.height, cells)

    override fun equals(other: Any?) = other is Tile<*> && EqualsBuilder()
        .append(bounds, other.bounds)
        .append(cells, other.cells)
        .isEquals

    override fun hashCode() = HashCodeBuilder()
        .append(bounds)
        .append(cells)
        .toHashCode()

    override fun toString(): String {
        val rows = values().rows().joinToString { "[${it.joinToString()}]" }
        return "Tile($rows)"
    }
}

data class TileValues<T>(internal val bounds: Rectangle, internal val cells: List<T>) {
    /** Returns the values in the tile. */
    fun all(): List<T> = cells

    /** Returns a row in the tile. */
    fun row(y: Int): List<T> {
        assert(y >= 0 && y < bounds.height) { "Row $y outside of tile bounds ($bounds)." }
        val start = y * bounds.width
        return cells.slice(start until start + bounds.width)
    }

    /** Returns a column in the tile. */
    fun column(x: Int): List<T> {
        assert(x >= 0 && x < bounds.width) { "Column $x outside of tile bounds ($bounds)." }
        return (0 until bounds.height).map { cells[(it * bounds.width) + x] }
    }

    /** Returns the rows in the tile. */
    fun rows(): List<List<T>> = cells.chunked(bounds.width)

    /** Returns the columns in the tile. */
    fun columns(): List<List<T>> = List(bounds.width) { column(it) }

    fun rays(point: Point, diagonal: Boolean = false): List<Pair<Direction, List<T>>> = Direction
        .values()
        .filter { diagonal || !it.diagonal }
        .map { d ->
            d to generateSequence(point) { it + d.delta }
                .takeWhile { bounds.contains(it) }
                .map { cells[(it.y * bounds.width) + it.x] }
                .toList()
        }
}

data class TileEntries<T>(private val values: TileValues<T>) {
    /** Returns the entries in the tile. */
    fun all(): List<Pair<Point, T>> = values
        .cells
        .withIndex()
        .map { (i, v) -> Point(i % values.bounds.width, i / values.bounds.width) to v }

    /** Returns a row in the tile. */
    fun row(y: Int): List<Pair<Point, T>> = values
        .row(y)
        .withIndex()
        .map { (x, v) -> Point(x, y) to v }

    /** Returns a column in the tile. */
    fun column(x: Int): List<Pair<Point, T>> = values
        .column(x)
        .withIndex()
        .map { (y, v) -> Point(x, y) to v }

    /** Returns the rows in the tile. */
    fun rows(): List<List<Pair<Point, T>>> = List(values.bounds.height) { row(it) }

    /** Returns the columns in the tile. */
    fun columns(): List<List<Pair<Point, T>>> = List(values.bounds.width) { column(it) }

    fun rays(point: Point, diagonal: Boolean = false): List<Pair<Direction, List<Pair<Point, T>>>> =
        Direction
            .values()
            .filter { diagonal || !it.diagonal }
            .map { d ->
                d to generateSequence(point) { it + d.delta }
                    .takeWhile { values.bounds.contains(it) }
                    .map { it to values.cells[(it.y * values.bounds.width) + it.x] }
                    .toList()
            }
}

/** Returns these strings as a tile of characters where each string is a row. */
fun List<String>.toTile(): Tile<Char> {
    assert(isNotEmpty() && this[0].isNotEmpty())

    val width = this[0].length
    val height = size
    assert(all { it.length == width })

    return Tile(width, height) { this[it.y][it.x] }
}

/** Returns this string as a tile of characters where each line is a row. */
fun String.toTile(): Tile<Char> =
    split("\n").toTile()

/** Returns these strings as a tile where each string is a row of values. */
fun <T> List<String>.toTile(separator: Regex = Regex("\\s+"), transform: (String) -> T): Tile<T> {
    val values = this.map { it.split(separator).map(transform) }

    assert(isNotEmpty() && values[0].isNotEmpty())

    val width = values[0].size
    val height = size
    assert(values.all { it.size == width })

    return Tile(width, height) { values[it.y][it.x] }
}

/** Returns this string as a tile where each line is a row of values. */
fun <T> String.toTile(separator: Regex = Regex("\\s+"), transform: (String) -> T): Tile<T> =
    split("\n").toTile(separator, transform)
