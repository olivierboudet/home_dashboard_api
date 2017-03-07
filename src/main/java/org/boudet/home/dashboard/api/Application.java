package org.boudet.home.dashboard.api;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.boudet.home.dashboard.api.enums.TypeEnum;
import org.boudet.home.dashboard.api.jobs.AbstractJob;
import org.boudet.home.dashboard.api.jobs.JobFactory;
import org.boudet.home.dashboard.api.model.SimpleStat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@SpringBootApplication
@Controller
@EnableScheduling
@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class, DataSourceTransactionManagerAutoConfiguration.class})
public class Application {

    @Autowired
    private EmitterManager emitterManager;

    @Autowired
    private JobFactory jobFactory;

    @Autowired
    private TaskScheduler scheduler;

    public static void main(String[] args) throws Exception {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public TaskScheduler taskScheduler() {
        return new ConcurrentTaskScheduler();
    }

    @InitBinder
    void initBinder(final WebDataBinder binder){
        binder.registerCustomEditor(TypeEnum.class, new TypeBinder());
    }

    @CrossOrigin(origins = "*")
    @RequestMapping(path = "/data/{type}", method = RequestMethod.GET)
    public SseEmitter getData(@PathVariable("type") TypeEnum type, @RequestParam(name="room", required = false) String room) throws IOException {

        SseEmitter emitter = new SseEmitter();

        JobKey key = new JobKey(type, room);

        emitterManager.register(key, emitter);

        AbstractJob job = jobFactory.getJob(type);
        if(job != null) {
            emitter.send(job.fetchData(key));
        }

        return emitter;
    }
}
