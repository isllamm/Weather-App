package com.weatherapp.presentation.features.addcitybottomsheet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.weatherapp.core.ui.components.DefaultButton
import com.weatherapp.core.ui.components.DefaultTextField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCityBottomSheet(
    onDismiss: () -> Unit = {},
    onApply: (String) -> Unit = {}
) {
    val modalBottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var cityName by remember { mutableStateOf("") }
    ModalBottomSheet(
        onDismissRequest = {
            onDismiss()
        },
        sheetState = modalBottomSheetState,
        dragHandle = { BottomSheetDefaults.DragHandle() },
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        containerColor = Color.White,
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Color.White,
                    shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                ),
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Spacer(modifier = Modifier.padding(16.dp))
                DefaultTextField(
                    hint = "City Name",
                    initialText = cityName,
                    modifier = Modifier.fillMaxWidth()
                ) { name ->
                    cityName = name
                }
                Spacer(modifier = Modifier.padding(32.dp))

                DefaultButton(
                    "Submit",
                    modifier = Modifier.fillMaxWidth()
                ){
                    onApply(cityName)
                }
                Spacer(modifier = Modifier.padding(16.dp))
            }
        }
    }
}