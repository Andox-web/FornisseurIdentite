package mg.itu.crypto.controllers;

import mg.itu.crypto.models.ChangeCrypto;
import mg.itu.crypto.repositories.ChangeCryptoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class AnalyseController {

    @Autowired
    private ChangeCryptoRepository changeCryptoRepository;

    @PostMapping("/analyseCours")
    public ResponseEntity<?> analyseCours(
            @RequestParam String typeAnalyse,
            @RequestParam List<String> selectedCryptos,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateMin,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateMax) {

        List<ChangeCrypto> changes = changeCryptoRepository.findAllByDateBetween(dateMin, dateMax);

        if (!selectedCryptos.contains("tous")) {
            changes = changes.stream()
                    .filter(change -> selectedCryptos.contains(change.getCryptomonnaie().getId().toString()))
                    .collect(Collectors.toList());
        }

        BigDecimal result = BigDecimal.ZERO;

        switch (typeAnalyse.toLowerCase()) {
            case "quartile":
                // Calculate the 1st quartile
                result = calculateQuartile(changes, 0.25);
                break;
            case "max":
                // Calculate the maximum value
                result = changes.stream()
                        .map(ChangeCrypto::getValeur)
                        .max(BigDecimal::compareTo)
                        .orElse(BigDecimal.ZERO);
                break;
            case "min":
                // Calculate the minimum value
                result = changes.stream()
                        .map(ChangeCrypto::getValeur)
                        .min(BigDecimal::compareTo)
                        .orElse(BigDecimal.ZERO);
                break;
            case "moyenne":
                // Calculate the average value
                result = changes.stream()
                        .map(ChangeCrypto::getValeur)
                        .reduce(BigDecimal.ZERO, BigDecimal::add)
                        .divide(new BigDecimal(changes.size()), BigDecimal.ROUND_HALF_UP);
                break;
            case "ecart-type":
                // Calculate the standard deviation
                BigDecimal mean = changes.stream()
                        .map(ChangeCrypto::getValeur)
                        .reduce(BigDecimal.ZERO, BigDecimal::add)
                        .divide(new BigDecimal(changes.size()), BigDecimal.ROUND_HALF_UP);
                BigDecimal variance = changes.stream()
                        .map(change -> change.getValeur().subtract(mean).pow(2))
                        .reduce(BigDecimal.ZERO, BigDecimal::add)
                        .divide(new BigDecimal(changes.size()), BigDecimal.ROUND_HALF_UP);
                result = new BigDecimal(Math.sqrt(variance.doubleValue()));
                break;
            default:
                return ResponseEntity.badRequest().body("Invalid analysis type");
        }

        return ResponseEntity.ok(Map.of("result", result));
    }

    private BigDecimal calculateQuartile(List<ChangeCrypto> changes, double quartile) {
        int index = (int) Math.ceil(quartile * changes.size());
        return changes.stream()
                .map(ChangeCrypto::getValeur)
                .sorted()
                .collect(Collectors.toList())
                .get(index - 1);
    }
}
