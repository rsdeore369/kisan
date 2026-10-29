package com.example.data

import com.example.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

object KrishiRepository {
  private val _userRole = MutableStateFlow(UserRole.UNSELECTED)
  val userRole: StateFlow<UserRole> = _userRole.asStateFlow()

  private val _userName = MutableStateFlow("Rajesh Kumar")
  val userName: StateFlow<String> = _userName.asStateFlow()

  private val _crops = MutableStateFlow(
    listOf(
      CropProduct(
        id = "c1",
        title = "Organic Basmati Rice",
        category = "Grains",
        pricePerUnit = 85.0,
        unit = "kg",
        availableQuantity = 1200.0,
        farmerName = "Rajesh Kumar",
        farmerPhone = "+91 98765 43210",
        location = "Karnal, Haryana",
        imageUrl = "https://images.unsplash.com/photo-1586201375761-83865001e31c?auto=format&fit=crop&w=600&q=80",
        description = "Aromatic long-grain organic Basmati rice harvested naturally without chemical fertilizers.",
        isOrganic = true,
        rating = 4.8f,
        harvestDate = "24 Oct 2026"
      ),
      CropProduct(
        id = "c2",
        title = "Fresh Alphonso Mangoes",
        category = "Fruits",
        pricePerUnit = 180.0,
        unit = "kg",
        availableQuantity = 450.0,
        farmerName = "Suresh Patil",
        farmerPhone = "+91 91234 56789",
        location = "Ratnagiri, Maharashtra",
        imageUrl = "https://images.unsplash.com/photo-1553279768-865429fa0078?auto=format&fit=crop&w=600&q=80",
        description = "Juicy, sweet, tree-ripened export quality Alphonso mangoes directly from orchard.",
        isOrganic = true,
        rating = 4.9f,
        harvestDate = "26 Oct 2026"
      ),
      CropProduct(
        id = "c3",
        title = "Farm Fresh Red Onions",
        category = "Vegetables",
        pricePerUnit = 32.0,
        unit = "kg",
        availableQuantity = 3500.0,
        farmerName = "Rameshwar Singh",
        farmerPhone = "+91 99887 76655",
        location = "Nashik, Maharashtra",
        imageUrl = "https://images.unsplash.com/photo-1618512495648-7004d7d6f517?auto=format&fit=crop&w=600&q=80",
        description = "Pungent, premium quality red onions sorted and graded for long shelf life.",
        isOrganic = false,
        rating = 4.6f,
        harvestDate = "25 Oct 2026"
      ),
      CropProduct(
        id = "c4",
        title = "Pure Organic Turmeric Powder",
        category = "Spices",
        pricePerUnit = 220.0,
        unit = "kg",
        availableQuantity = 200.0,
        farmerName = "Anita Devi",
        farmerPhone = "+91 97766 55443",
        location = "Erode, Tamil Nadu",
        imageUrl = "https://images.unsplash.com/photo-1615485500704-8e990f9900f1?auto=format&fit=crop&w=600&q=80",
        description = "High curcumin content natural sun-dried turmeric powder stone-ground.",
        isOrganic = true,
        rating = 4.9f,
        harvestDate = "20 Oct 2026"
      ),
      CropProduct(
        id = "c5",
        title = "Fresh Farm Milk (A2 Cow)",
        category = "Dairy",
        pricePerUnit = 65.0,
        unit = "litre",
        availableQuantity = 300.0,
        farmerName = "Baldev Yadav",
        farmerPhone = "+91 94433 22110",
        location = "Anand, Gujarat",
        imageUrl = "https://images.unsplash.com/photo-1550583724-b2692b85b150?auto=format&fit=crop&w=600&q=80",
        description = "Pure A2 desi cow milk delivered chilled within hours of milking.",
        isOrganic = true,
        rating = 4.7f,
        harvestDate = "Today"
      ),
      CropProduct(
        id = "c6",
        title = "Golden Wheat (Lokwan)",
        category = "Grains",
        pricePerUnit = 38.0,
        unit = "kg",
        availableQuantity = 5000.0,
        farmerName = "Rajesh Kumar",
        farmerPhone = "+91 98765 43210",
        location = "Karnal, Haryana",
        imageUrl = "https://images.unsplash.com/photo-1574323347407-f5e1ad6d020b?auto=format&fit=crop&w=600&q=80",
        description = "Cleaned and graded golden wheat grains ideal for soft rotis and baking.",
        isOrganic = false,
        rating = 4.5f,
        harvestDate = "15 Oct 2026"
      )
    )
  )
  val crops: StateFlow<List<CropProduct>> = _crops.asStateFlow()

  private val _orders = MutableStateFlow(
    listOf(
      OrderItem(
        id = "ORD-101",
        productId = "c1",
        productTitle = "Organic Basmati Rice",
        quantity = 50.0,
        unit = "kg",
        totalPrice = 4250.0,
        buyerName = "Green Leaf Grocers",
        farmerName = "Rajesh Kumar",
        status = OrderStatus.CONFIRMED,
        date = "27 Oct 2026",
        deliveryAddress = "Sector 18, Gurugram, Haryana"
      ),
      OrderItem(
        id = "ORD-102",
        productId = "c3",
        productTitle = "Farm Fresh Red Onions",
        quantity = 200.0,
        unit = "kg",
        totalPrice = 6400.0,
        buyerName = "Metro Supermarket",
        farmerName = "Rajesh Kumar",
        status = OrderStatus.PENDING,
        date = "28 Oct 2026",
        deliveryAddress = "Andheri East, Mumbai, Maharashtra"
      )
    )
  )
  val orders: StateFlow<List<OrderItem>> = _orders.asStateFlow()

  private val _weather = MutableStateFlow(
    WeatherInfo(
      temperature = 28,
      condition = "Sunny & Clear",
      humidity = 58,
      rainfallChance = 10,
      advisory = "Optimal weather for harvesting and drying grains. Ensure morning irrigation."
    )
  )
  val weather: StateFlow<WeatherInfo> = _weather.asStateFlow()

  private val _chatMessages = MutableStateFlow(
    listOf(
      ChatMessage(
        id = "m1",
        senderName = "Krishi AI Advisor",
        text = "Hello! I am your AI agricultural assistant. Ask me anything about crop diseases, market prices, or weather advisories.",
        timestamp = "10:00 AM",
        isFromUser = false
      )
    )
  )
  val chatMessages: StateFlow<List<ChatMessage>> = _chatMessages.asStateFlow()

  fun setUserRole(role: UserRole) {
    _userRole.value = role
  }

  fun setUserName(name: String) {
    _userName.value = name
  }

  fun addCrop(product: CropProduct) {
    _crops.update { listOf(product) + it }
  }

  fun placeOrder(order: OrderItem) {
    _orders.update { listOf(order) + it }
  }

  fun updateOrderStatus(orderId: String, newStatus: OrderStatus) {
    _orders.update { list ->
      list.map { if (it.id == orderId) it.copy(status = newStatus) else it }
    }
  }

  fun sendChatMessage(text: String) {
    val userMsg = ChatMessage(
      id = System.currentTimeMillis().toString(),
      senderName = _userName.value,
      text = text,
      timestamp = "Just now",
      isFromUser = true
    )
    _chatMessages.update { it + userMsg }

    // Simulated intelligent response or AI integration
    val aiReplyText = when {
      text.contains("price", ignoreCase = true) || text.contains("rate", ignoreCase = true) ->
        "Current market demand for organic grains is high. Basmati rice is trading at ₹80-90/kg in wholesale mandis."
      text.contains("pest", ignoreCase = true) || text.contains("disease", ignoreCase = true) ->
        "For leaf spot in crops, ensure proper spacing and apply neem-based organic bio-pesticides during early morning hours."
      text.contains("weather", ignoreCase = true) || text.contains("rain", ignoreCase = true) ->
        "Today's forecast shows clear skies with 28°C and low humidity. Perfect for harvesting."
      else ->
        "That's a great farming query! Make sure to maintain soil moisture and consult local Krishi Vigyan Kendra for soil testing kits."
    }

    val aiMsg = ChatMessage(
      id = (System.currentTimeMillis() + 1).toString(),
      senderName = "Krishi AI Advisor",
      text = aiReplyText,
      timestamp = "Just now",
      isFromUser = false
    )
    _chatMessages.update { it + aiMsg }
  }
}
