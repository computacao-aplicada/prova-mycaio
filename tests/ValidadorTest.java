import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ValidadorTest {

    @Test
    void deveValidarCPFValido() {
        assertTrue(Validador.validarCPF("529.982.247-25"));
        assertTrue(Validador.validarCPF("52998224725"));
    }

    @Test
    void deveValidarCPFComEspacosExternos() {
        assertTrue(Validador.validarCPF(" 529.982.247-25 "));
    }

    @Test
    void deveRejeitarCPFNulo() {
        assertFalse(Validador.validarCPF(null));
    }

    @Test
    void deveRejeitarCPFVazio() {
        assertFalse(Validador.validarCPF(""));
    }

    @Test
    void deveRejeitarCPFComCaractereInvalido() {
        assertFalse(Validador.validarCPF("529.982.247-2X"));
    }

    @Test
    void deveRejeitarCPFComSequenciaRepetida() {
        assertFalse(Validador.validarCPF("00000000000"));
        assertFalse(Validador.validarCPF("111.111.111-11"));
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
}
