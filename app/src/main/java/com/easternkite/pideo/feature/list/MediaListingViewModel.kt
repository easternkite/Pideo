package com.easternkite.pideo.feature.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.easternkite.pideo.core.common.Result
import com.easternkite.pideo.core.domain.GetMediaListUseCase
import com.easternkite.pideo.core.domain.GetRecentSearchQueryUseCase
import com.easternkite.pideo.core.domain.PutQueryUseCase
import com.easternkite.pideo.core.domain.entity.RefreshUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.scan
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MediaListingViewModel @Inject constructor(
    getMediaListUseCase: GetMediaListUseCase,
    getRecentSearchQueryUseCase: GetRecentSearchQueryUseCase,
    private val putQueryUseCase: PutQueryUseCase,
    private val refreshUseCase: RefreshUseCase
) : ViewModel() {
    val recentSearchQuery = getRecentSearchQueryUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = "Video"
        )

    val uiState = getMediaListUseCase(recentSearchQuery.value)
        .scan(MediaListingUiState()) { state, result ->
            when (result) {
                is Result.Loading -> state.copy(
                    isLoading = true,
                    error = null
                )

                is Result.Success -> state.copy(
                    isLoading = false,
                    mediaList = result.data,
                    error = null
                ).also { isRefresh.value = false }

                is Result.Error -> state.copy(
                    isLoading = false,
                    error = result.exception.localizedMessage
                ).also { isRefresh.value = false }
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = MediaListingUiState()
        )

    private var isRefresh = MutableStateFlow(false)
    val isRefreshing = isRefresh.asStateFlow()

    fun updateQuery(query: String) {
        viewModelScope.launch {
            putQueryUseCase(query)
        }
    }

    fun refreshList() {
        viewModelScope.launch {
            isRefresh.value = true
            refreshUseCase()
        }
    }
}