(ns platonus.test.network
  (:require [platonus.network :as network])
  (:use [clojure.test]))

(deftest new-network-is-empty
  (let [network (network/create)]
    (is true (empty? network))))
