const express = require('express');
const cors = require('cors');
const path = require('path');
const fs = require('fs');
require('dotenv').config();

const app = express();
const PORT = process.env.PORT || 5000;

app.use(cors());
app.use(express.json());
app.use(express.static(path.join(__dirname, 'public')));

// In-Memory Data Store (matching KrishiRepository.kt)
let crops = [
  {
    id: "c1",
    title: "Organic Basmati Rice",
    category: "Grains",
    pricePerUnit: 85.0,
    unit: "kg",
    availableQuantity: 1200.0,
    farmerName: "Rajesh Kumar",
    farmerPhone: "+91 98765 43210",
    location: "Karnal, Haryana",
    imageUrl: "https://images.unsplash.com/photo-1586201375761-83865001e31c?auto=format&fit=crop&w=600&q=80",
    description: "Aromatic long-grain organic Basmati rice harvested naturally without chemical fertilizers.",
    isOrganic: true,
    rating: 4.8,
    harvestDate: "24 Oct 2026"
  },
  {
    id: "c2",
    title: "Fresh Alphonso Mangoes",
    category: "Fruits",
    pricePerUnit: 180.0,
    unit: "kg",
    availableQuantity: 450.0,
    farmerName: "Suresh Patil",
    farmerPhone: "+91 91234 56789",
    location: "Ratnagiri, Maharashtra",
    imageUrl: "https://images.unsplash.com/photo-1553279768-865429fa0078?auto=format&fit=crop&w=600&q=80",
    description: "Juicy, sweet, tree-ripened export quality Alphonso mangoes directly from orchard.",
    isOrganic: true,
    rating: 4.9,
    harvestDate: "26 Oct 2026"
  },
  {
    id: "c3",
    title: "Farm Fresh Red Onions",
    category: "Vegetables",
    pricePerUnit: 32.0,
    unit: "kg",
    availableQuantity: 3500.0,
    farmerName: "Rameshwar Singh",
    farmerPhone: "+91 99887 76655",
    location: "Nashik, Maharashtra",
    imageUrl: "https://images.unsplash.com/photo-1618512495648-7004d7d6f517?auto=format&fit=crop&w=600&q=80",
    description: "Pungent, premium quality red onions sorted and graded for long shelf life.",
    isOrganic: false,
    rating: 4.6,
    harvestDate: "25 Oct 2026"
  },
  {
    id: "c4",
    title: "Pure Organic Turmeric Powder",
    category: "Spices",
    pricePerUnit: 220.0,
    unit: "kg",
    availableQuantity: 200.0,
    farmerName: "Anita Devi",
    farmerPhone: "+91 97766 55443",
    location: "Erode, Tamil Nadu",
    imageUrl: "https://images.unsplash.com/photo-1615485500704-8e990f9900f1?auto=format&fit=crop&w=600&q=80",
    description: "High curcumin content natural sun-dried turmeric powder stone-ground.",
    isOrganic: true,
    rating: 4.9,
    harvestDate: "20 Oct 2026"
  },
  {
    id: "c5",
    title: "Fresh Farm Milk (A2 Cow)",
    category: "Dairy",
    pricePerUnit: 65.0,
    unit: "litre",
    availableQuantity: 300.0,
    farmerName: "Baldev Yadav",
    farmerPhone: "+91 94433 22110",
    location: "Anand, Gujarat",
    imageUrl: "https://images.unsplash.com/photo-1550583724-b2692b85b150?auto=format&fit=crop&w=600&q=80",
    description: "Pure A2 desi cow milk delivered chilled within hours of milking.",
    isOrganic: true,
    rating: 4.7,
    harvestDate: "Today"
  },
  {
    id: "c6",
    title: "Golden Wheat (Lokwan)",
    category: "Grains",
    pricePerUnit: 38.0,
    unit: "kg",
    availableQuantity: 5000.0,
    farmerName: "Rajesh Kumar",
    farmerPhone: "+91 98765 43210",
    location: "Karnal, Haryana",
    imageUrl: "https://images.unsplash.com/photo-1574323347407-f5e1ad6d020b?auto=format&fit=crop&w=600&q=80",
    description: "Cleaned and graded golden wheat grains ideal for soft rotis and baking.",
    isOrganic: false,
    rating: 4.5,
    harvestDate: "15 Oct 2026"
  }
];

let orders = [
  {
    id: "ORD-101",
    productId: "c1",
    productTitle: "Organic Basmati Rice",
    quantity: 50.0,
    unit: "kg",
    totalPrice: 4250.0,
    buyerName: "Green Leaf Grocers",
    farmerName: "Rajesh Kumar",
    status: "CONFIRMED",
    date: "27 Oct 2026",
    deliveryAddress: "Sector 18, Gurugram, Haryana"
  },
  {
    id: "ORD-102",
    productId: "c3",
    productTitle: "Farm Fresh Red Onions",
    quantity: 200.0,
    unit: "kg",
    totalPrice: 6400.0,
    buyerName: "Metro Supermarket",
    farmerName: "Rajesh Kumar",
    status: "PENDING",
    date: "28 Oct 2026",
    deliveryAddress: "Andheri East, Mumbai, Maharashtra"
  }
];

const weather = {
  temperature: 28,
  condition: "Sunny & Clear",
  humidity: 58,
  rainfallChance: 10,
  advisory: "Optimal weather for harvesting and drying grains. Ensure morning irrigation."
};

// API: Get Crops
app.get('/api/crops', (req, res) => {
  res.json(crops);
});

// API: Add Crop
app.post('/api/crops', (req, res) => {
  const newCrop = {
    id: "c" + (crops.length + 1) + "_" + Date.now().toString(36),
    title: req.body.title || "Untitled Crop",
    category: req.body.category || "Grains",
    pricePerUnit: parseFloat(req.body.pricePerUnit) || 0,
    unit: req.body.unit || "kg",
    availableQuantity: parseFloat(req.body.availableQuantity) || 0,
    farmerName: req.body.farmerName || "Rajesh Kumar",
    farmerPhone: req.body.farmerPhone || "+91 98765 43210",
    location: req.body.location || "Haryana, India",
    imageUrl: req.body.imageUrl || "https://images.unsplash.com/photo-1500937386664-56d1dfef3854?auto=format&fit=crop&w=600&q=80",
    description: req.body.description || "Farm fresh produce",
    isOrganic: !!req.body.isOrganic,
    rating: 5.0,
    harvestDate: "Fresh Harvest"
  };
  crops.unshift(newCrop);
  res.status(201).json(newCrop);
});

// API: Get Orders
app.get('/api/orders', (req, res) => {
  res.json(orders);
});

// API: Place Order
app.post('/api/orders', (req, res) => {
  const newOrder = {
    id: "ORD-" + Math.floor(100 + Math.random() * 900),
    productId: req.body.productId,
    productTitle: req.body.productTitle,
    quantity: parseFloat(req.body.quantity) || 1,
    unit: req.body.unit || "kg",
    totalPrice: parseFloat(req.body.totalPrice) || 0,
    buyerName: req.body.buyerName || "Retail Buyer",
    farmerName: req.body.farmerName || "Farmer",
    status: "PENDING",
    date: new Date().toLocaleDateString('en-GB', { day: 'numeric', month: 'short', year: 'numeric' }),
    deliveryAddress: req.body.deliveryAddress || "Standard Delivery"
  };

  // Reduce available crop quantity
  const crop = crops.find(c => c.id === newOrder.productId);
  if (crop && crop.availableQuantity >= newOrder.quantity) {
    crop.availableQuantity -= newOrder.quantity;
  }

  orders.unshift(newOrder);
  res.status(201).json(newOrder);
});

// API: Update Order Status
app.patch('/api/orders/:id/status', (req, res) => {
  const { id } = req.params;
  const { status } = req.body;
  const order = orders.find(o => o.id === id);
  if (!order) {
    return res.status(404).json({ error: "Order not found" });
  }
  order.status = status;
  res.json(order);
});

// API: Weather info
app.get('/api/weather', (req, res) => {
  res.json(weather);
});

// API: AI Crop Advisor Chat
app.post('/api/chat', async (req, res) => {
  const { message } = req.body;
  if (!message) {
    return res.status(400).json({ error: "Message is required" });
  }

  const text = message.toLowerCase();
  let aiReplyText = "";

  if (text.includes("price") || text.includes("rate") || text.includes("mandi")) {
    aiReplyText = "Current mandi trends: Organic Basmati rice is trading high at ₹80-92/kg. Red onions in Nashik are holding firm at ₹30-35/kg. Demand for pulses and oilseeds is expected to rise next week.";
  } else if (text.includes("pest") || text.includes("disease") || text.includes("fungus") || text.includes("spot")) {
    aiReplyText = "For leaf spot and fungal pests in field crops: Spray Neem Oil (5ml/liter) or Panchagavya solution during early morning. Ensure proper spacing between rows to avoid excessive moisture accumulation.";
  } else if (text.includes("weather") || text.includes("rain") || text.includes("temp")) {
    aiReplyText = "Current conditions are 28°C and Sunny. Humidity is 58% with very low rainfall risk (10%). Excellent window for crop harvesting, field drying, and light morning irrigation.";
  } else if (text.includes("organic") || text.includes("fertilizer") || text.includes("manure")) {
    aiReplyText = "Organic tips: Use vermicompost (2-3 tonnes/acre) enriched with Trichoderma before sowing. Jeevamrut applied every 15 days increases microbial activity and soil water retention.";
  } else if (text.includes("wheat") || text.includes("sowing") || text.includes("season")) {
    aiReplyText = "For Rabi season wheat sowing, prepare well-drained loam soil with 2-3 deep ploughings. Recommended seed rate is 40-45 kg/acre. Seed treatment with Azotobacter improves germination rate.";
  } else {
    aiReplyText = `Great question on "${message}". To maximize crop yield and fetch top market prices, ensure soil testing every 2 seasons, adopt drip irrigation where feasible, and list your harvest early on Kisan Growth for pre-orders!`;
  }

  res.json({
    senderName: "Krishi AI Advisor",
    text: aiReplyText,
    timestamp: new Date().toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })
  });
});

// API: Download APK file
app.get('/download-apk', (req, res) => {
  const apkPath = path.join(__dirname, 'va.apk');
  if (fs.existsSync(apkPath)) {
    res.download(apkPath, 'KisanGrowth.apk');
  } else {
    res.status(404).send('APK file not found.');
  }
});

// Catch-all to serve index.html
app.get('*', (req, res) => {
  res.sendFile(path.join(__dirname, 'public', 'index.html'));
});

app.listen(PORT, '0.0.0.0', () => {
  console.log(`Kisan Growth app server listening on port ${PORT}`);
});

