(ns platonus.core
  (:require [platonus.network :as network])
  (:use [clojure.pprint :only (pprint)]))

(defn -main
  [& args]
  (let [network (-> (network/create)
                    (network/add-phrase "hello world")
                    (network/add-phrase "hello john")
                    (network/add-phrase "john doe"))]
    (pprint network)
    (pprint (repeatedly 10
              (partial network/generate network)))))
