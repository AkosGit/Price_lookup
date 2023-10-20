package com.uni.project.pricelookup.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp


public class CouponShapeRightSide : Shape {
    override fun createOutline(
        size: androidx.compose.ui.geometry.Size,
        layoutDirection: androidx.compose.ui.unit.LayoutDirection,
        density: Density
    ): Outline {
        return Outline.Generic(
            path = drawCouponRightSide(size = size)
        )
    }
}public class CouponShapeLeftSide : Shape {
    override fun createOutline(
        size: androidx.compose.ui.geometry.Size,
        layoutDirection: androidx.compose.ui.unit.LayoutDirection,
        density: Density
    ): Outline {
        return Outline.Generic(
            path = drawCouponLeftSide(size = size)
        )
    }
}
private fun drawCouponRightSide(size: androidx.compose.ui.geometry.Size): Path {
    return Path().apply {
        val cornerRadius = 18f
        val perforatedRadious = 12f
        // Top left arc
        arcTo(
            rect = Rect(
                left = -cornerRadius,
                top = -cornerRadius,
                right = cornerRadius,
                bottom = cornerRadius
            ),
            startAngleDegrees = 90.0f,
            sweepAngleDegrees = -90.0f,
            forceMoveTo = false
        )
        lineTo(x = size.width - cornerRadius, y = 0f)
        // Top right arc
        arcTo(
            rect = Rect(
                left = size.width - cornerRadius,
                top = -cornerRadius,
                right = size.width + cornerRadius,
                bottom = cornerRadius
            ),
            startAngleDegrees = 180.0f,
            sweepAngleDegrees = -90.0f,
            forceMoveTo = false
        )
        lineTo(x = size.width, y = size.height - cornerRadius)
        // Bottom right arc
        arcTo(
            rect = Rect(
                left = size.width - cornerRadius,
                top = size.height - cornerRadius,
                right = size.width + cornerRadius,
                bottom = size.height + cornerRadius
            ),
            startAngleDegrees = 270.0f,
            sweepAngleDegrees = -90.0f,
            forceMoveTo = false
        )
        lineTo(x = cornerRadius, y = size.height)
        // Bottom left arc
        arcTo(
            rect = Rect(
                left = -cornerRadius,
                top = size.height - cornerRadius,
                right = cornerRadius,
                bottom = size.height + cornerRadius
            ),
            startAngleDegrees = 0.0f,
            sweepAngleDegrees = -90.0f,
            forceMoveTo = false
        )
        lineTo(x = 0f, y = cornerRadius)

        //LEFT SIDE
        arcTo(
            Rect(
                left = -perforatedRadious,
                top = size.height/4 - perforatedRadious,
                right = perforatedRadious,
                bottom = size.height/4 + perforatedRadious
            ),
            startAngleDegrees = 90f,
            sweepAngleDegrees = -180f,
            forceMoveTo = false
        )

        arcTo(
            Rect(
                left = -perforatedRadious,
                top = size.height/2 - perforatedRadious,
                right = perforatedRadious,
                bottom = size.height/2 + perforatedRadious
            ),
            startAngleDegrees = 90f,
            sweepAngleDegrees = -180f,
            forceMoveTo = false
        )

        arcTo(
            Rect(
                left = -perforatedRadious,
                top = size.height*0.75f - perforatedRadious,
                right = perforatedRadious,
                bottom = size.height*0.75f + perforatedRadious
            ),
            startAngleDegrees = 90f,
            sweepAngleDegrees = -180f,
            forceMoveTo = false
        )

        close()
    }
}

private fun drawCouponLeftSide(size: androidx.compose.ui.geometry.Size): Path {
    return Path().apply {
        val cornerRadius = 18f
        val perforatedRadious = 12f

        // Top left arc
        arcTo(
            rect = Rect(
                left = -cornerRadius,
                top = -cornerRadius,
                right = cornerRadius,
                bottom = cornerRadius
            ),
            startAngleDegrees = 90.0f,
            sweepAngleDegrees = -90.0f,
            forceMoveTo = false
        )
        lineTo(x = size.width - cornerRadius, y = 0f)
        // Top right arc
        arcTo(
            rect = Rect(
                left = size.width - cornerRadius,
                top = -cornerRadius,
                right = size.width + cornerRadius,
                bottom = cornerRadius
            ),
            startAngleDegrees = 180.0f,
            sweepAngleDegrees = -90.0f,
            forceMoveTo = false
        )
        lineTo(x = size.width, y = size.height - cornerRadius)

        //RIGHT SIDE
        arcTo(
            Rect(
                left = size.width - perforatedRadious,
                top = size.height*0.75f - perforatedRadious,
                right = size.width + perforatedRadious,
                bottom = size.height*0.75f + perforatedRadious
            ),
            startAngleDegrees = 270f,
            sweepAngleDegrees = -180f,
            forceMoveTo = false
        )
        arcTo(
            Rect(
                left = size.width - perforatedRadious,
                top = size.height/2 - perforatedRadious,
                right = size.width + perforatedRadious,
                bottom = size.height/2 + perforatedRadious
            ),
            startAngleDegrees = 270f,
            sweepAngleDegrees = -180f,
            forceMoveTo = false
        )
        arcTo(
            Rect(
                left = size.width - perforatedRadious,
                top = size.height/4 - perforatedRadious,
                right = size.width + perforatedRadious,
                bottom = size.height/4 + perforatedRadious
            ),
            startAngleDegrees = 270f,
            sweepAngleDegrees = -180f,
            forceMoveTo = false
        )

        // Bottom right arc
        arcTo(
            rect = Rect(
                left = size.width - cornerRadius,
                top = size.height - cornerRadius,
                right = size.width + cornerRadius,
                bottom = size.height + cornerRadius
            ),
            startAngleDegrees = 270.0f,
            sweepAngleDegrees = -90.0f,
            forceMoveTo = false
        )
        lineTo(x = cornerRadius, y = size.height)
        // Bottom left arc
        arcTo(
            rect = Rect(
                left = -cornerRadius,
                top = size.height - cornerRadius,
                right = cornerRadius,
                bottom = size.height + cornerRadius
            ),
            startAngleDegrees = 0.0f,
            sweepAngleDegrees = -90.0f,
            forceMoveTo = false
        )
        lineTo(x = 0f, y = cornerRadius)
        close()


    }
}
@Preview
@Composable
fun CouponCard(){
    Box(
        modifier = Modifier.background(Color.Black)
    ){
        Box(modifier = Modifier.padding(10.dp)){
            Row {
                Box(
                    modifier = Modifier
                        .size(150.dp, 90.dp)
                        .clip(shape = CouponShapeLeftSide())
                        .background(Color.White)
                        .shadow(elevation = 4.dp, shape = CouponShapeLeftSide())
                )
                Box(
                    modifier = Modifier
                        .size(90.dp, 90.dp)
                        .clip(shape = CouponShapeRightSide())
                        .background(Color.White)
                        .shadow(elevation = 4.dp, shape = CouponShapeLeftSide())
                )
            }
        }
    }
}