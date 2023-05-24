package com.sandbox.serviceproject.ui.theme

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class Dimens(
    val grid: Grid,
    val staticGrid: StaticGrid
)

data class Grid(
    val x1: Dp, val x2: Dp, val x3: Dp, val x4: Dp, val x5: Dp, val x6: Dp,
    val x7: Dp, val x8: Dp, val x9: Dp, val x10: Dp, val x11: Dp, val x12: Dp,
    val x13: Dp, val x14: Dp, val x15: Dp, val x16: Dp, val x17: Dp, val x18: Dp,
    val x19: Dp, val x20: Dp, val x21: Dp, val x22: Dp, val x23: Dp, val x24: Dp,
)

data class StaticGrid(
    val x1: Dp = 4.dp, val x2: Dp = 8.dp, val x3: Dp = 12.dp, val x4: Dp = 16.dp,
    val x5: Dp = 20.dp, val x6: Dp = 24.dp, val x7: Dp = 28.dp, val x8: Dp = 32.dp,
    val x9: Dp = 36.dp, val x10: Dp = 40.dp, val x11: Dp = 44.dp, val x12: Dp = 48.dp,
    val x13: Dp = 52.dp, val x14: Dp = 56.dp, val x15: Dp = 60.dp, val x16: Dp = 64.dp,
    val x17: Dp = 68.dp, val x18: Dp = 72.dp, val x19: Dp = 76.dp, val x20: Dp = 80.dp,
    val x21: Dp = 84.dp, val x22: Dp = 88.dp, val x23: Dp = 92.dp, val x24: Dp = 96.dp
)
