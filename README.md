# Notes App

## Run with Docker Compose

```bash
docker compose up --build
```

The app starts on http://localhost:8080 with MongoDB on port 27017.

To stop:

```bash
docker compose down
```

To stop and remove stored data:

```bash
docker compose down -v
```

## API Endpoints

| Method | URL | Description |
|--------|-----|-------------|
| POST | /api/notes | Create a note |
| GET | /api/notes | List notes (Title + Created Date only) |
| GET | /api/notes/{noteId} | Get full note by ID |
| PUT | /api/notes/{noteId} | Update a note |
| DELETE | /api/notes/{noteId} | Delete a note |
| GET | /api/notes/stats/{noteId} | Word frequency stats for a note |
