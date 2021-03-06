package ppl.tests.scalatest.delite

import ppl.delite.runtime.graph.targets.Targets
import ppl.delite.framework.DeliteApplication
import ppl.delite.framework.codegen.cuda.TargetCuda
import ppl.dsl.optiml.{OptiMLApplicationRunner, OptiMLApplication}
import ppl.tests.scalatest._

/* Tests the generated code functionality for Delite ops, using OptiML data structures.
 *
 * author:  Arvind Sujeeth (asujeeth@stanford.edu)
 * created: 12/13/10
 *
 * Pervasive Parallelism Laboratory (PPL)
 * Stanford University
 *
 */

object DeliteMapRunner extends DeliteTestRunner with OptiMLApplicationRunner with DeliteMap
trait DeliteMap extends DeliteTestModule with OptiMLApplication {
  def main() = {

    val v = Vector[Double](1000,true)
    val v2 = v map { e => 10 }

    collect(v2.length == 1000)
    var i = 0
    while (i < v2.length) {
      collect(v2(i) == 10)
      i += 1
    }

    val ve = Vector[Double](0,true)
    val ve2 = ve map { e => 1 }
    collect(ve2.length == 0)
    
    mkReport
  }
}

object DeliteZipRunner extends DeliteTestRunner with OptiMLApplicationRunner with DeliteZip
trait DeliteZip extends DeliteTestModule with OptiMLApplication {
  def main() = {

    val v1 = Vector.ones(1000)
    val v2 = Vector[Double](1000,true) map { e => 2. }
    val v3 = v1 + v2

    collect(v3.length == 1000)

    var i = 0
    while (i < v3.length) {
      collect(v3(i) == 3)
      i += 1
    }

    val ve1 = Vector[Double](0,true)
    val ve2 = Vector[Double](0,true)    
    val ve3 = ve1.zip(ve2) { (a,b) => 1 }
    collect(ve3.length == 0)
    
    // TODO: one collection empty and the other not?
    // what is expected behavior? (should be an exception)
    
    mkReport
  }
}

object DeliteReduceRunner extends DeliteTestRunner with OptiMLApplicationRunner with DeliteReduce
trait DeliteReduce extends DeliteTestModule with OptiMLApplication {
  def main() = {

    val v = Vector.range(0, 1000)
    collect(v.sum == 499500)
    
    val ve = Vector[Int](0,true)
    collect(ve.sum == 0)

    mkReport
  }
}

object DeliteMapReduceRunner extends DeliteTestRunner with OptiMLApplicationRunner with DeliteMapReduce
trait DeliteMapReduce extends DeliteTestModule with OptiMLApplication {
  def main() = {

    val v = Vector.range(0, 1000)
    val x = sum(0, v.length) { i => v(i) }
    collect(x == 499500)

    mkReport
  }
}

object DeliteFilterRunner extends DeliteTestRunner with OptiMLApplicationRunner with DeliteFilter
trait DeliteFilter extends DeliteTestModule with OptiMLApplication {
  def main() = {

    val v1 = Vector.range(0, 100)
    val v2 = v1.filter(_ % 2 == 1)
    collect(v2.length == 50)

    var i = 0
    while (i < v2.length) {
      collect(v2(i) == v1(1+i*2))
      i += 1
    }

    val ve = Vector[Int](0,true)
    val ve2 = ve.filter(_ % 2 == 1)
    collect(ve2.length == 0)
    
    mkReport
  }
}

object DeliteForeachRunner extends DeliteTestRunner with OptiMLApplicationRunner with DeliteForeach
trait DeliteForeach extends DeliteTestModule with OptiMLApplication {
  def main() = {

    val v = Vector.range(0, 10)
    for (e <- v) {
      if ((e > 0) && (e < v.length-1)) {
        collect(v(e+1) - v(e-1) == 2)
      }
    }

    val ve = Vector[Int](0,true)
    for (e <- ve) {
      collect(false)
    }
    
    mkReport
  }
}

object DeliteZipWithReduceTupleRunner extends DeliteTestRunner with OptiMLApplicationRunner with DeliteZipWithReduceTuple
trait DeliteZipWithReduceTuple extends DeliteTestModule with OptiMLApplication {
  def main() = {

    val v = Vector.range(0, 10)
    collect(v.maxIndex == 9)

    mkReport
  }
}

object DeliteNestedMapRunner extends DeliteTestRunner with OptiMLApplicationRunner with DeliteNestedMap
trait DeliteNestedMap extends DeliteTestModule with OptiMLApplication {
  def main() = {

    val res = Vector[Double](1,true) map { e =>
      val v = Vector[Double](1000,true)
      v map { e => 10 }
    }

    val v2 = res(0)
    collect(v2.length == 1000)
    var i = 0
    while (i < v2.length) {
      collect(v2(i) == 10)
      i += 1
    }

    val res2 = Vector[Double](1,true) map { e =>
      val ve = Vector[Double](0,true)
      ve map { e => 10 }
    }
    collect(res2(0).length == 0)
    
    mkReport
  }
}

object DeliteNestedZipRunner extends DeliteTestRunner with OptiMLApplicationRunner with DeliteNestedZip
trait DeliteNestedZip extends DeliteTestModule with OptiMLApplication {
  def main() = {

    val res = Vector[Double](1,true) map { e =>
      val v1 = Vector.ones(1000)
      val v2 = Vector[Double](1000,true) map { e => 2. }
      v1 + v2
    }

    val v3 = res(0)
    collect(v3.length == 1000)
    var i = 0
    while (i < v3.length) {
      collect(v3(i) == 3)
      i += 1
    }
    
    val res2 = Vector[Double](1,true) map { e =>
      val ve1 = Vector.ones(0)
      val ve2 = Vector[Double](0,true) map { e => 2. }
      ve1 + ve2
    }
    collect(res2(0).length == 0)
    
    mkReport
  }
}

object DeliteNestedReduceRunner extends DeliteTestRunner with OptiMLApplicationRunner with DeliteNestedReduce
trait DeliteNestedReduce extends DeliteTestModule with OptiMLApplication {
  def main() = {

    val res = Vector[Double](1,true) map { e =>
      val v = Vector.range(0, 1000)
      v.sum
    }
    collect(res(0) == 499500)
    
    val res2 = Vector[Double](1,true) map { e =>
      val ve = Vector[Int](0, true)
      ve.sum
    }
    collect(res2(0) == 0)
    
    mkReport
  }
}

object DeliteNestedMapReduceRunner extends DeliteTestRunner with OptiMLApplicationRunner with DeliteNestedMapReduce
trait DeliteNestedMapReduce extends DeliteTestModule with OptiMLApplication {
  def main() = {

    val res = Vector[Double](1,true) map { e =>
      val v = Vector.range(0, 1000)
      sum(0, v.length) { i => v(i) }
    }
    collect(res(0) == 499500)

    mkReport
  }
}

object DeliteNestedForeachRunner extends DeliteTestRunner with OptiMLApplicationRunner with DeliteNestedForeach
trait DeliteNestedForeach extends DeliteTestModule with OptiMLApplication {
  def main() = {

    Vector[Double](1,true) foreach { e =>
      val v = Vector.range(0, 10)
      for (e <- v) {
        if ((e > 0) && (e < v.length-1)) {
          collect(v(e+1) - v(e-1) == 2)
        }
      }
      //e // fails with Unit return type because Java can't handle Array[Void]
    }
    
    Vector[Double](1,true) foreach { e =>
      val ve = Vector[Int](0, true)
      for (e <- ve) {
        collect(false)
      }
    }
    
    mkReport
  }
}

object DeliteIfThenElseRunner extends DeliteTestRunner with OptiMLApplicationRunner with DeliteIfThenElse
trait DeliteIfThenElse extends DeliteTestModule with OptiMLApplication {
  def main() = {

    val y = Vector.rand(10)
    if (y(0) == false){
      var x0 = 17
      collect(x0 == 17)
    }
    else {
      var x1 = 99
      collect(x1 == 99)
    }

    mkReport
  }
}

object DeliteHorizontalElemsRunner extends DeliteTestRunner with OptiMLApplicationRunner with DeliteHorizontalElems
trait DeliteHorizontalElems extends DeliteTestModule with OptiMLApplication {
  def main() = {
    var i = 0
    while (i < 10) { //small collection sizes, test with multiple threads!
      val size = readVar(i) //why?
      val v = Vector.ones(size)
      collect(v.sum == size)
      collect(v.count(_ > 1) == 0)
      collect((0::size).filter(_ % 2 == 0).length == (size+1)/2)
      collect(v.filter(_ > 1).length == 0)
      collect(v.map(e => e + 1).length == size)
      i += 1
    }
    mkReport
  }
}

class DeliteOpSuite extends DeliteSuite {
  def testDeliteMap() { compileAndTest(DeliteMapRunner, CHECK_MULTILOOP) }
  def testDeliteZip() { compileAndTest(DeliteZipRunner, CHECK_MULTILOOP) }
  def testDeliteReduce() { compileAndTest(DeliteReduceRunner, CHECK_MULTILOOP) }
  def testDeliteMapReduce() { compileAndTest(DeliteMapReduceRunner, CHECK_MULTILOOP) }
  def testDeliteFilter() { compileAndTest(DeliteFilterRunner) }
  def testDeliteForeach() { compileAndTest(DeliteForeachRunner) }
  def testDeliteZipWithReduceTuple() { compileAndTest(DeliteZipWithReduceTupleRunner, CHECK_MULTILOOP) }
  def testDeliteNestedMap() { compileAndTest(DeliteNestedMapRunner) }
  def testDeliteHorizontalElems() { compileAndTest(DeliteHorizontalElemsRunner, CHECK_MULTILOOP) }
  def testDeliteNestedZip() { compileAndTest(DeliteNestedZipRunner) }
  def testDeliteNestedReduce() { compileAndTest(DeliteNestedReduceRunner, CHECK_MULTILOOP) }
  def testDeliteNestedMapReduce() { compileAndTest(DeliteNestedMapReduceRunner, CHECK_MULTILOOP) }
  def testDeliteNestedForeach() { compileAndTest(DeliteNestedForeachRunner) }
  def testDeliteIfThenElse() { compileAndTest(DeliteIfThenElseRunner) }
}
