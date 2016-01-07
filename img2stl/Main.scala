import java.io.PrintWriter
import java.io.File
import javax.imageio.ImageIO
import java.awt.Color
import scala.collection.mutable.ListBuffer

object Main {

  def main(args: Array[String]) = {
    println("Application has started...")

    val toGray = { argb: Int =>
      val c = new Color(argb)
      // NTSC係数による加重平均法（近似値）
      // (2 * c.getRed + 4 * c.getGreen + c.getBlue) / 7d

      // 中間値
      (c.getRed + c.getGreen + c.getBlue) / 3d
    }

    val writeCube = { (x: Double, y: Double, z: Double, dx: Double, dy: Double, dz: Double) =>
      if ((dx > 0d) && (dy > 0d) && (dz > 0d)) {
        val base = """facet normal %s %s %s
                   |outer loop
                   |%s
                   |%s
                   |%s
                   |endloop
                   |endfacet
                   |""".stripMargin

        val s0 = 0d.formatted("%.6e")
        val s1 = 1d.formatted("%.6e")
        val si = -1d.formatted("%.6e")

        val d = 0.1d
        val sx1 = (x * d).formatted("%.6e")
        val sy1 = (y * d).formatted("%.6e")
        val sz1 = (z * d).formatted("%.6e")
        val sx2 = ((x + dx) * d).formatted("%.6e")
        val sy2 = ((y + dy) * d).formatted("%.6e")
        val sz2 = ((z + dz) * d).formatted("%.6e")

        val vA = s"vertex $sx1 $sy1 $sz1"
        val vB = s"vertex $sx2 $sy1 $sz1"
        val vC = s"vertex $sx2 $sy2 $sz1"
        val vD = s"vertex $sx1 $sy2 $sz1"
        val vO = s"vertex $sx1 $sy1 $sz2"
        val vP = s"vertex $sx2 $sy1 $sz2"
        val vQ = s"vertex $sx2 $sy2 $sz2"
        val vR = s"vertex $sx1 $sy2 $sz2"

        val res = ListBuffer[String]()

        // U face
        res += base.format(s0, s0, s1, vO, vP, vR)
        res += base.format(s0, s0, s1, vQ, vR, vP)
        // R face
        res += base.format(s1, s0, s0, vP, vB, vQ)
        res += base.format(s1, s0, s0, vC, vQ, vB)
        // D face
        res += base.format(s0, s0, si, vB, vA, vC)
        res += base.format(s0, s0, si, vD, vC, vA)
        // L face
        res += base.format(si, s0, s0, vA, vO, vD)
        res += base.format(si, s0, s0, vR, vD, vO)
        // B face
        res += base.format(s0, s1, s0, vR, vQ, vD)
        res += base.format(s0, s1, s0, vC, vD, vQ)
        // F face
        res += base.format(s0, si, s0, vP, vO, vB)
        res += base.format(s0, si, s0, vA, vB, vO)

        res.mkString
      } else ""
    }

    //    val writeCube4Pixel = { (x: Int, y: Int, dz: Double) =>
    //
    //      val d = 0.1d
    //      val sx1 = (d * x).formatted("%.6e")
    //      val sy1 = (d * -y).formatted("%.6e")
    //      val sx2 = (d * (x + 1)).formatted("%.6e")
    //      val sy2 = (d * -(y + 1)).formatted("%.6e")
    //
    //      val px = d * x
    //      val py = d * -(y + 1)
    //
    //      writeCube(d * x, d * -(y + 1), 0d, d, d, dz * d)
    //
    //      //      val vA = s"vertex $sx1 $sy1 $s0"
    //      //      val vB = s"vertex $sx2 $sy1 $s0"
    //      //      val vC = s"vertex $sx2 $sy2 $s0"
    //      //      val vD = s"vertex $sx1 $sy2 $s0"
    //      //
    //      //      val vO = f"vertex $sx1 $sy1 ${dz * d}%.6e"
    //      //      val vP = f"vertex $sx2 $sy1 ${dz * d}%.6e"
    //      //      val vQ = f"vertex $sx2 $sy2 ${dz * d}%.6e"
    //      //      val vR = f"vertex $sx1 $sy2 ${dz * d}%.6e"
    //    }
    val writeCube4Pixel = { (x: Int, y: Int, dz: Double) =>
      writeCube(x, -(y + 1), 0d, 1d, 1d, dz)
    }
    val writeCube4Line = { (x: Int, y: Int, dz: Double, len: Int) =>
      // x方向にlenの長さの直方体
      writeCube(x, -(y + 1), 0d, len, 1d, dz)
      //s"x: $x, y: $y, dz: $dz, len: $len\n"
    }

    val outW = new PrintWriter("outputW.stl", "utf8")
    val outB = new PrintWriter("outputB.stl", "utf8")
    val outR = new PrintWriter("outputR.stl", "utf8")
    val log = new PrintWriter("log.txt", "utf8")

    try {
      // Start行の書き込み
      outW.write("solid cube-ascii\n")
      outB.write("solid cube-ascii\n")
      outR.write("solid cube-ascii\n")

      // 画像読み込み
      val img = ImageIO.read(new File("test.bmp"))
      val w = img.getWidth()
      val h = img.getHeight()

      //      val cnv16 = { (d: Int) =>
      //        if (d > 256 - (256 / 16)) 4d else (4d / 16) * (d / (256 / 16)).ceil
      //      }
      //      log.write(s"${cnv16(0)}\n")
      //      log.write(s"${cnv16(1)}\n")
      //      log.write(s"${cnv16(238)}\n")
      //      log.write(s"${cnv16(239)}\n")
      //      log.write(s"${cnv16(240)}\n")
      //      log.write(s"${cnv16(241)}\n")
      //      log.write(s"${cnv16(255)}\n")
      //      log.write(s"${cnv16(256)}\n\n\n")

      val max = 15d
      val n = 16

      // 1ピクセルずつ取得
      for (y <- 0 to (h - 1)) {
        var kvol = -1d
        var klen = 0
        var rvol = -1d
        var rlen = 0

        for (x <- 0 to (w - 1)) {
          val argb = img.getRGB(x, y)
          // グレースケール (0~255)
          val d = toGray(argb)
          // ポリゴンの書き込み
          // outW.write(wStr(x, y, (d / 256) * h))
          // outB.write(wStr(x, y, ((256 - d) / 256) * h))

          // キーブレイク処理にする
          val vol = if (d < 128) 0d else max
          if (vol == kvol) {
            klen += 1
          } else {
            // output
            outW.write(writeCube4Line(x - klen, y, kvol, klen))
            outB.write(writeCube4Line(x - klen, y, max - kvol, klen))
            // save key
            kvol = vol
            klen = 1
          }

          // val vol4 = if (d > 256 - (256 / n)) max else (max / n) * (d / (256 / n)).ceil
          // val vol16 = (max / (n - 1)) * (d * n / 256).floor
          val vol16 = (d / 16).floor
          // val vol16 = if (d < 128) 0d else (d - 128)
          log.write(s"$vol16  $vol  ($d)\n")

          if (vol16 == rvol) {
            rlen += 1
          } else {
            // output
            outR.write(writeCube4Line(x - rlen, y, rvol, rlen))
            // save key
            rvol = vol16
            rlen = 1
          }
        }
        // output
        outW.write(writeCube4Line(w - klen, y, kvol, klen))
        outB.write(writeCube4Line(w - klen, y, max - kvol, klen))
        outR.write(writeCube4Line(w - rlen, y, rvol, rlen))
      }
      // output (base cube)
      outW.write(writeCube(0d, -h, -2d, w, h, 2d))
      outB.write(writeCube(0d, -h, -2d, w, h, 2d))
      outR.write(writeCube(0d, -h, -2d, w, h, 2d))

      // End行の書き込み
      outW.write("endsolid\n")
      outB.write("endsolid\n")
      outR.write("endsolid\n")
    } finally {
      outW.close()
      outB.close()
      outR.close()
      log.close()
    }

    println("output end...")
  }

}