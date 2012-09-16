(ns platonus.core
  (:require [clojure.java.io :as io]
            [platonus.audio :as audio]
            [platonus.network :as network]))

(defn- play-generated
  [filename]
  (let [network (audio/create-network-from-file filename)
        format  (:format network)
        bytes   (-> network
                    (network/generate)
                    (audio/concat-frames))]
    (audio/play-byte-seq bytes format)))

(defn -main
  [filename & args]
  (play-generated filename))
