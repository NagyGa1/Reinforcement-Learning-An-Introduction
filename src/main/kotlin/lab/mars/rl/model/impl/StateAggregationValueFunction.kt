package lab.mars.rl.model.impl

import lab.mars.rl.model.State
import lab.mars.rl.model.ValueFunction
import lab.mars.rl.util.matrix.Matrix
import org.apache.commons.math3.util.FastMath.ceil

class StateAggregationValueFunction(numStates: Int, numOfGroups: Int) : ValueFunction {
    override fun `∇`(s: State): Matrix {
        TODO("not implemented")
    }

    val w = DoubleArray(numOfGroups) { 0.0 }
    val groupSize = ceil(numStates.toDouble() / numOfGroups).toInt()
    override fun invoke(s: State): Double {
        if (s.isTerminal()) return 0.0
        val groupIdx = s[0] / groupSize
        return w[groupIdx]
    }

    override fun update(s: State, delta: Double) {
        if (s.isTerminal()) return
        val groupIdx = s[0] / groupSize
        w[groupIdx] += delta
    }

}