package com.henkahau.venueapp.ui.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

/**
 * Text field component to get user's input.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Search(modifier: Modifier = Modifier, onTextChanged: (String) -> Unit) {
    var text by remember {
        mutableStateOf("")
    }
    TextField(modifier = modifier, value = text, label = { SearchLabel() }, onValueChange = {
        text = it
        onTextChanged(it)
    })
}

@Composable
private fun SearchLabel() {
    Text(text = "Search nearby places")
}