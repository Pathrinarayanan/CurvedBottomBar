package com.example.curvedbottombar

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


@Composable
fun CurvedBottomBar(
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color.Red,
    height: Dp = 80.dp,
    horizontalPadding: Dp = 24.dp,
    content: @Composable RowScope.() -> Unit = {}
) {
    Box(modifier = modifier) {
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(height) // Use the height parameter here
        ) {
            val width = size.width
            val path = Path().apply {
                moveTo(0f, 0f)
                lineTo(width * 0.35f, 0f)
                cubicTo(
                    width * 0.42f, 0f,
                    width * 0.43f, size.height * 0.7f,
                    width * 0.5f, size.height * 0.7f
                )
                cubicTo(
                    width * 0.57f, size.height * 0.7f,
                    width * 0.58f, 0f,
                    width * 0.65f, 0f
                )
                lineTo(width, 0f)
                lineTo(width, size.height)
                lineTo(0f, size.height)
                close()
            }

            drawPath(
                path = path,
                color = backgroundColor
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(height)
                .padding(horizontal = horizontalPadding),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            content = content
        )
    }
}