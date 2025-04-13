package com.example.clevertapt

import android.app.NotificationManager
import android.content.ContentValues.TAG
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.util.Log.VERBOSE
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.clevertap.android.sdk.CleverTapAPI
import com.clevertap.android.sdk.PushPermissionResponseListener
import com.example.clevertapt.ui.theme.CleverTapTTheme
import java.util.Date

class MainActivity : ComponentActivity(), PushPermissionResponseListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), 1001)
            }
        }

        val cleverTapDefaultInstance = CleverTapAPI.getDefaultInstance(applicationContext)

        CleverTapAPI.setDebugLevel(VERBOSE)
        CleverTapAPI.onActivityResumed(this)

        super.onCreate(savedInstanceState)

        cleverTapDefaultInstance?.apply {
            enableDeviceNetworkInfoReporting(true)
            registerPushPermissionNotificationResponseListener(this@MainActivity)
        }

        genProfile()

        enableEdgeToEdge()
        setContent {
            CleverTapTTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainScreen(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding),
                        onEvent = { buttonClick() },
                        onNotification = { /*fakeNotification()*/ }
                    )
                }
            }
        }
    }

    /*private fun fakeNotification() {
        val bundle = Bundle().apply {
            putString("wzrk_acct_id", "TEST-WWR-KR5-KW7Z")
            putString("nm", "Grab 'em on Myntra's Maxessorize Sale")
            putString("nt", "Ye dil ❤️️ maange more accessories?")
            putString("pr", "")
            putString("wzrk_pivot", "")
            putString("wzrk_sound", "true")
            putString("wzrk_cid", "BRTesting")
            putString("wzrk_clr", "#ed732d")
            putString("wzrk_nms", "Grab 'em on Myntra's Maxessorize Sale")
            putString("wzrk_pid", (10000_00000..99999_99999).random().toString())
            putString("wzrk_rnv", "true")
            putString("wzrk_ttl", "1627731067")
            putString("wzrk_push_amp", "false")
            putString("wzrk_bc", "")
            putString("wzrk_bi", "2")
            putString("wzrk_bp", "https://imgur.com/6DavQwg.jpg")
            putString("wzrk_dl", "")
            putString("wzrk_dt", "FIREBASE")
            putString("wzrk_id", "1627639375_20210730")
            putString("wzrk_pn", "true")
        }

        Thread {
            CleverTapAPI.createNotification(applicationContext, bundle)
        }.start()
        Thread {
            CleverTapAPI.createNotification(applicationContext, bundle)
        }.start()
    }
    just for test - Github example
    */

    private fun genProfile() {
        val cleverTapDefaultInstance = CleverTapAPI.getDefaultInstance(applicationContext)

        // each of the below mentioned fields are optional
        val profileUpdate = HashMap<String, Any>()
        profileUpdate["Name"] = "Analu Sorbara" // String
        profileUpdate["Identity"] = 61026032 // String or number
        profileUpdate["Email"] = "clevertap+sorbaraanalu@gmail.com" // Email address of the user
        profileUpdate["Phone"] = "+14155551234" // Phone (with the country code, starting with +)
        profileUpdate["Gender"] = "F" // Can be either M or F
        profileUpdate["DOB"] =
            Date(1989, 2, 8) // Date of Birth. Set the Date object to the appropriate value first
// optional fields. controls whether the user will be sent email, push etc.
// optional fields. controls whether the user will be sent email, push etc.
        profileUpdate["MSG-email"] = false // Disable email notifications
        profileUpdate["MSG-push"] = true // Enable push notifications
        profileUpdate["MSG-sms"] = false // Disable SMS notifications
        profileUpdate["MSG-whatsapp"] = true // Enable WhatsApp notifications
        val stuff = ArrayList<String>()
        stuff.add("bag")
        stuff.add("shoes")
        profileUpdate["MyStuff"] = stuff //ArrayList of Strings
        val otherStuff = arrayOf("Jeans", "Perfume")
        profileUpdate["MyStuff"] = otherStuff //String Array

        cleverTapDefaultInstance?.pushProfile(profileUpdate)
    }

    private fun buttonClick() {
        val cleverTapDefaultInstance = CleverTapAPI.getDefaultInstance(applicationContext)

        val prodViewedAction = HashMap<String, Any>()
        prodViewedAction["Product ID"] = 1
        prodViewedAction["Product Image"] =
            "https://d35fo82fjcw0y8.cloudfront.net/2018/07/26020307/customer-success-clevertap.jpg"
        prodViewedAction["Product Name"] = "CleverTap"

        cleverTapDefaultInstance?.pushEvent("Product viewed", prodViewedAction)
    }

    override fun onPushPermissionResponse(accepted: Boolean) {
        Log.i(TAG, "InApp---> response() called  $accepted")
        if (accepted) {
            Toast.makeText(this, "Permission granted!", Toast.LENGTH_SHORT).show()

            //For Android 13+ we need to create notification channel after notification permission is accepted
            CleverTapAPI.createNotificationChannel(
                this, "BRTesting", "Core",
                "Core notifications", NotificationManager.IMPORTANCE_MAX, true
            )

            CleverTapAPI.createNotificationChannel(
                this, "PTTesting", "Push templates",
                "All push templates", NotificationManager.IMPORTANCE_MAX, true
            )
        } else {
            Toast.makeText(this, "Permission denied!", Toast.LENGTH_SHORT).show()
        }
    }
}

@Composable
fun MainScreen(name: String,
               modifier: Modifier = Modifier,
               onEvent: () -> Unit = {},
               onNotification: () -> Unit = {}) {
    Column(
        modifier = modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "CleverTap SDK Test $name!"
        )
        Button(onClick = onEvent) {
            Text("Click to send a product viewed event")
        }
       /*Button(onClick = onNotification) {
            Text("Click to send a push notification")
        }*/
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    CleverTapTTheme {
        MainScreen("Android")
    }
}