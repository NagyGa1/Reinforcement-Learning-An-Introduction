package lab.mars.rl.model

import lab.mars.rl.util.matrix.MatrixSpec

abstract class ApproximateFunction<E>(var conv: (Array<out Any>) -> E) {
  abstract val w: MatrixSpec
  
  operator fun invoke(vararg args: Any) = _invoke(conv(args))
  
  fun `∇`(vararg args: Any) = `_∇`(conv(args))
  
  abstract fun _invoke(input: E): Double
  abstract fun `_∇`(input: E): MatrixSpec
}