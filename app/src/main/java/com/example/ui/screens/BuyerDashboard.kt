package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BuyerDashboard(
  buyerName: String,
  crops: List<CropProduct>,
  orders: List<OrderItem>,
  onProductClick: (CropProduct) -> Unit,
  onNavigateToOrders: () -> Unit,
  onNavigateToAI: () -> Unit,
  onSwitchRole: () -> Unit
) {
  var searchQuery by remember { mutableStateOf("") }
  var selectedCategory by remember { mutableStateOf("All") }

  val categories = listOf("All", "Grains", "Vegetables", "Fruits", "Spices", "Organic", "Dairy")

  val filteredCrops = crops.filter { crop ->
    val matchesSearch = crop.title.contains(searchQuery, ignoreCase = true) ||
      crop.location.contains(searchQuery, ignoreCase = true) ||
      crop.farmerName.contains(searchQuery, ignoreCase = true)
    val matchesCategory = selectedCategory == "All" ||
      crop.category.equals(selectedCategory, ignoreCase = true) ||
      (selectedCategory == "Organic" && crop.isOrganic)
    matchesSearch && matchesCategory
  }

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
              modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.secondary),
              contentAlignment = Alignment.Center
            ) {
              Icon(Icons.Default.ShoppingCart, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
              Text(text = "Kisan Growth Market", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
              Text(text = "Welcome, $buyerName", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.secondary)
            }
          }
        },
        actions = {
          IconButton(onClick = onNavigateToAI, modifier = Modifier.testTag("ai_advisor_btn")) {
            Icon(Icons.Default.Psychology, contentDescription = "AI Advisor", tint = MaterialTheme.colorScheme.primary)
          }
          IconButton(onClick = onSwitchRole, modifier = Modifier.testTag("switch_role_button")) {
            Icon(Icons.Default.SwapHoriz, contentDescription = "Switch Role", tint = MaterialTheme.colorScheme.primary)
          }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
      )
    },
    bottomBar = {
      NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface
      ) {
        NavigationBarItem(
          selected = true,
          onClick = { },
          icon = { Icon(Icons.Default.Storefront, contentDescription = "Market") },
          label = { Text("Market") }
        )
        NavigationBarItem(
          selected = false,
          onClick = onNavigateToOrders,
          icon = { Icon(Icons.Default.LocalShipping, contentDescription = "Orders") },
          label = { Text("Orders") }
        )
        NavigationBarItem(
          selected = false,
          onClick = onNavigateToAI,
          icon = { Icon(Icons.Default.Psychology, contentDescription = "AI Advisor") },
          label = { Text("AI Advisor") }
        )
      }
    }
  ) { innerPadding ->
    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
        .background(MaterialTheme.colorScheme.background),
      contentPadding = PaddingValues(16.dp),
      verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      // Search Bar
      item {
        OutlinedTextField(
          value = searchQuery,
          onValueChange = { searchQuery = it },
          placeholder = { Text("Search crops, location, or farmer...") },
          leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
          trailingIcon = {
            if (searchQuery.isNotEmpty()) {
              IconButton(onClick = { searchQuery = "" }) {
                Icon(Icons.Default.Clear, contentDescription = "Clear")
              }
            }
          },
          modifier = Modifier
            .fillMaxWidth()
            .testTag("search_crops_input"),
          shape = RoundedCornerShape(24.dp),
          colors = OutlinedTextFieldDefaults.colors(
            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
            focusedContainerColor = MaterialTheme.colorScheme.surface
          ),
          singleLine = true
        )
      }

      // Banner Card
      item {
        Card(
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(20.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
        ) {
          Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column(modifier = Modifier.weight(1f)) {
              Surface(
                color = MaterialTheme.colorScheme.secondary,
                shape = RoundedCornerShape(6.dp)
              ) {
                Text(
                  text = "0% Commission",
                  style = MaterialTheme.typography.labelSmall,
                  color = Color.White,
                  modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                  fontWeight = FontWeight.Bold
                )
              }
              Spacer(modifier = Modifier.height(8.dp))
              Text(
                text = "Direct from Verified Farmers",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSecondaryContainer
              )
              Spacer(modifier = Modifier.height(4.dp))
              Text(
                text = "Get fresh agricultural produce at wholesale mandified rates.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.8f)
              )
            }
          }
        }
      }

      // Categories Horizontal Row
      item {
        Text(
          text = "Categories",
          style = MaterialTheme.typography.titleMedium,
          fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
          items(categories) { cat ->
            FilterChip(
              selected = selectedCategory == cat,
              onClick = { selectedCategory = cat },
              label = { Text(cat) },
              colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = MaterialTheme.colorScheme.primary,
                selectedLabelColor = Color.White
              )
            )
          }
        }
      }

      // Products Header
      item {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "Available Harvest (${filteredCrops.size})",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
          )
        }
      }

      // Products list
      if (filteredCrops.isEmpty()) {
        item {
          EmptyStateView(
            icon = Icons.Default.SearchOff,
            title = "No Crops Found",
            subtitle = "Try searching with a different keyword or category."
          )
        }
      } else {
        items(filteredCrops) { crop ->
          MarketplaceProductCard(crop = crop, onClick = { onProductClick(crop) })
        }
      }

      item {
        Spacer(modifier = Modifier.height(32.dp))
      }
    }
  }
}

@Composable
fun MarketplaceProductCard(crop: CropProduct, onClick: () -> Unit) {
  Card(
    onClick = onClick,
    modifier = Modifier
      .fillMaxWidth()
      .testTag("product_card_${crop.id}"),
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(14.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Box(
        modifier = Modifier
          .size(80.dp)
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
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = crop.title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
          )
          if (crop.isOrganic) {
            Surface(
              color = MaterialTheme.colorScheme.primaryContainer,
              shape = RoundedCornerShape(4.dp)
            ) {
              Text(
                text = "Organic",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                fontWeight = FontWeight.Bold
              )
            }
          }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
          text = "₹${crop.pricePerUnit} / ${crop.unit}",
          style = MaterialTheme.typography.titleSmall,
          color = MaterialTheme.colorScheme.primary,
          fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
          text = "Farmer: ${crop.farmerName} • ${crop.location}",
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
          text = "Available: ${crop.availableQuantity.toInt()} ${crop.unit}",
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }
    }
  }
}
