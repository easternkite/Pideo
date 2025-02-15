package com.easternkite.pideo.feature.list

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.easternkite.pideo.core.ui.component.PdLazyList
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MediaListingScreen(
    modifier: Modifier = Modifier.fillMaxSize(),
    viewModel: MediaListingViewModel = hiltViewModel(),
) {
    val scope = rememberCoroutineScope()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val listState = rememberLazyListState()
    val query by viewModel.recentSearchQuery.collectAsStateWithLifecycle("")
    val isRefreshing by viewModel.isRefreshing.collectAsStateWithLifecycle()
    var inputState by rememberSaveable(query) { mutableStateOf(query) }
    val isCenterLoading by remember { derivedStateOf { uiState.isLoading && !isRefreshing } }
    val showScrollToTopButton by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex > 0
        }
    }

    Scaffold(
        modifier = modifier
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(horizontal = 10.dp),
        topBar = {
            SearchBar(
                modifier = Modifier
                    .statusBarsPadding()
                    .fillMaxWidth()
                    .padding(bottom = 10.dp),
                query = inputState,
                onQueryChange = {
                    inputState = it
                    viewModel.updateQuery(it)
                }
            )
        },
    ) { padding ->
        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.primaryContainer)
                .padding(
                    top = padding.calculateTopPadding(),
                    start = padding.calculateStartPadding(LayoutDirection.Ltr),
                    end = padding.calculateEndPadding(LayoutDirection.Ltr)
                )
        ) {
            PdLazyList(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = padding.calculateBottomPadding() + 10.dp),
                isRefreshing = isRefreshing,
                onRefresh = viewModel::refreshList,
                listState = listState
            ) {
                if (uiState.mediaList.isEmpty() && !uiState.isLoading) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillParentMaxSize()
                                .systemBarsPadding()
                        ) {
                            Text(
                                text = "콘텐츠를 찾을 수 없어요.",
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }
                    }
                }
                items(uiState.mediaList) { media ->
                    MediaCell(
                        title = media.name,
                        date = SimpleDateFormat(
                            "yyyy-MM-dd HH:mm:ss",
                            Locale.KOREA
                        ).format(media.dateTime),
                        modifier = Modifier.fillMaxWidth(),
                        image = {
                            AsyncImage(
                                modifier = Modifier.fillMaxSize(),
                                model = media.imageUrl,
                                contentDescription = "media image",
                                contentScale = ContentScale.Crop
                            )
                        }
                    )
                    if (uiState.mediaList.last() != media) {
                        Spacer(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(10.dp)
                        )
                    }
                }
            }

            if (isCenterLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }

            ScrollToTopButton(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .systemBarsPadding()
                    .padding(bottom = 10.dp)
                ,
                onClick = {
                    scope.launch {
                        listState.animateScrollToItem(0)
                    }
                },
                isVisible = showScrollToTopButton
            )
        }
    }
}

@Composable
fun MediaCell(
    title: String,
    date: String,
    modifier: Modifier = Modifier,
    image: @Composable () -> Unit,
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(MaterialTheme.colorScheme.onSecondary)
            .padding(10.dp)
    ) {
        Box(modifier = Modifier.clip(RoundedCornerShape(10.dp))) {
            image()
        }
        Row(
            modifier = Modifier.padding(top = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                title,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.titleMedium,
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                modifier = Modifier.align(Alignment.Top),
                text = date,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    query: String = "",
    onQueryChange: (String) -> Unit = {},
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(MaterialTheme.colorScheme.background)
            .height(48.dp)
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier
                .size(36.dp)
                .padding(end = 10.dp),
            imageVector = Icons.Default.Search,
            contentDescription = "Search Icon"
        )
        BasicTextField(
            modifier = Modifier.fillMaxWidth(),
            maxLines = 1,
            value = query,
            onValueChange = onQueryChange,
        )
    }
}

@Composable
fun ScrollToTopButton(
    modifier: Modifier = Modifier,
    isVisible: Boolean = false,
    onClick: () -> Unit = {},
) {
    val shape = RoundedCornerShape(10.dp)
    AnimatedVisibility(
        visible = isVisible,
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .clip(shape)
                .size(48.dp)
                .background(MaterialTheme.colorScheme.primary)
                .clickable { onClick() }
        ) {
            Icon(
                modifier = Modifier
                    .size(36.dp)
                    .align(Alignment.Center),
                imageVector = Icons.Default.KeyboardArrowUp,
                tint = MaterialTheme.colorScheme.onPrimary,
                contentDescription = "ScrollToTop Icon"
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun MediaCellPreview() {
    MediaCell(
        title = "title",
        date = "2025-01-18",
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .size(400.dp)
                .background(MaterialTheme.colorScheme.secondary)
        )
    }
}

@Composable
@Preview(showBackground = true)
fun SearchBarPreview() {
    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(5.dp)
    ) {
        SearchBar()
    }
}

@Composable
@Preview(showBackground = true)
fun ScrollToTopButtonPreview() {
    Column(
        modifier = Modifier
            .background(Color.White)
            .padding(5.dp)
    ) {
        ScrollToTopButton()
    }
}