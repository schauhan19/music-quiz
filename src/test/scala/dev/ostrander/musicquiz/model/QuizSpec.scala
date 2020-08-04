package dev.ostrander.musicquiz.model

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should
import scala.util.Try

class QuizSpec extends AnyFlatSpec with should.Matchers {
  "Quiz.songs" should "load songs.json resource" in {
    val songs = Try { Quiz.songs }
    songs.isSuccess should be (true)
  }
}