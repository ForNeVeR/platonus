(ns platonus.test.network
  (:require [platonus.network :as network])
  (:use [clojure.test]
        [clojure.pprint]))

(deftest new-network-is-empty
  (let [network (network/create)]
    (is (empty? network))))

(deftest simple-phrase-test
  (let [network (-> (network/create)
                    (network/add-phrase "ave omnissiah"))
        words   (get network ["ave"])]
    (pprint network)
    (is (= 1 (get words "omnissiah")))
    (is (= ["ave" "omnissiah"] (network/generate network)))))

(deftest advanced-phrase-test
  (let [network (-> (network/create)
                    (network/add-phrase "all hail the machine"))]
    (is (= 1 (get (get network ["all" "hail"]) "the")))))
