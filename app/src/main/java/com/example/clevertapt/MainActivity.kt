package com.example.clevertapt

import android.os.Bundle
import android.util.Log.VERBOSE
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.clevertap.android.sdk.CleverTapAPI
import com.example.clevertapt.ui.theme.CleverTapTTheme
import java.util.Date

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val cleverTapDefaultInstance = CleverTapAPI.getDefaultInstance(applicationContext)
        CleverTapAPI.setDebugLevel(VERBOSE)
        CleverTapAPI.onActivityResumed(this)

        super.onCreate(savedInstanceState)

        genProfile()

        enableEdgeToEdge()
        setContent {
            CleverTapTTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding), onClick = { buttonClick() }
                    )
                }
            }
        }
    }

    fun genProfile() {
        val cleverTapDefaultInstance = CleverTapAPI.getDefaultInstance(applicationContext)

        // each of the below mentioned fields are optional
        val profileUpdate = HashMap<String, Any>()
        profileUpdate["Name"] = "Analu Sorbara" // String
        profileUpdate["Identity"] = 61026032 // String or number
        profileUpdate["Email"] = "clevertap+sorbaraanalu@gmail.com" // Email address of the user
        profileUpdate["Phone"] = "+14155551234" // Phone (with the country code, starting with +)
        profileUpdate["Gender"] = "F" // Can be either M or F
        profileUpdate["DOB"] = Date(1989, 2,8) // Date of Birth. Set the Date object to the appropriate value first
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

    fun buttonClick() {
        val cleverTapDefaultInstance = CleverTapAPI.getDefaultInstance(applicationContext)

        val prodViewedAction = HashMap<String, Any>()
        prodViewedAction["Product ID"] = 1
        prodViewedAction["Product Image"] = "https://d35fo82fjcw0y8.cloudfront.net/2018/07/26020307/customer-success-clevertap.jpg"
        prodViewedAction["Product Name"] = "CleverTap"

        cleverTapDefaultInstance?.pushEvent("Product viewed", prodViewedAction)
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Column {
        Text(
            text = "CleverTap SDK Test $name!",
            modifier = modifier
        )
        Button(onClick = onClick) {
            Text("Click to send a product viewed event")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CleverTapTTheme {
        Greeting("Android", onClick = {})
    }
}