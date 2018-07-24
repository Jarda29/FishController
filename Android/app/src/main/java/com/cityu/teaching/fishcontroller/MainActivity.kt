package com.cityu.teaching.fishcontroller

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.SeekBar
import com.cityu.teaching.fishcontroller.models.BLEConnection
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    val MAC_ADDRESS : String = "E0:E5:CF:24:5D:B9"
    private lateinit var bConn : BLEConnection

    val midlePower = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        seekPitch.setProgress(0);
        seekPitch.incrementProgressBy(1);
        seekPitch.setMax(7);

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

        seekPitch.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {

            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                // Display the current progress of SeekBar
                println("Progress : $i")
                sendData()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
            }
        })

    }
    private fun sendData(){
        if(!bConn.canSendData())
            return

        val b = ByteArray(2)
        b[0] = Integer.parseInt("00000000", 2).toByte();

        b[1] = Integer.parseInt("00000"+valToBits(seekPitch.progress), 2).toByte();
        bConn.sendData(b)
    }
    private fun valToBits(arg: Int) : String {
        if(arg==1)
            return "001";
        if(arg==2)
            return "010";
        if(arg==3)
            return "011";
        if(arg==4)
            return "100";
        if(arg==5)
            return "101";
        if(arg==6)
            return "110";
        if(arg==7)
            return "111";
        return "000"
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
