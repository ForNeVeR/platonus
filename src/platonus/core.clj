(ns platonus.core
  (:require [clojure.java.io :as io]
            [platonus.network :as network]
            [platonus.filesystem :as filesystem])
  (:use [clojure.pprint :only (pprint)]))

(defn -main
  [filename encoding & args]
  (let [network (filesystem/scan-file (network/create)
                                      filename
                                      encoding)]
    (pprint network)
    (pprint (repeatedly 10
              (partial network/generate network)))))
