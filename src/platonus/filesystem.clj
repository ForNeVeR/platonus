(ns platonus.filesystem
  (:require [clojure.java.io :as io]
            [platonus.network :as network])
  (:gen-class
     :name    platonus.Filesystem
     :methods [^{:static true} [scanDirectory [String String String] platonus.Network]]))

(def regex #"^\[.*?\] \* (.*?)(?: \*|:) (.*?)$")

(defn- contains-phrase?
  [line]
  (re-matches regex line))

(defn- get-nickname
  [line]
  (nth (re-matches regex line) 1))

(defn- line-to-phrase
  [line]
  (nth (re-matches regex line) 2))

(defn- add-line
  [network line]
  (network/add-phrase network
                      (line-to-phrase line)))

(defn scan-file
  [network nickname file encoding]
  (println "Scanning file" file)
  (with-open [reader (io/reader file :encoding encoding)]
    (reduce add-line
            network
            (->> (line-seq reader)
                 (filter contains-phrase?)
                 (filter #(= (get-nickname %) nickname))))))

(defn scan-directory
  [nickname path encoding]
  (let [directory (io/file path)
        network   (network/create-default)]
    (reduce #(scan-file %1 nickname %2 encoding)
            network
            (->> (file-seq directory)
                 (filter #(not (.isDirectory %)))
                 (sort-by #(.getName %))))))

(defn -scanDirectory
  [nickname path encoding]
  (scan-directory nickname path encoding))
