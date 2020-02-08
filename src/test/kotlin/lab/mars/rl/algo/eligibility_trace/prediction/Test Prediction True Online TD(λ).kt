package lab.mars.rl.algo.eligibility_trace.prediction

import javafx.application.Application
import kotlinx.coroutines.runBlocking
import lab.mars.rl.model.impl.func.LinearFunc
import lab.mars.rl.model.impl.func.SimpleTileCoding
import lab.mars.rl.model.impl.mdp.IndexedState
import lab.mars.rl.problem.`19-state RandomWalk`
import lab.mars.rl.util.*
import lab.mars.rl.util.tuples.tuple2
import lab.mars.rl.util.ui.ChartApp
import lab.mars.rl.util.ui.D2DChart
import lab.mars.rl.util.ui.Line
import lab.mars.rl.util.ui.LineChart
import org.apache.commons.math3.util.FastMath.pow
import org.apache.commons.math3.util.FastMath.sqrt
import org.junit.Test

class `Test Prediction True Online TDλ` {
  @Test
  fun `Performance`() {

    val (prob, π) = `19-state RandomWalk`.make()
    val realV = listOf(-20..20 step 2) { it / 20.0 }
    realV[0] = 0.0
    realV[20] = 0.0
    
    val λs = listOf(0.0, 0.4, 0.8, 0.9, 0.95, 0.975, 0.99, 1.0)
    val αs = listOf(100) { it * 0.01 }
    
    val episodes = 10
    val runs = 100
    val truncateValue = 0.6
    
    val chart = LineChart("True Online TD(λ)", "α", "Average RMS")
    runBlocking {
      for (λ in λs) {
        val line = Line("λ=$λ")
        chart += line
        asyncs(αs) { α ->
          var rms_sum = 0.0
          asyncs(runs) { run ->
            val func = LinearFunc(
                SimpleTileCoding(1,
                                 prob.states.size,
                                 1,
                                 0.0) { (s) -> (s as IndexedState)[0].toDouble() }
            )
            var rms = 0.0
            prob.`True Online TD(λ) prediction`(
                Vfunc = func, π = π,
                α = α, λ = λ,
                episodes = episodes,
                episodeListener = { _, _ ->
                  var error = 0.0
                  for (s in prob.states)
                    error += pow(func(s) - realV[s[0]], 2)
                  error /= prob.states.size
                  rms += sqrt(error)
                })
            println("finish λ=$λ α=${α.format(2)} run=$run")
            rms
          }.await { rms_sum += it }
          println("finish λ=$λ α=$α")
          tuple2(α, rms_sum / (episodes * runs))
        }.await { (α, rms) ->
          if (rms < truncateValue)
            line[α] = rms
        }
        println("finish λ=$λ")
      }
    }
    D2DChart.charts += chart
    Application.launch(ChartApp::class.java)
  }
}