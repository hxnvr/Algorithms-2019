@file:Suppress("UNUSED_PARAMETER", "unused")

package lesson5

import lesson5.Graph.*
import java.util.*
import lesson5.impl.GraphBuilder

/**
 * Эйлеров цикл.
 * Средняя
 *
 * Дан граф (получатель). Найти по нему любой Эйлеров цикл.
 * Если в графе нет Эйлеровых циклов, вернуть пустой список.
 * Соседние дуги в списке-результате должны быть инцидентны друг другу,
 * а первая дуга в списке инцидентна последней.
 * Длина списка, если он не пуст, должна быть равна количеству дуг в графе.
 * Веса дуг никак не учитываются.
 *
 * Пример:
 *
 *      G -- H
 *      |    |
 * A -- B -- C -- D
 * |    |    |    |
 * E    F -- I    |
 * |              |
 * J ------------ K
 *
 * Вариант ответа: A, E, J, K, D, C, H, G, B, C, I, F, B, A
 *
 * Справка: Эйлеров цикл -- это цикл, проходящий через все рёбра
 * связного графа ровно по одному разу
 * Трудоемкость: O(edges + vertices)?
 * Ресурсоемкость O(e+v)
 */
fun Graph.findEulerLoop(): List<Graph.Edge> {
    if (!checkForEulerCycle() || vertices.isEmpty()) return listOf()

    val stack = Stack<Vertex>()
    val resVertices = LinkedList<Vertex>()
    val resEdges = LinkedList<Edge>()
    val edgesList = edges

    stack.push(vertices.first())

    while (stack.isNotEmpty()) {
        val current = stack.peek()
        for (vertex in vertices) {
            val edge = getConnection(current, vertex) ?: continue

            if (edgesList.contains(edge)) {
                stack.push(vertex)
                edgesList.remove(edge)
                break
            }
        }

        if (current == stack.peek()) {
            stack.pop()
            resVertices.push(current)
        }
    }

    for (i in 0 until resVertices.size - 1) {
        resEdges.add(getConnection(resVertices[i], resVertices[i + 1])!!)
    }

    return resEdges
}

fun Graph.checkForEulerCycle(): Boolean = vertices.filterNot { getNeighbors(it).size % 2 == 0 }.isEmpty()
/**
 * Минимальное остовное дерево.
 * Средняя
 *
 * Дан граф (получатель). Найти по нему минимальное остовное дерево.
 * Если есть несколько минимальных остовных деревьев с одинаковым числом дуг,
 * вернуть любое из них. Веса дуг не учитывать.
 *
 * Пример:
 *
 *      G -- H
 *      |    |
 * A -- B -- C -- D
 * |    |    |    |
 * E    F -- I    |
 * |              |
 * J ------------ K
 *
 * Ответ:
 *
 *      G    H
 *      |    |
 * A -- B -- C -- D
 * |    |    |
 * E    F    I
 * |
 * J ------------ K
 * Трудоемкость O(V)
 * Ресурсоемкость O(V)
 */
fun Graph.minimumSpanningTree(): Graph {
    val builder = GraphBuilder()
    val verticesSet = mutableSetOf<Vertex>()
    val edgesSet = mutableSetOf<Edge>()

    if (vertices.isEmpty() || edges.isEmpty()) return builder.build()

    for (vertex in vertices) {
        for ((current, edge) in getConnections(vertex)) {
            if (current !in verticesSet) {
                verticesSet.add(current)
                edgesSet.add(edge)
            }
        }
    }

    return builder.apply {
        for (edge in edgesSet) {
            addVertex(edge.begin)
            addConnection(edge.begin, edge.end)
        }
    }.build()

}

/**
 * Максимальное независимое множество вершин в графе без циклов.
 * Сложная
 *
 * Дан граф без циклов (получатель), например
 *
 *      G -- H -- J
 *      |
 * A -- B -- D
 * |         |
 * C -- F    I
 * |
 * E
 *
 * Найти в нём самое большое независимое множество вершин и вернуть его.
 * Никакая пара вершин в независимом множестве не должна быть связана ребром.
 *
 * Если самых больших множеств несколько, приоритет имеет то из них,
 * в котором вершины расположены раньше во множестве this.vertices (начиная с первых).
 *
 * В данном случае ответ (A, E, F, D, G, J)
 *
 * Если на входе граф с циклами, бросить IllegalArgumentException
 *
 * Эта задача может быть зачтена за пятый и шестой урок одновременно
 */
fun Graph.largestIndependentVertexSet(): Set<Graph.Vertex> {
    TODO()
}

/**
 * Наидлиннейший простой путь.
 * Сложная
 *
 * Дан граф (получатель). Найти в нём простой путь, включающий максимальное количество рёбер.
 * Простым считается путь, вершины в котором не повторяются.
 * Если таких путей несколько, вернуть любой из них.
 *
 * Пример:
 *
 *      G -- H
 *      |    |
 * A -- B -- C -- D
 * |    |    |    |
 * E    F -- I    |
 * |              |
 * J ------------ K
 *
 * Ответ: A, E, J, K, D, C, H, G, B, F, I
 * Трудоемксоть O(v!)
 * Ресурсоемкость O(v!)
 */
fun Graph.longestSimplePath(): Path {
    if (vertices.isEmpty() || edges.isEmpty()) return Path()

    val deque = ArrayDeque<Path>()
    var longestPath = Path()

    for (vertex in vertices) {
        deque.add(Path(vertex))
    }

    while (deque.isNotEmpty()) {

        val currentPath = deque.pop()
        val lastVertex = currentPath.vertices.last()

        if (currentPath.length > longestPath.length) longestPath = currentPath

        for (neighbor in getNeighbors(lastVertex)) {
            if (!currentPath.contains(neighbor)) {
                deque.add(Path(currentPath, this, neighbor))
            }
        }
    }
    return longestPath
}