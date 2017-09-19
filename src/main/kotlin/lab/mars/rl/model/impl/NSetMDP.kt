@file:Suppress("NOTHING_TO_INLINE")

package lab.mars.rl.model.impl

import lab.mars.rl.model.Action
import lab.mars.rl.model.MDP
import lab.mars.rl.model.State
import lab.mars.rl.model.null_action
import lab.mars.rl.util.IntSlice
import lab.mars.rl.util.toDim
import lab.mars.rl.util.extension.nsetOf

/**
 * <p>
 * Created on 2017-09-14.
 * </p>
 *
 * @author wumo
 */

/**
 * @param gamma gamma 衰减因子
 * @param state_dim 统一的状态维度，V函数与状态集一致
 * @param action_dim 统一的动作维度，Q函数与状态集和动作集一致
 * @return 所有状态维度相同和动作维度相同的MDP实例
 */
inline fun NSetMDP(gamma: Double, state_dim: Any, action_dim: Any): MDP {
    val a_dim = action_dim.toDim()
    return NSetMDP(gamma, state_dim.toDim(), { a_dim })
}

/**
 * @param gamma  gamma 衰减因子
 * @param state_dim 统一的状态维度，V函数与状态集一致
 * @param action_dim 依据状态索引确定动作维度，Q函数与状态集和动作集一致
 * @return 统一状态维度而动作维度异构的MDP实例
 */
fun NSetMDP(gamma: Double, state_dim: Any, action_dim: (IntSlice) -> Any): MDP {
    val s_dim = state_dim.toDim()
    return MDP(
            gamma = gamma,
            states = nsetOf(s_dim) {
                State(it.copy()).apply { actions = nsetOf(action_dim(it).toDim()) { Action(it.copy()) } }
            },
            v_maker = { nsetOf(s_dim) { 0.0 } },
            q_maker = { nsetOf(s_dim) { nsetOf<Double>(action_dim(it).toDim()) { 0.0 } } },
            pi_maker = { nsetOf(s_dim) { null_action } })
}