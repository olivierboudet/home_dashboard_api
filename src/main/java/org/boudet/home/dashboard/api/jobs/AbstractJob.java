package org.boudet.home.dashboard.api.jobs;

import org.boudet.home.dashboard.api.JobKey;
import org.boudet.home.dashboard.api.EmitterManager;
import org.boudet.home.dashboard.api.enums.TypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public abstract class AbstractJob<Data> {
    @Autowired
    private EmitterManager emitterManager;

    public abstract TypeEnum getType();

    public abstract Data fetchData(JobKey key);

    protected Set<JobKey> getJobKeys() {
        return emitterManager.getJobKeys(getType());
    }

    public void execute(JobKey key) {
        List<SseEmitter> emitters = emitterManager.getEmitters(key);
        if(emitters != null && emitters.size() > 0) {
            Data data = fetchData(key);

            List<SseEmitter> emittersCloned = new ArrayList<>(emitters);
            emittersCloned.forEach(emitter -> {
                try {
                    emitter.send(data);
                } catch (IOException e) {
                    emitter.complete();
                    emitterManager.unregister(key, emitter);
                }
            });
        }
    }
}
