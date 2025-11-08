package com.comparador.ComparadorTI;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Component
public class WebSocketMetrics {

    private final MeterRegistry meterRegistry;
    private final ConcurrentMap<String, Counter> counters = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, Timer> timers = new ConcurrentHashMap<>();

    public WebSocketMetrics(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    public void recordSend(String destination, Runnable sendOperation) {
        Timer.Sample sample = Timer.start(meterRegistry);
        String outcome = "success";
        try {
            sendOperation.run();
        } catch (RuntimeException ex) {
            outcome = "error";
            throw ex;
        } finally {
            counter(destination, outcome).increment();
            sample.stop(timer(destination, outcome));
        }
    }

    private Counter counter(String destination, String outcome) {
        String key = "c|" + destination + "|" + outcome;
        return counters.computeIfAbsent(key, k ->
                Counter.builder("websocket.send.count")
                        .description("Number of websocket sends")
                        .tag("destination", destination)
                        .tag("outcome", outcome) // success|error
                        .register(meterRegistry)
        );
    }

    private Timer timer(String destination, String outcome) {
        String key = "t|" + destination + "|" + outcome;
        return timers.computeIfAbsent(key, k ->
                Timer.builder("websocket.send.latency")
                        .description("Latency of websocket send")
                        .tag("destination", destination)
                        .tag("outcome", outcome) // success|error
                        .publishPercentiles(0.5, 0.95)
                        .publishPercentileHistogram()
                        .register(meterRegistry)
        );
    }
}
