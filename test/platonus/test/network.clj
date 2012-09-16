(ns platonus.test.network
  (:require [platonus.network :as network])
  (:use [clojure.test]))

(deftest new-network-is-empty
  (let [network (-> (network/create-default)
                    (:network))]
    (is (empty? network))))

(deftest simple-phrase-test
  (let [network-obj (-> (network/create-default)
                        (network/add-phrase "ave omnissiah"))
        network     (:network network-obj)
        words       (get network ["ave"])]
    (is (= 1 (get words "omnissiah")))
    (is (= ["ave" "omnissiah"] (network/generate network-obj)))))

(deftest advanced-phrase-test
  (let [network (-> (network/create-default)
                    (network/add-phrase "all hail the machine")
                    (:network))]
    (is (= 1 (get (get network ["all" "hail"]) "the")))))

(deftest network-structure-test
  (let [network (-> (network/create-default)
                    (network/add-phrase "machine spirit")
                    (:network))]
    (is (= network {[:phrase-begin] {"machine" 1}
                    ["machine"] {"spirit" 1}
                    ["spirit"] {:phrase-end 1}
                    [:phrase-begin "machine"] {"spirit" 1}
                    ["machine" "spirit"] {:phrase-end 1}}))))
