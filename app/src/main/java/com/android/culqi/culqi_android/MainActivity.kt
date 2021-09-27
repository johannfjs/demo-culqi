package com.android.culqi.culqi_android

import androidx.appcompat.app.AppCompatActivity
import com.android.culqi.culqi_android.Validation.Validation
import android.app.ProgressDialog
import android.widget.TextView
import android.os.Bundle
import com.android.culqi.culqi_android.R
import android.text.TextWatcher
import android.text.Editable
import android.text.InputFilter
import android.text.InputFilter.LengthFilter
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.android.culqi.culqi_android.Culqi.Card
import com.android.culqi.culqi_android.Culqi.Token
import com.android.culqi.culqi_android.Culqi.TokenCallback
import org.json.JSONObject
import java.lang.Exception

class MainActivity : AppCompatActivity() {
    var validation: Validation? = null
    var progress: ProgressDialog? = null
    var txtcardnumber: TextView? = null
    var txtcvv: TextView? = null
    var txtmonth: TextView? = null
    var txtyear: TextView? = null
    var txtemail: TextView? = null
    var kind_card: TextView? = null
    var result: TextView? = null
    var btnPay: Button? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        validation = Validation()
        progress = ProgressDialog(this)
        progress!!.setMessage("Validando informacion de la tarjeta")
        progress!!.setCancelable(false)
        progress!!.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        txtcardnumber = findViewById<View>(R.id.txt_cardnumber) as TextView
        txtcvv = findViewById<View>(R.id.txt_cvv) as TextView
        txtmonth = findViewById<View>(R.id.txt_month) as TextView
        txtyear = findViewById<View>(R.id.txt_year) as TextView
        txtemail = findViewById<View>(R.id.txt_email) as TextView
        kind_card = findViewById<View>(R.id.kind_card) as TextView
        result = findViewById<View>(R.id.token_id) as TextView
        btnPay = findViewById<View>(R.id.btn_pay) as Button
        txtcvv!!.isEnabled = false
        txtcardnumber!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.length == 0) {
                    txtcvv!!.isEnabled = true
                }
            }

            override fun afterTextChanged(s: Editable) {
                val text = txtcardnumber!!.text.toString()
                if (s.length == 0) {
                    txtcardnumber!!.setBackgroundResource(R.drawable.border_error)
                }
                if (Validation.luhn(text)) {
                    txtcardnumber!!.setBackgroundResource(R.drawable.border_sucess)
                } else {
                    txtcardnumber!!.setBackgroundResource(R.drawable.border_error)
                }
                val cvv = validation!!.bin(text, kind_card!!)
                if (cvv > 0) {
                    txtcvv!!.filters = arrayOf<InputFilter>(LengthFilter(cvv))
                    txtcvv!!.isEnabled = true
                } else {
                    txtcvv!!.isEnabled = false
                    txtcvv!!.text = ""
                }
            }
        })
        txtyear!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                val text = txtyear!!.text.toString()
                if (validation!!.year(text)) {
                    txtyear!!.setBackgroundResource(R.drawable.border_error)
                } else {
                    txtyear!!.setBackgroundResource(R.drawable.border_sucess)
                }
            }
        })
        txtmonth!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                val text = txtmonth!!.text.toString()
                if (validation!!.month(text)) {
                    txtmonth!!.setBackgroundResource(R.drawable.border_error)
                } else {
                    txtmonth!!.setBackgroundResource(R.drawable.border_sucess)
                }
            }
        })
        btnPay!!.setOnClickListener {
            progress!!.show()
            val card = Card(
                txtcardnumber!!.text.toString(),
                txtcvv!!.text.toString(),
                txtmonth!!.text.toString().toInt(),
                ("20" + txtyear!!.text.toString()).toInt(),
                txtemail!!.text.toString()
            )
            val token = Token("pk_test_cbc6c27964d9d3ac")
            token.createToken(applicationContext, card, object : TokenCallback {
                override fun onSuccess(token: JSONObject) {
                    try {
                        result!!.text = token["id"].toString()
                        Toast.makeText(this@MainActivity,"Token: ${token["id"]}",Toast.LENGTH_SHORT).show()
                    } catch (ex: Exception) {
                        Toast.makeText(this@MainActivity,"Ocurrio una excepcion",Toast.LENGTH_SHORT).show()
                        progress!!.hide()
                    }
                    progress!!.hide()
                }

                override fun onError(error: Exception) {
                    Toast.makeText(this@MainActivity,"Ocurrio un error",Toast.LENGTH_SHORT).show()
                    progress!!.hide()
                }
            })
        }
    }
}