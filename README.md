# Stugbokningssystem

Ett enkelt REST API för bokning av stugor, byggt med Spring Boot och Java 21.

## Beskrivning

En bokningsapplikation där:
- **Turister** kan boka stugor och få en bokningsbekräftelse
- **Administratörer** kan se alla bokningar

## Teknisk stack

- Java 21
- Spring Boot 4.0.0
- H2 In-Memory Database
- Maven

## Kom igång

### Förutsättningar
- Java 21 eller senare

### Starta applikationen

**Windows:**
```cmd
mvnw.cmd spring-boot:run
```

**Mac/Linux:**
```bash
./mvnw spring-boot:run
```

Applikationen startar på `http://localhost:8080`

## Testa API:et

### Hämta alla stugor
```bash
curl http://localhost:8080/api/bookings/cabins
```

### Skapa en bokning
```bash
curl -X POST http://localhost:8080/api/bookings \
  -H "Content-Type: application/json" \
  -d '{
    "cabinId": 1,
    "guestName": "Anna Andersson",
    "guestEmail": "anna@example.com",
    "checkInDate": "2026-07-01",
    "checkOutDate": "2026-07-08",
    "numberOfGuests": 4
  }'
```

### Se alla bokningar (admin)
```bash
curl http://localhost:8080/api/admin/bookings
```

## API Endpoints

### För turister

**Hämta alla stugor:**
```
GET http://localhost:8080/api/bookings/cabins
```

**Boka en stuga:**
```
POST http://localhost:8080/api/bookings
Content-Type: application/json

{
  "cabinId": 1,
  "guestName": "Anna Andersson",
  "guestEmail": "anna@exempel.se",
  "checkInDate": "2026-07-01",
  "checkOutDate": "2026-07-08",
  "numberOfGuests": 4
}
```

**Exempel svar:**
```json
{
  "bookingId": 1,
  "bookingReference": "BK-A3F9D8E1",
  "cabinName": "Stugan till höger om Bolagsverket",
  "totalPrice": 8400.0,
  "message": "Bokning bekräftad! Din bokningsreferens är: BK-A3F9D8E1"
}
```

### För administratörer

**Hämta alla bokningar:**
```
GET http://localhost:8080/api/admin/bookings
```

## Testdata

Vid start finns 3 stugor tillgängliga:

1. **Stugan till höger om Bolagsverket** - Östermalmsgatan 89 (4 personer, 1200 kr/natt)
2. **Stugan till vänster om Bolagsverket** - Östermalmsgatan 85 (6 personer, 1500 kr/natt)
3. **Stugan framför Bolagsverket** - Trädgårdsgatan 12 (8 personer, 2500 kr/natt)

## Funktioner

- Bokningar med unik bokningsreferens
- Automatisk prisberäkning
- Validering av kapacitet och datum
- Konfliktkontroll (förhindrar dubbelbokningar)
