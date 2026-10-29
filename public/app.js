// Kisan Growth Client-Side Application

let currentRole = 'BUYER'; // 'BUYER' or 'FARMER'
let currentCategory = 'All';
let allCrops = [];
let allOrders = [];
let selectedCropForBuy = null;

// Initialize App
document.addEventListener('DOMContentLoaded', () => {
  loadWeather();
  loadCrops();
  loadOrders();
});

// Switch Views
function switchView(viewName) {
  const views = ['marketplace', 'farmer', 'orders', 'advisor'];
  views.forEach(v => {
    const el = document.getElementById(`view-${v}`);
    const navBtn = document.getElementById(`nav-${v}`);
    if (el) {
      if (v === viewName) {
        el.classList.remove('hidden');
      } else {
        el.classList.add('hidden');
      }
    }
    if (navBtn) {
      if (v === viewName) {
        navBtn.className = "nav-btn px-3 py-2 rounded-lg text-sm font-semibold text-brand-700 bg-brand-50 flex items-center space-x-2 transition";
      } else {
        navBtn.className = "nav-btn px-3 py-2 rounded-lg text-sm font-medium text-gray-600 hover:text-brand-700 hover:bg-gray-100 flex items-center space-x-2 transition";
      }
    }
  });

  window.scrollTo({ top: 0, behavior: 'smooth' });
  lucide.createIcons();
}

// Switch Role
function setRole(role) {
  currentRole = role;
  const buyerBtn = document.getElementById('role-buyer-btn');
  const farmerBtn = document.getElementById('role-farmer-btn');

  if (role === 'BUYER') {
    buyerBtn.className = "px-3 py-1.5 rounded-lg font-semibold bg-white text-gray-800 shadow-sm transition";
    farmerBtn.className = "px-3 py-1.5 rounded-lg font-semibold text-gray-500 hover:text-gray-800 transition";
    switchView('marketplace');
  } else {
    farmerBtn.className = "px-3 py-1.5 rounded-lg font-semibold bg-white text-gray-800 shadow-sm transition";
    buyerBtn.className = "px-3 py-1.5 rounded-lg font-semibold text-gray-500 hover:text-gray-800 transition";
    switchView('farmer');
  }
}

// Scroll to Crops
function scrollToCrops() {
  document.getElementById('crops-section').scrollIntoView({ behavior: 'smooth' });
}

// Load Weather
async function loadWeather() {
  try {
    const res = await fetch('/api/weather');
    if (res.ok) {
      const data = await res.json();
      document.getElementById('weather-temp').textContent = `${data.temperature}°C ${data.condition}`;
      document.getElementById('weather-humidity').textContent = `Humidity: ${data.humidity}%`;
      document.getElementById('weather-rain').textContent = `Rain Risk: ${data.rainfallChance}%`;
      document.getElementById('weather-advisory').textContent = data.advisory;
    }
  } catch (err) {
    console.error('Weather load error:', err);
  }
}

// Load Crops
async function loadCrops() {
  try {
    const res = await fetch('/api/crops');
    if (res.ok) {
      allCrops = await res.json();
      renderCrops();
      renderFarmerCrops();
      updateFarmerStats();
    }
  } catch (err) {
    console.error('Crops load error:', err);
  }
}

// Category selection
function selectCategory(cat) {
  currentCategory = cat;
  const pills = document.querySelectorAll('.cat-pill');
  pills.forEach(pill => {
    if (pill.textContent.trim().toLowerCase().includes(cat.toLowerCase()) || (cat === 'All' && pill.textContent.includes('All'))) {
      pill.className = "cat-pill active px-4 py-1.5 rounded-xl text-xs sm:text-sm font-semibold bg-brand-600 text-white shadow-sm shrink-0";
    } else {
      pill.className = "cat-pill px-4 py-1.5 rounded-xl text-xs sm:text-sm font-semibold bg-gray-100 text-gray-600 hover:bg-gray-200 shrink-0";
    }
  });
  renderCrops();
}

// Render Marketplace Crops
function renderCrops() {
  const container = document.getElementById('crops-grid');
  const search = (document.getElementById('search-input')?.value || '').toLowerCase().trim();

  const filtered = allCrops.filter(c => {
    const matchCategory = currentCategory === 'All' || 
      (currentCategory === 'Organic' ? c.isOrganic : c.category.toLowerCase() === currentCategory.toLowerCase());
    const matchSearch = !search || 
      c.title.toLowerCase().includes(search) || 
      c.location.toLowerCase().includes(search) || 
      c.farmerName.toLowerCase().includes(search);
    return matchCategory && matchSearch;
  });

  if (filtered.length === 0) {
    container.innerHTML = `
      <div class="col-span-full py-12 text-center text-gray-500">
        <i data-lucide="package-search" class="w-12 h-12 mx-auto text-gray-300 mb-2"></i>
        <p class="font-semibold text-lg text-gray-700">No crops found</p>
        <p class="text-xs text-gray-400">Try adjusting your category filter or search query.</p>
      </div>
    `;
    lucide.createIcons();
    return;
  }

  container.innerHTML = filtered.map(crop => `
    <div class="bg-white rounded-2xl border border-gray-200 overflow-hidden shadow-sm hover:shadow-md transition flex flex-col group">
      <div class="relative h-48 bg-gray-100 overflow-hidden">
        <img src="${crop.imageUrl}" alt="${crop.title}" class="w-full h-full object-cover group-hover:scale-105 transition duration-300">
        ${crop.isOrganic ? `
          <span class="absolute top-3 left-3 bg-emerald-600 text-white text-[11px] font-bold px-2.5 py-1 rounded-full shadow-md flex items-center space-x-1">
            <i data-lucide="leaf" class="w-3 h-3"></i>
            <span>100% Organic</span>
          </span>
        ` : ''}
        <span class="absolute bottom-3 right-3 bg-black/60 backdrop-blur-md text-white text-xs px-2.5 py-1 rounded-lg font-medium">
          ★ ${crop.rating || '4.8'}
        </span>
      </div>

      <div class="p-5 flex-grow flex flex-col justify-between space-y-4">
        <div>
          <div class="flex items-center justify-between text-xs text-gray-500 mb-1">
            <span class="font-semibold text-brand-700 uppercase tracking-wider">${crop.category}</span>
            <span>Harvest: ${crop.harvestDate}</span>
          </div>
          <h3 class="font-bold text-gray-900 text-lg leading-snug line-clamp-1">${crop.title}</h3>
          <p class="text-xs text-gray-600 line-clamp-2 mt-1">${crop.description}</p>
          
          <div class="mt-3 flex items-center space-x-2 text-xs text-gray-500">
            <i data-lucide="map-pin" class="w-3.5 h-3.5 text-gray-400"></i>
            <span>${crop.location}</span>
            <span>•</span>
            <span class="font-medium text-gray-700">${crop.farmerName}</span>
          </div>
        </div>

        <div class="pt-3 border-t border-gray-100 flex items-center justify-between">
          <div>
            <span class="text-xs text-gray-400 block">Price</span>
            <div class="text-xl font-extrabold text-brand-800">
              ₹${crop.pricePerUnit} <span class="text-xs font-normal text-gray-500">/ ${crop.unit}</span>
            </div>
            <span class="text-[11px] text-gray-400 font-medium">${crop.availableQuantity} ${crop.unit} available</span>
          </div>

          <button onclick="openBuyModal('${crop.id}')" class="bg-brand-600 hover:bg-brand-700 text-white font-semibold text-sm px-4 py-2 rounded-xl shadow-sm transition flex items-center space-x-1.5">
            <i data-lucide="shopping-bag" class="w-4 h-4"></i>
            <span>Order</span>
          </button>
        </div>
      </div>
    </div>
  `).join('');

  lucide.createIcons();
}

// Render Farmer Crops in Farmer Hub
function renderFarmerCrops() {
  const container = document.getElementById('farmer-crops-list');
  if (!container) return;

  container.innerHTML = allCrops.map(crop => `
    <div class="py-3 flex items-center justify-between gap-4">
      <div class="flex items-center space-x-3">
        <img src="${crop.imageUrl}" class="w-12 h-12 rounded-xl object-cover">
        <div>
          <h4 class="font-bold text-gray-900 text-sm">${crop.title}</h4>
          <span class="text-xs text-gray-500">${crop.category} • ₹${crop.pricePerUnit}/${crop.unit} • In Stock: ${crop.availableQuantity} ${crop.unit}</span>
        </div>
      </div>
      <span class="text-xs px-2.5 py-1 rounded-full font-semibold ${crop.availableQuantity > 0 ? 'bg-emerald-100 text-emerald-700' : 'bg-red-100 text-red-700'}">
        ${crop.availableQuantity > 0 ? 'Active Listing' : 'Sold Out'}
      </span>
    </div>
  `).join('');
}

// Update Farmer Stats
function updateFarmerStats() {
  const statCrops = document.getElementById('stat-crops-count');
  const statOrders = document.getElementById('stat-orders-count');
  const statRev = document.getElementById('stat-revenue');

  if (statCrops) statCrops.textContent = allCrops.length;
  if (statOrders) statOrders.textContent = allOrders.length;
  
  const totalRev = allOrders.reduce((sum, o) => sum + (o.totalPrice || 0), 0);
  if (statRev) statRev.textContent = `₹${totalRev.toLocaleString()}`;
}

// Load Orders
async function loadOrders() {
  try {
    const res = await fetch('/api/orders');
    if (res.ok) {
      allOrders = await res.json();
      renderAllOrders();
      renderFarmerOrders();
      updateFarmerStats();
    }
  } catch (err) {
    console.error('Orders load error:', err);
  }
}

// Render All Orders
function renderAllOrders() {
  const container = document.getElementById('all-orders-list');
  if (!container) return;

  if (allOrders.length === 0) {
    container.innerHTML = `<p class="text-gray-500 text-sm col-span-full py-8 text-center">No orders placed yet.</p>`;
    return;
  }

  container.innerHTML = allOrders.map(order => `
    <div class="bg-white rounded-2xl p-5 border border-gray-200 shadow-sm space-y-3">
      <div class="flex items-center justify-between">
        <span class="text-xs font-mono font-bold text-gray-500">${order.id}</span>
        ${getStatusBadge(order.status)}
      </div>

      <div class="border-b border-gray-100 pb-3">
        <h4 class="font-bold text-gray-900">${order.productTitle}</h4>
        <span class="text-xs text-gray-500">${order.quantity} ${order.unit} ordered • ₹${order.totalPrice.toLocaleString()}</span>
      </div>

      <div class="text-xs text-gray-600 space-y-1">
        <div><strong>Buyer:</strong> ${order.buyerName}</div>
        <div><strong>Producer:</strong> ${order.farmerName}</div>
        <div><strong>Address:</strong> ${order.deliveryAddress}</div>
        <div><strong>Date:</strong> ${order.date}</div>
      </div>
    </div>
  `).join('');

  lucide.createIcons();
}

// Render Farmer Orders in Farmer Hub with Status Selector
function renderFarmerOrders() {
  const container = document.getElementById('farmer-orders-list');
  if (!container) return;

  if (allOrders.length === 0) {
    container.innerHTML = `<p class="text-gray-500 text-xs py-4">No incoming buyer orders.</p>`;
    return;
  }

  container.innerHTML = allOrders.map(order => `
    <div class="p-4 bg-gray-50 rounded-xl border border-gray-200 flex flex-col sm:flex-row sm:items-center justify-between gap-3">
      <div>
        <div class="flex items-center space-x-2">
          <span class="text-xs font-mono font-bold text-brand-700">${order.id}</span>
          <span class="font-bold text-gray-900 text-sm">${order.productTitle}</span>
        </div>
        <p class="text-xs text-gray-500 mt-1">
          Quantity: <strong>${order.quantity} ${order.unit}</strong> • Total: <strong>₹${order.totalPrice.toLocaleString()}</strong> • Buyer: ${order.buyerName}
        </p>
        <p class="text-[11px] text-gray-400 mt-0.5">Ship to: ${order.deliveryAddress}</p>
      </div>

      <div class="flex items-center space-x-2">
        <label class="text-xs text-gray-500 font-semibold">Status:</label>
        <select onchange="updateOrderStatus('${order.id}', this.value)" class="text-xs font-semibold py-1.5 px-3 rounded-lg border border-gray-300 bg-white shadow-sm focus:ring-2 focus:ring-brand-500">
          <option value="PENDING" ${order.status === 'PENDING' ? 'selected' : ''}>PENDING</option>
          <option value="CONFIRMED" ${order.status === 'CONFIRMED' ? 'selected' : ''}>CONFIRMED</option>
          <option value="DISPATCHED" ${order.status === 'DISPATCHED' ? 'selected' : ''}>DISPATCHED</option>
          <option value="DELIVERED" ${order.status === 'DELIVERED' ? 'selected' : ''}>DELIVERED</option>
        </select>
      </div>
    </div>
  `).join('');

  lucide.createIcons();
}

// Update Order Status
async function updateOrderStatus(orderId, newStatus) {
  try {
    const res = await fetch(`/api/orders/${orderId}/status`, {
      method: 'PATCH',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ status: newStatus })
    });
    if (res.ok) {
      loadOrders();
    }
  } catch (err) {
    console.error('Failed to update status:', err);
  }
}

// Status Badges
function getStatusBadge(status) {
  switch (status) {
    case 'CONFIRMED':
      return `<span class="bg-blue-100 text-blue-700 text-xs px-2.5 py-0.5 rounded-full font-bold">CONFIRMED</span>`;
    case 'DISPATCHED':
      return `<span class="bg-purple-100 text-purple-700 text-xs px-2.5 py-0.5 rounded-full font-bold">DISPATCHED</span>`;
    case 'DELIVERED':
      return `<span class="bg-emerald-100 text-emerald-700 text-xs px-2.5 py-0.5 rounded-full font-bold">DELIVERED</span>`;
    default:
      return `<span class="bg-amber-100 text-amber-700 text-xs px-2.5 py-0.5 rounded-full font-bold">PENDING</span>`;
  }
}

// Open Buy Modal
function openBuyModal(cropId) {
  const crop = allCrops.find(c => c.id === cropId);
  if (!crop) return;
  selectedCropForBuy = crop;

  const content = document.getElementById('modal-product-content');
  content.innerHTML = `
    <div class="h-48 rounded-xl overflow-hidden mb-3">
      <img src="${crop.imageUrl}" alt="${crop.title}" class="w-full h-full object-cover">
    </div>

    <div>
      <div class="flex items-center justify-between text-xs text-gray-500 mb-1">
        <span class="font-semibold text-brand-700 uppercase tracking-wider">${crop.category}</span>
        <span>★ ${crop.rating} (Verified Harvest)</span>
      </div>
      <h3 class="text-xl font-bold text-gray-900">${crop.title}</h3>
      <p class="text-xs text-gray-500 mt-1">${crop.description}</p>
      <div class="text-xs text-gray-600 mt-2">
        <span>📍 ${crop.location}</span> • <span>Producer: <strong>${crop.farmerName}</strong> (${crop.farmerPhone})</span>
      </div>
    </div>

    <div class="bg-brand-50 p-3 rounded-xl border border-brand-100 flex items-center justify-between">
      <div>
        <span class="text-xs text-gray-500 block">Unit Rate</span>
        <span class="text-lg font-bold text-brand-800">₹${crop.pricePerUnit} / ${crop.unit}</span>
      </div>
      <div class="text-right">
        <span class="text-xs text-gray-500 block">Available</span>
        <span class="text-sm font-semibold text-gray-700">${crop.availableQuantity} ${crop.unit}</span>
      </div>
    </div>

    <form onsubmit="handlePlaceOrderSubmit(event)" class="space-y-3 pt-2">
      <div>
        <label class="block text-xs font-semibold text-gray-700 mb-1">Select Quantity (${crop.unit})</label>
        <div class="flex items-center space-x-3">
          <input type="number" id="buy-quantity" min="1" max="${crop.availableQuantity}" value="10" oninput="calculateBuyTotal()" class="w-full px-3 py-2 rounded-xl border border-gray-300 text-sm focus:ring-2 focus:ring-brand-500" required>
        </div>
      </div>

      <div class="grid grid-cols-2 gap-3">
        <div>
          <label class="block text-xs font-semibold text-gray-700 mb-1">Buyer / Firm Name</label>
          <input type="text" id="buy-name" required value="Green Leaf Grocers" class="w-full px-3 py-2 rounded-xl border border-gray-300 text-sm focus:ring-2 focus:ring-brand-500">
        </div>
        <div>
          <label class="block text-xs font-semibold text-gray-700 mb-1">Delivery City & State</label>
          <input type="text" id="buy-address" required value="Sector 18, Gurugram, Haryana" class="w-full px-3 py-2 rounded-xl border border-gray-300 text-sm focus:ring-2 focus:ring-brand-500">
        </div>
      </div>

      <div class="p-3 bg-gray-50 rounded-xl border border-gray-200 flex items-center justify-between text-sm">
        <span class="font-bold text-gray-700">Estimated Total:</span>
        <span class="text-xl font-extrabold text-brand-800" id="buy-total-price">₹${(crop.pricePerUnit * 10).toLocaleString()}</span>
      </div>

      <button type="submit" class="w-full bg-brand-600 hover:bg-brand-700 text-white font-bold py-3 rounded-xl shadow-md transition flex items-center justify-center space-x-2">
        <i data-lucide="check-circle" class="w-5 h-5"></i>
        <span>Confirm & Place Order</span>
      </button>
    </form>
  `;

  document.getElementById('buy-modal').classList.remove('hidden');
  lucide.createIcons();
}

function calculateBuyTotal() {
  if (!selectedCropForBuy) return;
  const qty = parseFloat(document.getElementById('buy-quantity').value) || 0;
  const total = qty * selectedCropForBuy.pricePerUnit;
  document.getElementById('buy-total-price').textContent = `₹${total.toLocaleString()}`;
}

function closeBuyModal() {
  document.getElementById('buy-modal').classList.add('hidden');
  selectedCropForBuy = null;
}

// Handle Place Order
async function handlePlaceOrderSubmit(e) {
  e.preventDefault();
  if (!selectedCropForBuy) return;

  const qty = parseFloat(document.getElementById('buy-quantity').value) || 1;
  const name = document.getElementById('buy-name').value;
  const address = document.getElementById('buy-address').value;
  const total = qty * selectedCropForBuy.pricePerUnit;

  try {
    const res = await fetch('/api/orders', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        productId: selectedCropForBuy.id,
        productTitle: selectedCropForBuy.title,
        quantity: qty,
        unit: selectedCropForBuy.unit,
        totalPrice: total,
        buyerName: name,
        farmerName: selectedCropForBuy.farmerName,
        deliveryAddress: address
      })
    });

    if (res.ok) {
      closeBuyModal();
      await loadCrops();
      await loadOrders();
      switchView('orders');
      alert(`Order placed successfully! Order ID generated.`);
    }
  } catch (err) {
    console.error('Order submission error:', err);
    alert('Failed to place order.');
  }
}

// Add Crop Modal
function openAddCropModal() {
  document.getElementById('add-crop-modal').classList.remove('hidden');
}

function closeAddCropModal() {
  document.getElementById('add-crop-modal').classList.add('hidden');
}

// Handle Add Crop Submit
async function handleAddCropSubmit(e) {
  e.preventDefault();
  const title = document.getElementById('crop-title').value;
  const category = document.getElementById('crop-category').value;
  const unit = document.getElementById('crop-unit').value;
  const pricePerUnit = parseFloat(document.getElementById('crop-price').value);
  const availableQuantity = parseFloat(document.getElementById('crop-quantity').value);
  const farmerName = document.getElementById('crop-farmer-name').value;
  const farmerPhone = document.getElementById('crop-farmer-phone').value;
  const location = document.getElementById('crop-location').value;
  const imageUrl = document.getElementById('crop-image').value || "https://images.unsplash.com/photo-1500937386664-56d1dfef3854?auto=format&fit=crop&w=600&q=80";
  const description = document.getElementById('crop-desc').value;
  const isOrganic = document.getElementById('crop-organic').checked;

  try {
    const res = await fetch('/api/crops', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        title, category, unit, pricePerUnit, availableQuantity,
        farmerName, farmerPhone, location, imageUrl, description, isOrganic
      })
    });

    if (res.ok) {
      closeAddCropModal();
      await loadCrops();
      switchView('marketplace');
      alert('Your harvest has been published to the marketplace!');
    }
  } catch (err) {
    console.error('Failed to add crop:', err);
  }
}

// AI Advisor Chat
async function handleChatSubmit(e) {
  e.preventDefault();
  const input = document.getElementById('chat-input');
  const message = input.value.trim();
  if (!message) return;

  input.value = '';
  appendUserMessage(message);

  try {
    const res = await fetch('/api/chat', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ message })
    });
    if (res.ok) {
      const data = await res.json();
      appendAIMessage(data.text);
    }
  } catch (err) {
    console.error('Chat error:', err);
    appendAIMessage("Sorry, I could not connect to the advisory server. Please check your network.");
  }
}

function sendQuickPrompt(promptText) {
  document.getElementById('chat-input').value = promptText;
  handleChatSubmit(new Event('submit'));
}

function appendUserMessage(text) {
  const container = document.getElementById('chat-messages');
  const el = document.createElement('div');
  el.className = "flex items-start justify-end space-x-3";
  el.innerHTML = `
    <div class="bg-brand-600 text-white p-3.5 rounded-2xl rounded-tr-none shadow-sm max-w-lg text-sm leading-relaxed">
      ${text}
    </div>
    <div class="w-8 h-8 rounded-full bg-gray-300 text-gray-700 flex items-center justify-center shrink-0 font-bold text-xs">
      You
    </div>
  `;
  container.appendChild(el);
  container.scrollTop = container.scrollHeight;
}

function appendAIMessage(text) {
  const container = document.getElementById('chat-messages');
  const el = document.createElement('div');
  el.className = "flex items-start space-x-3";
  el.innerHTML = `
    <div class="w-8 h-8 rounded-full bg-brand-600 text-white flex items-center justify-center shrink-0">
      <i data-lucide="bot" class="w-4 h-4"></i>
    </div>
    <div class="bg-white border border-gray-200 p-4 rounded-2xl rounded-tl-none shadow-sm max-w-lg text-sm leading-relaxed">
      <p class="font-semibold text-brand-800 text-xs mb-1">Krishi AI Advisor</p>
      ${text}
    </div>
  `;
  container.appendChild(el);
  container.scrollTop = container.scrollHeight;
  lucide.createIcons();
}

