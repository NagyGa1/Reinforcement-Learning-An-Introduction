package lab.mars.rl.algo.policy_gradient

import lab.mars.rl.algo.func_approx.FunctionApprox
import lab.mars.rl.algo.func_approx.FunctionApprox.Companion.log
import lab.mars.rl.model.*
import lab.mars.rl.model.impl.func.LinearFunc
import lab.mars.rl.util.buf.newBuf
import lab.mars.rl.util.log.debug
import lab.mars.rl.util.math.rand
import lab.mars.rl.util.matrix.times
import lab.mars.rl.util.matrix.Σ
import org.apache.commons.math3.util.FastMath

fun <E> FunctionApprox.REINFORCE(π: ApproximateFunction<E>, trans: (State, Action<State>) -> E) {
    for (episode in 1..episodes) {
        log.debug { "$episode/$episodes" }
        var step = 0
        val s = started()
        var a = rand(s.actions) { π(trans(s, it)) }
        val S = newBuf<State>()
        val A = newBuf<Action<State>>()
        val R = newBuf<Double>()

        S.append(s)
        R.append(0.0)
        var accu = 0.0
        var T: Int
        while (true) {
            step++
            A.append(a)
            val (s_next, reward) = a.sample()
            accu += reward
            R.append(accu)
            S.append(s_next)
            if (s_next.isTerminal()) {
                T = step
                break
            }
            a = rand(s.actions) { π(trans(s, it)) }
        }
        for (t in 0..T) {
            val G = accu - R[t]

            val `▽` = if (π is LinearFunc)
                π.x(trans(S[t], A[t])) - Σ(S[t].actions) { π(trans(S[t], it)) * π.x(trans(S[t], it)) }
            else
                π.`▽`(trans(S[t], A[t])) / π(trans(S[t], A[t]))
            π.w += α * FastMath.pow(γ, t) * G * `▽`
        }
    }
}