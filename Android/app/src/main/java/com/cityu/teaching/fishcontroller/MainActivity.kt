package com.cityu.teaching.fishcontroller

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.cityu.teaching.fishcontroller.models.BLEConnection
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    val MAC_ADDRESS : String = "E0:E5:CF:24:5D:B9"
    private lateinit var bConn : BLEConnection
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bConn = BLEConnection()
        getConnection()

        btnTest.setOnClickListener {
            if(!bConn.canSendData())
                return@setOnClickListener

            val b = ByteArray(2)
            /*val i = Integer.parseInt("01111110", 2)
            val btmp = i.toByte()
            val i2 = btmp.toInt()*/
            b[0] = Integer.parseInt("00000000", 2).toByte();
            b[1] = Integer.parseInt("00001111", 2).toByte();
            bConn.sendData(b)
        }


        btnStop.setOnClickListener {
            if(!bConn.canSendData())
                return@setOnClickListener

            val b = ByteArray(2)
            b[0] = Integer.parseInt("00000000", 2).toByte();
            b[1] = Integer.parseInt("00000000", 2).toByte();
            bConn.sendData(b)
        }

    }

    override fun onStop() {
        val b = ByteArray(2)
        b[0] = Integer.parseInt("00000000", 2).toByte();
        b[1] = Integer.parseInt("00000000", 2).toByte();
        bConn.sendData(b)
        bConn.disconnect()
        super.onStop()
    }

    private fun getConnection() {
        var isConnStart = bConn.connect(this,MAC_ADDRESS, object : BLEConnection.ConnectionChangedListener{
            override fun onConnectionChanged(connected: Boolean) {
                runOnUiThread {
                    if(connected)
                        lblInfo.setText("Connected")
                    else
                        lblInfo.setText("Not connected")
                }
            }
        })
    }
}
