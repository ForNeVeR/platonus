(ns platonus.core
  (:require [platonus.network :as network])
  (:use [clojure.pprint :only (pprint)]))

(defn -main
  [& args]
  (pprint (-> (network/create)
              (network/update "hello world")
              (network/update "hello john")
              (network/update "john doe"))))
