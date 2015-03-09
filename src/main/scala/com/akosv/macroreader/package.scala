package com.akosv

package object macroreader {

  private[macroreader] implicit class VeryRichString(x: String) {
    def toDecodedInt = {
      java.lang.Integer.decode(x).asInstanceOf[Int]
    }

    def toDecodedLong = {
      java.lang.Long.decode(x).asInstanceOf[Long]
    }

    def toDecodedByte = {
      java.lang.Byte.decode(x).asInstanceOf[Byte]
    }
  }

}
