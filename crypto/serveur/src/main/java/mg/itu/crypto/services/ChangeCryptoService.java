package mg.itu.crypto.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ChangeCryptoService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    // Cette méthode sera exécutée toutes les 10 secondes
    @Scheduled(fixedRate = 10000)
    public void updateCryptoValues() {
        // Mise à jour des valeurs en base de données
        jdbcTemplate.execute("SELECT update_crypto_values()");

        // Récupération des nouvelles valeurs depuis la base de données
        List<Map<String, Object>> cryptoData = jdbcTemplate.queryForList("SELECT * FROM changecrypto");

        // Envoi des nouvelles valeurs aux clients via WebSocket
        messagingTemplate.convertAndSend("/topic/crypto", cryptoData);
    }
}
