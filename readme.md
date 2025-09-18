[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-22041afd0340ce965d47ae6ef1cefeee28c7c493a6346c4f15d667ab976d596c.svg)](https://classroom.github.com/a/6t9Jux3G)
# Plano de Testes (Simplificado + Tutorial) — ValidadorCPF

> **Contexto:** atividade de aula para validar um algoritmo clássico: verificação de **CPF**.
> **Stack dos exemplos:** Java 17 + JUnit 5.
> **Assinatura alvo:**
> - `boolean validarCPF(String cpf)` → retorna `true` se válido, `false` caso contrário.

---

## 1) O que exatamente vamos testar?

- **Entradas válidas:** aceitar CPF **com máscara** (`"529.982.247-25"`) e **sem máscara** (`"52998224725"`).
- **Entradas com espaços externos:** `" 529.982.247-25 "` → `true`.
- **Formato:** apenas **11 dígitos**. Qualquer outro tamanho → `false`.
- **Caracteres inválidos:** só aceita `.` e `-` na máscara. Outros → `false`.
- **Nulos/vazios:** `null` ou `""` → `false`.
- **Sequências repetidas:** `"00000000000"`, `"11111111111"` etc. → `false`.
- **Algoritmo de verificação:** calcular os dois **dígitos verificadores (DV1 e DV2)** e comparar com os fornecidos.

---

## 2) Como validar um CPF (algoritmo simplificado)

1. **Sanitizar entrada:** remover espaços, `.` e `-`.
2. **Checar tamanho:** deve restar **11 dígitos**.
3. **Rejeitar sequências:** todos dígitos iguais.
4. **Calcular DV1:**
    - Usar 9 primeiros dígitos e pesos `10..2`.
    - `S1 = soma(d1×10 + d2×9 … + d9×2)`.
    - `r1 = S1 % 11`.
    - `DV1 = (r1 < 2) ? 0 : 11 - r1`.
5. **Calcular DV2:**
    - Usar 9 dígitos + DV1 e pesos `11..2`.
    - `S2 = soma(d1×11 + d2×10 … + d9×3 + DV1×2)`.
    - `r2 = S2 % 11`.
    - `DV2 = (r2 < 2) ? 0 : 11 - r2`.
6. **Comparar:** os dois DV calculados devem bater com os dígitos 10 e 11 do CPF.

**Exemplo prático:** `529.982.247-25` → **válido** (DV1=2, DV2=5).

---

## 3) Critérios de Aceitação

1. Entradas corretas retornam `true`.
2. Formatos inválidos/nulos/vazios retornam `false`.
3. Sequências repetidas retornam `false`.
4. DV incorreto retorna `false`.
5. Testes devem cobrir **partições** (válidos/ inválidos) e **limites** (11 dígitos, dígitos finais).

---

## 4) Cenários de Teste (tabela‑guia)

| Categoria                 | Entrada                    | Esperado |
|---------------------------|----------------------------|----------|
| Válido (com máscara)      | `529.982.247-25`           | `true`   |
| Válido (sem máscara)      | `52998224725`              | `true`   |
| Espaços externos          | ` 529.982.247-25 `         | `true`   |
| Nulo/Vazio                | `null`, `""`               | `false`  |
| Tamanho menor             | `935.411.347-8` (10 díg.)  | `false`  |
| Tamanho maior             | `935.411.347-800` (12 díg.)| `false`  |
| Caractere inválido        | `529.982.247-2X`           | `false`  |
| Sequência repetida        | `000.000.000-00`           | `false`  |
| DV incorreto (mesmo CPF)  | `529.982.247-24`           | `false`  |
| DV incorreto (genérico)   | `123.456.789-00`           | `false`  |

---

## 5) Tutorial Passo‑a‑Passo (TDD light)

### Passo 1 — Primeiro teste (feliz com e sem máscara)
```java
@Test
void deveValidarCPFValido() {
  assertTrue(Validador.validarCPF("529.982.247-25"));
  assertTrue(Validador.validarCPF("52998224725"));
}
```

### Passo 2 — Implementação mínima para passar
```java
public static boolean validarCPF(String cpf) {
  if (cpf == null || cpf.trim().isEmpty()) return false;
  String limpo = cpf.trim().replaceAll("[.-]", "");
  if (!limpo.matches("\\d{11}")) return false;
  if (limpo.chars().distinct().count() == 1) return false;
  // TODO: calcular DV
  return false;
}
```

### Passo 3 — Testes de entradas inválidas
```java
@Test
void deveRejeitarEntradasInvalidas() {
  assertFalse(Validador.validarCPF(null));
  assertFalse(Validador.validarCPF(""));
  assertFalse(Validador.validarCPF("529.982.247-2X"));
  assertFalse(Validador.validarCPF("00000000000"));
}
```

### Passo 4 — Testes de tamanho incorreto
```java
@Test
void deveRejeitarTamanhosIncorretos() {
  assertFalse(Validador.validarCPF("935.411.347-8"));   // 10 dígitos
  assertFalse(Validador.validarCPF("935.411.347-800")); // 12 dígitos
}
```

### Passo 5 — Implementar algoritmo dos dígitos verificadores
```java
private static boolean checarDigitos(String cpf) {
  int[] d = cpf.chars().map(c -> c - '0').toArray();
  // DV1
  int s1 = 0;
  for (int i = 0; i < 9; i++) s1 += d[i] * (10 - i);
  int r1 = s1 % 11;
  int dv1 = (r1 < 2) ? 0 : 11 - r1;
  if (d[9] != dv1) return false;
  // DV2
  int s2 = 0;
  for (int i = 0; i < 10; i++) s2 += d[i] * (11 - i);
  int r2 = s2 % 11;
  int dv2 = (r2 < 2) ? 0 : 11 - r2;
  return d[10] == dv2;
}
```

### Passo 6 — Testes de DV incorreto
```java
@Test
void deveRejeitarDVIncorreto() {
  assertFalse(Validador.validarCPF("529.982.247-24"));
  assertFalse(Validador.validarCPF("123.456.789-00"));
}
```

### Passo 7 — Refatorar e documentar
- Extrair sanitização, checagem de formato e cálculo dos DVs em métodos privados.
- Adicionar comentários explicando cada regra.
- Deixar o código mais legível.

---

## 6) Checklist de Entrega

- [ ] Testes para CPFs válidos (com e sem máscara).
- [ ] Testes de formatos inválidos (nulos, vazios, caracteres extras, tamanhos).
- [ ] Testes de sequências repetidas.
- [ ] Testes de DVs incorretos.
- [ ] Implementação do cálculo de DV1 e DV2.
- [ ] Refatoração + documentação.

---

## 7) Rubrica de Avaliação (10 pts)

- Validação correta dos DVs → **4,0 pts**
- Tratamento de formatos e sequências inválidas → **3,0 pts**
- Clareza e cobertura dos testes → **2,0 pts**
- Refatoração e documentação → **1,0 pt**

---

## Boas práticas e falhas comuns

- **Commit pequeno:** um teste por vez, depois código mínimo para passar.
- **Mensagens claras:** testes que falham devem dizer exatamente o que quebrou.
- **Refatoração:** evite duplicação de código (ex.: sanitização em método único).
- **Cobertura:** não esqueça de cenários de borda (sequência igual, DV errado).

---

# Apêndice A — Exemplo detalhado de cálculo

# Validador de CPF — Explicação aprimorada + Exemplo numérico (anexo do plano)

Este anexo complementa o **Plano-CPF-Tutorial-Simplificado.md** com uma explicação mais clara do algoritmo e um **exemplo passo a passo**.

---

## Como funciona a validação do CPF (visão prática)

1. **Sanitização:** remova espaços e máscara `.`/`-`, ficando só com dígitos (`\\d`).
    - Ex.: `" 529.982.247-25 "` → `"52998224725"`
2. **Formato básico:** o resultado deve ter **exatamente 11 dígitos**. Caso contrário, **inválido**.
3. **Filtro de sequência:** CPFs formados por **todos os dígitos iguais** (ex.: `00000000000`, `11111111111`) são **inválidos**.
4. **Cálculo dos dígitos verificadores (DV1 e DV2):**
    - Use **pesos decrescentes** e **módulo 11**.
    - **DV1** usa os **9 primeiros dígitos** e pesos **10..2**.
        - Soma ponderada: `S1 = d1×10 + d2×9 + ... + d9×2`
        - Resto: `r1 = S1 % 11`
        - Regra: `DV1 = (r1 < 2) ? 0 : (11 - r1)`
    - **DV2** usa os **9 primeiros dígitos + DV1** e pesos **11..2**.
        - `S2 = d1×11 + d2×10 + ... + d9×3 + DV1×2`
        - `r2 = S2 % 11`
        - `DV2 = (r2 < 2) ? 0 : (11 - r2)`
5. **Comparação final:** compare os dois DVs calculados com os **dígitos 10 e 11** do CPF. Só é **válido** se ambos baterem.

> **Observações úteis**
> - O **caso especial** `r < 2 → DV = 0` evita que restos pequenos virem dígitos altos.
> - Máscara é **opcional**; caracteres além de `.`/`-` tornam o CPF inválido.
> - O algoritmo **não depende** de BigDecimal; tudo é feito com inteiros e módulo 11.

---

## Exemplo completo: `529.982.247-25` (válido)

**Entrada (com máscara):** `529.982.247-25`
**Sanitizado:** `52998224725`
**Dígitos:** `5 2 9 9 8 2 2 4 7 2 5`
**Primeiros 9 dígitos:** `5 2 9 9 8 2 2 4 7`

### 1) Cálculo do **DV1**
Pesos: `10 9 8 7 6 5 4 3 2`

| dígito | 5 | 2 | 9 | 9 | 8 | 2 | 2 | 4 | 7 |
|:-----:|---|---|---|---|---|---|---|---|---|
| peso  |10 | 9 | 8 | 7 | 6 | 5 | 4 | 3 | 2 |
| prod. |50 |18 |72 |63 |48 |10 | 8 |12 |14 |

`S1 = 50+18+72+63+48+10+8+12+14 = 295`
`r1 = 295 % 11 = 9`
`DV1 = 11 - 9 = 2` (como `r1 ≥ 2`)

### 2) Cálculo do **DV2**
Agora use `5 2 9 9 8 2 2 4 7` **+ DV1=2** → `5 2 9 9 8 2 2 4 7 2`
Pesos: `11 10 9 8 7 6 5 4 3 2`

| dígito | 5 | 2 | 9 | 9 | 8 | 2 | 2 | 4 | 7 | 2 |
|:-----:|---|---|---|---|---|---|---|---|---|---|
| peso  |11 |10 | 9 | 8 | 7 | 6 | 5 | 4 | 3 | 2 |
| prod. |55 |20 |81 |72 |56 |12 |10 |16 |21 | 4 |

`S2 = 55+20+81+72+56+12+10+16+21+4 = 347`
`r2 = 347 % 11 = 6`
`DV2 = 11 - 6 = 5` (como `r2 ≥ 2`)

**DV informados pelo CPF:** `2` e `5` → **batem** com os calculados. Logo, **válido** ✅

---

## Mini‑pseudocódigo (referência)

```text
validarCPF(cpf):
  if cpf == null: return false
  limpo = removerEspacosPontosTracos(cpf)
  if !somenteDigitos(limpo) or len(limpo) != 11: return false
  if todosIguais(limpo): return false
  dv1 = calcularDV(limpo[0..8], pesos=10..2)
  dv2 = calcularDV(limpo[0..8] + dv1, pesos=11..2)
  return dv1 == limpo[9] and dv2 == limpo[10]
```

## Trecho Java (cálculo dos DVs)

```java
private static int calcularDV(int[] digitos, int pesoInicial) {
  int soma = 0;
  int peso = pesoInicial;
  for (int d : digitos) soma += d * (peso--);
  int r = soma % 11;
  return (r < 2) ? 0 : (11 - r);
}
```
---

Bom trabalho e bons testes! 🎯
