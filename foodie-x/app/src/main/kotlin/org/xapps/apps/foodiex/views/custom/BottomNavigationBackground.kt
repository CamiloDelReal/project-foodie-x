package org.xapps.apps.foodiex.views.custom

import android.content.Context
import android.content.res.ColorStateList
import android.content.res.TypedArray
import android.util.AttributeSet
import android.util.TypedValue
import com.google.android.material.card.MaterialCardView
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel
import com.skydoves.whatif.whatIf
import org.xapps.apps.foodiex.R
import org.xapps.apps.foodiex.core.utils.debug
import kotlin.math.abs


class BottomNavigationBackground @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : MaterialCardView(context, attrs, defStyle) {

    init {
        val a: TypedArray = getContext().obtainStyledAttributes(attrs, R.styleable.BottomNavigationBackground)
        val fabCradleMargin = a.getDimension(R.styleable.BottomNavigationBackground_bnb_fab_cradle_margin, 0f)
        val fabCradleRoundedCornerRadius = a.getDimension(R.styleable.BottomNavigationBackground_bnb_fab_cradle_rounded_corner_radius, 0f)
        val cradleVerticalOffset = a.getDimension(R.styleable.BottomNavigationBackground_bnb_cradle_vertical_offset, 0f)
        val fabDiameter = a.getDimension(R.styleable.BottomNavigationBackground_bnb_fab_diameter, 0f)
        val fabPercentPosition = a.getFloat(R.styleable.BottomNavigationBackground_bnb_fab_percent_position, 0.5f)
        val elevation = a.getDimension(R.styleable.BottomNavigationBackground_bnb_elevation, 0f)
        val cornerRadius = a.getDimension(R.styleable.BottomNavigationBackground_bnb_corner_radius, 0f)
        val theme = context.theme
        val typedValue = TypedValue()
        theme.resolveAttribute(R.attr.colorPrimary, typedValue, true)
        val colorPrimary = typedValue.data
        val backgroundColor = a.getColor(R.styleable.BottomNavigationBackground_bnb_background_color, colorPrimary)
        a.recycle()

        debug<BottomNavigationBackground>("FabCradleMargin = $fabCradleMargin")
        debug<BottomNavigationBackground>("fabCradleRoundedCornerRadius = $fabCradleRoundedCornerRadius")
        debug<BottomNavigationBackground>("cradleVerticalOffset = $cradleVerticalOffset")
        debug<BottomNavigationBackground>("fabDiameter = $fabDiameter")
        debug<BottomNavigationBackground>("fabPercentPosition = $fabPercentPosition")

        val edge = BottomNavigationTopEdgeTreatment(
            fabCradleMargin = fabCradleMargin,
            fabCradleRoundedCornerRadius = fabCradleRoundedCornerRadius,
            cradleVerticalOffset = cradleVerticalOffset,
            fabDiameter = fabDiameter,
            fabPercentPosition = fabPercentPosition
        )
        val appearanceModel = ShapeAppearanceModel.builder()
            .setTopEdge(edge)
            .whatIf(
                given = (cornerRadius > 0),
                whatIf = {
                    setAllCorners(CornerFamily.ROUNDED, cornerRadius)
                },
                whatIfNot = {
                    setAllCorners(CornerFamily.CUT, 0f)
                }
            )
            .build()

        shapeAppearanceModel = appearanceModel
        backgroundTintList = ColorStateList.valueOf(backgroundColor)

        val backgroundDrawable = MaterialShapeDrawable(appearanceModel)
        backgroundDrawable.fillColor = ColorStateList.valueOf(backgroundColor)

        radius = abs(cornerRadius)
        cardElevation = abs(elevation)
        background = backgroundDrawable
    }

}