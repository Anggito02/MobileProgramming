package com.example.ppbproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView

class WorkExpActivity : AppCompatActivity() , OnClickListener {
    // Navigation Button
    private lateinit var Btn_NavButtonBack : ImageButton
    private lateinit var Btn_NavButtonNext : ImageButton
    private var state: Int = 0

    // Exp Content Getter Setter
    private var expInfos = HashMap<String, String>()
    private var techLogos = HashMap<Int, MutableList<HashMap<String, Int>>>()    // state => arr(map(logo_name, logo_src))
    private var expCount : Int = 0
    private val fields = R.string::class.java.fields

    // Text Views
    private lateinit var Tv_ExpCompany : TextView
    private lateinit var Tv_ExpPosition : TextView
    private lateinit var Tv_ExpTimeframe : TextView
    private lateinit var Tv_ExpDesc : TextView
    private lateinit var Tv_ExpJobdesc : TextView

    // Tech Stack Logos
    private lateinit var LL_TechStackLayout : LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_work_exp)

        Btn_NavButtonBack = findViewById<ImageButton>(R.id.nav_button_back)
        Btn_NavButtonNext = findViewById<ImageButton>(R.id.nav_button_next)

        Tv_ExpCompany = findViewById<TextView>(R.id.exp_company)
        Tv_ExpTimeframe = findViewById<TextView>(R.id.exp_timeframe)
        Tv_ExpPosition = findViewById<TextView>(R.id.exp_position)
        Tv_ExpDesc = findViewById<TextView>(R.id.exp_desc)
        Tv_ExpJobdesc = findViewById<TextView>(R.id.exp_job_desc)

        LL_TechStackLayout = findViewById<LinearLayout>(R.id.exp_tech_stack_layout)

        // Get experiences fields
        for (field in fields) {
            val name : String = field.name
            val content : String = getString(field.getInt(null))
            if (name.contains("exp_")) {
                expInfos[name] = content

                val count : Int = name.split('_').last().toInt()
                expCount = if (expCount < count) count else expCount
            }

            if (name.contains("tech_icon")) {
                val nameFromatted : List<String> = name.split("_")
                val techIdx = nameFromatted.last().toInt()
                val techName = nameFromatted[nameFromatted.size - 2]

                val innerMap : HashMap<String, Int> = HashMap()
                innerMap[content] = resources.getIdentifier(techName, "drawable", "com.example.ppbproject")

                if (techLogos.containsKey(techIdx)) {
                    techLogos[techIdx]!!.add(innerMap)
                }
                else {
                    val tempList : MutableList<HashMap<String, Int>> = mutableListOf()
                    tempList.add(innerMap)
                    techLogos[techIdx] = tempList
                }
            }
        }

        Btn_NavButtonBack.setOnClickListener(this)
        Btn_NavButtonNext.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            // Back Button
            R.id.nav_button_back -> {
                state--
                Btn_NavButtonNext.isEnabled = true

                if (state < 0) {
                    _openHomeActivity()
                    finish()
                }
                else {
                    _updateContent(state)
                    _updateTechLogo(state)
                }
            }

            // Next Button
            R.id.nav_button_next -> {
                println(techLogos)
                state++
                if (state + 1 > expCount) {
                    Btn_NavButtonNext.isEnabled = false
                }
                _updateContent(state)
                _updateTechLogo(state)
            }
        }
    }

    // Open Home
    private fun _openHomeActivity() {
        val activityIntent : Intent = Intent(this, MainActivity::class.java)
        startActivity(activityIntent)
    }

    // Update content based on state
    private fun _updateContent(state: Int) {
        val expContents = HashMap<String, String>()
        for (expInfo in expInfos) {
            if (expInfo.key.contains(state.toString())) expContents[expInfo.key] = expInfo.value
        }

        Tv_ExpCompany.text = expContents["exp_company_$state"]
        Tv_ExpTimeframe.text = expContents["exp_timeframe_$state"]
        Tv_ExpPosition.text = expContents["exp_position_$state"]
        Tv_ExpDesc.text = expContents["exp_desc_$state"]
        Tv_ExpJobdesc.text = expContents["exp_desc_jobdesc_$state"]
    }

    private fun _updateTechLogo(state: Int) {
        LL_TechStackLayout.removeAllViews()
        var counter : Int = 0
        var linearLayout : LinearLayout? = null


        for (techIdx in 0..< techLogos[state]!!.size) {
            if (counter == 3) {
                counter = 0
                linearLayout = null
            }

            if (counter == 0) {
                linearLayout = LinearLayout(this)
                linearLayout.orientation = LinearLayout.HORIZONTAL

                val layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    1.0f
                )

                linearLayout.layoutParams = layoutParams
                LL_TechStackLayout.addView(linearLayout)
            }

            val imageView : ImageView = ImageView(this)
            imageView.setImageResource(techLogos[state]!![techIdx].values.toIntArray()[0])
            imageView.contentDescription = techLogos[state]?.get(techIdx)?.keys?.toString()

            val layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.FILL_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1.0f
            )

            imageView.layoutParams = layoutParams
            linearLayout?.addView(imageView)

            counter++
        }
    }
}