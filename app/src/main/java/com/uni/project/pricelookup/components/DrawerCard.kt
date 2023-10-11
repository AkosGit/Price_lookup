package com.uni.project.pricelookup.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.uni.project.pricelookup.R


@Preview
@Composable
public fun DrawerCard(
    drawerWidth: Dp = 260.dp
){
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        modifier = androidx.compose.ui.Modifier
//                                            .height(150.dp)
            .width(drawerWidth)
        ,
        shape = RectangleShape,
        content = {
            Box(
                modifier = Modifier
                    .width(drawerWidth)
                    .padding(all = 10.dp)
                ,
                contentAlignment = Alignment.TopCenter
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painterResource(R.mipmap.ic_launcher_foreground),
                        contentDescription = "appIcon"
                    )
                    Text("UX designer: Pálosi Ákos")
                    Text("UI designer: Dézsi Csaba")
                }
            }

        }
    )
}