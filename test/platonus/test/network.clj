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
  (let [network (-> (network/create 2 network/parse-phrase)
                    (network/add-phrase "all hail the machine")
                    (:network))]
    (is (= 1 (get (get network ["all" "hail"]) "the")))))

(deftest network-structure-test
  (let [network (-> (network/create 2 network/parse-phrase)
                    (network/add-phrase "machine spirit")
                    (:network))]
    (is (= network {[:phrase-begin] {"machine" 1}
                    ["machine"] {"spirit" 1}
                    ["spirit"] {:phrase-end 1}
                    [:phrase-begin "machine"] {"spirit" 1}
                    ["machine" "spirit"] {:phrase-end 1}}))))

(deftest network-normalization-test
  (let [network (-> (network/create 1 network/parse-phrase)
                    (network/add-phrase "ave emperor")
                    (network/add-phrase "ave omnissiah"))
        normalized (network/normalized network)]
    (is (= normalized {[:phrase-begin] {"ave" 1}
                       ["ave"] {"emperor" 1/2
                                "omnissiah" 1/2}
                       ["emperor"] {:phrase-end 1}
                       ["omnissiah"] {:phrase-end 1}}))))

(deftest network-diff-test
  (let [network1 (-> (network/create-default)
                     (network/add-phrase "a b c d"))
        network2 (-> (network/create-default)
                     (network/add-phrase "a b c"))
        network3 (-> (network/create-default)
                     (network/add-phrase "a b d"))]
    (is (= 3/9 (network/diff network1 network2)))
    (is (= 4/8 (network/diff network2 network3)))))
