package ru.karmazin.shorturl;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.karmazin.shorturl.service.UrlShortening;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class UrlShorteningTests {
    private final UrlShortening urlShortening = new UrlShortening();

    @Test
    public void encode_lessThan62() {
        assertEquals("k", urlShortening.idToShortURL(10));
    }

    @Test
    public void encode_moreThan62() {
        assertEquals("bq", urlShortening.idToShortURL(78));
    }

    @Test
    public void decode_singleCharacter() {
        assertEquals(11, urlShortening.shortURLtoID("l"));
    }
}
