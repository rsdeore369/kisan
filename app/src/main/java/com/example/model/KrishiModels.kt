package com.example.model

enum class UserRole {
  FARMER,
  BUYER,
  UNSELECTED
}

enum class OrderStatus {
  PENDING,
  CONFIRMED,
  DISPATCHED,
  DELIVERED
}

data class CropProduct(
  val id: String,
  val title: String,
  val category: String, // Grains, Vegetables, Fruits, Spices, Organic, Dairy
  val pricePerUnit: Double,
  val unit: String, // e.g. "kg", "quintal", "crate", "litre"
  val availableQuantity: Double,
  val farmerName: String,
  val farmerPhone: String,
  val location: String,
  val imageUrl: String,
  val description: String,
  val isOrganic: Boolean,
  val rating: Float,
  val harvestDate: String
)

data class OrderItem(
  val id: String,
  val productId: String,
  val productTitle: String,
  val quantity: Double,
  val unit: String,
  val totalPrice: Double,
  val buyerName: String,
  val farmerName: String,
  val status: OrderStatus,
  val date: String,
  val deliveryAddress: String
)

data class WeatherInfo(
  val temperature: Int,
  val condition: String,
  val humidity: Int,
  val rainfallChance: Int,
  val advisory: String
)

data class ChatMessage(
  val id: String,
  val senderName: String,
  val text: String,
  val timestamp: String,
  val isFromUser: Boolean
)
