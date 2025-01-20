package dev.ethans.relicrush.team;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;

@RequiredArgsConstructor
@Getter
public class Team {

    private final String name;

    private final List<Player> players;

    @Setter
    private int score;

    @Setter
    private Location spawnLocation;
}
