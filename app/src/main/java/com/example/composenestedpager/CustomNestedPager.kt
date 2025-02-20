package com.example.composenestedpager

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.ScrollScope
import androidx.compose.foundation.gestures.TargetedFlingBehavior
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private class CustomNestedScrollConnection(private val defaultNestedScrollConnection: NestedScrollConnection) :
    NestedScrollConnection {
    override suspend fun onPostFling(consumed: Velocity, available: Velocity): Velocity {
        return Velocity.Zero
    }

    override fun onPreScroll(available: Offset, source: NestedScrollSource) =
        defaultNestedScrollConnection.onPreScroll(available, source)


    override fun onPostScroll(
        consumed: Offset,
        available: Offset,
        source: NestedScrollSource
    ) = defaultNestedScrollConnection.onPostScroll(consumed, available, source)
}

private class CustomSnapFlingBehavior(
    private val pagerState: PagerState,
    private val defaultFlingBehavior: TargetedFlingBehavior
) :
    TargetedFlingBehavior {
    override suspend fun ScrollScope.performFling(
        initialVelocity: Float,
        onRemainingDistanceUpdated: (Float) -> Unit
    ): Float {
        if (initialVelocity > 0 && !pagerState.canScrollForward || initialVelocity < 0 && !pagerState.canScrollBackward) {
            return initialVelocity
        }

        with(defaultFlingBehavior) {
            return performFling(initialVelocity, onRemainingDistanceUpdated)
        }
    }

}

@Composable
fun CustomNestedPager() {
    Box(modifier = Modifier.fillMaxSize()) {
        Text(text = "CustomNestedPager", fontSize = 40.sp)

        val rootPagerState = rememberPagerState { 2 }
        val defaultNestedScrollConnection = PagerDefaults.pageNestedScrollConnection(
            state = rootPagerState,
            orientation = Orientation.Horizontal
        )
        val customNestedScrollConnection =
            remember { CustomNestedScrollConnection(defaultNestedScrollConnection) }

        HorizontalPager(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 100.dp),
            state = rootPagerState,
            pageNestedScrollConnection = customNestedScrollConnection
        ) { page ->
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "Root page $page", fontSize = 40.sp)

                val innerPagerState = rememberPagerState { 2 }
                val defaultFlingBehavior = PagerDefaults.flingBehavior(state = innerPagerState)
                val customSnapFlingBehavior =
                    CustomSnapFlingBehavior(innerPagerState, defaultFlingBehavior)

                HorizontalPager(
                    state = innerPagerState,
                    flingBehavior = customSnapFlingBehavior
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