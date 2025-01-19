package pl.pkasiewicz.infrastructure.offer.controller;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.pkasiewicz.domain.offer.OfferFacade;
import pl.pkasiewicz.domain.offer.dto.OfferResponseDto;

import java.util.List;

@RestController
@RequestMapping("/offers")
@AllArgsConstructor
@Log4j2
public class JobOfferRestController {

    private final OfferFacade offerFacade;

    @GetMapping
    public ResponseEntity<List<OfferResponseDto>> getAllOffers() {
        List<OfferResponseDto> allOffers = offerFacade.findAllOffers();
        return ResponseEntity.ok(allOffers);
    }
}
