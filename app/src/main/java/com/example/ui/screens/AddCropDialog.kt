package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.model.CropProduct

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCropDialog(
  farmerName: String,
  farmerPhone: String,
  onDismiss: () -> Unit,
  onAddCrop: (CropProduct) -> Unit
) {
  var title by remember { mutableStateOf("") }
  var selectedCategory by remember { mutableStateOf("Grains") }
  var price by remember { mutableStateOf("") }
  var unit by remember { mutableStateOf("kg") }
  var quantity by remember { mutableStateOf("") }
  var location by remember { mutableStateOf("Karnal, Haryana") }
  var description by remember { mutableStateOf("") }
  var isOrganic by remember { mutableStateOf(true) }

  val categories = listOf("Grains", "Vegetables", "Fruits", "Spices", "Organic", "Dairy")
  val units = listOf("kg", "quintal", "crate", "litre", "tonne")

  val sampleImages = mapOf(
    "Grains" to "https://images.unsplash.com/photo-1586201375761-83865001e31c?auto=format&fit=crop&w=600&q=80",
    "Vegetables" to "https://images.unsplash.com/photo-1618512495648-7004d7d6f517?auto=format&fit=crop&w=600&q=80",
    "Fruits" to "https://images.unsplash.com/photo-1553279768-865429fa0078?auto=format&fit=crop&w=600&q=80",
    "Spices" to "https://images.unsplash.com/photo-1615485500704-8e990f9900f1?auto=format&fit=crop&w=600&q=80",
    "Organic" to "https://images.unsplash.com/photo-1540420773420-3366772f4999?auto=format&fit=crop&w=600&q=80",
    "Dairy" to "https://images.unsplash.com/photo-1550583724-b2692b85b150?auto=format&fit=crop&w=600&q=80"
  )

  AlertDialog(
    onDismissRequest = onDismiss,
    title = {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Box(
            modifier = Modifier
              .size(40.dp)
              .clip(CircleShape)
              .background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center
          ) {
            Icon(Icons.Default.AddCircle, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
          }
          Spacer(modifier = Modifier.width(12.dp))
          Text("List New Harvest", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleLarge)
        }
        IconButton(onClick = onDismiss) {
          Icon(Icons.Default.Close, contentDescription = "Close")
        }
      }
    },
    text = {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .verticalScroll(rememberScrollState())
          .padding(vertical = 4.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
      ) {
        // Category Selection Chips
        Text("Select Category", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
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

        OutlinedTextField(
          value = title,
          onValueChange = { title = it },
          label = { Text("Crop Title (e.g. Organic Basmati Rice)") },
          leadingIcon = { Icon(Icons.Default.Eco, contentDescription = null) },
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(12.dp),
          singleLine = true
        )

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          OutlinedTextField(
            value = price,
            onValueChange = { price = it },
            label = { Text("Price (₹)") },
            leadingIcon = { Icon(Icons.Default.CurrencyRupee, contentDescription = null) },
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(12.dp),
            singleLine = true
          )
          OutlinedTextField(
            value = quantity,
            onValueChange = { quantity = it },
            label = { Text("Qty (${unit})") },
            leadingIcon = { Icon(Icons.Default.Inventory, contentDescription = null) },
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(12.dp),
            singleLine = true
          )
        }

        // Unit selector chips
        Text("Unit Type", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
          items(units) { u ->
            FilterChip(
              selected = unit == u,
              onClick = { unit = u },
              label = { Text(u) }
            )
          }
        }

        OutlinedTextField(
          value = location,
          onValueChange = { location = it },
          label = { Text("Location / Mandi Hub") },
          leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = null) },
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(12.dp),
          singleLine = true
        )

        OutlinedTextField(
          value = description,
          onValueChange = { description = it },
          label = { Text("Quality & Description Details") },
          leadingIcon = { Icon(Icons.Default.Description, contentDescription = null) },
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(12.dp),
          maxLines = 3
        )

        Surface(
          color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
          shape = RoundedCornerShape(12.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
          ) {
            Checkbox(
              checked = isOrganic,
              onCheckedChange = { isOrganic = it }
            )
            Column {
              Text("Certified Organic Produce", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
              Text("Highlights your crop with organic badge", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
          }
        }
      }
    },
    confirmButton = {
      Button(
        onClick = {
          if (title.isNotBlank() && price.isNotBlank() && quantity.isNotBlank()) {
            val imageUrl = sampleImages[selectedCategory] ?: sampleImages["Grains"]!!
            val newCrop = CropProduct(
              id = "c_${System.currentTimeMillis()}",
              title = title,
              category = selectedCategory,
              pricePerUnit = price.toDoubleOrNull() ?: 50.0,
              unit = unit,
              availableQuantity = quantity.toDoubleOrNull() ?: 100.0,
              farmerName = farmerName,
              farmerPhone = farmerPhone,
              location = location,
              imageUrl = imageUrl,
              description = description.ifBlank { "Fresh farm produce harvested directly with high quality grading." },
              isOrganic = isOrganic,
              rating = 5.0f,
              harvestDate = "Today"
            )
            onAddCrop(newCrop)
            onDismiss()
          }
        },
        modifier = Modifier
          .fillMaxWidth()
          .height(52.dp),
        shape = RoundedCornerShape(26.dp)
      ) {
        Icon(Icons.Default.Check, contentDescription = null)
        Spacer(modifier = Modifier.width(8.dp))
        Text("Publish Crop Listing", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
      }
    }
  )
}
