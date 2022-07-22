package com.ixsans.modulebridge

import android.content.Context
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel

private const val CHANNEL = "com.ixsans.flutermodule"

enum class ModuleState {
    INITIAL,
    LOADING,
    SUCCESS,
    FAILED
}

class FlutterBridgeActivity : FlutterActivity() {

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        //this.overridePendingTransition(0, 0);
        window.setWindowAnimations(0);
    }

    companion object {
        var stateListener: StateChangeListener? = null

        fun withCachedEngine(cachedEngineId: String): CachedEngineIntentBuilder {
            return CachedEngineIntentBuilder(FlutterBridgeActivity::class.java, cachedEngineId)
        }

        fun withNewEngine(): NewEngineIntentBuilder {
            return NewEngineIntentBuilder(FlutterBridgeActivity::class.java)
        }
        fun listenState(state: StateChangeListener) {
            stateListener = state
        }
    }

    interface StateChangeListener {
        fun onStateChanged(state: ModuleState, data: String? = null)
    }

    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)
        MethodChannel(flutterEngine.dartExecutor.binaryMessenger, CHANNEL)
            .setMethodCallHandler { call, result ->
                val data = call.argument<String>("data")
                when (call.method) {
                    "setLoading" -> {
                        Log.d("FLUTTERMODULE", "STATE:: stateLoading")
                        stateListener?.onStateChanged(ModuleState.LOADING)
                    }
                    "setSuccess" -> {
                       Log.d("FLUTTERMODULE","STATE:: STATE SUCCESS")
                        stateListener?.onStateChanged(ModuleState.SUCCESS, data)
                        result.success("");
                    }
                    "setFailed" -> {
                        Log.d("FLUTTERMODULE","STATE:: STATE FAILED")
                        stateListener?.onStateChanged(ModuleState.FAILED)
                    }
                    else -> {
                        Log.d("FLUTTERMODULE","STATE:: STATE INITIAL")
                        stateListener?.onStateChanged(ModuleState.INITIAL)
                    }
                }
            }
    }
}