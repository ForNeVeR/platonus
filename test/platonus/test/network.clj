(ns platonus.test.network
  (:require [platonus.network :as network])
  (:use [clojure.test]))

(deftest new-network-is-empty
  (let [network (network/create)]
    (is true (empty? network))))

(deftest simple-phrase-test
  (let [network (-> (network/create)
                    (network/add-phrase "ave omnissiah"))
        words   (get network "ave")]
    (is 3 (count network))
    (is 1 (count words))
    (is 1 (get words "omnissiah"))))
