package controllers

import play.api._
import play.api.mvc._
import play.api.libs.json._

object Application extends Controller {

  def index = Action {
    Ok(views.html.sudoku())
  }

  def solveSudoku(prm: String) = Action {

    val p1 = (Stream(prm) /: (0 to 80))((st, i) => {

      val q1 = st.flatMap(s => {
        //println("i " + i + " flatmap s : " + s.substring(0, 50))
        if (s(i) != '0')
          Stream(s)
        else {
          ('1' to '9').toStream.filter(c => {
            // row col box に同じ数字がないかチェック
            (0 to 8).forall(j => {
              val t4 = Seq(
                i - i % 9 + j,
                i % 9 + j * 9,
                i - i % 27 + j / 3 * 9 + i % 9 / 3 * 3 + j % 3)
              t4.forall(s(_) != c)
            })
          }).map(s.updated(i, _))
        }
      })
      val q2 = q1.mkString
      //println(q2.substring(0, 9) + " " + q2.substring(9, 9) + " " + q2.substring(18, 9) + " " + q2.substring(27, 9) + " " + q2.substring(36, 9) + " " + q2.substring(45, 9))
      print(q2.substring(0, 9))
      print(" " + q2.substring(9, 18))
      print(" " + q2.substring(18, 27))
      print(" " + q2.substring(27, 36))
      print(" " + q2.substring(36, 45))
      print(" " + q2.substring(45, 54))
      print(" " + q2.substring(54, 63))
      println(" " + q2.substring(63, 72))
      q1
    })
    //p1.foreach(_.grouped(9).foreach(println))

    Ok(Json.toJson(p1.mkString.map(_.toString.toInt)))
  }

  def solveSudoku2(prm: String) = Action {

    var l = prm.padTo(81, "0").map(_.toString.toInt)

    var cnt = -1

    var cells = l.map(x => {
      cnt += 1
      var col = cnt % 9
      var row = (cnt - col) / 9

      var box = cnt match {
        case 0 | 1 | 2 | 9 | 10 | 11 | 18 | 19 | 20 => 0
        case 3 | 4 | 5 | 12 | 13 | 14 | 21 | 22 | 23 => 1
        case 6 | 7 | 8 | 15 | 16 | 17 | 24 | 25 | 26 => 2
        case 27 | 28 | 29 | 36 | 37 | 38 | 45 | 46 | 47 => 3
        case 30 | 31 | 32 | 39 | 40 | 41 | 48 | 49 | 50 => 4
        case 33 | 34 | 35 | 42 | 43 | 44 | 51 | 52 | 53 => 5
        case 54 | 55 | 56 | 63 | 64 | 65 | 72 | 73 | 74 => 6
        case 57 | 58 | 59 | 66 | 67 | 68 | 75 | 76 | 77 => 7
        case 60 | 61 | 62 | 69 | 70 | 71 | 78 | 79 | 80 => 8
      }

      new cell(x, row, col, box)
    })

    // 簡単な解法だけで解く
    while (_solve(cells)) {}

    // 解けた？
    var unknownCells = cells.filter(!_.solved)
    println("unknown cell count:" + unknownCells.length)

    // ダメなら総当り
    if (unknownCells.length > 0) {
      //      _testKoho(cells, unknownCells)
    }

    Ok(Json.toJson(cells.map(_.num)))
  }

  private def _solve(cells: Seq[cell]): Boolean = {

    var result = false

    cells.foreach(x => {
      if (!x.solved) {
        cells.filter(y => {
          y.solved && (y.rowIndex == x.rowIndex || y.colIndex == x.colIndex || y.boxIndex == x.boxIndex)
        }).foreach(z => result = result || x.delKoho(z.num))
        result = result || x.solve()
      }
    })

    result
  }

  private def _testKoho(cells: Seq[cell], partCells: Seq[cell]): Boolean = {

    if (partCells.size == 10) {
      println(cells.filter(!_.solved).map(_.num))
    }

    var cell = partCells.head
    for (k <- cell.koho) {
      cell.num = k

      if (partCells.size > 1) {
        _testKoho(cells, partCells.tail)
      } else {
        // check
        //        cells.foreach(x => {
        //          print(x.num)
        //        })
        //        println(_check(cells))
      }
    }

    false
  }

  private def _check(cells: Seq[cell]): Boolean = {

    var result = true

    for (i <- 0 to 8) {
      result = result &&
        (cells.filter(_.rowIndex == i).map(_.num).toSet.filter(_ != 0).size == 9) &&
        (cells.filter(_.colIndex == i).map(_.num).toSet.filter(_ != 0).size == 9) &&
        (cells.filter(_.boxIndex == i).map(_.num).toSet.filter(_ != 0).size == 9)
    }
    result
  }

  class cell(var num: Int, var rowIndex: Int, var colIndex: Int, var boxIndex: Int) {
    var solved = num != 0
    var koho = Range(1, 9 + 1).filter(_ != num)

    def delKoho(x: Int): Boolean = {
      val result = koho.filter(_ != x)
      if (koho == result) {
        false
      } else {
        koho = result
        true
      }
    }

    def solve(): Boolean = {
      //      println(this.rowIndex + "-" + this.colIndex + " : " + this.koho)
      if (!this.solved && (this.koho.length == 1)) {
        num = this.koho.head
        this.solved = true
        true
      } else {
        false
      }
    }
  }

}