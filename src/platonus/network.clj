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
    (println "key: " key)
    (assoc network key
      (assoc words word count))))
