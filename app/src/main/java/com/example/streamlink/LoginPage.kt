package com.example.streamlink

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.colorResource

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)
@Composable
fun LoginPage(modifier: Modifier = Modifier) {
    var meetingId by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    val context = LocalContext.current
    val balooFont=FontFamily(
        Font(R.font.baloo_black, FontWeight.Black)
    )

    val electricBlue = Color(0xFF5BF0FD)
    val dodgerBlue = Color(0xFF0593FA)
    val lightGray = Color(0xFFF5F5F5)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.oxford_blue)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Define Montserrat Font
        val montserratFont = FontFamily(
            Font(R.font.montserrat_black, FontWeight.Black)
        )


      // App Title
        Text(
            text = "StreamLink",
            fontSize = 32.sp,
            fontWeight = FontWeight.Black, // Set to Black weight
            fontFamily = montserratFont, // Apply Montserrat Black
            color = Color.White,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // Card Container
        Card(
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier
                .padding(16.dp)
                .border(2.dp, dodgerBlue, RoundedCornerShape(20.dp))
                .shadow(8.dp, shape = RoundedCornerShape(20.dp)),
            colors = CardDefaults.cardColors(
                containerColor = Color.White // White background
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        )
        {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Meeting ID",
                    fontWeight = FontWeight.Bold,
                    fontFamily = balooFont,// Make text bold
                    textAlign = TextAlign.Start, // Align text to the start (left)
                    modifier = Modifier.fillMaxWidth() // Ensures alignment works properly
                )
                Spacer(modifier = Modifier.height(16.dp))


                OutlinedTextField(
                    value = meetingId,
                    onValueChange = { meetingId = it },
                    placeholder = { Text("Enter Meeting ID",fontFamily = balooFont)},
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        containerColor = Color.White
                    ),
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                )


                Spacer(modifier = Modifier.height(30.dp))
                Text(
                    text = "Your Name",
                    fontWeight = FontWeight.Bold,
                    fontFamily = balooFont,
                    textAlign = TextAlign.Start, // Align text to the start (left)
                    modifier = Modifier.fillMaxWidth() // Ensures alignment works properly
                )
                Spacer(modifier = Modifier.height(16.dp))
                // Name Field
                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it },
                    //label = { Text("Your Name") },
                    placeholder = { Text("Enter Your Name",fontFamily = balooFont,) },

                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        containerColor = Color.White
                    )
                )

                Spacer(modifier = Modifier.height(30.dp))

                // Join Meeting Button
                Button(
                    enabled = meetingId.length == 10 && username.length >= 3,
                    onClick = {
                        val intent = Intent(context, ConferenceActivity::class.java)
                        intent.putExtra("MEETING_ID", meetingId)
                        intent.putExtra("USERNAME", username)
                        context.startActivity(intent)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .shadow(6.dp, shape = RoundedCornerShape(35.dp)),
                    colors = ButtonDefaults.buttonColors(containerColor = electricBlue),
                    shape = RoundedCornerShape(35.dp)
                ) {
                    Text(
                        text = "Join Meeting",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black, // Set to Black weight
                        fontFamily = montserratFont, // Apply Montserrat Black
                        color = Color.White,

                        )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Create Meeting Button
                OutlinedButton(
                    enabled = username.length >= 3,
                    onClick = {
                        try {
                            val randomMeetingId = (1..10).map { (0..9).random() }.joinToString("")
                            val intent = Intent(context, ConferenceActivity::class.java).apply {
                                putExtra("MEETING_ID", randomMeetingId)
                                putExtra("USERNAME", username)
                            }
                            context.startActivity(intent)
                        } catch (e: Exception) {
                            e.printStackTrace()
                            Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .border(
                            2.dp,
                            if (username.length >= 3) dodgerBlue else Color.Gray, // Gray when disabled
                            RoundedCornerShape(35.dp)
                        )
                        .shadow(4.dp, shape = RoundedCornerShape(35.dp)),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Color.White, // White background
                        contentColor = if (username.length >= 3) dodgerBlue else Color.Gray // Gray text when disabled
                    ),
                    shape = RoundedCornerShape(35.dp)
                ) {
                    Text(
                        text = "Create Meeting",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black, // Set to Black weight
                        fontFamily = montserratFont, // Apply Montserrat Black
                        color = if (username.length >= 3) electricBlue else Color.Gray // Change text color dynamically
                    )
                }

            }
        }
    }
}
