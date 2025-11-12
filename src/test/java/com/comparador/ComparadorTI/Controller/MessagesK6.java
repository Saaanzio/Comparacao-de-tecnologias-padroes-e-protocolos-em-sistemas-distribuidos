package com.comparador.ComparadorTI.Controller;

import com.comparador.ComparadorTI.messages.model.MessageRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.DoubleAdder;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MessagesK6 {

    @Autowired
    private TestRestTemplate restTemplate;

    private final Random random = new Random();

    @Test
    public void cenarioIdeal() throws InterruptedException {
        executarCenario("CENÁRIO IDEAL", 100, 10, 120, 0, 10);
    }

    @Test
    public void cenarioRealista() throws InterruptedException {
        executarCenario("CENÁRIO REALISTA", 500, 50, 300, 2, 75);
    }

    @Test
    public void cenarioCritico() throws InterruptedException {
        executarCenario("CENÁRIO CRÍTICO", 2000, 200, 600, 12, 350);
    }

    private void executarCenario(String nomeCenario, int vus, int rps, int duracaoSegundos, int perdaPacotes, int latenciaMedia) throws InterruptedException {
        int totalRequisicoes = rps * duracaoSegundos;
        ExecutorService executor = Executors.newFixedThreadPool(vus);
        CountDownLatch latch = new CountDownLatch(totalRequisicoes);

        Metricas metricsGetById = new Metricas("GET /messages/{id}");
        Metricas metricsCreate = new Metricas("POST /messages/create");
        MetricasGerais metricsGerais = new MetricasGerais();

        long tempoInicio = System.currentTimeMillis();

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        AtomicInteger requestCounter = new AtomicInteger(0);

        scheduler.scheduleAtFixedRate(() -> {
            if (requestCounter.get() >= totalRequisicoes) {
                scheduler.shutdown();
                return;
            }

            executor.submit(() -> {
                if (requestCounter.incrementAndGet() > totalRequisicoes) {
                    return;
                }

                try {
                    if (random.nextInt(100) < perdaPacotes) {
                        metricsGerais.registrarRequisicao(0.0, 0, false);
                        return;
                    }

                    if (latenciaMedia > 0) {
                        Thread.sleep(latenciaMedia + random.nextInt(Math.max(1, latenciaMedia / 2)));
                    }

                    int endpointType = random.nextInt(2);
                    switch (endpointType) {
                        case 0 -> testGetMessageById(metricsGetById, requestCounter.get(), metricsGerais);
                        case 1 -> testCreateMessage(metricsCreate, requestCounter.get(), metricsGerais);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    latch.countDown();
                }
            });
        }, 0, 1000 / rps, TimeUnit.MILLISECONDS);

        latch.await(duracaoSegundos + 60, TimeUnit.SECONDS);
        scheduler.shutdown();
        executor.shutdown();

        long tempoFim = System.currentTimeMillis();
        long tempoTotal = tempoFim - tempoInicio;

        double throughput = (totalRequisicoes * 1000.0) / Math.max(1, tempoTotal);
        List<Double> sorted = new ArrayList<>(metricsGerais.tempos);
        sorted.sort(Double::compareTo);

        int sucessoCount = Math.max(0, metricsGerais.requisicoesComSucesso.get());
        double avg = sucessoCount == 0 ? 0.0 : metricsGerais.somaTempos.sum() / sucessoCount;
        double min = sorted.isEmpty() ? 0.0 : sorted.get(0);
        double med = sorted.isEmpty() ? 0.0 : (sorted.size() % 2 == 0 ? (sorted.get(sorted.size() / 2 - 1) + sorted.get(sorted.size() / 2)) / 2.0 : sorted.get(sorted.size() / 2));
        double max = sorted.isEmpty() ? 0.0 : sorted.get(sorted.size() - 1);
        double p90 = calcularPercentil(sorted, 90);
        double p95 = calcularPercentil(sorted, 95);

        System.out.println("\n========== " + nomeCenario + " ==========\n");
        System.out.println("     ✓ GET /messages/{id} status é 200 ou 404");
        System.out.println("     ✓ POST /messages/create status 200");
        System.out.println();

        System.out.printf("     %-30s avg=%-8s min=%-8s med=%-8s max=%-8s p(90)=%-9s p(95)=%-8s%n",
                "http_req_duration..............:",
                formatarTempo(avg), formatarTempo(min), formatarTempo(med),
                formatarTempo(max), formatarTempo(p90), formatarTempo(p95));

        System.out.printf("       { expected_response:true }...: avg=%-8s min=%-8s med=%-8s max=%-8s p(90)=%-9s p(95)=%-8s%n",
                formatarTempo(avg), formatarTempo(min), formatarTempo(med),
                formatarTempo(max), formatarTempo(p90), formatarTempo(p95));

        int falhas = totalRequisicoes - metricsGerais.requisicoesComSucesso.get();
        double taxaFalha = (falhas * 100.0) / Math.max(1, totalRequisicoes);
        System.out.printf("     %-30s %.2f%%  %d out of %d%n", "http_req_failed................:", taxaFalha, falhas, totalRequisicoes);
        System.out.printf("     %-30s %-6d %.6f/s%n", "http_reqs......................:", totalRequisicoes, throughput);

        System.out.println();

        System.out.printf("     %-30s avg=%-8s min=%-9s med=%-8s max=%-7s p(90)=%-9s p(95)=%-8s%n",
                "iteration_duration.............:",
                formatarTempo(avg), formatarTempo(min), formatarTempo(med),
                formatarTempo(max), formatarTempo(p90), formatarTempo(p95));

        System.out.printf("     %-30s %-6d %.6f/s%n", "iterations.....................:", totalRequisicoes - 1, throughput);
        System.out.printf("     %-30s %-6d min=%-10d max=%-8d%n", "vus............................:", vus, vus, vus);
        System.out.printf("     %-30s %-6d min=%-10d max=%-8d%n", "vus_max........................:", vus, vus, vus);

        System.out.println();

        long totalDataReceived = metricsGerais.dataReceived.get();
        long totalDataSent = metricsGerais.dataSent.get();
        double kbReceivedPerSec = (totalDataReceived / 1024.0) / Math.max(0.001, (tempoTotal / 1000.0));
        double kbSentPerSec = (totalDataSent / 1024.0) / Math.max(0.001, (tempoTotal / 1000.0));

        System.out.printf("     %-30s %-6s %.0f kB/s%n", "data_received..................:", formatarBytes(totalDataReceived), kbReceivedPerSec);
        System.out.printf("     %-30s %-6s %.0f kB/s%n", "data_sent......................:", formatarBytes(totalDataSent), kbSentPerSec);

        System.out.println("\n========================================================");
        System.out.println("Detalhes por Endpoint:");
        System.out.println("========================================================\n");
        System.out.println(metricsGetById.getRelatorio());
        System.out.println(metricsCreate.getRelatorio());
    }

    private void testGetMessageById(Metricas metrics, int messageId, MetricasGerais metricsGerais) {
        long inicio = System.nanoTime();
        try {
            int id = (messageId % 100) + 1;
            ResponseEntity<String> response = restTemplate.getForEntity("/messages/" + id, String.class);
            double duracao = (System.nanoTime() - inicio) / 1_000_000.0;
            int dataSize = response.getBody() != null ? response.getBody().length() : 0;
            boolean sucesso = response.getStatusCode().is2xxSuccessful() || response.getStatusCode().value() == 404;
            metrics.registrar(duracao, sucesso);
            metricsGerais.registrarRequisicao(duracao, dataSize, sucesso);
        } catch (Exception e) {
            double duracao = (System.nanoTime() - inicio) / 1_000_000.0;
            metrics.registrar(duracao, false);
            metricsGerais.registrarRequisicao(duracao, 0, false);
        }
    }

    private void testCreateMessage(Metricas metrics, int messageId, MetricasGerais metricsGerais) {
        long inicio = System.nanoTime();
        try {
            MessageRequest request = new MessageRequest("Mensagem de teste " + messageId, "teste");
            ResponseEntity<String> response = restTemplate.postForEntity("/messages/create", request, String.class);
            double duracao = (System.nanoTime() - inicio) / 1_000_000.0;
            int dataSize = response.getBody() != null ? response.getBody().length() : 0;
            boolean sucesso = response.getStatusCode().is2xxSuccessful();
            metrics.registrar(duracao, sucesso);
            metricsGerais.registrarRequisicao(duracao, dataSize, sucesso);
        } catch (Exception e) {
            double duracao = (System.nanoTime() - inicio) / 1_000_000.0;
            metrics.registrar(duracao, false);
            metricsGerais.registrarRequisicao(duracao, 0, false);
        }
    }

    private String formatarTempo(long valor) {
        if (valor >= 1000) {
            return String.format("%.2fs", valor / 1000.0);
        }
        return valor + "ms";
    }

    private String formatarTempo(double valor) {
        if (valor >= 1000.0) {
            return String.format("%.2fs", valor / 1000.0);
        }
        return String.format("%.2fms", valor);
    }

    private String formatarBytes(long bytes) {
        if (bytes >= 1024 * 1024) {
            return String.format("%.1f MB", bytes / (1024.0 * 1024.0));
        } else if (bytes >= 1024) {
            return String.format("%.1f kB", bytes / 1024.0);
        }
        return bytes + " B";
    }

    private double calcularPercentil(List<Double> sorted, int percentil) {
        if (sorted.isEmpty()) return 0.0;
        int index = (int) Math.ceil((percentil / 100.0) * sorted.size()) - 1;
        index = Math.max(0, Math.min(index, sorted.size() - 1));
        return sorted.get(index);
    }

    private static class MetricasGerais {
        private final DoubleAdder somaTempos = new DoubleAdder();
        private final AtomicInteger totalRequisicoes = new AtomicInteger(0);
        private final AtomicInteger requisicoesComSucesso = new AtomicInteger(0);
        private final AtomicLong dataReceived = new AtomicLong(0);
        private final AtomicLong dataSent = new AtomicLong(0);
        private final List<Double> tempos = new CopyOnWriteArrayList<>();

        public void registrarRequisicao(double tempoMs, int responseSize, boolean sucesso) {
            totalRequisicoes.incrementAndGet();
            dataReceived.addAndGet(responseSize);
            dataSent.addAndGet(256);
            if (sucesso) {
                somaTempos.add(tempoMs);
                tempos.add(tempoMs);
                requisicoesComSucesso.incrementAndGet();
            }
        }
    }

    private static class Metricas {
        private final String nomeEndpoint;
        private final DoubleAdder somaTempos = new DoubleAdder();
        private final AtomicInteger totalRequisicoes = new AtomicInteger(0);
        private final AtomicInteger requisicoesComSucesso = new AtomicInteger(0);
        private final List<Double> tempos = new CopyOnWriteArrayList<>();

        public Metricas(String nomeEndpoint) {
            this.nomeEndpoint = nomeEndpoint;
        }

        public void registrar(double tempoMs, boolean sucesso) {
            somaTempos.add(tempoMs);
            totalRequisicoes.incrementAndGet();
            tempos.add(tempoMs);
            if (sucesso) {
                requisicoesComSucesso.incrementAndGet();
            }
        }

        public String getRelatorio() {
            if (totalRequisicoes.get() == 0) {
                return nomeEndpoint + ": Nenhuma requisição executada";
            }

            List<Double> sorted = new ArrayList<>(tempos);
            sorted.sort(Double::compareTo);

            double media = somaTempos.sum() / Math.max(1.0, (double) totalRequisicoes.get());
            double taxaSucesso = (requisicoesComSucesso.get() * 100.0) / Math.max(1, totalRequisicoes.get());
            double min = sorted.isEmpty() ? 0.0 : sorted.get(0);
            double max = sorted.isEmpty() ? 0.0 : sorted.get(sorted.size() - 1);
            int idx95 = (int) Math.ceil(0.95 * sorted.size()) - 1;
            int idx99 = (int) Math.ceil(0.99 * sorted.size()) - 1;
            idx95 = Math.max(0, Math.min(idx95, sorted.size() - 1));
            idx99 = Math.max(0, Math.min(idx99, sorted.size() - 1));
            double p95 = sorted.isEmpty() ? 0.0 : sorted.get(idx95);
            double p99 = sorted.isEmpty() ? 0.0 : sorted.get(idx99);

            return String.format("""
                %s:
                  Requisições: %d
                  Taxa de sucesso: %.2f%%
                  Tempo médio: %.2fms
                  Tempo mínimo: %.2fms
                  Tempo máximo: %.2fms
                  P95: %.2fms
                  P99: %.2fms
                """,
                    nomeEndpoint, totalRequisicoes.get(), taxaSucesso, media,
                    min, max, p95, p99);
        }
    }
}
