package com.weatherapp.core.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.weatherapp.core.ui.theme.AppFont
import com.weatherapp.core.ui.theme.LightBlue
import com.weatherapp.core.ui.theme.TextColor

@Composable
fun DefaultButton(
    title: String,
    modifier: Modifier = Modifier,
    backgroundColor: Color = LightBlue,
    textColor: Color = TextColor,
    strokeWidth: Dp = 0.dp,
    fontSize:Int=16,
    strokeColor: Color = Color.Unspecified,
    isEnabled: Boolean = true,
    onClick: () -> Unit = {}
) {
    Button(
        onClick = { onClick() },
        modifier = modifier.defaultMinSize(minHeight = 56.dp),
        shape = RoundedCornerShape(50),
        border = BorderStroke(strokeWidth, strokeColor),
        enabled = isEnabled,
        colors = ButtonDefaults.buttonColors(
            contentColor = textColor,
            containerColor = backgroundColor,
        ),

    ) {
        Text(text = title, fontFamily = AppFont.MontserratFont, fontSize = fontSize.sp, )
    }
}