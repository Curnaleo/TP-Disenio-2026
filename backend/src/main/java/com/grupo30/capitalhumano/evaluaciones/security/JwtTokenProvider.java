package com.grupo30.capitalhumano.evaluaciones.security;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Instant;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Map;
/**
 * Proveedor de tokens JWT firmado con HMAC-SHA256 para autenticar Consultores y Candidatos.
 */
@Component
public class JwtTokenProvider {

    private static final String HMAC_ALGORITHM = "HmacSHA256";
    private static final Base64.Encoder URL_ENCODER = Base64.getUrlEncoder().withoutPadding();
    private static final Base64.Decoder URL_DECODER = Base64.getUrlDecoder();

    private final ObjectMapper objectMapper;
    private final byte[] secretBytes;
    private final long expirationMs;

    public JwtTokenProvider(
        ObjectMapper objectMapper,
        @Value("${app.jwt.secret:CapitalHumanoGrupo30SecretKey2026SuperSegura!}") String secret,
        @Value("${app.jwt.expiration-ms:28800000}") long expirationMs) {
                this.objectMapper = objectMapper;
                this.secretBytes = secret.getBytes(StandardCharsets.UTF_8);
                this.expirationMs = expirationMs;
            }

    public String generarTokenConsultor(Integer idUsuario, String username) {
        Map<String, Object> claims = new LinkedHashMap<>();
        claims.put("sub", username);
        claims.put("role", "CONSULTOR");
        claims.put("idUsuario", idUsuario);
        return crearToken(claims);
    }

    public String generarTokenCandidato(Integer nroCandidato, Integer idCuestionario, String identificadorDoc) {
        Map<String, Object> claims = new LinkedHashMap<>();
        claims.put("sub", identificadorDoc);
        claims.put("role", "CANDIDATO");
        claims.put("nroCandidato", nroCandidato);
        claims.put("idCuestionario", idCuestionario);
        return crearToken(claims);
    }

    public boolean validarToken(String token) {
        try {
            Map<String, Object> claims = extraerClaims(token);
            Number exp = (Number) claims.get("exp");
            return exp != null && Instant.now().getEpochSecond() < exp.longValue();
        } catch (Exception ex) {
            return false;
        }
    }
    public Map<String, Object> extraerClaims(String token) {
        if (token == null || token.isBlank()) {
            throw new IllegalArgumentException("Token vacío.");
        }
        String[] partes = token.split("\\.");
        if (partes.length != 3) {
            throw new IllegalArgumentException("Formato JWT inválido.");
        }

        String signingInput = partes[0] + "." + partes[1];
        byte[] firmaEsperada = firmar(signingInput);
        byte[] firmaRecibida = URL_DECODER.decode(partes[2]);
        if (!MessageDigest.isEqual(firmaEsperada, firmaRecibida)) {
            throw new IllegalArgumentException("Firma JWT inválida.");
        }
        byte[] payloadBytes = URL_DECODER.decode(partes[1]);
        String payloadJson = new String(payloadBytes, StandardCharsets.UTF_8);

        return objectMapper.readValue(payloadJson, new TypeReference<>() {});
    }

    private String crearToken(Map<String, Object> claims) {
        long nowSec = Instant.now().getEpochSecond();
        long expSec = nowSec + (expirationMs / 1000);
        Map<String, Object> header = Map.of("alg", "HS256", "typ", "JWT");
        Map<String, Object> payload = new LinkedHashMap<>(claims);
        payload.put("iat", nowSec);
        payload.put("exp", expSec);

        String encodedHeader = URL_ENCODER.encodeToString(
        objectMapper.writeValueAsString(header).getBytes(StandardCharsets.UTF_8));
        String encodedPayload = URL_ENCODER.encodeToString(
        objectMapper.writeValueAsString(payload).getBytes(StandardCharsets.UTF_8));
        String signingInput = encodedHeader + "." + encodedPayload;
        String encodedSignature = URL_ENCODER.encodeToString(firmar(signingInput));
        
        return signingInput + "." + encodedSignature;
    }

    private byte[] firmar(String data) {
        try {
            Mac mac = Mac.getInstance(HMAC_ALGORITHM);
            mac.init(new SecretKeySpec(secretBytes, HMAC_ALGORITHM));
            return mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
        } catch (Exception ex) {
            throw new IllegalStateException("Error al firmar el token JWT.", ex);
        }
    }
}

