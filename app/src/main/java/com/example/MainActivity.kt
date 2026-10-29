package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.data.KrishiRepository
import com.example.model.CropProduct
import com.example.model.OrderStatus
import com.example.model.UserRole
import com.example.ui.screens.*
import com.example.ui.theme.MyApplicationTheme

enum class Screen {
  SPLASH,
  ONBOARDING,
  FARMER_DASHBOARD,
  BUYER_DASHBOARD,
  PRODUCT_DETAIL,
  ORDERS,
  AI_ADVISOR
}

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      MyApplicationTheme {
        Surface(
          modifier = Modifier.fillMaxSize(),
          color = MaterialTheme.colorScheme.background
        ) {
          KrishiSetuApp()
        }
      }
    }
  }
}

@Composable
fun KrishiSetuApp() {
  var currentScreen by remember { mutableStateOf(Screen.SPLASH) }
  val userRole by KrishiRepository.userRole.collectAsState()
  val userName by KrishiRepository.userName.collectAsState()
  val crops by KrishiRepository.crops.collectAsState()
  val orders by KrishiRepository.orders.collectAsState()
  val weather by KrishiRepository.weather.collectAsState()
  val chatMessages by KrishiRepository.chatMessages.collectAsState()

  var selectedProduct by remember { mutableStateOf<CropProduct?>(null) }
  var showAddCropDialog by remember { mutableStateOf(false) }

  BackHandler(enabled = currentScreen != Screen.SPLASH) {
    when (currentScreen) {
      Screen.PRODUCT_DETAIL -> currentScreen = Screen.BUYER_DASHBOARD
      Screen.ORDERS -> currentScreen = if (userRole == UserRole.FARMER) Screen.FARMER_DASHBOARD else Screen.BUYER_DASHBOARD
      Screen.AI_ADVISOR -> currentScreen = if (userRole == UserRole.FARMER) Screen.FARMER_DASHBOARD else Screen.BUYER_DASHBOARD
      Screen.FARMER_DASHBOARD, Screen.BUYER_DASHBOARD -> currentScreen = Screen.ONBOARDING
      else -> currentScreen = Screen.SPLASH
    }
  }

  when (currentScreen) {
    Screen.SPLASH -> {
      SplashScreen(
        onGetStarted = {
          currentScreen = Screen.ONBOARDING
        }
      )
    }

    Screen.ONBOARDING -> {
      OnboardingScreen(
        onRoleSelected = { role ->
          KrishiRepository.setUserRole(role)
          currentScreen = if (role == UserRole.FARMER) Screen.FARMER_DASHBOARD else Screen.BUYER_DASHBOARD
        }
      )
    }

    Screen.FARMER_DASHBOARD -> {
      FarmerDashboard(
        farmerName = userName,
        crops = crops,
        orders = orders,
        weather = weather,
        onAddCropClick = { showAddCropDialog = true },
        onNavigateToAI = { currentScreen = Screen.AI_ADVISOR },
        onUpdateOrderStatus = { orderId, status ->
          KrishiRepository.updateOrderStatus(orderId, status)
        },
        onSwitchRole = {
          currentScreen = Screen.ONBOARDING
        }
      )
    }

    Screen.BUYER_DASHBOARD -> {
      BuyerDashboard(
        buyerName = userName,
        crops = crops,
        orders = orders,
        onProductClick = { product ->
          selectedProduct = product
          currentScreen = Screen.PRODUCT_DETAIL
        },
        onNavigateToOrders = { currentScreen = Screen.ORDERS },
        onNavigateToAI = { currentScreen = Screen.AI_ADVISOR },
        onSwitchRole = {
          currentScreen = Screen.ONBOARDING
        }
      )
    }

    Screen.PRODUCT_DETAIL -> {
      selectedProduct?.let { product ->
        ProductDetailScreen(
          product = product,
          buyerName = userName,
          onBack = { currentScreen = Screen.BUYER_DASHBOARD },
          onPlaceOrder = { newOrder ->
            KrishiRepository.placeOrder(newOrder)
          }
        )
      } ?: run {
        currentScreen = Screen.BUYER_DASHBOARD
      }
    }

    Screen.ORDERS -> {
      OrdersScreen(
        orders = orders,
        onBack = {
          currentScreen = if (userRole == UserRole.FARMER) Screen.FARMER_DASHBOARD else Screen.BUYER_DASHBOARD
        }
      )
    }

    Screen.AI_ADVISOR -> {
      AICropAdvisorScreen(
        messages = chatMessages,
        onSendMessage = { text ->
          KrishiRepository.sendChatMessage(text)
        },
        onBack = {
          currentScreen = if (userRole == UserRole.FARMER) Screen.FARMER_DASHBOARD else Screen.BUYER_DASHBOARD
        }
      )
    }
  }

  if (showAddCropDialog) {
    AddCropDialog(
      farmerName = userName,
      farmerPhone = "+91 98765 43210",
      onDismiss = { showAddCropDialog = false },
      onAddCrop = { newCrop ->
        KrishiRepository.addCrop(newCrop)
      }
    )
  }
}
