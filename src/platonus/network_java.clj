(ns platonus.network-java
  (:require [clojure.string :as string]
  	        [platonus.network :as network])
  (:gen-class
     :name platonus.Network
     :init create-default
     :state "state"
     :methods [[addPhrase [String] Object]
               [generate [] String]
               [diff [platonus.Network] Double]]))

(defn -create-default
  []
  [[] (atom (network/create-default))])

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
    (network/diff network1 network2)))