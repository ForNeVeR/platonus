(ns platonus.core
  (:require [clojure.java.io :as io]
            [platonus.network :as network]
            [platonus.filesystem :as filesystem])
  (:use [clojure.pprint :only (pprint)]))

(defn -main
  [mode path encoding & args]
  (let [network (cond
                  (= mode "-file") (filesystem/scan-file
                                     (network/create)
                                     path
                                     encoding)
                  (= mode "-directory") (filesystem/scan-directory
                                          path
                                          encoding))]
      (pprint (repeatedly 10
                (partial network/generate network)))))
