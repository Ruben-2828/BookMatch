package com.example.bookmatch.ui.main;

import java.util.Arrays;

public class GenreMapping {
    private static final String[] ENGLISH_GENRES = {
            "Art",
            "Architecture",
            "Biography",
            "Computers",
            "Cooking",
            "Design",
            "Drama",
            "Education",
            "Fiction",
            "Games",
            "History",
            "Juvenile fiction",
            "Law",
            "Mathematics",
            "Music",
            "Nature",
            "Pets",
            "Philosophy",
            "Religion",
            "Science",
            "Sports",
            "Technology",
            "Travel"
    };

    private static final String[] ITALIAN_GENRES = {
            "Arte",
            "Architettura",
            "Biografie",
            "Informatica",
            "Cucina",
            "Design",
            "Teatro",
            "Educazione",
            "Narrativa",
            "Giochi",
            "Storia",
            "Narrativa per ragazzi",
            "Diritto",
            "Matematica",
            "Musica",
            "Natura",
            "Animali domestici",
            "Filosofia",
            "Religione",
            "Scienza",
            "Sport",
            "Tecnologia",
            "Viaggi"
    };

    public static String getEnglishGenre(String italianGenre) {
        int index = Arrays.asList(ITALIAN_GENRES).indexOf(italianGenre);
        if (index != -1 && index < ENGLISH_GENRES.length) {
            return ENGLISH_GENRES[index];
        }
        return null;
    }
}