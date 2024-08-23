package net.superkat.jetlag.config;

import java.awt.*;

public class SpeedlineConfigInstance {
    public int maxSpawnTicks = 3;
    public int minSpawnTicks = 1;
    public int maxSpawnAmount = 2;
    public int minSpawnAmount = 1;
    public boolean velBasedSpawnAmount = true;

    public float width = 0.2f;
    public float length = 2.0f;

    public float spawnRadius = 12.0f;
    public float randomSpawnRadius = 1f;
    public boolean velBasedSpawnRadius = true;
    public float speed = 0.5f;
    public float velMultiplier = 1.2f;

    public Color color = Color.white;
    public boolean velBasedOpacity = true;
    public boolean fadeIn = true;
    public boolean rainbowMode = false;

    public boolean moveFromTurn = true;
    public float turnMoveMultiplier = 1f;
    public float maxMoveAmountX = 35f;
    public float maxMoveAmountY = 17f;

    public int maxAge = 50;
    public int minAge = 15;

    public SpeedlineConfigInstance rocketMode() {
        return rocket_subtleAndSweet();
    }

    public SpeedlineConfigInstance subtleAndSweet() {
        return this;
    }

    public SpeedlineConfigInstance rocket_subtleAndSweet() {
        this.width = 0.5f;
        this.length = 3f;
        this.spawnRadius = 13f;
        this.randomSpawnRadius = 1.7f;
        this.velBasedSpawnRadius = false;
        this.speed = 0.5f;
        this.velMultiplier = 1.2f;
        this.color = new Color(30, 210, 234);
        this.velBasedOpacity = true;
        this.fadeIn = true;
        return this;
    }

    public SpeedlineConfigInstance niceAndNoticeable() {
        this.width = 0.7f;
        this.length = 2.5f;
        this.spawnRadius = 13f;
        this.randomSpawnRadius = 2f;
        this.velBasedSpawnRadius = true;
        this.speed = 0.5f;
        this.velMultiplier = 1.1f;
        this.velBasedOpacity = true;
        this.fadeIn = true;
        return this;
    }

    public SpeedlineConfigInstance rocket_niceAndNoticeable() {
        this.width = 0.7f;
        this.length = 3.0f;
        this.spawnRadius = 10.5f;
        this.randomSpawnRadius = 1.7f;
        this.velBasedSpawnRadius = false;
        this.speed = 0.55f;
        this.velMultiplier = 1.35f;
        this.color = new Color(30, 210, 234);
        this.velBasedOpacity = true;
        this.fadeIn = true;
        return this;
    }

    public SpeedlineConfigInstance animeStyled() {
        this.width = 1f;
        this.length = 3.5f;
        this.spawnRadius = 4.5f;
        this.randomSpawnRadius = 3f;
        this.velBasedSpawnRadius = true;
        this.speed = 1.7f;
        this.velMultiplier = 1.45f;
        this.velBasedOpacity = true;
        this.fadeIn = false;
        return this;
    }

    public SpeedlineConfigInstance rocket_animeStyled() {
        this.width = 1.3f;
        this.length = 5f;
        this.spawnRadius = 5.7f;
        this.randomSpawnRadius = 1.3f;
        this.velBasedSpawnRadius = false;
        this.speed = 1.7f;
        this.velMultiplier = 1.5f;
        this.color = new Color(30, 210, 234);
        this.fadeIn = false;
        return this;
    }

    public SpeedlineConfigInstance HYPERDRIVE() {
        this.maxSpawnTicks = 3;
        this.minSpawnTicks = 1;
        this.maxSpawnAmount = 20;
        this.minSpawnAmount = 7;
        return this;
    }

    public SpeedlineConfigInstance rocket_HYPERDRIVE() {
        this.maxSpawnTicks = 2;
        this.minSpawnTicks = 1;
        this.maxSpawnAmount = 20;
        this.minSpawnAmount = 7;
        this.width = 0.5f;
        this.length = 3f;
        this.spawnRadius = 13f;
        this.randomSpawnRadius = 1.7f;
        this.velBasedSpawnRadius = false;
        this.speed = 0.5f;
        this.velMultiplier = 1.2f;
        this.color = new Color(30, 210, 234);
        this.velBasedOpacity = true;
        this.fadeIn = true;
        return this;
    }
}
