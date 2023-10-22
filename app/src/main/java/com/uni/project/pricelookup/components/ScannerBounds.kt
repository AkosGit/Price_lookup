package scannerboundpath

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.PathFillType.Companion.NonZero
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeCap.Companion.Butt
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.StrokeJoin.Companion.Miter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.ImageVector.Builder
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

public val ScannerBounds: ImageVector
    get() {
        if (_group1 != null) {
            return _group1!!
        }
        _group1 = Builder(name = "Group1", defaultWidth = 500.0.dp, defaultHeight = 350.0.dp,
            viewportWidth = 500.0f, viewportHeight = 350.0f).apply {
            path(fill = SolidColor(Color.White), stroke = null, strokeLineWidth = 0.0f,
                strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                pathFillType = NonZero) {
                moveTo(124.0f, 85.0f)
                curveTo(124.0f, 82.239f, 126.239f, 80.0f, 129.0f, 80.0f)
                horizontalLineTo(167.443f)
                curveTo(168.303f, 80.0f, 169.0f, 80.697f, 169.0f, 81.557f)
                verticalLineTo(81.557f)
                curveTo(169.0f, 82.417f, 168.303f, 83.115f, 167.443f, 83.115f)
                horizontalLineTo(132.047f)
                curveTo(129.285f, 83.115f, 127.047f, 85.353f, 127.047f, 88.115f)
                verticalLineTo(124.477f)
                curveTo(127.047f, 125.318f, 126.365f, 126.0f, 125.523f, 126.0f)
                verticalLineTo(126.0f)
                curveTo(124.682f, 126.0f, 124.0f, 125.318f, 124.0f, 124.477f)
                verticalLineTo(85.0f)
                close()
            }
            path(fill = SolidColor(Color.White), stroke = null, strokeLineWidth = 0.0f,
                strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                pathFillType = NonZero) {
                moveTo(359.5f, 80.5f)
                curveTo(362.261f, 80.5f, 364.5f, 82.739f, 364.5f, 85.5f)
                lineTo(364.5f, 123.943f)
                curveTo(364.5f, 124.803f, 363.803f, 125.5f, 362.943f, 125.5f)
                verticalLineTo(125.5f)
                curveTo(362.083f, 125.5f, 361.385f, 124.803f, 361.385f, 123.943f)
                lineTo(361.385f, 88.547f)
                curveTo(361.385f, 85.785f, 359.147f, 83.547f, 356.385f, 83.547f)
                lineTo(320.023f, 83.547f)
                curveTo(319.182f, 83.547f, 318.5f, 82.865f, 318.5f, 82.023f)
                verticalLineTo(82.023f)
                curveTo(318.5f, 81.182f, 319.182f, 80.5f, 320.023f, 80.5f)
                lineTo(359.5f, 80.5f)
                close()
            }
            path(fill = SolidColor(Color.White), stroke = null, strokeLineWidth = 0.0f,
                strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                pathFillType = NonZero) {
                moveTo(363.5f, 232.5f)
                curveTo(363.5f, 235.261f, 361.261f, 237.5f, 358.5f, 237.5f)
                lineTo(320.057f, 237.5f)
                curveTo(319.197f, 237.5f, 318.5f, 236.803f, 318.5f, 235.943f)
                verticalLineTo(235.943f)
                curveTo(318.5f, 235.083f, 319.197f, 234.385f, 320.057f, 234.385f)
                lineTo(355.453f, 234.385f)
                curveTo(358.215f, 234.385f, 360.453f, 232.147f, 360.453f, 229.385f)
                lineTo(360.453f, 193.023f)
                curveTo(360.453f, 192.182f, 361.135f, 191.5f, 361.977f, 191.5f)
                verticalLineTo(191.5f)
                curveTo(362.818f, 191.5f, 363.5f, 192.182f, 363.5f, 193.023f)
                lineTo(363.5f, 232.5f)
                close()
            }
            path(fill = SolidColor(Color.White), stroke = null, strokeLineWidth = 0.0f,
                strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                pathFillType = NonZero) {
                moveTo(128.0f, 237.0f)
                curveTo(125.239f, 237.0f, 123.0f, 234.761f, 123.0f, 232.0f)
                lineTo(123.0f, 193.557f)
                curveTo(123.0f, 192.697f, 123.697f, 192.0f, 124.557f, 192.0f)
                verticalLineTo(192.0f)
                curveTo(125.417f, 192.0f, 126.115f, 192.697f, 126.115f, 193.557f)
                lineTo(126.115f, 228.953f)
                curveTo(126.115f, 231.715f, 128.353f, 233.953f, 131.115f, 233.953f)
                lineTo(167.477f, 233.953f)
                curveTo(168.318f, 233.953f, 169.0f, 234.635f, 169.0f, 235.477f)
                verticalLineTo(235.477f)
                curveTo(169.0f, 236.318f, 168.318f, 237.0f, 167.477f, 237.0f)
                lineTo(128.0f, 237.0f)
                close()
            }
        }
            .build()
        return _group1!!
    }

private var _group1: ImageVector? = null
