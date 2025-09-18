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

    @Test
    void deveRejeitarTamanhosIncorretos() {
        assertFalse(Validador.validarCPF("935.411.347-8"));   // 10 dígitos
        assertFalse(Validador.validarCPF("935.411.347-800")); // 12 dígitos
    }

    @Test
    void deveRejeitarDVIncorreto() {
        assertFalse(Validador.validarCPF("529.982.247-24"));
        assertFalse(Validador.validarCPF("123.456.789-00"));
    }

    @Test
    void deveValidarCPFComEspacosExternos() {
        assertTrue(Validador.validarCPF(" 529.982.247-25 "));
    }
}
