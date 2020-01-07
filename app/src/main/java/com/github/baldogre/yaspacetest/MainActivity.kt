package com.github.baldogre.yaspacetest

import android.graphics.LinearGradient
import android.graphics.Shader
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.baldogre.yaspacetest.model.Event
import com.google.android.material.chip.Chip
import kotlinx.android.synthetic.main.action_bar_activity_main.*
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.max

class MainActivity : AppCompatActivity() {

    private val checkedDates = mutableListOf<String>()
    private val constHashTags = mutableListOf("date", "girls", "cinema", "party")
    private val eventAdapter = EventAdapter(getEvents())


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar?.setCustomView(R.layout.action_bar_activity_main)

        val textShader = LinearGradient(
            0f,
            0f,
            action_bar_text.paint.measureText(getString(R.string.events)),
            action_bar_text.textSize,
            intArrayOf(
                ContextCompat.getColor(this, R.color.colorAccent),
                ContextCompat.getColor(this, R.color.color_gradient)
            ),
            null,
            Shader.TileMode.CLAMP
        )

        action_bar_text.paint.shader = textShader
        chip_group.isSingleSelection = false

        for (i in 0..5) {
            chip_group.addView(createChip(i))
        }

        event_list.layoutManager = LinearLayoutManager(this)

        event_list.adapter = eventAdapter
    }

    private fun getEvents(): MutableList<Event> {
        val list = mutableListOf<Event>()
        val dateFormatter = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        for (i in 0..10) {
            val date = GregorianCalendar.getInstance()
            date.add(Calendar.DAY_OF_YEAR, (Math.random() * 100).toInt())
            val iAmGoing = (Math.random() * 100).toInt() % 2 == 0
            val maxParticipant = max((Math.random() * 100).toInt(), 10)

            val numberOfParticipant = (Math.random() * 100).toInt() % maxParticipant
            list.add(
                Event(
                    i,
                    "Title #$i",
                    dateFormatter.format(date.time),
                    getHashTags(),
                    iAmGoing,
                    numberOfParticipant,
                    maxParticipant
                )
            )
        }

        return list
    }

    private fun getHashTags(): List<String> {
        val hashTags = mutableListOf<String>()

        hashTags.addAll(constHashTags)

        hashTags.removeAt((Math.random() * 10).toInt() % hashTags.size)

        return hashTags
    }

    private fun createChip(i: Int): Chip {
        val chip = LayoutInflater.from(this).inflate(R.layout.chip_date_range, null) as Chip
        val dateFormatted = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        chip.isCheckable = true
        when (i) {
            0 -> {
                chip.isChecked = true
                chip.text = getString(R.string.today)

                chip.setOnCheckedChangeListener { _, isChecked ->
                    val date = dateFormatted.format(Date())
                    onChipChecked(date, isChecked, chip)
                }
            }
            1 -> {
                chip.text = getString(R.string.tomorrow)

                chip.setOnCheckedChangeListener { _, isChecked ->
                    val calendar = GregorianCalendar()
                    calendar.add(Calendar.DATE, 1)
                    val date = dateFormatted.format(calendar.time)
                    onChipChecked(date, isChecked, chip)
                }
            }
            2 -> {
                chip.text = getString(R.string.on_the_week)

                chip.setOnCheckedChangeListener { _, isChecked ->
                    val calendar = GregorianCalendar.getInstance()
                    val dates = mutableListOf<String>()
                    do {
                        dates.add(dateFormatted.format(calendar.time))
                        calendar.add(Calendar.DAY_OF_WEEK, 1)
                    } while (calendar.getActualMaximum(Calendar.DAY_OF_WEEK) != calendar.get(
                            Calendar.DAY_OF_WEEK
                        )
                    )

                    onChipChecked(dates, isChecked, chip)
                }
            }
            3 -> {
                chip.text = getString(R.string.this_month)

                chip.setOnCheckedChangeListener { _, isChecked ->
                    val calendar = GregorianCalendar.getInstance()
                    val dates = mutableListOf<String>()

                    calendar.add(Calendar.DAY_OF_MONTH, -1)

                    do {
                        calendar.add(Calendar.DAY_OF_MONTH, 1)
                        dates.add(dateFormatted.format(calendar.time))
                    } while (calendar.getActualMaximum(Calendar.DAY_OF_MONTH) != calendar.get(
                            Calendar.DAY_OF_MONTH
                        )
                    )

                    onChipChecked(dates, isChecked, chip)
                }
            }
            4 -> {
                chip.text = getString(R.string.next_month)

                chip.setOnCheckedChangeListener { _, isChecked ->
                    val calendar = GregorianCalendar.getInstance()
                    calendar.add(Calendar.MONTH, 1)
                    calendar.set(Calendar.DAY_OF_MONTH, 1)
                    val dates = mutableListOf<String>()

                    calendar.add(Calendar.DAY_OF_MONTH, -1)

                    do {
                        calendar.add(Calendar.DAY_OF_MONTH, 1)
                        dates.add(dateFormatted.format(calendar.time))
                    } while (calendar.getActualMaximum(Calendar.DAY_OF_MONTH) != calendar.get(
                            Calendar.DAY_OF_MONTH
                        )
                    )

                    onChipChecked(dates, isChecked, chip)
                }
            }
            5 -> {
                chip.text = getString(R.string.this_year)

                chip.setOnCheckedChangeListener { _, isChecked ->
                    val calendar = GregorianCalendar.getInstance()
                    val dates = mutableListOf<String>()

                    calendar.add(Calendar.DAY_OF_YEAR, -1)
                    do {
                        calendar.add(Calendar.DAY_OF_YEAR, 1)
                        dates.add(dateFormatted.format(calendar.time))
                    } while (calendar.getActualMaximum(Calendar.DAY_OF_YEAR) != calendar.get(
                            Calendar.DAY_OF_YEAR
                        )
                    )

                    onChipChecked(dates, isChecked, chip)
                }
            }
        }
        return chip
    }

    private fun onChipChecked(date: String, isChecked: Boolean, chip: Chip) {
        if (isChecked) {
            checkedDates.add(date)
            chip.setChipBackgroundColorResource(R.color.colorAccent)
            chip.setTextColor(ContextCompat.getColor(this, android.R.color.white))
        } else {
            checkedDates.remove(date)
            chip.setChipBackgroundColorResource(android.R.color.white)
            chip.setTextColor(ContextCompat.getColor(this, android.R.color.darker_gray))
        }

        val builder = StringBuilder()

        for (item in checkedDates) {
            builder.append(item).append(";")
        }

        eventAdapter.filter.filter(builder.toString())
    }

    private fun onChipChecked(dates: List<String>, isChecked: Boolean, chip: Chip) {
        if (isChecked) {
            checkedDates.addAll(dates)
            chip.setChipBackgroundColorResource(R.color.colorAccent)
            chip.setTextColor(ContextCompat.getColor(this, android.R.color.white))
        } else {
            checkedDates.removeAll(dates)
            chip.setChipBackgroundColorResource(android.R.color.white)
            chip.setTextColor(ContextCompat.getColor(this, android.R.color.darker_gray))
        }

        val builder = StringBuilder()

        for (item in checkedDates) {
            builder.append(item).append(";")
        }

        eventAdapter.filter.filter(builder.toString())
    }
}
