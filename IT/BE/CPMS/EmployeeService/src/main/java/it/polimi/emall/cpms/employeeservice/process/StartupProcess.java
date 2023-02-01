package it.polimi.emall.cpms.employeeservice.process;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.emall.cpms.employeeservice.generated.http.server.model.SignupRequestDto;
import it.polimi.emall.cpms.employeeservice.usecase.SignupUseCase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class StartupProcess {

    private final Resource employeesResource;
    private final ObjectMapper jsonObjectMapper;
    private final SignupUseCase signupUseCase;

    public StartupProcess(@Value("classpath:employees.json") Resource employeesResource, ObjectMapper jsonObjectMapper, SignupUseCase signupUseCase) {
        this.employeesResource = employeesResource;
        this.jsonObjectMapper = jsonObjectMapper;
        this.signupUseCase = signupUseCase;
    }

    @EventListener(ApplicationReadyEvent.class)
    @Async
    public void startup(){
        try {
            List<SignupRequestDto> employees =
                    jsonObjectMapper.readValue(employeesResource.getInputStream(), new TypeReference<>() {});

            for(int i = 0; i < employees.size(); i++){
                signupUseCase.signup((long) i, employees.get(i));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
