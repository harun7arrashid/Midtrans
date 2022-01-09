package com.kita.store.payment

import android.os.PersistableBundle
import com.kita.store.GlobalData
import com.midtrans.sdk.corekit.callback.TransactionFinishedCallback
import com.midtrans.sdk.corekit.core.MidtransSDK
import com.midtrans.sdk.corekit.core.TransactionRequest
import com.midtrans.sdk.corekit.core.themes.CustomColorTheme
import com.midtrans.sdk.corekit.models.BillingAddress
import com.midtrans.sdk.corekit.models.CustomerDetails
import com.midtrans.sdk.corekit.models.ItemDetails
import com.midtrans.sdk.corekit.models.ShippingAddress
import com.midtrans.sdk.uikit.SdkUIFlowBuilder
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_pesan.*
import com.kita.store.R
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class PaymentsMidtrans: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setContentView(R.layout.activity_pesan)
        name.text = GlobalData.names
        harga.text = "$ " + GlobalData.hargas.toString()
        deskripsi.text = GlobalData.deskripsis
        Picasso.get().load(GlobalData.photos).into(image)

        // Inisialisasi
        SdkUIFlowBuilder.init()
            .setClientKey("SB-Mid-client-NOrfEqgt1GpzUBJF") // ClientKey Midtrans
            .setContext(applicationContext)
            // jika transaksi nya finisih, dapatkan Callbacknya disini
            .setTransactionFinishedCallback(TransactionFinishedCallback {
                //TransactionResult class buat liat key statusnya apa aja

            }) 
            .setMerchantBaseUrl("https://store.pangperhebat.my.id/ppresponse.php/")
            .enableLog(true)
            .setColorTheme(CustomColorTheme("#FFE51255", "#B61548", "#FFE51255"))
            .setLanguage("id") // indonesia
            .buildSDK()

        pesan.setOnClickListener {
            val hargaproduct = GlobalData.hargas
            val edittextHarga = jumlah.text.toString()
            val catatan = catatan.text.toString()
            GlobalData.jumlah = edittextHarga.toInt()
            GlobalData.catatan = catatan.toString()
            val convertharga = edittextHarga.toInt() // QTY atau jumlah barang
            var kalikan = convertharga * hargaproduct.toDouble()

            // jadi ID nya make waktu saat ini, biar bisa beda walau beda 1detik
            val transactionRequest = TransactionRequest("ID-${System.currentTimeMillis().toShort()}", kalikan)
            val detail = ItemDetails("NamaItemId", GlobalData.hargas.toDouble(), convertharga, "Ini Nama Produk")
            // dimasukan kedalam Array
            val itemDetails = ArrayList<ItemDetails>()
            itemDetails.add(detail)
            uiKitDetails(transactionRequest, "Harun AR-Rashid, S.Kom")
            transactionRequest.itemDetails = itemDetails

            // Final
            MidtransSDK.getInstance().transactionRequest = transactionRequest
            MidtransSDK.getInstance().startPaymentUiFlow(this)

        }
    }

    // biar Customer Detailnya dinamis
    fun uiKitDetails(transactionRequest: TransactionRequest, name: String) {
        val customerDetails = CustomerDetails()
        customerDetails.customerIdentifier = name
        customerDetails.phone = "085770859752"
        customerDetails.firstName = "Harun"
        customerDetails.lastName  = "Arrashid"
        customerDetails.email = "harun7arrashid@gmail.com"

        val shippingAddress = ShippingAddress() // alamat pengiriman
        shippingAddress.address = "Perum Pangulah Permai"
        shippingAddress.city = "Karawang"
        shippingAddress.postalCode = "41374"
        customerDetails.shippingAddress = shippingAddress

        val billingAddress = BillingAddress()
        billingAddress.address = "Pangulah Permai"
        billingAddress.city = "Karawang"
        billingAddress.postalCode = "41374"
        customerDetails.billingAddress = billingAddress

        transactionRequest.customerDetails = customerDetails
    }
}