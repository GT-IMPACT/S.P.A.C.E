package space.impact.space.utils

typealias Array2<T> = Array<Array<T>>
typealias Array3<T> = Array<Array<Array<T>>>

inline fun <reified T> array2OfNulls(sizeFirst: Int, sizeSecond: Int): Array2<T?> {
    return Array(sizeFirst) { arrayOfNulls(sizeSecond) }
}