public class Validador {

    public static boolean validarCPF(String cpf) {
        if(cpf == null || cpf.trim().isEmpty()){
            return false;
        }
        String limpo = cpf.trim().replaceAll("[.-]", "");
        if(!limpo.matches("\\d{11}")){
            return false;
        }
        if(limpo.chars().distinct().count() == 1){
            return false;
        }
        int[] d = limpo.chars().map(c -> c - '0').toArray();
        int s1 = 0;
        for (int i = 0; i < 9; i++) s1 += d[i] * (10 - i);
        int r1 = s1 % 11;
        int dv1 = (r1 < 2) ? 0 : 11 - r1;
        if(d[9] != dv1){
            return false;
        }
        int s2 = 0;
        for (int i = 0; i < 10; i++) s2 += d[i] * (11 - i);
        int r2 = s2 % 11;
        int dv2 = (r2 < 2) ? 0 : 11 - r2;
        if(d[10] == dv2) {
            return true;
        }



        return false;
    }

}

