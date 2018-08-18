package pl.klolo.game.extensions

import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec


internal class StringExtensionsKtTest : StringSpec({
    "should parse red color" {
        val white = "#ff0000".toColor()
        white.r shouldBe 1f
        white.g shouldBe 0f
        white.b shouldBe 0f
    }

    "should parse green color" {
        val white = "#00ff00".toColor()
        white.r shouldBe 0f
        white.g shouldBe 1f
        white.b shouldBe 0f
    }

    "should parse blue color" {
        val white = "#0000ff".toColor()
        white.r shouldBe 0f
        white.g shouldBe 0f
        white.b shouldBe 1f
    }

    "should parse white color" {
        val white = "#ffffff".toColor()
        white.r shouldBe 1f
        white.g shouldBe 1f
        white.b shouldBe 1f
    }
})