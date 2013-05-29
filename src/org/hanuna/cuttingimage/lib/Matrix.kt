package util

/**
 * @author abreslav
 */

public trait Matrix<out T> {
    val width: Int
    val height: Int
    fun get(x: Int, y: Int): T
}

public fun <T> Matrix<T>.traverseLines(f: (x: Int, y: Int, value: T) -> Unit) {
    for (y in 0..height - 1) {
        for (x in 0..width - 1) {
            f(x, y, get(x, y))
        }
    }
}

public trait MutableMatrix<T> : Matrix<T> {
    fun set(x: Int, y: Int, value: T)
}

public class MutableMatrixImpl<T> (
        override val width: Int,
        override val height: Int,
        initialCellValues: (Int, Int) -> T
) : MutableMatrix<T> {

    private val cells: Array<T> = Array(width * height) {
        i ->
        val x = i % width
        val y = i / width
        initialCellValues(x, y)
    }

    override fun get(x: Int, y: Int): T {
        return cells[toIndex(x, y)]
    }

    override fun set(x: Int, y: Int, value: T) {
        cells[toIndex(x, y)] = value
    }

    private fun toIndex(x: Int, y: Int): Int {
        check(x, y)
        return y * width + x
    }

    private fun check(x: Int, y: Int) {
        if (x !in 0..width - 1) {
            throw IndexOutOfBoundsException("x = $x is out of range [0, $width)")
        }
        if (y !in 0..height - 1) {
            throw IndexOutOfBoundsException("y = $y is out of range [0, $height)")
        }
    }
}

public fun MutableMatrix<T>(w: Int, h: Int, f: (x: Int, y: Int) -> T): Matrix<T> = MutableMatrixImpl(w, h, f)

public fun <T> MutableMatrix<T>.fill(f: (x: Int, y: Int, value: T) -> T) {
    for (y in 0..height - 1) {
        for (x in 0..width - 1) {
            set(x, y, f(x, y, get(x, y)))
        }
    }
}

public fun <T> MutableMatrix<T>.copyFrom(m: Matrix<T>) {
    for (y in 0..height - 1) {
        for (x in 0..width - 1) {
            set(x, y, m[x, y])
        }
    }
}

public fun <T> Matrix<T>.toMutableMatrix(): MutableMatrix<T> = MutableMatrixImpl(width, height) {x, y -> get(x, y)}

