(require '[clojure.string :as str])
(require '[clojure.spec.alpha :as s])
(import '(java.io File))

(println "Hello world!")
(println "What is this:" (+ 1 2))
(println (concat [1,2] [3,4] [23] [666]))
(println (/ 22 7))
(println (/ 22.0 7))
;(println (. class (/ 22.0 7)))
;(println (. class (/ 22 7)))
(println)
;(println (class 22))
;(println (class 22.0))
;(println (class 22N))
;(println (class 22M))
; O maior valor de long é 9223372036854775807
(println (class 9223372036854775808))

(println "Multiline Java
            Com algumas 
            Limitações")

(println (.toUpperCase "lower case"))


(defrecord Person [name age])
(def john (->Person "John Doe" 30))

(println john)
(println (:name john)) ; acesso via palavra chave
(println (.age john)) ; acesso via função

(defn namorar [person-1 person-2 person-3 & outros-peguetes]
  (println person-1 "," person-2 "e" person-3 "sairam como trisal." (count outros-peguetes) " ficaram de fora."))

(namorar "Dona Flor" "Vadinho" "Deodoro" "Peguete 1" "Peguete 2")
(namorar "Dona Flor" "Vadinho" "Deodoro" "Peguete 1")
(namorar "Dona Flor" "Vadinho" "Deodoro")


(def bar 10)
(println bar)


(defn cantos-quadrado [baixo esquerda tamanho]
  (let [cima (+ baixo tamanho) direita (+ esquerda tamanho)]
    [[baixo esquerda] [cima esquerda] [cima direita] [baixo direita]]))

(println (cantos-quadrado 1,2,5))

(def destrutor (let [[x y :as elementos] [2 4 6 8]]
    (str "X: " x " Y: " y " Total elementos: " (count elementos))))
(println destrutor)


(println (str/join ["A" "B" "C" "..."]))
(println (str/join "_" ["A" "B" "C" "..."]))
(println (str "A" "B" "C" "..." "D"))


(defn parentetizar [s palavra]
  (if (empty? palavra)
    s
    (recur (str "(" s (first palavra) ") ") (rest palavra))))

(defn separar [s]
  (let [split (str/split s #" ")]
    (parentetizar "" split)))

(binding [*in* (new java.io.InputStreamReader System/in "UTF-8")
          *out* (new java.io.OutputStreamWriter System/out "UTF-8")]
  (println (separar "Clojure é uma linguagem que usa muitos parênteses")))

(println (for [[k v] {:a 1 :b 2}] [k (* v v)]) ([:a 1] [:b 4]))
(println (for [x (range 5) :when (odd? x)] (* x x)))

(println
 (for [a (range 2) b (range 2), c (range 2)]
   [a b c]))


(println (first "Hello"))
(println (rest "Hello"))
(println (cons \H "ello"))


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
(s/valid? ::uuid-string "invalid-uuid-string")   

(re-seq #"\w+" "the quick brown fox") 

(map #(.toUpperCase %) (re-seq #"\w+" "the quick brown fox"))



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

(apply + [1 2 3 4 5])

(apply + 10 [1 2 3])	


(seq (.listFiles (File. ".")))