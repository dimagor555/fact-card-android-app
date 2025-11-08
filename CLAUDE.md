Fact Card is an Android drawing application that allows users to create files containing fact cards (text boxes) connected by lines. Users can create, edit, move, and connect fact cards with customizable text sizes and positions.

**Package**: `ru.dimagor555.factcard`
**Application ID**: `ru.dimagor555.factcard` (debug: `ru.dimagor555.factcard.debug`)

## Build Commands

```bash
# Build the project
./gradlew build

# Clean build
./gradlew clean
```

## Architecture

### Tech Stack
- **Language**: Kotlin 1.4.32
- **Build**: Gradle with Android Gradle Plugin 4.2.1
- **DI**: Dagger Hilt 2.33-beta
- **Database**: Room 2.3.0 (SQLite)
- **UI**: View Binding + Data Binding
- **Navigation**: Jetpack Navigation 2.3.5 with SafeArgs
- **Architecture**: MVVM with ViewModels and LiveData

### Package Structure

```
ru.dimagor555.factcard/
├── di/                      # Dependency injection modules
│   └── DatabaseModule.kt    # Room database and DAO providers
├── data/                    # Data layer
│   ├── AppDatabase.kt       # Room database definition
│   ├── FileCache.kt         # In-memory file caching
│   ├── file/                # File entity and DAO
│   ├── factcard/            # FactCard entity and DAO
│   └── line/                # Line entity and DAO
└── ui/                      # Presentation layer
    ├── files/               # File list screen
    ├── createfile/          # File creation screen
    ├── drawfile/            # Main drawing screen
    │   └── canvas/          # Custom canvas implementation
    │       ├── FileCanvas.kt
    │       ├── creation/    # Creating fact cards and lines
    │       ├── render/      # Rendering logic
    │       └── input/       # Touch input handling
    └── settings/            # App settings
```

### Data Model

The app uses a normalized Room database with three main entities:

1. **File** (`files` table): Top-level container for a drawing
   - `idFile` (PK), `name`, `lastUseTime`

2. **FactCard** (`fact_cards` table): Text boxes in a file
   - `idFactCard` (PK), `text`, `fileId` (FK), `positionX`, `positionY`, `textSize`
   - Foreign key to File with CASCADE delete

3. **Line** (`lines` table): Connections between fact cards
   - `idLine` (PK), `fileId` (FK), `firstCardId`, `secondCardId`, `firstPointId`, `secondPointId`
   - Foreign key to File with CASCADE delete
   - Unique index on connection points

### Navigation Flow

The app uses Navigation Component with three main screens:

1. **FilesFragment** (start destination): Lists all files
   - Can navigate to CreateFileFragment, DrawFileFragment, or SettingsFragment

2. **CreateFileFragment**: Creates new file
   - Navigates to DrawFileFragment with new file ID, popping back to FilesFragment

3. **DrawFileFragment**: Main drawing screen (requires `fileId` argument)
   - Contains FileCanvas for drawing and interaction

### Custom Canvas System

The drawing system is built around `FileCanvas` with:

- **Creation**: `FactCardCreator`, `LineCreator`, `LinePathBuilder`
- **Rendering**: `FileRenderer`, `FactCardRenderer`, `LineRenderer`, `TextRenderer`
- **Input**: `FileCanvasGestureListener`, `FileCanvasClickProcessor`, `FactCardInputProcessor`, `FactCardMover`
- **State Management**: Render models for fact cards and lines

### Dependency Injection

Uses Hilt with:
- `@HiltAndroidApp` on Application class
- `@AndroidEntryPoint` on Activity and Fragments
- `DatabaseModule` provides singleton database and DAOs
- All ViewModels are `@HiltViewModel` injected

## Important Implementation Notes

### Database Migrations
The database uses `fallbackToDestructiveMigration()` - schema changes will clear all data. The current version is 5.

### Permissions
The app requires `WRITE_EXTERNAL_STORAGE` permission, requested in `MainActivity.onStart()`.

### Canvas Interaction
Fact cards support:
- Selection and moving via touch gestures
- Resizing text via increase/decrease methods
- Point-based connections (4 points per card)
- Scale range limitations for text size (MIN: 10, MAX: 200, interval: 5)

### Color Management
Uses `ColorManager` for drawing colors and integrates with JareD Rummler's ColorPicker library for color selection.
