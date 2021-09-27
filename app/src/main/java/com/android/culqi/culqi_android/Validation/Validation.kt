package com.android.culqi.culqi_android.Validation

import android.widget.TextView
import java.util.*

/**
 * Created by culqi on 1/19/17.
 */
class Validation {
    fun bin(bin: String, kind_card: TextView): Int {
        if (bin.length > 1) {
            if (Integer.valueOf(bin.substring(0, 2)) == 41) {
                kind_card.text = "VISA"
                return 3
            } else if (Integer.valueOf(bin.substring(0, 2)) == 51) {
                kind_card.text = "MasterCard"
                return 3
            } else {
            }
        } else {
            kind_card.text = ""
        }
        if (bin.length > 1) {
            if (Integer.valueOf(bin.substring(0, 2)) == 36) {
                kind_card.text = "Diners Club"
                return 3
            } else if (Integer.valueOf(bin.substring(0, 2)) == 38) {
                kind_card.text = "Diners Club"
                return 3
            } else if (Integer.valueOf(bin.substring(0, 2)) == 37) {
                kind_card.text = "AMEX"
                return 3
            } else {
            }
        }
        if (bin.length > 2) {
            if (Integer.valueOf(bin.substring(0, 3)) == 300) {
                kind_card.text = "Diners Club"
                return 3
            } else if (Integer.valueOf(bin.substring(0, 3)) == 305) {
                kind_card.text = "Diners Club"
                return 3
            } else {
            }
        }
        return 0
    }

    fun month(month: String): Boolean {
        if (month != "") {
            if (Integer.valueOf("" + month) > 12) {
                return true
            }
        }
        return false
    }

    fun year(year: String): Boolean {
        val today = Date()
        val calendar = Calendar.getInstance()
        calendar.time = today
        if (year != "") {
            if (Integer.valueOf("20$year") < calendar[Calendar.YEAR]) {
                return true
            }
        }
        return false
    }

    companion object {
        fun luhn(number: String?): Boolean {
            var s1 = 0
            var s2 = 0
            val reverse = StringBuffer(number!!).reverse().toString()
            for (i in 0 until reverse.length) {
                val digit = Character.digit(reverse[i], 10)
                if (i % 2 == 0) { //this is for odd digits, they are 1-indexed in the algorithm
                    s1 += digit
                } else { //add 2 * digit for 0-4, add 2 * digit - 9 for 5-9
                    s2 += 2 * digit
                    if (digit >= 5) {
                        s2 -= 9
                    }
                }
            }
            return (s1 + s2) % 10 == 0
        }
    }
}