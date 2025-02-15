package com.easternkite.pideo.feature.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun MediaListingScreen(
    modifier: Modifier = Modifier.fillMaxSize(),
    contentPadding: PaddingValues = PaddingValues(0.dp),
    viewModel: MediaListingViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val query by viewModel.recentSearchQuery.collectAsStateWithLifecycle("")
    var inputState by rememberSaveable(query) { mutableStateOf(query) }

    Box(
        modifier = modifier
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(10.dp)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = contentPadding
        ) {
            item {
                SearchBar(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp),
                    query = inputState,
                    onQueryChange = {
                        inputState = it
                        viewModel.updateQuery(it)
                    }
                )
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

        if (uiState.isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
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

