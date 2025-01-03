package com.weatherapp.core.ui.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.weatherapp.core.ui.theme.AppFont
import com.weatherapp.core.ui.theme.LightBlue
import com.weatherapp.core.ui.theme.StrokeColor
import com.weatherapp.core.ui.theme.TextColor


@Composable
fun DefaultTextField(
    hint: String,
    modifier: Modifier = Modifier,
    initialText: String = "",
    maxLines: Int = 1,
    cornerRadius: Dp = 46.dp,
    fontSize: Int = 14,
    readOnly: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Next,
    backgroundColor: Color = Color.Transparent,
    focusedBackgroundColor: Color = Color.Unspecified,
    enabled: Boolean = true,
    isLabelEnabled: Boolean = true,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    onClickListener: (() -> Unit)? = null,
    onValueChange: (s: String) -> Unit = { }
) {
    var focusState by remember { mutableStateOf(false) }
    var text by remember { mutableStateOf("") }
    OutlinedTextField(
        value = initialText,
        onValueChange = {
            text = it
            onValueChange(text)
        },
        readOnly = readOnly,
        singleLine = maxLines==1,
        modifier = modifier.onFocusChanged {
            focusState = it.isFocused
        },
        maxLines = maxLines,
        interactionSource = if (onClickListener != null) {
            remember { MutableInteractionSource() }
                .also { interactionSource ->
                    LaunchedEffect(interactionSource) {
                        interactionSource.interactions.collect {
                            if (it is PressInteraction.Release) {
                                onClickListener()
                            }
                        }
                    }
                }
        } else remember { MutableInteractionSource() },
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType, imeAction = imeAction),
        shape = RoundedCornerShape(cornerRadius),
        placeholder = {
            Text(
                text =
                if (isLabelEnabled) hint
                else {
                    if (!focusState) hint else ""
                },
                color = StrokeColor, fontFamily = AppFont.MontserratFont,
                fontSize = fontSize.sp
            )
        },
        textStyle = TextStyle(
            fontSize = fontSize.sp,
            fontFamily = AppFont.MontserratFont
        ),
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = TextColor,
            unfocusedTextColor = TextColor,
            focusedBorderColor = LightBlue,
            unfocusedBorderColor = StrokeColor,
            unfocusedContainerColor = backgroundColor,
            focusedContainerColor = focusedBackgroundColor
        ),
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        enabled = enabled,
    )
}

