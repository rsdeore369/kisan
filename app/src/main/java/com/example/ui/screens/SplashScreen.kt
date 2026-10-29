package com.example.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Agriculture
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SplashScreen(onGetStarted: () -> Unit) {
  val infiniteTransition = rememberInfiniteTransition(label = "pulse")
  val scale by infiniteTransition.animateFloat(
    initialValue = 0.95f,
    targetValue = 1.05f,
    animationSpec = infiniteRepeatable(
      animation = tween(1200, easing = FastOutSlowInEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "scale"
  )

  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(
        Brush.verticalGradient(
          colors = listOf(
            MaterialTheme.colorScheme.primaryContainer,
            MaterialTheme.colorScheme.primary,
            MaterialTheme.colorScheme.primaryContainer
          )
        )
      )
      .padding(24.dp),
    contentAlignment = Alignment.Center
  ) {
    Column(
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Center,
      modifier = Modifier.fillMaxWidth()
    ) {
      Box(
        modifier = Modifier
          .size(110.dp)
          .scale(scale)
          .clip(CircleShape)
          .background(MaterialTheme.colorScheme.surface),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = Icons.Default.Agriculture,
          contentDescription = "KrishiSetu Logo",
          tint = MaterialTheme.colorScheme.primary,
          modifier = Modifier.size(64.dp)
        )
      }

      Spacer(modifier = Modifier.height(28.dp))

      Text(
        text = "Kisan Growth",
        style = MaterialTheme.typography.headlineLarge,
        fontWeight = FontWeight.Bold,
        color = Color.White,
        letterSpacing = 1.5.sp
      )

      Spacer(modifier = Modifier.height(8.dp))

      Surface(
        color = Color.White.copy(alpha = 0.2f),
        shape = RoundedCornerShape(16.dp)
      ) {
        Text(
          text = "Direct Farm-to-Table Marketplace",
          style = MaterialTheme.typography.bodyMedium,
          color = Color.White,
          modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
          fontWeight = FontWeight.Medium
        )
      }

      Spacer(modifier = Modifier.height(48.dp))

      Text(
        text = "Empowering farmers with transparent pricing, direct wholesale buyers, and AI crop advisory.",
        style = MaterialTheme.typography.bodyLarge,
        color = Color.White.copy(alpha = 0.9f),
        textAlign = TextAlign.Center,
        modifier = Modifier.padding(horizontal = 16.dp)
      )

      Spacer(modifier = Modifier.height(64.dp))

      Button(
        onClick = onGetStarted,
        modifier = Modifier
          .fillMaxWidth()
          .height(56.dp)
          .testTag("get_started_button"),
        colors = ButtonDefaults.buttonColors(
          containerColor = MaterialTheme.colorScheme.secondary,
          contentColor = MaterialTheme.colorScheme.onSecondary
        ),
        shape = RoundedCornerShape(28.dp),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp)
      ) {
        Icon(
          imageVector = Icons.Default.Eco,
          contentDescription = null,
          modifier = Modifier.size(22.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
          text = "Get Started",
          style = MaterialTheme.typography.titleMedium,
          fontWeight = FontWeight.Bold
        )
      }
    }
  }
}
