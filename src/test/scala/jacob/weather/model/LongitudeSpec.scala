package jacob.weather.model

import cats.implicits.*
import org.scalacheck.*
import org.specs2.*
import org.specs2.execute.Typecheck.*
import org.specs2.matcher.TypecheckMatchers
import org.specs2.mutable.Specification
import org.specs2.scalacheck.*

import scala.language.implicitConversions

class LongitudeSpec extends Specification with ScalaCheck with TypecheckMatchers:
  "Longitude" should {
    "require validation" in {
      typecheck("Longitude(0)") must failWith("")
      typecheck("Longitude.max.get.copy(milliDegrees = Int.MaxValue)") must failWith("")
    }

    "accept the prime meridan" in {
      Longitude.validated(0) should beSome(Longitude.PrimeMeridian)
    }

    "accept the western antimeridian" in {
      Longitude.validated(Longitude.minMilliDegrees) should beSome(Longitude.AntiMeridianWest)
    }

    "accept the eastern antimeridian" in {
      Longitude.validated(Longitude.maxMilliDegrees) should beSome(Longitude.AntiMeridianEast)
    }

    "accept valid millidegrees" in {
      Longitude.validated(134_629) should beSome
    }

    "reject invalid millidegrees" in {
      Longitude.validated(-900_000) should beNone
    }

    "accept valid millidegrees" in {
      Longitude.fromDouble(-54.294) should beSome
    }

    "reject invalid millidegrees" in {
      Longitude.fromDouble(5629.410) should beNone
    }

    "produce simple strings" in {
      Longitude.AntiMeridianEast.simpleString === "180.000"
      Longitude.AntiMeridianWest.simpleString === "-180.000"
      Longitude.PrimeMeridian.simpleString === "0.000"
      Longitude.validated(123_456).map(_.simpleString) should beSome("123.456")
      Longitude.validated(-123_450).map(_.simpleString) should beSome("-123.450")
    }

    "show fancy strings" in {
      Longitude.AntiMeridianEast.show === "180° E"
      Longitude.AntiMeridianWest.show === "180° W"
      Longitude.PrimeMeridian.show === "0° E"
      Longitude.validated(123_456).map(_.show) should beSome("123.456° E")
      Longitude.validated(-123_450).map(_.show) should beSome("123.45° W")
    }

    "accept valid millidegreesfrom Gen" in prop { (d: Int) =>
      Longitude.validated(d) should beSome
    }.setGen(validMilliDegreeGen)

    "reject invalid millidegreesfrom Gen" in prop { (d: Int) =>
      Longitude.validated(d) should beNone
    }.setGen(invalidMilliDegreeGen)

    "accept valid millidegreesfrom Gen" in prop { (d: Double) =>
      Longitude.fromDouble(d) should beSome
    }.setGen(validDegreeGen)

    "reject invalid millidegreesfrom Gen" in prop { (d: Double) =>
      Longitude.fromDouble(d) should beNone
    }.setGen(invalidDegreeGen)

    "produce simple strings from Gen" in prop { (g: (Int, String)) =>
      Longitude.validated(g._1).map(_.simpleString) should beSome(g._2)
    }.setGen(validDegreeStringGen)

  }

  val validMilliDegreeGen: Gen[Int] = Arbitrary
    .arbitrary[Int]
    .filter(i => i < Longitude.maxMilliDegrees && i > Longitude.minMilliDegrees)
  val validDegreeGen: Gen[Double] = validMilliDegreeGen.map(_.toDouble / 1000)
  val validDegreeStringGen: Gen[(Int, String)] = validMilliDegreeGen
    .map(i => (i, f"${i.toDouble / 1000}%.3f"))

  val invalidMilliDegreeGen: Gen[Int] = Arbitrary
    .arbitrary[Int]
    .filter(i => i > Longitude.maxMilliDegrees || i < Longitude.minMilliDegrees)
  val invalidDegreeGen: Gen[Double] = invalidMilliDegreeGen.map(_.toDouble / 1000)
