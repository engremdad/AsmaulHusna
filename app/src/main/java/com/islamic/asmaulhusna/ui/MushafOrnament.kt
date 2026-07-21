package com.islamic.asmaulhusna.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.islamic.asmaulhusna.ui.theme.Gold

/**
 * A faint gold star-dot lattice — the ambient texture behind every screen,
 * echoing the pin-prick gilding of an illuminated page. Cheap: it draws a grid
 * of small dots directly in the background layer.
 */
fun Modifier.starLattice(step: Dp = 26.dp): Modifier = drawBehind {
    val gap = step.toPx()
    val r = 1.1f
    val colour = Gold.copy(alpha = 0.10f)
    var y = gap / 2f
    while (y < size.height) {
        var x = gap / 2f
        while (x < size.width) {
            drawCircle(color = colour, radius = r, center = Offset(x, y))
            x += gap
        }
        y += gap
    }
}

/**
 * The four L-shaped gold corner brackets that frame a name on the detail hero,
 * mirroring the manuscript mockup. Draws inside the receiving Box.
 */
fun Modifier.cornerBrackets(
    inset: Dp = 10.dp,
    arm: Dp = 16.dp
): Modifier = drawBehind {
    val i = inset.toPx()
    val a = arm.toPx()
    val w = 1.5.dp.toPx()
    val c = Gold.copy(alpha = 0.7f)
    fun corner(ox: Float, oy: Float, dx: Int, dy: Int) {
        // horizontal arm
        drawLine(c, Offset(ox, oy), Offset(ox + dx * a, oy), strokeWidth = w)
        // vertical arm
        drawLine(c, Offset(ox, oy), Offset(ox, oy + dy * a), strokeWidth = w)
    }
    corner(i, i, +1, +1)                        // top-left
    corner(size.width - i, i, -1, +1)           // top-right
    corner(i, size.height - i, +1, -1)          // bottom-left
    corner(size.width - i, size.height - i, -1, -1) // bottom-right
}

/** Fills its parent with the ambient lattice; place at the bottom of a Box. */
@Composable
fun LatticeBackground(modifier: Modifier = Modifier) {
    Box(modifier.fillMaxSize().starLattice())
}
