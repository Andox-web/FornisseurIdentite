package mg.itu.crypto.controllers;

import mg.itu.crypto.models.PourcentageCommission;
import mg.itu.crypto.repositories.CommissionRepository;
import mg.itu.crypto.repositories.PourcentageCommissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class CommissionController {

    @Autowired
    private PourcentageCommissionRepository pourcentageCommissionRepository;

    @Autowired
    private CommissionRepository commissionRepository;

    @PostMapping("/updateCommission")
    public ResponseEntity<?> updateCommission(@RequestParam BigDecimal achatCommission, @RequestParam BigDecimal venteCommission) {
        if (achatCommission.compareTo(BigDecimal.ZERO) < 0 || achatCommission.compareTo(new BigDecimal(100)) > 0 ||
            venteCommission.compareTo(BigDecimal.ZERO) < 0 || venteCommission.compareTo(new BigDecimal(100)) > 0) {
            return ResponseEntity.badRequest().body("Commission values must be between 0 and 100");
        }
        PourcentageCommission commission = pourcentageCommissionRepository.findFirstByOrderByIdAsc().orElse(new PourcentageCommission());
        commission.setAchatCommission(achatCommission.divide(new BigDecimal(100)));
        commission.setVenteCommission(venteCommission.divide(new BigDecimal(100)));
        commission.setDate(LocalDateTime.now());
        pourcentageCommissionRepository.save(commission);
        return ResponseEntity.ok("Commissions updated successfully");
    }

    @GetMapping("/getCurrentCommissions")
    public ResponseEntity<?> getCurrentCommissions() {
        Optional<PourcentageCommission> commissionOpt = pourcentageCommissionRepository.findFirstByOrderByIdAsc();
        if (commissionOpt.isEmpty()) {
            return ResponseEntity.status(404).body("No commission data found");
        }
        PourcentageCommission commission = commissionOpt.get();
        return ResponseEntity.ok(Map.of(
                "achatCommission", commission.getAchatCommission().multiply(new BigDecimal(100)),
                "venteCommission", commission.getVenteCommission().multiply(new BigDecimal(100)
        )));
    }

    @PostMapping("/analyseCommission")
    public ResponseEntity<?> analyseCommissions(
            @RequestParam String typeAnalyse,
            @RequestParam String selectedCrypto,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateMin,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateMax) {

        // Implement the logic to analyze commissions based on the provided parameters
        // This is a placeholder implementation
        BigDecimal result = BigDecimal.ZERO;

        if ("somme".equalsIgnoreCase(typeAnalyse)) {
            // Calculate the sum of commissions
            result = commissionRepository.calculateSum(selectedCrypto, dateMin, dateMax);
        } else if ("moyenne".equalsIgnoreCase(typeAnalyse)) {
            // Calculate the average of commissions
            result = commissionRepository.calculateAverage(selectedCrypto, dateMin, dateMax);
        }

        return ResponseEntity.ok(Map.of("result", result));
    }
}
