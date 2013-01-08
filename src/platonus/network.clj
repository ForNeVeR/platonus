(ns platonus.network
  (:require [clojure.string :as string])
  (:gen-class
     :init create-default
     :state "state"
     :methods [[addPhrase [String] Object]
	             [doGenerate [] String]]))

;;; Creation:
(defn create
  [chain-length tokenizer]
  {:chain-length chain-length
   :tokenizer    tokenizer
   :network      {}})

(defn create-default
  []
  (create 2 #(string/split % #"\s")))

(defn -create-default
  []
  [[] (atom (create-default))])

;;; Update:
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

(defn- normalize
  [seq]
  (take-while #(not (= % :phrase-end)) seq))

(defn- add-subphrase
  [initial-network words]
  (let [prev-words (normalize (drop-last 1 words))
        next-word  (last words)]
    (if (empty? prev-words)
      initial-network
      (let [network (update initial-network prev-words next-word)]
        (add-subphrase network prev-words)))))

(defn add-phrase
  [{chain-length :chain-length
    tokenizer    :tokenizer
    network      :network
    :as initial-network}
   replic]
  (println initial-network)
  (assoc initial-network :network
    (let [words (concat [:phrase-begin]
                        (tokenizer replic)
                        [:phrase-end])
          keys  (apply map vector
                           (map #(concat (drop % words)          ; TODO: temporary solution? Maybe need
                                         (repeat % :phrase-end)) ; specialized map function. Especially
                                (range 0 (+ chain-length 1))))]  ; suspicious is the repeat part.
      (reduce add-subphrase network keys))))

(defn -addPhrase
  [this phrase]
  (swap! (.state this) add-phrase phrase))

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
  [network chain-length initial-prev-words]
  (let [prev-words (take-last chain-length initial-prev-words)
        next-word (get-next-word network prev-words)]
    (concat initial-prev-words [next-word])))

(defn generate
  [{network      :network
    chain-length :chain-length}]
  (if (empty? network)
    []
    (let [initial-phrase [:phrase-begin]]
      (->> (iterate (partial resume-phrase network chain-length)
                    initial-phrase)
           (take-while #(not (phrase-ends? %)))
           (last)       ; Taken last of phrase variants.
           (drop 1))))) ; Dropped the :phrase-begin keyword.

(defn -doGenerate
  [this]
  (string/join " " (generate @(.state this))))