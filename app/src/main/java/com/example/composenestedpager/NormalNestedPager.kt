package com.example.composenestedpager

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

val colorList: List<Color> = listOf(
    Color.Black,
    Color.LightGray,
    Color.Magenta,
    Color.DarkGray,
)

@Composable
fun NormalNestedPager() {
    Box(modifier = Modifier.fillMaxSize()) {
        Text(text = "NormalNestedPager", fontSize = 40.sp)

        HorizontalPager(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 100.dp),
            state = rememberPagerState { 2 }
        ) { page ->
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "Root page $page", fontSize = 40.sp)

                HorizontalPager(
                    state = rememberPagerState { 2 }
                ) { innerPage ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(color = colorList[page * 2 + innerPage]),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "inner page $innerPage", fontSize = 20.sp, color = Color.White)
                    }
                }
            }
        }
    }
}