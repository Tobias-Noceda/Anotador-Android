package com.tobs.anotador.ui.theme

import androidx.compose.ui.graphics.Color
import kotlin.math.*

val primary = Color(0xFF338447)
val secondary = Color(0xFFFF5900)

fun Color.darken(factor: Float): Color {
    return this.copy(
        red = red * factor,
        green = green * factor,
        blue = blue * factor
    )
}

fun Color.desaturate(saturationFactor: Float): Color {
    // Convert RGB to HSL
    val r = red
    val g = green
    val b = blue

    val max = max(r, max(g, b))
    val min = min(r, min(g, b))
    val l = (max + min) / 2

    val d = max - min
    var h = 0f
    var s = 0f

    if (max != min) {
        s = if (l > 0.5f) d / (2 - max - min) else d / (max + min)
        when (max) {
            r -> h = (g - b) / d + (if (g < b) 6 else 0)
            g -> h = (b - r) / d + 2
            b -> h = (r - g) / d + 4
        }
        h /= 6
    }

    // Desaturate
    s *= saturationFactor

    // Convert HSL back to RGB
    fun hue2rgb(p: Float, q: Float, t: Float): Float {
        var tVar = t
        if (tVar < 0) tVar += 1f
        if (tVar > 1) tVar -= 1f
        if (tVar < 1f / 6f) return p + (q - p) * 6f * tVar
        if (tVar < 1f / 2f) return q
        if (tVar < 2f / 3f) return p + (q - p) * (2f / 3f - tVar) * 6f
        return p
    }
    val q = if (l < 0.5f) l * (1 + s) else l + s - l * s
    val p = 2 * l - q

    val rFinal = hue2rgb(p, q, h + 1f / 3f)
    val gFinal = hue2rgb(p, q, h)
    val bFinal = hue2rgb(p, q, h - 1f / 3f)

    return Color(rFinal, gFinal, bFinal)
}