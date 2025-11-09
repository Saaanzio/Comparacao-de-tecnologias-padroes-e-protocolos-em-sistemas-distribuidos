package com.comparador.ComparadorTI.Controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EventControllerTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testeDeStressGetAllEvents() throws InterruptedException {
        int numeroDeThreads = 100;
        int requisicoesPorThread = 10;

        ExecutorService executor = Executors.newFixedThreadPool(numeroDeThreads);
        AtomicLong menorTempo = new AtomicLong(Long.MAX_VALUE);
        AtomicLong maiorTempo = new AtomicLong(0);
        AtomicLong somaTempos = new AtomicLong(0);

        long tempoInicio = System.currentTimeMillis();

        for (int i = 0; i < numeroDeThreads; i++) {
            executor.submit(() -> {
                for (int j = 0; j < requisicoesPorThread; j++) {
                    long reqInicio = System.currentTimeMillis();
                    ResponseEntity<String> response = restTemplate.getForEntity("/all", String.class);
                    long reqFim = System.currentTimeMillis();
                    long tempoRequisicao = reqFim - reqInicio;

                    menorTempo.updateAndGet(min -> Math.min(min, tempoRequisicao));
                    maiorTempo.updateAndGet(max -> Math.max(max, tempoRequisicao));
                    somaTempos.addAndGet(tempoRequisicao);

                    System.out.println("Status da resposta: " + response.getStatusCode() + " - Tempo: " + tempoRequisicao + "ms");
                }
            });
        }

        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.MINUTES);

        long tempoFim = System.currentTimeMillis();
        long tempoTotal = tempoFim - tempoInicio;
        int totalRequisicoes = numeroDeThreads * requisicoesPorThread;
        long tempoMedioReal = somaTempos.get() / totalRequisicoes;

        System.out.println("\n=== TESTE DE STRESS - GET ALL EVENTS ===");
        System.out.println("Total de requisições: " + totalRequisicoes);
        System.out.println("Tempo total do teste: " + tempoTotal + "ms");
        System.out.println("Tempo médio por requisição: " + tempoMedioReal + "ms");
        System.out.println("Menor tempo de resposta: " + menorTempo.get() + "ms");
        System.out.println("Maior tempo de resposta: " + maiorTempo.get() + "ms");
        System.out.println("Requisições por segundo (throughput): " + (totalRequisicoes * 1000.0 / tempoTotal));
    }

    @Test
    public void testeDeStressCriarEventos() throws InterruptedException {
        int numeroDeRequisicoes = 500;
        ExecutorService executor = Executors.newFixedThreadPool(50);
        CountDownLatch latch = new CountDownLatch(numeroDeRequisicoes);
        AtomicLong menorTempo = new AtomicLong(Long.MAX_VALUE);
        AtomicLong maiorTempo = new AtomicLong(0);
        AtomicLong somaTempos = new AtomicLong(0);

        long tempoInicio = System.currentTimeMillis();

        for (int i = 0; i < numeroDeRequisicoes; i++) {
            final int eventId = i;
            executor.submit(() -> {
                try {
                    long reqInicio = System.currentTimeMillis();
                    String url = String.format("/create?type=Type%d&source=Source%d", eventId, eventId);
                    ResponseEntity<String> response = restTemplate.postForEntity(url, null, String.class);
                    long reqFim = System.currentTimeMillis();
                    long tempoRequisicao = reqFim - reqInicio;

                    menorTempo.updateAndGet(min -> Math.min(min, tempoRequisicao));
                    maiorTempo.updateAndGet(max -> Math.max(max, tempoRequisicao));
                    somaTempos.addAndGet(tempoRequisicao);

                    System.out.println("Evento " + eventId + " criado - Status: " + response.getStatusCode() + " - Tempo: " + tempoRequisicao + "ms");
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await(5, TimeUnit.MINUTES);
        executor.shutdown();

        long tempoFim = System.currentTimeMillis();
        long tempoTotal = tempoFim - tempoInicio;
        long tempoMedioReal = somaTempos.get() / numeroDeRequisicoes;

        System.out.println("\n=== TESTE DE STRESS - CRIAR EVENTOS ===");
        System.out.println("Total de requisições: " + numeroDeRequisicoes);
        System.out.println("Tempo total do teste: " + tempoTotal + "ms");
        System.out.println("Tempo médio por requisição: " + tempoMedioReal + "ms");
        System.out.println("Menor tempo de resposta: " + menorTempo.get() + "ms");
        System.out.println("Maior tempo de resposta: " + maiorTempo.get() + "ms");
        System.out.println("Requisições por segundo (throughput): " + (numeroDeRequisicoes * 1000.0 / tempoTotal));
    }

    @Test
    public void testeDeStressGetEvent() throws InterruptedException {
        int numeroDeThreads = 100;
        int requisicoesPorThread = 10;

        ExecutorService executor = Executors.newFixedThreadPool(numeroDeThreads);
        AtomicLong menorTempo = new AtomicLong(Long.MAX_VALUE);
        AtomicLong maiorTempo = new AtomicLong(0);
        AtomicLong somaTempos = new AtomicLong(0);

        long tempoInicio = System.currentTimeMillis();

        for (int i = 0; i < numeroDeThreads; i++) {
            executor.submit(() -> {
                for (int j = 0; j < requisicoesPorThread; j++) {
                    long reqInicio = System.currentTimeMillis();
                    ResponseEntity<String> response = restTemplate.getForEntity("/1", String.class);
                    long reqFim = System.currentTimeMillis();
                    long tempoRequisicao = reqFim - reqInicio;

                    menorTempo.updateAndGet(min -> Math.min(min, tempoRequisicao));
                    maiorTempo.updateAndGet(max -> Math.max(max, tempoRequisicao));
                    somaTempos.addAndGet(tempoRequisicao);

                    System.out.println("Status da resposta: " + response.getStatusCode() + " - Tempo: " + tempoRequisicao + "ms");
                }
            });
        }

        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.MINUTES);

        long tempoFim = System.currentTimeMillis();
        long tempoTotal = tempoFim - tempoInicio;
        int totalRequisicoes = numeroDeThreads * requisicoesPorThread;
        long tempoMedioReal = somaTempos.get() / totalRequisicoes;

        System.out.println("\n=== TESTE DE STRESS - GET EVENT ===");
        System.out.println("Total de requisições: " + totalRequisicoes);
        System.out.println("Tempo total do teste: " + tempoTotal + "ms");
        System.out.println("Tempo médio por requisição: " + tempoMedioReal + "ms");
        System.out.println("Menor tempo de resposta: " + menorTempo.get() + "ms");
        System.out.println("Maior tempo de resposta: " + maiorTempo.get() + "ms");
        System.out.println("Requisições por segundo (throughput): " + (totalRequisicoes * 1000.0 / tempoTotal));
    }

    @Test
    public void testeDeStressUpdateStatus() throws InterruptedException {
        int numeroDeRequisicoes = 500;
        ExecutorService executor = Executors.newFixedThreadPool(50);
        CountDownLatch latch = new CountDownLatch(numeroDeRequisicoes);
        AtomicLong menorTempo = new AtomicLong(Long.MAX_VALUE);
        AtomicLong maiorTempo = new AtomicLong(0);
        AtomicLong somaTempos = new AtomicLong(0);

        long tempoInicio = System.currentTimeMillis();

        for (int i = 0; i < numeroDeRequisicoes; i++) {
            final int eventId = 1;
            executor.submit(() -> {
                try {
                    long reqInicio = System.currentTimeMillis();
                    restTemplate.patchForObject("/status/" + eventId, null, String.class);
                    long reqFim = System.currentTimeMillis();
                    long tempoRequisicao = reqFim - reqInicio;

                    menorTempo.updateAndGet(min -> Math.min(min, tempoRequisicao));
                    maiorTempo.updateAndGet(max -> Math.max(max, tempoRequisicao));
                    somaTempos.addAndGet(tempoRequisicao);

                    System.out.println("Status atualizado - Tempo: " + tempoRequisicao + "ms");
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await(5, TimeUnit.MINUTES);
        executor.shutdown();

        long tempoFim = System.currentTimeMillis();
        long tempoTotal = tempoFim - tempoInicio;
        long tempoMedioReal = somaTempos.get() / numeroDeRequisicoes;

        System.out.println("\n=== TESTE DE STRESS - UPDATE STATUS ===");
        System.out.println("Total de requisições: " + numeroDeRequisicoes);
        System.out.println("Tempo total do teste: " + tempoTotal + "ms");
        System.out.println("Tempo médio por requisição: " + tempoMedioReal + "ms");
        System.out.println("Menor tempo de resposta: " + menorTempo.get() + "ms");
        System.out.println("Maior tempo de resposta: " + maiorTempo.get() + "ms");
        System.out.println("Requisições por segundo (throughput): " + (numeroDeRequisicoes * 1000.0 / tempoTotal));
    }
}
