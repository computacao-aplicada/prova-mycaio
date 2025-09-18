import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ValidadorTest {

    @Test
    void deveValidarCPFValido() {
        assertTrue(Validador.validarCPF("529.982.247-25"));
        assertTrue(Validador.validarCPF("52998224725"));
    }

    @Test
    void deveRejeitarEntradasInvalidas() {
        assertFalse(Validador.validarCPF(null));
        assertFalse(Validador.validarCPF(""));
        assertFalse(Validador.validarCPF("529.982.247-2X"));
        assertFalse(Validador.validarCPF("00000000000"));
    }
}
