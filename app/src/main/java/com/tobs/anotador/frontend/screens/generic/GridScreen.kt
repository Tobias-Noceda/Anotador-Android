package com.tobs.anotador.frontend.screens.generic

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp

@Composable
fun GridScreen(modifier: Modifier, columns: Int, cells: List<@Composable () -> Unit>) {
    if (columns == 2 || columns == 3) {
        Box(modifier.padding(horizontal = 6.dp)) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(columns),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                items(cells.size) { i ->
                    cells[i]()
                }
            }
        }
    } else {
        val scrollState = rememberScrollState(0)

        // Get the screen width
        val configuration = LocalConfiguration.current
        val screenWidth = configuration.screenWidthDp.dp
        val width = screenWidth * 0.318f * columns + 2.dp * columns

        Box(
            modifier = Modifier
                .fillMaxHeight()
                .padding(horizontal = 6.dp)
                .horizontalScroll(scrollState)
        ) {
            Row(Modifier.width(width)) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(columns),
                    modifier = Modifier
                        .padding(8.dp)
                ) {
                    items(cells.size) { i ->
                        cells[i]()
                    }
                }
            }
        }
    }
}
