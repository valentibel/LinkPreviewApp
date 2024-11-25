package com.valentibel.linkpreviewapp.screens.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.valentibel.linkpreviewapp.data.PreviewUiState
import com.valentibel.linkpreviewapp.repository.PreviewRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(private val repository: PreviewRepository): ViewModel() {
    private val _uiState = MutableStateFlow<PreviewUiState>(PreviewUiState.Idle)
    val uiState: StateFlow<PreviewUiState> = _uiState

    private var loadJob: Job? = null

    fun loadPreview(url: String) {
        _uiState.value = PreviewUiState.Loading

        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            val result = repository.getPreview(url)
            if (result.isSuccess) {
                result.getOrNull()?.apply {
                    _uiState.value = PreviewUiState.Success(data = this) }
                    ?: Log.e("HomeScreenViewModel", "Data is empty")
            } else {
                _uiState.value = PreviewUiState.Error(message = result.exceptionOrNull()?.message)
            }
        }
    }
}