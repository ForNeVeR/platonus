(ns platonus.network-java
  (:require [clojure.string :as string]
  	        [platonus.network :as network])
  (:gen-class
    :name platonus.Network
    :init constructor
    :constructors {[] []
                   [Integer] []}
    :state "state"
    :methods [[addPhrase [String] Object]
              [generate [] String]
              [diff [Object] Double]]))

(defn -constructor
  ([] [[] (atom (network/create-default))])
  ([^Integer chain-length] [[] (atom (network/create chain-length network/parse-phrase))]))

(defn -create-default
  []
  )

(defn -addPhrase
  [this phrase]
  (swap! (.state this) network/add-phrase phrase))

(defn -generate
  [this]
  (string/join " " (network/generate @(.state this))))

(defn -diff
  [this other]
  (let [network1 @(.state this)
        network2 @(.state other)]
    (double (network/diff network1 network2))))