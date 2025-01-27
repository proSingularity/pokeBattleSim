package com.example.demo.Pokedex;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

import com.example.demo.JSONHandler;
import com.example.demo.RequestMode;
import com.example.demo.Searches.PokemonSearch.PokemonSearch;

public class Pokedex {
    /**
     *
     */
    public static final int CLASSICAL_POKEMON_RANGE = 151;
    public static final String HOST = "https://pokeapi.co/";
    public static final String API_PATH = "api/v2/";
    public static final String POKEMON_PATH = "pokemon/";
    public static final String CLASSICAL_POKEMON_QUERY = "?limit=" + CLASSICAL_POKEMON_RANGE + "/";
    public static final String RELEVANT_ATTACKS = "move/?offset=0&limit=200";// actually, it suffices to get 0-165
    public static final String TYPE_PATH = "type/";
    public static final String CLASSICAL_VERSION_PATH = "version-group/1/";
    public static final String GENERATION_I_PATH = "generation/1/";
    public static final String CLASSICAL_TYPES_PATH = "type?limit=16";

    public static String getClassicalVersionKey() {
        String key = "red-blue";
        try {
            key = getPokeData(API_PATH + CLASSICAL_VERSION_PATH, Name.class, RequestMode.JAVA_11).name;
        } catch (IOException | InterruptedException e) {
            System.out.println("___VERSION NAME NOT FOUND___" + e.getClass());
        }
        return key;
    }

    public static <T> T getPokeData(String path, Class<T> type, RequestMode mode)
            throws IOException, InterruptedException, RuntimeException {
        return mode == RequestMode.TRADITIONAL_OLD ? getPokeDataNotModernly(path, type)
                : getPokeDataModernly(path, type);
    }

    private static <T> T getPokeDataModernly(String path, Class<T> type) throws IOException, InterruptedException {
        var url = URI.create(HOST + path);
        var client = HttpClient.newHttpClient();
        var request = HttpRequest.newBuilder(url).build();
        var response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String responseBody = response.body();
        T data = JSONHandler.convertJSON(responseBody, type);
        return data;
    }

    private static <T> T getPokeDataNotModernly(String path, Class<T> type) throws IOException, RuntimeException {
        URL pokemonsURL = new URL(HOST + path);
        HttpURLConnection connection = (HttpURLConnection) pokemonsURL.openConnection();
        connection.setRequestMethod("GET");
        connection.connect();
        int responseCode = connection.getResponseCode();
        if (responseCode != 200) {
            throw new RuntimeException("Request failed: Http Error " + responseCode);
        }
        String responseString = "";
        Scanner scanner = new Scanner(pokemonsURL.openStream());
        while (scanner.hasNext()) {
            responseString += scanner.nextLine();
        }
        T data = JSONHandler.convertJSON(responseString, type);
        scanner.close();
        connection.disconnect();
        return data;
    }

    public static String getStringPokeData(int index) throws IOException, InterruptedException {
        var url = URI.create(HOST + POKEMON_PATH + index);
        var client = HttpClient.newHttpClient();
        var request = HttpRequest.newBuilder(url).build();
        var response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String responseBody = response.body();
        return responseBody;
    }

    public static PokemonSearch getPokemon(int index, RequestMode mode)
            throws RuntimeException, IOException, InterruptedException {
        if (index <= 0 || index > CLASSICAL_POKEMON_RANGE) {
            throw new RuntimeException("___ONLY CLASSICAL POKEMON ALLOWED___");
        }
        String pathByPokeIndex = API_PATH + POKEMON_PATH + index + "/";
        PokemonSearch pokemon = getPokeData(pathByPokeIndex, PokemonSearch.class, mode);
        return pokemon;
    }

    public static PokemonSearch getPokemon(String name, RequestMode mode)
            throws RuntimeException, IOException, InterruptedException {
        String pathByPokeName = API_PATH + POKEMON_PATH + name + "/";
        return getPokeData(pathByPokeName, PokemonSearch.class, mode);
    }

    public static PokemonSearch[] getRandomTeam(RequestMode mode)
            throws RuntimeException, IOException, InterruptedException {
        PokemonSearch[] pokemonTeam = new PokemonSearch[6];
        for (int i = 0; i < 6; i++) {
            pokemonTeam[i] = getPokemon(getRandomPokeIndex(), mode);
        }

        return pokemonTeam;
    }

    private static int getRandomPokeIndex() {
        int index = 1 + (int) Math.floor(Math.random() * CLASSICAL_POKEMON_RANGE);
        return index;
    }

    public static <T> T getPokeDataByURL(String url, Class<T> type, RequestMode mode)
            throws IOException, InterruptedException {
        String pathFromURL = url.substring(HOST.length());
        return getPokeData(pathFromURL, type, mode);
    }

    public static String CLASSICAL_VERSION_KEY = getClassicalVersionKey();
}

class Name {
    public String name;
}