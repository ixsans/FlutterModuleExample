package com.ixsans.hostapp

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import com.ixsans.modulebridge.FlutterBridgeActivity
import com.ixsans.modulebridge.ModuleState
import io.flutter.embedding.android.FlutterActivityLaunchConfigs

class MainActivity : AppCompatActivity() {

    private val FLUTTER_ENGINE_ID = "flutter_engine_id"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Avoid animation during finish the called activity
        window.setWindowAnimations(0);

        val text = findViewById<TextView>(R.id.textView)
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)

        findViewById<Button>(R.id.button).setOnClickListener {
            overridePendingTransition(0, 0);
            startActivity(
                FlutterBridgeActivity
                    .withNewEngine()
                    //.withCachedEngine(FLUTTER_ENGINE_ID)
                    .backgroundMode(FlutterActivityLaunchConfigs.BackgroundMode.transparent)
                    .build(this)
                    .addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            )
        }

        FlutterBridgeActivity.listenState(object: FlutterBridgeActivity.StateChangeListener {
            @SuppressLint("SetTextI18n")
            override fun onStateChanged(state: ModuleState, data: String?) {
                when(state) {
                    ModuleState.INITIAL -> Unit
                    ModuleState.LOADING -> {
                        text.text = "Loading..."
                        progressBar.visibility = View.VISIBLE
                    }
                    ModuleState.SUCCESS -> {
                        progressBar.visibility = View.GONE
                        text.text = "Data: $data"
                    }
                    ModuleState.FAILED -> {
                        progressBar.visibility = View.GONE
                        text.text = "Failed: $data"
                    }
                }
            }

        })
    }
}