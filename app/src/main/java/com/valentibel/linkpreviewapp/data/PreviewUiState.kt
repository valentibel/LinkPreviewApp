package com.valentibel.linkpreviewapp.data

import com.valentibel.linkpreviewapp.model.PreviewItem

sealed class PreviewUiState {
    data class Success(val data: PreviewItem): PreviewUiState()
    data class Error(val message: String?): PreviewUiState()
    data object Idle : PreviewUiState()
    data object Loading : PreviewUiState()
}