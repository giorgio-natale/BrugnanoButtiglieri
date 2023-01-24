package it.polimi.cpms.bookingservice.controller;


import it.polimi.emall.cpms.bookingservice.generated.http.server.controller.TestApi;
import it.polimi.emall.cpms.bookingservice.generated.http.server.model.TestComponentDto;
import it.polimi.emall.cpms.bookingservice.generated.http.server.model.TestComponentFeeDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController implements TestApi {
    @Override
    public ResponseEntity<TestComponentDto> getTestComponent() {
        return new ResponseEntity<>(
                new TestComponentDto()
                        .welcomeMesage("Hi!")
                        .fee(new TestComponentFeeDto().value(2L).currency("EUR")),
                HttpStatus.OK
        );
    }

    @Override
    public ResponseEntity<TestComponentDto> postTest(TestComponentDto testComponentDto) {
        return new ResponseEntity<>(testComponentDto, HttpStatus.OK);
    }
}
