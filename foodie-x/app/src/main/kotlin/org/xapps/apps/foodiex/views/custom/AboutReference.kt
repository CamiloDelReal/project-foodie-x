package org.xapps.apps.foodiex.views.custom

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import org.xapps.apps.foodiex.R
import org.xapps.apps.foodiex.databinding.ContentAboutReferenceBinding


class AboutReference @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0,
    defStyleRes: Int = 0
) : LinearLayout(context, attrs, defStyle, defStyleRes) {

    private var bindings: ContentAboutReferenceBinding

    init {
        val a: TypedArray = getContext().obtainStyledAttributes(attrs, R.styleable.AboutReference)
        val icon = a.getResourceId(R.styleable.AboutReference_ar_icon, -1)
        val text = a.getString(R.styleable.AboutReference_ar_text)
        val link = a.getString(R.styleable.AboutReference_ar_link)
        a.recycle()

        val layoutInflater = LayoutInflater.from(context)
        bindings = ContentAboutReferenceBinding.inflate(layoutInflater, this, true)

        if (icon != -1) {
            bindings.imgIcon.setImageResource(icon)
        }

        bindings.tvText.text = text
        bindings.tvLink.text = link
        bindings.imgActionable.visibility = View.GONE
    }

    fun setText(text: String) {
        bindings.tvText.text = text
    }

    fun setLink(link: String) {
        bindings.tvLink.text = link
    }

    override fun setOnClickListener(newListener: OnClickListener?) {
        bindings.imgActionable.visibility = View.VISIBLE
        bindings.mcvRoot.setOnClickListener(newListener)
    }

}