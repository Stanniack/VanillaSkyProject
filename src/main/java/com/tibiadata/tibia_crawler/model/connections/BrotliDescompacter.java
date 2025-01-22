package com.tibiadata.tibia_crawler.model.connections;

import java.io.*;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.zip.GZIPInputStream;
import org.brotli.dec.BrotliInputStream;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 *
 * @author Devmachine
 */
@Component
@Scope("prototype")
public class BrotliDescompacter {

    public String gzipBrDescompacter(String url) throws MalformedURLException, IOException {
        StringBuilder response = null;

        HttpURLConnection connection = getHttpURLConnection(url);

        InputStream inputStream = connection.getInputStream();
        String encoding = connection.getContentEncoding();

        if ("gzip".equalsIgnoreCase(encoding)) {
            inputStream = new GZIPInputStream(inputStream);
        } else if ("br".equalsIgnoreCase(encoding)) {
            inputStream = new BrotliInputStream(inputStream);
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line).append("\n");
            }
        } finally {
            connection.disconnect(); // fecha a conexão
        }

        return response.toString();
    }

    private static HttpURLConnection getHttpURLConnection(String url) throws IOException {
        URL link = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) link.openConnection();

        // Desabilitar cache
        connection.setRequestProperty("Cache-Control", "no-cache, no-store, must-revalidate");
        connection.setRequestProperty("Pragma", "no-cache");
        connection.setRequestProperty("Expires", "0");

        // Outros cabeçalhos
        connection.setRequestProperty("Accept-Encoding", "gzip, br");
        connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36");
        return connection;
    }

}
