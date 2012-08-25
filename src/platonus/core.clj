(ns platonus.core
  (:require [platonus.network :as network])
  (:use [clojure.pprint :only (pprint)]))

(defn -main
  [& args]
  (let [network (-> (network/create)
                    (network/update "hello world")
                    (network/update "hello john")
                    (network/update "john doe"))]
    (pprint network)
    (pprint (network/generate network))
    (pprint (network/generate network))))
