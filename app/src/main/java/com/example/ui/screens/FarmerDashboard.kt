package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.model.CropProduct
import com.example.model.OrderItem
import com.example.model.OrderStatus
import com.example.model.WeatherInfo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FarmerDashboard(
  farmerName: String,
  crops: List<CropProduct>,
  orders: List<OrderItem>,
  weather: WeatherInfo,
  onAddCropClick: () -> Unit,
  onNavigateToAI: () -> Unit,
  onUpdateOrderStatus: (String, OrderStatus) -> Unit,
  onSwitchRole: () -> Unit
) {
  var selectedTab by remember { mutableIntStateOf(0) } // 0: Crops, 1: Orders
  val myCrops = crops.filter { it.farmerName.equals(farmerName, ignoreCase = true) || true }
  val myOrders = orders.filter { it.farmerName.equals(farmerName, ignoreCase = true) || true }

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
              modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary),
              contentAlignment = Alignment.Center
            ) {
              Icon(Icons.Default.Agriculture, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
              Text(text = "Kisan Growth • $farmerName", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
              Text(text = "Verified Producer (Karnal)", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.primary)
            }
          }
        },
        actions = {
          IconButton(onClick = onSwitchRole, modifier = Modifier.testTag("switch_role_button")) {
            Icon(Icons.Default.SwapHoriz, contentDescription = "Switch Role", tint = MaterialTheme.colorScheme.primary)
          }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
      )
    },
    floatingActionButton = {
      FloatingActionButton(
        onClick = onAddCropClick,
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = Color.White,
        modifier = Modifier.testTag("add_crop_fab")
      ) {
        Icon(Icons.Default.Add, contentDescription = "Add Crop")
      }
    }
  ) { innerPadding ->
    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
        .background(MaterialTheme.colorScheme.background),
      contentPadding = PaddingHorizontal(16.dp),
      verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      item {
        Spacer(modifier = Modifier.height(4.dp))
        // Kisan Growth Banner Card
        Card(
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
        ) {
          Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Box(
              modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.secondary),
              contentAlignment = Alignment.Center
            ) {
              Icon(Icons.Default.TrendingUp, contentDescription = null, tint = Color.White)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = "Kisan Growth Program",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSecondaryContainer
              )
              Spacer(modifier = Modifier.height(2.dp))
              Text(
                text = "+34% profit increase via direct wholesale buyer connection",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.8f)
              )
            }
          }
        }
      }

      item {
        // Weather & AI Advisory Card
        Card(
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.WbSunny, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                  text = "${weather.temperature}°C • ${weather.condition}",
                  style = MaterialTheme.typography.titleMedium,
                  fontWeight = FontWeight.Bold,
                  color = MaterialTheme.colorScheme.onPrimaryContainer
                )
              }
              TextButton(onClick = onNavigateToAI) {
                Icon(Icons.Default.Psychology, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("AI Advisor")
              }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
              text = weather.advisory,
              style = MaterialTheme.typography.bodyMedium,
              color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.9f)
            )
          }
        }
      }

      // Quick Stats
      item {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
          StatCard(
            title = "Active Crops",
            value = myCrops.size.toString(),
            icon = Icons.Default.Inventory,
            modifier = Modifier.weight(1f)
          )
          StatCard(
            title = "Orders",
            value = myOrders.size.toString(),
            icon = Icons.Default.LocalShipping,
            modifier = Modifier.weight(1f)
          )
          StatCard(
            title = "Est. Revenue",
            value = "₹45.2K",
            icon = Icons.Default.CurrencyRupee,
            modifier = Modifier.weight(1f)
          )
        }
      }

      // Tab selection
      item {
        TabRow(
          selectedTabIndex = selectedTab,
          containerColor = MaterialTheme.colorScheme.surface,
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
        ) {
          Tab(
            selected = selectedTab == 0,
            onClick = { selectedTab = 0 },
            text = { Text("My Crops (${myCrops.size})", fontWeight = FontWeight.Bold) }
          )
          Tab(
            selected = selectedTab == 1,
            onClick = { selectedTab = 1 },
            text = { Text("Orders (${myOrders.size})", fontWeight = FontWeight.Bold) }
          )
        }
      }

      if (selectedTab == 0) {
        // Crops List
        if (myCrops.isEmpty()) {
          item {
            EmptyStateView(
              icon = Icons.Default.Inventory2,
              title = "No Crops Listed Yet",
              subtitle = "Tap the + button below to list your first harvest."
            )
          }
        } else {
          items(myCrops) { crop ->
            FarmerCropCard(crop = crop)
          }
        }
      } else {
        // Orders List
        if (myOrders.isEmpty()) {
          item {
            EmptyStateView(
              icon = Icons.Default.Assignment,
              title = "No Orders Received",
              subtitle = "Orders from wholesale buyers will appear here."
            )
          }
        } else {
          items(myOrders) { order ->
            FarmerOrderCard(order = order, onUpdateStatus = onUpdateOrderStatus)
          }
        }
      }

      item {
        Spacer(modifier = Modifier.height(32.dp))
      }
    }
  }
}

@Composable
fun StatCard(title: String, value: String, icon: androidx.compose.ui.graphics.vector.ImageVector, modifier: Modifier = Modifier) {
  Card(
    modifier = modifier,
    shape = RoundedCornerShape(14.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
  ) {
    Column(
      modifier = Modifier.padding(14.dp),
      horizontalAlignment = Alignment.Start
    ) {
      Icon(imageVector = icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(24.dp))
      Spacer(modifier = Modifier.height(8.dp))
      Text(text = value, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
      Text(text = title, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
  }
}

@Composable
fun FarmerCropCard(crop: CropProduct) {
  Card(
    modifier = Modifier.fillMaxWidth(),
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Box(
        modifier = Modifier
          .size(64.dp)
          .clip(RoundedCornerShape(12.dp))
          .background(MaterialTheme.colorScheme.surfaceVariant),
        contentAlignment = Alignment.Center
      ) {
        AsyncImage(
          model = crop.imageUrl,
          contentDescription = crop.title,
          modifier = Modifier.fillMaxSize(),
          contentScale = ContentScale.Crop
        )
      }
      Spacer(modifier = Modifier.width(16.dp))
      Column(modifier = Modifier.weight(1f)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Text(text = crop.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
          if (crop.isOrganic) {
            Spacer(modifier = Modifier.width(6.dp))
            Surface(
              color = MaterialTheme.colorScheme.primaryContainer,
              shape = RoundedCornerShape(4.dp)
            ) {
              Text(
                text = "Organic",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
              )
            }
          }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
          text = "₹${crop.pricePerUnit} / ${crop.unit} • ${crop.availableQuantity.toInt()} ${crop.unit} available",
          style = MaterialTheme.typography.bodyMedium,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
          text = "Location: ${crop.location}",
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.primary
        )
      }
    }
  }
}

@Composable
fun FarmerOrderCard(order: OrderItem, onUpdateStatus: (String, OrderStatus) -> Unit) {
  Card(
    modifier = Modifier.fillMaxWidth(),
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
  ) {
    Column(modifier = Modifier.padding(16.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(text = order.id, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
        StatusBadge(status = order.status)
      }

      Spacer(modifier = Modifier.height(8.dp))

      Text(text = order.productTitle, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
      Text(text = "Quantity: ${order.quantity} ${order.unit} • Total: ₹${order.totalPrice}", style = MaterialTheme.typography.bodyMedium)
      Text(text = "Buyer: ${order.buyerName} (${order.deliveryAddress})", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)

      Spacer(modifier = Modifier.height(12.dp))

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End
      ) {
        if (order.status == OrderStatus.PENDING) {
          OutlinedButton(onClick = { onUpdateStatus(order.id, OrderStatus.CONFIRMED) }) {
            Text("Accept Order")
          }
        } else if (order.status == OrderStatus.CONFIRMED) {
          Button(onClick = { onUpdateStatus(order.id, OrderStatus.DISPATCHED) }) {
            Text("Dispatch")
          }
        } else if (order.status == OrderStatus.DISPATCHED) {
          Button(onClick = { onUpdateStatus(order.id, OrderStatus.DELIVERED) }) {
            Text("Mark Delivered")
          }
        }
      }
    }
  }
}

@Composable
fun StatusBadge(status: OrderStatus) {
  val (color, text) = when (status) {
    OrderStatus.PENDING -> MaterialTheme.colorScheme.secondary to "Pending"
    OrderStatus.CONFIRMED -> MaterialTheme.colorScheme.primary to "Confirmed"
    OrderStatus.DISPATCHED -> Color(0xFF1976D2) to "Dispatched"
    OrderStatus.DELIVERED -> Color(0xFF388E3C) to "Delivered"
  }

  Surface(
    color = color.copy(alpha = 0.15f),
    shape = RoundedCornerShape(8.dp)
  ) {
    Text(
      text = text,
      style = MaterialTheme.typography.labelMedium,
      color = color,
      fontWeight = FontWeight.Bold,
      modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
    )
  }
}

@Composable
fun EmptyStateView(icon: androidx.compose.ui.graphics.vector.ImageVector, title: String, subtitle: String, modifier: Modifier = Modifier) {
  Column(
    modifier = modifier
      .fillMaxWidth()
      .padding(40.dp),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center
  ) {
    Icon(imageVector = icon, contentDescription = null, modifier = Modifier.size(64.dp), tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f))
    Spacer(modifier = Modifier.height(16.dp))
    Text(text = title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
    Spacer(modifier = Modifier.height(6.dp))
    Text(text = subtitle, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, textAlign = androidx.compose.ui.text.style.TextAlign.Center)
  }
}

fun PaddingHorizontal(dp: androidx.compose.ui.unit.Dp): PaddingValues = PaddingValues(horizontal = dp, vertical = dp)
