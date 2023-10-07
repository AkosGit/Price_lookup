package com.uni.project.pricelookup.Views

import android.widget.GridLayout
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
//import com.uni.project.pricelookup.components.OutlinedSearchBar
//import com.uni.project.pricelookup.components.SearchBar
import com.uni.project.pricelookup.components.SearchWidget
import com.uni.project.pricelookup.components.ProductPreviewCard
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment

@Composable
fun MainPage(navigation: NavController) {
    Column {
        Row(
            //horizontalArrangement
        ) {SearchWidget(
                text = "",
                onTextChange = {
                    //
                },
                onSearchClicked = {

                },
                onCloseClicked = {
                    //navController.popBackStack()
                }
            )
            Button(onClick = { /*TODO*/ }) {

            }
        }
        Column {
            ProductPreviewCard()
        }

    }


}
