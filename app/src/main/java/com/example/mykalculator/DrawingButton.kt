package com.example.mykalculator

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.ArrayList

@Composable
fun DrawingButton(modifier: Modifier = Modifier, text : String, state: MutableState<String>, expression: MutableState<String>){
    Box (
        modifier = modifier
            .background(Color.LightGray, shape = RoundedCornerShape(42.dp))
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .clickable {
                LogicOfInteraction(text, state, expression)
            },

        contentAlignment = Alignment.Center
    ){
        Text(
            fontSize = 42.sp,
            text = text
        )
    }
}