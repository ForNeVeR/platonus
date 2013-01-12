(ns platonus.network-java
  (:require [clojure.string :as string]
  	    [platonus.network :as network])
  (:gen-class
     :name platonus.Network
     :init create-default
     :state "state"
     :methods [[addPhrase [String] Object]
	             [doGenerate [] String]]))

(defn -create-default
  []
  [[] (atom (network/create-default))])

(defn -addPhrase
  [this phrase]
  (swap! (.state this) network/add-phrase phrase))

(defn -doGenerate
  [this]
  (string/join " " (network/generate @(.state this))))
