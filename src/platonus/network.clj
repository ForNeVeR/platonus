(ns platonus.network
  (:require [clojure.string :as string]))

(defn- split-replic
  [replic]
  (string/split replic #"\s"))

(defn create
  []
  {})

(defn update
  [network replic]
  (let [[key word] (split-replic replic)
        words (if (contains? network key)
                (get network key)
                {})
        count (+ (if (contains? words word)
                     (get words word)
                     0)
                 1)]
    (assoc network key
      (assoc words word count))))

(defn- random-key
  [map]
  (first (rand-nth (seq map))))

(defn get-first-word
  [network]
  (random-key network))

(def phrase-end)

(defn get-next-word
  [network prev-word]
  (if (contains? network prev-word)
    (random-key (get network prev-word)) ;; TODO: Weighted random.
    phrase-end))

(defn generate
  [network]
  (if (empty? network)
    []
    (take-while #(not (= % phrase-end))
      (iterate (partial get-next-word network)
               (get-first-word network)))))
