object Main {

  def main(args: Array[String]) = {
    println("Hello, Scala!")
    println(cnv256to16(-1) + ", " + cnv256toLimit(-1, 16))
    println(cnv256to16(0) + ", " + cnv256toLimit(0, 16))
    println(cnv256to16(0.1) + ", " + cnv256toLimit(0.1, 16))
    println(cnv256to16(100) + ", " + cnv256toLimit(100, 16))
    println(cnv256to16(127) + ", " + cnv256toLimit(127, 16))
    println(cnv256to16(128) + ", " + cnv256toLimit(128, 16))
    println(cnv256to16(129) + ", " + cnv256toLimit(129, 16))
    println(cnv256to16(200) + ", " + cnv256toLimit(200, 16))
    println(cnv256to16(255) + ", " + cnv256toLimit(255, 16))
    println(cnv256to16(256) + ", " + cnv256toLimit(256, 16))
    println(cnv256to16(257) + ", " + cnv256toLimit(257, 16))
  }

  def cnv256toLimit = { (n: Double, limit: Int) =>
    //    val b = 256 / limit
    //    n match {
    //      //case m if m < b => 0
    //      case m if (m >= 0) && (m <= 256) => (m / limit).floor.toInt
    //      //case m if m < 256 => limit
    //      case _ => -1
    //    }
    (n / limit).floor.toInt
  }

  def cnv256to16 = { (n: Double) =>
    n match {
      case m if m < 16 => 0
      case m if m < 32 => 1
      case m if m < 48 => 2
      case m if m < 64 => 3
      case m if m < 80 => 4
      case m if m < 96 => 5
      case m if m < 112 => 6
      case m if m < 128 => 7
      case m if m < 144 => 8
      case m if m < 160 => 9
      case m if m < 176 => 10
      case m if m < 192 => 11
      case m if m < 208 => 12
      case m if m < 224 => 13
      case m if m < 240 => 14
      case m if m < 256 => 15
      case _ => -1
    }
  }
}