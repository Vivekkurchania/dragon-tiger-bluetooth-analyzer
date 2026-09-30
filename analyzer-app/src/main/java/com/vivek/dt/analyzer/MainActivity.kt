package com.vivek.dt.analyzer
import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.os.Bundle
import android.widget.*
import java.util.UUID
import kotlin.concurrent.thread
class MainActivity:Activity(){
 private val uuid=UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");private val data=ArrayDeque<String>();private lateinit var status:TextView;private lateinit var view:TextView
 override fun onCreate(b:Bundle?){super.onCreate(b);if(android.os.Build.VERSION.SDK_INT>=31)requestPermissions(arrayOf(Manifest.permission.BLUETOOTH_CONNECT,Manifest.permission.BLUETOOTH_SCAN),11);ui();server()}
 private fun ui(){val r=LinearLayout(this);r.orientation=LinearLayout.VERTICAL;r.setPadding(28,28,28,28);val h=TextView(this);h.text="📊 Dragon Tiger Analyzer";h.textSize=26f;r.addView(h);status=TextView(this);status.text="Waiting for Sender…";status.textSize=16f;r.addView(status);view=TextView(this);view.text="Latest 100: 0\n\nNext estimate: —";view.textSize=20f;r.addView(view);setContentView(r)}
 private fun server(){thread{try{val s=BluetoothAdapter.getDefaultAdapter().listenUsingRfcommWithServiceRecord("DT Analyzer",uuid);runOnUiThread{status.text="Waiting for Bluetooth connection…"};val sock=s.accept();runOnUiThread{status.text="Sender connected"};val br=sock.inputStream.bufferedReader();while(true){val x=br.readLine()?:break;if(x=="D"||x=="T")add(x)}}catch(e:Exception){runOnUiThread{status.text="Bluetooth: ${e.message}"}}}}
 private fun add(x:String){synchronized(data){if(data.size>=100)data.removeFirst();data.addLast(x)};runOnUiThread{update()}}
 private fun update(){val a=data.toList();val d=a.count{it=="D"};val t=a.size-d;var dd=0;var dt=0;var td=0;var tt=0;for(i in 1 until a.size)when(a[i-1]+a[i]){"DD"->dd++;"DT"->dt++;"TD"->td++;"TT"->tt++};val last=a.last();val p=if(last=="D")(dd+1.0)/(dd+dt+2)else(td+1.0)/(td+tt+2);val f=(d+1.0)/(a.size+2);val score=.6*p+.4*f;val est=if(score>=.5)"🐉 DRAGON" else "🐯 TIGER";view.text="Latest results: ${a.size}/100\nDragon: $d   Tiger: $t\nLast: $last\n\nNEXT STATISTICAL ESTIMATE\n$est\nScore: ${(score*100).toInt()}%\n\nStatistical estimate only — RNG outcomes cannot be guaranteed."}
}
