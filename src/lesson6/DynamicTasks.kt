@file:Suppress("UNUSED_PARAMETER")

package lesson6

import java.io.File
import java.lang.Integer.min
import java.util.*
import kotlin.collections.ArrayList

/**
 * Наибольшая общая подпоследовательность.
 * Средняя
 *
 * Дано две строки, например "nematode knowledge" и "empty bottle".
 * Найти их самую длинную общую подпоследовательность -- в примере это "emt ole".
 * Подпоследовательность отличается от подстроки тем, что её символы не обязаны идти подряд
 * (но по-прежнему должны быть расположены в исходной строке в том же порядке).
 * Если общей подпоследовательности нет, вернуть пустую строку.
 * Если есть несколько самых длинных общих подпоследовательностей, вернуть любую из них.
 * При сравнении подстрок, регистр символов *имеет* значение.
 * Трудоемкость: O(first.length*second.length)
 * Ресурсоемкость: O(first.length*second.length)
 */
fun longestCommonSubSequence(first: String, second: String): String {
    if (first.isEmpty() || second.isEmpty()) return ""
    var str = String()
    val array = Array(first.length + 1) { IntArray(second.length + 1) }

    for (i in 0..first.length) {
        for (j in 0..second.length) {
            if (i == 0 || j == 0) array[i][j] = 0
            else if (first[i - 1] == second[j - 1]) array[i][j] = array[i - 1][j - 1] + 1
            else array[i][j] = maxOf(array[i - 1][j], array[i][j - 1])
        }
    }

    var index = array[first.length][second.length]
    val lcs = ArrayDeque<Char>()
    var i = first.length
    var j = second.length

    if (index > 0) {
        while (i > 0 && j > 0) {
            when {
                first[i - 1] == second[j - 1] -> {
                    lcs.push(first[i - 1])
                    i--
                    j--
                    index--
                }
                array[i - 1][j] > array[i][j - 1] -> i--
                else -> j--
            }
        }
    }

    for (i in 0 until lcs.size) {
        str += lcs.pollFirst()
    }

    return str
}

/**
 * Наибольшая возрастающая подпоследовательность
 * Сложная
 *
 * Дан список целых чисел, например, [2 8 5 9 12 6].
 * Найти в нём самую длинную возрастающую подпоследовательность.
 * Элементы подпоследовательности не обязаны идти подряд,
 * но должны быть расположены в исходном списке в том же порядке.
 * Если самых длинных возрастающих подпоследовательностей несколько (как в примере),
 * то вернуть ту, в которой числа расположены раньше (приоритет имеют первые числа).
 * В примере ответами являются 2, 8, 9, 12 или 2, 5, 9, 12 -- выбираем первую из них.
 */
fun longestIncreasingSubSequence(list: List<Int>): List<Int> {
    TODO()
}

/**
 * Самый короткий маршрут на прямоугольном поле.
 * Средняя
 *
 * В файле с именем inputName задано прямоугольное поле:
 *
 * 0 2 3 2 4 1
 * 1 5 3 4 6 2
 * 2 6 2 5 1 3
 * 1 4 3 2 6 2
 * 4 2 3 1 5 0
 *
 * Можно совершать шаги длиной в одну клетку вправо, вниз или по диагонали вправо-вниз.
 * В каждой клетке записано некоторое натуральное число или нуль.
 * Необходимо попасть из верхней левой клетки в правую нижнюю.
 * Вес маршрута вычисляется как сумма чисел со всех посещенных клеток.
 * Необходимо найти маршрут с минимальным весом и вернуть этот минимальный вес.
 *
 * Здесь ответ 2 + 3 + 4 + 1 + 2 = 12
 * Трудоемкость O(width*height)
 * Ресурсоемкость O(width*height)
 */
fun shortestPathOnField(inputName: String): Int {
    val field = ArrayList<IntArray>()
    File(inputName).readLines().forEach {
        field.add(it.split(' ').map { it.toInt() }.toIntArray())
    }
    if (field.isEmpty()) return 0
    val width = field.first().size
    val height = field.size
    for (i in 0 until height) {
        for (j in 0 until width) {
            when {
                i == 0 && j > 0 -> field[i][j] += field[i][j - 1]
                i > 0 && j == 0 -> field[i][j] += field[i - 1][j]
                i == 0 && j == 0 -> field[i][j] += field[i][j]
                else -> field[i][j] += minOf(field[i - 1][j], field[i][j - 1], field[i - 1][j - 1])
            }
        }
    }
    return field[height - 1][width - 1]
}

// Задачу "Максимальное независимое множество вершин в графе без циклов"
// смотрите в уроке 5