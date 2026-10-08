//### This file created by BYACC 1.8(/Java extension  1.15)
//### Java capabilities added 7 Jan 97, Bob Jamison
//### Updated : 27 Nov 97  -- Bob Jamison, Joe Nieten
//###           01 Jan 98  -- Bob Jamison -- fixed generic semantic constructor
//###           01 Jun 99  -- Bob Jamison -- added Runnable support
//###           06 Aug 00  -- Bob Jamison -- made state variables class-global
//###           03 Jan 01  -- Bob Jamison -- improved flags, tracing
//###           16 May 01  -- Bob Jamison -- added custom stack sizing
//###           04 Mar 02  -- Yuval Oren  -- improved java performance, added options
//###           14 Mar 02  -- Tomas Hurka -- -d support, static initializer workaround
//### Please send bug reports to tom@hukatronic.cz
//### static char yysccsid[] = "@(#)yaccpar	1.8 (Berkeley) 01/20/90";






//#line 8 "mini.y"
import java.io.*;
import java.util.*;
//#line 20 "Parser.java"




public class Parser
{

boolean yydebug;        //do I want debug output?
int yynerrs;            //number of errors so far
int yyerrflag;          //was there an error?
int yychar;             //the current working character

//########## MESSAGES ##########
//###############################################################
// method: debug
//###############################################################
void debug(String msg)
{
  if (yydebug)
    System.out.println(msg);
}

//########## STATE STACK ##########
final static int YYSTACKSIZE = 500;  //maximum stack size
int statestk[] = new int[YYSTACKSIZE]; //state stack
int stateptr;
int stateptrmax;                     //highest index of stackptr
int statemax;                        //state when highest index reached
//###############################################################
// methods: state stack push,pop,drop,peek
//###############################################################
final void state_push(int state)
{
  try {
		stateptr++;
		statestk[stateptr]=state;
	 }
	 catch (ArrayIndexOutOfBoundsException e) {
     int oldsize = statestk.length;
     int newsize = oldsize * 2;
     int[] newstack = new int[newsize];
     System.arraycopy(statestk,0,newstack,0,oldsize);
     statestk = newstack;
     statestk[stateptr]=state;
  }
}
final int state_pop()
{
  return statestk[stateptr--];
}
final void state_drop(int cnt)
{
  stateptr -= cnt; 
}
final int state_peek(int relative)
{
  return statestk[stateptr-relative];
}
//###############################################################
// method: init_stacks : allocate and prepare stacks
//###############################################################
final boolean init_stacks()
{
  stateptr = -1;
  val_init();
  return true;
}
//###############################################################
// method: dump_stacks : show n levels of the stacks
//###############################################################
void dump_stacks(int count)
{
int i;
  System.out.println("=index==state====value=     s:"+stateptr+"  v:"+valptr);
  for (i=0;i<count;i++)
    System.out.println(" "+i+"    "+statestk[i]+"      "+valstk[i]);
  System.out.println("======================");
}


//########## SEMANTIC VALUES ##########
//public class ParserVal is defined in ParserVal.java


String   yytext;//user variable to return contextual strings
ParserVal yyval; //used to return semantic vals from action routines
ParserVal yylval;//the 'lval' (result) I got from yylex()
ParserVal valstk[];
int valptr;
//###############################################################
// methods: value stack push,pop,drop,peek.
//###############################################################
void val_init()
{
  valstk=new ParserVal[YYSTACKSIZE];
  yyval=new ParserVal();
  yylval=new ParserVal();
  valptr=-1;
}
void val_push(ParserVal val)
{
  if (valptr>=YYSTACKSIZE)
    return;
  valstk[++valptr]=val;
}
ParserVal val_pop()
{
  if (valptr<0)
    return new ParserVal();
  return valstk[valptr--];
}
void val_drop(int cnt)
{
int ptr;
  ptr=valptr-cnt;
  if (ptr<0)
    return;
  valptr = ptr;
}
ParserVal val_peek(int relative)
{
int ptr;
  ptr=valptr-relative;
  if (ptr<0)
    return new ParserVal();
  return valstk[ptr];
}
final ParserVal dup_yyval(ParserVal val)
{
  ParserVal dup = new ParserVal();
  dup.ival = val.ival;
  dup.dval = val.dval;
  dup.sval = val.sval;
  dup.obj = val.obj;
  return dup;
}
//#### end semantic value section ####
public final static short ID=257;
public final static short NUM_INT=258;
public final static short NUM_REAL=259;
public final static short INT=260;
public final static short DOUBLE=261;
public final static short BOOL=262;
public final static short VOID=263;
public final static short IF=264;
public final static short ELSE=265;
public final static short WHILE=266;
public final static short BREAK=267;
public final static short RETURN=268;
public final static short PRINT=269;
public final static short TRUE=270;
public final static short FALSE=271;
public final static short EQ=272;
public final static short NE=273;
public final static short LE=274;
public final static short GE=275;
public final static short AND=276;
public final static short OR=277;
public final static short MOSTRA_TS=278;
public final static short TRACE_ON=279;
public final static short TRACE_OFF=280;
public final static short IFX=281;
public final static short UMINUS=282;
public final static short YYERRCODE=256;
final static short yylhs[] = {                           -1,
    0,    7,    7,    7,    9,    9,   11,   11,   11,   10,
    3,    3,    4,    4,    5,    5,    1,    1,    1,    8,
   12,   13,   16,   15,   14,   14,   14,   17,   17,   17,
   17,   19,   17,   17,   17,   17,   17,   17,   18,    6,
    6,    2,    2,    2,    2,    2,    2,    2,    2,    2,
    2,    2,    2,    2,    2,    2,    2,    2,    2,    2,
    2,    2,    2,
};
final static short yylen[] = {                            2,
    3,    0,    2,    2,    0,    2,    1,    1,    1,    3,
    1,    3,    1,    2,    3,    4,    1,    1,    1,    4,
    2,    3,    0,    4,    0,    2,    2,    4,    5,    3,
    5,    0,    4,    2,    2,    3,    5,    1,    3,    3,
    4,    3,    3,    3,    3,    3,    3,    3,    3,    3,
    3,    3,    3,    3,    2,    2,    3,    1,    1,    1,
    1,    1,    2,
};
final static short yydefred[] = {                         2,
    0,    0,   17,   18,   19,    0,    7,    8,    9,    0,
    5,    3,    4,    0,   21,    0,    0,   11,    0,    0,
    0,    0,   10,    0,    6,    0,    0,    0,   12,   25,
   20,   15,    0,    0,   16,    0,    0,    0,    0,    0,
    0,   23,   22,   27,   38,   26,    0,    0,    0,    0,
    0,   32,   34,    0,   58,   59,   60,   61,    0,    0,
   35,    0,    0,    0,   25,    0,    0,    0,    0,    0,
    0,    0,    0,   56,   55,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   36,
    0,    0,   40,   28,    0,    0,   39,    0,   33,   57,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   44,   45,   46,    0,   24,   41,   29,   31,   37,
};
final static short yydgoto[] = {                          1,
   10,   63,   17,   18,   22,   49,    2,   11,   19,   12,
   44,   14,   31,   34,   45,   65,   46,   51,   72,
};
final static short yysindex[] = {                         0,
    0,  -58,    0,    0,    0, -255,    0,    0,    0, -248,
    0,    0,    0,  -13,    0,  -72,   -7,    0, -150,    7,
 -194,  -25,    0, -248,    0,  -55,  -23, -183,    0,    0,
    0,    0,  -11,  171,    0,  -60,   60,   60,   33,  105,
   62,    0,    0,    0,    0,    0,  125,  125,  -47,  125,
  -56,    0,    0,   13,    0,    0,    0,    0,  125,  125,
    0,  125,   -9,  125,    0,   -2,   12,  125,  125,   36,
 -157,  -56,   22,    0,    0,   43,  125,  125,  125,  125,
  125,  125,  125,  125,  125,  125,  125,  125,  125,    0,
   52,  198,    0,    0,   64,   73,    0,  -56,    0,    0,
  106,  106,  181,  181,   94,   80,  181,  181,  -21,  -21,
    0,    0,    0,   75,    0,    0,    0,    0,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    2,    0,    0,  119,    0,
    0,    3,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  -37,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  234,    0,  -30,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  148,  155,  -17,   28,  157,  141,  138,  140,  126,  132,
    0,    0,    0,    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
    0,  447,    0,  120,    0,   92,    0,    0,    0,    0,
    1,    0,    0,   82,    0,    0,  -33,  114,    0,
};
final static int YYTABLESIZE=536;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         62,
   48,   15,   13,   62,   62,   62,   63,   62,   16,   62,
   63,   63,   63,   69,   63,   89,   63,   71,   21,   25,
   87,   62,   62,   49,   62,   88,   20,   89,   63,   63,
   47,   63,   87,   85,   89,   86,   24,   88,   99,   87,
   85,   49,   86,   68,   88,   13,   14,   26,   89,   90,
   83,   23,   84,   87,   85,   62,   86,   83,   88,   84,
   13,   14,   63,   27,  118,   28,   42,   30,   50,   32,
   94,   83,   89,   84,   33,   49,   97,   87,   85,   89,
   86,   35,   88,  100,   87,   85,   50,   86,   89,   88,
   93,   53,  114,   87,   85,   83,   86,   84,   88,   50,
   89,   64,   83,   47,   84,   87,   85,   98,   86,   89,
   88,   83,   68,   84,   87,   85,   89,   86,    1,   88,
   50,   87,   85,   83,   86,   84,   88,    7,    8,    9,
   89,  117,   83,  119,   84,   87,   85,   60,   86,   83,
   88,   84,   89,   29,   62,   73,   92,   87,   85,   59,
   86,   52,   88,   83,    0,   84,  116,   60,    0,    0,
    0,    0,    0,   61,   62,   83,   42,   84,   42,   59,
   42,    0,   43,    0,   43,    0,   43,    0,   47,    0,
   48,   54,    0,    0,   42,   42,    0,   42,   51,    0,
   43,   43,    0,   43,    0,   52,   47,   53,   48,   54,
   36,    3,    4,    5,    6,    0,   51,   37,    0,   38,
   39,   40,   41,   52,    0,   53,    0,   89,   42,    7,
    8,    9,   87,   85,   43,   86,    0,   88,    0,    0,
   47,    0,   48,   54,   62,   62,   62,   62,   62,   62,
   51,   63,   63,   63,   63,   63,   63,   52,    0,   53,
    0,    0,    0,    0,   49,   49,    0,    0,   49,   49,
    0,    0,   77,   78,   79,   80,   81,   82,    0,   77,
   78,   79,   80,   81,   82,    0,    0,    0,    0,    0,
    0,    0,    0,   77,   78,   79,   80,   81,   82,    0,
    0,    0,    0,   42,    0,   43,    0,    0,    0,   50,
   50,    0,    0,   50,   50,    0,    0,   77,   78,   79,
   80,   81,   82,    0,   77,   78,   79,   80,   81,   82,
   42,    0,  115,   77,   78,   79,   80,   81,   82,    0,
    0,    0,    0,    0,    0,   77,   78,   79,   80,   81,
   82,    0,    0,    0,   77,   78,   79,   80,   81,   82,
    0,   77,   78,   79,   80,   81,   30,    0,   30,    0,
    0,   54,   55,   56,    0,   77,   78,   79,   80,    0,
    0,    0,    0,    0,   57,   58,    0,    0,    0,   79,
   80,   54,   55,   56,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   57,   58,    0,   42,   42,   42,
   42,   42,   42,   43,   43,   43,   43,   43,   43,   47,
   47,   48,   48,   47,   47,   48,   48,   54,    0,    0,
    0,    0,    0,   51,   51,    0,    0,   36,    0,    0,
   52,   52,   53,   53,   37,    0,   38,   39,   40,   41,
    0,    0,    0,    0,    0,    0,    0,    0,    7,    8,
    9,    0,    0,    0,   36,    0,    0,    0,    0,    0,
    0,   37,    0,   38,   39,   40,   41,    0,    0,    0,
    0,    0,    0,    0,    0,    7,    8,    9,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   30,    0,    0,   66,   67,    0,   70,   30,    0,   30,
   30,   30,   30,    0,    0,   74,   75,    0,   76,    0,
   91,   30,   30,   30,   95,   96,    0,    0,    0,    0,
    0,    0,    0,  101,  102,  103,  104,  105,  106,  107,
  108,  109,  110,  111,  112,  113,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         37,
   61,  257,    2,   41,   42,   43,   37,   45,  257,   47,
   41,   42,   43,   61,   45,   37,   47,   51,   91,   19,
   42,   59,   60,   41,   62,   47,   40,   37,   59,   60,
   91,   62,   42,   43,   37,   45,   44,   47,   72,   42,
   43,   59,   45,   91,   47,   44,   44,   41,   37,   59,
   60,   59,   62,   42,   43,   93,   45,   60,   47,   62,
   59,   59,   93,  258,   98,   91,  123,  123,   41,   93,
   59,   60,   37,   62,  258,   93,   41,   42,   43,   37,
   45,   93,   47,   41,   42,   43,   59,   45,   37,   47,
   93,   59,   41,   42,   43,   60,   45,   62,   47,   40,
   37,   40,   60,   91,   62,   42,   43,  265,   45,   37,
   47,   60,   91,   62,   42,   43,   37,   45,    0,   47,
   93,   42,   43,   60,   45,   62,   47,  278,  279,  280,
   37,   59,   60,   59,   62,   42,   43,   33,   45,   60,
   47,   62,   37,   24,   40,   54,   65,   42,   43,   45,
   45,   38,   47,   60,   -1,   62,   93,   33,   -1,   -1,
   -1,   -1,   -1,   59,   40,   60,   41,   62,   43,   45,
   45,   -1,   41,   -1,   43,   -1,   45,   -1,   41,   -1,
   41,   41,   -1,   -1,   59,   60,   -1,   62,   41,   -1,
   59,   60,   -1,   62,   -1,   41,   59,   41,   59,   59,
  257,  260,  261,  262,  263,   -1,   59,  264,   -1,  266,
  267,  268,  269,   59,   -1,   59,   -1,   37,   93,  278,
  279,  280,   42,   43,   93,   45,   -1,   47,   -1,   -1,
   93,   -1,   93,   93,  272,  273,  274,  275,  276,  277,
   93,  272,  273,  274,  275,  276,  277,   93,   -1,   93,
   -1,   -1,   -1,   -1,  272,  273,   -1,   -1,  276,  277,
   -1,   -1,  272,  273,  274,  275,  276,  277,   -1,  272,
  273,  274,  275,  276,  277,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,  272,  273,  274,  275,  276,  277,   -1,
   -1,   -1,   -1,  123,   -1,  125,   -1,   -1,   -1,  272,
  273,   -1,   -1,  276,  277,   -1,   -1,  272,  273,  274,
  275,  276,  277,   -1,  272,  273,  274,  275,  276,  277,
  123,   -1,  125,  272,  273,  274,  275,  276,  277,   -1,
   -1,   -1,   -1,   -1,   -1,  272,  273,  274,  275,  276,
  277,   -1,   -1,   -1,  272,  273,  274,  275,  276,  277,
   -1,  272,  273,  274,  275,  276,  123,   -1,  125,   -1,
   -1,  257,  258,  259,   -1,  272,  273,  274,  275,   -1,
   -1,   -1,   -1,   -1,  270,  271,   -1,   -1,   -1,  274,
  275,  257,  258,  259,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,  270,  271,   -1,  272,  273,  274,
  275,  276,  277,  272,  273,  274,  275,  276,  277,  272,
  273,  272,  273,  276,  277,  276,  277,  277,   -1,   -1,
   -1,   -1,   -1,  276,  277,   -1,   -1,  257,   -1,   -1,
  276,  277,  276,  277,  264,   -1,  266,  267,  268,  269,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  278,  279,
  280,   -1,   -1,   -1,  257,   -1,   -1,   -1,   -1,   -1,
   -1,  264,   -1,  266,  267,  268,  269,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,  278,  279,  280,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
  257,   -1,   -1,   47,   48,   -1,   50,  264,   -1,  266,
  267,  268,  269,   -1,   -1,   59,   60,   -1,   62,   -1,
   64,  278,  279,  280,   68,   69,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   77,   78,   79,   80,   81,   82,   83,
   84,   85,   86,   87,   88,   89,
};
}
final static short YYFINAL=1;
final static short YYMAXTOKEN=282;
final static String yyname[] = {
"end-of-file",null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,"'!'",null,null,null,"'%'",null,null,"'('","')'","'*'","'+'",
"','","'-'",null,"'/'",null,null,null,null,null,null,null,null,null,null,null,
"';'","'<'","'='","'>'",null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,"'['",null,"']'",null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,"'{'",null,"'}'",null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,"ID","NUM_INT","NUM_REAL","INT",
"DOUBLE","BOOL","VOID","IF","ELSE","WHILE","BREAK","RETURN","PRINT","TRUE",
"FALSE","EQ","NE","LE","GE","AND","OR","MOSTRA_TS","TRACE_ON","TRACE_OFF","IFX",
"UMINUS",
};
final static String yyrule[] = {
"$accept : programa",
"programa : globais decl_main finais",
"globais :",
"globais : globais decl_var",
"globais : globais diretiva",
"finais :",
"finais : finais diretiva",
"diretiva : MOSTRA_TS",
"diretiva : TRACE_ON",
"diretiva : TRACE_OFF",
"decl_var : tipo lista_ids ';'",
"lista_ids : item_id",
"lista_ids : lista_ids ',' item_id",
"item_id : ID",
"item_id : ID dims_decl",
"dims_decl : '[' NUM_INT ']'",
"dims_decl : dims_decl '[' NUM_INT ']'",
"tipo : INT",
"tipo : DOUBLE",
"tipo : BOOL",
"decl_main : cab_main '(' ')' corpo",
"cab_main : VOID ID",
"corpo : '{' itens '}'",
"$$1 :",
"bloco : '{' $$1 itens '}'",
"itens :",
"itens : itens cmd",
"itens : itens diretiva",
"cmd : ID '=' expr ';'",
"cmd : ID indices '=' expr ';'",
"cmd : IF cond cmd",
"cmd : IF cond cmd ELSE cmd",
"$$2 :",
"cmd : WHILE cond $$2 cmd",
"cmd : BREAK ';'",
"cmd : RETURN ';'",
"cmd : RETURN expr ';'",
"cmd : PRINT '(' expr ')' ';'",
"cmd : bloco",
"cond : '(' expr ')'",
"indices : '[' expr ']'",
"indices : indices '[' expr ']'",
"expr : expr '+' expr",
"expr : expr '-' expr",
"expr : expr '*' expr",
"expr : expr '/' expr",
"expr : expr '%' expr",
"expr : expr '<' expr",
"expr : expr '>' expr",
"expr : expr LE expr",
"expr : expr GE expr",
"expr : expr EQ expr",
"expr : expr NE expr",
"expr : expr AND expr",
"expr : expr OR expr",
"expr : '!' expr",
"expr : '-' expr",
"expr : '(' expr ')'",
"expr : NUM_INT",
"expr : NUM_REAL",
"expr : TRUE",
"expr : FALSE",
"expr : ID",
"expr : ID indices",
};

//#line 129 "mini.y"
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
    // nesta versão só há declarações globais; VAR_LOCAL fica para quando houver locais
    Simbolo.Classe c = ts.nivel() == 0 ? Simbolo.Classe.VAR_GLOBAL : Simbolo.Classe.VAR_LOCAL;
    for (DeclId d : ids) {
      // int m[3][4] -> array(3, array(4, int)): constrói da dimensão mais interna para a externa
      Tipo t = base;
      List<Integer> dims = d.dims();
      for (int i = dims.size() - 1; i >= 0; i--) {
        if (dims.get(i) <= 0) erro("dimensão " + (i + 1) + " de '" + d.nome() + "' deve ser positiva");
        t = Tipo.array(t, dims.get(i));
      }
      declara(new Simbolo(d.nome(), c, t, ts.nivel(), d.linha()));
    }
  }

  void abreFuncao(String nome, Tipo ret) {    // usada para main
    funcAtual = new Simbolo(nome, Simbolo.Classe.FUNCAO, ret, 0, linha());
    declara(funcAtual);                 // inserida ANTES do corpo: permite recursão
    ts.abreEscopo("função " + nome, linha());    // escopo dos parâmetros e variáveis locais
  }

  void fechaFuncao() { ts.fechaEscopo(linha()); funcAtual = null; }

  /* ================= verificação de tipos ================= */
  Tipo usaVar(String nome) {
    Simbolo s = ts.resolve(nome, linha());
    if (s == null) { erro("identificador '" + nome + "' não declarado"); return Tipo.ERRO; }
    if (s.classe == Simbolo.Classe.FUNCAO) { erro("'" + nome + "' é uma função; use " + nome + "(...)"); return Tipo.ERRO; }
    return s.tipo;
  }

  /** nome[i1][i2]...: cada índice "descasca" uma dimensão do tipo. */
  Tipo indexa(String nome, List<Tipo> idx) {
    Tipo t = usaVar(nome);
    if (t.ehErro()) return Tipo.ERRO;
    if (!t.ehArray()) { erro("'" + nome + "' não é um arranjo"); return Tipo.ERRO; }
    if (idx.size() > t.dimensoes()) {
      erro("'" + nome + "' tem " + t.dimensoes() + " dimensão(ões), mas foi indexado com " + idx.size() + " índice(s)");
      return Tipo.ERRO;
    }
    for (int i = 0; i < idx.size(); i++) {
      Tipo ti = idx.get(i);
      if (!ti.ehErro() && ti != Tipo.INT)
        erro("índice " + (i + 1) + " de '" + nome + "' deve ser int, encontrado " + ti);
      t = t.elem;                       // int[3][4] --[i]--> int[4] --[j]--> int
    }
    return t;                           // pode ainda ser arranjo (indexação parcial: m[i])
  }

  void atribui(String nome, List<Tipo> idx, Tipo tExpr) {
    Tipo alvo = (idx == null) ? usaVar(nome) : indexa(nome, idx);
    if (alvo.ehArray()) {
      erro("não é permitido atribuir a um arranjo ('" + nome + "' " + (idx == null ? "" : "indexado ")
           + "tem tipo " + alvo + ")");
      return;
    }
    if (!tExpr.atribuivelA(alvo))
      erro("tipos incompatíveis na atribuição a '" + nome + "': esperado " + alvo + ", encontrado " + tExpr);
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
    // a gramática já exige a função main; a verificação fica para versões com várias funções
    Simbolo m = ts.buscaLocal("main");
    if (m == null || m.classe != Simbolo.Classe.FUNCAO) erro("programa sem função 'main'");
    ts.fechaEscopo(linha());
  }

  /* =====================================================================
   * RESERVADO PARA FUTURAS IMPLEMENTAÇÕES
   * Não é usado pela gramática desta versão (não há parâmetros nem chamadas).
   * Para ativar: produções de parâmetros e de chamada, como na versão completa.
   * ===================================================================== */
  void declaraParam(String nome, Tipo t) {
    funcAtual.params.add(t);            // completa a assinatura (T1,...,Tn) -> U
    declara(new Simbolo(nome, Simbolo.Classe.PARAM, t, ts.nivel(), linha()));
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
//#line 612 "Parser.java"
//###############################################################
// method: yylexdebug : check lexer state
//###############################################################
void yylexdebug(int state,int ch)
{
String s=null;
  if (ch < 0) ch=0;
  if (ch <= YYMAXTOKEN) //check index bounds
     s = yyname[ch];    //now get it
  if (s==null)
    s = "illegal-symbol";
  debug("state "+state+", reading "+ch+" ("+s+")");
}





//The following are now global, to aid in error reporting
int yyn;       //next next thing to do
int yym;       //
int yystate;   //current parsing state from state table
String yys;    //current token string


//###############################################################
// method: yyparse : parse input and execute indicated items
//###############################################################
int yyparse()
{
boolean doaction;
  init_stacks();
  yynerrs = 0;
  yyerrflag = 0;
  yychar = -1;          //impossible char forces a read
  yystate=0;            //initial state
  state_push(yystate);  //save it
  val_push(yylval);     //save empty value
  while (true) //until parsing is done, either correctly, or w/error
    {
    doaction=true;
    if (yydebug) debug("loop"); 
    //#### NEXT ACTION (from reduction table)
    for (yyn=yydefred[yystate];yyn==0;yyn=yydefred[yystate])
      {
      if (yydebug) debug("yyn:"+yyn+"  state:"+yystate+"  yychar:"+yychar);
      if (yychar < 0)      //we want a char?
        {
        yychar = yylex();  //get next token
        if (yydebug) debug(" next yychar:"+yychar);
        //#### ERROR CHECK ####
        if (yychar < 0)    //it it didn't work/error
          {
          yychar = 0;      //change it to default string (no -1!)
          if (yydebug)
            yylexdebug(yystate,yychar);
          }
        }//yychar<0
      yyn = yysindex[yystate];  //get amount to shift by (shift index)
      if ((yyn != 0) && (yyn += yychar) >= 0 &&
          yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
        {
        if (yydebug)
          debug("state "+yystate+", shifting to state "+yytable[yyn]);
        //#### NEXT STATE ####
        yystate = yytable[yyn];//we are in a new state
        state_push(yystate);   //save it
        val_push(yylval);      //push our lval as the input for next rule
        yychar = -1;           //since we have 'eaten' a token, say we need another
        if (yyerrflag > 0)     //have we recovered an error?
           --yyerrflag;        //give ourselves credit
        doaction=false;        //but don't process yet
        break;   //quit the yyn=0 loop
        }

    yyn = yyrindex[yystate];  //reduce
    if ((yyn !=0 ) && (yyn += yychar) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
      {   //we reduced!
      if (yydebug) debug("reduce");
      yyn = yytable[yyn];
      doaction=true; //get ready to execute
      break;         //drop down to actions
      }
    else //ERROR RECOVERY
      {
      if (yyerrflag==0)
        {
        yyerror("syntax error");
        yynerrs++;
        }
      if (yyerrflag < 3) //low error count?
        {
        yyerrflag = 3;
        while (true)   //do until break
          {
          if (stateptr<0)   //check for under & overflow here
            {
            yyerror("stack underflow. aborting...");  //note lower case 's'
            return 1;
            }
          yyn = yysindex[state_peek(0)];
          if ((yyn != 0) && (yyn += YYERRCODE) >= 0 &&
                    yyn <= YYTABLESIZE && yycheck[yyn] == YYERRCODE)
            {
            if (yydebug)
              debug("state "+state_peek(0)+", error recovery shifting to state "+yytable[yyn]+" ");
            yystate = yytable[yyn];
            state_push(yystate);
            val_push(yylval);
            doaction=false;
            break;
            }
          else
            {
            if (yydebug)
              debug("error recovery discarding state "+state_peek(0)+" ");
            if (stateptr<0)   //check for under & overflow here
              {
              yyerror("Stack underflow. aborting...");  //capital 'S'
              return 1;
              }
            state_pop();
            val_pop();
            }
          }
        }
      else            //discard this token
        {
        if (yychar == 0)
          return 1; //yyabort
        if (yydebug)
          {
          yys = null;
          if (yychar <= YYMAXTOKEN) yys = yyname[yychar];
          if (yys == null) yys = "illegal-symbol";
          debug("state "+yystate+", error recovery discards token "+yychar+" ("+yys+")");
          }
        yychar = -1;  //read another
        }
      }//end error recovery
    }//yyn=0 loop
    if (!doaction)   //any reason not to proceed?
      continue;      //skip action
    yym = yylen[yyn];          //get count of terminals on rhs
    if (yydebug)
      debug("state "+yystate+", reducing "+yym+" by rule "+yyn+" ("+yyrule[yyn]+")");
    if (yym>0)                 //if count of rhs not 'nil'
      yyval = val_peek(yym-1); //get current semantic value
    yyval = dup_yyval(yyval); //duplicate yyval if ParserVal is used as semantic value
    switch(yyn)
      {
//########## USER-SUPPLIED ACTIONS ##########
case 1:
//#line 34 "mini.y"
{ verificaMain(); }
break;
case 7:
//#line 45 "mini.y"
{ ts.imprime(linha()); }
break;
case 8:
//#line 46 "mini.y"
{ ts.setTrace(true); }
break;
case 9:
//#line 47 "mini.y"
{ ts.setTrace(false); }
break;
case 10:
//#line 51 "mini.y"
{ declaraVars((Tipo)val_peek(2).obj, (List<DeclId>)val_peek(1).obj); }
break;
case 11:
//#line 53 "mini.y"
{ List<DeclId> l = new ArrayList<>(); l.add((DeclId)val_peek(0).obj); yyval.obj = l; }
break;
case 12:
//#line 54 "mini.y"
{ ((List<DeclId>)val_peek(2).obj).add((DeclId)val_peek(0).obj); yyval.obj = val_peek(2).obj; }
break;
case 13:
//#line 56 "mini.y"
{ yyval.obj = new DeclId(val_peek(0).sval, List.of(), linha()); }
break;
case 14:
//#line 57 "mini.y"
{ yyval.obj = new DeclId(val_peek(1).sval, (List<Integer>)val_peek(0).obj, linha()); }
break;
case 15:
//#line 59 "mini.y"
{ List<Integer> l = new ArrayList<>(); l.add(val_peek(1).ival); yyval.obj = l; }
break;
case 16:
//#line 60 "mini.y"
{ ((List<Integer>)val_peek(3).obj).add(val_peek(1).ival); yyval.obj = val_peek(3).obj; }
break;
case 17:
//#line 62 "mini.y"
{ yyval.obj = Tipo.INT; }
break;
case 18:
//#line 63 "mini.y"
{ yyval.obj = Tipo.DOUBLE; }
break;
case 19:
//#line 64 "mini.y"
{ yyval.obj = Tipo.BOOL; }
break;
case 20:
//#line 68 "mini.y"
{ fechaFuncao(); }
break;
case 21:
//#line 70 "mini.y"
{ if (!val_peek(0).sval.equals("main"))
                                                erro("nesta versão só é permitida a função 'main' (encontrado '" + val_peek(0).sval + "')");
                                              abreFuncao(val_peek(0).sval, Tipo.VOID); }
break;
case 23:
//#line 78 "mini.y"
{ ts.abreEscopo("bloco", linha()); }
break;
case 24:
//#line 79 "mini.y"
{ ts.fechaEscopo(linha()); }
break;
case 28:
//#line 85 "mini.y"
{ atribui(val_peek(3).sval, null, (Tipo)val_peek(1).obj); }
break;
case 29:
//#line 86 "mini.y"
{ atribui(val_peek(4).sval, (List<Tipo>)val_peek(3).obj, (Tipo)val_peek(1).obj); }
break;
case 32:
//#line 89 "mini.y"
{ laco++; }
break;
case 33:
//#line 90 "mini.y"
{ laco--; }
break;
case 34:
//#line 91 "mini.y"
{ if (laco == 0) erro("'break' fora de um laço"); }
break;
case 35:
//#line 92 "mini.y"
{ checaReturn(null); }
break;
case 36:
//#line 93 "mini.y"
{ checaReturn((Tipo)val_peek(1).obj); }
break;
case 37:
//#line 94 "mini.y"
{ checaPrint((Tipo)val_peek(2).obj); }
break;
case 39:
//#line 97 "mini.y"
{ checaCond((Tipo)val_peek(1).obj); }
break;
case 40:
//#line 99 "mini.y"
{ List<Tipo> l = new ArrayList<>(); l.add((Tipo)val_peek(1).obj); yyval.obj = l; }
break;
case 41:
//#line 100 "mini.y"
{ ((List<Tipo>)val_peek(3).obj).add((Tipo)val_peek(1).obj); yyval.obj = val_peek(3).obj; }
break;
case 42:
//#line 104 "mini.y"
{ yyval.obj = aritmetico("+", (Tipo)val_peek(2).obj, (Tipo)val_peek(0).obj); }
break;
case 43:
//#line 105 "mini.y"
{ yyval.obj = aritmetico("-", (Tipo)val_peek(2).obj, (Tipo)val_peek(0).obj); }
break;
case 44:
//#line 106 "mini.y"
{ yyval.obj = aritmetico("*", (Tipo)val_peek(2).obj, (Tipo)val_peek(0).obj); }
break;
case 45:
//#line 107 "mini.y"
{ yyval.obj = aritmetico("/", (Tipo)val_peek(2).obj, (Tipo)val_peek(0).obj); }
break;
case 46:
//#line 108 "mini.y"
{ yyval.obj = modulo((Tipo)val_peek(2).obj, (Tipo)val_peek(0).obj); }
break;
case 47:
//#line 109 "mini.y"
{ yyval.obj = relacional("<",  (Tipo)val_peek(2).obj, (Tipo)val_peek(0).obj); }
break;
case 48:
//#line 110 "mini.y"
{ yyval.obj = relacional(">",  (Tipo)val_peek(2).obj, (Tipo)val_peek(0).obj); }
break;
case 49:
//#line 111 "mini.y"
{ yyval.obj = relacional("<=", (Tipo)val_peek(2).obj, (Tipo)val_peek(0).obj); }
break;
case 50:
//#line 112 "mini.y"
{ yyval.obj = relacional(">=", (Tipo)val_peek(2).obj, (Tipo)val_peek(0).obj); }
break;
case 51:
//#line 113 "mini.y"
{ yyval.obj = igualdade("==", (Tipo)val_peek(2).obj, (Tipo)val_peek(0).obj); }
break;
case 52:
//#line 114 "mini.y"
{ yyval.obj = igualdade("!=", (Tipo)val_peek(2).obj, (Tipo)val_peek(0).obj); }
break;
case 53:
//#line 115 "mini.y"
{ yyval.obj = logico("&&", (Tipo)val_peek(2).obj, (Tipo)val_peek(0).obj); }
break;
case 54:
//#line 116 "mini.y"
{ yyval.obj = logico("||", (Tipo)val_peek(2).obj, (Tipo)val_peek(0).obj); }
break;
case 55:
//#line 117 "mini.y"
{ yyval.obj = negacao((Tipo)val_peek(0).obj); }
break;
case 56:
//#line 118 "mini.y"
{ yyval.obj = menosUnario((Tipo)val_peek(0).obj); }
break;
case 57:
//#line 119 "mini.y"
{ yyval.obj = val_peek(1).obj; }
break;
case 58:
//#line 120 "mini.y"
{ yyval.obj = Tipo.INT; }
break;
case 59:
//#line 121 "mini.y"
{ yyval.obj = Tipo.DOUBLE; }
break;
case 60:
//#line 122 "mini.y"
{ yyval.obj = Tipo.BOOL; }
break;
case 61:
//#line 123 "mini.y"
{ yyval.obj = Tipo.BOOL; }
break;
case 62:
//#line 124 "mini.y"
{ yyval.obj = usaVar(val_peek(0).sval); }
break;
case 63:
//#line 125 "mini.y"
{ yyval.obj = indexa(val_peek(1).sval, (List<Tipo>)val_peek(0).obj); }
break;
//#line 967 "Parser.java"
//########## END OF USER-SUPPLIED ACTIONS ##########
    }//switch
    //#### Now let's reduce... ####
    if (yydebug) debug("reduce");
    state_drop(yym);             //we just reduced yylen states
    yystate = state_peek(0);     //get new state
    val_drop(yym);               //corresponding value drop
    yym = yylhs[yyn];            //select next TERMINAL(on lhs)
    if (yystate == 0 && yym == 0)//done? 'rest' state and at first TERMINAL
      {
      if (yydebug) debug("After reduction, shifting from state 0 to state "+YYFINAL+"");
      yystate = YYFINAL;         //explicitly say we're done
      state_push(YYFINAL);       //and save it
      val_push(yyval);           //also save the semantic value of parsing
      if (yychar < 0)            //we want another character?
        {
        yychar = yylex();        //get next character
        if (yychar<0) yychar=0;  //clean, if necessary
        if (yydebug)
          yylexdebug(yystate,yychar);
        }
      if (yychar == 0)          //Good exit (if lex returns 0 ;-)
         break;                 //quit the loop--all DONE
      }//if yystate
    else                        //else not done yet
      {                         //get next state and push, for next yydefred[]
      yyn = yygindex[yym];      //find out where to go
      if ((yyn != 0) && (yyn += yystate) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yystate)
        yystate = yytable[yyn]; //get new state
      else
        yystate = yydgoto[yym]; //else go to new defred
      if (yydebug) debug("after reduction, shifting from state "+state_peek(0)+" to state "+yystate+"");
      state_push(yystate);     //going again, so push state & val...
      val_push(yyval);         //for next action
      }
    }//main loop
  return 0;//yyaccept!!
}
//## end of method parse() ######################################



//## run() --- for Thread #######################################
//## The -Jnorun option was used ##
//## end of method run() ########################################



//## Constructors ###############################################
/**
 * Default constructor.  Turn off with -Jnoconstruct .

 */
public Parser()
{
  //nothing to do
}


/**
 * Create a parser, setting the debug to true or false.
 * @param debugMe true for debugging, false for no debug.
 */
public Parser(boolean debugMe)
{
  yydebug=debugMe;
}
//###############################################################



}
//################### END OF CLASS ##############################
