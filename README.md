# NLW Unite (Java) - pass.in API

pass.in is an application for managing participants in in-person events. The tool allows the organizer to register an event and open a public registration page. Registered participants can issue a credential for check-in on the day of the event. The system will scan the participant's credentials to allow entry to the event.

![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)
![Apache Maven](https://img.shields.io/badge/Apache%20Maven-C71A36?style=for-the-badge&logo=Apache%20Maven&logoColor=white)

## Setup

1. Clone the repository;

   ```sh
   git clone https://github.com/mat-afk/pass-in-api.git
   ```

2. Navigate to the project directory;

   ```sh
   cd pass-in-api
   ```

3. Run application;

4. Test it! (I personally recommend testing with [Hoppscotch](https://hoppscotch.io/)).

## Requirements

### Functional requirements

- [x] The organizer must be able to register a new event;
- [x] The organizer must be able to view event data;
- [x] The organizer must be able to view the list of participants;
- [x] The participant must be able to register for an event;
- [x] The participant must be able to view their registration badge;
- [x] The participant must be able to check in at the event;

### Business rules

- [x] The participant can only register for an event once;
- [x] Participants can only register for events with available places;
- [x] The participant can only check-in to an event once;

### Non-functional requirements

- [x] Check-in at the event will be carried out using a QRCode;

## API Documentation (Swagger)

For API documentation, visit the link: https://nlw-unite-nodejs.onrender.com/docs

## Database

In this application we will use a relational database (SQL). For the development environment, we will continue with HyperSQL due to the ease of the environment.

### ERD Diagram

<img src=".github/erd.svg" width="400" alt="ERD Database Diagram" />

### Database structure (SQL)

```sql
-- CreateTable
CREATE TABLE "events" (
    "id" TEXT NOT NULL PRIMARY KEY,
    "title" TEXT NOT NULL,
    "details" TEXT,
    "slug" TEXT NOT NULL,
    "maximum_attendees" INTEGER
);

-- CreateTable
CREATE TABLE "attendees" (
    "id" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    "name" TEXT NOT NULL,
    "email" TEXT NOT NULL,
    "event_id" TEXT NOT NULL,
    "created_at" DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT "attendees_event_id_fkey" FOREIGN KEY ("event_id") REFERENCES "events" ("id") ON DELETE RESTRICT ON UPDATE CASCADE
);

-- CreateTable
CREATE TABLE "check_ins" (
    "id" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    "created_at" DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    "attendeeId" INTEGER NOT NULL,
    CONSTRAINT "check_ins_attendeeId_fkey" FOREIGN KEY ("attendeeId") REFERENCES "attendees" ("id") ON DELETE RESTRICT ON UPDATE CASCADE
);

-- CreateIndex
CREATE UNIQUE INDEX "events_slug_key" ON "events"("slug");

-- CreateIndex
CREATE UNIQUE INDEX "attendees_event_id_email_key" ON "attendees"("event_id", "email");

-- CreateIndex
CREATE UNIQUE INDEX "check_ins_attendeeId_key" ON "check_ins"("attendeeId");
```

## HTTP

### Events `/events`

#### GET `/events/{id}/details`

Return data from a specific event.

##### Response body

```json
{
  "id": "string",
  "title": "string",
  "details": "string",
  "slug": "string",
  "maximumAttendees": 0,
  "attendeesAmount": 0
}
```

#### POST `/events/`

Create a new event.

##### Request body

```json
{
  "title": "string",
  "details": "string",
  "maximumAttendees": 0
}
```

##### Response body

```json
{
  "eventId": "string"
}
```

#### GET `/events/{id}/attendees`

Return an event's attendees list.

##### Response body

```json
{
  "attendees": [
    {
      "id": "string",
      "name": "string",
      "email": "string",
      "createdAt": "2000-01-01T00:00:00.000000",
      "checkedInAt": "2000-01-01T00:00:00.000000"
    },
    {
      "id": "string",
      "name": "string",
      "email": "string",
      "createdAt": "2000-01-01T00:00:00.000000",
      "checkedInAt": "2000-01-01T00:00:00.000000"
    },
    ...
  ]
}
```

#### POST `/events/{id}/attendees`

Register a participant in an event.

##### Request body

```json
{
  "name": "string",
  "email": "string"
}
```

##### Response body

```json
{
  "attendeeId": "string"
}
```

### Attendees `/attendees`

#### GET `/attendees/{id}/badge`

Return the participant's badge.

##### Response body

```json
{
  "badge": {
    "name": "string",
    "email": "string",
    "checkInUrl": "http://localhost:8080/attendees/{id}/check-in",
    "eventId": "string"
  }
}
```

#### POST `/attendees/{id}/check-in`

Check-in the participant at the event.
