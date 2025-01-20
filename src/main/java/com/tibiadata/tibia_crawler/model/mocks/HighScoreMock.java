package com.tibiadata.tibia_crawler.model.mocks;

import com.tibiadata.tibia_crawler.model.connections.GetContent;
import com.tibiadata.tibia_crawler.model.parsers.characterservice.CharacterProcessor;
import com.tibiadata.tibia_crawler.model.utils.ElementsUtils;
import com.tibiadata.tibia_crawler.model.utils.TibiaUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author Devmachine
 */
@Component
public class HighScoreMock {

    private static final int NICKNAME_INDEX = 1;

    private final CharacterProcessor characterProcessor;
    private GetContent getContent;
    private ElementsUtils elementsUtil;

    @Autowired
    public HighScoreMock(CharacterProcessor characterProcessor, ElementsUtils elementsUtils, GetContent getContent) {
        this.characterProcessor = characterProcessor;
        this.getContent = getContent;
        this.elementsUtil = elementsUtils;
    }

    /**
     * Obtém informações detalhadas dos jogadores do ranking a partir de uma URL
     * fornecida, usando os filtros especificados. O método recupera a lista de
     * jogadores a partir da URL e filtra os dados conforme os parâmetros.
     *
     * @param url A URL do ranking de jogadores.
     * @param filter1 O primeiro filtro utilizado para processar os dados.
     * @param filter2 O segundo filtro utilizado para processar os dados.
     * @return Uma lista contendo as informações detalhadas dos jogadores.
     * @throws IOException Se ocorrer um erro ao obter ou processar os dados da
     * página.
     */
    public List<List<String>> getHighscorePlayers(String url, String filter1, String filter2) throws IOException {
        List<List<String>> content = getContent.getPlayersInfo(
                url,
                filter1,
                filter2);

        return content;
    }

    /**
     * Obtém os nomes dos jogadores do ranking a partir da URL fornecida, usando
     * os filtros especificados. O método chama
     * {@link #getHighscorePlayers(String, String, String)} para obter os dados
     * e extrai os nomes dos jogadores.
     *
     * @param url A URL do ranking de jogadores.
     * @param filter1 O primeiro filtro utilizado para processar os dados.
     * @param filter2 O segundo filtro utilizado para processar os dados.
     * @return Uma lista contendo os nomes dos jogadores.
     * @throws IOException Se ocorrer um erro ao obter ou processar os dados da
     * página.
     */
    public List<String> getHighscoreNicks(String url, String filter1, String filter2) throws IOException {
        List<String> nicks = new ArrayList<>();
        List<List<String>> content = getHighscorePlayers(
                url,
                filter1,
                filter2);

        for (List<String> playerInfo : content) {
            nicks.add(playerInfo.get(NICKNAME_INDEX));
        }

        return nicks;
    }

    /**
     * Método responsável por simular o processamento dos jogadores do ranking
     * utilizando múltiplas threads.
     *
     * Divide o trabalho de processamento de páginas em múltiplas threads, sendo
     * que cada thread processa um intervalo de páginas especificado. O número
     * de threads e páginas por thread pode ser configurado conforme necessário.
     *
     * O método cria um pool de threads com o número especificado de threads e
     * distribui as páginas entre as threads, executando o processamento em
     * paralelo.
     *
     * Após o início das tarefas, o ExecutorService é finalizado utilizando o
     * método shutdown, aguardando o término das tarefas em execução.
     */
    public void mockGlobalHighscorePlayers() {

        int totalThreads = 1; // Número de threads conforme necessário
        int pagesPerThread = 20; // Número de páginas por thread

        ExecutorService executor = Executors.newFixedThreadPool(totalThreads); // Criação de pool com o número de threads especificado

        for (int i = 0; i < totalThreads; i++) {
            final int start = i * pagesPerThread;
            final int end = (i + 1) * pagesPerThread;
            Runnable task = () -> processGlobalHighscorePages(start, end); // Criação de uma nova tarefa para cada thread
            executor.execute(task); // Execução da tarefa
        }
        executor.shutdown(); // Finaliza o ExecutorService
    }

    /**
     * Processa uma faixa de páginas do ranking GLOBAL, chamando o método
     * {@link CharacterProcessor#processNames} para cada página. Caso ocorra uma
     * exceção de entrada/saída (IOException), a página atual será repetida até
     * a execução ser bem-sucedida. O processamento de cada página é feito com
     * base na URL fornecida, usando os filtros fornecidos para obter os nomes
     * dos jogadores.
     *
     * @param start O índice inicial da página a ser processada.
     * @param end O índice final da página a ser processada.
     * @throws IOException Se ocorrer um erro ao obter ou processar os dados da
     * página.
     */
    private void processGlobalHighscorePages(int start, int end) {
        for (int i = start; i < end; i++) {
            try {
                characterProcessor.processNames(
                        getHighscoreNicks(
                                "https://www.tibia.com/community/?subtopic=highscores&world=&beprotection=-1&category=6&profession=&currentpage=" + (i + 1),
                                elementsUtil.getTrBgcolor2(),
                                elementsUtil.getTd()));

            } catch (IOException ex) {
                Logger.getLogger(CharacterProcessor.class.getName()).log(Level.SEVERE, "Erro ao processar pagina");
                i--; // Se a internet cair ou algum erro lançado, ele continua na mesma página até se estabelecer novamente

                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();//
                }
            }
        }
    }

    /**
     * Processa todas as páginas de pontuação de todos os mundos, profissões e
     * páginas possíveis, distribuindo o trabalho entre múltiplas threads para
     * otimizar o tempo de execução.
     *
     * A função divide o total de páginas de pontuação entre as threads de forma
     * que cada thread processe um subconjunto específico de páginas, sem
     * sobreposição de dados entre as threads. Após o processamento, as threads
     * são finalizadas.
     */
    public void processAllHighscorePages() {
        List<String> worlds = TibiaUtils.getWorlds();
        int totalThreads = 10; // Número máximo de threads permitidas

        List<HighscoreTask> tasks = new ArrayList<>();

        // Gera todas as combinações possíveis de (mundo, profissão, página)
        for (String world : worlds) {
            for (int j = 1; j <= 5; j++) { // 5 profissões
                for (int k = 1; k <= 20; k++) { // 20 páginas por profissão
                    tasks.add(new HighscoreTask(world, j, k));
                }
            }
        }

        int totalTasks = tasks.size(); // Número total de combinações (mundo, profissão, página)
        int tasksPerThread = (int) Math.ceil(totalTasks / (double) totalThreads); // Divide as tarefas entre as threads

        ExecutorService executor = Executors.newFixedThreadPool(totalThreads); // Cria um pool fixo de threads

        // Divisão das tarefas entre as threads
        for (int i = 0; i < totalThreads; i++) {
            final int start = i * tasksPerThread; // Índice inicial para a thread
            final int end = Math.min(start + tasksPerThread, totalTasks); // Índice final da thread (evita ultrapassar o tamanho da lista)

            List<HighscoreTask> threadTasks = tasks.subList(start, end); // Define o subconjunto de tarefas para esta thread

            Runnable task = () -> {
                for (HighscoreTask highscoreTask : threadTasks) {
                    try {
                        // Monta a URL com os parâmetros do mundo, profissão e página
                        List<String> content = getHighscoreNicks(
                                String.format(
                                        "https://www.tibia.com/community/?subtopic=highscores&world=%s&beprotection=-1&category=6&profession=%d&currentpage=%d",
                                        highscoreTask.world, highscoreTask.profession, highscoreTask.page
                                ),
                                elementsUtil.getTrBgcolor2(),
                                elementsUtil.getTd()
                        );

                        // Processa os nicks obtidos da página
                        for (String nick : content) {
                            characterProcessor.processName(nick);
                        }
                    } catch (IOException ex) {
                        Logger.getLogger(HighScoreMock.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            };

            executor.execute(task); // Envia a tarefa para ser executada no pool de threads
        }

        executor.shutdown(); // Finaliza o ExecutorService após a execução de todas as threads
    }

    /**
     * Classe auxiliar que representa uma tarefa de processamento de pontuação
     * de um mundo, profissão e página específicos.
     */
    private static class HighscoreTask {

        String world; // Mundo a ser processado
        int profession; // Profissão a ser processada
        int page; // Página de pontuação a ser processada

        /**
         * Construtor da classe HighscoreTask.
         *
         * @param world o mundo a ser processado
         * @param profession a profissão a ser processada
         * @param page a página a ser processada
         */
        HighscoreTask(String world, int profession, int page) {
            this.world = world;
            this.profession = profession;
            this.page = page;
        }
    }

}
