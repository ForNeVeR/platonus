(ns platonus.network
  (:require [clojure.string :as string]
            [clojure.contrib.math :as math]))

;;; Phrase parsing
(defn parse-phrase
  [phrase]
  (doall
    (->> (string/split phrase #"\s")
         (map #(.intern %)))))

;;; Creation:
(defn create
  [chain-length tokenizer]
  {:chain-length chain-length
   :tokenizer    tokenizer
   :network      {}})

(defn create-default
  []
  (create 1 parse-phrase))

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
  (assoc initial-network :network
    (let [words (concat [:phrase-begin]
                        (tokenizer replic)
                        [:phrase-end])
          keys  (apply map vector
                           (map #(concat (drop % words)          ; TODO: temporary solution? Maybe need
                                         (repeat % :phrase-end)) ; specialized map function. Especially
                                (range 0 (+ chain-length 1))))]  ; suspicious is the repeat part.
      (reduce add-subphrase network keys))))

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

;;; Normalization:
(defn- network-size
  [network]
  (->> network
    (vals)
    (mapcat vals)
    (reduce +)))

(defn- normalize-branch
  [size result [key branch]]
  (assoc result
    key (reduce-kv (fn [r k v] (assoc r k (/ v size))) {} branch)))

(defn normalized
  [{network :network}]
  (let [size (network-size network)]
    (reduce
      (partial normalize-branch size)
      {}
      network)))

;;; Difference:
(defn- sum-keys
  [network1 network2]
  (-> (keys network1)
      (concat (keys network2))
      (set)))

(def map-diff)

(defn- calculate-diff-by-key
  [network1 network2 key]
  (let [value1 (get network1 key)
        value2 (get network2 key)]
    (cond
      (or (map? value1)
          (map? value2)) (map-diff value1 value2)
      (and (nil? value1)
           (nil? value2)) 0
      (nil? value1)       value2
      (nil? value2)       value1
      :otherwise          (math/abs (- value2 value1)))))

(defn- map-diff
  [map1 map2]
  (let [keys (sum-keys map1 map2)]
    (reduce
      (fn [acc key]
        (+ acc (calculate-diff-by-key map1 map2 key)))
      0
      keys)))

(defn diff
  [network1 network2]
  (let [n1 (normalized network1)
        n2 (normalized network2)
        size 2]
    (/ (map-diff n1 n2)
       size)))
