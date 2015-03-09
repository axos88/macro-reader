package com.akosv.macroreader

import scala.collection.{immutable, mutable}

object Macros {
  import com.akosv.macroreader.VeryRichString

  type Macrodefs = immutable.Map[String, String]

  implicit class RichMacrodefs(md: Macrodefs)  {
    def Int(key: String): Int = md(key).toDecodedInt
    def Byte(key: String): Byte = md(key).toDecodedByte
  }

  val data: mutable.Map[String, Macrodefs] = new mutable.HashMap[String, Macrodefs]()

  def load(f: String): Macrodefs = {
    import scala.sys.process._

    val lines = s"echo #include<${f.toString}>" #| "gcc -dM -E -" lineStream

    val macros = lines.toList.flatMap { x =>
      val op :: name :: values = x.split(" ").toList

      op match {
        case "#define" =>
          values match {
            case Nil => None
            case value :: _ => Some((name, value))
          }
        case _ => None
      }
    }

    data.update(f, macros.toMap)
    macros.toMap
  }

  def apply(f: String): Macrodefs = {
    data.getOrElse(f, load(f))
  }

  def apply(fs: String*): Macrodefs = {
    val maps: Seq[Macrodefs] = fs.map(f => apply(f))

    maps.foldRight(Map().asInstanceOf[Macrodefs]) { (a, i) => a ++ i }
  }
}
