(ns platonus.network
  (:require [clojure.string :as string]))

;;; Creation:
(defn create
  []
  {})

;;; Update:
(defn- split-replic
  [replic]
  (string/split replic #"\s"))

(defn- update
  [network prev-word next-word]
  (let [words (if (contains? network prev-word)
                (get network prev-word)
                {})
        count (+ (if (contains? words next-word)
                     (get words next-word)
                     0)
                 1)]
    (assoc network prev-word
      (assoc words next-word count))))

(defn add-phrase
  [initial-network replic]
  (let [words (concat [:phrase-begin]
                      (split-replic replic)
                      [:phrase-end])
        pairs (map vector words (drop 1 words))]
    (reduce (fn [network [prev-word next-word]]
              (update network prev-word next-word))
            initial-network
            pairs)))

;;; Generation:
(defn- random-key
  [map]
  (first (rand-nth (seq map))))

(defn- get-first-word
  [network]
  (random-key (get network :phrase-begin)))

(defn- get-next-word
  [network prev-word]
  (if (contains? network prev-word)
    (random-key (get network prev-word)) ;; TODO: Weighted random.
    :phrase-end))

(defn generate
  [network]
  (if (empty? network)
    []
    (take-while #(not (= % :phrase-end))
      (iterate (partial get-next-word network)
               (get-first-word network)))))
