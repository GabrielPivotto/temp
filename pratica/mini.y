/* mini.y — análise sintática + semântica (tabela de símbolos e tipos) da linguagem Mini */
%{
import java.io.*;
import java.util.*;
%}

%token <sval> ID
%token <ival> NUM_INT
%token <dval> NUM_REAL
%token INT DOUBLE BOOL VOID IF ELSE WHILE BREAK RETURN PRINT TRUE FALSE
%token EQ NE LE GE AND OR
%token MOSTRA_TS TRACE_ON TRACE_OFF      /* diretivas de depuração */

%nonassoc IFX
%nonassoc ELSE
%left  OR
%left  AND
%nonassoc EQ NE
%nonassoc '<' '>' LE GE
%left  '+' '-'
%left  '*' '/' '%'
%right '!' UMINUS

%type <obj> tipo expr chamada lista_ids item_id args lista_args

%%

programa   : lista_decl                     { verificaMain(); }
           ;
lista_decl : lista_decl decl
           | decl
           ;
decl       : decl_var
           | decl_func
           | diretiva
           ;

/* ---------- diretivas: visualizar a TS em qualquer ponto do programa ---------- */
diretiva   : MOSTRA_TS                      { ts.imprime(linha()); }
           | TRACE_ON                       { ts.setTrace(true); }
           | TRACE_OFF                      { ts.setTrace(false); }
           ;

/* ---------- declarações de variáveis ---------- */
decl_var   : tipo lista_ids ';'             { declaraVars((Tipo)$1, (List<DeclId>)$2); }
           ;
lista_ids  : item_id                        { List<DeclId> l = new ArrayList<>(); l.add((DeclId)$1); $$ = l; }
           | lista_ids ',' item_id          { ((List<DeclId>)$1).add((DeclId)$3); $$ = $1; }
           ;
item_id    : ID                             { $$ = new DeclId($1, -1, linha()); }
           | ID '[' NUM_INT ']'             { $$ = new DeclId($1, $3, linha()); }
           ;
tipo       : INT                            { $$ = Tipo.INT; }
           | DOUBLE                         { $$ = Tipo.DOUBLE; }
           | BOOL                           { $$ = Tipo.BOOL; }
           ;


/* ---------- declarações de prototipos ---------- */
decl_prot  : cab '(' params ')' ';'

cab        : tipo ID                        { if(ts.busca($2) != null) {abreFuncao($2, (Tipo)$1)}
                                              else {declaraFunc($2, (Tipo)$1)}; }
           | VOID ID                        { if(ts.busca($2) != null) {abreFuncao($2, Tipo.VOID)}
                                              else {declaraFunc($2, Tipo.VOID)}; }
           ;

decl_func  : cab '(' params ')' corpo  { fechaFuncao(); }
           ;


params     : /* vazio */
           | lista_params
           ;
           
lista_params : param
           | lista_params ',' param
           ;
param      : tipo ID                        { declaraParam($2, (Tipo)$1); }
           ;
corpo      : '{' itens '}'                  /* mesmo escopo dos parâmetros */
           ;

/* ---------- blocos e comandos ---------- */
bloco      : '{'                            { ts.abreEscopo("bloco", linha()); }
             itens '}'                      { ts.fechaEscopo(linha()); }
           ;
itens      : /* vazio */
           | itens item
           ;
item       : decl_var
           | cmd
           | diretiva
           ;
cmd        : ID '=' expr ';'                { atribui($1, null, (Tipo)$3); }
           | ID '[' expr ']' '=' expr ';'   { atribui($1, (Tipo)$3, (Tipo)$6); }
           | chamada ';'
           | IF cond cmd %prec IFX
           | IF cond cmd ELSE cmd
           | WHILE cond                     { laco++; }
             cmd                            { laco--; }
           | BREAK ';'                      { if (laco == 0) erro("'break' fora de um laço"); }
           | RETURN ';'                     { checaReturn(null); }
           | RETURN expr ';'                { checaReturn((Tipo)$2); }
           | PRINT '(' expr ')' ';'         { checaPrint((Tipo)$3); }
           | bloco
           ;
cond       : '(' expr ')'                   { checaCond((Tipo)$2); }
           ;

/* ---------- expressões: o atributo sintetizado é o TIPO ---------- */
expr       : expr '+' expr                  { $$ = aritmetico("+", (Tipo)$1, (Tipo)$3); }
           | expr '-' expr                  { $$ = aritmetico("-", (Tipo)$1, (Tipo)$3); }
           | expr '*' expr                  { $$ = aritmetico("*", (Tipo)$1, (Tipo)$3); }
           | expr '/' expr                  { $$ = aritmetico("/", (Tipo)$1, (Tipo)$3); }
           | expr '%' expr                  { $$ = modulo((Tipo)$1, (Tipo)$3); }
           | expr '<' expr                  { $$ = relacional("<",  (Tipo)$1, (Tipo)$3); }
           | expr '>' expr                  { $$ = relacional(">",  (Tipo)$1, (Tipo)$3); }
           | expr LE expr                   { $$ = relacional("<=", (Tipo)$1, (Tipo)$3); }
           | expr GE expr                   { $$ = relacional(">=", (Tipo)$1, (Tipo)$3); }
           | expr EQ expr                   { $$ = igualdade("==", (Tipo)$1, (Tipo)$3); }
           | expr NE expr                   { $$ = igualdade("!=", (Tipo)$1, (Tipo)$3); }
           | expr AND expr                  { $$ = logico("&&", (Tipo)$1, (Tipo)$3); }
           | expr OR expr                   { $$ = logico("||", (Tipo)$1, (Tipo)$3); }
           | '!' expr                       { $$ = negacao((Tipo)$2); }
           | '-' expr %prec UMINUS          { $$ = menosUnario((Tipo)$2); }
           | '(' expr ')'                   { $$ = $2; }
           | NUM_INT                        { $$ = Tipo.INT; }
           | NUM_REAL                       { $$ = Tipo.DOUBLE; }
           | TRUE                           { $$ = Tipo.BOOL; }
           | FALSE                          { $$ = Tipo.BOOL; }
           | ID                             { $$ = usaVar($1); }
           | ID '[' expr ']'                { $$ = indexa($1, (Tipo)$3); }
           | chamada                        { $$ = $1; }
           ;
chamada    : ID '(' args ')'                { $$ = chama($1, (List<Tipo>)$3); }
           ;
args       : /* vazio */                    { $$ = new ArrayList<Tipo>(); }
           | lista_args                     { $$ = $1; }
           ;
lista_args : expr                           { List<Tipo> l = new ArrayList<>(); l.add((Tipo)$1); $$ = l; }
           | lista_args ',' expr            { ((List<Tipo>)$1).add((Tipo)$3); $$ = $1; }
           ;

%%
  /* ================= infraestrutura ================= */
  private Yylex lexer;
  private final TabSimb ts = new TabSimb();
  private Simbolo funcAtual = null;   // função sendo analisada (para checar return)
  private int laco = 0;               // profundidade de laços (para checar break)
  private int nErros = 0;

  private int yylex() {
    int tok = -1;
    try { yylval = new ParserVal(0); tok = lexer.yylex(); }
    catch (IOException e) { System.err.println("Erro de E/S: " + e); }
    return tok;
  }
  public void yyerror(String msg) { nErros++; System.out.printf("Linha %d: erro sintático (%s)%n", linha(), msg); }

  public Parser(Reader r, boolean verbose, boolean trace) {
    lexer = new Yylex(r, this);
    ts.setVerbose(verbose);
    ts.setTrace(trace);
    ts.abreEscopo("global", linha());
  }

  int  linha()            { return lexer.getLine(); }
  void erro(String msg)   { nErros++; System.out.printf("Linha %d: erro: %s%n", linha(), msg); }
  void aviso(String msg)  { System.out.printf("Linha %d: aviso: %s%n", linha(), msg); }

  /* ================= tabela de símbolos ================= */
  boolean declara(Simbolo s) {
    Simbolo ant = ts.buscaLocal(s.nome);
    if (ant != null) { erro("'" + s.nome + "' já declarado neste escopo (linha " + ant.linha + ")"); return false; }
    Simbolo ext = ts.busca(s.nome);
    if (ext != null && ts.nivel() > 0) aviso("'" + s.nome + "' oculta a declaração da linha " + ext.linha);
    ts.insere(s);
    return true;
  }

  void declaraVars(Tipo base, List<DeclId> ids) {
    Simbolo.Classe c = ts.nivel() == 0 ? Simbolo.Classe.VAR_GLOBAL : Simbolo.Classe.VAR_LOCAL;
    for (DeclId d : ids) {
      if (d.tam() == 0) erro("arranjo '" + d.nome() + "' deve ter tamanho positivo");
      Tipo t = d.tam() < 0 ? base : Tipo.array(base, d.tam());
      declara(new Simbolo(d.nome(), c, t, ts.nivel(), d.linha()));
    }
  }

 void declaraFunc(String nome, Tipo ret) {
    funcAtual = new Simbolo(nome, Simbolo.Classe.FUNCAO, ret, 0, linha());
    declara(funcAtual);                 // inserida ANTES do corpo: permite recursão
  }


  void abreFuncao(String nome, Tipo ret) {
    funcAtual = ts.busca(nome)
    ts.abreEscopo("função " + nome, linha());    // escopo dos parâmetros e variáveis locais

  }

  void declaraParam(String nome, Tipo t) {
    funcAtual.params.add(t);            // completa a assinatura (T1,...,Tn) -> U
    declara(new Simbolo(nome, Simbolo.Classe.PARAM, t, ts.nivel(), linha()));
  }

  void fechaFuncao() { ts.fechaEscopo(linha()); funcAtual = null; }

  /* ================= verificação de tipos ================= */
  Tipo usaVar(String nome) {
    Simbolo s = ts.resolve(nome, linha());
    if (s == null) { erro("identificador '" + nome + "' não declarado"); return Tipo.ERRO; }
    if (s.classe == Simbolo.Classe.FUNCAO) { erro("'" + nome + "' é uma função; use " + nome + "(...)"); return Tipo.ERRO; }
    return s.tipo;
  }

  Tipo indexa(String nome, Tipo tIdx) {
    Tipo t = usaVar(nome);
    if (t.ehErro()) return Tipo.ERRO;
    if (!t.ehArray()) { erro("'" + nome + "' não é um arranjo"); return Tipo.ERRO; }
    if (!tIdx.ehErro() && tIdx != Tipo.INT) erro("índice de '" + nome + "' deve ser int, encontrado " + tIdx);
    return t.elem;
  }

  void atribui(String nome, Tipo tIdx, Tipo tExpr) {
    Tipo alvo;
    if (tIdx == null) {
      alvo = usaVar(nome);
      if (alvo.ehArray()) { erro("não é permitido atribuir ao arranjo inteiro '" + nome + "'"); return; }
    } else alvo = indexa(nome, tIdx);
    if (!tExpr.atribuivelA(alvo))
      erro("tipos incompatíveis na atribuição a '" + nome + "': esperado " + alvo + ", encontrado " + tExpr);
  }

  Tipo chama(String nome, List<Tipo> args) {
    Simbolo f = ts.resolve(nome, linha());
    if (f == null) { erro("função '" + nome + "' não declarada"); return Tipo.ERRO; }
    if (f.classe != Simbolo.Classe.FUNCAO) { erro("'" + nome + "' não é uma função"); return Tipo.ERRO; }
    if (args.size() != f.params.size())
      erro("'" + nome + "' espera " + f.params.size() + " argumento(s), recebeu " + args.size());
    else
      for (int i = 0; i < args.size(); i++)
        if (!args.get(i).atribuivelA(f.params.get(i)))
          erro("argumento " + (i + 1) + " de '" + nome + "': esperado " + f.params.get(i) + ", encontrado " + args.get(i));
    return f.tipo;                      // o tipo de retorno é conhecido mesmo com erro nos argumentos
  }

  Tipo aritmetico(String op, Tipo a, Tipo b) {
    if (a.ehErro() || b.ehErro()) return Tipo.ERRO;            // não repete o erro (cascata)
    if (a.numerico() && b.numerico())
      return (a == Tipo.DOUBLE || b == Tipo.DOUBLE) ? Tipo.DOUBLE : Tipo.INT;   // promoção
    erro("operador '" + op + "' não se aplica a " + a + " e " + b);
    return Tipo.ERRO;
  }

  Tipo modulo(Tipo a, Tipo b) {
    if (a.ehErro() || b.ehErro()) return Tipo.ERRO;
    if (a == Tipo.INT && b == Tipo.INT) return Tipo.INT;
    erro("operador '%' exige int e int, encontrado " + a + " e " + b);
    return Tipo.ERRO;
  }

  Tipo relacional(String op, Tipo a, Tipo b) {
    if (!a.ehErro() && !b.ehErro() && !(a.numerico() && b.numerico()))
      erro("operador '" + op + "' não se aplica a " + a + " e " + b);
    return Tipo.BOOL;                   // o resultado é bool de qualquer forma
  }

  Tipo igualdade(String op, Tipo a, Tipo b) {
    if (a.ehErro() || b.ehErro()) return Tipo.BOOL;
    boolean ok = (a.numerico() && b.numerico())
              || (a.igual(b) && a != Tipo.VOID && !a.ehArray());
    if (!ok) erro("não é possível comparar " + a + " " + op + " " + b);
    return Tipo.BOOL;
  }

  Tipo logico(String op, Tipo a, Tipo b) {
    if (!a.ehErro() && a != Tipo.BOOL) erro("operando esquerdo de '" + op + "' deve ser bool, encontrado " + a);
    if (!b.ehErro() && b != Tipo.BOOL) erro("operando direito de '"  + op + "' deve ser bool, encontrado " + b);
    return Tipo.BOOL;
  }

  Tipo negacao(Tipo t) {
    if (!t.ehErro() && t != Tipo.BOOL) erro("operador '!' exige bool, encontrado " + t);
    return Tipo.BOOL;
  }

  Tipo menosUnario(Tipo t) {
    if (t.ehErro()) return Tipo.ERRO;
    if (t.numerico()) return t;
    erro("menos unário exige operando numérico, encontrado " + t);
    return Tipo.ERRO;
  }

  /* ================= verificações de comandos ================= */
  void checaCond(Tipo t) {
    if (!t.ehErro() && t != Tipo.BOOL) erro("condição deve ser bool, encontrado " + t);
  }

  void checaReturn(Tipo t) {
    Tipo esperado = funcAtual.tipo;
    if (esperado == Tipo.VOID) {
      if (t != null) erro("função void '" + funcAtual.nome + "' não pode retornar valor");
    } else if (t == null) {
      erro("'" + funcAtual.nome + "' deve retornar um valor do tipo " + esperado);
    } else if (!t.atribuivelA(esperado)) {
      erro("retorno incompatível em '" + funcAtual.nome + "': esperado " + esperado + ", encontrado " + t);
    }
  }

  void checaPrint(Tipo t) {
    if (t == Tipo.VOID || t.ehArray()) erro("print não aceita expressão do tipo " + t);
  }

  void verificaMain() {
    Simbolo m = ts.buscaLocal("main");
    if (m == null || m.classe != Simbolo.Classe.FUNCAO) erro("programa sem função 'main'");
    ts.fechaEscopo(linha());
  }

  /* ================= programa principal ================= */
  public static void main(String[] args) throws IOException {
    System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out), true, "UTF-8"));
    boolean verbose = false, trace = false; String arq = null;
    for (String a : args) {
      switch (a) {
        case "-v" -> verbose = true;      // imprime cada escopo ao fechar
        case "-t" -> trace = true;        // registra cada operação na TS
        case "-h" -> { System.out.println("Uso: java Parser [-v] [-t] [arquivo.mini]\n"
                     + "  -v  imprime cada escopo no momento em que é fechado\n"
                     + "  -t  trace: abre/insere/resolve/fecha escopo\n"
                     + "No fonte: $MOSTRA_TS mostra a pilha de escopos; $TRACE_ON / $TRACE_OFF");
                     return; }
        default   -> arq = a;
      }
    }
    Reader in = (arq == null) ? new InputStreamReader(System.in) : new FileReader(arq);
    Parser p = new Parser(in, verbose, trace);
    p.yyparse();
    System.out.println(p.nErros == 0 ? "Programa semanticamente correto."
                                     : p.nErros + " erro(s) encontrado(s).");
  }
