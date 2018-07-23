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
            bConn.sendData("R-100-0")
            Thread.sleep(500)
            bConn.sendData("L-100-0")
        }


    }

    override fun onStop() {
        bConn.sendData("STOP")
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
