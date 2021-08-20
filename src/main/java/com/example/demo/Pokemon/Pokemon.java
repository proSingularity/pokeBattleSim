package com.example.demo.Pokemon;

import java.io.IOException;
import java.util.Arrays;

import com.example.demo.RequestMode;
import com.example.demo.Controller.LogPokemon;
import com.example.demo.Pokedex.Pokedex;
import com.example.demo.Searches.MoveSearch.MoveSearch;
import com.example.demo.Searches.PokemonSearch.MoveBySearch;
import com.example.demo.Searches.PokemonSearch.PokemonSearch;
import com.example.demo.Searches.PokemonSearch.StatBySearch;
import com.example.demo.Searches.PokemonSearch.TypeHolder;

public class Pokemon {
    private String name;
    private Stat[] stats;
    private Type[] types;
    private Attack[] attacks;
    private String frontSpriteUrl;
    private String backSpriteUrl;
    private int exhaustionPoint;

    public Pokemon(PokemonSearch data) {
        this.name = data.name;
        this.stats = Arrays.stream(data.stats).map(StatBySearch::convert).toArray(Stat[]::new);
        this.types = Arrays.stream(data.types).map(TypeHolder::convert).toArray(Type[]::new);
        this.attacks = creatAttacks(data.moves);
        this.backSpriteUrl = data.sprites.back_default;
        this.frontSpriteUrl = data.sprites.front_default;
        this.exhaustionPoint = 0;
    }

    public int getStatSum() {
        var statValues = Arrays.stream(this.stats).map(stat -> stat.value);
        int sum = statValues.reduce(0, (cur, prev) -> cur + prev);
        return sum;
    }

    public int getExhaustion() {
        return this.exhaustionPoint;
    }

    public void addExhaustion() {
        this.exhaustionPoint++;
    }

    public Type[] getPokeTypes() {
        return this.types;
    }

    public LogPokemon getLogData() {

        return new LogPokemon(this.name, this.backSpriteUrl, this.frontSpriteUrl);
    }

    public String getName() {
        return this.name;
    }

    public Attack[] getFinishingBlows() {
        Attack[] finishingAttacks = Arrays.stream(this.attacks).filter(Attack::doesDamage).toArray(Attack[]::new);
        return finishingAttacks;
    }

    private static int[] getMoveSelection(int maxIndex) {
        int selectionSize = Math.min(4, maxIndex + 1);
        int[] randomMoveIndices = new int[selectionSize];
        for (int i = 0; i < selectionSize; i++) {
            int randomIndex;
            do {

                randomIndex = (int) Math.round(Math.random() * maxIndex);
            } while (moveIndexTaken(randomMoveIndices, randomIndex, i));
            randomMoveIndices[i] = randomIndex;
        }
        return randomMoveIndices;
    }

    private static boolean moveIndexTaken(int[] indices, int newIndex, int trimIndex) {
        return Arrays.stream(indices).limit(trimIndex).anyMatch(index -> index == newIndex);
    }

    private Attack[] creatAttacks(MoveBySearch[] moves) {
        String[] filteredURLs = Arrays.stream(moves).filter(MoveBySearch::isSupported).map(move -> move.move.url)
                .toArray(String[]::new);
        int moveAmount = filteredURLs.length;
        int[] selectedIndices = getMoveSelection(moveAmount - 1);
        // TODO: throw exception and use "Verzweifler" if selectionSize is less than 1
        int selectionSize = Math.min(4, moveAmount);
        Attack[] selectedAttacks = new Attack[selectionSize];
        try {
            for (int k = 0; k < selectionSize; k++) {
                int selectedIndex = selectedIndices[k];
                String selectedURL = filteredURLs[selectedIndex];
                selectedAttacks[k] = createAttack(selectedURL);
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("___INDEX OUT OF BOUNCE DURING ATTACK SELECTION___" + e.getClass());
        }
        ;

        return selectedAttacks;
    }

    static private Attack createAttack(String URL) {
        String attackPath = Pokedex.getPathFromURL(URL);
        try {
            MoveSearch move = Pokedex.getPokeData(attackPath, MoveSearch.class, RequestMode.JAVA_11);
            Attack attack = move.convert();
            return attack;
        } catch (IOException | InterruptedException e) {
            System.out.println("___ATTACK BUILDING FAILED___" + e.getClass());
        }
        return new Attack();
    }

}
