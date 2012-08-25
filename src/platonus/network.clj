(ns platonus.network
  (:require [clojure.string :as string]))

;;; Parameters:
(def depth 1)

;;; Creation:
(defn create
  []
  {})

;;; Update:
(defn- split-replic
  [replic]
  (string/split replic #"\s"))

(defn- update
  [network key next-word]
  (let [words (if (contains? network key)
                (get network key)
                {})
        count (+ (if (contains? words next-word)
                   (get words next-word)
                   0)
                 1)]
    (assoc network key
      (assoc words next-word count))))

(defn add-phrase
  [initial-network replic]
  (let [words (concat [:phrase-begin]
                      (split-replic replic)
                      [:phrase-end])
        keys  (map vector words (drop 1 words))] ; TODO: Check depth variable
    (reduce (fn [network [prev-word next-word]]
              (update network [prev-word] next-word))
            initial-network
            keys)))

;;; Generation:
(defn- phrase-ends?
  [phrase]
  (= (last phrase) :phrase-end))

(defn- random-word
  [map]
  (rand-nth 
    (->> (seq map)
         (mapcat (fn [[word count]]
                   (repeat count word))))))

(defn- get-next-word
  [network prev-words]
  (if (contains? network prev-words)
    (random-word (get network prev-words))
    (if (> 1 (count prev-words))
      (recur network (drop 1 prev-words))
      :phrase-end)))

(defn- resume-phrase
  [network initial-prev-words]
  (let [prev-words (take-last depth initial-prev-words)
        next-word (get-next-word network prev-words)]
    (concat initial-prev-words [next-word])))

(defn generate
  [network]
  (if (empty? network)
    []
    (let [initial-phrase [:phrase-begin]]
      (->> (iterate (partial resume-phrase network)
                    initial-phrase)
           (take-while #(not (phrase-ends? %)))
           (last)       ; Taken last of phrase variants.
           (drop 1))))) ; Dropped the :phrase-begin keyword.
