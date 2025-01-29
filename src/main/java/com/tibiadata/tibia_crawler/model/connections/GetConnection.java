package com.tibiadata.tibia_crawler.model.connections;

import com.tibiadata.tibia_crawler.model.utils.ProxiesUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.util.Random;

@Component
public class GetConnection {
    private static HttpURLConnection connection = null;

    /**
     * Retorna um proxy aleatório da lista de proxies configurada.
     *
     * @return Um proxy no formato "host:port" escolhido aleatoriamente.
     */
    private static String getRandomProxy() {
        Random random = new Random();
        return ProxiesUtils.getHttpsBrProxies().get(random.nextInt(ProxiesUtils.getHttpsBrProxies().size()));
    }

    /**
     * Estabelece uma conexão HTTP com a URL especificada, sem o uso de proxies.
     * <p>
     * O método cria uma conexão direta com a URL fornecida e define os cabeçalhos
     * padrão da requisição.
     *
     * @param url a URL para a qual a conexão será aberta.
     * @return uma instância de {@link HttpURLConnection} configurada.
     * @throws IOException se ocorrer um erro ao abrir a conexão.
     */
    public static HttpURLConnection getHttpURLConnection(String url) throws IOException {
        URL link = new URL(url);
        connection = (HttpURLConnection) link.openConnection();

        setConnectionProperties(); // configura o cabeçalho da conexão

        return connection;
    }

    /**
     * Estabelece uma conexão HTTP com a URL especificada utilizando proxies aleatórios.
     * O método tenta conectar repetidamente até encontrar um proxy funcional.
     *
     * @param url A URL de destino para a conexão.
     * @return Uma instância de {@link HttpURLConnection} configurada e pronta para uso.
     * @throws IOException Se nenhum proxy disponível funcionar.
     */
    public static HttpURLConnection getHttpURLConnectionWithProxy(String url) throws IOException {
        // Tentativa de conectar usando um proxy válido da lista
        while (true) {
            try {
                String proxy = getRandomProxy();  // Escolhe um proxy aleatório
                connection = getUrlProxyConnection(url, proxy);  // Tenta a conexão com o proxy
                break;  // Se a conexão for bem-sucedida, sai do loop
            } catch (IOException ignored) {
                // Se falhar, tenta outro proxy aleatório
            }
        }

        if (connection == null) {
            throw new IOException("Nenhum proxy disponível funcionou.");
        }

        setProxiesSupport(); //Configura suporte para proxies SOCKS4, HTTP E HTTPS
        setConnectionProperties(); // configura o cabeçalho da conexão

        return connection;
    }

    /**
     * Cria e configura uma conexão HTTP para a URL fornecida, usando o proxy informado.
     *
     * @param url   URL para a qual será feita a requisição.
     * @param proxy Proxy no formato "host:port" a ser utilizado na conexão.
     * @return A conexão HTTP configurada com o proxy.
     * @throws IOException Se ocorrer um erro ao abrir a conexão ou configurar o proxy.
     */
    public static HttpURLConnection getUrlProxyConnection(String url, String proxy) throws IOException {
        URL link = new URL(url);

        // Selecionar o proxy informado
        String[] proxyParts = proxy.split(":");
        String proxyHost = proxyParts[0];
        int proxyPort = Integer.parseInt(proxyParts[1]);

        // Configurar proxy para a conexão
        Proxy proxyObj = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHost, proxyPort));
        connection = (HttpURLConnection) link.openConnection(proxyObj);

        return connection;
    }

    /**
     * Configura as propriedades da conexão HTTP.
     * Define cabeçalhos para desabilitar o cache, permitir redirecionamentos
     * e especificar o encoding e o agente do usuário.
     */
    private static void setConnectionProperties() {
        // Desabilitar cache
        connection.setRequestProperty("Cache-Control", "no-cache, no-store, must-revalidate");
        connection.setRequestProperty("Pragma", "no-cache");
        connection.setRequestProperty("Expires", "0");
        connection.setInstanceFollowRedirects(true);  // Isso permite que o redirecionamento seja seguido

        // Outros cabeçalhos
        connection.setRequestProperty("Accept-Encoding", "gzip, br");
        connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36");
    }

    /**
     * Configura os proxies para os protocolos SOCKS, HTTP e HTTPS.
     * <p>
     * Este método define as propriedades do sistema para direcionar o tráfego de rede através
     * de proxies específicos para SOCKS, HTTP e HTTPS.
     */
    private static void setProxiesSupport() {
        // Para SOCKS
        System.setProperty("socksProxyHost", "proxy_socks_host");
        System.setProperty("socksProxyPort", "proxy_socks_port");

        // Para HTTP
        System.setProperty("http.proxyHost", "proxy_http_host");
        System.setProperty("http.proxyPort", "proxy_http_port");

        // Para HTTPS
        System.setProperty("https.proxyHost", "proxy_https_host");
        System.setProperty("https.proxyPort", "proxy_https_port");
    }
}
