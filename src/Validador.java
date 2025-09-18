public class Validador {

    public static boolean validarCPF(String cpf) {
        if (cpf == null) {
            return false;
        }

        String cpfLimpo = cpf.trim().replaceAll("[.-]", "");

        if (!cpfLimpo.matches("\\d{11}")) {
            return false;
        }

        if (temDigitosRepetidos(cpfLimpo)) {
            return false;
        }

        return checarDigitosVerificadores(cpfLimpo);
    }

    private static boolean temDigitosRepetidos(String cpf) {
        return cpf.chars().distinct().count() == 1;
    }

    private static boolean checarDigitosVerificadores(String cpf) {
        int[] digitos = cpf.chars().map(c -> c - '0').toArray();

        int soma1 = 0;
        for (int i = 0; i < 9; i++) {
            soma1 += digitos[i] * (10 - i);
        }
        int resto1 = soma1 % 11;
        int dv1 = (resto1 < 2) ? 0 : 11 - resto1;

        if (digitos[9] != dv1) {
            return false;
        }

        int soma2 = 0;
        for (int i = 0; i < 10; i++) {
            soma2 += digitos[i] * (11 - i);
        }
        int resto2 = soma2 % 11;
        int dv2 = (resto2 < 2) ? 0 : 11 - resto2;

        return digitos[10] == dv2;
    }
}
