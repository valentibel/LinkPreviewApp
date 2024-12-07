package com.valentibel.linkpreviewapp.screens.home

import android.webkit.URLUtil
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SearchBarDefaults.InputField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.valentibel.linkpreviewapp.R
import com.valentibel.linkpreviewapp.data.PreviewUiState
import com.valentibel.linkpreviewapp.model.PreviewItem

@Composable
fun HomeScreen(viewModel: HomeScreenViewModel = hiltViewModel()) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    Column(modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {
        val keyboardController = LocalSoftwareKeyboardController.current
        val searchQuery = rememberSaveable() {
            mutableStateOf("")
        }
        val valid = rememberSaveable(searchQuery.value) {
            URLUtil.isValidUrl(searchQuery.value)
        }
        SearchTextField(
            searchQuery,
            KeyboardActions { keyboardController?.hide() }
        )
        Button(onClick = {
            viewModel.loadPreview(searchQuery.value)
            //searchQuery.value = ""
            keyboardController?.hide()
                         },
            enabled = valid) {
            Text(stringResource(R.string.get_preview))
        }

        when (val state = uiState.value) {
            is PreviewUiState.Error -> {
                Text(text = state.message.toString(), style = MaterialTheme.typography.bodyLarge, color = Color.Red,
                    modifier = Modifier.padding(15.dp))
                }
            is PreviewUiState.Idle -> {
                Box{}
            }
            is PreviewUiState.Loading -> {
                LinearProgressIndicator()
            }
            is PreviewUiState.Success -> {WebPagePreview(state.data)}
        }

    }
}

@Composable
fun WebPagePreview(previewItem: PreviewItem) {
    val uriHandler = LocalUriHandler.current
    Card(modifier = Modifier.padding(25.dp).background(color = Color.White), onClick = {
    uriHandler.openUri(previewItem.url)
     }) {
        Column(modifier = Modifier.background(color = Color.White).padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally) {
            AsyncImage(model = previewItem.image, contentDescription = "preview image")
            Text(text = previewItem.title, style = MaterialTheme.typography.titleMedium)
            Text(text = previewItem.description, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
fun SearchTextField(searchState: MutableState<String>, keyboardActions: KeyboardActions = KeyboardActions.Default) {

    OutlinedTextField(value = searchState.value,
        onValueChange = {
            searchState.value = it
        },
        label = { Text(text = stringResource(R.string.url)) },
        singleLine = true,
        textStyle = TextStyle(fontSize = 18.sp,
            color = MaterialTheme.colorScheme.onBackground),
        modifier = Modifier
            .padding(bottom = 10.dp, start = 10.dp, end = 10.dp)
            .fillMaxWidth(),
        enabled = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Uri,
            imeAction = ImeAction.Next),
        keyboardActions = keyboardActions
    )
}