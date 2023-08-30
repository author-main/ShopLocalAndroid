package com.training.shoplocal.screens.remember

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.saveable.rememberSaveable
import com.training.shoplocal.log
import com.training.shoplocal.screens.ScreenRouter

private val MapScreenData = mutableMapOf<ScreenRouter.KEYSCREEN, ScreenData>()
private data class ScreenData(
    val index: Int,
    val offset: Int
)

@Composable
fun rememberLazyViewState(key: ScreenRouter.KEYSCREEN,
                          initFirstVisibleItemIndex: Int = 0,
                          initFirstVisibleItemScrollOffset: Int = 0): LazyGridState {

     val scrollState = rememberSaveable(saver = LazyGridState.Saver) {
        val savedValue = MapScreenData[key]
        val savedIndex = savedValue?.index ?: initFirstVisibleItemIndex
        val savedOffset = savedValue?.offset ?: initFirstVisibleItemScrollOffset
        LazyGridState(
            savedIndex,
            savedOffset
        )
    }
    DisposableEffect(Unit) {
        onDispose {
            val index  = scrollState.firstVisibleItemIndex
            val offset = scrollState.firstVisibleItemScrollOffset
            MapScreenData[key] = ScreenData(index, offset)
        }
    }
    return scrollState
}