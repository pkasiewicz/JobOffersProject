package pl.pkasiewicz.infrastructure.offer.controller;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.pkasiewicz.domain.offer.OfferFacade;
import pl.pkasiewicz.domain.offer.dto.OfferRequestDto;
import pl.pkasiewicz.domain.offer.dto.OfferResponseDto;

import javax.validation.Valid;
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

    @GetMapping("/{id}")
    public ResponseEntity<OfferResponseDto> getOfferById(@PathVariable String id) {
        OfferResponseDto offer = offerFacade.findOfferById(id);
        return ResponseEntity.ok(offer);
    }

    @PostMapping
    public ResponseEntity<OfferResponseDto> saveOffer(@RequestBody @Valid OfferRequestDto offer) {
        OfferResponseDto savedOffer = offerFacade.saveOffer(offer);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedOffer);
    }
}
