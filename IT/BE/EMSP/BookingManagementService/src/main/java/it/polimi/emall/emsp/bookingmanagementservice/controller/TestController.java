package it.polimi.emall.emsp.bookingmanagementservice.controller;

import it.polimi.emall.emsp.bookingmanagementservice.generated.http.controller.TestApi;
import it.polimi.emall.emsp.bookingmanagementservice.generated.http.model.DeliveringStatusDto;
import it.polimi.emall.emsp.bookingmanagementservice.generated.http.model.TestComponentFeeDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import it.polimi.emall.emsp.bookingmanagementservice.generated.http.model.TestComponentDto;

@RestController
public class TestController implements TestApi {

    @Override
    public ResponseEntity<TestComponentDto> getTestComponent() {
        return new ResponseEntity<>(
                new TestComponentDto()
                        .welcomeMesage("Hi!")
                        .fee(new TestComponentFeeDto().value(2L).currency("EUR"))
                        .status(new DeliveringStatusDto(3)),
                HttpStatus.OK
        );
    }

    @Override
    public ResponseEntity<TestComponentDto> postTest(TestComponentDto testComponentDto) {
        return new ResponseEntity<>(testComponentDto, HttpStatus.OK);
    }
}
