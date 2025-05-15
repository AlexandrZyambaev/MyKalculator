package com.example.mykalculator

import android.os.Bundle
import android.provider.CalendarContract.Colors
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mykalculator.ui.theme.DarkGrey
import com.example.mykalculator.ui.theme.MyKalculatorTheme
import com.example.mykalculator.ui.theme.SuperLightGrey
//import 'package:flutter/material.dart';


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyKalculatorTheme {
                    Greeting()
            }
        }
    }
}

@Composable
fun Greeting() {
    var result = remember { mutableStateOf("") }
    var list_of_ancestors = remember { mutableStateListOf<String>() }
    var expression = remember { mutableStateOf("") }
    val listState = rememberLazyListState()
    val scrollState = rememberScrollState()
    var textSize = 16.sp


    Column (
        modifier = Modifier.fillMaxSize()
    ){
        Card (
            modifier = Modifier
                .background(DarkGrey)
                .fillMaxWidth()
                .fillMaxHeight(0.45f)
                .padding(start = 12.dp, end = 12.dp, bottom = 12.dp, top = 48.dp),
            shape = RoundedCornerShape(42.dp),
            elevation = CardDefaults.cardElevation(12.dp)
        ){
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Gray, shape = RoundedCornerShape(42.dp))
                    .padding(16.dp)
            ){
                Column (

                    modifier = Modifier.align(Alignment.BottomEnd)
                        .padding(bottom = 8.dp, top = 8.dp, end = 8.dp),
                    horizontalAlignment = Alignment.End
                ){

                    LazyColumn (
                        state = listState,
                        reverseLayout = true,
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.Bottom,
                        horizontalAlignment = Alignment.End
                    ){
                        itemsIndexed (
                            list_of_ancestors
                        ){ _, item ->
                            Text(
                                modifier = Modifier.padding(bottom = 12.dp),
                                text = item,
                                color = Color.LightGray,
                                textAlign = TextAlign.End,
                                lineHeight = 48.sp,
                                fontSize = 48.sp
                            )
                        }
                    }
                    // Разделитель между результатом и историей вычислений
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 16.dp),
                        thickness = 4.dp,
                        color = if  (expression.value == ""  && list_of_ancestors.isEmpty()) Color(0x00000000) else DarkGrey
                    )

                    Text(
                        modifier = Modifier.weight(0.4f).fillMaxWidth().horizontalScroll(scrollState),
                        text = result.value,
                        color = SuperLightGrey,
                        textAlign = TextAlign.End,
                        maxLines = 1,
                        overflow = TextOverflow.Visible,
                        fontSize = if (result.value.length > 13) 32.sp else 64.sp
                    )
                }

            }

        }
        Box (
            modifier = Modifier
                .background(DarkGrey)
                .fillMaxSize()
                .clip(RoundedCornerShape(24.dp))
                .padding(start = 12.dp, end = 12.dp, bottom = 28.dp, top = 12.dp)
        ){
            Row (
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ){
                Column (
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ){
                    DrawingButton(modifier = Modifier.weight(1f), "AC", result, expression)
                    DrawingButton(modifier = Modifier.weight(1f), "7", result, expression)
                    DrawingButton(modifier = Modifier.weight(1f), "4", result, expression)
                    DrawingButton(modifier = Modifier.weight(1f), "1", result, expression)
                    DrawingButton(modifier = Modifier.weight(1f), "0", result, expression)
                }
                Column (
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ){
                    DrawingButton(modifier = Modifier.weight(1f), "( )", result, expression)
                    DrawingButton(modifier = Modifier.weight(1f), "8", result, expression)
                    DrawingButton(modifier = Modifier.weight(1f), "5", result, expression)
                    DrawingButton(modifier = Modifier.weight(1f), "2", result, expression)
                    DrawingButton(modifier = Modifier.weight(1f), ".", result, expression)
                }
                Column (
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ){
                    DrawingButton(modifier = Modifier.weight(1f), "%", result, expression)
                    DrawingButton(modifier = Modifier.weight(1f), "9", result, expression)
                    DrawingButton(modifier = Modifier.weight(1f), "6", result, expression)
                    DrawingButton(modifier = Modifier.weight(1f), "3", result, expression)
                    DrawingButton(modifier = Modifier.weight(1f), "◀", result, expression)
                }
                Column (
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ){
                    DrawingButton(modifier = Modifier.weight(1f), "÷", result, expression)
                    DrawingButton(modifier = Modifier.weight(1f), "x", result, expression)
                    DrawingButton(modifier = Modifier.weight(1f), "-", result, expression)
                    DrawingButton(modifier = Modifier.weight(1f), "+", result, expression)
                    DrawingButton(modifier = Modifier.weight(1f), "=", result, expression)
                }
            }
        }
    }

    // Добавление вычислений в историю и удаление сообщений из истории
    LaunchedEffect(expression.value) {
       if (expression.value != "") {
           if(list_of_ancestors.isEmpty() || expression.value != list_of_ancestors.first())
            list_of_ancestors.add(0, expression.value)
       }else {
           // Если нажали АС очистить список
            if (list_of_ancestors.isNotEmpty() &&  expression.value == "" && result.value == ""){
                list_of_ancestors.clear()
            } else {
                if (list_of_ancestors.isNotEmpty() &&  list_of_ancestors.last() != expression.value) {
                    list_of_ancestors.removeAt(0)
                    expression.value = if (list_of_ancestors.isEmpty()) "" else list_of_ancestors.first()
                }
            }
       }
    }

    // Автопрокрутка при добавлении новых сообщений
    LaunchedEffect(list_of_ancestors.size) {
        if (list_of_ancestors.isNotEmpty()) {
            listState.scrollToItem(0) // Прокрутка к новому сообщению
        }
    }

    LaunchedEffect(result.value) {
        scrollState.animateScrollTo(scrollState.maxValue)  // Прокручивает в конец
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyKalculatorTheme {
        Greeting()
    }
}