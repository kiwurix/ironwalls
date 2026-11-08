# IronWalls

A tower-defense game written in Java with JavaFX. Place defenses, upgrade units and survive waves of zombies.

## Features

- Two maps and three difficulty levels.
- Snipers, bombers, walls and soldier factories.
- Different zombie types and boss abilities.
- Unit upgrades and a local best-wave record.
- JUnit tests for game logic, defenses and record storage.

## Run

Requires JDK 21+ and Maven. JavaFX dependencies are defined in `pom.xml`.

```bash
mvn javafx:run
```

Choose a map and difficulty in the main menu. Use the buttons or keys **1-4** to select a defense, then click on the map to place it. Click an existing unit to open its upgrade window.

Run the existing tests with:

```bash
mvn test
```

## Structure

- `src/main/java/game` - game state, controller and loop.
- `src/main/java/attack` and `defense` - enemies and defensive units.
- `src/main/java/attackStrategies`, `factories` and `finalBossDecorators` - attack and unit behavior.
- `src/main/java/ui` and `windows` - JavaFX interface.
- `src/main/resources` - game images.
- `src/test/java` - tests.
