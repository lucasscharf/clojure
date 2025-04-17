(import 'java.time.LocalTime)
(require '[clojure.java.io :as io])

(defn hello-world
  "provides an hour-appropriate greeting string"
  []
  (let [now (LocalTime/now)]
    (if (< (.getHour now) 12)
      (println "Good Morning")
      (println "Good Afternoon"))))

(defn greetings
  "provides an hour-appropriate greeting string"
  [hour]
  (if (< hour 12)
    "Good Morning"
    "Good Afternoon"))

(defn delete-the-file
  "print a message and remove the file."
  [path]
  (print "Deleting" path)
  (io/delete-file path))

(defn read-log-by-id
  "read log file logfile-<log-id>."
  [log-id]
  (slurp (str "logfile-" log-id)))

(defn logfile-name-by-id
  "Return the name of the logfile with the given id."
  [log-id]
  (str "logfile-" log-id))

(def mapa (array-map :first 1 :second 2))
(println mapa)
(println (:second mapa))

(def currencies {:usd {:divisor 100  :symbol "USD"}
                 :brl {:divisor 100 :symbol "BRL"}})

(def default-currency (:brl currencies))

(defn make-money
  "takes an amount and a currency, creating a Money entity"
  ([] {:amount 0
       :currency default-currency})
  ([amount] {:amount amount
             :currency default-currency})
  ([amount currency] {:amount amount
                     :currency currency})
)

(make-money)
(make-money 5)
(make-money 10 {:divisor 100  :symbol "EUR"})