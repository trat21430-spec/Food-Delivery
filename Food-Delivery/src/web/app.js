document.addEventListener('DOMContentLoaded', () => {
    fetchStats();
    fetchMapData();
    fetchSimulationResults();
    document.getElementById('btn-run-sim').addEventListener('click', runSimulation);
});
let throughputChart = null;
let errorChart = null;
async function fetchStats() {
    try {
        const res = await fetch('/api/stats');
        const data = await res.json();
        document.getElementById('val-customers').innerText = data.customers.toLocaleString();
        document.getElementById('val-restaurants').innerText = data.restaurants.toLocaleString();
        document.getElementById('val-drivers').innerText = data.drivers.toLocaleString();
        document.getElementById('val-orders').innerText = data.orders.toLocaleString();
    } catch (e) {
        console.error('Failed to fetch stats:', e);
    }
}
async function fetchMapData() {
    try {
        const res = await fetch('/api/map-data');
        const data = await res.json();
        renderCanvasMap(data);
    } catch (e) {
        console.error('Failed to fetch map data:', e);
    }
}
function renderCanvasMap(data) {
    const canvas = document.getElementById('mapCanvas');
    const ctx = canvas.getContext('2d');
    const width = canvas.width;
    const height = canvas.height;
    ctx.clearRect(0, 0, width, height);
    // Map boundary bounds
    const MIN_LAT = 21.000, MAX_LAT = 21.060;
    const MIN_LON = 105.800, MAX_LON = 105.900;
    function toX(lon) {
        return ((lon - MIN_LON) / (MAX_LON - MIN_LON)) * width;
    }
    function toY(lat) {
        return (1.0 - (lat - MIN_LAT) / (MAX_LAT - MIN_LAT)) * height;
    }
    // Grid lines
    ctx.strokeStyle = '#1e293b';
    ctx.lineWidth = 1;
    for (let x = 0; x < width; x += 40) {
        ctx.beginPath(); ctx.moveTo(x, 0); ctx.lineTo(x, height); ctx.stroke();
    }
    for (let y = 0; y < height; y += 40) {
        ctx.beginPath(); ctx.moveTo(0, y); ctx.lineTo(width, y); ctx.stroke();
    }
        // Draw Customers
    ctx.fillStyle = '#38bdf8';
    data.customers.forEach(c => {
        ctx.beginPath();
        ctx.arc(toX(c.lon), toY(c.lat), 3, 0, 2 * Math.PI);
        ctx.fill();
    });
    // Draw Restaurants
    ctx.fillStyle = '#f87171';
    data.restaurants.forEach(r => {
        ctx.beginPath();
        ctx.arc(toX(r.lon), toY(r.lat), 6, 0, 2 * Math.PI);
        ctx.fill();
    });
    // Draw Drivers
    ctx.fillStyle = '#4ade80';
    data.drivers.forEach(d => {
        ctx.beginPath();
        ctx.arc(toX(d.lon), toY(d.lat), 4, 0, 2 * Math.PI);
        ctx.fill();
    });
}
async function runSimulation() {
    const btn = document.getElementById('btn-run-sim');
    const status = document.getElementById('sim-status');
    const mechanism = document.getElementById('mech-select').value;
    btn.disabled = true;
    status.innerText = `⏳ Running multi-threaded benchmark for ${mechanism}... Please wait.`;
    try {
        const res = await fetch('/api/simulate', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ mechanism: mechanism, count: 500 })
        });
        const data = await res.json();
        status.innerText = `✅ Completed ${mechanism} simulation in ${data.durationMs} ms!`;
        document.getElementById('res-mech').innerText = data.mechanism;
        document.getElementById('res-tp').innerText = `${data.throughput.toFixed(2)} ops/sec`;
        document.getElementById('res-dur').innerText = `${data.durationMs} ms`;
        document.getElementById('res-double').innerText = data.doubleAssignments;
        document.getElementById('res-oversell').innerText = data.oversells;
        document.getElementById('res-overload').innerText = data.driverOverloads;
        fetchSimulationResults();
        fetchStats();
    } catch (e) {
        status.innerText = `❌ Error running simulation: ${e.message}`;
    } finally {
        btn.disabled = false;
    }
}
async function fetchSimulationResults() {
    try {
        const res = await fetch('/api/simulation-results');
        const runs = await res.json();
        updateTable(runs);
        updateCharts(runs);
    } catch (e) {
        console.error('Failed to fetch simulation results:', e);
    }
}
function updateTable(runs) {
    const tbody = document.getElementById('table-body');
    tbody.innerHTML = '';
    runs.forEach(r => {
        const tr = document.createElement('tr');
        const errRate = ((r.totalErrors / r.totalOrders) * 100).toFixed(1);
        tr.innerHTML = `
            <td><strong>${r.mechanism}</strong></td>
            <td>${r.totalOrders}</td>
            <td>${r.durationMs}</td>
            <td>${r.throughput.toFixed(2)}</td>
            <td><span class="badge ${r.doubleAssignments > 0 ? 'error' : ''}">${r.doubleAssignments}</span></td>
            <td><span class="badge ${r.oversells > 0 ? 'error' : ''}">${r.oversells}</span></td>
            <td><span class="badge ${r.driverOverloads > 0 ? 'error' : ''}">${r.driverOverloads}</span></td>
            <td><strong style="color: ${r.totalErrors > 0 ? '#f87171' : '#4ade80'}">${errRate}%</strong></td>
        `;
        tbody.appendChild(tr);
    });
}
function updateCharts(runs) {
    if (runs.length === 0) return;
    const labels = runs.map(r => r.mechanism);
    const throughputs = runs.map(r => r.throughput);
    const errorRates = runs.map(r => ((r.totalErrors / r.totalOrders) * 100).toFixed(1));
    // Throughput Chart
    const ctxTp = document.getElementById('throughputChart').getContext('2d');
    if (throughputChart) throughputChart.destroy();
    throughputChart = new Chart(ctxTp, {
        type: 'bar',
        data: {
            labels: labels,
            datasets: [{
                label: 'Throughput (ops/sec)',
                data: throughputs,
                backgroundColor: ['#f87171', '#38bdf8', '#c084fc', '#4ade80']
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: { legend: { display: false } },
            scales: {
                y: { ticks: { color: '#94a3b8' }, grid: { color: '#334155' } },
                x: { ticks: { color: '#94a3b8' }, grid: { color: '#334155' } }
            }
        }
    });
     // Error Chart
    const ctxErr = document.getElementById('errorChart').getContext('2d');
    if (errorChart) errorChart.destroy();
    errorChart = new Chart(ctxErr, {
        type: 'bar',
        data: {
            labels: labels,
            datasets: [{
                label: 'Error Rate (%)',
                data: errorRates,
                backgroundColor: '#f87171'
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: { legend: { display: false } },
            scales: {
                y: { ticks: { color: '#94a3b8' }, grid: { color: '#334155' } },
                x: { ticks: { color: '#94a3b8' }, grid: { color: '#334155' } }
            }
        }
    });
}
