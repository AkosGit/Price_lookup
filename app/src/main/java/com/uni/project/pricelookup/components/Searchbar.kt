package com.uni.project.pricelookup.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.style.TextAlign


@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SearchWidget(
    text: String,
    onTextChange: (String) -> Unit,
    onSearchClicked: (String) -> Unit,
    onCloseClicked: () -> Unit,
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    //ez van a search widget 'mögött/körül' -> amin rajta van
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .semantics {
                contentDescription = "SearchWidget"
            },

        color = MaterialTheme.colorScheme.primaryContainer,

        content = {
            TextField(
                modifier = Modifier
                    .semantics {
                        contentDescription = "TextField"
                    }
                    .padding(bottom = 5.dp)
                    .fillMaxWidth()
                    .scale(.83f)
                ,

                shape = RoundedCornerShape(size = 30.dp),

                value = text,
                onValueChange = { onTextChange(it) },

                colors = TextFieldDefaults.textFieldColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = MaterialTheme.colorScheme.onPrimary
                ),

                placeholder = {
                    Text(
                        modifier = Modifier
                            .alpha(alpha = DefaultAlpha),
                        text = "Search here...",
                        color = MaterialTheme.colorScheme.onPrimary,
                    )
                },
                textStyle = TextStyle(
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontStyle = MaterialTheme.typography.bodySmall.fontStyle,
                    textAlign = TextAlign.Start,
                    fontSize = MaterialTheme.typography.bodyLarge.fontSize
                ),
                singleLine = true,
                leadingIcon = {
                    IconButton(
                        modifier = Modifier
                            .alpha(alpha = DefaultAlpha),
                        onClick = { onSearchClicked(text) }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search Icon",
                            tint = MaterialTheme.colorScheme.onPrimary //to be
                        )
                    }
                },
                trailingIcon = {
                    IconButton(
                        modifier = Modifier
                            .semantics {
                                contentDescription = "CloseButton"
                            },
                        onClick = {
                            if (text.isNotEmpty()) {
                                onTextChange("")
                            } else {
                                keyboardController?.hide()
                                onCloseClicked()

                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close Icon",
                            tint = MaterialTheme.colorScheme.onPrimary //to be
                        )
                    }
                },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Search
                ),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        onSearchClicked(text)
                    }
                ),

                )
        }
    )
}

//@Composable
//@Preview
//fun SearchWidgetPreview() {
//    SearchWidget(
//        text = "Search",
//        onTextChange = {},
//        onSearchClicked = {},
//        onCloseClicked = {},
//    )
//}