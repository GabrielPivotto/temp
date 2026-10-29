# Verificador semântico da linguagem Mini (JFlex + BYACC/J)

Material prático da Unidade 4.3–4.4 — Tabela de Símbolos e Verificação Semântica
(Construção de Compiladores, PUCRS).

## Arquivos
| Arquivo | Conteúdo |
|---|---|
| `mini.flex` | analisador léxico (JFlex, modo `%byaccj`) |
| `mini.y` | gramática + ações semânticas + `main` (BYACC/J) |
| `Tipo.java` | tipos da linguagem e relação ≤ (`atribuivelA`), tipo ERRO (⊥) |
| `Simbolo.java` | entrada da tabela de símbolos |
| `TabSimb.java` | tabela de símbolos: pilha de escopos (`LinkedHashMap` por escopo) |
| `DeclId.java` | record auxiliar para listas de declaração |
| `exemplos/ok.mini` | programa correto (gera 1 aviso de ocultação) |
| `exemplos/erros.mini` | programa com 13 erros semânticos |
| `exemplos/escopos.mini` | demonstra `$MOSTRA_TS` e `$TRACE_ON`/`$TRACE_OFF` |
| `Makefile` | geração, execução, teste e limpeza |

## Como gerar e executar
```sh
jflex -q mini.flex              # gera Yylex.java
byaccj -J -Jnorun mini.y        # gera Parser.java e ParserVal.java
javac -encoding UTF-8 *.java
java Parser exemplos/erros.mini
java Parser -v exemplos/ok.mini # -v imprime cada escopo ao ser fechado
```
(ou simplesmente `make`; outros alvos: `make ok`, `make erros`, `make test`,
`make conflitos`, `make clean`. Também há o script `./build.sh`.) Testado com JFlex 1.7, BYACC/J 1.15 e Java 21;
`byaccj -v` não reporta conflitos.

## Visualizando a tabela de símbolos e os escopos
| Recurso | Onde | Efeito |
|---|---|---|
| `-v` | linha de comando | imprime cada escopo no momento em que é fechado |
| `-t` | linha de comando | trace de todas as operações: abre/fecha escopo, insere, resolve (`x → x@9`) |
| `$MOSTRA_TS` | no fonte (como declaração ou comando) | imprime a pilha inteira de escopos abertos naquele ponto |
| `$TRACE_ON` / `$TRACE_OFF` | no fonte | liga/desliga o trace só em um trecho |

`java Parser -h` mostra a ajuda. As diretivas são tokens da gramática (não ações do léxico),
por isso executam exatamente no ponto em que aparecem — depois que as declarações anteriores
já foram inseridas na tabela.

## O que é verificado
- redeclaração no mesmo escopo; aviso de ocultação (shadowing) em escopo interno
- uso de identificador não declarado; função usada como variável e vice-versa
- tipos em expressões aritméticas, relacionais, lógicas e de igualdade (com coerção int → double)
- indexação: variável deve ser arranjo e índice deve ser int
- atribuição (T2 ≤ T1), número e tipo dos argumentos das chamadas
- condição de `if`/`while` do tipo bool; `break` só dentro de laço
- `return` compatível com o tipo da função; programa com `main`
- recuperação: o tipo ERRO (⊥) evita mensagens em cascata

## Exercícios (ver slides, grupo C)
C2 `do … while`, C3 atribuição de arranjos, C4 "toda função não void retorna",
C5 relatar cada não declarado uma única vez.
