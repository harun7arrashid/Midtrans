package com.kita.store

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.kita.store.payment.PaymentActivity
import com.kita.store.payment.PaymentsMidtrans
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_detail.*

class ActivityDetail : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        name.text = GlobalData.names
        Picasso.get().load(GlobalData.photos).into(image)
        harga.text = "$ " + GlobalData.hargas.toString()
        deskripsi.text = GlobalData.deskripsis

        pesan.setOnClickListener {
            var i = Intent(applicationContext, PaymentActivity::class.java)
            startActivity(i)
        }
    }
}