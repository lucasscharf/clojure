# Considerações gerais
Notação prefixada: (println "Hello world!")

# Tipo de dados

Tipos de dados (forms) | Exemplo
---|---
Número | 3.14, 9
Símbolo | user/foo, java.lang.String
String | "Coxinha"
Booleano| true/false
Caracter| \a, \c
Palavra chave| :tag, :doc
List | (1 2 3)
Nulo | Nil
Array| [1 2 3]
Mapa | {:name Bill, :age 42}
Record | #user.Person{:name Bill, :age 42}

## Número

**Atenção pra pegadinha**

```clojure
> (println(class (/ 22.0 7)))
clojure.lang.Ratio

> (println(class (/ 22 7)))
java.lang.Double
```

Os sufixos `N` e `M` alteram o tipo de dados de `java.lang.Long`,`java.lang.Double` para `clojure.lang.BigInt` e `java.math.BigDecimal`. Se o número for arbitrariamente longo, clojure parece fazer a conversão corretamente. Contudo, se isso acontecer dentro de uma operação, ele não converte.

```clojure
>(println (class 22))
java.lang.Long

>(println (class 22.0))
java.lang.Double

>(println (class 22N))
clojure.lang.BigInt

>(println (class 22M))
java.math.BigDecimal

>; O maior valor de long é 9223372036854775807
>(println (class 9223372036854775808))
clojure.lang.BigInt

>(println (class (+ 9223372036854775807 20)))
long overflow
```

## Símbolos
Servem pra nomear coisas. Aparentemente, tudo o que tem um nome é um símbolo.

* Funções
* Operadores
* Classes Java
* Namespaces
* Estrutura de dados

Símbolos não podem começar com número, mas aceitam alfanuméricos e `+`,`-`,`*`,`/`,`!`,`.`, e `_`.

## Strings
Usam aspas duplas `""`. Suportam multilinha, porém não fazem nenhum tratamento especial para espaços e formatação. Se tiver espaço, então ele será considerado parte da string. Também é possível fazer quebra de linha com `\n`.

É possível usar a interoperabilidade com Java para os métodos de String.

```clojure
>(println (.toUpperCase "lower case"))
LOWER 

>(println (class "lower case"))
java.lang.String
```

## Boleanos
true é true

false é false

\nil é false

o resto true

0 é true

coleção vazia é true

tirando o que não é false, tudo é true

## Mapas
Par de chave/valor. Podem ser separados por vírgulas ou espaços. Usamos chaves `{}` para delimitar um par.

```clojure
>(def inventores {"Chuveiro Elétrico" "Francisco Canho", "Avião" "Santos Dummont"})
>(println (inventores "Chuveiro Elétrico"))
Francisco Canho

>(println (inventores "Escorredor de arroz"))
nil
```

## Records
Uma estrutura de dados imutável estrutrada com campos nomeados. São semelhantes a mapas, porém podem implementar interfaces Java e tem performance superior. São definidos com a macro `defrecord`.
A sintaxe fica assim ``(defrecord Name [field1 field2 ...])``.

```clojure
(defrecord Person [name age])
(def john (->Person "John Doe" 30))

>(println john)
#user.Person{:name John Doe, :age 30}

>(println (:name john)) ; acesso via palavra chave
John Doe

>(println (.age john)) ; acesso via função
30
```

# Variáveis
Clojure é imutável. Não tem variáveis. É possível definir constantes através das palavras `def` e `let`. Ambos funcionam de forma semelhante. Mas tem algumas pegadinhas.

Característica|`let`|`def`
Escopo|Local|Global dentro de um `namespace`
Mutabilidate|Imutável|"Mutável" pode definir outro elemento
Ciclo de vida|Apenas dentro do bloco|Enquanto o programa estivar rodando


```clojure
>(def foo 10) 
>(println foo)
10

>(defn cantos-quadrado [baixo esquerda tamanho]; retorna quatro pontos delimitando um quadrado
>  (let [cima (+ baixo tamanho) direita (+ esquerda tamanho)]
>    [[baixo esquerda] [cima esquerda] [cima direita] [baixo direita]]))
>(println (cantos-quadrado 1,2,5))
[[1 2] [6 2] [6 7] [1 7]]
```

A função `var` permite a manipulação da própria variável para o caso de metaprogramação. Também é possível usar a macro `#'` para fazer isso.

# Destrutores
É possível quebrar "objetos" (como mapas, records e vetores). Isso significa que é possível pegar utilizar apenas um subconjunto de elementos de uma coleção.

```clojure
>(let [[_ _ z] [1 2 3]]
>    (println z))
2

>(defn ola-1 [pessoa]
>    (str "Opa-1, " (:nome pessoa))) 

>(defn ola-2 [{primeiro-nome :nome}]
>    (str "Opa-2, " primeiro-nome)) 

>(defrecord Pessoa[nome])
>(def pessoa-1 (->Pessoa "Julio") )
>(def pessoa-2 {:nome "Cesar"})

>(println (ola-1 pessoa-1))
Opa-1, Julio

>(println (ola-1 pessoa-2))
Opa-1, Cesar

>(println (ola-2 pessoa-1))
Opa-2, Julio

>(println (ola-2 pessoa-2))
Opa-2, Cesar
```

A cláusula `:as` permite fazer o `binding` tanto dos elementos quanto das coleções simultaneamente:

```clojure
>(def destrutor (let [[x y :as elementos] [2 4 6 8]]
    (str "X: " x " Y: " y " Total elementos: " (count elementos))
    ))
>(println destrutor)
X: 2 Y: 4 Total elementos: 4
```

# Funções
Nessa seção, são algumas das principais funções existentes.
A síntaxe de uma função em clojure é uma lista (definida por parênteses cujo primeiro elemento é a função a ser executada).
As funções permitem sobrecarga.

Para definir a própria função, a sintaxe é:
```clojure
(defn name doc-string? attr-map? [params*] body)
```

Exemplo:
```clojure
>(defn opa-mano
>"Retorna uma saudação com o formato 'Opa, mano, {username}.'"
>([] (opa-mano "Parça"))
>([username](str "Opa, mano " username)))

>(println (opa-mano "Ale"))
Opa, mano Ale

>(println (opa-mano))
Opa, mano Parça
```

A função `(clojure.repl/doc f)` permite ver a documentação (estilo JavaDoc) da função `f`.

O operador `&` permite agrupar parâmetros dentro de uma coleção numa chamada de função. Funciona como o operador `...` em Java. 

A macro `#`permite a criação de funções anônimas para serem utilizadas dentro de outras funções. As seguintes funções são equivalentes e produzem uma sequência de caracteres em caixa alta:

```clojure
(defn upper-case-manual [s]
  (.toUpperCase s))

(let [vetor ["a", "b", "c"]]
  (map upper-case-manual vetor))

(let [vetor ["a", "b", "c"]]
  (map #(.toUpperCase %) vetor)
  )

(let [vetor ["a", "b", "c"]]
  (map (fn [x] (.toUpperCase x)) vetor)
)
```

## concat
Concatena coleções

```clojure
> (println (concat [1 1 1] [2 2 2] [3]))
(1 1 1 2 2 2 3)
```

## str
Transforma os parâmetros em texto/string. Equivalente ao `.toString()` do Java. Com a diferença junta vários argumentos e ignora `\nil`.

```clojure
>(println(str 1 2 nil 3))
123
```

## clojure.string.split
Quebra uma sentença em palavras

### clojure.string.join
Faz a concatenação de string e concatenação de elementos de uma coleção. Semelhante ao `String.joining` do Java.

```clojure
(require '[clojure.string :as str])

(println (str/join ["A" "B" "C" "..."]))
(println (str/join "_" ["A" "B" "C" "..."]))
(println (str "A" "B" "C" "..." "D"))
```

## filter
`(filter pred coll)`
Retorna recebe um predicado e uma coleção. Seleciona apenas os elementos cujo o resultado do predicado é `true`.

## take-while
`(take-while pred coll)`. Retorna todos os elementos da sequência enquanto o predicato é `true`. Em outras palavras, cria uma coleção sequência com os primeiros elementos da coleção enquanto o predicado for true.

## drop-while
`(drop-while pred coll)`. Ignora todos os elementos da sequência enquanto o predicato é `true`. Em outras palavras, cria uma coleção sequência com os últimos à partir do momento que o predicado é falso.

## split-at e split-with
Quebram uma coleção em duas. `(split-at index coll)` quebra em um índice e `(split-with pred coll)` quebra de acordo com um  predicado. No primeiro momento que o predicado retornar `false`, ele gerará a outra parte da sequência.

```clojure
(println (split-with neg? (range -10 10)))
>[(-10 -9 -8 -7 -6 -5 -4 -3 -2 -1) (0 1 2 3 4 5 6 7 8 9)]

(println (split-with fpn? (range -10 10)))
>[(-10) (-9 -8 -7 -6 -5 -4 -3 -2 -1 0 1 2 3 4 5 6 7 8 9)]
```

## interleave
Alterna elementos de diferentes coleções. Considera o tamanho da menor coleção

```clojure
>(println (interleave [1 1 1] [2 2 2] [3 3 3]))
(1 2 3 1 2 3 1 2 3)

>(println (interleave [1 1 1] [2 2 2] [3]))
(1 2 3)

> (println (interleave "Attack at midnight" "The purple elephant chortled"))
(A T t h t e a   c p k u   r a p t l   e m   i e d l n e i p g h h a t n)
```

## interpose
`(interpose separator coll)` separa os elementos da coleção usando o separador. Funciona semelhante ao str/join.


## first
`(first seq)` retorna o primeiro elemento de uma sequência.

## rest
`(rest seq)` e `(rest n seq)` retorna uma nova sequência com resto da sequência. Na versão de 1 parâmetro retorna sem o primeiro elemento. Na versão de dois parâmetros, retorna à partir do n-ésimo elemento.

## seq
`(seq col)` recebe uma coleção e retorna uma sequência sobre a mesma.

## next
`(next seq)` retorna uma sequência com todos os elementos depois do primeiro. Se não tem mais nenhum elemento, retorna `nil`. É o mesmo que `(seq (rest col))`.

## sorted-set/sorted-map
`(sorted-set & elements)` retorna um conjunto ou mapa permitindo que sejam atravassados pelo ordenamento natural.

## cons
`(cons elem seq)` constrói uma nova sequência adicionando novos elementos no começo de uma sequência.

## conj
`(conj colection & elementos)` adiciona um elemento ou mais elementos em uma coleção.

## into
`(conj to-coll from-coll)` adiciona todos os elementos de uma coleção em outra.

## range
`(range começo? fim passo?)` cria sequências, inclui o valor do `começo` mas não o do `fim`. Os valores padrões para `começo` e `passo` são 0 e 1, respectivamente. Funciona apenas com números.

## repeat
`(repeat n x)` repete o valor `x` por `n` vezes.

## iterate
`(iterate f x)` cria uma sequência começando com o valor `x`n, aplicando a função `f` para cvalcular o próximo. Isso gera uma sequência infinita. É ncessário utilizar junto com funções como `take`.

```clojure
(println (take 10 (iterate inc 1)))
-> (1 2 3 4 5 6 7 8 9 10)
```

## take-nth
`(take-nth n coll)`
Pega o n-ésimo elemento de uma coleção suscetivamente.

```clojure
>(println(take-nth 2 [1 2 3 4 5 6 7 8 9]))
(1 3 5 7 9)
```

## take 
`(take n seq)` retorna os n primeiros elementos de uma coleção.

## cycle
`(cycle coll)` cria uma sequência infinita repetindo os elementos de uma determinada coleção.

```clojure
(println (take 10 (cycle [0 1 2])))
-> (0 1 2 0 1 2 0 1 2 0)
```

## mod
`(mod a b)` operador módulo. Equivalente à (a%b) em Java.

## quot
`(quot a b)` operador quociente. Também chamado de divisão inteira. O arredondamento é para o inteiro mais próximo.

## apply
`(apply f args)`

A função `apply` "abre" uma coleção e passa os seus elementos como argumentos "individuais" (ou em conjunto, de acordo com a função `f`) para a função `f`.
Funciona como uma espécie de acumulador. Caso o objeto seja aplicar uma função dentro dos elementos de uma sequência gerando uma nova sequência, olhar a função `map`.

```clojure
(apply + [1 2 3 4 5])
-> 15

(apply + 10 [1 2 3])
-> 16

> (println (str (interleave "Attack at midnight" "The purple elephant chortled")))
clojure.lang.LazySeq@d4ea9f36

> (println (apply str (interleave "Attack at midnight" "The purple elephant chortled")))
ATthtea cpku raptl em iedlneipghhatn
```

## map
`(map f coll*)` igual a Java. Aplica `f`aos elementos de `coll`criando uma nova coleção. 
`map` pode receber mais de uma `coll`, se isso acontecer, ela pegar um elemento de cada coleção e aplicará sobre a função `f`.

## reduce
`(reduce f coll)` a função `f` recebe dois parâmetros. Ela é aplicada aos dois primeiros elementos de `coll`. O resultado é aplicado ao terceiro, quarto, etc...

## sort e sort-by
As funções de ordenamento `sort` e `sort-by` possuem assinaturas semelhantes: `(sort coll)`, `(sort comparator coll)`, `(sort-by keyfn coll)` e `(sort-by keyfn comparator coll)`. A função `sort` retorna o ordenamento natural dos elementos de `coll`, assumindo que eles são comparáveis. Ela também possui uma sobrecarga que aceita um comparador (`comparator`) — uma função que recebe dois elementos e retorna um número indicando a ordem entre eles. Já sort-by permite fornecer uma função (`keyfn`) para extrair uma chave dos elementos (como em uma lista de mapas), facilitando o ordenamento com base em atributos específicos.

## doall/dorun
`(doall coll)` e `(dorun coll)` força a avaliação/execução de uma sequência preguicosa. A diferença é que o `durun`não armazena os elementos em memória. Dessa forma, o `dorun` pode ser utilizado para avaliar sequências muito grandes.

## keys e vals
`(keys mapa)` e `(vals mapa)` manipulam as chaves e valores de um mapa. `vals`retorna uma sequência ordenada dos valores em um mapa e o `keys`faz o mesmo para as chaves. Utiliza o mesmo ordenamento que `(seq map)`.

## re-matcher e re-seq
`(re-matcher regexp string)` e `(re-seq regex string)` manipulam strings e expressões regulares. `re-matcher` não é estilo clojure. `re-seq` retorna uma sequência imutável de strings que satisfazem a expressão regular. Essa sequência pode ser manipulada por meio de operações de sequência.

```clojure
(re-seq #"\w+" "the quick brown fox") 
-> ("the" "quick" "brown" "fox")

(map #(.toUpperCase %) (re-seq #"\w+" "the quick brown fox"))
-> ("THE" "QUICK" "BROWN" "FOX")
```

## re-seq
`(re-seq regexp string)` retorna uma sequência de strings que satisfazem as condições da expressão regular passada em `regexp`.

## slupr
`(slurp f & opts)` abre um leitor (`clojure.java.io/reader`) `f` e lê todo o conteúdo retornando como uma string.

# predicados
Predicados são um conjunto de funções que retornam `true` ou `false`. Por convenção terminam com um ponto de interrogação. 

## pos?
`(pos? num)` Predicado que verifica se um número é maior que zero (aka.  **pos**itivo).

## zero?
`(zero? num)` Predicado que verifica se um número é zero.

## every? some? not-every? not-any?
`(every? pred coll)` Aplica `pred` sobre a coleção `coll`e retorna `true`se pred for verdadeiror para todos os elementos.

`some?` retorna `true` se alguma avaliação retornar `true`.
`not-every?` retorna `true` se alguma avaliação retornar `false`.
`not-any?` retorna `true` se todas as avaliações retornarem `false`.

# Estruturas de controle

## if 
`(if a b c)`
Se `a` for `true`, execute `b`, caso contrário, execute `c`. Se `c` não for especificado, o if retorna nil quando a for falso.

Para executar `b` e `c` com multiplas linhas, é necessário utilizar o `form` `do`. Esse form executará todos os forms internos e retornará a última linha como resultado.


```clojure
(if true (println "Oi") (println "mundo"))
>Oi
(if false (println "Oi") (println "mundo"))
>mundo
(if true (do
           (println "Oi")
           (println "123")
           ) (println "mundo"))
>Oi 
>123
```

## when
`(when a b)` se `a` for verdadeiro, então execute `b`.

## loop/recur
`(loop [bindings *] exprs*)`. Funciona de forma semelhante ao `let` que permite fazer bindings e executar expressões sobre esses bindings. Enquanto o `let` faz binding imutáveis, os bindings do `loop` mudam através do `recur`.
O form especial `(recur exprs*)` permite o binding de novos valores dentro do `loop`.

```clojure
(println (loop [result [] x 5]
    (if (zero? x)
        result
    (recur (conj result x) (dec x)))))
>[5 4 3 2 1]
```

Basicamente, o par `loop/recur` acaba funcionando como uma espécie de chamadas recursivas dentro do loop. O `recur` permite reutilizar a pilha de execução garantindo o funcoinamento sem precisar gerar novas chamadas e uso adicional de memória (a ideia é semelhante a presente em `tail call optimization` porém no contexto de um `loop`). O `recur` precisa ter os mesmos parâmetros que o `loop` ou teremos erro de compilação. O `recur` deve ser a última expressão avaliada dentro de loop. Caso contrário, o compilador gera um erro. 

```clojure
(println (loop [result [] x 5]
    (when (= x 6)
             (recur result (dec x)))
    (if (zero? x)
        result
    (recur (conj result x) (dec x)))))
> não compila

(println (loop [result [] x 5]
    (if (= x 6)
             (recur result (dec x))
    (if (zero? x)
        result
    (recur (conj result x) (dec x))))))
>[5 4 3 2 1]
```

Caso não esteja pareado com um `loop`, o `recur` irá fazer a recorrência para a função que ele faz parte.

```clojure
(defn countdown [result x]
(if (zero? x)
result
(recur (conj result x) (dec x))))

(println (countdown [] 5))
> [5 4 3 2 1]
```

## for
`(for [binding-form coll ... ] expr)`. A macro `for`gera uma sequência preguiçosa baseado nos bindings e mapeamentos. Essa macro itera sobre os elementos do de `coll` fazendo os devidos bindings. Para cada combinação de bindings correta, ele aplica a `expr`. Caso exista mais do que coleção, a macro faz o produto cartesiano entre os elementos. É possível utilizar cláusulas especiais `:when` para filtrar elementos, o `:while` para indicar que a macro deve terminar ou o `let` para bindings auxiliares. 
A macro `for` também pode ser utilizado para desconstruir mapas.

O for só é executado quando for chamado (ou seja, a sequência é preguioçosa). 

```clojure
(println (for [x (range 6) :when (odd? x)] (* x x)))
>(1 9 25)
(println (for [[k v] {:a 1 :b 2 :c 3}] [k (* v v)]))
>([:a 1] [:b 4] [:c 9])
(println 
 (for [a (range 2) b (range 2), c (range 2)] 
     [a b c])
)
>([0 0 0] [0 0 1] [0 1 0] [0 1 1] [1 0 0] [1 0 1] [1 1 0] [1 1 1])

```

# Macro
Macros são funções que operam diretamente no código. Eles extendem a linguagem no tempo de compilação. São usados para metaprogramação.

# Reader macros
São estruturas de código que facilitam a leitura e entendedimento do código. São definidos pelo clojure. O exemplo de reader macro mais simples é o comentário `;`.

# Integração com Java
Clojure roda na JVM. Logo ele consegue acessar tudo o que temos de JVM, incluindo código Java. Além da parte de namespaces, e importações, é possível acessar outras coisas diretamente do JVM.
Por padrão, clojure importa o pacote `java.lang`.

O `form` `new` funciona como a palavra reservada `new` do Java para instanciar um objeto. 

A forma de chamar os métodos Java dentro do clojure é a seguinte:

```clojure
(. classe-ou-instância método-ou-simbolo & parâmetros)
(. classe-ou-instância (método-ou-simbolo & parâmetros))
```

É possível fazer `binding` caso queira-se utilizar o novo objeto. 

```clojure
>(println (. (new java.util.Random) nextInt))
12312572123

>(def rdn (new java.util.Random))
>(println (. rdn nextInt))
-4123912351

>(. Math PI)
3.141592653589793
```

Para importar pacotes java, utiliza-se a seguinte sintaxe: `(import [& lista-de-importações])`.

# Metadata
Dados sobre dados. Para ler os metadados, utiliza-se o form `(meta #'form)` onde `form` é o elemento que queremos ler os metadados. Para adicionar as informações sobre dados, utiliza-se o form `^metada form`.

# clojure.spec
`clojure.spec` é uma biblioteca para validações de dados. Serve para definir a estrutura e comportamento dos dados de forma declarativa. Permite validar os dados conforme especificações, transformar dados, gerar dados de testes, instrumentar funções para verificar a corretude dos testes. Ele permite fazer composições de espeficifações. O import do pacote é: `(require '[clojure.spec.alpha :as s])`. Todos os exemplos aqui usam esse import.

## s/def
`(def k spec-form)` dado uma `keyword` ou símbolo `k` e um `spec`, `spec-name`, predicado, ou `regex-op` faz o mapeamento de `k`para o `spec`. A nulidade `nil` remove a entrada para o registro `k`.

Serve pare definir uma estrutura.

```clojure
(s/def ::even even?)
(s/valid? ::even 4)
;; => true
(s/valid? ::even 5)  
;; => false

(s/def ::username string?)
(s/def ::usernames (s/coll-of ::username))
(s/valid? ::usernames ["alice" "bob" "carol"])
;; => true
(s/valid? ::usernames ["alice" 123])
;; => false

(def uuid-regex #"(?i)^[0-9A-F]{8}-[0-9A-F]{4}-4[0-9A-F]{3}-[89AB][0-9A-F]{3}-[0-9A-F]{12}$")
(s/def ::uuid-string
  (s/and string? #(re-matches uuid-regex %)))
(s/def ::uuid-string (s/and string? #(re-matches uuid-regex %)))

(s/valid? ::uuid-string "a06baf1e-3d77-49b4-8279-bceb5cd74ecd")
;; => true
(s/valid? ::uuid-string "invalid-uuid-string")
;; => false
```

## s/valid? 
`(s/valid? spec x)` o predicado retorna true se `x` for válido para o `spec` definido.
