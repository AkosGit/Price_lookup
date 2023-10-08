package com.uni.project.pricelookup.Views


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.uni.project.pricelookup.components.PhotoGrid
import com.uni.project.pricelookup.components.SearchWidget

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

        val list = arrayListOf<String>()
        for (i in 1..30) {
            list.add("asd")
        }
        PhotoGrid(recommendedItems = list)

    }


}
