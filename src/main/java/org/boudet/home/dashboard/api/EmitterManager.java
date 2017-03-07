package org.boudet.home.dashboard.api;

import org.boudet.home.dashboard.api.enums.TypeEnum;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class EmitterManager {
    private final Map<JobKey, List<SseEmitter>> emitters = new ConcurrentHashMap<>();

    public void register(JobKey key, SseEmitter emitter) {
        if(!emitters.containsKey(key)) {
            emitters.put(key, new ArrayList<>());
        }

        List listEmitters = emitters.get(key);
        listEmitters.add(emitter);
    }

    public void unregister(JobKey key, SseEmitter emitter) {
        List listEmitters = emitters.get(key);
        listEmitters.remove(emitter);
    }

    public List<SseEmitter> getEmitters(JobKey key) {
        return emitters.get(key);
    }

    public Set<JobKey> getJobKeys(TypeEnum type) {
        return emitters.keySet().stream().filter(key -> key.getType().equals(type)).collect(Collectors.toSet());
    }
}
