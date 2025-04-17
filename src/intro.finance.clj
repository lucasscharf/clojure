(ns intro.finance
  (:use [clojure.repl :only [doc]])
  (:import (java.time LocalDateTime))
  (:require [clojure.spec.alpha :as s]))

(s/def :money/amount int?)
(s/def :currency/divisor int?)
(s/def :currency/code (and string? #{"USD" "BRL" "UKG"}))
(s/def :currency/sign (s/nilable string?))
(s/def :currency/desc (s/nilable string?))
(s/def :finance/currency (s/keys :req-un [:currency/divisor
                                          :currency/code]
                                 :opt-un [:currency/sign
                                          :currency/desc]))

(def currencies {:usd {:divisor 100  :code "USD"}
                 :brl {:divisor 100 :code "BRL"}})

(def default-currency (:brl currencies))

(defn make-money
  "takes an amount and a currency, creating a Money entity"
  ([] {:amount 0
       :currency default-currency})
  ([amount] {:amount amount
             :currency default-currency})
  ([amount currency] {:amount amount
                      :currency currency}))

(defn- same-currency?
  "true if the Currencies of the Money entities are the same"
  ([m1] true)
  ([m1 m2]
   (= (:currency m1) (:currency m2)))
  ([m1 m2 & monies]
   (every? true? (map #(same-currency? m1 %) (conj monies m2)))))

(defn- same-amount?
  "true if the amount of the Money entities are the same"
  ([m1] true)
  ([m1 m2] (zero? (.compareTo (:amount m1) (:amount m2))))
  ([m1 m2 & monies]
   (every? true? (map #(same-amount? m1 %) (conj monies m2)))))

(defn- ensure-same-currency!
  "throws an exception if the Currencies do not match, true otherwise"
  ([m1] true)
  ([m1 m2]
   (or (same-currency? m1 m2)
       (throw
        (ex-info "Currencies do not match."
                 {:m1 m1 :m2 m2}))))
  ([m1 m2 & monies]
   (every? true? (map #(ensure-same-currency! m1 %) (conj monies m2)))))

(defn =$
  "true if Money entities are equal"
  ([m1] true)
  ([m1 m2]
   (and (same-currency? m1 m2)
        (same-amount? m1 m2)))
  ([m1 m2 & monies]
   (every? true? (map #(=$ m1 %) (conj monies m2)))))

(defn +$
  "creates a Money object equal to the sum of the Money arguments"
  ([m1] m1)
  ([m1 m2]
   (ensure-same-currency! m1 m2)
   (make-money (+ (:amount m1) (:amount m2)) (:currency m1)))
  ([m1 m2 & monies]
   (apply ensure-same-currency! m1 m2 monies)
   (let [amounts (map :amount (conj monies m1 m2))
         new-amount (reduce + amounts)]
     (make-money new-amount (:currency m1)))))