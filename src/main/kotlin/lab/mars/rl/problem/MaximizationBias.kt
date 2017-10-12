package lab.mars.rl.problem

import lab.mars.rl.model.MDP
import lab.mars.rl.model.Possible
import lab.mars.rl.model.impl.CNSetMDP
import lab.mars.rl.util.Rand
import lab.mars.rl.util.dimension.x
import lab.mars.rl.util.emptyNSet

object MaximizationBias {
    val mean = -0.1
    val actionsOfB = 10
    fun make(): MDP {
        val mdp = CNSetMDP(gamma = 1.0,
                           state_dim = 4,
                           action_dim = {
                               when (it[0]) {
                                   1 -> actionsOfB
                                   2 -> 2
                                   else -> 1
                               }
                           })
        mdp.apply {
            states[0].actions = emptyNSet()
            states[3].actions = emptyNSet()
            started = states(2)
            for (a in states[2].actions)
                a.sample = {
                    val next = if (a[0] == 0) 1 else 3
                    Possible(states[next], 0.0, 1.0)
                }
            for (a in states[1].actions)
                a.sample = {
                    Possible(states[0], Rand().nextGaussian() + mean, 1.0)
                }
        }
        return mdp
    }
}