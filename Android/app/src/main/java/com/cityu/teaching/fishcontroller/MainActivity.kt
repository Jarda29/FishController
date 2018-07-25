package com.cityu.teaching.fishcontroller

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.SeekBar
import com.cityu.teaching.fishcontroller.models.BLEConnection
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    val MAC_ADDRESS : String = "E0:E5:CF:24:5D:B9"
    private lateinit var bConn : BLEConnection
    lateinit var dataSent: ByteArray
    var leftMotorPow = 0
    var rightMotorPow = 0
    var midleMotorPow = 0
    var leftMotorDir = 0
    var rightMotorDir = 0
    var midleMotorDir = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        seekPitch.incrementProgressBy(1);
        seekPitch.setMax(7);
        seekPitch.setProgress(0)

        val b = ByteArray(2)
        b[0] = Integer.parseInt("00000000", 2).toByte();
        b[1] = Integer.parseInt("00000000", 2).toByte();
        dataSent = b;

        bConn = BLEConnection()
        getConnection()

        btnStop.setOnClickListener {
            if(!bConn.canSendData())
                return@setOnClickListener

            seekPitch.setProgress(0)
            leftMotorPow = 0
            rightMotorPow = 0
            leftMotorDir = 0
            rightMotorDir = 0
            sendData()
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

        joyStick.setOnMoveListener { angle, strength ->
            //println(angle.toString() + " - "+strength)
            calculatePerformance(angle, strength)
            sendData()
        }

    }
    private fun calculatePerformance(angle: Int, strength: Int) {
        if(angle<30 || angle>330){
            leftMotorPow = 7
            rightMotorPow = 7
            leftMotorDir = 1
            rightMotorDir = 0
        }
        else if(angle<70){
            leftMotorPow = 7
            rightMotorPow = 3
            leftMotorDir = 1
            rightMotorDir = 1
        }
        else if(angle<110) {
            leftMotorPow = 7
            rightMotorPow = 7
            leftMotorDir = 1
            rightMotorDir = 1
        }
        else if(angle<150){
            leftMotorPow = 3
            rightMotorPow = 7
            leftMotorDir = 1
            rightMotorDir = 1
        }
        else if(angle<210){
            leftMotorPow = 7
            rightMotorPow = 7
            leftMotorDir = 0
            rightMotorDir = 1
        }
        else if(angle<250){
            leftMotorPow = 7
            rightMotorPow = 3
            leftMotorDir = 0
            rightMotorDir = 0
        }
        else if(angle<290){
            leftMotorPow = 7
            rightMotorPow = 7
            leftMotorDir = 0
            rightMotorDir = 0
        }
        else if(angle<330){
            leftMotorPow = 3
            rightMotorPow = 7
            leftMotorDir = 0
            rightMotorDir = 0
        }

        if(strength<10){
            leftMotorPow = 0
            rightMotorPow = 0
        }
        else if(strength<30){
            for (i in 0..5){
                if(leftMotorPow>0)
                    leftMotorPow--
                if(rightMotorPow>0)
                    rightMotorPow--
            }
        }
        else if(strength<60){
            for (i in 0..3){
                if(leftMotorPow>0)
                    leftMotorPow--
                if(rightMotorPow>0)
                    rightMotorPow--
            }
        }
        else if(strength<80){
            for (i in 0..2){
                if(leftMotorPow>0)
                    leftMotorPow--
                if(rightMotorPow>0)
                    rightMotorPow--
            }
        }
        else if(strength<90){
            for (i in 0..1){
                if(leftMotorPow>0)
                    leftMotorPow--
                if(rightMotorPow>0)
                    rightMotorPow--
            }
        }
        println("left:" +leftMotorPow)
        println("right:" +rightMotorPow)
    }

    private fun sendData(){
        if(!bConn.canSendData())
            return

        val b = ByteArray(2)
        b[0] = Integer.parseInt(rightMotorDir.toString()+valToBits(rightMotorPow)+leftMotorDir.toString()+valToBits(leftMotorPow), 2).toByte();
        b[1] = Integer.parseInt("00001"+valToBits(seekPitch.progress), 2).toByte();

        if(dataSent[0] == b[0] && dataSent[1] == b[1])
            return

        dataSent[0] = b[0]
        dataSent[1] = b[1]

        println("left:" +leftMotorPow + "; right: "+rightMotorPow + "; midle: "+midleMotorPow)
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
        leftMotorPow = 0
        rightMotorPow = 0
        leftMotorDir = 0
        rightMotorDir = 0
        sendData()
        bConn.disconnect()
        super.onStop()
    }

    private fun getConnection() {
        var isConnStart = bConn.connect(this,MAC_ADDRESS, object : BLEConnection.ConnectionChangedListener{
            override fun onConnectionChanged(connected: Boolean) {
                runOnUiThread {
                    if(connected){
                        lblInfo.setText("Connected")
                    }
                    else {
                        lblInfo.setText("Not connected")
                        getConnection()
                    }
                }
            }
        })
    }
}
