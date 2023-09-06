package ru.karmazin.shorturl.service;

import org.springframework.stereotype.Service;

@Service
public class UrlShortening {
    private static final String templateChar = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    public String idToShortURL(long n)
    {
        StringBuilder shortUrl = new StringBuilder();

        while (n > 0)
        {
            shortUrl.append(templateChar.charAt((int) (n % 62)));
            n = n/62;
        }
        return shortUrl.reverse().toString();
    }

    public long shortURLtoID(String shortURL)
    {
        long id = 0;

        for (int i=0; i < shortURL.length(); i++)
        {
            char ch = shortURL.charAt(i);
            if ('a' <= ch && ch <= 'z') {
                id = id*62 + ch - 'a';
            }
            if ('A' <= ch && ch <= 'Z') {
                id = id*62 + ch - 'A' + 26;
            }
            if ('0' <= ch && ch <= '9') {
                id = id*62 + ch - '0' + 52;
            }
        }
        return id;
    }
}
