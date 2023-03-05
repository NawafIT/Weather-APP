package com.api.weatherapp

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import com.api.weatherapp.mvvm.WeatherViewModel
import com.api.weatherapp.ui.theme.*
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeatherAppTheme {
                // A surface container using the 'background' color from the theme
                Start()
            }
        }
    }
}

@Composable
fun Start() {
    val context = LocalContext.current
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
    var isNetworkAvailable by remember {
        mutableStateOf(connectivityManager?.activeNetworkInfo?.isConnected ?: false)
    }

    if (isNetworkAvailable) {
        Content()
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator(color = textColor)
            Spacer(modifier = Modifier.padding(top = 16.dp))
            Button(
                onClick = {
                    isNetworkAvailable =
                        connectivityManager?.activeNetworkInfo?.isConnected ?: false
                },
                modifier = Modifier.fillMaxWidth(0.5f),
                colors = ButtonDefaults.buttonColors(topBar)
            ) {
                Text(text = "reload")
                Toast.makeText(context, "There is not Internet", Toast.LENGTH_SHORT).show()
            }
        }
    }

}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun Content() {
    val view: WeatherViewModel = viewModel()
    val weather by view.weather.observeAsState()


    Scaffold(topBar = { TopApp() }) {
        if (weather == null) {
            Column(
                modifier = Modifier
                    .background(Background)
                    .fillMaxSize()
                    .padding(32.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator(color = textColor)
            }

        } else {
            Column(
                modifier = Modifier
                    .background(Background)
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally

            ) {
                Row(
                    modifier = Modifier.align(alignment = Alignment.Start)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_baseline_location_on_24),
                        contentDescription = "",
                        modifier = Modifier
                            .size(35.dp)
                            .alpha(0.7f)
                    )

                    Text(
                        text = weather!!.location.name,
                        style = MaterialTheme.typography.h4,
                        color = textColor
                    )
                }
                Row(
                    modifier = Modifier.align(alignment = Alignment.Start)
                ) {
                    Text(
                        weather!!.location.country,
                        color = textColor,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(start = 32.dp)
                    )
                }

                if (weather!!.current.condition.text.contains('s')
                    || weather!!.current.condition.text.contains('S')
                ) {
                    ImageAnimation(R.drawable.sun)

                } else if (weather!!.current.condition.text == "Cloudy") {

                    ImageAnimation(R.drawable.cloud)

                } else if (weather!!.current.condition.text.contains('M')
                    || weather!!.current.condition.text.contains('g')
                    || weather!!.current.condition.text.contains('G')
                ) {
                    ImageAnimation(R.drawable.moon)
                } else if (weather!!.current.condition.text.contains('W')) {
                    ImageAnimation(R.drawable.wind)
                } else if (weather!!.current.condition.text == "Partly cloudy") {
                    ImageAnimation(R.drawable.cloud)
                }
                Text(
                    weather!!.current.condition.text,
                    fontWeight = FontWeight.Normal,
                    fontSize = 46.sp,
                    color = textColor
                )

                Text(
                    "${weather!!.current.temp_c}°C",
                    style = MaterialTheme.typography.h5,
                    color = Color.LightGray
                )

                Text(
                    weather!!.location.localtime,
                    color = Color.LightGray
                )


            }
        }
    }


}


@Composable
fun TopApp() {
    TopAppBar(
        title = {
            Box(
                modifier = Modifier.fillMaxWidth(), Alignment.Center
            ) {
                Text(
                    text = "Weather",
                    fontWeight = FontWeight.Bold,
                    fontSize = 25.sp,
                    color = Color.White,
                )
            }
        },
        backgroundColor = topBar
    )
}


@Composable
fun ImageAnimation(photo: Int) {
    var visible by remember { mutableStateOf(false) }
    val transition = updateTransition(targetState = visible, label = "Image Transition")
    val translateY by transition.animateDp(
        label = "Translate Y",
        transitionSpec = {
            if (false isTransitioningTo true) {
                tween(durationMillis = 1800)
            } else {
                tween(durationMillis = 1500)
            }
        }
    ) {
        if (it) 7.dp else (-7).dp
    }

    LaunchedEffect(Unit) {
        while (true) {
            visible = !visible
            delay(2000)
        }
    }

    Image(
        painter = painterResource(id = photo),
        contentDescription = "Animated Image",
        modifier = Modifier
            .size(350.dp)
            .offset(y = translateY)
            .alpha(0.8f)
    )


}