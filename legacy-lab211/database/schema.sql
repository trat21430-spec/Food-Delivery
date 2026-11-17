-- ==============================================================================
-- FOOD DELIVERY PLATFORM (FDS) - MASTER PLAN DATABASE SCHEMA
-- PostgreSQL 16 + PostGIS Relational Architecture (Trang 9 - 10 Master Plan)
-- ==============================================================================

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE EXTENSION IF NOT EXISTS "postgis";

-- ------------------------------------------------------------------------------
-- 1. IDENTITY & ACCESS MANAGEMENT DOMAIN
-- ------------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS roles (
    role_id VARCHAR(50) PRIMARY KEY,
    role_name VARCHAR(100) NOT NULL,
    description TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS permissions (
    permission_id VARCHAR(100) PRIMARY KEY,
    module VARCHAR(50) NOT NULL,
    action VARCHAR(50) NOT NULL,
    description TEXT
);

CREATE TABLE IF NOT EXISTS users (
    user_id VARCHAR(50) PRIMARY KEY,
    username VARCHAR(100) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    email VARCHAR(150),
    phone VARCHAR(20),
    full_name VARCHAR(150) NOT NULL,
    status VARCHAR(30) DEFAULT 'ACTIVE',
    linked_customer_id VARCHAR(50),
    linked_driver_id VARCHAR(50),
    linked_merchant_id VARCHAR(50),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    version BIGINT DEFAULT 1
);

CREATE TABLE IF NOT EXISTS user_roles (
    user_id VARCHAR(50) REFERENCES users(user_id) ON DELETE CASCADE,
    role_id VARCHAR(50) REFERENCES roles(role_id) ON DELETE CASCADE,
    PRIMARY KEY (user_id, role_id)
);

CREATE TABLE IF NOT EXISTS refresh_tokens (
    token_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id VARCHAR(50) REFERENCES users(user_id) ON DELETE CASCADE,
    token_hash VARCHAR(255) NOT NULL,
    expires_at TIMESTAMP WITH TIME ZONE NOT NULL,
    revoked BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- ------------------------------------------------------------------------------
-- 2. CUSTOMER DOMAIN
-- ------------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS customer_profiles (
    customer_id VARCHAR(50) PRIMARY KEY,
    user_id VARCHAR(50) REFERENCES users(user_id),
    full_name VARCHAR(150) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    email VARCHAR(150),
    default_address TEXT,
    loyalty_points INT DEFAULT 0,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS user_addresses (
    address_id VARCHAR(50) PRIMARY KEY,
    customer_id VARCHAR(50) REFERENCES customer_profiles(customer_id) ON DELETE CASCADE,
    label VARCHAR(50), -- Home, Work
    recipient_name VARCHAR(150),
    phone VARCHAR(20),
    street_address TEXT NOT NULL,
    ward VARCHAR(100),
    district VARCHAR(100) DEFAULT 'TP. Quy Nhơn',
    city VARCHAR(100) DEFAULT 'Bình Định',
    latitude NUMERIC(10, 7) NOT NULL,
    longitude NUMERIC(10, 7) NOT NULL,
    geom GEOMETRY(Point, 4326),
    is_default BOOLEAN DEFAULT FALSE
);

-- ------------------------------------------------------------------------------
-- 3. MERCHANT & RESTAURANT DOMAIN
-- ------------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS merchants (
    merchant_id VARCHAR(50) PRIMARY KEY,
    business_name VARCHAR(200) NOT NULL,
    tax_code VARCHAR(50),
    representative_name VARCHAR(150),
    contact_phone VARCHAR(20),
    contact_email VARCHAR(150),
    bank_account_no VARCHAR(50),
    bank_name VARCHAR(100),
    bank_account_name VARCHAR(150),
    status VARCHAR(30) DEFAULT 'ACTIVE',
    commission_rate NUMERIC(5, 2) DEFAULT 20.00, -- 20% platform fee
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS restaurants (
    restaurant_id VARCHAR(50) PRIMARY KEY,
    merchant_id VARCHAR(50) REFERENCES merchants(merchant_id),
    name VARCHAR(200) NOT NULL,
    category VARCHAR(100) NOT NULL,
    address TEXT NOT NULL,
    ward VARCHAR(100),
    latitude NUMERIC(10, 7) NOT NULL,
    longitude NUMERIC(10, 7) NOT NULL,
    geom GEOMETRY(Point, 4326),
    rating NUMERIC(3, 2) DEFAULT 4.5,
    is_open BOOLEAN DEFAULT TRUE,
    opening_time TIME DEFAULT '06:00:00',
    closing_time TIME DEFAULT '22:00:00',
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- ------------------------------------------------------------------------------
-- 4. CATALOG & MENU DOMAIN
-- ------------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS food_categories (
    category_id VARCHAR(50) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    slug VARCHAR(100) UNIQUE,
    display_order INT DEFAULT 0
);

CREATE TABLE IF NOT EXISTS products (
    product_id VARCHAR(50) PRIMARY KEY,
    restaurant_id VARCHAR(50) REFERENCES restaurants(restaurant_id) ON DELETE CASCADE,
    category_id VARCHAR(50) REFERENCES food_categories(category_id),
    name VARCHAR(200) NOT NULL,
    description TEXT,
    price_vnd BIGINT NOT NULL, -- Integer currency
    stock_qty INT DEFAULT 50,
    image_url TEXT,
    is_available BOOLEAN DEFAULT TRUE,
    version BIGINT DEFAULT 1,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- ------------------------------------------------------------------------------
-- 5. ORDERING DOMAIN & SNAPSHOTS
-- ------------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS quotes (
    quote_id VARCHAR(50) PRIMARY KEY,
    customer_id VARCHAR(50) REFERENCES customer_profiles(customer_id),
    restaurant_id VARCHAR(50) REFERENCES restaurants(restaurant_id),
    food_amount_vnd BIGINT NOT NULL,
    delivery_fee_vnd BIGINT NOT NULL,
    discount_amount_vnd BIGINT DEFAULT 0,
    final_total_vnd BIGINT NOT NULL,
    voucher_code VARCHAR(50),
    distance_km NUMERIC(6, 2),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    expires_at TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE TABLE IF NOT EXISTS orders (
    order_id VARCHAR(50) PRIMARY KEY,
    customer_id VARCHAR(50) REFERENCES customer_profiles(customer_id),
    restaurant_id VARCHAR(50) REFERENCES restaurants(restaurant_id),
    driver_id VARCHAR(50),
    order_state VARCHAR(30) NOT NULL DEFAULT 'PENDING',
    payment_state VARCHAR(30) NOT NULL DEFAULT 'INITIATED',
    delivery_state VARCHAR(30) NOT NULL DEFAULT 'CREATED',
    settlement_state VARCHAR(30) NOT NULL DEFAULT 'OPEN',
    total_food_amount_vnd BIGINT NOT NULL,
    delivery_fee_vnd BIGINT NOT NULL,
    discount_amount_vnd BIGINT DEFAULT 0,
    final_amount_vnd BIGINT NOT NULL,
    voucher_code VARCHAR(50),
    payment_method VARCHAR(30) DEFAULT 'COD', -- COD, VIETQR
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    version BIGINT DEFAULT 1
);

CREATE TABLE IF NOT EXISTS order_items (
    order_item_id VARCHAR(50) PRIMARY KEY,
    order_id VARCHAR(50) REFERENCES orders(order_id) ON DELETE CASCADE,
    product_id VARCHAR(50) REFERENCES products(product_id),
    product_name VARCHAR(200) NOT NULL,
    quantity INT NOT NULL,
    unit_price_vnd BIGINT NOT NULL,
    subtotal_vnd BIGINT NOT NULL
);

-- Immutable Order Snapshot (Trang 5 & 10 Master Plan)
CREATE TABLE IF NOT EXISTS order_snapshots (
    snapshot_id VARCHAR(50) PRIMARY KEY,
    order_id VARCHAR(50) UNIQUE REFERENCES orders(order_id) ON DELETE CASCADE,
    customer_name VARCHAR(150),
    customer_phone VARCHAR(20),
    delivery_address TEXT,
    delivery_lat NUMERIC(10, 7),
    delivery_lon NUMERIC(10, 7),
    restaurant_name VARCHAR(200),
    restaurant_address TEXT,
    items_json JSONB NOT NULL,
    food_amount_vnd BIGINT NOT NULL,
    delivery_fee_vnd BIGINT NOT NULL,
    discount_amount_vnd BIGINT NOT NULL,
    final_total_vnd BIGINT NOT NULL,
    voucher_code VARCHAR(50),
    payment_method VARCHAR(30),
    snapshot_timestamp TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- ------------------------------------------------------------------------------
-- 6. DRIVER & DELIVERY DISPATCH DOMAIN
-- ------------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS drivers (
    driver_id VARCHAR(50) PRIMARY KEY,
    user_id VARCHAR(50) REFERENCES users(user_id),
    full_name VARCHAR(150) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    license_plate VARCHAR(30) NOT NULL,
    vehicle_type VARCHAR(50) DEFAULT 'Xe máy',
    current_lat NUMERIC(10, 7) DEFAULT 13.7829,
    current_lng NUMERIC(10, 7) DEFAULT 109.2197,
    geom GEOMETRY(Point, 4326),
    status VARCHAR(30) DEFAULT 'OFFLINE', -- AVAILABLE, BUSY, OFFLINE
    rating NUMERIC(3, 2) DEFAULT 4.9,
    active_assignments_count INT DEFAULT 0,
    version BIGINT DEFAULT 1,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS delivery_tasks (
    task_id VARCHAR(50) PRIMARY KEY,
    order_id VARCHAR(50) UNIQUE REFERENCES orders(order_id),
    restaurant_id VARCHAR(50) REFERENCES restaurants(restaurant_id),
    customer_id VARCHAR(50) REFERENCES customer_profiles(customer_id),
    pickup_lat NUMERIC(10, 7) NOT NULL,
    pickup_lng NUMERIC(10, 7) NOT NULL,
    dropoff_lat NUMERIC(10, 7) NOT NULL,
    dropoff_lng NUMERIC(10, 7) NOT NULL,
    delivery_state VARCHAR(30) DEFAULT 'CREATED',
    assigned_driver_id VARCHAR(50) REFERENCES drivers(driver_id),
    dispatch_attempt INT DEFAULT 0,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    picked_up_at TIMESTAMP WITH TIME ZONE,
    delivered_at TIMESTAMP WITH TIME ZONE,
    version BIGINT DEFAULT 1
);

CREATE TABLE IF NOT EXISTS driver_offers (
    offer_id VARCHAR(50) PRIMARY KEY,
    task_id VARCHAR(50) REFERENCES delivery_tasks(task_id) ON DELETE CASCADE,
    order_id VARCHAR(50) REFERENCES orders(order_id),
    driver_id VARCHAR(50) REFERENCES drivers(driver_id),
    match_score NUMERIC(6, 2) NOT NULL,
    shipping_fee_vnd BIGINT NOT NULL,
    sent_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    expires_at TIMESTAMP WITH TIME ZONE NOT NULL,
    status VARCHAR(30) DEFAULT 'PENDING' -- PENDING, ACCEPTED, REJECTED, EXPIRED
);

-- ------------------------------------------------------------------------------
-- 7. PAYMENT, FINANCE & SETTLEMENT DOMAIN (DOUBLE-ENTRY LEDGER)
-- ------------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS payments (
    payment_id VARCHAR(50) PRIMARY KEY,
    order_id VARCHAR(50) REFERENCES orders(order_id),
    payment_method VARCHAR(30) NOT NULL, -- COD, VIETQR
    amount_vnd BIGINT NOT NULL,
    payment_state VARCHAR(30) DEFAULT 'INITIATED',
    transaction_ref VARCHAR(100),
    signature VARCHAR(255),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    captured_at TIMESTAMP WITH TIME ZONE
);

-- Standard Financial Ledger (Trang 10 & 14 Master Plan)
CREATE TABLE IF NOT EXISTS financial_ledger (
    entry_id VARCHAR(50) PRIMARY KEY,
    transaction_ref VARCHAR(100) NOT NULL,
    order_id VARCHAR(50) REFERENCES orders(order_id),
    account_code VARCHAR(50) NOT NULL, -- 111_CASH, 112_VIETQR, 331_REST, 331_DRV, 511_COMM, 642_PROMO
    debit_vnd BIGINT DEFAULT 0,
    credit_vnd BIGINT DEFAULT 0,
    currency VARCHAR(10) DEFAULT 'VND',
    occurred_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    source_type VARCHAR(50) NOT NULL, -- ORDER_PAYMENT, RESTAURANT_PAYOUT, DRIVER_PAYOUT, REFUND
    source_id VARCHAR(100),
    reversal_of VARCHAR(50),
    note TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS merchant_settlements (
    settlement_id VARCHAR(50) PRIMARY KEY,
    merchant_id VARCHAR(50) REFERENCES merchants(merchant_id),
    period_start DATE NOT NULL,
    period_end DATE NOT NULL,
    total_orders_count INT NOT NULL,
    gross_food_amount_vnd BIGINT NOT NULL,
    platform_commission_vnd BIGINT NOT NULL, -- 20%
    net_payable_vnd BIGINT NOT NULL,        -- 80%
    settlement_state VARCHAR(30) DEFAULT 'OPEN',
    paid_at TIMESTAMP WITH TIME ZONE,
    bank_reference VARCHAR(100),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- ------------------------------------------------------------------------------
-- 8. SYSTEM, IDEMPOTENCY & TRANSACTIONAL OUTBOX
-- ------------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS idempotency_keys (
    idempotency_key VARCHAR(128) PRIMARY KEY,
    status_code INT NOT NULL,
    response_payload JSONB NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    expires_at TIMESTAMP WITH TIME ZONE NOT NULL
);

-- Transactional Outbox Table (Trang 12 Master Plan)
CREATE TABLE IF NOT EXISTS outbox_events (
    event_id VARCHAR(50) PRIMARY KEY,
    aggregate_type VARCHAR(50) NOT NULL, -- ORDER, DELIVERY, PAYMENT, SETTLEMENT
    aggregate_id VARCHAR(50) NOT NULL,
    event_type VARCHAR(50) NOT NULL,     -- ORDER_CREATED, DRIVER_OFFER_SENT, DRIVER_ASSIGNED, etc.
    payload_json JSONB NOT NULL,
    status VARCHAR(30) DEFAULT 'PENDING',-- PENDING, PUBLISHED, FAILED
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    published_at TIMESTAMP WITH TIME ZONE
);

-- ------------------------------------------------------------------------------
-- INDEXES & SPATIAL QUERIES
-- ------------------------------------------------------------------------------
CREATE INDEX IF NOT EXISTS idx_restaurants_geom ON restaurants USING GIST(geom);
CREATE INDEX IF NOT EXISTS idx_drivers_geom ON drivers USING GIST(geom);
CREATE INDEX IF NOT EXISTS idx_orders_customer ON orders(customer_id);
CREATE INDEX IF NOT EXISTS idx_orders_restaurant ON orders(restaurant_id);
CREATE INDEX IF NOT EXISTS idx_orders_state ON orders(order_state);
CREATE INDEX IF NOT EXISTS idx_delivery_tasks_driver ON delivery_tasks(assigned_driver_id);
CREATE INDEX IF NOT EXISTS idx_financial_ledger_order ON financial_ledger(order_id);
CREATE INDEX IF NOT EXISTS idx_financial_ledger_tx ON financial_ledger(transaction_ref);
CREATE INDEX IF NOT EXISTS idx_outbox_events_status ON outbox_events(status);
