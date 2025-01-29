package com.tibiadata.tibia_crawler.model.connections;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.zip.GZIPInputStream;

import com.tibiadata.tibia_crawler.model.utils.ProxiesUtils;
import org.brotli.dec.BrotliInputStream;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
public class BrotliDescompacter {

    /**
     * Realiza uma requisição HTTP para a URL fornecida e descompacta a resposta no formato GZIP ou Brotli.
     *
     * @param url URL a ser requisitada.
     * @return O conteúdo da resposta HTTP, descompactado e como uma String.
     * @throws IOException Se ocorrer um erro ao fazer a requisição ou ao ler a resposta.
     */
    public String gzipBrDescompacter(String url) throws IOException {
        StringBuilder response = null;

        HttpURLConnection connection = GetConnection.getHttpURLConnection(url);

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
}
