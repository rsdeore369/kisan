package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.UserRole

@Composable
fun OnboardingScreen(onRoleSelected: (UserRole) -> Unit) {
  var step by remember { mutableStateOf(0) }

  val titles = listOf(
    "Direct Farm-to-Table Trading",
    "Fair Prices & Zero Middlemen",
    "AI Crop & Weather Advisory"
  )

  val descriptions = listOf(
    "Connect directly with verified wholesale buyers, retailers, and consumer cooperatives across the nation.",
    "Farmers set their own fair prices, ensuring higher profits without commission agent cuts.",
    "Get real-time weather alerts, pest control guidance, and dynamic market price recommendations."
  )

  val icons = listOf(
    Icons.Default.Storefront,
    Icons.Default.MonetizationOn,
    Icons.Default.Psychology
  )

  Surface(
    modifier = Modifier.fillMaxSize(),
    color = MaterialTheme.colorScheme.background
  ) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(24.dp),
      verticalArrangement = Arrangement.SpaceBetween,
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      // Top indicator
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(top = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "Kisan Growth",
          style = MaterialTheme.typography.titleLarge,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.primary
        )
        if (step < 3) {
          TextButton(onClick = { step = 3 }) {
            Text("Skip", color = MaterialTheme.colorScheme.onSurfaceVariant)
          }
        }
      }

      // Content
      Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
      ) {
        if (step < 3) {
          // Onboarding step
          Box(
            modifier = Modifier
              .size(140.dp)
              .clip(CircleShape)
              .background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = icons[step],
              contentDescription = null,
              modifier = Modifier.size(72.dp),
              tint = MaterialTheme.colorScheme.primary
            )
          }

          Spacer(modifier = Modifier.height(36.dp))

          Text(
            text = titles[step],
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onBackground
          )

          Spacer(modifier = Modifier.height(16.dp))

          Text(
            text = descriptions[step],
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = 16.dp)
          )

          Spacer(modifier = Modifier.height(40.dp))

          // Dots indicator
          Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
          ) {
            repeat(3) { index ->
              Box(
                modifier = Modifier
                  .padding(4.dp)
                  .size(if (index == step) 24.dp else 8.dp, 8.dp)
                  .clip(RoundedCornerShape(4.dp))
                  .background(
                    if (index == step) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.surfaceVariant
                  )
              )
            }
          }
        } else {
          // Role Selection step
          Icon(
            imageVector = Icons.Default.Groups,
            contentDescription = null,
            modifier = Modifier.size(80.dp),
            tint = MaterialTheme.colorScheme.primary
          )

          Spacer(modifier = Modifier.height(24.dp))

          Text(
            text = "Select Your Role",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
          )

          Spacer(modifier = Modifier.height(8.dp))

          Text(
            text = "How will you be using Kisan Growth today?",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )

          Spacer(modifier = Modifier.height(32.dp))

          // Farmer Card
          Card(
            onClick = { onRoleSelected(UserRole.FARMER) },
            modifier = Modifier
              .fillMaxWidth()
              .testTag("role_farmer_card"),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
          ) {
            Row(
              modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth(),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Box(
                modifier = Modifier
                  .size(56.dp)
                  .clip(CircleShape)
                  .background(MaterialTheme.colorScheme.primary),
                contentAlignment = Alignment.Center
              ) {
                Icon(
                  imageVector = Icons.Default.Agriculture,
                  contentDescription = null,
                  tint = Color.White,
                  modifier = Modifier.size(32.dp)
                )
              }
              Spacer(modifier = Modifier.width(16.dp))
              Column(modifier = Modifier.weight(1f)) {
                Text(
                  text = "I am a Farmer / Producer",
                  style = MaterialTheme.typography.titleMedium,
                  fontWeight = FontWeight.Bold,
                  color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                  text = "List your crops, manage orders, get AI advice & sell directly",
                  style = MaterialTheme.typography.bodySmall,
                  color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                )
              }
            }
          }

          Spacer(modifier = Modifier.height(16.dp))

          // Buyer Card
          Card(
            onClick = { onRoleSelected(UserRole.BUYER) },
            modifier = Modifier
              .fillMaxWidth()
              .testTag("role_buyer_card"),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
          ) {
            Row(
              modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth(),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Box(
                modifier = Modifier
                  .size(56.dp)
                  .clip(CircleShape)
                  .background(MaterialTheme.colorScheme.secondary),
                contentAlignment = Alignment.Center
              ) {
                Icon(
                  imageVector = Icons.Default.ShoppingCart,
                  contentDescription = null,
                  tint = Color.White,
                  modifier = Modifier.size(32.dp)
                )
              }
              Spacer(modifier = Modifier.width(16.dp))
              Column(modifier = Modifier.weight(1f)) {
                Text(
                  text = "I am a Buyer / Wholesaler",
                  style = MaterialTheme.typography.titleMedium,
                  fontWeight = FontWeight.Bold,
                  color = MaterialTheme.colorScheme.onSecondaryContainer
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                  text = "Browse fresh marketplace produce, negotiate & place direct orders",
                  style = MaterialTheme.typography.bodySmall,
                  color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.8f)
                )
              }
            }
          }
        }
      }

      // Bottom Action Button
      if (step < 3) {
        Button(
          onClick = { step++ },
          modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
            .testTag("next_onboarding_button"),
          shape = RoundedCornerShape(26.dp)
        ) {
          Text(text = "Continue", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
          Spacer(modifier = Modifier.width(8.dp))
          Icon(imageVector = Icons.Default.ArrowForward, contentDescription = null)
        }
      } else {
        Spacer(modifier = Modifier.height(20.dp))
      }
    }
  }
}
