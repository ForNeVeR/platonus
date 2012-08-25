(ns platonus.filesystem
  (:require [clojure.java.io :as io]
            [platonus.network :as network]))

(def regex #"^\[.*?\] \* (.*?)(?: \*|:) (.*?)$")

(defn- contains-phrase?
  [line]
  (re-matches regex line))

(defn- line-to-phrase
  [line]
  (nth (re-matches regex line) 2))

(defn- add-line
  [network line]
  (network/add-phrase network
                      (line-to-phrase line)))

(defn scan-file
  [initial-network file encoding]
  (println "Scanning file" file)
  (with-open [reader (io/reader file :encoding encoding)]
    (reduce add-line
            initial-network
            (filter contains-phrase? (line-seq reader)))))

(defn scan-directory
  [path encoding]
  (let [directory (io/file path)
        network   (network/create)]
    (reduce #(scan-file %1 %2 encoding)
            network
            (->> (file-seq directory)
                 (filter #(not (.isDirectory %)))
                 (sort-by #(.getName %))))))