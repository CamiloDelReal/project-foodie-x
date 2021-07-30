package org.xapps.apps.foodiex.views.custom

import com.google.android.material.shape.EdgeTreatment
import com.google.android.material.shape.ShapePath
import org.xapps.apps.foodiex.core.utils.warning
import kotlin.math.sqrt


class BottomNavigationTopEdgeTreatment(
    private var fabCradleMargin: Float,
    private var fabCradleRoundedCornerRadius: Float,
    private var cradleVerticalOffset: Float,
    private val fabDiameter: Float,
    private val fabPercentPosition: Float
) :
    EdgeTreatment(), Cloneable {

    override fun getEdgePath(
        length: Float, center: Float, interpolation: Float, shapePath: ShapePath
    ) {
        if(cradleVerticalOffset < 0.0) {
            warning<BottomNavigationTopEdgeTreatment>("Cradle vertical offset must be positive")
        }
        if (fabDiameter == 0f) {
            warning<BottomNavigationTopEdgeTreatment>("FAB diameter must greater than zero")
            shapePath.lineTo(length, 0f)
            return
        }
        val cradleDiameter = fabCradleMargin * 2 + fabDiameter
        val cradleRadius = cradleDiameter / 2f
        val roundedCornerOffset = interpolation * fabCradleRoundedCornerRadius
        val customCenter = (length * fabPercentPosition).toFloat()
        val middle = customCenter

        val verticalOffset = interpolation * cradleVerticalOffset + (1 - interpolation) * cradleRadius
        val verticalOffsetRatio = verticalOffset / cradleRadius
        if (verticalOffsetRatio >= 1.0f) {
            warning<BottomNavigationTopEdgeTreatment>("Vertical offset ratio must not be less than 1.0")
            shapePath.lineTo(length, 0f)
            return
        }

        val distanceBetweenCenters = cradleRadius + roundedCornerOffset
        val distanceBetweenCentersSquared = distanceBetweenCenters * distanceBetweenCenters
        val distanceY = verticalOffset + roundedCornerOffset
        val distanceX =
            sqrt((distanceBetweenCentersSquared - distanceY * distanceY).toDouble()).toFloat()

        val leftRoundedCornerCircleX = middle - distanceX
        val rightRoundedCornerCircleX = middle + distanceX

        val cornerRadiusArcLength =
            Math.toDegrees(Math.atan((distanceX / distanceY).toDouble())).toFloat()
        val cutoutArcOffset = ARC_QUARTER - cornerRadiusArcLength

        shapePath.lineTo(leftRoundedCornerCircleX, 0f)

        shapePath.addArc(
            leftRoundedCornerCircleX - roundedCornerOffset, 0f,
            leftRoundedCornerCircleX + roundedCornerOffset,
            roundedCornerOffset * 2,
            ANGLE_UP.toFloat(),
            cornerRadiusArcLength
        )

        shapePath.addArc(
            middle - cradleRadius,
            -cradleRadius - verticalOffset,
            middle + cradleRadius,
            cradleRadius - verticalOffset,
            ANGLE_LEFT - cutoutArcOffset,
            cutoutArcOffset * 2 - ARC_HALF
        )

        shapePath.addArc(
            rightRoundedCornerCircleX - roundedCornerOffset, 0f,
            rightRoundedCornerCircleX + roundedCornerOffset,
            roundedCornerOffset * 2,
            ANGLE_UP - cornerRadiusArcLength,
            cornerRadiusArcLength
        )

        shapePath.lineTo(length, 0f)
    }

    companion object {
        private const val ARC_QUARTER = 90
        private const val ARC_HALF = 180
        private const val ANGLE_UP = 270
        private const val ANGLE_LEFT = 180
    }

}