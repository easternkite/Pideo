package com.easternkite.pideo.core.ui.component

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.PullToRefreshState
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PdLazyList(
    modifier: Modifier = Modifier,
    isVertical: Boolean = true,
    pullToRefreshState: PullToRefreshState = rememberPullToRefreshState(),
    listState: LazyListState = rememberLazyListState(),
    userScrollEnabled: Boolean = true,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    isRefreshing: Boolean = false,
    onRefresh: () -> Unit = { },
    refreshIndicator: @Composable BoxScope.() -> Unit = {
        Indicator(
            modifier = Modifier.align(Alignment.TopCenter),
            isRefreshing = isRefreshing,
            state = pullToRefreshState
        )
    },
    content: LazyListScope.() -> Unit
) {
    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = onRefresh,
        state = pullToRefreshState,
        indicator = refreshIndicator
    ) {
        if (isVertical) {
            LazyColumn(
                modifier = modifier,
                state = listState,
                contentPadding = contentPadding,
                userScrollEnabled = userScrollEnabled,
                content = content
            )
        } else {
            LazyRow(
                modifier = modifier,
                state = listState,
                contentPadding = contentPadding,
                userScrollEnabled = userScrollEnabled,
                content = content
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview(showBackground = true)
fun PdLazyListPreview() {
    val state = rememberPullToRefreshState()
    val scope = rememberCoroutineScope()
    var isRefreshing by remember { mutableStateOf(false) }

    PdLazyList(
        pullToRefreshState = state,
        isVertical = true,
        contentPadding = PaddingValues(10.dp),
        isRefreshing = isRefreshing,
        onRefresh = {
            scope.launch {
                isRefreshing = true
                delay(1000L)
                isRefreshing = false
            }
        }
    ) {
        items(100) {
            Text(text = "item $it", modifier = Modifier.fillMaxWidth().height(48.dp))
        }
    }
}