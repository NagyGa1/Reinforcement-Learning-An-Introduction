package lab.mars.rl.model.impl

import lab.mars.rl.util.nsetOf
import lab.mars.rl.util.invoke
import lab.mars.rl.util.x
import org.junit.Assert
import org.junit.Test

/**
 * <p>
 * Created on 2017-09-01.
 * </p>
 *
 * @author wumo
 */
class TestNSet {
    @Test
    fun `example`() {
        val set = nsetOf(2(3, 4)) { println(it);0 }
    }

    @Test
    fun `make nset using dim2`() {
        val r1 = mutableListOf<IntArray>()
        val r2 = mutableListOf<IntArray>()
        val set = nsetOf(0(3, 2 x 10 x 10)) { r1.add(it.toIntArray());0 }
        for (index in set.indices()) {
            r2.add(index.toIntArray())
        }
        r1.forEach { println(it.asList()) }
        Assert.assertArrayEquals(r1.toTypedArray(), r2.toTypedArray())
    }

    @Test
    fun `make nset using dim 3`() {
        val r1 = mutableListOf<IntArray>()
        val r2 = mutableListOf<IntArray>()
        val dim =
                0(
                        2,
                        2,
                        2,
                        0(
                                2,
                                2 x 3 x 4,
                                (2 x 3)(
                                        2,
                                        3 x 4
                                )
                        )
                )
        val set = nsetOf(dim) { r1.add(it.toIntArray());0 }
        for (index in set.indices()) {
            println(index)
            r2.add(index.toIntArray())
        }
        var i = 0
        val size = r1.size
        for (i in 0 until size) {
            println("$i\t ${r1[i].asList()} vs.${r2[i].asList()}");
        }
        Assert.assertArrayEquals(r1.toTypedArray(), r2.toTypedArray())
    }

    @Test
    fun `make using dimension and {}`() {
        val dim =
                (2 x { 3 x 4 } x 4 x { 4 } x 2(3, 3))(2, 3 x 4 x { 1 }, 4)
        val dim2 = 2 x 2(3)
        val dim3 = 2 x 0(3, 4, 5)
        val set = nsetOf(dim) { 0 }
    }

    @Test
    fun `make nset`() {
        val set = nsetOf(1, 2, 3)
        for (i in set) {
            println(i)
        }
    }

    @Test
    fun `test general shape`() {
        var i = 0
        val tmp = nsetOf(2) { i++ }
        val set = nsetOf(2 x { 3 x 4 }) { i++ }
        println(set[0, 0, 0, 0])
        set[1] = tmp
        Assert.assertEquals(0, set[1, 0])
        Assert.assertEquals(1, set[1, 1])
        Assert.assertEquals(2, set[0, 0, 0, 0])
    }

    @Test
    fun `get sub set`() {
        var i = 0
        val set = nsetOf(2 x { 3 x 4 }) { i++ }
        for (withIndex in set.withIndices()) {
            println(withIndex)
        }

        for (withIndex in set(1).withIndices()) {
            println(withIndex)
        }

    }

    @Test
    fun `one level iterate`() {
        var i = 0
        val set = nsetOf(5) { i++ }
        i = 0
        for (a in set) {
            println(a)
            Assert.assertEquals(i++, a)
        }
    }


    @Test
    fun `two level iterate`() {
        var i = 0
        val set = nsetOf(5(3)) { i++ }
        i = 0
        for (a in set) {
            println(a)
            Assert.assertEquals(i++, a)
        }
        for (index in set.indices()) {
            println(index)
        }
        for ((idx, s) in set.withIndices()) {
            println("$idx=$s")
        }
    }

    @Test
    fun `test copycat`() {
        var i = 0
//        val set = nsetOf<Int>(3 x 3) { nsetOf<Int>(it[0] + 1) { i++ } }
//        val set2 = set.copycat<String> { println(it); it.toString() }
    }

    @Test
    fun `test null`() {
        val set = nsetOf(2 x 2) { 0 }
        val result: Int? = set[0, 0]
    }

    @Test
    fun `inti raw with correct index and 0`() {
        var i = 0
        var set = nsetOf(3 x 4 x 5) { idx -> println(idx); i++ }
//            set.forEach { println(it) }
//            println(set[2, 3, 4])
        set[2, 3, 4] = 100
//            println(set[2, 3, 4])
        set = nsetOf(3 x 4 x 5) { 0 }
        set[0, 0, 0] = 1
//            println(set[0, 0, 0])
    }

//    @Test
//    fun `variational bound`() {
//        var i = 0
//        val set = nsetOf<Int>(3 x 4) { nsetOf<Int>(5) { i++ } }
//        for (a in set) {
//            print("$a,")
//        }
//        println()
//        val a = set[2, 3, 4]
//        for (a in 0 until 3)
//            for (b in 0 until 4)
//                for (c in 0 until 5) {
//                    println(set[a, b, c])
//                }
//        set[2, 3, 4] = 100
//        set[0, 0] = nsetOf(2) { 1 }
//        println(set[0, 0, 1])
//        println(set[2, 3, 4])
//    }

    @Test
    fun `reset`() {
        val dim =
                0(
                        2,
                        2,
                        2,
                        0(
                                2,
                                2 x 3 x 4,
                                (2 x 3)(
                                        2,
                                        3 x 4
                                )
                        )
                )
        val set = nsetOf<Int>(dim) { 0 }
        for (withIndex in set.withIndices()) {
            println(withIndex)
        }
        set.set { _, old -> println(old);2 }
        for (withIndex in set.withIndices()) {
            println(withIndex)
        }
    }
}