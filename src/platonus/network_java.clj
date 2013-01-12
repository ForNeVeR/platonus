(ns platonus.network-java
  (:require [clojure.string :as string]
  	    [platonus.network :as network])
  (:gen-class
     :name platonus.Network
     :init create-default
     :state "state"
     :methods [[addPhrase [String] Object]
	             [generate [] String]]))

(defn -create-default
  []
  [[] (atom (network/create-default))])

(defn -addPhrase
  [this phrase]
  (swap! (.state this) network/add-phrase phrase))

(defn -generate
  [this]
  (string/join " " (network/generate @(.state this))))
