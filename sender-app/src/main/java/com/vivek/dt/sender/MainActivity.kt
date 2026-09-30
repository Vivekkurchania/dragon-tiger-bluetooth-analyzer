package com.vivek.dt.sender
import android.Manifest
import android.app.Activity
import android.bluetooth.*
import android.os.Bundle
import android.widget.*
import java.io.OutputStream
import java.util.UUID
class MainActivity:Activity(){
 private val uuid=UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"); private var out:OutputStream?=null; private lateinit var status:TextView; private val adapter=BluetoothAdapter.getDefaultAdapter()
 override fun onCreate(b:Bundle?){super.onCreate(b);if(android.os.Build.VERSION.SDK_INT>=31)requestPermissions(arrayOf(Manifest.permission.BLUETOOTH_CONNECT,Manifest.permission.BLUETOOTH_SCAN),10);ui()}
 private fun ui(){val r=LinearLayout(this);r.orientation=LinearLayout.VERTICAL;r.setPadding(32,32,32,32);val h=TextView(this);h.text="🐉 Dragon Tiger Sender";h.textSize=26f;r.addView(h);status=TextView(this);status.text="Not connected";r.addView(status);val ds=adapter?.bondedDevices?.toList()?:emptyList();val sp=Spinner(this);sp.adapter=ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,if(ds.isEmpty())listOf("No paired devices")else ds.map{"${it.name} (${it.address})"});r.addView(sp);val c=Button(this);c.text="Connect to Analyzer";r.addView(c);val d=Button(this);d.text="🐉 DRAGON";d.textSize=22f;r.addView(d);val t=Button(this);t.text="🐯 TIGER";t.textSize=22f;r.addView(t);c.setOnClickListener{if(ds.isNotEmpty())connect(ds[sp.selectedItemPosition])};d.setOnClickListener{send("D")};t.setOnClickListener{send("T")};setContentView(r)}
 private fun connect(dev:BluetoothDevice){Thread{try{val s=dev.createRfcommSocketToServiceRecord(uuid);s.connect();out=s.outputStream;runOnUiThread{status.text="Connected: ${dev.name}"}}catch(e:Exception){runOnUiThread{status.text="Failed: ${e.message}"}}}.start()}
 private fun send(x:String){try{out?.write("$x\n".toByteArray());out?.flush();Toast.makeText(this,"Sent $x",Toast.LENGTH_SHORT).show()}catch(e:Exception){Toast.makeText(this,"Connect first",Toast.LENGTH_SHORT).show()}}
}
