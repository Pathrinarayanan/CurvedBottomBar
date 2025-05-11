package com.example.curvedbottombar

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.curvedbottombar.model.NavItem
@Composable
fun FloatedNavigationBar(
    modifier: Modifier = Modifier,
    selectedIndex: Int,
    items: List<NavItem>,
    onItemSelected: (Int) -> Unit,
    ballColor: Color = MaterialTheme.colorScheme.primary,
    backgroundColor: Color = MaterialTheme.colorScheme.primaryContainer,
    indicatorColor: Color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
) {
    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .height(80.dp)
            .shadow(elevation = 16.dp, shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
            .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
            .background(backgroundColor)
    ) {
        val maxWidth = constraints.maxWidth
        val itemWidth = maxWidth / items.size.toFloat()
        val ballPosition = remember { Animatable((itemWidth * selectedIndex) + (itemWidth / 2)) }

        LaunchedEffect(selectedIndex) {
            ballPosition.animateTo(
                targetValue = (itemWidth * selectedIndex) + (itemWidth / 2),
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
        }

        // Background indicators
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            items.forEachIndexed { index, _ ->
                val isSelected = index == selectedIndex
                val bgAlpha by animateFloatAsState(
                    targetValue = if (isSelected) 1f else 0f,
                    animationSpec = tween(300)
                )

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(8.dp)
                        .clip(CircleShape)
                        .background(indicatorColor.copy(alpha = bgAlpha))
                        .align(Alignment.CenterVertically)
                )
            }
        }

        // Floating ball indicator
        Box(
            modifier = Modifier
                .size(52.dp)
                .offset(x = with(LocalDensity.current) { (ballPosition.value - 26.dp.toPx()).toDp() })
                .padding(top = 8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .shadow(8.dp, CircleShape)
                    .background(ballColor, CircleShape)
                    .align(Alignment.Center)
            )
        }

        // Navigation items
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEachIndexed { index, item ->
                val isSelected = index == selectedIndex
                val contentAlpha by animateFloatAsState(
                    targetValue = if (isSelected) 1f else 0.6f,
                    animationSpec = tween(300)
                )
                val iconSize by animateFloatAsState(
                    targetValue = if (isSelected) 1.2f else 1f,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                )
                val iconTranslationY by animateFloatAsState(
                    targetValue = if (isSelected) -16f else 0f,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                )

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .weight(1f)
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) {
                            if (index != selectedIndex) {
                                onItemSelected(index)
                            }
                        }
                ) {
                    BadgedBox(
                        badge = {

                        },
                        modifier = Modifier
                            .padding(bottom = 4.dp)
                            .graphicsLayer {
                                scaleX = iconSize
                                scaleY = iconSize
                                translationY = iconTranslationY
                            }
                    ) {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.label,
                            tint = if (isSelected)
                                MaterialTheme.colorScheme.onPrimary
                            else
                                MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    // Only show text for unselected items
                    if (!isSelected) {
                        Text(
                            text = item.label,
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = contentAlpha),
                            style = MaterialTheme.typography.labelSmall,
                            fontSize = 10.sp,
                            maxLines = 1,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(top = 2.dp)
                        )
                    }
                }
            }
        }
    }
}