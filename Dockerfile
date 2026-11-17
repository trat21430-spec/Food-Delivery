# ==============================================================================
# FOOD DELIVERY NETWORK SIMULATION (FDS) - ENTERPRISE DOCKERFILE
# Multi-stage production build for pure OOP Java 8+ Web Server & PWA
# ==============================================================================

# ------------------------------------------------------------------------------
# Stage 1: Build Java Application Classes
# ------------------------------------------------------------------------------
FROM openjdk:8-jdk-alpine AS builder

WORKDIR /build

# Copy source code and build structure
COPY src ./src
COPY data ./data

# Compile all pure OOP Java controllers, models, repositories and web server
RUN mkdir -p bin && \
    javac -encoding UTF-8 -d bin $(find src -name "*.java")

# ------------------------------------------------------------------------------
# Stage 2: Minimal Production Runtime
# ------------------------------------------------------------------------------
FROM openjdk:8-jre-alpine

LABEL maintainer="Food Delivery LAB211 Team - Quy Nhon University"
LABEL description="Fullstack Food Delivery System with Pure OOP Java Backend & Modern PWA"
LABEL version="2.0.0"

WORKDIR /app

# Install curl for container health check
RUN apk add --no-cache curl tzdata && \
    cp /usr/share/zoneinfo/Asia/Ho_Chi_Minh /etc/localtime && \
    echo "Asia/Ho_Chi_Minh" > /etc/timezone

# Copy compiled classes from builder
COPY --from=builder /build/bin ./bin

# Copy runtime assets: CSV storage, Web frontend, and documentation
COPY data ./data
COPY web ./web

# Expose default HTTP port
EXPOSE 8080

# Environment variables
ENV PORT=8080 \
    JAVA_OPTS="-Dfile.encoding=UTF-8 -Xms128m -Xmx512m -XX:+UseG1GC"

# Healthcheck endpoint (REST status ping)
HEALTHCHECK --interval=20s --timeout=5s --start-period=5s --retries=3 \
    CMD curl -f http://localhost:8080/api/status || exit 1

# Start the embedded Java web server
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -cp bin web.FoodDeliveryWebServer $PORT"]
