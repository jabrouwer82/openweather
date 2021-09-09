package jacob.weather.model

import cats.implicits.*
import org.scalacheck.*
import org.specs2.*
import org.specs2.execute.Typecheck.*
import org.specs2.matcher.TypecheckMatchers
import org.specs2.mutable.Specification
import org.specs2.scalacheck.*

import scala.language.implicitConversions

class LatitudeSpec extends Specification with ScalaCheck with TypecheckMatchers:
  "Latitude" should {
    "require validation" in {
      typecheck("Latitude(0)") must failWith("")
      typecheck("Latitude.max.get.copy(milliDegrees = Int.MaxValue)") must failWith("")
    }

    "accept 0 degrees" in {
      Latitude.validated(0) should beSome(Latitude.Equator)
    }

    "accept min degrees" in {
      Latitude.validated(Latitude.minMilliDegrees) should beSome(Latitude.SouthPole)
    }

    "accept max" in {
      Latitude.validated(Latitude.maxMilliDegrees) should beSome(Latitude.NorthPole)
    }

    "accept valid millidegrees" in {
      Latitude.validated(-56_294) should beSome
    }

    "reject invalid millidegrees" in {
      Latitude.validated(100_000) should beNone
    }

    "accept valid millidegrees" in {
      Latitude.fromDouble(47.724) should beSome
    }

    "reject invalid millidegrees" in {
      Latitude.fromDouble(-295.256) should beNone
    }

    "produce simple strings" in prop { (g: (Int, String)) =>
      Latitude.NorthPole.simpleString === "90.000"
      Latitude.SouthPole.simpleString === "-90.000"
      Latitude.Equator.simpleString === "0.000"
      Latitude.validated(12_345).map(_.simpleString) should beSome("12.345")
      Latitude.validated(-12_340).map(_.simpleString) should beSome("-12.340")
    }

    "show fancy strings" in {
      Latitude.NorthPole.show === "90° N"
      Latitude.SouthPole.show === "90° S"
      Latitude.Equator.show === "0° N"
      Latitude.validated(12_345).map(_.show) should beSome("12.345° N")
      Latitude.validated(-12_340).map(_.show) should beSome("12.34° S")
    }

    "accept valid millidegrees from Gen" in prop { (d: Int) =>
      Latitude.validated(d) should beSome
    }.setGen(validMilliDegreeGen)

    "reject invalid millidegrees from Gen" in prop { (d: Int) =>
      Latitude.validated(d) should beNone
    }.setGen(invalidMilliDegreeGen)

    "accept valid millidegrees from Gen" in prop { (d: Double) =>
      Latitude.fromDouble(d) should beSome
    }.setGen(validDegreeGen)

    "reject invalid millidegrees from Gen" in prop { (d: Double) =>
      Latitude.fromDouble(d) should beNone
    }.setGen(invalidDegreeGen)

    "produce simple strings from Gen" in prop { (g: (Int, String)) =>
      Latitude.validated(g._1).map(_.simpleString) should beSome(g._2)
    }.setGen(validDegreeStringGen)
  }

  val validMilliDegreeGen: Gen[Int] = Arbitrary
    .arbitrary[Int]
    .filter(i => i < Latitude.maxMilliDegrees && i > Latitude.minMilliDegrees)
  val validDegreeGen: Gen[Double] = validMilliDegreeGen.map(_.toDouble / 1000)
  val validDegreeStringGen: Gen[(Int, String)] = validMilliDegreeGen
    .map(i => (i, f"${i.toDouble / 1000}%.3f"))

  val invalidMilliDegreeGen: Gen[Int] = Arbitrary
    .arbitrary[Int]
    .filter(i => i > Latitude.maxMilliDegrees || i < Latitude.minMilliDegrees)

  val invalidDegreeGen: Gen[Double] = invalidMilliDegreeGen.map(_.toDouble / 1000)

