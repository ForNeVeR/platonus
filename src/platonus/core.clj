(ns platonus.core
  (:require [clojure.java.io :as io]
            [platonus.network :as network]
            [platonus.filesystem :as filesystem]))

(defn- print-phrase
  [phrase]
  (apply println phrase))

(defn -main
  [nickname mode path encoding & args]
  (let [network (cond
                  (= mode "-file") (filesystem/scan-file
                                     nickname
                                     (network/create)
                                     path
                                     encoding)
                  (= mode "-directory") (filesystem/scan-directory
                                          nickname
                                          path
                                          encoding))]
    (println "Network size:" (count network))
    (doseq [phrase (repeatedly 20
                     (partial network/generate network))]
      (print-phrase phrase))))
