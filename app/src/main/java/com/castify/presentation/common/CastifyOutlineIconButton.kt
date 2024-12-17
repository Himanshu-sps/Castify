package com.castify.presentation.common

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Border
import androidx.tv.material3.ButtonDefaults
import androidx.tv.material3.Glow
import androidx.tv.material3.Icon
import androidx.tv.material3.LocalContentColor
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.OutlinedButton
import androidx.tv.material3.OutlinedButtonDefaults
import androidx.tv.material3.Text

@Composable
fun CastifyOutlineIconButton(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    buttonText: String,
    roundPercentage: Int = 20,
    isFocusedGlow: Boolean = false,
    onClick: () -> Unit
) {
    OutlinedButton(
        modifier = modifier.fillMaxWidth(),
        onClick = onClick,
        glow = OutlinedButtonDefaults.glow(
            focusedGlow = if (!isFocusedGlow) {
                Glow.None
            } else Glow(elevationColor = LocalContentColor.current, elevation = 5.dp)
        ),
        scale = OutlinedButtonDefaults.scale(
            scale = 1f,
            focusedScale = 1.025f
        ),
        border = OutlinedButtonDefaults.border(
            border = Border(
                shape = RoundedCornerShape(percent = roundPercentage),
                border = BorderStroke(
                    width = 1.dp,
                    color = LocalContentColor.current
                ),
                inset = 0.dp
            ),
            focusedBorder = Border(
                shape = RoundedCornerShape(percent = roundPercentage),
                border = BorderStroke(
                    width = 1.dp,
                    color = LocalContentColor.current
                ),
                inset = 0.dp
            ),
        ),
        shape = OutlinedButtonDefaults.shape(shape = RoundedCornerShape(roundPercentage)),
        contentPadding = ButtonDefaults.ButtonWithIconContentPadding
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null
        )

        Spacer(Modifier.size(8.dp))

        Text(
            text = buttonText,
            style = MaterialTheme.typography.titleSmall
        )
    }
}